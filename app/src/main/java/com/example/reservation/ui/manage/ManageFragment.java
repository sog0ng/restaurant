package com.example.reservation.ui.manage;

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
    private String open_minute="";
    private String close_hour="";
    private String close_minute="";
    EditText editrestaurant_name;
    TimePicker open_tp, close_tp;
    int openhour, openminute,closehour, closeminute;
    private static final int TIME_PICKER_INTERVAL=30;
    Button editcheck;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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

        editrestaurant_name = (EditText) root.findViewById(R.id.restaurant_name);
        open_tp=(TimePicker)root.findViewById(R.id.timePicker_open);
        close_tp=(TimePicker)root.findViewById(R.id.timePicker_close);
        editcheck = (Button) root.findViewById(R.id.check);
        setTimePickerInterval(open_tp);
        setTimePickerInterval(close_tp);

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {//id값으로 정보를 찾음
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMyRestaurantManage(dataSnapshot, id1);
                editrestaurant_name.setText(restaurant1);
                openhour = open_tp.getHour();
                openminute=open_tp.getMinute();
                closehour=close_tp.getHour();
                closeminute=close_tp.getMinute();

                editcheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myRef1.child(key1).child("open_hour").setValue(String.valueOf(openhour));//시간 변경
                        myRef1.child(key1).child("open_minute").setValue(String.valueOf(openminute*30));
                        myRef1.child(key1).child("close_hour").setValue(String.valueOf(closehour));
                        myRef1.child(key1).child("close_minute").setValue(String.valueOf(closeminute*30));
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


    private void getMyRestaurantManage(@NonNull DataSnapshot dataSnapshot, String myId) {
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            User user_each = childSnapshot.getValue(User.class);
            if (user_each.getId1().equals(myId)) {//intent로 받은 값이랑 반복문을 통해서 확인한 아이디 값이랑 같으면
                restaurant1 = user_each.getRestaurant_name();//어플 사용하는 사장님의 가게 이름이다
                open_hour=user_each.getOpen_hour();
                open_minute=user_each.getOpen_minute();
                close_hour=user_each.getClose_hour();
                close_minute=user_each.getClose_minute();
                Log.i("가게이름", restaurant1);
                Log.i("manage_open",open_hour);
                Log.i("manage_close",close_hour);
                //Toast.makeText(getContext(), id1+"\n"+key1+"\nrestaurant1: "+restaurant1,Toast.LENGTH_LONG).show();
                break;
            } else {
                continue;
            }
        }
    }
    //타임피커 30분단위 설정
    private void setTimePickerInterval(TimePicker timePicker) {
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