package com.example.reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

public class MainActivity extends AppCompatActivity {
    EditText editpw, editid;
    Button login, signUp;
    ValueEventListener mValueEventListener;
    String userid, userpw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database.getReference("User_info/");


        editid=(EditText) findViewById(R.id.editid);
        editpw=(EditText) findViewById(R.id.editpw);
        login = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signUp);

        userid = editid.getText().toString().trim();
        userpw = editpw.getText().toString().trim();
        final String c_id, o_id;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // id, pw 맞는지 검사하는 코드 작성...
                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                            String key=childSnapshot.getKey();
                            User user_each=childSnapshot.getValue(User.class);

                            if((editid.getText().toString().equals(user_each.getId1())) && (user_each.getIs_owner().equals("1")) && (editpw.getText().toString().equals(user_each.getPassword())) ){//
                                //손님이고 아이디,패스워드 일치할 경우
                                Log.i("id:", editid.getText().toString());
                                Log.i("pw:", editpw.getText().toString());
                                Log.i("누구냐:",user_each.getIs_owner());
                                Intent customer = new Intent(getApplicationContext(), CustomerHomeActivity.class);//손님 첫화면으로
                                startActivity(customer);
                                break;
                            }
                            else if((editid.getText().toString().equals(user_each.getId1())) && (user_each.getIs_owner().equals("0")) && (editpw.getText().toString().equals(user_each.getPassword())) ){
                                //사장이고 아이디, 패스워드 일치할 경우
                                //오류!!! 사장일 경우에만 전부 일치해도 토스트 메세지가 뜸,,,들어가는 거는 정상적
                                Log.i("id:", editid.getText().toString());
                                Log.i("pw:", editpw.getText().toString());
                                Log.i("누구냐:",user_each.getIs_owner());
                                Intent owner = new Intent(getApplicationContext(), OwnerHomeActivity.class);//사장 첫화면으로
                                startActivity(owner);
                                break;
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "아이디와 비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show();
                            }


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(sign);
            }
        });


    }
}
