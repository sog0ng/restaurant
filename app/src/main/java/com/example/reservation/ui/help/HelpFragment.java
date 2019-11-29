package com.example.reservation.ui.help;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import im.dacer.androidcharts.LineView;

import com.example.reservation.R;
import com.example.reservation.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HelpFragment extends Fragment {
    private LineView lineView;
    private HelpViewModel sendViewModel;
    TextView textView1;
    String is_owner;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(HelpViewModel.class);
        View root = inflater.inflate(R.layout.fragment_help, container, false);

        textView1 = (TextView) root.findViewById(R.id.textView1);

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("User_info/");

        Intent intent = getActivity().getIntent();

        final String id1 = intent.getExtras().getString("id");
        final String key1 = intent.getExtras().getString("key");

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {//id값으로 정보를 찾음
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMyRestaurantHelp(dataSnapshot, id1);
                if(is_owner.equals("0")){
                    textView1.setText("  반갑습니다." +
                            " \n 사장님 계정입니다. 좌측 네비게이션 아이콘을 클릭하면 다양한 기능을 이용할 수 있습니다."+
                            "\n 1. 홈화면 " +
                            "\n 오늘의 예약을 승인 또는 거절 할 수 있습니다."+
                            "\n 2. 계정 정보" +
                            "\n 계정의 비밀번호, 핸드폰 번호를 변경할 수 있습니다."+
                            "\n 3. 식당 관리" +
                            "\n 가게의 운영시간을 변경할 수 있습니다."+
                            "\n 4. 통계" +
                            "\n 한달동안의 요일별 예약수를 볼 수 있습니다. ");
                }else{
                    textView1.setText("반갑습니다."+
                            "\n손님 계정입니다. 좌측 네비게이션 아이콘을 클릭하면 다양한 기능을 이용할 수있습니다." +
                            "\n 1. 홈화면" +
                            "\n 가게 리스트와 가게를 검색 할 수 있고 해당 가게를 클릭시 예약을 할 수 있습니다."+
                            "\n 2. 계정 정보" +
                            "\n 계정의 비밀번호, 핸드폰 번호를 변경할 수 있습니다."+
                            "\n 3.예약 내역 조회" +
                            "\n 자신의 예약이 처리 중인 상태일 경우 자신의 예약 내역과 예약 변경 및 취소를 할 수 있습니다."+
                            "방문했을 경우 가게에 대한 평점을 남길 수 있습니다.");
                }



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return root;
    }

    //기존의 값들을 받아옴
    private void getMyRestaurantHelp(@NonNull DataSnapshot dataSnapshot, String myId) {
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            User user_each = childSnapshot.getValue(User.class);
            if (user_each.getId1().equals(myId)) {
                is_owner = user_each.getIs_owner();
                Log.i("사장이니???", is_owner);
                break;
            } else {
                continue;
            }
        }
    }
}