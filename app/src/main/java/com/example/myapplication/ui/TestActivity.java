package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Model.Location.District;
import com.example.myapplication.data.Model.Location.Province;
import com.example.myapplication.data.Model.Location.Ward;
import com.example.myapplication.data.Repository.Location.LocationAPIClient;

import java.util.List;

// cai nay de ben backend test data thoi khong quan trong dau
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationAPIClient locationAPI = new LocationAPIClient();
        locationAPI.searchDistrictByName("Tây Hoof", new LocationAPIClient.OnDistrictListCallback() {
            @Override
            public void onSuccess(List<District> districts) {
                for (District p : districts) {
                    Log.d("Found Province", p.name + " - Code: " + p.code);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("Error", errorMessage);
            }
        });

    }
}
