package com.example.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.example.reservation.ui.home.CustomerHomeFragment;
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
    // private ListViewAdapter adapter;
    private SearchView editsearch;
    private InputMethodManager imm;

    private long backKeyPressTime;

    final ArrayList<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_home);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef2 = database.getReference("User_info/");

        // 네비게이션
        Toolbar toolbar = findViewById(R.id.customer_toolbar);
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
                R.id.nav_customer_home, R.id.nav_info, R.id.nav_query, R.id.nav_help)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(System.currentTimeMillis() > backKeyPressTime + 2000){
            backKeyPressTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }
        //2번째 백버튼 클릭 (종료)
        else {
            finishAffinity();
        }
    }
}