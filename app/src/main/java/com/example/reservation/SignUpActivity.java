package com.example.reservation;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.Object;

public class SignUpActivity extends AppCompatActivity {
    private Button submit, same_id_check, same_rsrt_check, openTimeButton, closeTimeButton;
    private RadioButton owner, customer;
    private EditText restaurant_name, id1, password, password_check, phone_num;
    private Spinner typeSpinner;
    private TextView restaurant_TV, type_TV, openTime, closeTime;
    private TimePickerDialog timePickerDialog;
    private String s_restaurant_name, s_id1, s_password, s_phone_num, c_id1, c_password, c_phone_num, s_type ,tmp;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> typeList;
    private LinearLayout RSRTLayout, typeLayout, openTimeLayout, closeTimeLayout;

    private boolean sameIdChecker = false, sameRSRTChecker = false;
    private static final int TIME_PICKER_INTERVAL=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("회원가입");
        setContentView(R.layout.signup);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("User_info");
        final DatabaseReference myRef2 = database.getReference("Type");
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
        typeSpinner = (Spinner) findViewById(R.id.type);
        restaurant_TV = (TextView)findViewById(R.id.text7);
        type_TV = (TextView)findViewById(R.id.text8);
        openTime = (TextView) findViewById(R.id.open_time);
        closeTime = (TextView) findViewById(R.id.close_time);
        openTimeButton = (Button) findViewById(R.id.open_time_button);
        closeTimeButton = (Button) findViewById(R.id.close_time_button);
        RSRTLayout = (LinearLayout) findViewById(R.id.restaurant_name_layout) ;
        typeLayout = (LinearLayout) findViewById(R.id.type_layout);
        openTimeLayout = (LinearLayout) findViewById(R.id.open_time_layout);
        closeTimeLayout = (LinearLayout) findViewById(R.id.close_time_layout);

        typeList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, typeList);

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    typeList.add(data.getValue().toString());
                }
                arrayAdapter.notifyDataSetChanged();
                typeSpinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s_type = typeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        openTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(SignUpActivity.this, android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        openTime.setText(hourOfDay+"시 "+minute+"분");//새 오픈시간

                    }
                }, 15,24, false);
                timePickerDialog.show();
                setTimePickerInterval(timePickerDialog);
            }
        });

        closeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(SignUpActivity.this, android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        closeTime.setText(hourOfDay+"시 "+minute+"분");//새 오픈시간

                    }
                }, 15,24, false);
                timePickerDialog.show();
                setTimePickerInterval(timePickerDialog);
            }
        });

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
                RSRTLayout.setVisibility(View.VISIBLE);
                typeLayout.setVisibility(View.VISIBLE);
                openTimeLayout.setVisibility(View.VISIBLE);
                closeTimeLayout.setVisibility(View.VISIBLE);

                if (owner.isChecked()) {//오너클릭시
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

                            if (s_type == null) {
                                Toast.makeText(SignUpActivity.this, "업종을 입력하세요!", Toast.LENGTH_SHORT).show();
                                typeSpinner.requestFocus();
                                return;
                            }

                            if (!sameRSRTChecker) {
                                Toast.makeText(getApplicationContext(), "가게명 중복체크를 해주세요",  Toast.LENGTH_SHORT).show();
                                restaurant_name.requestFocus();
                                return;
                            }

                            if (openTime.getText().equals("")) {
                                Toast.makeText(getApplicationContext(), "개점시간을 선택하세요",  Toast.LENGTH_SHORT).show();
                            }

                            if (closeTime.getText().equals("")) {
                                Toast.makeText(getApplicationContext(), "폐점시간을 선택하세요",  Toast.LENGTH_SHORT).show();
                            }

                            // 데이터 db에 전송하는 코드 작성...
                            GetDataFromEditText();
                            user_info.setRestaurant_name(s_restaurant_name);
                            user_info.setId1(s_id1);
                            user_info.setPhone_num(s_phone_num);
                            user_info.setPassword(s_password);
                            user_info.setOpen(openTime.getText().toString());
                            user_info.setClose(closeTime.getText().toString());
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
                    typeSpinner.setVisibility(View.GONE);
                    type_TV.setVisibility(View.GONE);
                    same_rsrt_check.setVisibility(View.GONE);
                }
            }
        });

        customer.setOnClickListener(new RadioButton.OnClickListener() {//customer체크박스 클릭시
            @Override
            public void onClick(View v) {
                if (customer.isChecked()) {//고객 클릭시
                    RSRTLayout.setVisibility(View.GONE);
                    typeLayout.setVisibility(View.GONE);
                    openTimeLayout.setVisibility(View.GONE);
                    closeTimeLayout.setVisibility(View.GONE);

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

    private void setTimePickerInterval(TimePickerDialog timePicker) {
        try {
            NumberPicker minutePicker = (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier(
                    "minute", "id", "android"));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue((60/ TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
