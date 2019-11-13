package com.example.reservation.item;

import android.widget.Button;

public class ListViewItem {
    private int type;

    private String nickname;
    private String r_date;
    private String arrival_time;

    private boolean accept;
    private boolean reject
            ;
    private int covers;
    private int score;
    private int year;
    private int month;
    private int day;

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public boolean isReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int yaer) {
        this.year = yaer;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getR_date() {
        return r_date;
    }

    public void setR_date(String r_date) {
        this.r_date = r_date;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public int getCovers() {
        return covers;
    }

    public void setCovers(int covers) {
        this.covers = covers;
    }


}
