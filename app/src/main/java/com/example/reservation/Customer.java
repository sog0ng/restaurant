package com.example.reservation;

import java.util.ArrayList;

public class Customer {

    private String c_id;
    private String c_phone_num;
    private  String c_pw;
    private ArrayList<String> reservation;

    public String getC_id() { return c_id; }
    public void setC_id(String c_id) { this.c_id = c_id; }

    public String getC_phone_num() { return c_phone_num; }
    public void setC_phone_num(String c_phone_num) { this.c_phone_num = c_phone_num; }

    public String getC_pw() { return c_pw; }
    public void setC_pw(String c_pw) { this.c_pw = c_pw; }

    public ArrayList<String> getReservation() { return reservation; }
    public void setReservation(ArrayList<String> reservation) { this.reservation = reservation; }


    public Customer(){}
    public Customer(String c_id, String c_phone_num, String c_pw, ArrayList<String> reservation){
        this.c_id=c_id;
        this.c_phone_num=c_phone_num;
        this.c_pw=c_pw;
        this.reservation=reservation;
    }
}
