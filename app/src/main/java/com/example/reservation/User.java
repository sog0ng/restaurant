package com.example.reservation;

public class User {
    private String id1;
    private String password;
    private String phone_num;
    private String restaurant_name;//사장일 경우만, 고객 null
    private String operation_hour;//사장일 경우만, 고객 null
    private String is_owner;//사장일 경우 0, 손님일 경우 1


    public String getId1() { return id1; }
    public void setId1(String id1) { this.id1 = id1; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone_num() { return phone_num; }
    public void setPhone_num(String phone_num) { this.phone_num = phone_num; }

    public String getRestaurant_name() { return restaurant_name; }
    public void setRestaurant_name(String restaurant_name) { this.restaurant_name = restaurant_name; }

    public String getOperation_hour() { return operation_hour; }
    public void setOperation_hour(String operation_hour) { this.operation_hour = operation_hour; }

    public String getIs_owner() { return is_owner; }
    public void setIs_owner(String is_owner) { this.is_owner = is_owner; }



    public User() { }

    public User(String id1, String password, String phone_num, String restaurant_name, String operation_hour, String is_owner) {
        this.id1 = id1;
        this.phone_num = phone_num;
        this.password = password;
        this.restaurant_name = restaurant_name;
        this.operation_hour = operation_hour;
        this.is_owner = is_owner;
    }

}
