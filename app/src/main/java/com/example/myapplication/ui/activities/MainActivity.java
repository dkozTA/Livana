package com.example.myapplication.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.myapplication.R;
import com.example.myapplication.data.Enum.Role;
import com.example.myapplication.data.Repository.User.UserRepository;
import com.example.myapplication.ui.auth.LoginActivity;
import com.example.myapplication.ui.fragments.ExploreFragment;
import com.example.myapplication.ui.fragments.MessagesFragment;
import com.example.myapplication.ui.fragments.ProfileFragment;
import com.example.myapplication.ui.fragments.TripsFragment;
import com.example.myapplication.ui.fragments.WishlistFragment;
import com.example.myapplication.ui.misc.WishlistManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.core.splashscreen.SplashScreen;

public class MainActivity extends AppCompatActivity {
    private UserRepository userRepository;
    private WishlistManager wishlistManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Install splash screen
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // Check user and redirect before showing content
        userRepository = new UserRepository(this);
        checkUserAndRedirect();

        // Only set content view if we're not redirecting
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        // Initialize UserRepository and WishlistManager
        userRepository = new UserRepository(this);
        wishlistManager = WishlistManager.getInstance();

        // Load user wishlist (get current user's UID)
        loadUserWishlist();

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

    private void loadUserWishlist() {
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("WishlistLoad", "Loading wishlist for user: " + userUID);

        wishlistManager.loadUserWishlist(
                userUID,
                userRepository,
                unused -> {
                    // Sau khi load xong, cập nhật giao diện hoặc adapter nếu cần
                    Log.d("WishlistLoad", "Wishlist loaded successfully.");
                    // Nếu cần cập nhật lại giao diện hoặc adapter, làm ở đây
                },
                e -> {
                    Log.e("WishlistLoad", "Lỗi khi load wishlist", e);
                }
        );
    }

    private void checkUserAndRedirect() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            // User is not logged in, go to login screen
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        userRepository.getUserByUid(currentUser.getUid(),
                user -> {
                    if (user != null && user.role == Role.HOST) {
                        // We're in MainActivity and need to go to HostMainActivity
                        startActivity(new Intent(this, HostMainActivity.class));
                        finish();
                    }
                },
                e -> {
                    // Error occurred, just continue in current activity
                }
        );
    }
}