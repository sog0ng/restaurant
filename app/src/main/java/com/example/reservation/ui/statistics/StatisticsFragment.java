package com.example.reservation.ui.statistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import im.dacer.androidcharts.LineView;

import com.example.reservation.R;
import com.example.reservation.Reservation;
import com.example.reservation.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class StatisticsFragment extends Fragment {
    private LineView lineView;
    private StatisticsViewModel sendViewModel;
    private String restaurant1 = "";
    int this_week;
    int last_week;
    int last2_week;
    int last3_week;
    Calendar today;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        today = Calendar.getInstance(Locale.KOREA);

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("User_info/");

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef2 = database1.getReference("Reservation/");

        sendViewModel =
                ViewModelProviders.of(this).get(StatisticsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        final TextView textView = root.findViewById(R.id.text_statistics);
        sendViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // textView.setText(s);
            }
        });

        lineView = (LineView) root.findViewById(R.id.line_view);

        Intent intent = getActivity().getIntent();

        final String id1 = intent.getExtras().getString("id");

        //id값으로 가게이름 가져오기
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMyRestaurant(dataSnapshot, id1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countWeeklyStatistics(dataSnapshot, restaurant1);
                ArrayList<Integer> al1 = new ArrayList<>();
                ArrayList<String> bTL = new ArrayList<>();

                if(today.get(Calendar.DATE) > 21) { // 3주전
                    al1.add(last3_week);
                    bTL.add(today.get(Calendar.MONTH) + 1 +"월" + (today.get(Calendar.DATE) - 21)+"일");
                } if(today.get(Calendar.DATE) > 14) {   // 2주전
                    al1.add(last2_week);
                    bTL.add(today.get(Calendar.MONTH) + 1 +"월" + (today.get(Calendar.DATE) - 14)+"일");
                } if(today.get(Calendar.DATE) > 7) {    // 1주전
                    al1.add(last_week);
                    bTL.add(today.get(Calendar.MONTH) + 1 +"월" + (today.get(Calendar.DATE) - 7)+"일");
                }
                // 이번주
                bTL.add(today.get(Calendar.MONTH) + 1 +"월" + (today.get(Calendar.DATE))+"일");
                al1.add(this_week);

                Log.i("this_week: ", Integer.toString(this_week));
                Log.i("last_week: ", Integer.toString(last_week));
                Log.i("last2_week: ", Integer.toString(last2_week));
                Log.i("last3_week: ", Integer.toString(last3_week));

                ArrayList<ArrayList<Integer>> dataList = new ArrayList<>();
                dataList.add(al1);

                System.out.println(bTL.size());
                System.out.println(dataList.size());

                lineView.setDrawDotLine(true);
                lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);
                lineView.setColorArray(new int[] {Color.GRAY, Color.RED, Color.BLUE});
                lineView.setBottomTextList(bTL);
                lineView.setDataList(dataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }

    private void getMyRestaurant(@NonNull DataSnapshot dataSnapshot, String myId) {
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            User user_each = childSnapshot.getValue(User.class);
            if (user_each.getId1().equals(myId)) {//intent로 받은 값이랑 반복문을 통해서 확인한 아이디 값이랑 같으면
                restaurant1 = user_each.getRestaurant_name();//어플 사용하는 사장님의 가게 이름이다
  //              Log.i("가게이름", restaurant1);
                //Toast.makeText(getContext(), id1+"\n"+key1+"\nrestaurant1: "+restaurant1,Toast.LENGTH_LONG).show();
                break;
            } else {
                continue;
            }
        }
    }

    // 이번주~3주전 예약수 카운팅
    private void countWeeklyStatistics(@NonNull DataSnapshot dataSnapshot, String myRestaurant) {
        this_week = 0;
        last_week = 0;
        last2_week = 0;
        last3_week = 0;

        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            Reservation reservation_each = childSnapshot.getValue(Reservation.class);
            if (reservation_each.getRestaurant_name().equals(myRestaurant) && checkInMonth(reservation_each)) { //레스토랑 이름 동일하고 오늘날짜에 해당하는 예약들에 대해서만 추가
                if(reservation_each.getDay() - today.get(Calendar.DATE) < 7) {  // 이번주
                    this_week = this_week + 1;
                } else if(reservation_each.getDay() - today.get(Calendar.DATE) < 14) {  // 지난주
                    last_week = last_week + 1;
                } else if(reservation_each.getDay() - today.get(Calendar.DATE) < 21) {  // 2주전
                    last2_week = last2_week +1;
                } else if(reservation_each.getDay() - today.get(Calendar.DATE) < 28) {  // 3주전
                    last3_week = last3_week +1;
                }

            } else {
                continue;
            }
        }
    }

    private boolean checkInMonth(Reservation reservation) {
        if( reservation.getYear()==today.get(Calendar.YEAR) && reservation.getMonth() - (today.get(Calendar.MONTH) + 1) >= -1 && reservation.getMonth() - (today.get(Calendar.MONTH) + 1) <= 1 ) {  // 오늘에서 1달 전후이면
            return true;
        } else {
            return false;
        }
    }

}