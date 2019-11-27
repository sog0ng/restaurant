package com.example.reservation.item;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.reservation.CustomerHomeActivity;
import com.example.reservation.OwnerHomeActivity;
import com.example.reservation.R;
import com.example.reservation.Reservation;
import com.example.reservation.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class CustomerListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    Activity activity;

    public CustomerListViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return listViewItemList.get(position).getType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        ListViewItem listViewItem = listViewItemList.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.customer_reserved_listview_item, parent, false);



        TextView restName = (TextView) convertView.findViewById(R.id.restaurant_name);
        TextView r_date = (TextView) convertView.findViewById(R.id.r_date);
        TextView covers = (TextView) convertView.findViewById(R.id.covers);


        restName.setText(listViewItem.getRestaurant_name());
        r_date.setText(listViewItem.getR_date());
        covers.setText(listViewItem.getCovers() + "명");

        return convertView;
    }

    public void addItem(String restaurantName ,String nickname, int year,
                        int month, int day, int hour, int minute, int covers) {
        ListViewItem item = new ListViewItem();

        item.setRestaurant_name(restaurantName);
        item.setNickname(nickname);
        item.setYear(year);
        item.setMonth(month);
        item.setDay(day);
        //item.setArrival_time(arrival_time);
        item.setCovers(covers);
        item.setR_date(month + "월" + day + "일" + hour + "시" + minute + "분");

        listViewItemList.add(0, item);
    }

    public void clear() {
        listViewItemList.clear();
    }
}
