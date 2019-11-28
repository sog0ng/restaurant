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
import java.util.Calendar;
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

        final String str_nickname=getIntent().getStringExtra("nickname");
        final String str_restaurantName = getIntent().getStringExtra("restaurant_name");

        final int year = getIntent().getIntExtra("year",0);
        final int month = getIntent().getIntExtra("month",0);
        final int day = getIntent().getIntExtra("day",0);
        final int hour = getIntent().getIntExtra("hour",0);
        final int minute = getIntent().getIntExtra("minute",0);

        final int cover = getIntent().getIntExtra("covers", 0);


        final String is_accepted = getIntent().getStringExtra("is_accepted");//예약 승인여부
        final String is_confirm = getIntent().getStringExtra("is_confirm");//방문 확인ㄴ 여부

        int iDday = countDday(year, month, day);

        TextView r_nickname = (TextView) findViewById(R.id.r_nickname);
        TextView restaurantName = (TextView) findViewById(R.id.restaurant_name);

        TextView r_date = (TextView) findViewById(R.id.r_date);
        TextView r_time = (TextView) findViewById(R.id.r_time);
        TextView covers = (TextView) findViewById(R.id.covers);

        TextView status = (TextView) findViewById(R.id.status);
        Button submit = (Button) findViewById(R.id.submit);
        Button cancel = (Button) findViewById(R.id.cancel);

        restaurantName.setText(str_restaurantName);
        r_nickname.setText(str_nickname);

        r_date.setText(year + "년" + month + "월" + day + "일");
        r_time.setText(hour + "시 " + minute + "분");
        covers.setText(cover + "명");

        if (iDday < 0) {
            //과거내역인 경우
            if (is_accepted.equals("1") && is_confirm.equals("null")) {
                status.setText("방문 확인 중");
            } else if (is_accepted.equals("1") && is_confirm.equals("1")) {
                status.setText("방문");
            } else if (is_accepted.equals("1") && is_confirm.equals("0")) {
                status.setText("미방문");
            } else if(is_accepted.equals("0")){
                status.setText("예약 거절");
            }else if(is_accepted.equals("-1")){
                status.setText("예약 취소");
            }
        } else {
            //미래에 대한 것
            if (is_accepted.equals("null")) {
                status.setText("예약 처리 중");
            } else if (is_accepted.equals("1")) {
                status.setText("예약 승인");
            } else if (is_accepted.equals("0")) {
                status.setText("예약 거절");
            }else if (is_accepted.equals("-1")) {
                status.setText("예약 취소");
            }
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

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //need to implement
                //데이터베이스에서 해당 내용을 삭제하기보다는 상태를 취소로 하는게 좋을듯
            }
        });

    }

    public int countDday(int myear, int mmonth, int mday) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar todaCal = Calendar.getInstance();
        Calendar ddayCal = Calendar.getInstance();

        mmonth -= 1;
        ddayCal.set(myear, mmonth, mday);

        long today = todaCal.getTimeInMillis() / 86400000;
        long dday = ddayCal.getTimeInMillis() / 86400000;

        long count = dday - today;

        return (int) count;
    }


}
