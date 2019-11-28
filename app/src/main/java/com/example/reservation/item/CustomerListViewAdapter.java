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
import androidx.recyclerview.widget.RecyclerView;

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
        int iDday = countDday(listViewItem.getYear(), listViewItem.getMonth(), listViewItem.getDay());

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (iDday < 0) {
                v = inflater.inflate(R.layout.customer_past_listview_item, parent, false);

            } else {
                v = inflater.inflate(R.layout.customer_reserved_listview_item, parent, false);
            }

            //v = inflater.inflate(R.layout.customer_reserved_listview_item, parent, false);
            ViewHolder holder = new ViewHolder(v);

            v.setTag(holder);

        }


        if (listViewItem != null) {
            final ViewHolder holder = (ViewHolder) v.getTag();
            holder.restName.setText(listViewItem.getRestaurant_name());
            holder.r_date.setText(listViewItem.getR_date());
            holder.covers.setText(listViewItem.getCovers() + "명");


            if (iDday < 0) {
                //과거내역인 경우
                if (listViewItem.getIs_accepted() == "1" && listViewItem.getIs_confirm() == "null") {
                    holder.accept.setText("<미확인>");
                } else if (listViewItem.getIs_accepted() == "1" && listViewItem.getIs_confirm() == "1") {
                    holder.confirm.setText("<방문>");
                    holder.confirm.setVisibility(View.GONE);
                    holder.score.setVisibility(View.VISIBLE);

                    holder.submit.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {
                            holder.score.setHint("평점");
                            //평점 넣어주어야함 gtr DB에 넣어주어야함
                        }
                    });
                } else if (listViewItem.getIs_accepted() == "1" && listViewItem.getIs_confirm() == "0") {
                    holder.accept.setText("<미방문>");
                } else {
                    holder.accept.setText("<거절>");
                }
            } else {
                //미래에 대한 것
                if (listViewItem.getIs_accepted() == "null") {
                    holder.accept.setText("<처리중>");
                } else if (listViewItem.getIs_accepted() == "1") {
                    holder.accept.setText("<승인>");
                } else if (listViewItem.getIs_accepted() == "0") {
                    holder.accept.setText("<거절>");
                }
            }


        }

        return v;
        //return convertView;
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

    public int countDday(int myear, int mmonth, int mday) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar todaCal = Calendar.getInstance();
        Calendar ddayCal = Calendar.getInstance();

        mmonth -= 1;
        ddayCal.set(myear, mmonth, mday);

        long today = todaCal.getTimeInMillis() / 86400000;
        long dday = ddayCal.getTimeInMillis() / 86400000;

        long count = dday - today;

        return (int) count;
    }

    public class ViewHolder {
        final TextView restName;
        final TextView r_date;
        final TextView covers;
        final TextView accept;
        final TextView confirm;
        final EditText score;
        final Button submit;

        public ViewHolder(View root) {
            restName = (TextView) root.findViewById(R.id.restaurant_name);
            r_date = (TextView) root.findViewById(R.id.r_date);
            covers = (TextView) root.findViewById(R.id.covers);

            accept = (TextView) root.findViewById(R.id.accept);
            confirm = (TextView) root.findViewById(R.id.confirm);
            score = (EditText) root.findViewById(R.id.score);
            submit = (Button) root.findViewById(R.id.submit);
        }
    }

}
