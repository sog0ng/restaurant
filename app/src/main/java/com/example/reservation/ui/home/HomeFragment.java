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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
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
import com.example.reservation.item.ListViewItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private Button refreshButton;
    private HomeViewModel homeViewModel;
    private String restaurant1 = "";

    ArrayList<String> list;
    //public ArrayAdapter adapter;
    private ListViewAdapter l_adapter;
    ListView listview;

    boolean loadFirstToday = true, loadFirstAll = true;

    Calendar today;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);//fragment_home.xml

        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });

        list = new ArrayList<>();

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("User_info/");

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef2 = database2.getReference("Reservation/");

        Intent intent = getActivity().getIntent();

        final String id1 = intent.getExtras().getString("id");
        final String key1 = intent.getExtras().getString("key");

        Log.i("id야 나와라", id1);
        Log.i("키야 나와라", key1);

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


        listview = (ListView) root.findViewById(R.id.ListView); //fragment_home.xml의 리스트뷰
        l_adapter = new ListViewAdapter(getActivity());
        listview.setAdapter(l_adapter);//어댑터 지정해주고

        today = Calendar.getInstance(Locale.KOREA);//오늘 날짜 갱신

        //레스토랑 이름으로 자신의 리스트 addItem
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewTodayList(dataSnapshot, restaurant1);//오늘에 대한 예약만 불러온다
                setListViewHeightBasedOnChildren(listview);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refreshButton = (Button) root.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l_adapter.notifyDataSetChanged();
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

    private void viewTodayList(@NonNull DataSnapshot dataSnapshot, String myRestaurant){
        l_adapter.clear();
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            Reservation reservation_each = childSnapshot.getValue(Reservation.class);
            if (reservation_each.getRestaurant_name().equals(myRestaurant)
                    && checkToday(reservation_each)) { //레스토랑 이름 동일하고 오늘날짜에 해당하는 예약들에 대해서만 추가
                Log.i("닉네임:", reservation_each.getNickname());
                Log.i("연도", Integer.toString(reservation_each.getYear()));
                l_adapter.addItem(childSnapshot.getKey(), reservation_each.getR_id(), reservation_each.getOwner_id(),
                        reservation_each.getNickname(),
                        reservation_each.getYear(), reservation_each.getMonth(), reservation_each.getDay(),
                        reservation_each.getHour(), reservation_each.getMinute(), reservation_each.getCovers(),
                        reservation_each.getIs_accepted(), reservation_each.getIs_confirm(),reservation_each.getType(),
                        reservation_each.getScoredByRestaurant(), reservation_each.getScoredByCustomer());
                //Toast.makeText(getContext(), reservation_each.getNickname()+"\nrestaurant1: "+myRestaurant,Toast.LENGTH_LONG).show();
            } else {
                continue;
            }
        }
        if(l_adapter.isEmpty())
        {
            Toast.makeText(getContext(), "당일 예약 내역이 존재하지 않습니다.",Toast.LENGTH_LONG).show();
        }
        l_adapter.sort();//시간순 정렬
        if(loadFirstToday) {
            l_adapter.notifyDataSetChanged();//새로고침
            loadFirstToday = false;
        }
    }

    private boolean checkToday(Reservation reservation){
        boolean result=false;
        if(reservation.getYear()==today.get(Calendar.YEAR)&&(reservation.getMonth()==today.get(Calendar.MONTH)+1)&&(reservation.getDay()==today.get(Calendar.DATE))){
            result = true;
        }
        return result;
    };

    public void setListViewHeightBasedOnChildren(ListView listView) { // Get list adpter of listview;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int numberOfItems = listAdapter.getCount();
        listView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height = listView.getMeasuredHeight();
        int totalItemsHeight = 0;
        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
            View item = listAdapter.getView(itemPos, null, listView);
            item.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            totalItemsHeight += item.getMeasuredHeightAndState();
            // totalItemsHeight += height;
        }
        // Get total height of all item dividers.
        int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);
        // Set list height.
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}