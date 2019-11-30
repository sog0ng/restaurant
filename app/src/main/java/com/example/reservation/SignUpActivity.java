package com.example.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.Object;

public class SignUpActivity extends AppCompatActivity {
    Button submit, same_id_check, same_rsrt_check;
    RadioButton owner, customer;
    EditText restaurant_name, id1, password, password_check, phone_num, type;
    TextView restaurant_TV, type_TV;
    String s_restaurant_name, s_id1, s_password, s_phone_num, c_id1, c_password, c_phone_num,s_type;

    private boolean sameIdChecker = false, sameRSRTChecker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("회원가입");
        setContentView(R.layout.signup);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("User_info");
        final String user_info_key = myRef.push().getKey();//고유키

        submit = (Button) findViewById(R.id.submit);
        owner = (RadioButton) findViewById(R.id.owner);
        customer = (RadioButton) findViewById(R.id.customer);
        id1 = (EditText) findViewById(R.id.id1);
        password = (EditText) findViewById(R.id.password);
        password_check = (EditText) findViewById(R.id.password_check);
        phone_num = (EditText) findViewById(R.id.phone_num);
        restaurant_name = (EditText) findViewById(R.id.restaurant_name);
        same_id_check = (Button) findViewById(R.id.same_id_check);
        same_rsrt_check = (Button) findViewById(R.id.same_rsrt_check);
        type = (EditText) findViewById(R.id.type);
        restaurant_TV = (TextView)findViewById(R.id.text7);
        type_TV = (TextView)findViewById(R.id.text8);

        // 사용자 타입 선택 여부 검사
        // 검사라고 할 수 없음 그냥 토스트 메시지 띄워주는거 말고는 하는 기능이 없음
        // radioGroupListener 찾아봐야함
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !owner.isChecked() && !customer.isChecked() ) {
                    Toast.makeText(SignUpActivity.this, "회원가입 타입을 선택하세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 비밀번호 일치 검사
        password_check.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String str_password = password.getText().toString();
                String str_password_check = password_check.getText().toString();

                if (str_password.equals(str_password_check)) {
                    password.setTextColor(Color.BLACK);
                    password_check.setTextColor(Color.BLACK);
                } else {
                    password.setTextColor(Color.RED);
                    password_check.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //아이디 중복확인
        same_id_check.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();
                            User user_each = childSnapshot.getValue(User.class);

                            if (id1.getText().toString().equals(user_each.getId1())) {
                                Toast.makeText(SignUpActivity.this, "이미 사용중이거나 탈퇴한 아이디입니다.", Toast.LENGTH_SHORT).show();
                                sameIdChecker = false;
                                id1.setText("");
                                id1.requestFocus();
                                break;
                            } else if (id1.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                                sameIdChecker = false;
                                id1.requestFocus();
                                break;
                            }
                            else {
                                Toast.makeText(SignUpActivity.this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                                sameIdChecker = true;
                                password.requestFocus();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        //가게이름 중복확인
        same_rsrt_check.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();
                            User user_each = childSnapshot.getValue(User.class);

                            if (restaurant_name.getText().toString().equals(user_each.getRestaurant_name())) {
                                Toast.makeText(SignUpActivity.this, "이미 사용중인 가게명입니다.", Toast.LENGTH_SHORT).show();
                                sameRSRTChecker = false;
                                restaurant_name.setText("");
                                restaurant_name.requestFocus();
                                break;
                            } else if (restaurant_name.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "가게명를 입력하세요", Toast.LENGTH_SHORT).show();
                                sameRSRTChecker = false;
                                restaurant_name.requestFocus();
                                break;
                            }
                            else {
                                Toast.makeText(SignUpActivity.this, "사용 가능한 가게명입니다.", Toast.LENGTH_SHORT).show();
                                sameRSRTChecker = true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        //전화번호 입력시 -입력
        phone_num.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        owner.setOnClickListener(new RadioButton.OnClickListener() {//owner체크박스 클릭시
            @Override
            public void onClick(View v) {
                if (owner.isChecked()) {//오너클릭시
                    restaurant_name.setVisibility(View.VISIBLE);//가게 이름 나타나게
                    restaurant_TV.setVisibility(View.VISIBLE);
                    type.setVisibility(View.VISIBLE);//업종나타나게
                    type_TV.setVisibility(View.VISIBLE);
                    same_rsrt_check.setVisibility(View.VISIBLE);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            User user_info = new User();

                            // 아이디 입력 확인
                            if (id1.getText().toString().length() == 0) {
                                Toast.makeText(SignUpActivity.this, "아이디를 입력하세요!", Toast.LENGTH_SHORT).show();
                                id1.requestFocus();
                                return;
                            }

                            if (!sameIdChecker) {
                                Toast.makeText(getApplicationContext(), "아이디 중복체크를 해주세요",  Toast.LENGTH_SHORT).show();
                                id1.requestFocus();
                                return;
                            }

                            // 비밀번호 입력 확인
                            if (password.getText().toString().length() == 0) {
                                Toast.makeText(SignUpActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                                password.requestFocus();
                                return;
                            }

                            // 비밀번호 확인 입력 확인
                            if (password_check.getText().toString().length() == 0) {
                                Toast.makeText(SignUpActivity.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                                password_check.requestFocus();
                                return;
                            }

                            // 비밀번호 일치 확인
                            if (!password.getText().toString().equals(password_check.getText().toString())) {
                                Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                                password.setText("");
                                password_check.setText("");
                                password.requestFocus();
                                return;
                            }

                            // 비밀번호 조건 확인
                            if (!ConfirmPassword(password.getText().toString())) {
                                Toast.makeText(SignUpActivity.this, "8~16자 길이로 영어 대소문자, 숫자를 혼합하세요!", Toast.LENGTH_SHORT).show();
                                password.setText("");
                                password_check.setText("");
                                password.requestFocus();
                                return;
                            }

                            // 전화 번호 조건 확인
                            if( !ConfirmPhonenum(phone_num.getText().toString()) )
                            {
                                Toast.makeText(SignUpActivity.this,"올바른 번호를 입력하세요!",Toast.LENGTH_SHORT).show();
                                phone_num.setText("");
                                phone_num.requestFocus();
                                return;
                            }

                            // 아이디 입력 확인
                            if (restaurant_name.getText().toString().length() == 0) {
                                Toast.makeText(SignUpActivity.this, "가게명을 입력하세요!", Toast.LENGTH_SHORT).show();
                                restaurant_name.requestFocus();
                                return;

                            }

                            if (type.getText().toString().length() == 0) {
                                Toast.makeText(SignUpActivity.this, "업종을 입력하세요!", Toast.LENGTH_SHORT).show();
                                type.requestFocus();
                                return;
                            }

                            if (!sameRSRTChecker) {
                                Toast.makeText(getApplicationContext(), "가게명 중복체크를 해주세요",  Toast.LENGTH_SHORT).show();
                                restaurant_name.requestFocus();
                                return;
                            }

                            // 데이터 db에 전송하는 코드 작성...
                            GetDataFromEditText();
                            user_info.setRestaurant_name(s_restaurant_name);
                            user_info.setId1(s_id1);
                            user_info.setPhone_num(s_phone_num);
                            user_info.setPassword(s_password);
                            user_info.setOpen("null");
                            user_info.setClose("null");
                            user_info.setType(s_type);
                            user_info.setIs_owner("0");//사장일 경우 0

                            myRef.child(user_info_key).setValue(user_info);

                            Intent main = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(main);
                        }
                    });
                } else {
                    restaurant_name.setVisibility(View.GONE);
                    restaurant_TV.setVisibility(View.GONE);
                    type.setVisibility(View.GONE);
                    type_TV.setVisibility(View.GONE);
                    same_rsrt_check.setVisibility(View.GONE);
                }
            }
        });

        customer.setOnClickListener(new RadioButton.OnClickListener() {//customer체크박스 클릭시
            @Override
            public void onClick(View v) {
                if (customer.isChecked()) {//고객 클릭시
                    restaurant_name.setVisibility(View.GONE);  //가게 이름 나타나지않게
                    restaurant_TV.setVisibility(View.GONE);
                    type.setVisibility(View.GONE);
                    type_TV.setVisibility(View.GONE);
                    same_rsrt_check.setVisibility(View.GONE);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            User user_info = new User();

                            // 아이디 입력 확인
                            if (id1.getText().toString().length() == 0) {
                                Toast.makeText(SignUpActivity.this, "아이디를 입력하세요!", Toast.LENGTH_SHORT).show();
                                id1.requestFocus();
                                return;
                            }

                            if (!sameIdChecker) {
                                Toast.makeText(getApplicationContext(), "아이디 중복체크를 해주세요",  Toast.LENGTH_SHORT).show();
                                id1.requestFocus();
                                return;
                            }

                            // 비밀번호 입력 확인
                            if (password.getText().toString().length() == 0) {
                                Toast.makeText(SignUpActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                                password.requestFocus();
                                return;
                            }

                            // 비밀번호 확인 입력 확인
                            if (password_check.getText().toString().length() == 0) {
                                Toast.makeText(SignUpActivity.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                                password_check.requestFocus();
                                return;
                            }

                            // 비밀번호 일치 확인
                            if (!password.getText().toString().equals(password_check.getText().toString())) {
                                Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                                password.setText("");
                                password_check.setText("");
                                password.requestFocus();
                                return;
                            }

                            // 비밀번호 조건 확인
                            if (!ConfirmPassword(password.getText().toString())) {
                                Toast.makeText(SignUpActivity.this, "8~16자 길이로 영어 대소문자, 숫자를 혼합하세요!", Toast.LENGTH_SHORT).show();
                                password.setText("");
                                password_check.setText("");
                                password.requestFocus();
                                return;
                            }

                            // 전화 번호 조건 확인
                            if( !ConfirmPhonenum(phone_num.getText().toString()) )
                            {
                                Toast.makeText(SignUpActivity.this,"올바른 번호를 입력하세요!",Toast.LENGTH_SHORT).show();
                                phone_num.setText("");
                                phone_num.requestFocus();
                                return;
                            }

                            // 데이터 db에 전송하는 코드 작성...
                            GetDataFromEditText();
                            user_info.setRestaurant_name("null");
                            user_info.setId1(s_id1);
                            user_info.setPassword(s_password);
                            user_info.setPhone_num(s_phone_num);
                            user_info.setOpen("null");
                            user_info.setClose("null");
                            user_info.setType("null");
                            user_info.setIs_owner("1");//손님일 경우1
                            myRef.child(user_info_key).setValue(user_info);
                            Intent main = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(main);
                        }
                    });
                } else {
                    restaurant_name.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    public void GetDataFromEditText() {
        //edittext값을 string으로 바꿔줌
        s_restaurant_name = restaurant_name.getText().toString().trim();
        s_id1 = id1.getText().toString().trim();
        s_password = password.getText().toString().trim();
        s_phone_num = phone_num.getText().toString().trim();
        s_type=type.getText().toString().trim();
    }

    public boolean ConfirmPassword(String input) {
        boolean result = false;
        //비밀번호 조건은 8자리~16자리
        // 영문 대소문자, 숫자 혼합 두가지만 하면 true

        Pattern pAlphaLow = Pattern.compile("[a-z]]");
        Pattern pAlphaUp = Pattern.compile("[A-Z]]");
        Pattern pNumber = Pattern.compile("[0-9]]");
        Matcher match;
        int nCharType = 0;//비밀번호가 몇가지의 조합인지를 확인

        // 영소문자 포함?
        match = pAlphaLow.matcher(input);
        if (match.find())
            nCharType++;
        // 영대문자 포함?
        match = pAlphaUp.matcher(input);
        if (match.find())
            nCharType++;
        // 숫자 포함?
        match = pNumber.matcher(input);
        if (match.find())
            nCharType++;

        // 두가지 이상 섞여있고 길이 제한도 잘 지켜진 경우
        //if( nCharType >=2 && 8 <= input.length() && input.length() <= 16 ){
        if (8 <= input.length() && input.length() <= 16) {
            result = true;
        }
        //else if( nCharType < 2 || input.length() < 8 || input.length() > 16 ){
        else if (input.length() < 8 || input.length() > 16) {
            //Toast.makeText(SignUpActivity.this, "nCharType:"+nCharType+" input.length():"+input.length(), Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }


    public boolean ConfirmPhonenum(String input){
        boolean result = false;

        String regExp_mobile ="^01[016789][.-]?\\d{3,4}[.-]?\\d{4}$";
        String regExp_wireline = "^\\d{2,3}[.-]?\\d{3,4}[.-]?\\d{4}$";

        if( input.matches(regExp_mobile) || input.matches(regExp_wireline) ){
            result = true;
        }else {
            result = false;
        }

        return result;
    }

}
