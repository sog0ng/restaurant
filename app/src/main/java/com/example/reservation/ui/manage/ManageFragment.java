package com.example.reservation.ui.manage;

import android.content.Intent;
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

import com.example.reservation.R;
import com.example.reservation.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManageFragment extends Fragment {

    private ManageViewModel shareViewModel;
    private String restaurant1 = "";
    private String open="";
    private String close="";

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ManageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_manage, container, false);
        final TextView textView = root.findViewById(R.id.text_manage);
        shareViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // textView.setText(s);
            }
        });


        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database1.getReference("User_info/");

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference myRef2 = database1.getReference("Reservation/");

        Intent intent = getActivity().getIntent();

        final String id1 = intent.getExtras().getString("id");
        final String key1 = intent.getExtras().getString("key");

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {//id값으로 정보를 찾음
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMyRestaurantManage(dataSnapshot, id1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });






        return root;

    }
    private void getMyRestaurantManage(@NonNull DataSnapshot dataSnapshot, String myId) {
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            User user_each = childSnapshot.getValue(User.class);
            if (user_each.getId1().equals(myId)) {//intent로 받은 값이랑 반복문을 통해서 확인한 아이디 값이랑 같으면
                restaurant1 = user_each.getRestaurant_name();//어플 사용하는 사장님의 가게 이름이다
                open=user_each.getOpen();
                close=user_each.getClose();

                Log.i("가게이름", restaurant1);
                Log.i("manage_open",open);
                Log.i("manage_close",close);
                //Toast.makeText(getContext(), id1+"\n"+key1+"\nrestaurant1: "+restaurant1,Toast.LENGTH_LONG).show();
                break;
            } else {
                continue;
            }
        }
    }

}