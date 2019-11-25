package com.example.reservation;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SetupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup);

        Button button = (Button) findViewById(R.id.button);
        Button change_restaurant = (Button) findViewById(R.id.change_restaurant);
        Button change_time = (Button) findViewById(R.id.change_time);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUp();
            }
        });

        change_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRestaurant();
            }
        });

        change_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTime();
            }
        });

    }

    private void setUp(){

    }
    private void changeRestaurant(){

    }
    private void changeTime(){

    }

    private void show() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        alert.setTitle("메뉴 추가");
        alert.setMessage("메뉴와 가격을 입력하세요.");

        EditText menu = new EditText(this);
        menu.setHint("menu");
        EditText price = new EditText(this);
        price.setHint("price");

        layout.addView(menu);
        layout.addView(price);

        alert.setView(layout);
        alert.setPositiveButton("추가", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // db에 menu, price 추가
            }
        });

        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }
}
