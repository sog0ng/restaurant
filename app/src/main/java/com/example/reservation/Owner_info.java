package com.example.reservation;

import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Owner_info {

    private String id1;
    private String password;
    private String phone_num;
    private String restaurant_name;

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }




    public Owner_info(){}
    public Owner_info(String id1, String phone_num, String password, String restaurant_name){

        this.id1=id1;
        this.password=password;
        this.phone_num=phone_num;
        this.restaurant_name=restaurant_name;

    }

}