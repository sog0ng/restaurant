package com.example.reservation;

import java.util.Date;

public class Reservation {
    String r_id;//아직 추가X
    String restaurant_name;
    String nickname;
    int covers;
    String r_date;
    String is_accepted;//기본 값 0으로 거절
    String is_confirm;//기본 값 1로 방문되어있음, 미방문시 0으로 바꿔주기
    String time;//아직 추가x
    String gtr;
    String gtc;
    String is_owner;

    public String getR_id() { return r_id; }
    public void setR_id(String r_id) { this.r_id = r_id; }

    public String getRestaurant_name() { return restaurant_name; }
    public void setRestaurant_name(String restaurant_name) { this.restaurant_name = restaurant_name; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public int getCovers() { return covers; }
    public void setCovers(int covers) { this.covers = covers; }


    public void setR_date(String r_date) { this.r_date = r_date; }
    public String getR_date() { return r_date; }

    public String getIs_accepted() { return is_accepted; }
    public void setIs_accepted(String is_accepted) { this.is_accepted = is_accepted; }

    public String getIs_confirm() { return is_confirm; }
    public void setIs_confirm(String is_confirm) { this.is_confirm = is_confirm; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getGtr() { return gtr; }
    public void setGtr(String gtr) { this.gtr = gtr; }

    public String getGtc() { return gtc; }
    public void setGtc(String gtc) { this.gtc = gtc; }

    public String getIs_owner() { return is_owner; }
    public void setIs_owner(String is_owner) { this.is_owner = is_owner; }



}
