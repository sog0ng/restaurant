package com.example.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.reservation.item.ListViewAdapter;
import com.example.reservation.item.ListViewItem;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomerHomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    // 가게 리스트 임시 데이터,,, db로부터 가져오는 코드 작성...
    //String[] list = {"리스트1", "리스트2", "리스트3", "리스트4", "리스트5", "리스트6", "리스트7", "리스트8", "리스트9", "리스트10", "리스트11", "리스트12", "리스트13", "리스트14", "리스트15", "리스트16"};
    private ListViewAdapter adapter;

    final ArrayList<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_home);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef2 = database.getReference("User_info/");

        // 네비게이션
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Intent intent = getIntent();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.menuIdTextView);
        navUsername.setText("안녕하세요, " + intent.getExtras().getString("id") + "님!");

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_info, R.id.nav_query,
                R.id.nav_modification, R.id.nav_manage, R.id.nav_statistics)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // 리스트 검색 코드 작성

        //ArrayAdapter<String> adapter;
        ListView listview = (ListView) findViewById(R.id.ListView);
        adapter = new ListViewAdapter(this);
        listview.setAdapter(adapter);

//리스트가 뜨는데 돋보기 눌러야뜸
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                update_list(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){//리스트 클릭시
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){ //리스트뷰 클릭시 해당하는 레스토랑 정보로 감
                Intent reservation2 = new Intent(getApplicationContext(), Reservation2Activity.class);
                reservation2.putExtra("restaurant_name", list.get(position));
                startActivity(reservation2);
            }
        });

    }

    private void update_list(@NonNull DataSnapshot dataSnapshot) {
        for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
            String key=childSnapshot.getKey();
            User user_each=childSnapshot.getValue(User.class);
            // list.add(user_each.getRestaurant_name());
            if(!user_each.getRestaurant_name().equals("null")) { //가게 이름이 null이 아니면 list에 추가
                Log.i("가게이름:", user_each.getRestaurant_name());
                Log.i("이름", key);
                list.add(user_each.getRestaurant_name());

            }else {
                continue;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.logout:
                btn_logout();
                return true;
            case R.id.refresh:
                //리스트 업데이트 함수
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"새로고침 되었습니다.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void btn_logout() {
        new AlertDialog.Builder(this)
                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }

    //뒤로가기 버튼 disable
    @Override public void onBackPressed() {
        //super.onBackPressed();
    }

}