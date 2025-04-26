package com.example.myapplication.ui.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.data.Repository.User.UserRepository;
import com.example.myapplication.ui.fragments.ExploreFragment;
import com.example.myapplication.ui.fragments.MessagesFragment;
import com.example.myapplication.ui.fragments.ProfileFragment;
import com.example.myapplication.ui.fragments.TripsFragment;
import com.example.myapplication.ui.fragments.WishlistFragment;
import com.example.myapplication.ui.misc.WishlistManager;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private UserRepository userRepository;
    private WishlistManager wishlistManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}