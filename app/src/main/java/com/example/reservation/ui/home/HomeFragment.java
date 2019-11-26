package com.example.reservation.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.reservation.MainActivity;
import com.example.reservation.OwnerHomeActivity;
import com.example.reservation.R;
import com.example.reservation.Reservation;
import com.example.reservation.User;
import com.example.reservation.item.ListViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    private Button refreshButton;
    private HomeViewModel homeViewModel;
    private ListViewAdapter adapter;
    private String restaurant1 ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("User_info/");

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef2 = database1.getReference("Reservation/");

        Intent intent=getActivity().getIntent();

        final String id1= intent.getExtras().getString("id");
        final String key1 = intent.getExtras().getString("key");

        //id값으로 가게이름 가져오기
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    User user_each = childSnapshot.getValue(User.class);
                    if(user_each.getId1().equals(id1)){
                        restaurant1 =user_each.getRestaurant_name();
                        Log.i("가게이름", restaurant1);
                        break;

                    } else{
                        continue;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // 임시 데이터
        ListView listview;
        adapter = new ListViewAdapter(getActivity());

        listview = (ListView) root.findViewById(R.id.ListView);
        listview.setAdapter(adapter);


        // adapter.addItem 으로 db 에 있는 예약 내역 저장
        // 닉네임 년 월 일 시간 분 도착시간 인원

//        adapter.addItem("닉네임", 2019, 11, 9, 5, 7, 3);
//        adapter.addItem("닉네임", 2019, 11, 10, 5, 7, 3);
//        adapter.addItem("닉네임", 2019, 11, 11, 5, 7, 3);
//        adapter.addItem("닉네임", 2019, 11, 12, 5, 7, 3);
//        adapter.addItem("닉네임", 2019, 11, 13, 5, 7, 3);
//        adapter.addItem("닉네임", 2019, 11, 14, 5, 7, 3);
//        adapter.addItem("닉네임", 2019, 11, 15, 5, 7, 3);
//        adapter.addItem("닉네임", 2019, 11, 16, 5, 7, 3);
//        adapter.addItem("닉네임", 2019, 11, 27, 5, 7, 3);


        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                view_my_list(dataSnapshot, restaurant1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });


        refreshButton = (Button) root.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "새로고침 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void view_my_list(@NonNull DataSnapshot dataSnapshot, String restaurant) {//자기 자신의 레스토랑 이름

        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            //String key = childSnapshot.getKey();
            Reservation reservation_each= childSnapshot.getValue(Reservation.class);
            if (reservation_each.getRestaurant_name().equals(restaurant)) { //자신의 레스토랑 이름과 일치하면 addItem
                Log.i("닉네임:", reservation_each.getNickname());
                Log.i("연도", Integer.toString(reservation_each.getYear()));

                adapter.addItem(reservation_each.getNickname(), reservation_each.getYear(), reservation_each.getMonth(), reservation_each.getDay(), reservation_each.getHour(), reservation_each.getMinute(), reservation_each.getCovers());
            } else {
                continue;
            }
        }
    }
}