package com.example.reservation;

import java.util.Date;

public class Reservation {
    String r_id;
    String nickname;
    String restaurant_name;

    int year;
    int month;
    int day;
    int hour;
    int minute;
    int covers;

    String is_accepted = "null";
    String is_confirm = "0";
    String gtr = "null";
    String gtc = "null";

    String is_owner;
    String owner_id;
    String week;


    public String getR_id() {
        return r_id;
    }
    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }
    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
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
    public String getGtr() {
        return gtr;
    }
    public void setGtr(String gtr) {
        this.gtr = gtr;
    }
    public String getGtc() {
        return gtc;
    }
    public void setGtc(String gtc) {
        this.gtc = gtc;
    }

    public String getIs_owner() {
        return is_owner;
    }
    public void setIs_owner(String is_owner) {
        this.is_owner = is_owner;
    }
    public String getOwner_id() {
        return owner_id;
    }
    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }
    public String getWeek() {
        return week;
    }
    public void setWeek(String week) {
        this.week = week;
    }
}