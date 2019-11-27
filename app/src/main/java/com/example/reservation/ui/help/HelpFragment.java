package com.example.reservation.ui.help;

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

public class HelpFragment extends Fragment {
    private LineView lineView;
    private HelpViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(HelpViewModel.class);
        View root = inflater.inflate(R.layout.fragment_help, container, false);




        return root;
    }
}