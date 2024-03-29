package com.example.reservation.item;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reservation.CustomerHomeActivity;
import com.example.reservation.OwnerHomeActivity;
import com.example.reservation.R;
import com.example.reservation.Reservation;
import com.example.reservation.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CustomerListViewAdapter extends BaseAdapter {
    Activity activity;
    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    final DatabaseReference myRef2 = database2.getReference("Reservation/");

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    private int selectedScore;

    public CustomerListViewAdapter(Activity activity) {
        this.activity = activity;
    }
    public CustomerListViewAdapter(){};

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
        final ListViewItem listViewItem = listViewItemList.get(position);
        int iDday = countDday(listViewItem.getYear(), listViewItem.getMonth(), listViewItem.getDay());

        final ArrayList<Integer> scoreList = new ArrayList<>();
        for (int i = 1; i < 6; i++)
            scoreList.add(i);

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(R.layout.customer_listview_item, parent, false);

            ViewHolder holder = new ViewHolder(v);

            v.setTag(holder);

        }


        if (listViewItem != null) {//리스트뷰아이템에 뭐라도 있으면 값을 설정해서 보이도록
            final ViewHolder holder = (ViewHolder) v.getTag();
            setVisibilityToGone(holder);

            holder.restName.setText(listViewItem.getRestaurant_name());
            holder.r_date.setText(listViewItem.getR_date());
            holder.covers.setText(listViewItem.getCovers() + "명");
            holder.title.setText("예약");

            if (listViewItem.getIs_accepted().equals("null")) {                                     //예약 승인 안됨

                holder.status.setVisibility(View.VISIBLE);
                holder.status.setText("<처리중>");

            }
            else if (listViewItem.getIs_accepted().equals("1")) {   //예약 승인됨

                if (iDday <= 0) {                                                                   //방문 예정일 : 과거 ~ 오늘

                    if (!listViewItem.getIs_confirm().equals("1")) {                                 //방문 확인 안됨

                        holder.status.setVisibility(View.VISIBLE);
                        holder.status.setText("<미방문>");

                    } else {        //방문 확인 됨

                        if (listViewItem.getScoredByCustomer().equals("null")) {

                            holder.title.setText("예약 - 평점 입력 대기");

                        } else {

                            holder.title.setText("완료");

                            holder.status.setVisibility(View.VISIBLE);
                            holder.status.setText("입력 평점 : " + listViewItem.getScoredByRestaurant());

                        }

                    }

                } else {

                    holder.status.setVisibility(View.VISIBLE);
                    holder.status.setText("<D - " + iDday + ">");

                }

            }
            else if(listViewItem.getIs_accepted().equals("0")){
                //예약 거절됨
                holder.title.setText("완료");

                holder.status.setVisibility(View.VISIBLE);
                holder.status.setText("<예약 거절>");

            }
            else {

                holder.title.setText("완료");

                holder.status.setVisibility(View.VISIBLE);
                holder.status.setText("<예약 취소>");

            }

            v.setTag(holder);

        }

        return v;
    }

    private void setVisibilityToGone(ViewHolder holder) {
        holder.status.setVisibility(View.GONE);
        holder.scoreSpinner.setVisibility(View.GONE);
        holder.submitButton.setVisibility(View.GONE);
    }

    public void addItemC(String key, String r_id, String owner_id, String restaurant_name, String nickname, int year,
                         int month, int day, int hour, int minute, int covers,
                         String is_accepted, String is_confirm, String type2,
                         String scoredByRestaurant, String scoredByCustomer) {
        ListViewItem item = new ListViewItem();
        item.setKey(key);
        item.setR_id(r_id);
        item.setOwner_id(owner_id);
        item.setRestaurant_name(restaurant_name);
        item.setNickname(nickname);
        item.setYear(year);
        item.setMonth(month);
        item.setDay(day);
        item.setHour(hour);
        item.setMinute(minute);
        //item.setArrival_time(arrival_time);
        item.setCovers(covers);
        item.setIs_accepted(is_accepted);
        item.setIs_confirm(is_confirm);
        item.setType2(type2);
        item.setScoredByRestaurant(scoredByRestaurant);
        item.setScoredByCustomer(scoredByCustomer);

        item.setR_date(month + "월" + day + "일\n" + hour + "시" + minute + "분");

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

    public void setScoreGtr(ListViewItem item, String value) {
        Log.i("scoredByCustomer 키값: ", item.getKey());
        myRef2.child(item.getKey()).child("scoredByCustomer").setValue(value);
    }

    public void setScore(@NonNull DataSnapshot dataSnapshot, ListViewItem item) {

        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            User user_each = childSnapshot.getValue(User.class);
            if (user_each.getId1().equals(myRef2.child(item.getOwner_id()))) {
                Log.i("원래 SumScore ", Integer.toString(user_each.getSumScore()));
                user_each.setSumScore(user_each.getSumScore() + Integer.parseInt(item.getScoredByCustomer()));
                user_each.setCount(user_each.getCount() + 1);
                user_each.setAvgScore(user_each.getSumScore() / user_each.getCount());
                Log.i("변경된 SumScore ", Integer.toString(user_each.getSumScore()));

                myRef2.child(item.getOwner_id()).child("sumScore").setValue(user_each.getSumScore());
                myRef2.child(item.getOwner_id()).child("count").setValue(user_each.getCount());
                myRef2.child(item.getOwner_id()).child("avgScore").setValue(user_each.getAvgScore());

            } else {
                continue;
            }
        }


    }

    public class ViewHolder {
        final TextView title;
        final TextView restName;
        final TextView r_date;
        final TextView covers;
        final TextView status;
        final Spinner scoreSpinner;
        final Button submitButton;

        public ViewHolder(View root) {
            title = (TextView) root.findViewById(R.id.title);
            restName = (TextView) root.findViewById(R.id.restaurant_name);
            r_date = (TextView) root.findViewById(R.id.r_date);
            covers = (TextView) root.findViewById(R.id.covers);

            status = (TextView) root.findViewById(R.id.status);
            scoreSpinner = (Spinner) root.findViewById(R.id.score_spinner);
            submitButton = (Button) root.findViewById(R.id.submit);
        }
    }

}
