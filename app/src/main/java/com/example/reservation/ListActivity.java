package com.example.reservation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {

    // 가게 리스트 임시 데이터,,, db로부터 가져오는 코드 작성...
    String[] list = {"리스트1", "리스트2", "리스트3", "리스트4", "리스트5", "리스트6", "리스트7", "리스트8", "리스트9", "리스트10", "리스트11", "리스트12", "리스트13", "리스트14", "리스트15", "리스트16"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list);

        // 리스트 검색 코드 작성

        ArrayAdapter<String> adapter;

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        ListView listview = (ListView) findViewById(R.id.ListView);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Intent reservation = new Intent(getApplicationContext(), ReservationActivity.class);
                reservation.putExtra("restaurant_name", list[position]);
                startActivity(reservation);
            }
        });

    }
}
