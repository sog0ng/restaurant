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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.reservation.MainActivity;
import com.example.reservation.R;
import com.example.reservation.Reservation2Activity;
import com.example.reservation.User;
import com.example.reservation.item.ListViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomerHomeFragment extends Fragment {
    //private SearchView searchView;
    private HomeViewModel homeViewModel;
    private Button refreshButton;
    private SearchView searchView;
    private ListView listview;
    ArrayList<String> list;
    ArrayList<String> openTimeList;
    ArrayList<String> closeTimeList;

    public ArrayAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef2 = database.getReference("User_info/");

        list = new ArrayList<>();
        openTimeList = new ArrayList<>();
        closeTimeList = new ArrayList<>();

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_customer_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });

        // 리스트 검색 코드 작성
        //리스트가 뜨는데 돋보기 눌러야뜸
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                update_list(dataSnapshot);
                setListViewHeightBasedOnChildren(listview);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listview = (ListView) root.findViewById(R.id.customer_listView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        Intent intent=getActivity().getIntent();
        final String id2= intent.getExtras().getString("id");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){//리스트 클릭시
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){ //리스트뷰 클릭시 해당하는 레스토랑 정보로 감
                Intent reservation2 = new Intent(getActivity(), Reservation2Activity.class);
                reservation2.putExtra("restaurant_name", list.get(position));
                reservation2.putExtra("openTime", openTimeList.get(position));
                reservation2.putExtra("closeTime", closeTimeList.get(position));
                reservation2.putExtra("id", id2);
                startActivity(reservation2);
            }
        });

        refreshButton = (Button) root.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(),"새로고침 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        giveFocusToSearchView(root);

        return root;
    }

    private void giveFocusToSearchView(View root) {
        searchView = (SearchView) root.findViewById(R.id.searchView);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
    }

    public void update_list(@NonNull DataSnapshot dataSnapshot) {
        list.clear();
        for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
            String key=childSnapshot.getKey();
            User user_each=childSnapshot.getValue(User.class);
            // list.add(user_each.getRestaurant_name());
            if(!user_each.getRestaurant_name().equals("null")) { //가게 이름이 null이 아니면 list에 추가
                Log.i("가게이름:", user_each.getRestaurant_name());
                Log.i("이름", key);
                list.add(user_each.getRestaurant_name());
                openTimeList.add(user_each.getOpen());
                closeTimeList.add(user_each.getClose());
            }else {
                continue;
            }
        }
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