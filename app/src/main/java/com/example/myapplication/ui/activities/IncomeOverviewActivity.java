package com.example.myapplication.ui.activities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class IncomeOverviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_overview); // layout bạn đã thiết kế

        ImageButton backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> finish());

        ImageButton closeButton = findViewById(R.id.btnClose);
        closeButton.setOnClickListener(v -> finish());
    }
}
