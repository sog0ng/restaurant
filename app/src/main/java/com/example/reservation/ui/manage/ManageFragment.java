package com.example.reservation.ui.manage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
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

import com.example.reservation.OwnerHomeActivity;
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
    private View root;
    private ManageViewModel shareViewModel;

    FirebaseDatabase database;
    DatabaseReference myRef1;

    private String restaurant1 = "";
    private String open_hour="";
    private String close_hour="";
    private static final int TIME_PICKER_INTERVAL=10;
    TextView edit_close,edit_open;
    TextView editrestaurant_name;
    String open,close,type;
    Button editcheck, closeTimeButton, openTimeButton;
    TimePickerDialog timePickerDialog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        context=container.getContext();
        root = inflater.inflate(R.layout.fragment_manage, container, false);

        shareViewModel =
                ViewModelProviders.of(this).get(ManageViewModel.class);

        initDatabase();


        Intent intent = getActivity().getIntent();

        final String id1 = intent.getExtras().getString("id");
        final String key1 = intent.getExtras().getString("key");

        checkPwDialog(id1, key1);

        editrestaurant_name = (TextView) root.findViewById(R.id.restaurant_name);
        edit_close=(TextView) root.findViewById(R.id.edit_close);
        edit_open=(TextView) root.findViewById(R.id.edit_open);
        editcheck = (Button) root.findViewById(R.id.check);
        openTimeButton = (Button) root.findViewById(R.id.open_time_button);
        closeTimeButton = (Button) root.findViewById(R.id.close_time_button);


        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {//id값으로 정보를 찾음
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMyRestaurantManage(dataSnapshot, id1);
                editrestaurant_name.setText(restaurant1+"("+ type+")");
                edit_open.setText(open);//현재 오픈시간
                edit_close.setText(close);//현재 마감시간

                openTimeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog,new OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                edit_open.setText(hourOfDay+"시 "+minute+"분");//새 오픈시간

                            }
                        }, 15,24, false);
                        timePickerDialog.show();
                        setTimePickerInterval(timePickerDialog);
                    }
                });
                closeTimeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog, new OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                edit_close.setText(hourOfDay+"시 "+minute+"분");//새 마감시간
                            }
                        }, 15,24, false);
                        timePickerDialog.show();
                        setTimePickerInterval(timePickerDialog);
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
                type=user_each.getType();
                Log.i("가게이름", restaurant1);
                Log.i("manage_open",open_hour);
                Log.i("manage_close",close_hour);

                break;
            } else {
                continue;
            }
        }
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

    void checkPwDialog(final String id1,final String key)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("비밀 번호 확인");
        builder.setMessage("본인확인 위해 비밀번호를 입력해주세요");
        final EditText input = new EditText(getActivity());
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("User_info/");

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());

        builder.setView(input);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String ed = input.getText().toString();
                        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean login_success = false;
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String key = childSnapshot.getKey();
                                    User user_each = childSnapshot.getValue(User.class);

                                    if (id1.equals(user_each.getId1())
                                            && input.getText().toString().equals(user_each.getPassword())) {
                                        Toast.makeText(getContext(), "아이디와 비밀번호가 일치 합니다.",Toast.LENGTH_SHORT).show();
                                        login_success = true;
                                        break;
                                    }
                                }
                                if(!login_success) {
                                    Toast.makeText(getContext(), "아이디와 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(), OwnerHomeActivity.class);
                                    intent.putExtra("key", key);
                                    intent.putExtra("id", id1);
                                    startActivity(intent);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }
    private void initDatabase(){
        database = FirebaseDatabase.getInstance();
        myRef1 = database.getReference("User_info/");
    }
}
