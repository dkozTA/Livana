package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.example.myapplication.R;
import com.example.myapplication.data.Model.User.User;
import com.example.myapplication.data.Repository.User.UserRepository;
import java.text.SimpleDateFormat;
import java.util.Locale;
import com.example.myapplication.data.Enum.Role;

public class ProfileFragment extends Fragment {
    private ImageView profileImage;
    private TextView profileName;
    private TextView profilePhone;
    private TextView profileRole;
    private TextView profileCreatedAt;
    private TextView profileRentingHistory;
    private TextView profileWishList;
    private UserRepository userRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        profileImage = view.findViewById(R.id.profile_image);
        profileName = view.findViewById(R.id.profile_name);
        profilePhone = view.findViewById(R.id.profile_phone);
        profileRole = view.findViewById(R.id.profile_role);
        profileCreatedAt = view.findViewById(R.id.profile_created_at);
        profileRentingHistory = view.findViewById(R.id.profile_renting_history);
        profileWishList = view.findViewById(R.id.profile_wish_list);

        // Initialize repository
        userRepository = new UserRepository(requireContext());

        // Load user data
        loadUserProfile();

        return view;
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Please log in first", Toast.LENGTH_SHORT).show();
            return;
        }

        userRepository.getUserByUid(currentUser.getUid(),
                user -> {
                    if (user != null) {
                        // Set basic info
                        profileName.setText(user.full_name);
                        profilePhone.setText(user.phone_number);

                        // Set default role as USER if null
                        if (user.role == null) {
                            user.role = Role.USER;
                            userRepository.updateUser(user,
                                    aVoid -> profileRole.setText(user.role.toString()),
                                    e -> Toast.makeText(getContext(),
                                            "Failed to update role: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show()
                            );
                        } else {
                            profileRole.setText(user.role.toString());
                        }

                        // Format and set creation date with null check
                        if (user.created_at != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                            profileCreatedAt.setText(sdf.format(user.created_at));
                        } else {
                            profileCreatedAt.setText("Not available");
                        }

                        // Set renting history with null check
                        String rentingHistory = (user.rentingHistory != null && !user.rentingHistory.isEmpty()) ?
                                String.join(", ", user.rentingHistory) : "No renting history";
                        profileRentingHistory.setText(rentingHistory);

                        // Set wish list with null check
                        String wishList = (user.wish_list != null && !user.wish_list.isEmpty()) ?
                                String.join(", ", user.wish_list) : "No items in wish list";
                        profileWishList.setText(wishList);

                        // Load profile image
                        if (user.avatar_link != null && !user.avatar_link.isEmpty()) {
                            Glide.with(requireContext())
                                    .load(user.avatar_link)
                                    .placeholder(R.drawable.photo1)
                                    .into(profileImage);
                        } else {
                            profileImage.setImageResource(R.drawable.photo1);
                        }
                    }
                },
                e -> Toast.makeText(getContext(),
                        "Failed to load profile: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show()
        );
    }
}