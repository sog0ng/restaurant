package com.example.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button login;
    Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signUp);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customer = new Intent(getApplicationContext(), CustomerHomeActivity.class);
                // id, pw 맞는지 검사하는 코드 작성...
                startActivity(customer);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(sign);
            }
        });

        // 테스트용
        Intent owner = new Intent(getApplicationContext(), OwnerHomeActivity.class);
        startActivity(owner);
    }
}
