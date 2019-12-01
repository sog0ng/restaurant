package com.example.reservation.item;

import android.widget.Button;

import java.util.Comparator;
import java.util.List;

public class ListViewItem {
    //손님이 예앾

    private String r_id;
    private String owner_id;
    private int type;
    private String r_date;
    private int score;
    private String key;
    private String nickname;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int covers;
    private String restaurant_name;
    private String is_accepted = "null";//기본값 "null"
    private String is_confirm = "null";//기본값 "null"
    private String scoredByCustomer = "null";//기본값 "null"
    private String scoredByRestaurant = "null";//기본값 "null"
    private String type2;

    public String getPhone_num() { return phone_num; }
    public void setPhone_num(String phone_num) { this.phone_num = phone_num; }

    private String phone_num;
    public String getType2() { return type2; }

    public void setType2(String type2) { this.type2 = type2; }



    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getCovers() {
        return covers;
    }

    public void setCovers(int covers) {
        this.covers = covers;
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

    public String getR_date() {
        return r_date;
    }

    public void setR_date(String r_date) {
        this.r_date = r_date;
    }

    public String getIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(String is_accepted) {
        this.is_accepted = is_accepted;
    }

    public String getIs_confirm() {
        return is_confirm;
    }

    public void setIs_confirm(String is_confirm) {
        this.is_confirm = is_confirm;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getScoredByCustomer() {
        return scoredByCustomer;
    }

    public void setScoredByCustomer(String scoredByCustomer) {
        this.scoredByCustomer = scoredByCustomer;
    }

    public String getScoredByRestaurant() {
        return scoredByRestaurant;
    }

    public void setScoredByRestaurant(String scoredByRestaurant) {
        this.scoredByRestaurant = scoredByRestaurant;
    }
}

