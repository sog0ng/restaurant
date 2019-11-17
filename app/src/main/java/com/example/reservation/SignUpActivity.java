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
        final DatabaseReference myRef = database.getReference("User_info");
        final String user_info_key =  myRef.push().getKey();//고유키


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
                            User user_info = new User();

                            // 데이터 db에 전송하는 코드 작성...
                            GetDataFromEditText();
                            user_info .setRestaurant_name(s_restaurant_name);
                            user_info .setId1(s_id1);
                            user_info .setPhone_num(s_phone_num);
                            user_info .setPassword(s_password);
                            user_info.setOperation_hour("null");//나중에 추가
                            user_info .setIs_owner("0");//사장일 경우 0
                            user_info.setConfirm("0");//
                            user_info.setIs_accepted("null");
                            myRef.child(user_info_key).setValue(user_info);

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
                            User user_info = new User();
                            // 데이터 db에 전송하는 코드 작성...
                            GetDataFromEditText();
                            user_info.setId1(s_id1);
                            user_info.setPassword(s_password);
                            user_info.setPhone_num(s_phone_num);
                            user_info.setOperation_hour("null");//손님일 경우 null
                            user_info .setIs_owner("1");//손님일 경우1
                            user_info.setConfirm("null");//손님일 경우 null
                            user_info.setIs_accepted("0");
                            myRef.child(user_info_key).setValue(user_info);
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
        //edittext값을 string으로 바꿔줌
        s_restaurant_name = restaurant_name.getText().toString().trim();
        s_id1 = id1.getText().toString().trim();
        s_password = password.getText().toString().trim();
        s_phone_num = phone_num.getText().toString().trim();


    }

}
