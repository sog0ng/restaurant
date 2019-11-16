package com.example.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    Button submit;
    CheckBox owner;
    EditText restaurantName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        owner = (CheckBox) findViewById(R.id.owner);
        restaurantName = (EditText) findViewById(R.id.restaurant_name);

        restaurantName.setVisibility(View.GONE);
        owner.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(owner.isChecked()) {
                    restaurantName.setVisibility(View.VISIBLE);
                } else {
                    restaurantName.setVisibility(View.GONE);
                }
            }
        });




        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 데이터 db에 전송하는 코드 작성...
                Intent main = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(main);
            }
        });
    }
}