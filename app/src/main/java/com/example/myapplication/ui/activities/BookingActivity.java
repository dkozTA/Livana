package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.fragments.RoomBookingFragment;


public class BookingActivity extends AppCompatActivity {
    private ImageButton backButton;
    private ImageButton exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Get data from intent
        Bundle args = getIntent().getExtras();

        // Create and load fragment
        RoomBookingFragment bookingFragment = new RoomBookingFragment();
        bookingFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, bookingFragment)
                .commit();

        // Initialize buttons
        backButton = findViewById(R.id.btnBack);
        exitButton = findViewById(R.id.btnClose);

        // Back button - goes to previous screen
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        // Exit button - returns to home screen
        exitButton.setOnClickListener(v -> {
            // Create intent to return to main activity/home screen
            Intent intent = new Intent(BookingActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}