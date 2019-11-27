package com.example.reservation.item;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    Activity activity;
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

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
        TextView accept = (TextView) convertView.findViewById(R.id.accept);
        TextView confirm = (TextView) convertView.findViewById(R.id.confirm);

        EditText score = (EditText) convertView.findViewById(R.id.score);
        Button submit = (Button) convertView.findViewById(R.id.submit);


        restName.setText(listViewItem.getRestaurant_name());
        r_date.setText(listViewItem.getR_date());
        covers.setText(listViewItem.getCovers() + "명");

        if (listViewItem.getIs_accepted() == "1") {
            accept.setText("<승인>");
        } if(listViewItem.getIs_accepted() == "0"){
            accept.setText("<거절>");
        }
        if (listViewItem.getIs_confirm() == "1") {
            confirm.setText("<방문>");
        } if (listViewItem.getIs_confirm() == "0"){
            confirm.setText("<미방문>");
        }

        submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //평점 입력
                //score.setText();
            }
        });


        return convertView;
    }

    public void addItem(String key, String restaurant_name, String nickname, int year,

                        int month, int day, int hour, int minute, int covers) {
        ListViewItem item = new ListViewItem();
        item.setKey(key);
        item.setRestaurant_name(restaurant_name);
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

    public void sort() {
        Collections.sort(listViewItemList, new Comparator<ListViewItem>() {
            @Override
            public int compare(ListViewItem o1, ListViewItem o2) {
                int ret = 0;

                if (o1.getYear() < o2.getYear()) {
                    ret = -1;
                } else if (o1.getYear() == o2.getYear()) {
                    if (o1.getMonth() < o2.getMonth()) {
                        ret = -1;
                    } else if (o1.getMonth() == o2.getMonth()) {
                        if (o1.getDay() < o2.getDay()) {
                            ret = -1;
                        } else if (o1.getDay() == o2.getDay()) {
                            if (o1.getHour() < o2.getHour()) {
                                ret = -1;
                            } else if (o1.getHour() == o2.getHour()) {
                                if (o1.getMinute() < o2.getMinute()) {
                                    ret = -1;
                                } else if (o1.getMinute() == o2.getMinute()) {
                                    ret = 0;
                                } else {
                                    ret = 1;
                                }
                            } else {
                                ret = 1;
                            }
                        } else {
                            ret = 1;
                        }
                    } else {
                        ret = 1;
                    }
                } else {
                    ret = 1;
                }
                return ret;
            }
        });
    }

}
