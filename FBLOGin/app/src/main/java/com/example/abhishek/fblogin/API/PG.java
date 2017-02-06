package com.example.abhishek.fblogin.API;

/**
 * Created by Abhishek on 28-12-2016.
 */
public class PG {

    private String name;
    private int id;
    private String address;
    private String phoneNo;
    private String image_url1;
    private String image_url2;
    private String image_url3;
    private String image_url4;
    private String image_url5;
    private String rental1;
    private String rental2;
    private String rental3;
    private String amenties;
    private String cancellation_policy;
    private String hostel_rule;
    private String gender;
    private String sharing;


    public PG(int id, String name, String address, String image_url1, String image_url2, String image_url3,
              String image_url4, String image_url5, String rental1, String rental2, String rental3, String amenties, String cancellation_policy
            , String hostel_rule, String gender
            , String sharing, String phoneNo) {

        this.name = name;
        this.image_url1 = image_url1;
        this.image_url2 = image_url2;
        this.image_url3 = image_url3;
        this.image_url4 = image_url4;
        this.image_url5 = image_url5;
        this.rental1 = rental1;
        this.rental2 = rental2;
        this.rental3 = rental3;
        this.amenties = amenties;
        this.cancellation_policy = cancellation_policy;
        this.hostel_rule = hostel_rule;
        this.gender = gender;
        this.id = id;
        this.phoneNo = phoneNo;
        this.sharing = sharing;
        this.address = address;
    }

    public String getImage_url1() {
        return image_url1;
    }
    public void setImage_url1(String image_url1) {
        this.image_url1 = image_url1;
    }

    public String getImage_url2() {
        return image_url2;
    }

    public void setImage_url2(String image_url2) {
        this.image_url2 = image_url2;
    }

    public String getImage_url3() {
        return image_url3;
    }

    public void setImage_url3(String image_url3) {
        this.image_url3 = image_url3;
    }

    public String getImage_url4() {
        return image_url4;
    }

    public void setImage_url4(String image_url4) {
        this.image_url4 = image_url4;
    }

    public String getImage_url5() {
        return image_url5;
    }

    public void setImage_url5(String image_url5) {
        this.image_url5 = image_url5;
    }

    public String getRental1() {
        return rental1;
    }

    public void setRental1(String rental1) {
        this.rental1 = rental1;
    }

    public String getRental2() {
        return rental2;
    }

    public void setRental2(String rental2) {
        this.rental2 = rental2;
    }

    public String getRental3() {
        return rental3;
    }

    public void setRental3(String rental3) {
        this.rental3 = rental3;
    }

    public String getAmenties() {
        return amenties;
    }

    public void setAmenties(String amenties) {
        this.amenties = amenties;
    }

    public String getCancellation_policy() {
        return cancellation_policy;
    }

    public void setCancellation_policy(String cancellation_policy) {
        this.cancellation_policy = cancellation_policy;
    }

    public String getHostel_rule() {
        return hostel_rule;
    }

    public void setHostel_rule(String hostel_rule) {
        this.hostel_rule = hostel_rule;
    }

    public String getSharing() {
        return sharing;
    }

    public void setSharing(String sharing) {
        this.hostel_rule = sharing;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
