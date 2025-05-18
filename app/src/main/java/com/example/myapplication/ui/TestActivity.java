package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Model.Review.Review;
import com.example.myapplication.data.Repository.Auth.AuthRepository;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.example.myapplication.data.Repository.Conversation.ConversationRepository;
import com.example.myapplication.data.Repository.Review.ReviewRepository;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "SearchTestActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConversationRepository conversationRepository = new ConversationRepository(this);
        AuthRepository authRepository = new AuthRepository(this);
        String id = authRepository.getUserUid();
        conversationRepository.getAllConversationByHostID(id,
                conversations -> {
                    for (int i = 0; i < conversations.size(); i++) {
                        Log.d(TAG, "onCreate: " + conversations.get(i).id);
                    }
                }, e -> {
                    Log.d(TAG, "onCreate: " + e.getMessage());
                });
    }
}