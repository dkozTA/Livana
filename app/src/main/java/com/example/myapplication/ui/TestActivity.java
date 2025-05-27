package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
        PropertyRepository propertyRepository = new PropertyRepository(this);
        String propertyID = "ef3ec1b7-f988-4d97-83a1-0582b846aca7";
        String linkID = "f7995b46-8305-4a7b-b64a-fd98bee08327";
        propertyRepository.addLinksToBothProperty(linkID, propertyID, unused -> {
            Log.d(TAG, "addLinksToProperty: success");
        }, e-> {
            Log.d(TAG, "addLinksToProperty: fail");
        });
    }
}