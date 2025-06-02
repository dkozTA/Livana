package com.example.myapplication.ui;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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
import com.example.myapplication.data.Model.Statistic.PropertyStatistic;
import com.example.myapplication.data.Model.Statistic.PropertyStatisticDetails;
import com.example.myapplication.data.Model.Statistic.ReviewStatisticDetails;
import com.example.myapplication.data.Repository.Auth.AuthRepository;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.example.myapplication.data.Repository.Conversation.ConversationRepository;
import com.example.myapplication.data.Repository.Location.LocationAPIClient;
import com.example.myapplication.data.Repository.Property.PropertyRepository;
import com.example.myapplication.data.Repository.Review.ReviewRepository;
import com.example.myapplication.data.Repository.Search.PropertyAPIClient;
import com.example.myapplication.data.Repository.Statistic.StatisticRepository;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatisticRepository statisticRepository = new StatisticRepository(this);
        String propertyID = "382d0a84-cb3e-420f-bbfa-c88ee98ba238";
        String hostID = "v4528ioquLTQbtmKYieS3quQUsp2";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //for(int i= 1; i< 6; i++) {
                statisticRepository.getAllPropertyStatistic(hostID, LocalDate.of(2025, 5, 5), propertyStatistic -> {

                }, e-> {

                });
            //}
        }

        /*
        statisticRepository.bookingRepository.getBookingsByPropertyId(propertyID, bookings -> {
            List<String> dateSeries = new ArrayList<>();
            for (Booking booking : bookings) {
                List<String> series = statisticRepository.generateDateSeries(booking.check_in_day, booking.check_out_day);
                if(statisticRepository.propertyRepository.validateBookedDate(dateSeries, series)) {
                    dateSeries.addAll(series);
                } else {
                    statisticRepository.bookingRepository.deleteBooking(booking.id, unused -> {
                        Log.d(TAG, "onCreate: Booking deleted " + booking.id);
                    }, e-> {
                        Log.d(TAG, "onCreate: Booking not deleted " + booking.id);
                    });
                }
            }

        }, e-> {
            Log.d(TAG, "onCreate: " + e.getMessage());
        });

        statisticRepository.bookingRepository.getAllBookingsByHostID(hostID, bookings -> {
            Map<String, List<String>> map = new HashMap<>();
            for(Booking booking : bookings) {
                if(!map.containsKey(booking.property_id)) {
                    map.put(booking.property_id, new ArrayList<>());
                    map.get(booking.property_id).add(booking.check_in_day);
                    map.get(booking.property_id).add(booking.check_out_day);
                } else {
                    map.get(booking.property_id).add(booking.check_in_day);
                    map.get(booking.property_id).add(booking.check_out_day);
                }
            }
            for(Map.Entry<String, List<String>> entry : map.entrySet()) {
                Log.d(TAG, entry.getKey());
                Log.d(TAG, entry.getValue().toString());
            }
        }, e-> {
            Log.d(TAG, "onCreate: " + e.getMessage());
        });
        */
    }
}