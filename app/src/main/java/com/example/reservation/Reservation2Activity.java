package com.example.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class Reservation2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation2);

        Intent intent = getIntent();

        final TextView restaurant_name = (TextView) findViewById(R.id.restaurant_name);
        restaurant_name.setText(intent.getStringExtra("restaurant_name"));

        Button next = (Button) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent(getApplicationContext(), ResultActivity.class);
                result.putExtra("restaurant_name", restaurant_name.getText());
                // 메뉴, 수량, 시간, 인원수 전달해주기.
                startActivity(result);
            }
        });
    }


}
