package com.example.reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import android.content.SharedPreferences;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    //view
    EditText editpw, editid;
    Button login, signUp;
    private CheckBox autoLogin;
    ActionBar ab;
    //listener
    ValueEventListener mValueEventListener;

    //data
    private SharedPreferences setting;
    private SharedPreferences.Editor editor;
    FirebaseDatabase database;
    DatabaseReference myRef1;

    private String userid, userpw, autoId, autoPW;
    private boolean autoLoginChecker = false;
    String c_id, o_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("넌.이.예.");
        setContentView(R.layout.activity_main);

        initView();
        initDatabase();
        initData();

        autoLogin.setOnClickListener(autoLoginListener);
        login.setOnClickListener(loginListener);
        signUp.setOnClickListener(signUpListener);


    }

    private void initView() {
        ab = getSupportActionBar();
        ab.setTitle("뭐먹?");

        editid = findViewById(R.id.editid);
        editpw = findViewById(R.id.editpw);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
        autoLogin = findViewById(R.id.autoLogin);
    }

    public void initDatabase() {
        database = FirebaseDatabase.getInstance();
        myRef1 = getMyRef1();
        setting = getSharedPreferences("login", 0);
        editor = setting.edit();
    }

    private void initData() {
        userid = editid.getText().toString().trim();
        userpw = editpw.getText().toString().trim();
        autoId = setting.getString("ID", "");
        autoPW = setting.getString("PW", "");

        if (!autoId.equals("") && !autoPW.equals("")) {
            editid.setText(autoId);
            editpw.setText(autoPW);
            autoLogin.performClick();
            login.performClick();
        }
    }

    private void saveForAutoLogin() {
        if (autoLoginChecker) {
            editor.putString("ID", editid.getText().toString());
            editor.putString("PW", editpw.getText().toString());
            editor.commit();
        } else {
            editor.putString("ID", "");
            editor.putString("PW", "");
            editor.commit();
        }
    }

    CheckBox.OnClickListener autoLoginListener = new CheckBox.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (autoLogin.isChecked()) {
                autoLoginChecker = true;
            } else {
                autoLoginChecker = false;
            }
        }
    };

    View.OnClickListener signUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent sign = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(sign);
        }
    };

    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // id, pw 맞는지 검사하는 코드 작성...
            myRef1.addListenerForSingleValueEvent(mValueListener);
        }
    };

    ValueEventListener mValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            boolean login_success = false;
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                String key = getKeyCheck(childSnapshot);
                User user_each = getValueCheck(childSnapshot);

                String id = editid.getText().toString();
                String pw = editpw.getText().toString();

                if (userLoginCheck(id, pw, user_each)) {
                    login_success = userDivisionCheck(user_each, key, id);
                    editid.setText(null);
                    editpw.setText(null);
                    break;
                }
            }
            if (!login_success) {
                Toast.makeText(getApplicationContext(), "아이디와 비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                editid.setText(null);
                editpw.setText(null);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };


    // 데이터베이스 단위테스트를 진행하기위한 메서드
    // null 인지 아닌지 체크하면 좋을듯
    public DatabaseReference getMyRef1() {
        return database.getReference("User_info/");
    }

    public String getKeyCheck(DataSnapshot childSnapshot) {
        return childSnapshot.getKey();
    }

    public User getValueCheck(DataSnapshot childSnapshot) {
        return childSnapshot.getValue(User.class);
    }

    public boolean userLoginCheck(String id, String pw, User user_each){
        return (id.equals(user_each.getId1()) && pw.equals(user_each.getPassword()));

    }

    public boolean userDivisionCheck(User user_each, String key, String id){
        if (user_each.getIs_owner().equals("0")) {//사장인 경우
//                        Log.i("id:", editid.getText().toString());
//                        Log.i("pw:", editpw.getText().toString());
//                        Log.i("누구냐:", user_each.getIs_owner());
//                        Log.i("업종은?",user_each.getType());
            Intent owner = new Intent(getApplicationContext(), OwnerHomeActivity.class);//사장 첫화면으로
            owner.putExtra("key", key);
            owner.putExtra("id", id);
            owner.putExtra("isOwner", user_each.getIs_owner());
            saveForAutoLogin();
            startActivity(owner);

            return true;

        } else if(user_each.getIs_owner().equals("1")) {//고객인 경우
//                        Log.i("id:", editid.getText().toString());//test용 log
//                        Log.i("pw:", editpw.getText().toString());//test용 log
//                        Log.i("누구냐:", user_each.getIs_owner());//test용 log
//                        Log.i("key값:", key);//test용 log
            Intent customer = new Intent(getApplicationContext(), CustomerHomeActivity.class);//손님 첫화면으로
            customer.putExtra("key", key);
            customer.putExtra("id", id);
            customer.putExtra("isOwner", user_each.getIs_owner());
            saveForAutoLogin();
            startActivity(customer);

            return true;

        }

        return false;
    }


}