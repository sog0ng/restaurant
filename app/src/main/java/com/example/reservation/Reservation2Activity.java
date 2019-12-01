package com.example.reservation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Reservation2Activity extends AppCompatActivity {
    private Calendar minDate = Calendar.getInstance();
    private TimePicker tp;
    private static final int TIME_PICKER_INTERVAL=10;
    private DatePickerDialog datePickerDialog;
    private boolean mIgnoreEvent=false;
    String owner_id,type;
    private int curYear, curMonth, curDay, curHour, curMin;
    private int rsvYear, rsvMonth, rsvDay, rsvHour, rsvMin;
    private String rsvWeek, openTimeString, closeTimeString, avgScoreRestaurant;

    Date openTime, closeTime, rsvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation2);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef3 = database.getReference("User_info");

        Intent intent = getIntent();

        final TextView restaurant_name = (TextView) findViewById(R.id.restaurant_name);
        restaurant_name.setText(intent.getStringExtra("restaurant_name"));
        final TextView type1 = (TextView) findViewById(R.id.type);
        final TextView scoreTV = (TextView) findViewById(R.id.scoreTV);

        final String restaurant_name1=intent.getStringExtra("restaurant_name");
        final String r_id=intent.getExtras().getString("id");

        openTimeString = intent.getStringExtra("openTime");
        closeTimeString = intent.getStringExtra("closeTime");

        try {
            openTime = new SimpleDateFormat("HH시 mm분").parse(openTimeString);
            closeTime = new SimpleDateFormat("HH시 mm분").parse(closeTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    User user_each = childSnapshot.getValue(User.class);
                    if(user_each.getRestaurant_name().equals(restaurant_name1)){
                        owner_id = user_each.getId1();
                        type = user_each.getType();
                        int scoreCount = user_each.getCount();
                        if (scoreCount < 30 )
                            avgScoreRestaurant = "평점 : 0점";
                        else
                            avgScoreRestaurant = "평점 : " + user_each.getAvgScore() + "점";

                        type1.setText(type);

                        scoreTV.setText(avgScoreRestaurant);
                        Log.i("사장아이디",owner_id);
                        Log.i("type",type);

                        break;
                    }
                    else{
                        continue;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // 날짜 선택
        Button date = findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        tp = (TimePicker) findViewById(R.id.timePicker);
        setTimePickerInterval(tp);

        Button next = (Button) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            EditText covers,nickname;
            @Override
            public void onClick(View v) {
                SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY");
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                SimpleDateFormat hourFormat = new SimpleDateFormat("hh");
                SimpleDateFormat minFormat = new SimpleDateFormat("mm");
                SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE");


                curYear = Integer.parseInt(yearFormat.format(minDate.getTime().getTime()));
                curMonth = Integer.parseInt(monthFormat.format(minDate.getTime().getTime()));
                curDay = Integer.parseInt(dayFormat.format(minDate.getTime().getTime()));
                curHour = Integer.parseInt(hourFormat.format(minDate.getTime().getTime()));
                curMin = Integer.parseInt(minFormat.format(minDate.getTime().getTime()));
                Date date = new Date(rsvYear, rsvMonth - 1, rsvDay - 1);
                rsvWeek = weekFormat.format(date);
                rsvHour = tp.getHour();
                rsvMin = tp.getMinute()*10;


                covers = (EditText) findViewById(R.id.covers);
                nickname = (EditText) findViewById(R.id.nickname);

                try {
                    rsvTime = new SimpleDateFormat("HH시 mm분").parse(rsvHour + "시 " + rsvMin + "분");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (covers.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "인원수를 입력해주세요", Toast.LENGTH_SHORT).show();
                    covers.requestFocus();
                }
                else if (rsvYear == 0 || rsvMonth == 0 || rsvDay == 0){
                    Toast.makeText(getApplicationContext(), "예약 일자를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                else if (nickname.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "예약자 성명을 입력해주세요", Toast.LENGTH_SHORT).show();
                    nickname.requestFocus();
                } else if (openTime.compareTo(rsvTime) > 0 || closeTime.compareTo(rsvTime) < 0) {
                    Toast.makeText(getApplicationContext(), "예약 가능 시간이 아닙니다.\n예약 가능 시간은 " + openTimeString + "부터 " + closeTimeString + "까지 입니다.", Toast.LENGTH_SHORT).show();
                }
                //예약 가능 시간 확인
                else if (rsvYear == curYear && rsvMonth == curMonth && rsvDay == curDay && (rsvHour < curHour + 1 || (rsvHour == curHour + 1 && rsvMin < curMin))) {
                    Toast.makeText(getApplicationContext(), "현재시간 1시간 이후부터 예약 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent result = new Intent(getApplicationContext(), ResultActivity.class);
                    result.putExtra("type",type);
                    result.putExtra("restaurant_name", restaurant_name.getText());
                    result.putExtra("year", rsvYear);
                    result.putExtra("month", rsvMonth);
                    result.putExtra("day", rsvDay);
                    result.putExtra("week", rsvWeek);
                    result.putExtra("hour",tp.getHour());
                    result.putExtra("minute",tp.getMinute()*10);
                    result.putExtra("covers", covers.getText().toString());
                    result.putExtra("nickname", nickname.getText().toString());
                    result.putExtra("id3",r_id);
                    result.putExtra("owner_id",owner_id);
                    startActivity(result);
                    finish();
                }
            }
        });
    }

    void showDate() {
        Calendar c = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this,
                R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            TextView textView_date = (TextView) findViewById(R.id.textView_date);
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                rsvYear = year;
                rsvMonth = month+1;
                rsvDay = dayOfMonth;
                textView_date.setText(rsvYear + "년" + rsvMonth + "월" + rsvDay + "일");

            }
        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), Calendar.DAY_OF_MONTH);

        //최저예약일자 설정
        datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());

        datePickerDialog.show();
    }

    //타임피커 10분단위 설정
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
