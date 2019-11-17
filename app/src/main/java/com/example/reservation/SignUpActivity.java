package com.example.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    Button submit;
    CheckBox owner,customer;
    EditText restaurant_name,id1, password, phone_num;
    String s_restaurant_name,s_id1, s_password, s_phone_num, c_id1,c_password, c_phone_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Owner_info/");
        final DatabaseReference myRef1 = database.getReference("customer_info/");

        final String owner_info_key =  myRef.push().getKey();//고유키
        final String customer_info_key =  myRef1.push().getKey();//고유키

        submit = (Button) findViewById(R.id.submit);
        owner = (CheckBox) findViewById(R.id.owner);
        customer = (CheckBox) findViewById(R.id.customer);
        id1=(EditText) findViewById(R.id.id1);
        password=(EditText) findViewById(R.id.password);
        phone_num=(EditText) findViewById(R.id.phone_num);
        restaurant_name = (EditText) findViewById(R.id.restaurant_name);

        owner.setOnClickListener(new CheckBox.OnClickListener() {//owner체크박스 클릭시
            @Override
            public void onClick(View v) {
                if (owner.isChecked()) {//오너클릭시
                    restaurant_name.setVisibility(View.VISIBLE);//가게 이름 나타나게

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Owner_info owner_info = new Owner_info();
                            // 데이터 db에 전송하는 코드 작성...
                            GetDataFromEditText();
                            owner_info.setRestaurant_name(s_restaurant_name);
                            owner_info.setId1(s_id1);
                            owner_info.setPhone_num(s_phone_num);
                            owner_info.setPassword(s_password);
                            myRef.child(owner_info_key).setValue(owner_info);
                            Intent main = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(main);
                        }
                    });
                }

            else{
                restaurant_name.setVisibility(View.GONE);
            }
        }
        });

        customer.setOnClickListener(new CheckBox.OnClickListener() {//customer체크박스 클릭시
            @Override
            public void onClick(View v) {
                if (customer.isChecked()) {//오너클릭시
                    //가게 이름 나타나게
                    restaurant_name.setVisibility(View.GONE);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Customer_info customer_info = new Customer_info();
                            // 데이터 db에 전송하는 코드 작성...
                            GetDataFromEditText();
                            customer_info.setC_id(s_id1);
                            customer_info.setC_phone_num(s_phone_num);
                            customer_info.setC_password(s_password);
                            myRef1.child(customer_info_key).setValue(customer_info);
                            Intent main = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(main);
                        }
                    });
                }

                else{
                    restaurant_name.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    public void GetDataFromEditText(){
        //owner_info
        s_restaurant_name = restaurant_name.getText().toString().trim();
        s_id1 = id1.getText().toString().trim();
        s_password = password.getText().toString().trim();
        s_phone_num = phone_num.getText().toString().trim();
        //customer_info
        c_id1 = id1.getText().toString().trim();
        c_password = password.getText().toString().trim();
        c_phone_num = phone_num.getText().toString().trim();

    }

}