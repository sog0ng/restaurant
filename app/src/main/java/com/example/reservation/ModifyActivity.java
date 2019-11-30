package com.example.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ModifyActivity extends AppCompatActivity {
    EditText editcover;
    private Calendar minDate = Calendar.getInstance();
    private TimePicker tp;
    private static final int TIME_PICKER_INTERVAL=10;
    private DatePickerDialog datePickerDialog;
    private int curYear, curMonth, curDay, curHour, curMin;
    private int rsvYear, rsvMonth, rsvDay, rsvHour, rsvMin;
    private String rsvWeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database.getReference("Reservation/");

        Intent intent = getIntent();


        final String restaurant_name = getIntent().getStringExtra("reservation_name");
        final String nickname = getIntent().getStringExtra("nickname");
        final int year = getIntent().getIntExtra("year",0);
        final int month = intent.getIntExtra("month",0);
        final int day = intent.getIntExtra("day",0);
        final int hour = intent.getIntExtra("hour",0);                ;
        final int minute = intent.getIntExtra("minute",0);
        final int covers = intent.getIntExtra("covers",0);
        final String key1 = getIntent().getStringExtra("key");
        String hour1 = String.valueOf(hour);
        String minute1 = String.valueOf(minute);
        String year1 = String.valueOf(year);
        String month1 = String.valueOf(month);
        String day1 = String.valueOf(day);
        String covers1 = String.valueOf(covers);

        final TextView restaurant_name1 = (TextView) findViewById(R.id.restaurant_name);
        final TextView nickname1 = (TextView) findViewById(R.id.nickname);
        restaurant_name1.setText(restaurant_name);
        editcover = (EditText) findViewById(R.id.covers);

        editcover.setText(covers1);
        nickname1.setText(nickname);

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
                rsvHour = tp.getHour() < curYear ? tp.getHour() + 12 : tp.getHour();
                rsvMin = tp.getMinute()*10;


                if (editcover.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "인원수를 입력해주세요", Toast.LENGTH_SHORT).show();
                    editcover.requestFocus();
                }
                else if (rsvYear == 0 || rsvMonth == 0 || rsvDay == 0){
                    Toast.makeText(getApplicationContext(), "예약 일자를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                //예약 가능 시간 확인
                else if (rsvYear == curYear && rsvMonth == curMonth && rsvDay == curDay && (rsvHour < curHour + 1 || (rsvHour == curHour + 1 && rsvMin < curMin))) {
                    Toast.makeText(getApplicationContext(), "현재시간 1시간 이후부터 예약 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    myRef1.child(key1).child("covers").setValue(editcover.getText().toString());//인원변경
                    myRef1.child(key1).child("year").setValue(rsvYear);
                    myRef1.child(key1).child("month").setValue(rsvMin);
                    myRef1.child(key1).child("day").setValue(rsvDay);
                    myRef1.child(key1).child("week").setValue(rsvWeek);
                    myRef1.child(key1).child("hour").setValue(rsvHour);
                    myRef1.child(key1).child("minite").setValue(rsvMin);
                      Toast.makeText(getApplicationContext(), "예약이 변경되었습니다.", Toast.LENGTH_LONG).show();

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
