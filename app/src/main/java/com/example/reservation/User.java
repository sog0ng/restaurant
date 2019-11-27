package com.example.reservation;

public class User {
    private String id1;
    private String password;
    private String phone_num;
    private String restaurant_name;//사장일 경우만, 고객 null
    private String is_owner;//사장일 경우 0, 손님일 경우 1
    private String close_hour;// 손님일 경우 null
    private String close_minute;//
    private String open_hour;// 손님일 경우 null
    private String open_minute;

    public String getClose_hour() {
        return close_hour;
    }

    public void setClose_hour(String close_hour) {
        this.close_hour = close_hour;
    }

    public String getClose_minute() {
        return close_minute;
    }

    public void setClose_minute(String close_minute) {
        this.close_minute = close_minute;
    }

    public String getOpen_hour() {
        return open_hour;
    }

    public void setOpen_hour(String open_hour) {
        this.open_hour = open_hour;
    }

    public String getOpen_minute() {
        return open_minute;
    }

    public void setOpen_minute(String open_minute) {
        this.open_minute = open_minute;
    }


    public String getId1() { return id1; }
    public void setId1(String id1) { this.id1 = id1; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone_num() { return phone_num; }
    public void setPhone_num(String phone_num) { this.phone_num = phone_num; }

    public String getRestaurant_name() { return restaurant_name; }
    public void setRestaurant_name(String restaurant_name) { this.restaurant_name = restaurant_name; }

    public String getIs_owner() { return is_owner; }
    public void setIs_owner(String is_owner) { this.is_owner = is_owner; }

    public User() { }

    public User(String id1, String password, String phone_num, String restaurant_name, String is_owner, String open_hour,String open_minute, String close_hour,String close_minute) {
        this.id1 = id1;
        this.phone_num = phone_num;
        this.password = password;
        this.restaurant_name = restaurant_name;
        this.is_owner = is_owner;
        this.open_hour=open_hour;
        this.open_minute=open_minute;
        this.close_hour=close_hour;
        this.close_minute=close_minute;
    }

}
