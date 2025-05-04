package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Model.Conversation.Conversation;
import com.example.myapplication.data.Repository.Auth.AuthRepository;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.example.myapplication.data.Repository.Conversation.ConversationRepository;

import java.util.Objects;


public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BookingRepository bookingRepository = new BookingRepository(this);
        Booking booking = new Booking("33a845db-7fb9-4492-962c-c621ce4cfb55",
                "4fjk29rTyPhr4Q5pFad1PUPs7Fj2",
                "v4528ioquLTQbtmKYieS3quQUsp2",
                "29-05-2025",
                "05-06-2025",
                5000000,
                "Please work ok im so tied fuck");

        bookingRepository.createBooking(booking,
                unused -> {
                    Log.d("Test", "Successfully created booking " + booking.id);
                },
                e -> {
                    Log.d("Test", "Failed to create booking " + e.getMessage());
                });
    }
}
