package com.example.reservation.ui.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.reservation.R;
import com.example.reservation.SignUpActivity;
import com.example.reservation.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoFragment extends Fragment {
    private Context context;
    private InfoViewModel infoViewModel;
    private String restaurant1;
    private String phone;
    private String password;
    private String is_owner;
    EditText editpw, editid1,editpassword_check,editphone_num,editrestaurant_name;
    TextView textrestaurant_name;
    Button editsubmit;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context=container.getContext();
        infoViewModel =
                ViewModelProviders.of(this).get(InfoViewModel.class);

        View root = inflater.inflate(R.layout.fragment_info, container, false);

        final TextView textView = root.findViewById(R.id.text_info);

        infoViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("User_info/");

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef2 = database1.getReference("Reservation/");

        Intent intent=getActivity().getIntent();
        final String id1= intent.getExtras().getString("id");
        final String key1 = intent.getExtras().getString("key");
        Log.i("info_id",id1);
        Log.i("info_key",key1);
        //id값으로 가게이름 가져오기

        editid1 = (EditText) root.findViewById(R.id.id1);
        editpw = (EditText) root.findViewById(R.id.password);
        editpassword_check = (EditText) root.findViewById(R.id.password_check);
        editphone_num = (EditText) root.findViewById(R.id.phone_num);
        editrestaurant_name = (EditText) root.findViewById(R.id.restaurant_name);
        textrestaurant_name = (TextView) root.findViewById(R.id.text7);
        editsubmit = (Button) root.findViewById(R.id.submit);

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMyRestaurantInfo(dataSnapshot, id1);
                if(is_owner.equals("0")){//사장일 경우
                    editrestaurant_name.setVisibility(View.VISIBLE);
                    editid1.setHint(id1);
                    editpw.setText(password);
                    editpassword_check.setText(password);
                    editphone_num.setText(phone);
                    editrestaurant_name.setHint(restaurant1);


                    editsubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 비밀번호 입력 확인
                            if (editpw.getText().toString().length() == 0) {
                                Toast.makeText(context, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                                editpw.requestFocus();
                                return;
                            }

                            // 비밀번호 확인 입력 확인
                            if (editpassword_check.getText().toString().length() == 0) {
                                Toast.makeText(context, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                                editpassword_check.requestFocus();
                                return;
                            }

                            // 비밀번호 일치 확인
                            if (!editpw.getText().toString().equals(editpassword_check.getText().toString())) {
                                Toast.makeText(context, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                                editpw.setText("");
                                editpassword_check.setText("");
                                editpw.requestFocus();
                                return;
                            }

                            // 비밀번호 조건 확인
                            if (!ConfirmPassword(editpw.getText().toString())) {
                                Toast.makeText(context, "8~16자 길이로 영어 대소문자, 숫자를 혼합하세요!", Toast.LENGTH_SHORT).show();
                                editpw.setText("");
                                editpassword_check.setText("");
                                editpw.requestFocus();
                                return;
                            }
                            // 전화 번호 조건 확인
                            if( !ConfirmPhonenum(editphone_num.getText().toString()) )
                            {
                                Toast.makeText(context,"올바른 번호를 입력하세요!",Toast.LENGTH_SHORT).show();
                                editphone_num.setText("");
                                editphone_num.requestFocus();
                                return;
                            }
                            myRef1.child(key1).child("password").setValue(editpw.getText().toString());//비밀번호 변경
                            myRef1.child(key1).child("phone_num").setValue(editphone_num.getText().toString());//번호 변경
                            Toast.makeText(context, "변경사항이 수정되었습니다.", Toast.LENGTH_LONG).show();


                        }
                    });
                }
                else{//고객일경우
                    editrestaurant_name.setVisibility(View.GONE);
                    textrestaurant_name.setVisibility(View.GONE);


                    editid1.setHint(id1);
                    editpw.setText((password));
                    editpassword_check.setText(password);
                    editphone_num.setText((phone));

                    editsubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 비밀번호 입력 확인
                            if (editpw.getText().toString().length() == 0) {
                                Toast.makeText(context, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                                editpw.requestFocus();
                                return;
                            }

                            // 비밀번호 확인 입력 확인
                            if (editpassword_check.getText().toString().length() == 0) {
                                Toast.makeText(context, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                                editpassword_check.requestFocus();
                                return;
                            }

                            // 비밀번호 일치 확인
                            if (!editpw.getText().toString().equals(editpassword_check.getText().toString())) {
                                Toast.makeText(context, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                                editpw.setText("");
                                editpassword_check.setText("");
                                editpw.requestFocus();
                                return;
                            }
                            // 비밀번호 조건 확인
                            if (!ConfirmPassword(editpw.getText().toString())) {
                                Toast.makeText(context, "8~16자 길이로 영어 대소문자, 숫자를 혼합하세요!", Toast.LENGTH_SHORT).show();
                                editpw.setText("");
                                editpassword_check.setText("");
                                editpw.requestFocus();
                                return;
                            }
                            // 전화 번호 조건 확인
                            if( !ConfirmPhonenum(editphone_num.getText().toString()) )
                            {
                                Toast.makeText(context,"올바른 번호를 입력하세요!",Toast.LENGTH_SHORT).show();
                                editphone_num.setText("");
                                editphone_num.requestFocus();
                                return;
                            }
                            myRef1.child(key1).child("password").setValue(editpw.getText().toString());//비밀번호 변경
                            myRef1.child(key1).child("phone_num").setValue(editphone_num.getText().toString());//번호 변경
                            Toast.makeText(context, "변경사항이 수정되었습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        return root;

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


    //가게의 정보를 가져옴
    private void getMyRestaurantInfo(@NonNull DataSnapshot dataSnapshot, String myId) {
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            User user_each = childSnapshot.getValue(User.class);
            if (user_each.getId1().equals(myId)) {//intent로 받은 값이랑 반복문을 통해서 확인한 아이디 값이랑 같으면
                restaurant1 = user_each.getRestaurant_name();//어플 사용하는 사장님의 가게 이름이다
                password=user_each.getPassword();
                phone=user_each.getPhone_num();
                is_owner=user_each.getIs_owner();
                Log.i("가게이름", restaurant1);
                Log.i("비밀번호", password);
                Log.i("핸드폰 번호", phone);
                Log.i("사장이니?", is_owner);//사장이면 0

                break;
            } else {
                continue;
            }
        }
    }

}