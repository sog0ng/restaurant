package com.example.reservation;

import java.util.Date;

public class Reservation {
    int r_id;

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    String restaurant_name;
    String nickname;
    int covers;
    Date r_date;
    int is_accepted;
    int r_index;
    int confirm;

    public int getR_id() {
        return r_id;
    }

    public void setR_id(int r_id) {
        this.r_id = r_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCovers() {
        return covers;
    }

    public void setCovers(int covers) {
        this.covers = covers;
    }

    public Date getR_date() {
        return r_date;
    }

    public void setR_date(Date r_date) {
        this.r_date = r_date;
    }

    public int getIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(int is_accepted) {
        this.is_accepted = is_accepted;
    }

    public int getR_index() {
        return r_index;
    }

    public void setR_index(int r_index) {
        this.r_index = r_index;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }
}
