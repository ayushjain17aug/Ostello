package com.example.abhishek.fblogin.API;

import android.util.Log;

/**
 * Created by Abhishek on 28-12-2016.
 */
public class PG {

    private String name;
    private int id;
    private String address;
    private String phoneNo;
    private String image_url;

    public PG(int id,String name, String address,String image_url,String phoneNo) {

        Log.d("abhi","Object created"+name);
        this.name = name;
        this.image_url = image_url;
        this.address=address;
        this.id=id;
        this.phoneNo=phoneNo;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
