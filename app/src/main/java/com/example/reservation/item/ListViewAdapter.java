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

import com.example.reservation.CustomerHomeActivity;
import com.example.reservation.OwnerHomeActivity;
import com.example.reservation.R;
import com.example.reservation.User;
import com.example.reservation.Reservation;
import com.example.reservation.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ListViewAdapter extends BaseAdapter {
    Activity activity;
    //Adapter에서 이러는건 아닌것 같지만 일단은 이전에 사용한것과 동일한 방식으로 접근
    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    final DatabaseReference myRef2 = database2.getReference("Reservation/");
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    private int selectedScore;

    public ListViewAdapter(Activity activity) {
        this.activity = activity;
    }

    public ListViewAdapter(CustomerHomeActivity customerHomeActivity, int simple_list_item_1, ArrayList<User> list) {

    }

    public ListViewAdapter(OwnerHomeActivity ownerHomeActivity, int simple_list_item_1, ArrayList<Reservation> list) {
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
        final ListViewItem listViewItem = listViewItemList.get(position);
        int iDday = countDday(listViewItem.getYear(), listViewItem.getMonth(), listViewItem.getDay());

        final ArrayList<Integer> scoreList = new ArrayList<>();
        for (int i = 1; i < 6; i++)
            scoreList.add(i);

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(R.layout.owner_listview_item, parent, false);

            ViewHolder holder = new ViewHolder(v);

            v.setTag(holder);
        }

        if (listViewItem != null) {//리스트뷰아이템에 뭐라도 있으면 값을 설정해서 보이도록
            final ViewHolder holder = (ViewHolder) v.getTag();
            setVisibilityToGone(holder);

            holder.nickname.setText(listViewItem.getNickname());
            holder.r_date.setText(listViewItem.getR_date());
            holder.covers.setText(listViewItem.getCovers() + "명");

            if (listViewItem.getIs_accepted().equals("null")) {                                     //예약 승인 안됨
                holder.title.setText("승인 대기");

                holder.acceptButton.setVisibility(View.VISIBLE);
                holder.rejectButton.setVisibility(View.VISIBLE);
                // 아직 승인이나 거절 안함
                holder.acceptButton.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {// 승인 팝업 띄움
                        showAcceptPopup(listViewItem);
                    }
                });

                holder.rejectButton.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {// 거절 팝업 띄움
                        showRejectPopup(listViewItem);
                    }
                });
            } else if (listViewItem.getIs_accepted().equals("1")) {                                 //예약 승인됨
                if (iDday <= 0) {                                                                   //방문 예정일 : 과거 ~ 오늘
                    if (listViewItem.getIs_confirm().equals("0")) {                                 //방문 확인 안됨
                        holder.title.setText("방문 대기");

                        holder.confirmButton.setVisibility(View.VISIBLE);
                        holder.noshowButton.setVisibility(View.VISIBLE);

                        holder.confirmButton.setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View v) {
                                // 방문 확인 팝업 띄움
                                showConfirmPopup(listViewItem);
                            }
                        });

                        holder.noshowButton.setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View v) {
                                // 미방문 확인 팝업 띄움
                                showNoshowPopup(listViewItem);
                            }
                        });
                    } else {                                                                        //방문 확인됨
                        if (listViewItem.getIs_confirm().equals("1")) {
                            holder.title.setText("방문 예약 - 평점 입력 대기");
                        } else {
                            holder.title.setText("미방문 예약 - 평점 입력 대기");
                        }

                        holder.scoreSpinner.setVisibility(View.VISIBLE);
                        holder.submitButton.setVisibility(View.VISIBLE);

                        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(context,
                                R.layout.support_simple_spinner_dropdown_item, scoreList);
                        holder.scoreSpinner.setAdapter(arrayAdapter);
                        holder.scoreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectedScore = scoreList.get(i);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });

                        holder.submitButton.setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View v) {
                                Toast.makeText(context,
                                        "평점 " + selectedScore + "점 이 입력되었습니다.",
                                        Toast.LENGTH_SHORT).show();
                                /*holder.gtc.setText(Integer.toString(selectedScore)); //사장 화면에 몇점줬는지 나오도록 텍스트 설정만

                                //평점 DB에 넣어준다 사장이 고객에 대한 평가하는거니까 gtc(give to customer)
                                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        setScoreGtc(listViewItem, Integer.toString(selectedScore));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });*/
                            }
                        });
                    }

                } else {
                    holder.title.setText("승인 완료");

                    holder.status.setVisibility(View.VISIBLE);
                    holder.status.setText("D - " + iDday);
                }
            } else {                                                                                //예약 거절됨
                holder.title.setText("완료");

                holder.status.setVisibility(View.VISIBLE);
                holder.status.setText("거절된 예약");
            }
        }

        return v;
    }

    private void setVisibilityToGone(ViewHolder holder) {
        holder.status.setVisibility(View.GONE);
        holder.acceptButton.setVisibility(View.GONE);
        holder.rejectButton.setVisibility(View.GONE);
        holder.confirmButton.setVisibility(View.GONE);
        holder.noshowButton.setVisibility(View.GONE);
        holder.scoreSpinner.setVisibility(View.GONE);
        holder.submitButton.setVisibility(View.GONE);
    }


    public void showConfirmPopup(final ListViewItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        alert.setTitle("방문 확인");
        alert.setMessage("방문을 확인하시겠습니까?");

        alert.setView(layout);

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // db에 방문 확인 변수 변경 is_confirm->1
                setConfirm(item, "1");
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void showNoshowPopup(final ListViewItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        alert.setTitle("미방문 확인");
        alert.setMessage("미방문을 확인하시겠습니까?");

        alert.setView(layout);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // db에 방문 확인 변수 변경 is_confirm->0
                setConfirm(item, "0");

            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void showAcceptPopup(final ListViewItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        alert.setTitle("예약 승인");
        alert.setMessage("예약을 승인하시겠습니까?");

        alert.setView(layout);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // db에 거절된 예약 추가 is_accepted -> 1
                setAccepted(item, "1");

            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void showRejectPopup(final ListViewItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        alert.setTitle("예약 거절");
        alert.setMessage("예약을 거절하시겠습니까?");

        alert.setView(layout);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // db에 거절된 예약 추가 is_accepted -> 0
                setAccepted(item, "0");
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void addItem(String key, String nickname, int year,
                        int month, int day, int hour, int minute, int covers,
                        String is_accepted, String is_confirm) {
        ListViewItem item = new ListViewItem();
        item.setKey(key);
        item.setNickname(nickname);
        item.setYear(year);
        item.setMonth(month);
        item.setDay(day);
        item.setHour(hour);
        item.setMinute(minute);
        item.setCovers(covers);
        item.setIs_accepted(is_accepted);
        item.setIs_confirm(is_confirm);

        item.setR_date(month + "월" + day + "일\n" + hour + "시" + minute + "분");

        listViewItemList.add(0, item);
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


    private void setConfirm(ListViewItem item, String value) {
        // item의 키값을 받아와서 reservation의 키값이랑 같은 곳에 setValue
        Log.i("set Confirm item 키값: ", item.getKey());
        myRef2.child(item.getKey()).child("is_confirm").setValue(value);
    }

    private void setAccepted(ListViewItem item, String value) {
        Log.i("set Accepted item 키값: ", item.getKey());
        myRef2.child(item.getKey()).child("is_accepted").setValue(value);
    }

    public void setScoreGtc(ListViewItem item, String value) {
        Log.i("set GTC item 키값: ", item.getKey());
        myRef2.child(item.getKey()).child("gtc").setValue(value);
    }

    public class ViewHolder {
        final TextView title;

        final TextView nickname;
        final TextView r_date;
        final TextView covers;

        final  TextView status;

        final Button acceptButton;
        final Button rejectButton;

        final Button confirmButton;
        final Button noshowButton;

        final Spinner scoreSpinner;
        final Button submitButton;

        public ViewHolder(View root) {
            title = (TextView) root.findViewById(R.id.title);

            nickname = (TextView) root.findViewById(R.id.nickname);
            r_date = (TextView) root.findViewById(R.id.r_date);
            covers = (TextView) root.findViewById(R.id.covers);

            status = (TextView) root.findViewById(R.id.status);

            acceptButton = (Button) root.findViewById(R.id.acceptButton);//예약 승인 버튼
            rejectButton = (Button) root.findViewById(R.id.rejectButton);//예약 거절 버튼
            confirmButton = (Button) root.findViewById(R.id.confirmButton);//방문 확인 버튼
            noshowButton = (Button) root.findViewById(R.id.noshowButton);//방문 미확인 버튼

            scoreSpinner = (Spinner) root.findViewById(R.id.scoreSpinner);
            submitButton = (Button) root.findViewById(R.id.submitButton);
        }
    }
}

