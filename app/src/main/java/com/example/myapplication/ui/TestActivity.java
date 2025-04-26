package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Model.Conversation.Conversation;
import com.example.myapplication.data.Repository.Auth.AuthRepository;
import com.example.myapplication.data.Repository.Conversation.ConversationRepository;

import java.util.Objects;


public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConversationRepository conversationRepository = new ConversationRepository(this);
        AuthRepository authRepository = new AuthRepository(this);
        conversationRepository.getAllConversationByHostID(authRepository.getUserUid(),
                conversations -> {
                    for(Conversation conversation : conversations) {
                        Log.d("Conversation", "Conversation: " + conversation.id);
                    }
                },
                e -> {
                    Log.d("Conversation", Objects.requireNonNull(e.getMessage()));
                });
    }
}
