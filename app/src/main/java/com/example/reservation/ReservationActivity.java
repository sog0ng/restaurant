package com.example.reservation;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReservationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef2 = database.getReference("User_info/");

        Intent intent = getIntent();

        final TextView restaurant_name = (TextView) findViewById(R.id.restaurant_name);
        restaurant_name.setText(intent.getStringExtra("restaurant_name"));

        // 임시 데이터,,, db 연동하여 데이터 가져오도록 코드 작성...
        //String[] menu = {"삼겹살", "목살", "항정살"};
        //String[] price = {"12000", "10000", "13000"};

        final ArrayList<String> menu = new ArrayList<>();
        final ArrayList<String> price = new ArrayList<>();

        menu.add("삼겹살");
        menu.add("목살");
        menu.add("항정살");
        price.add("12000원");
        price.add("10000원");
        price.add("13000원");

        //ArrayAdapter<String> adapter;
        ListView listview = (ListView) findViewById(R.id.ListView);


        TableLayout t1 = (TableLayout) findViewById(R.id.table);
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < menu.size(); i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(lp);

            TextView tv1 = new TextView(this);
            tv1.setLayoutParams(lp);
            tv1.setText(menu.get(i));

            TextView tv2 = new TextView(this);
            tv2.setLayoutParams(lp);
            tv2.setText(price.get(i));

            CheckBox check = new CheckBox (this);
            check.setLayoutParams(lp);
            //number.setInputType(InputType.TYPE_CLASS_NUMBER);

            tr.addView(tv1);
            tr.addView(tv2);
            tr.addView(check);
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
