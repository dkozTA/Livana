package com.example.myapplication.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;

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

    private void setupFooterNavigation() {
        findViewById(R.id.button_explore).setOnClickListener(v ->
                loadFragment(new ExploreFragment()));
        findViewById(R.id.button_wishlists).setOnClickListener(v ->
                loadFragment(new WishlistsFragment()));
        findViewById(R.id.button_trips).setOnClickListener(v ->
                loadFragment(new TripsFragment()));
        findViewById(R.id.button_messages).setOnClickListener(v ->
                loadFragment(new MessagesFragment()));
        findViewById(R.id.button_profile).setOnClickListener(v ->
                loadFragment(new ProfileFragment()));
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}