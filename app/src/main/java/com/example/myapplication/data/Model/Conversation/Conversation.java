package com.example.myapplication.data.Model.Conversation;

public class Conversation {
    public String id;
    public String guest_id;
    public String host_id;

    public Conversation() {}

    public Conversation(String id, String guest_id, String host_id) {
        this.id = id;
        this.guest_id = guest_id;
        this.host_id = host_id;
    }
}
