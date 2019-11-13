package com.example.reservation.item;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.reservation.MainActivity;
import com.example.reservation.OwnerHomeActivity;
import com.example.reservation.R;
import com.example.reservation.ReservationActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    Activity activity;
    public ListViewAdapter(Activity activity) {
        this.activity = activity;
    }
    public ListViewAdapter() {

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
///        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(iDday < 0 ) {
                convertView = inflater.inflate(R.layout.owner_past_listview_item, parent, false);
            } else if(listViewItem.isAccept()) {
                convertView = inflater.inflate(R.layout.owner_accepted_listview_item, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.owner_unaccepted_listview_item, parent, false);
            }

//        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView nickname = (TextView) convertView.findViewById(R.id.nickname);
        TextView r_date = (TextView) convertView.findViewById(R.id.r_date);
        TextView arrival_time = (TextView) convertView.findViewById(R.id.time);
        TextView covers = (TextView) convertView.findViewById(R.id.covers);
        TextView dDay = (TextView) convertView.findViewById(R.id.dDay);
        Button accept = (Button) convertView.findViewById(R.id.accept);
        Button reject = (Button) convertView.findViewById(R.id.reject);

        if(iDday < 0) {
            // paste
            title.setText("과거 내역");
            nickname.setText(listViewItem.getNickname());
            r_date.setText(listViewItem.getR_date());
            arrival_time.setText(listViewItem.getArrival_time());
            covers.setText(Integer.toString(listViewItem.getCovers()) + "명");
            Button submit = (Button) convertView.findViewById(R.id.submit);

            submit.setOnClickListener(new Button.OnClickListener() {
               public void onClick(View v) {
                   // db 에 평점 전달.
               }
            });

        } else if(listViewItem.isAccept()) {
            // accepted
            title.setText("승인 예약");
            nickname.setText(listViewItem.getNickname());
            r_date.setText(listViewItem.getR_date());
            covers.setText(Integer.toString(listViewItem.getCovers()) + "명");
            dDay.setText(Integer.toString(iDday));
        } else {
            // unaccepted
            title.setText("미승인 예약");
            nickname.setText(listViewItem.getNickname());
            r_date.setText(listViewItem.getR_date());
            covers.setText(Integer.toString(listViewItem.getCovers()) + "명");
            accept.setText("승인");
            reject.setText("거절");

            accept.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    // 승인 팝업 띄움
                    showAcceptPopup();
                }
            });

            reject.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    // 거절 팝업 띄움
                    showRejectPopup();
                }
            });


        }

        return convertView;
    }

    void showAcceptPopup(){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        alert.setTitle("예약 승인");
        alert.setMessage("예약을 승인하시겠습니까?");

        alert.setView(layout);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // db에 승인된 예약 추가
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    void showRejectPopup() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        alert.setTitle("예약 거절");
        alert.setMessage("예약을 거절하시겠습니까?");

        alert.setView(layout);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // db에 거절된 예약 추가
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }



    public void addItem(String nickname, int year, int month, int day, int hour,int minute, String arrival_time, int covers){
        ListViewItem item = new ListViewItem();

        item.setNickname(nickname);
        item.setYear(year);
        item.setMonth(month);
        item.setDay(day);
        item.setArrival_time(arrival_time);
        item.setCovers(covers);
        item.setR_date(month + "월" + day + "일" + hour + "시" + minute + "분");

        listViewItemList.add(0, item);
    }

    public int countDday(int myear, int mmonth, int mday) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar todaCal = Calendar.getInstance();
        Calendar ddayCal = Calendar.getInstance();

        mmonth -= 1;
        ddayCal.set(myear, mmonth, mday);

        long today = todaCal.getTimeInMillis()/86400000;
        long dday = ddayCal.getTimeInMillis()/86400000;

        long count = dday - today;

        return (int) count;
    }
}
