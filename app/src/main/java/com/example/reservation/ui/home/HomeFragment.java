package com.example.reservation.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.reservation.MainActivity;
import com.example.reservation.OwnerHomeActivity;
import com.example.reservation.R;
import com.example.reservation.item.ListViewAdapter;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ListViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // 임시 데이터
        ListView listview;
        adapter = new ListViewAdapter(getActivity());


        listview = (ListView)root.findViewById(R.id.ListView);
        listview.setAdapter(adapter);

        // adapter.addItem 으로 db 에 있는 예약 내역 저장
        // 닉네임 년 월 일 시간 분 도착시간 인원
        adapter.addItem("닉네임", 2019, 11, 9, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 10, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 11, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 12, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 13, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 14, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 15, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 16, 5, 7, "도착시간", 3);
        adapter.addItem("닉네임", 2019, 11, 27, 5, 7, "도착시간", 3);

        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.logout:
                btn_logout();
                return true;
            case R.id.refresh:
                //리스트 업데이트 함수
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"새로고침 되었습니다.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btn_logout() {
        new AlertDialog.Builder(getActivity())
                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }

}