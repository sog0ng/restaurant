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
    int sun;
    int mon;
    int tue;
    int wed;
    int thu;
    int fri;
    int sat;
    Calendar today;
    ArrayList<Integer> al1;
    ArrayList<String> bTL;

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
                countWeekStatistics(dataSnapshot, restaurant1);
                al1 = new ArrayList<>();
                bTL = new ArrayList<>();

                al1.add(sun);
                al1.add(mon);
                al1.add(tue);
                al1.add(wed);
                al1.add(thu);
                al1.add(fri);
                al1.add(sat);

                bTL.add("일");
                bTL.add("월");
                bTL.add("화");
                bTL.add("수");
                bTL.add("목");
                bTL.add("금");
                bTL.add("토");

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

    // 이번달 요일 카운팅
    private void countWeekStatistics(@NonNull DataSnapshot dataSnapshot, String myRestaurant) {
        sun = 0;
        mon = 0;
        tue = 0;
        wed = 0;
        thu = 0;
        fri = 0;
        sat = 0;
        String week;
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            Reservation reservation_each = childSnapshot.getValue(Reservation.class);
            week = reservation_each.getWeek();

            if (week != null && reservation_each.getRestaurant_name().equals(myRestaurant) && checkInMonth(reservation_each)) { //레스토랑 이름 동일하고 오늘날짜에 해당하는 예약들에 대해서만 추가

                switch(week) {
                    case "Sunday" :
                        sun = sun + 1;
                    case "Monday" :
                        mon = mon + 1;
                        break;
                    case "Tuesday" :
                        tue = tue + 1;
                        break;
                    case "Wednesday" :
                        wed = wed + 1;
                        break;
                    case "Thursday" :
                        thu = thu + 1;
                        break;
                    case "Friday":
                        fri = fri + 1;
                        break;
                    case "Saturday" :
                        sat = sat + 1;
                        break;
                    default:
                        break;
                }
            } else {
                continue;
            }
        }
    }

    private boolean checkInMonth(Reservation reservation) {
        if( reservation.getYear()==today.get(Calendar.YEAR) && reservation.getMonth() == (today.get(Calendar.MONTH) + 1)) {  // 오늘에서 1달 전후이면
            Log.i("날짜 차이: ", (today.get(Calendar.MONTH) + 1 - reservation.getMonth())+"");
            return true;
        } else {
            return false;
        }
    }

}