package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Enum.PropertyStatus;
import com.example.myapplication.data.Enum.PropertyType;
import com.example.myapplication.data.Model.Auth.AuthRegister;
import com.example.myapplication.data.Model.Property.Address;
import com.example.myapplication.data.Model.Property.Amenities;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Model.Property.Rooms;
import com.example.myapplication.data.Repository.Auth.AuthRepository;
import com.example.myapplication.data.Repository.Property.PropertyRepository;

// cai nay de ben backend test data thoi khong quan trong dau
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        AuthRepository auth = new AuthRepository(this);
        AuthRegister register = new AuthRegister("hello@gmail.com", "12345678", "Nguyen Vu Ha Y", "0909090909");
        auth.register(
                register,
                unused -> Log.d("TEST", "Đăng ký thành công" + auth.getUserUid()),
                e -> Log.e("TEST", "Đăng ký thất bại: " + e.getMessage())
        );

        Property sampleProperty = new Property(
                "host123", // host_id
                "Biệt thự ven sông", // name
                PropertyType.Villa, // property_type
                PropertyStatus.Active, // status

                new Address(79, 769, 26899, "123 Nguyễn Văn Linh, Phường Tân Phong"), // TP.HCM, Q.7, P. Tân Phong

                new Rooms(3, 1, 1), // 3 phòng ngủ, 1 phòng khách, 1 bếp

                new Amenities(
                        2,       // 2 TV
                        true,    // có wifi
                        true,    // cho phép thú cưng
                        true,    // có hồ bơi
                        true,    // có máy giặt
                        true,    // có bữa sáng
                        true,    // có điều hòa
                        true,    // có BBQ
                        "Có bàn bida và khu vui chơi trẻ em" // more
                ),

                1200000,  // normal_price (1.200.000 VND)
                1500000,  // weekend_price
                1800000,  // holiday_price
                1000000   // deposit
        );
         */
    }
}
