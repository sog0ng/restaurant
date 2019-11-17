package com.example.reservation;

import java.util.ArrayList;

public class Customer_info {

    private String c_id;
    private String c_phone_num;
    private  String c_password;


    public String getC_id() { return c_id; }
    public void setC_id(String c_id) { this.c_id = c_id; }

    public String getC_phone_num() { return c_phone_num; }
    public void setC_phone_num(String c_phone_num) { this.c_phone_num = c_phone_num; }

    public String getC_pw() { return c_password; }
    public void setC_password(String c_password) { this.c_password = c_password; }


    public Customer_info(){}
    public Customer_info(String c_id, String c_phone_num, String c_password){
        this.c_id=c_id;
        this.c_phone_num=c_phone_num;
        this.c_password=c_password;

    }
}
