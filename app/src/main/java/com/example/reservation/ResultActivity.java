package com.example.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Reservation");
        final String reservation_key = myRef.push().getKey();//고유키

        int year = getIntent().getIntExtra("year",0);
        int month = getIntent().getIntExtra("month",0);
        int day = getIntent().getIntExtra("day",0);
        final String str_covers = getIntent().getStringExtra("covers");
        final String str_restaurantName = getIntent().getStringExtra("restaurant_name");
        final String str_nickname=getIntent().getStringExtra("nickname");

        TextView r_date = (TextView) findViewById(R.id.r_date);
        TextView covers = (TextView) findViewById(R.id.covers);
        TextView restaurantName = (TextView) findViewById(R.id.restaurant_name);
        TextView r_nickname = (TextView) findViewById(R.id.r_nickname);
        Button submit = (Button) findViewById(R.id.submit);

        restaurantName.setText(str_restaurantName);
        r_date.setText(year+"년"+month+"월"+day+"일");
        covers.setText(str_covers+"명");
        r_nickname.setText(str_nickname);



        submit.setOnClickListener(new View.OnClickListener() {
            EditText covers;
            @Override
            public void onClick(View v) {
                Reservation reservation = new Reservation();
                reservation.setRestaurant_name(str_restaurantName);
                reservation.setConfirm(0);
                reservation.setIs_accepted(0);
                reservation.setNickname(str_nickname);
                reservation.setCovers(Integer.parseInt(str_covers));
               // reservation.setR_date();//날짜,시간 추가해야함
                reservation.setSend_score(0);

                myRef.child(reservation_key).setValue(reservation);
                Intent home = new Intent(getApplicationContext(), CustomerHomeActivity.class);
                startActivity(home);
            }



        });

    }


}
