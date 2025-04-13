package com.example.myapplication.ui.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.ui.fragments.ExploreFragment;
import com.example.myapplication.ui.fragments.MessagesFragment;
import com.example.myapplication.ui.fragments.ProfileFragment;
import com.example.myapplication.ui.fragments.TripsFragment;
import com.example.myapplication.ui.fragments.WishlistFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ExploreFragment())
                    .commit();
        }

        setupFooterNavigation();
    }

    private void updateButtonStates(int selectedButtonId) {
        int[] buttonIds = {
                R.id.button_explore,
                R.id.button_wishlists,
                R.id.button_trips,
                R.id.button_messages,
                R.id.button_profile
        };

        for (int id : buttonIds) {
            findViewById(id).setSelected(id == selectedButtonId);
        }
    }

    private void setupFooterNavigation() {
        findViewById(R.id.button_explore).setOnClickListener(v -> {
            loadFragment(new ExploreFragment());
            updateButtonStates(R.id.button_explore);
        });
        findViewById(R.id.button_wishlists).setOnClickListener(v -> {
            loadFragment(new WishlistFragment());
            updateButtonStates(R.id.button_wishlists);
        });
        findViewById(R.id.button_trips).setOnClickListener(v -> {
            loadFragment(new TripsFragment());
            updateButtonStates(R.id.button_trips);
        });
        findViewById(R.id.button_messages).setOnClickListener(v -> {
            loadFragment(new MessagesFragment());
            updateButtonStates(R.id.button_messages);
        });
        findViewById(R.id.button_profile).setOnClickListener(v -> {
            loadFragment(new ProfileFragment());
            updateButtonStates(R.id.button_profile);
        });

        // Set initial state
        updateButtonStates(R.id.button_explore);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}