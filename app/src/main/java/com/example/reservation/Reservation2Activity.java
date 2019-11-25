package com.example.reservation;

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

import androidx.appcompat.app.AppCompatActivity;

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

    int y=0,m=0,d=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation2);

        Intent intent = getIntent();

        final TextView restaurant_name = (TextView) findViewById(R.id.restaurant_name);
        restaurant_name.setText(intent.getStringExtra("restaurant_name"));
        final String r_id=intent.getExtras().getString("id");


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
                int curYear = Integer.parseInt(yearFormat.format(minDate.getTime().getTime())),
                        curMonth = Integer.parseInt(monthFormat.format(minDate.getTime().getTime())),
                        curDay = Integer.parseInt(dayFormat.format(minDate.getTime().getTime())),
                        curHour = Integer.parseInt(hourFormat.format(minDate.getTime().getTime())),
                        curMin = Integer.parseInt(minFormat.format(minDate.getTime().getTime()));

                covers = (EditText) findViewById(R.id.covers);
                nickname = (EditText) findViewById(R.id.nickname);

                if (covers.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "인원수를 입력해주세요", Toast.LENGTH_SHORT).show();
                    covers.requestFocus();
                }
                else if (y == 0 || m == 0 || d == 0) {
                    Toast.makeText(getApplicationContext(), "예약 일자를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                //예약 가능 시간 확인
                else if (y == curYear && m == curMonth && d == curDay && (tp.getHour() < curHour + 1 || (tp.getHour() == curHour + 1 && tp.getMinute() < curMin))) {
                    Toast.makeText(getApplicationContext(), "현재시간 1시간 이후부터 예약 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent result = new Intent(getApplicationContext(), ResultActivity.class);
                    result.putExtra("restaurant_name", restaurant_name.getText());

                    // 시간,id 전달
                    result.putExtra("year", y);
                    result.putExtra("month", m);
                    result.putExtra("day", d);
                    result.putExtra("hour",tp.getHour());
                    result.putExtra("minute",tp.getMinute());
                    result.putExtra("covers", covers.getText().toString());
                    result.putExtra("nickname", nickname.getText().toString());
                    result.putExtra("id3",r_id);
                    startActivity(result);
                    finish();
                }
            }
        });
    }

    void showDate() {
        Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            TextView textView_date = (TextView) findViewById(R.id.textView_date);
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;

                textView_date.setText(y+"년"+m+"월"+d+"일");
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
            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
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
