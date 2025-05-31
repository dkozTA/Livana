package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Enum.PropertyStatus;
import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Model.Location.District;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Model.Property.SearchProperty;
import com.example.myapplication.data.Model.Review.Review;
import com.example.myapplication.data.Model.Search.BookedDateRequest;
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
        String propertyID = "ef3ec1b7-f988-4d97-83a1-0582b846aca7";
        String linkID = "f7995b46-8305-4a7b-b64a-fd98bee08327";

        List<String> dates =  new ArrayList<>();
        dates.add("10-11-2025");
        dates.add("11-11-2025");
        dates.add("12-11-2025");
        dates.add("13-11-2025");
        dates.add("14-11-2025");
        dates.add("15-11-2025");
        BookedDateRequest datesRequest = new BookedDateRequest(dates);

        propertyAPIClient.removeBookedDate(propertyID, datesRequest, new PropertyAPIClient.OnPropertyCallback() {
            @Override
            public void onSuccess(SearchResponse response) {
                Log.d(TAG, "Update Success");
            }

            @Override
            public void onError(String errorMessage) {
                Log.d(TAG, "Update Fail " + errorMessage);
            }
        });
    }
}