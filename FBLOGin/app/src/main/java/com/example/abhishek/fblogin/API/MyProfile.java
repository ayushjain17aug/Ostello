package com.example.abhishek.fblogin.API;

/**
 * Created by Abhishek on 08-01-2017.
 */

public class MyProfile {
    private String user_id;
    private String name;
    private String age;
    private String email;
    private String phone;
    private String gender;
    private String image;

    public MyProfile(String user_id, String name, String phone, String age, String email, String gender, String image) {
        this.user_id = user_id;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
