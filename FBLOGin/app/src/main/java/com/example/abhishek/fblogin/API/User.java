package com.example.abhishek.fblogin.API;

/**
 * Created by MOHIT on 12-Sep-16.
 */
public class User {

    private String user_id;
    private String name;
    private String photo_url;
    private String email;


    public User(){}

    public User(String user_id,String name, String email,
                String photo_url){
        this.user_id=user_id;
        this.name = name;
        this.email = email;
        this.photo_url=photo_url;
        }
    public String getUser_id(){return user_id;}
    public void setUser_id(String user_id){this.user_id=user_id;}

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPhotoUrl(){return photo_url;}
    public void setPhotoUrl(String photo_url){ this.photo_url=photo_url;}

}