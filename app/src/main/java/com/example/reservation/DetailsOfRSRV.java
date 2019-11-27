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

        TextView r_date = (TextView) findViewById(R.id.r_date);
        TextView covers = (TextView) findViewById(R.id.covers);
        TextView restaurantName = (TextView) findViewById(R.id.restaurant_name);
        TextView r_nickname = (TextView) findViewById(R.id.r_nickname);
        TextView r_time = (TextView) findViewById(R.id.r_time);

        Button submit = (Button) findViewById(R.id.submit);

        restaurantName.setText(str_restaurantName);
        r_time.setText(hour + "시 " + minute + "분");
        r_date.setText(year + "년" + month + "월" + day + "일");
        covers.setText(cover + "명");
        r_nickname.setText(str_nickname);

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



    }


}
