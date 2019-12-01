package com.example.reservation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reservation.item.ListViewItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class DetailsOfRSRV extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRefReservation;
    DatabaseReference myRefUser;
    Intent intent;

    private String key, r_id, str_restaurantName, str_nickname, type,
            is_accepted, is_confirm, isOwner, scoredByCustomer, scoredByRestaurant;
    private int year, month, day, hour, minute, cover, iDday, selectedScore;

    TextView r_nickname, restaurantName, r_date, r_time, covers, status;
    Button submit,cancel, modify;
    Spinner scoreSpinner;
    View dialogView;

    android.app.AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_of_reservation);

        initDatabase();
        initData();
        initView();

        setStatus();
        submit.setOnClickListener(submitListener);
        checkChangeable();
        modify.setOnClickListener(modifyListener);
        cancel.setOnClickListener(cancelListener);
    }

    void setStatus(){
        if (is_accepted.equals("null")) {                                     //예약 승인 안됨

            status.setText("처리중");

        }
        else if (is_accepted.equals("1")) {   //예약 승인됨

            if (iDday <= 0) {                                                                   //방문 예정일 : 과거 ~ 오늘

                if (is_confirm.equals("null")) {                                 //방문 확인 안됨

                    status.setText("미방문");

                } else if (is_confirm.equals("1")) {        //방문 확인 됨

                    if (isOwner.equals("0")) {

                        if (scoredByRestaurant.equals("null")) {

                            status.setText("평점 입력 대기");
                            scorePopupOnwer();

                        }
                        else {

                            status.setText("평점 : " + scoredByRestaurant);

                        }

                    }
                    else {

                        if (scoredByCustomer.equals("null")) {

                            status.setText("평점 입력 대기");
                            scorePopupCustomer();

                        } else {

                            status.setText("평점 : " + scoredByCustomer);

                        }

                    }

                } else {
                    if (isOwner.equals("0")) {

                        if (scoredByRestaurant.equals("null")) {

                            status.setText("평점 입력 대기");
                            scorePopupOnwer();

                        }
                        else {

                            status.setText("평점 : " + scoredByRestaurant);

                        }

                    } else {

                        status.setText("미방문");

                    }

                }

            } else {

                status.setText("방문 예정 : D - " + iDday);

            }

        }
        else if(is_accepted.equals("0")){

            status.setText("예약 거절");

        }
        else {

            status.setText("예약 취소");

        }
    }

    void scorePopupOnwer() {
        builder.setMessage("방문한 고객은 어땠나요?");
        builder.setView(dialogView);

        scoreSpinner.setOnItemSelectedListener(scoreSpinnerListener);

        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DetailsOfRSRV.this, "평점 " + selectedScore + "점 이 입력되었습니다.", Toast.LENGTH_SHORT).show();

                        setScoredByRestaurant(Integer.toString(selectedScore));

                        myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                setScoreToCustomer(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void setScoredByRestaurant(String value) {
        myRefReservation.child(key).child("scoredByRestaurant").setValue(value);
    }

    public void setScoreToCustomer(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

            User user_each = childSnapshot.getValue(User.class);

            if (user_each.getId1().equals(r_id)) {

                Log.i("원래 SumScore ", Integer.toString(user_each.getSumScore()));
                user_each.setSumScore(user_each.getSumScore() + selectedScore);
                user_each.setCount(user_each.getCount() + 1);
                user_each.setAvgScore(user_each.getSumScore() / user_each.getCount());
                Log.i("변경된 SumScore ", Integer.toString(user_each.getSumScore()));

                myRefUser.child(childSnapshot.getKey()).child("sumScore").setValue(user_each.getSumScore());
                myRefUser.child(childSnapshot.getKey()).child("count").setValue(user_each.getCount());
                myRefUser.child(childSnapshot.getKey()).child("avgScore").setValue(user_each.getAvgScore());

                break;
            }
        }
    }

    void scorePopupCustomer() {
        builder.setMessage("방문한 매장은 어땠나요?");
        builder.setView(dialogView);

        scoreSpinner.setOnItemSelectedListener(scoreSpinnerListener);

        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DetailsOfRSRV.this, "평점 " + selectedScore + "점 이 입력되었습니다.", Toast.LENGTH_SHORT).show();

                        setScoredByCustomer(Integer.toString(selectedScore));

                        myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                setScoreToRestaurant(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void setScoredByCustomer(String value) {
        myRefReservation.child(key).child("scoredByCustomer").setValue(value);
    }

    public void setScoreToRestaurant(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            User user_each = childSnapshot.getValue(User.class);
            if (user_each.getRestaurant_name().equals(str_restaurantName)) {

                //Log.i("원래 SumScore ", Integer.toString(user_each.getSumScore()));
                user_each.setSumScore(user_each.getSumScore() + selectedScore);
                user_each.setCount(user_each.getCount() + 1);
                user_each.setAvgScore(user_each.getSumScore() / user_each.getCount());
                //Log.i("변경된 SumScore ", Integer.toString(user_each.getSumScore()));

                myRefUser.child(childSnapshot.getKey()).child("sumScore").setValue(user_each.getSumScore());
                myRefUser.child(childSnapshot.getKey()).child("count").setValue(user_each.getCount());
                myRefUser.child(childSnapshot.getKey()).child("avgScore").setValue(user_each.getAvgScore());

                break;
            }

        }

    }

    void checkChangeable() {
        if (iDday >= 1 && (is_accepted.equals("null"))) {//하루 이전이고 예약 미처리 경우, 수정 가능,취소 불가
            modify.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);

        } else if (iDday >= 1 && (is_accepted.equals("1"))) {//하루 이전이고 승인일 경우 수정 불가, 취소 가능
            cancel.setVisibility(View.VISIBLE);
        } else if (iDday == 0 && is_accepted.equals("null")) {//하루이내이고 미처리시 수정가능, 취소 가능
            modify.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        }
    }

    public int countDday(int myear, int mmonth, int mday) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar todaCal = Calendar.getInstance();
        Calendar ddayCal = Calendar.getInstance();

        mmonth -= 1;
        ddayCal.set(myear, mmonth, mday);

        long today = todaCal.getTimeInMillis() / 86400000;
        long dday = ddayCal.getTimeInMillis() / 86400000;

        long count = dday - today;

        return (int) count;
    }

    private void initDatabase(){
        database = FirebaseDatabase.getInstance();
        myRefReservation = database.getReference("Reservation/");
        myRefUser = database.getReference("User_info/");
    }

    private void initData() {
        intent = getIntent();
        str_nickname = intent.getStringExtra("nickname");
        str_restaurantName = intent.getStringExtra("restaurant_name");
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        hour = intent.getIntExtra("hour", 0);
        minute = intent.getIntExtra("minute", 0);
        cover = intent.getIntExtra("covers", 0);
        key = intent.getStringExtra("key");
        is_accepted = intent.getStringExtra("is_accepted");//예약 승인여부
        is_confirm = intent.getStringExtra("is_confirm");//방문 확인 여부
        type = intent.getStringExtra("type");
        isOwner = intent.getStringExtra("isOwner");
        scoredByCustomer = intent.getStringExtra("scoredByCustomer");
        scoredByRestaurant = intent.getStringExtra("scoredByRestaurant");
        r_id = intent.getStringExtra("r_id");

        iDday = countDday(year, month, day);
    };

    private void initView(){
        r_nickname = (TextView) findViewById(R.id.r_nickname);
        restaurantName = (TextView) findViewById(R.id.restaurant_name);
        r_date = (TextView) findViewById(R.id.r_date);
        r_time = (TextView) findViewById(R.id.r_time);
        covers = (TextView) findViewById(R.id.covers);
        status = (TextView) findViewById(R.id.status);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
        modify = (Button) findViewById(R.id.modify);

        cancel.setVisibility(View.GONE);
        modify.setVisibility(View.GONE);

        restaurantName.setText(str_restaurantName+"("+type+")");
        r_nickname.setText(str_nickname);
        r_date.setText(year + "년 " + month + "월 " + day + "일");
        r_time.setText(hour + "시 " + minute + "분");
        covers.setText(cover + "명");

        dialogView = getLayoutInflater().inflate(R.layout.score_popup, null);
        scoreSpinner = (Spinner) dialogView.findViewById(R.id.scoreSpinner);

        builder = new android.app.AlertDialog.Builder(DetailsOfRSRV.this);
        builder.setTitle("평점 입력");
    };

    View.OnClickListener submitListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    View.OnClickListener modifyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //need to implement
            Intent modify = new Intent(getApplicationContext(), ModifyActivity.class);
            modify.putExtra("reservation_name",str_restaurantName);
            modify.putExtra("nickname",str_nickname);
            modify.putExtra("year",year);
            modify.putExtra("month",month);
            modify.putExtra("day",day);
            modify.putExtra("hour",hour);
            modify.putExtra("minute",minute);
            modify.putExtra("covers",cover);
            modify.putExtra("key",key);
            modify.putExtra("type",type);
            startActivity(modify);
        }
    };

    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //need to implement
            //데이터베이스에서 해당 내용을 삭제하기보다는 상태를 취소로 하는게 좋을듯
            AlertDialog.Builder alert;
            alert = new AlertDialog.Builder(DetailsOfRSRV.this);
            alert.setTitle("예약 삭제");
            alert.setMessage("예약을 삭제하시겠습니까?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myRefReservation.child(key).child("is_accepted").setValue("-1");
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();
        }
    };

    AdapterView.OnItemSelectedListener scoreSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedScore = Integer.parseInt(scoreSpinner.getSelectedItem().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


}




