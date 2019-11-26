package com.example.reservation.ui.statistics;

import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {
    private LineView lineView;
    private StatisticsViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(StatisticsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        final TextView textView = root.findViewById(R.id.text_statistics);
        sendViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        lineView = (LineView) root.findViewById(R.id.line_view);

        ArrayList<Integer> al1 = new ArrayList<>();
        ArrayList<Integer> al2 = new ArrayList<>();
        ArrayList<Integer> al3 = new ArrayList<>();
        ArrayList<String> bTL = new ArrayList<>();

        for(int i = 0; i < 30; i ++) {
            al1.add(i);
            al2.add(i + 1);
            al3.add(i + 2);
            bTL.add("Num" + (i + 1));
        }

        ArrayList<ArrayList<Integer>> dataList = new ArrayList<>();
        dataList.add(al1);
        dataList.add(al2);
        dataList.add(al3);

        System.out.println(bTL.size());
        System.out.println(dataList.size());

        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);
        lineView.setColorArray(new int[] {Color.GRAY, Color.RED, Color.BLUE});
        lineView.setBottomTextList(bTL);
        lineView.setDataList(dataList);

        return root;
    }
}