package com.example.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class DetailsOfRSRV extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_of_reservation);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Reservation");

        final int year = getIntent().getIntExtra("year",0);
        final int month = getIntent().getIntExtra("month",0);
        final int day = getIntent().getIntExtra("day",0);
        final int cover = getIntent().getIntExtra("covers", 0);
        final String str_restaurantName = getIntent().getStringExtra("restaurant_name");
        final String str_nickname=getIntent().getStringExtra("nickname");
        final int hour = getIntent().getIntExtra("hour",0);
        final int minute = getIntent().getIntExtra("minute",0);
        final String is_accepted = getIntent().getStringExtra("is_accepted");
        final String is_confirm = getIntent().getStringExtra("is_confirm");

        TextView r_date = (TextView) findViewById(R.id.r_date);
        TextView covers = (TextView) findViewById(R.id.covers);
        TextView restaurantName = (TextView) findViewById(R.id.restaurant_name);
        TextView r_nickname = (TextView) findViewById(R.id.r_nickname);
        TextView r_time = (TextView) findViewById(R.id.r_time);

        TextView status = (TextView) findViewById(R.id.status);

        Button submit = (Button) findViewById(R.id.submit);

        restaurantName.setText(str_restaurantName);
        r_time.setText(hour + "시 " + minute + "분");
        r_date.setText(year + "년" + month + "월" + day + "일");
        covers.setText(cover + "명");
        r_nickname.setText(str_nickname);

        //신청한 예약에 대해 어떤 상태인지
        if(is_accepted==null){
            status.setText("예약 처리 중");
        }else if(is_accepted=="1"){
            status.setText("예약 승인");
        }else{
            status.setText("예약 거절");
        }

        //방문했는지 확인해야
        //다시 is_confirm 은 기본값 0이였다가 -> 예약 한 날짜가 지나면 null로 바뀌는것으로 되어야함
        if(is_confirm==null){
            status.setText("방문 확인 중");
        }else if(is_confirm=="1"){
            status.setText("방문");
        }else{
            status.setText("미방문");
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

        Button modify = (Button) findViewById(R.id.modify);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //need to implement
            }
        });

//취소 버튼 만들어야함

    }


}
