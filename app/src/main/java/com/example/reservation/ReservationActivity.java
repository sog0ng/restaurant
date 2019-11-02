package com.example.reservation;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ReservationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);

        Intent intent = getIntent();

        final TextView restaurant_name = (TextView) findViewById(R.id.restaurant_name);
        restaurant_name.setText(intent.getStringExtra("restaurant_name"));

        // 임시 데이터,,, db 연동하여 데이터 가져오도록 코드 작성...
        String[] menu = {"삼겹살", "목살", "항정살"};
        String[] price = {"12000", "10000", "13000"};

        TableLayout t1 = (TableLayout)findViewById(R.id.table);
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            for(int i = 0; i < menu.length;i++) {
                TableRow tr = new TableRow(this);
                tr.setLayoutParams(lp);

                TextView tv1 = new TextView(this);
                tv1.setLayoutParams(lp);
                tv1.setText(menu[i]);

                TextView tv2 = new TextView(this);
                tv2.setLayoutParams(lp);
                tv2.setText(price[i]);

                EditText number = new EditText(this);
                number.setLayoutParams(lp);
                number.setInputType(InputType.TYPE_CLASS_NUMBER);

                tr.addView(tv1);
                tr.addView(tv2);
                tr.addView(number);
                t1.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            }


            Button next = (Button) findViewById(R.id.next);

            next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reservation2 = new Intent(getApplicationContext(), Reservation2Activity.class);
                reservation2.putExtra("restaurant_name", restaurant_name.getText());
                // 메뉴, 수량도 전달해주기.
                startActivity(reservation2);
            }
        });
    }
}
