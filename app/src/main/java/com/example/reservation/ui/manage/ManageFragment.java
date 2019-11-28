package com.example.reservation.ui.manage;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.reservation.R;
import com.example.reservation.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageFragment extends Fragment {
    private Context context;
    private ManageViewModel shareViewModel;
    private String restaurant1 = "";
    private String open_hour="";
    private String close_hour="";
    EditText edit_close,edit_open;
    TextView editrestaurant_name;
    String open,close;
    Button editcheck;
    TimePickerDialog timePickerDialog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        context=container.getContext();
        shareViewModel =
                ViewModelProviders.of(this).get(ManageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_manage, container, false);
        final TextView textView = root.findViewById(R.id.text_manage);
        shareViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // textView.setText(s);
            }
        });

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("User_info/");

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef2 = database1.getReference("Reservation/");

        Intent intent = getActivity().getIntent();

        final String id1 = intent.getExtras().getString("id");
        final String key1 = intent.getExtras().getString("key");

        editrestaurant_name = (TextView) root.findViewById(R.id.restaurant_name);
        edit_close=(EditText)root.findViewById(R.id.edit_close);
        edit_open=(EditText)root.findViewById(R.id.edit_open);
        editcheck = (Button) root.findViewById(R.id.check);


        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {//id값으로 정보를 찾음
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMyRestaurantManage(dataSnapshot, id1);
                editrestaurant_name.setText(restaurant1);
                edit_open.setText(open);//현재 오픈시간
                edit_close.setText(close);//현재 마감시간

                edit_open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog,new OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                edit_open.setText(hourOfDay+"시 "+minute+"분");//새 오픈시간

                            }
                        }, 15,24, false);
                        timePickerDialog.show();
                    }
                });
                edit_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog, new OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                edit_close.setText(hourOfDay+"시 "+minute+"분");//새 마감시간
                            }
                        }, 15,24, false);
                        timePickerDialog.show();
                    }
                });

             editcheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef1.child(key1).child("open").setValue(String.valueOf(edit_open.getText().toString()));//시간 변경
                    myRef1.child(key1).child("close").setValue(String.valueOf(edit_close.getText().toString()));
                    Toast.makeText(context, "운영시간이 변경되었습니다.", Toast.LENGTH_LONG).show();
                }
            });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return root;

    }
    //기존의 값들을 받아옴
    private void getMyRestaurantManage(@NonNull DataSnapshot dataSnapshot, String myId) {
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            User user_each = childSnapshot.getValue(User.class);
            if (user_each.getId1().equals(myId)) {
                restaurant1 = user_each.getRestaurant_name();
                open=user_each.getOpen();
                close=user_each.getClose();

                Log.i("가게이름", restaurant1);
                Log.i("manage_open",open_hour);
                Log.i("manage_close",close_hour);

                break;
            } else {
                continue;
            }
        }
    }


}