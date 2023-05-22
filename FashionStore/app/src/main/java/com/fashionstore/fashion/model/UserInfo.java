package com.fashionstore.fashion.model;

public class UserInfo {
    private String phone;
    private String address;
    private String fullName;

    public UserInfo(){

    }

    public UserInfo(String phone, String address, String fullName) {
        this.phone = phone;
        this.address = address;
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
