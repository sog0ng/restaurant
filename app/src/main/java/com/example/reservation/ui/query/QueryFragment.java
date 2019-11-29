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
import android.widget.AdapterView;
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

import com.example.reservation.DetailsOfRSRV;
import com.example.reservation.R;
import com.example.reservation.Reservation;
import com.example.reservation.Reservation2Activity;
import com.example.reservation.User;
import com.example.reservation.item.ListViewAdapter;
import com.example.reservation.item.ListViewItem;
import com.example.reservation.ui.home.HomeViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class QueryFragment extends Fragment {
    String restaurant1 = "null";
    ListView listview;
    boolean isCustomer;
    private Button refreshButton;
    private QueryViewModel queryViewModel;
    private ListViewAdapter l_adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        queryViewModel =
                ViewModelProviders.of(this).get(QueryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_query, container, false);

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("User_info/");

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef2 = database2.getReference("Reservation/");

        Intent intent = getActivity().getIntent();
        final String id1 = intent.getExtras().getString("id");
        final String key1 = intent.getExtras().getString("key");

        //key값으로 가게이름 가져오기
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (childSnapshot.getKey().equals(key1)) {
                        restaurant1 = childSnapshot.getValue(User.class).getRestaurant_name();
                        break;
                    } else {
                        continue;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // final String restaurant1 = intent.getExtras().getString("restaurant_name");
        Log.i("id야 나와라", id1);
        Log.i("키야 나와라", key1);
        Log.i("restaurant_name나와라", restaurant1);

        listview = (ListView) root.findViewById(R.id.ListView_query); //fragment_query.xml의 리스트뷰
        l_adapter = new ListViewAdapter(getActivity());

        //final ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        listview.setAdapter(l_adapter);//어댑터 지정해주고


        //레스토랑 이름으로 자신의 리스트 addItem
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewMyList(dataSnapshot, restaurant1);//이건 전체 다 보여주는 경우
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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem) l_adapter.getItem(position);
                Intent detailsOfRSRV = new Intent(getActivity(), DetailsOfRSRV.class);
                detailsOfRSRV.putExtra("id", id);

                detailsOfRSRV.putExtra("nickname", item.getNickname());
                detailsOfRSRV.putExtra("restaurant_name", item.getRestaurant_name());

                detailsOfRSRV.putExtra("year", item.getYear());
                detailsOfRSRV.putExtra("month", item.getMonth());
                detailsOfRSRV.putExtra("day", item.getDay());
                detailsOfRSRV.putExtra("hour", item.getHour());
                detailsOfRSRV.putExtra("minute", item.getMinute());
                detailsOfRSRV.putExtra("covers", item.getCovers());

                detailsOfRSRV.putExtra("is_accepted", item.getIs_accepted());//예약 승인 여부
                detailsOfRSRV.putExtra("is_confirm", item.getIs_confirm());//방문 여부

                System.out.println(item.getNickname());
                startActivity(detailsOfRSRV);
            }
        });

        return root;
    }

    private void viewMyList(@NonNull DataSnapshot dataSnapshot, String myRestaurant) {//자기 자신의 레스토랑 이름
        l_adapter.clear();
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            Reservation reservation_each = childSnapshot.getValue(Reservation.class);

            if (reservation_each.getRestaurant_name().equals(myRestaurant)) { //자신의 레스토랑 이름과 일치하면 addItem

                Log.i("닉네임:", reservation_each.getNickname());
                Log.i("연도", Integer.toString(reservation_each.getYear()));
                l_adapter.addItem(childSnapshot.getKey(), reservation_each.getNickname(),
                        reservation_each.getYear(), reservation_each.getMonth(),
                        reservation_each.getDay(), reservation_each.getHour(),
                        reservation_each.getMinute(), reservation_each.getCovers(),
                        reservation_each.getIs_accepted(), reservation_each.getIs_confirm());

                //Toast.makeText(getContext(), reservation_each.getNickname()+"\nrestaurant1: "+myRestaurant,Toast.LENGTH_LONG).show();
            } else {
                continue;
            }
        }
        //과거 예약내역 조회를 위한 임시데이터
        //l_adapter.addItem("key1", "징징이", 2000, 1, 1, 1, 00, 3);
        //l_adapter.addItem("key2", "집게사장", 2000, 5, 5, 5, 50, 2);
        if (l_adapter.isEmpty()) {
            Toast.makeText(getContext(), "예약 내역이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
        }
        l_adapter.sort();//시간순 정렬
        l_adapter.notifyDataSetChanged();//새로고침
    }

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