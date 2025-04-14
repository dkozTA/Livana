package com.example.myapplication.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.myapplication.R;
import com.example.myapplication.data.Model.User.User;
import com.example.myapplication.data.Repository.User.UserRepository;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProfileInfoActivity extends AppCompatActivity {
    private ImageView profileImage;
    private TextView profileName;
    private TextView profilePhone;
    private TextView profileRole;
    private TextView profileCreatedAt;
    private TextView profileRentingHistory;
    private TextView profileWishList;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        initViews();
        loadUserProfile();
    }

    private void initViews() {
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        profilePhone = findViewById(R.id.profile_phone);
        profileRole = findViewById(R.id.profile_role);
        profileCreatedAt = findViewById(R.id.profile_created_at);
        profileRentingHistory = findViewById(R.id.profile_renting_history);
        profileWishList = findViewById(R.id.profile_wish_list);
        userRepository = new UserRepository(this);
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userRepository.getUserByUid(currentUser.getUid(),
                user -> {
                    if (user != null) {
                        // Set user data
                        profileName.setText(user.full_name);
                        profilePhone.setText(user.phone_number);
                        profileRole.setText(user.role != null ? user.role.toString() : "USER");

                        // Format and set creation date
                        if (user.created_at != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                            profileCreatedAt.setText(sdf.format(user.created_at));
                        } else {
                            profileCreatedAt.setText("Not available");
                        }

                        // Set collections
                        String rentingHistory = (user.rentingHistory != null && !user.rentingHistory.isEmpty()) ?
                                String.join(", ", user.rentingHistory) : "No renting history";
                        profileRentingHistory.setText(rentingHistory);

                        String wishList = (user.wish_list != null && !user.wish_list.isEmpty()) ?
                                String.join(", ", user.wish_list) : "No items in wish list";
                        profileWishList.setText(wishList);

                        // Load profile image
                        if (user.avatar_link != null && !user.avatar_link.isEmpty()) {
                            Glide.with(this)
                                    .load(user.avatar_link)
                                    .placeholder(R.drawable.photo1)
                                    .into(profileImage);
                        }
                    }
                },
                e -> Toast.makeText(this,
                        "Failed to load profile: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show()
        );
    }
}