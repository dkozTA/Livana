package com.example.myapplication.data.Model.User;

import com.example.myapplication.data.Enum.Role;

import java.util.Date;

public class User {
    public String uid;
    public String full_name;
    public String phone_number;
    public String avatar_link;
    public Role role;
    public String[] rentingHistory;
    public String[] wish_list;
    public Date created_at;

    public User() {} // Firestore cần constructor rỗng

    public User(String uid, String full_name, String phone_number) {
        this.uid = uid;
        this.full_name = full_name;
        this.phone_number = phone_number;
        this.created_at = new Date();
        this.role = Role.USER;
        this.rentingHistory = new String[0];
        this.wish_list = new String[0];
        this.avatar_link = "";
    }
}
