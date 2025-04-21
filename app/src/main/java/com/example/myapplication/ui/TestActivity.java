package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.Repository.Auth.AuthRepository;
import com.example.myapplication.data.Repository.User.UserRepository;
import com.example.myapplication.ui.fragments.ExploreFragment;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserRepository userRepository = new UserRepository(this);
        AuthRepository authRepository = new AuthRepository(this);
        userRepository.getPropertyInUserWishList(authRepository.getUserUid(),
                properties -> {
                    properties.forEach(property -> {
                        Log.d("TestActivity", "Property in wish list: " + property.id);
                    });
                },
                e -> {
                    Log.e("TestActivity", "Error getting properties in wish list", e);
                });
    }
}
