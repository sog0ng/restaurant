package com.example.reservation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.reservation.item.ListViewAdapter;
import com.google.android.material.navigation.NavigationView;

public class OwnerHomeActivity extends AppCompatActivity{
    private AppBarConfiguration mAppBarConfiguration;
    //private ListViewAdapter adapter;
    private long backKeyPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owner_home);

        ListView listview = (ListView) findViewById(R.id.ListView);
        listview.setVisibility(View.GONE);

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
                R.id.nav_modification, R.id.nav_statistics)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
/*
        // 임시 데이터
        ListView listview;
        adapter = new ListViewAdapter(this);

        listview = (ListView)findViewById(R.id.ListView);
        listview.setAdapter(adapter);

        // adapter.addItem 으로 db 에 있는 예약 내역 저장
        // 닉네임 년 월 일 시간 분 도착시간 인원
        adapter.addItem("닉네임", 2019, 11, 9, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 10, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 11, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 12, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 13, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 14, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 15, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 16, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 17, 5, 7, "도착시간", 3);

 */
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
                //adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"새로고침 되었습니다.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //뒤로가기 버튼 disable
    @Override public void onBackPressed() {
        //super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(System.currentTimeMillis() > backKeyPressTime + 2000){
            backKeyPressTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }
        //2번째 백버튼 클릭 (종료)
        else {
            finishAffinity();
        }
    }
}
