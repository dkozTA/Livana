package com.example.myapplication;

import android.app.Application;

import com.example.myapplication.data.Repository.FirebaseService;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
