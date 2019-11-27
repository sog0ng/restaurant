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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    Activity activity;

    //Adapter에서 이러는건 아닌것 같지만 일단은 이전에 사용한것과 동일한 방식으로 접근
    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    final DatabaseReference myRef2 = database2.getReference("Reservation/");

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
///        if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (iDday < 0) {
            convertView = inflater.inflate(R.layout.owner_past_listview_item, parent, false);
        } else if (listViewItem.getIs_accepted() != null) {
            convertView = inflater.inflate(R.layout.owner_accepted_listview_item, parent, false);
        } else {
            convertView = inflater.inflate(R.layout.owner_unaccepted_listview_item, parent, false);
        }

//        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView nickname = (TextView) convertView.findViewById(R.id.nickname);
        TextView r_date = (TextView) convertView.findViewById(R.id.r_date);
        TextView covers = (TextView) convertView.findViewById(R.id.covers);
        TextView dDay = (TextView) convertView.findViewById(R.id.dDay);
        Button accept = (Button) convertView.findViewById(R.id.accept);//예약 승인
        Button reject = (Button) convertView.findViewById(R.id.reject);//예약 거절
        Button confirm = (Button) convertView.findViewById(R.id.confirm);//방문 확인
        Button noshow = (Button) convertView.findViewById(R.id.noshow);//방문 미확인
        final EditText score = (EditText) convertView.findViewById(R.id.score);//평점 숫자
        Button submit = (Button) convertView.findViewById(R.id.submit);//확인(평점 등록할때)


        myRef2.addValueEventListener(new ValueEventListener() {
            //이거 위치 모르겠음 find에 listviewitem을 넘겨주려면 여기여야하는것같고,,,,
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Reservation matchingReservation = find(dataSnapshot, listViewItem);
                //데이터베이스 내부를 반복해서 돌면서 listViewItem과 일치하는 Reservation을 리턴
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if (iDday < 0 && listViewItem.getIs_accepted() == null) { //과거 내역 방문확인을 해야함
            // past
            title.setText("과거 내역");
            nickname.setText(listViewItem.getNickname());
            r_date.setText(listViewItem.getR_date());
            covers.setText(listViewItem.getCovers() + "명");

            //방문확인부터 먼저
            confirm.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    // 방문 확인 팝업 띄움
                    showConfirmPopup();
                }
            });

            noshow.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    // 미방문 확인 팝업 띄움
                    showNoshowPopup();

                }
            });
        } else if (iDday < 0 && listViewItem.getIs_accepted() != null) {//과거 내역 방문확인을 했으니 평점을 줄 수 있다
            title.setText("과거 내역");
            nickname.setText(listViewItem.getNickname());
            r_date.setText(listViewItem.getR_date());
            //arrival_time.setText(listViewItem.getArrival_time());
            covers.setText(listViewItem.getCovers() + "명");

            submit.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    // db 에 평점 전달.

/*
                    if(matchingReservation.getIs_owner()==1){//사장인 경우
                        matchingReservation.setGtc(score);//Edittext의 값을 givetocustomer에 넣는것

                    }else{
                        matchingReservation.setGtr(score);//Edittext의 값을 givetorestaurant에 넣는것
                    }
*/
                }
            });

        } else if (listViewItem.getIs_accepted() != null) {
            // accepted
            title.setText("예약 내역");
            nickname.setText(listViewItem.getNickname());
            r_date.setText(listViewItem.getR_date());
            covers.setText(listViewItem.getCovers() + "명");
            dDay.setText(Integer.toString(iDday));
        } else {
            // unaccepted
            title.setText("미승인 예약 내역");
            nickname.setText(listViewItem.getNickname());
            r_date.setText(listViewItem.getR_date());
            covers.setText(listViewItem.getCovers() + "명");
            accept.setText("승인");//버튼 자체의 글씨를 쓰는것
            reject.setText("거절");

            accept.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    // 승인 팝업 띄움
                    showAcceptPopup();
                    //팝업 내부에서 reservation 값을 변경시키는 방법을 모르겠음
                    //해당 Reservation의 변수값을 변경시켜주어야하는데 이걸 어떻게 하지


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


    public void showConfirmPopup() {
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
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void showNoshowPopup() {
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
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void showAcceptPopup() {

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

            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void showRejectPopup() {
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
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }


    public Reservation find(@NonNull DataSnapshot dataSnapshot, ListViewItem listViewItem) {
        Reservation result = null;
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            Reservation reservation_each = childSnapshot.getValue(Reservation.class);
            if (reservation_each.getRestaurant_name().equals(listViewItem.getRestaurant_name())
                    && reservation_each.getNickname().equals(listViewItem.getNickname())
                    && reservation_each.getYear() == listViewItem.getYear()
                    && reservation_each.getMonth() == listViewItem.getMonth()
                    && reservation_each.getDay() == listViewItem.getDay()
                    && reservation_each.getHour() == listViewItem.getHour()
                    && reservation_each.getMinute() == listViewItem.getMinute()
                    && reservation_each.getCovers() == listViewItem.getCovers()
            ) {
                result = reservation_each;
                break;
            } else {
                continue;
            }
        }
        return result;
    }


    public void addItem(String nickname, int year,

                        int month, int day, int hour, int minute, int covers) {
        ListViewItem item = new ListViewItem();

        item.setNickname(nickname);
        item.setYear(year);
        item.setMonth(month);
        item.setDay(day);
        //item.setArrival_time(arrival_time);
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


}
