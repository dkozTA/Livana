package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Enum.PropertyStatus;
import com.example.myapplication.data.Enum.PropertyType;
import com.example.myapplication.data.Model.Auth.AuthRegister;
import com.example.myapplication.data.Model.Property.Address;
import com.example.myapplication.data.Model.Property.Amenities;
import com.example.myapplication.data.Model.Property.AmenityStatus;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Model.Property.Rooms;
import com.example.myapplication.data.Repository.Auth.AuthRepository;
import com.example.myapplication.data.Repository.Property.PropertyRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        v4528ioquLTQbtmKYieS3quQUsp2
        */
        Property sampleProperty = new Property(
                "host789", // host_id (giả định)
                "Họa Giấy 1 - Một Homestay Sóc Sơn", // name
                PropertyType.Villa, // property_type
                PropertyStatus.Active, // status

                new Address(1, 123, 45678, "Thôn Lâm Trường, xã Minh Phú, huyện Sóc Sơn"), // Hà Nội, Sóc Sơn, Minh Phú

                new Rooms(2, AmenityStatus.Available, AmenityStatus.Available), // 2 phòng ngủ, 1 phòng khách, 1 bếp
                2,
                new Amenities(
                        AmenityStatus.Available,     // 1 TV
                        AmenityStatus.Available,    // có wifi
                        AmenityStatus.Available,   // không cho phép thú cưng
                        AmenityStatus.Available,    // có hồ bơi
                        AmenityStatus.Available,    // có máy giặt
                        AmenityStatus.Available,   // không có bữa sáng
                        AmenityStatus.Available,    // có điều hòa
                        AmenityStatus.Available,    // có BBQ
                        "View rừng thông, có sân nướng, xích đu, lò sưởi ngoài trời", // more
                        "Không gây ồn ào sau 22h tối và tắt đèn trước 2 giờ sáng"
                ),

                2000000,  // normal_price (1.000.000 VND)
                2300000,  // weekend_price
                2600000,  // holiday_price
                1800000    // deposit
        );

        String mainImage = "https://cdn.justfly.vn/2048x1364/media/202406/20/1718849102-justfly-hoa-giay-1-mot-homestay-soc-ha-noi-5.jpg";

        List<String> sub_images = Arrays.asList(
                "https://cdn.justfly.vn/2048x1364/media/202406/20/1718849099-justfly-hoa-giay-1-mot-homestay-soc-ha-noi-2.jpg",
                "https://cdn.justfly.vn/2048x1366/media/202406/20/1718849116-justfly-hoa-giay-1-mot-homestay-soc-ha-noi-18.jpg",
                "https://cdn.justfly.vn/2048x1366/media/202406/20/1718849111-justfly-hoa-giay-1-mot-homestay-soc-ha-noi-13.jpg",
                "https://cdn.justfly.vn/1366x2048/media/202406/20/1718849118-justfly-hoa-giay-1-mot-homestay-soc-ha-noi-20.jpg",
                "https://cdn.justfly.vn/2048x1364/media/202406/20/1718849104-justfly-hoa-giay-1-mot-homestay-soc-ha-noi-7.jpg",
                "https://cdn.justfly.vn/2048x1364/media/202406/20/1718849100-justfly-hoa-giay-1-mot-homestay-soc-ha-noi-3.jpg",
                "https://cdn.justfly.vn/2048x1366/media/202406/20/1718849115-justfly-hoa-giay-1-mot-homestay-soc-ha-noi-17.jpg",
                "https://cdn.justfly.vn/2048x1366/media/202406/20/1718849120-justfly-hoa-giay-1-mot-homestay-soc-ha-noi-22.jpg",
                "https://cdn.justfly.vn/2048x1366/media/202406/20/1718849110-justfly-hoa-giay-1-mot-homestay-soc-ha-noi-12.jpg",
                "https://cdn.justfly.vn/2048x1366/media/202406/20/1718849112-justfly-hoa-giay-1-mot-homestay-soc-ha-noi-14.jpg",
                "https://cdn.justfly.vn/2048x1366/media/202406/20/1718849123-justfly-hoa-giay-1-mot-homestay-soc-ha-noi-24.jpg"
        );


        PropertyRepository propertyRepository = new PropertyRepository(this);
        propertyRepository.addProperty(sampleProperty, mainImage, sub_images,
                unused -> {
                    Log.d("TEST", "Thêm thành công " + sampleProperty.id);
                },
                e -> {
                    Log.e("TEST", "Thêm thất bại " + e.getMessage());
                });
    }
}
