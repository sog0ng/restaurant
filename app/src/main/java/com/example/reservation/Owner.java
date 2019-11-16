package com.example.reservation;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Owner {
    private ArrayList<String> Statistics;
    private String id;
    private ArrayList<String> menu;
    private String operation_hour;
    private String phone_num;
    private String pw;
    private ArrayList<String> res;
    private String restaurant_name;
    private ArrayList<String> seat;


    public ArrayList<String> getStatistics() { return Statistics; }
    public void setStatistics(ArrayList<String> statistics) { Statistics = statistics; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public ArrayList<String> getMenu() { return menu; }
    public void setMenu(ArrayList<String> menu) { this.menu = menu; }

    public String getOperation_hour() { return operation_hour; }
    public void setOperation_hour(String operation_hour) { this.operation_hour = operation_hour; }

    public String getPhone_num() { return phone_num; }
    public void setPhone_num(String phone_num) { this.phone_num = phone_num; }

    public String getPw() { return pw; }
    public void setPw(String pw) { this.pw = pw; }

    public ArrayList<String> getRes() { return res; }
    public void setRes(ArrayList<String> res) { this.res = res; }

    public String getRestaurant_name() { return restaurant_name; }
    public void setRestaurant_name(String restaurant_name) { this.restaurant_name = restaurant_name; }

    public ArrayList<String>  getSeat() { return seat; }
    public void setSeat(ArrayList<String> seaet) { this.seat = seat; }

    public Owner(){}
    public Owner(ArrayList<String> Statistics, String id, ArrayList<String> menu, String operation_hour, String phone_num, String pw, ArrayList<String> res, String restaurant_name, ArrayList<String> seat){
        this.Statistics=Statistics;
        this.id=id;
        this.menu=menu;
        this.operation_hour=operation_hour;
        this.phone_num=phone_num;
        this.pw=pw;
        this.res=res;
        this.restaurant_name=restaurant_name;
        this.seat=seat;

    }

}