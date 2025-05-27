package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainer;

import com.example.myapplication.R;
import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Model.Location.District;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Model.Property.SearchProperty;
import com.example.myapplication.data.Model.Review.Review;
import com.example.myapplication.data.Model.Search.SearchResponse;
import com.example.myapplication.data.Repository.Auth.AuthRepository;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.example.myapplication.data.Repository.Conversation.ConversationRepository;
import com.example.myapplication.data.Repository.Location.LocationAPIClient;
import com.example.myapplication.data.Repository.Property.PropertyRepository;
import com.example.myapplication.data.Repository.Review.ReviewRepository;
import com.example.myapplication.data.Repository.Search.PropertyAPIClient;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "SearchTestActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_header_sample);
        setSupportActionBar(findViewById(R.id.toolbar));
    }
}