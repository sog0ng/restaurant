package com.example.reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    EditText editpw, editid;
    Button login, signUp;
    ValueEventListener mValueEventListener;
    private String userid, userpw, autoId, autoPW;
    private CheckBox autoLogin;
    private SharedPreferences setting;
    private SharedPreferences.Editor editor;
    private boolean autoLoginChecker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("뭐먹?");
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database.getReference("User_info/");

        setting = getSharedPreferences("login", 0);
        editor = setting.edit();

        editid = (EditText) findViewById(R.id.editid);
        editpw = (EditText) findViewById(R.id.editpw);
        login = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signUp);
        autoLogin = (CheckBox) findViewById(R.id.autoLogin);

        userid = editid.getText().toString().trim();
        userpw = editpw.getText().toString().trim();
        final String c_id, o_id;

        autoId = setting.getString("ID", "");
        autoPW = setting.getString("PW", "");

        autoLogin.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoLogin.isChecked()) {
                    autoLoginChecker = true;
                } else {
                    autoLoginChecker = false;
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // id, pw 맞는지 검사하는 코드 작성...
                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        boolean login_success = false;
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();
                            User user_each = childSnapshot.getValue(User.class);

                            if (editid.getText().toString().equals(user_each.getId1())
                                    && editpw.getText().toString().equals(user_each.getPassword())) {
                                if (user_each.getIs_owner().equals("0")) {//사장인 경우
                                    Log.i("id:", editid.getText().toString());
                                    Log.i("pw:", editpw.getText().toString());
                                    Log.i("누구냐:", user_each.getIs_owner());
                                    Intent owner = new Intent(getApplicationContext(), OwnerHomeActivity.class);//사장 첫화면으로
                                    owner.putExtra("key", key);
                                    owner.putExtra("id", editid.getText().toString());
                                    saveForAutoLogin();
                                    startActivity(owner);
                                } else {//고객인 경우
                                    Log.i("id:", editid.getText().toString());//test용 log
                                    Log.i("pw:", editpw.getText().toString());//test용 log
                                    Log.i("누구냐:", user_each.getIs_owner());//test용 log
                                    Log.i("key값:", key);//test용 log
                                    Intent customer = new Intent(getApplicationContext(), CustomerHomeActivity.class);//손님 첫화면으로
                                    customer.putExtra("key", key);
                                    customer.putExtra("id", editid.getText().toString());
                                    saveForAutoLogin();
                                    startActivity(customer);
                                }
                                editid.setText(null);
                                editpw.setText(null);
                                login_success = true;
                                break;
                            }
                        }
                        if( !login_success ){
                            Toast.makeText(getApplicationContext(), "아이디와 비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show();
                            editid.setText(null);
                            editpw.setText(null);
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

        if(!autoId.equals("") && !autoPW.equals("")) {
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
}
