package com.example.myapplication.data.Model.Conversation;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    public String id;
    public String guest_id;
    public String host_id;
    public List<Message> messages;
    public Conversation() {}

    public Conversation(String id, String guest_id, String host_id) {
        this.id = id;
        this.guest_id = guest_id;
        this.host_id = host_id;
        this.messages = new ArrayList<>();
    }
}
