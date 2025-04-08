package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Model.Auth.AuthRegister;
import com.example.myapplication.data.Repository.Auth.AuthRepository;

// cai nay de ben backend test data thoi khong quan trong dau
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthRepository auth = new AuthRepository(this);
        AuthRegister register = new AuthRegister("hello@gmail.com", "12345678", "Nguyen Vu Ha Y", "0909090909");
        auth.register(
                register,
                unused -> Log.d("TEST", "Đăng ký thành công" + auth.getUserUid()),
                e -> Log.e("TEST", "Đăng ký thất bại: " + e.getMessage())
        );
    }
}
