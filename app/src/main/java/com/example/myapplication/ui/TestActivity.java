package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.appcompat.app.AppCompatActivity;

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
        PropertyAPIClient propertyAPIClient = new PropertyAPIClient();
        PropertyRepository propertyRepository = new PropertyRepository(this);

        propertyRepository.getAllProperties(properties -> {
            AtomicInteger count = new AtomicInteger(0);
            for(Property property : properties) {
                SearchProperty searchProperty = new SearchProperty(property);
                propertyAPIClient.addPropertyToAlgolia(searchProperty, new PropertyAPIClient.OnPropertyCallback() {
                    @Override
                    public void onSuccess(SearchResponse response) {
                        Log.d(TAG, count.getAndIncrement() + "");
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.d(TAG, "onError: " + errorMessage + property.id);
                    }
                });
            }
        }, e -> {
            Log.d(TAG, "onError: " + e.getMessage());
        });

        // Sóc Sơn Ba Vì Hà Nội
    }
}