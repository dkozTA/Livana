package com.example.myapplication.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.ui.fragments.host.BookingManageFragment;
import com.example.myapplication.ui.fragments.host.PropertyManageFragment;
import com.example.myapplication.ui.fragments.host.HostMessageFragment;
import com.example.myapplication.ui.fragments.host.StatisticFragment;
import com.example.myapplication.ui.fragments.ProfileFragment;

public class HostMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_main);

        if (savedInstanceState == null) {
            loadFragment(new PropertyManageFragment());
            updateButtonStates(R.id.button_property_manage);
        }

        setupFooterNavigation();
    }

    private void setupFooterNavigation() {
        findViewById(R.id.button_property_manage).setOnClickListener(v -> {
            loadFragment(new PropertyManageFragment());
            updateButtonStates(R.id.button_property_manage);
        });
        findViewById(R.id.button_booking_manage).setOnClickListener(v -> {
            loadFragment(new BookingManageFragment());
            updateButtonStates(R.id.button_booking_manage);
        });
        findViewById(R.id.button_message).setOnClickListener(v -> {
            loadFragment(new HostMessageFragment());
            updateButtonStates(R.id.button_message);
        });
        findViewById(R.id.button_statistic).setOnClickListener(v -> {
            loadFragment(new StatisticFragment());
            updateButtonStates(R.id.button_statistic);
        });
        findViewById(R.id.button_profile).setOnClickListener(v -> {
            loadFragment(new ProfileFragment());
            updateButtonStates(R.id.button_profile);
        });
    }

    private void updateButtonStates(int selectedButtonId) {
        int[] buttonIds = {
                R.id.button_property_manage,
                R.id.button_booking_manage,
                R.id.button_message,
                R.id.button_statistic,
                R.id.button_profile
        };
        for (int id : buttonIds) {
            findViewById(id).setSelected(id == selectedButtonId);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.host_fragment_container, fragment)
                .commit();
    }
}