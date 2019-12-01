package com.example.reservation.ui.query;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.reservation.DetailsOfRSRV;
import com.example.reservation.R;
import com.example.reservation.Reservation;
import com.example.reservation.User;
import com.example.reservation.item.CustomerListViewAdapter;
import com.example.reservation.item.ListViewAdapter;
import com.example.reservation.item.ListViewItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerQueryFragment extends Fragment {
    public CustomerListViewAdapter adapter;
    ArrayList<String> list;
    ListView listview;
    private Button refreshButton;
    private QueryViewModel queryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        queryViewModel =
                ViewModelProviders.of(this).get(QueryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_query_customer, container, false);//fragment_query_customer.xml

        list = new ArrayList<>();

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database1.getReference("Reservation/");

        Intent intent = getActivity().getIntent();
        final String id = intent.getExtras().getString("id");
        final String key = intent.getExtras().getString("key");
        final String isOwner = intent.getStringExtra("isOwner");

        Log.i("id야 나와라", id);
        Log.i("키야 나와라", key);

        listview = (ListView) root.findViewById(R.id.ListViewCustomer); //fragment_query_customer.xml의 리스트뷰
        adapter = new CustomerListViewAdapter(getActivity());//CustomerListViewAdapter 인스턴스인 adapter
        listview.setAdapter(adapter);//어댑터 지정해주고


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewMyReserve(dataSnapshot, id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem) adapter.getItem(position);
                Intent detailsOfRSRV = new Intent(getActivity(), DetailsOfRSRV.class);
                detailsOfRSRV.putExtra("id", id);
                detailsOfRSRV.putExtra("r_id", item.getR_id());
                detailsOfRSRV.putExtra("nickname", item.getNickname());
                detailsOfRSRV.putExtra("restaurant_name", item.getRestaurant_name());
                detailsOfRSRV.putExtra("key", item.getKey());
                detailsOfRSRV.putExtra("year", item.getYear());
                detailsOfRSRV.putExtra("month", item.getMonth());
                detailsOfRSRV.putExtra("day", item.getDay());
                detailsOfRSRV.putExtra("hour", item.getHour());
                detailsOfRSRV.putExtra("minute", item.getMinute());
                detailsOfRSRV.putExtra("covers", item.getCovers());
                detailsOfRSRV.putExtra("type", item.getType2());
                detailsOfRSRV.putExtra("is_accepted", item.getIs_accepted());//예약 승인 여부
                detailsOfRSRV.putExtra("is_confirm", item.getIs_confirm());//방문 여부
                detailsOfRSRV.putExtra("scoredByCustomer", item.getScoredByCustomer());
                detailsOfRSRV.putExtra("scoredByRestaurant", item.getScoredByRestaurant());
                detailsOfRSRV.putExtra("isOwner", isOwner);
                startActivity(detailsOfRSRV);
            }
        });

        return root;
    }

    private void viewMyReserve(@NonNull DataSnapshot dataSnapshot, String myId) {
        adapter.clear();
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            Reservation reservation_each = childSnapshot.getValue(Reservation.class);
            if (reservation_each.getR_id().equals(myId)) {
                //아이템 하나하나를 넣어준다

                adapter.addItemC(childSnapshot.getKey(),
                        reservation_each.getR_id(), reservation_each.getOwner_id(),
                        reservation_each.getRestaurant_name(), reservation_each.getNickname(),
                        reservation_each.getYear(), reservation_each.getMonth(),
                        reservation_each.getDay(), reservation_each.getHour(), reservation_each.getMinute(),
                        reservation_each.getCovers(), reservation_each.getIs_accepted(), reservation_each.getIs_confirm(),reservation_each.getType());
            } else {
                continue;
            }
        }
        if (adapter.isEmpty()) {
            Toast.makeText(getContext(), "예약 내역이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
        }
        adapter.sort();//정렬
        adapter.notifyDataSetChanged();//새로고침
    }


}