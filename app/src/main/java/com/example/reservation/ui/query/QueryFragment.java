package com.example.reservation.ui.query;

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

import com.example.reservation.R;
import com.example.reservation.Reservation;
import com.example.reservation.User;
import com.example.reservation.item.ListViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QueryFragment extends Fragment {
    private Button refreshButton;
    private QueryViewModel queryViewModel;
    private ListViewAdapter adapter;
    String restaurant1;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        queryViewModel =
                ViewModelProviders.of(this).get(QueryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        final TextView textView = root.findViewById(R.id.text_query);

        queryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
    //            textView.setText(s);
            }
        });

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
                getMyRestaurant(dataSnapshot, id1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // final String restaurant1 = intent.getExtras().getString("restaurant_name");
        Log.i("id야 나와라",id1);
        Log.i("키야 나와라",key1);
        //Log.i("restaurant_name나와라",restaurant1);

        // 임시 데이터
        ListView listview;
        adapter = new ListViewAdapter(getActivity());

        listview = (ListView) root.findViewById(R.id.ListView);
        listview.setAdapter(adapter);


        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                view_my_list(dataSnapshot, restaurant1);//자신의 레스토랑 이름을 가지는 리스트 보여주도록
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // adapter.addItem 으로 db 에 있는 예약 내역 저장
        // 닉네임 년 월 일 시간 분 인원

        adapter.addItem("닉네임", 2019, 11, 9, 5, 7, 3);
        adapter.addItem("닉네임", 2019, 11, 10, 5, 7, 3);
        adapter.addItem("닉네임", 2019, 11, 11, 5, 7, 3);
        adapter.addItem("닉네임", 2019, 11, 12, 5, 7, 3);
        adapter.addItem("닉네임", 2019, 12, 5, 5, 7, 100);
        adapter.addItem("닉네임", 2019, 12, 6, 5, 7, 100);
        adapter.addItem("닉네임", 2019, 12, 7, 5, 7, 100);
        adapter.addItem("닉네임", 2019, 12, 8, 5, 7, 100);
        adapter.addItem("닉네임", 2019, 12, 9, 5, 7, 100);


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
    private void getMyRestaurant(@NonNull DataSnapshot dataSnapshot, String myId) {
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            User user_each = childSnapshot.getValue(User.class);
            if (user_each.getId1().equals(myId)) {//intent로 받은 값이랑 반복문을 통해서 확인한 아이디 값이랑 같으면
                restaurant1 = user_each.getRestaurant_name();//어플 사용하는 사장님의 가게 이름이다
                Log.i("가게이름", restaurant1);
                //Toast.makeText(getContext(), id1+"\n"+key1+"\nrestaurant1: "+restaurant1,Toast.LENGTH_LONG).show();
                break;
            } else {
                continue;
            }
        }
    }

    private void view_my_list(@NonNull DataSnapshot dataSnapshot, String restaurant) {//자기 자신의 레스토랑 이름
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            //String key = childSnapshot.getKey();
            Reservation res_each = childSnapshot.getValue(Reservation.class);
            if (res_each.getRestaurant_name().equals(restaurant)) { //자신의 레스토랑 이름과 일치하면 addItem
                Log.i("닉네임:", res_each.getNickname());
                Log.i("연도", Integer.toString(res_each.getYear()));
                Log.i("월", Integer.toString(res_each.getMonth()));
                Log.i("일", Integer.toString(res_each.getDay()));
                Log.i("시", Integer.toString(res_each.getHour()));
                Log.i("분", Integer.toString(res_each.getMinute()));
                Log.i("인원", Integer.toString(res_each.getCovers()));
                adapter.addItem(res_each.getNickname(), res_each.getYear(), res_each.getMonth(), res_each.getDay(), res_each.getHour(), res_each.getMinute(), res_each.getCovers());
            } else {
                continue;
            }
        }
    }
}