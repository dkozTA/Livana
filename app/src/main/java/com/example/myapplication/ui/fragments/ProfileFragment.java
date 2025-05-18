package com.example.myapplication.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.Enum.Role;
import com.example.myapplication.data.Repository.User.UserRepository;
import com.example.myapplication.ui.activities.HostMainActivity;
import com.example.myapplication.ui.activities.MainActivity;
import com.example.myapplication.ui.activities.PersonalInfoActivity;
import com.example.myapplication.ui.activities.ProfileInfoActivity;
import com.example.myapplication.ui.auth.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Context;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView profileImage;
    private TextView profileName;
    private TextView profileRole;
    private CardView profileShowcase;
    private CardView personalInfoCard;
    private CardView loginSecurityCard;
    private SwitchCompat roleSwitch;
    private UserRepository userRepository;
    private MaterialButton logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        setupClickListeners();
        loadUserProfile();
        return view;
    }

    private void initViews(View view) {
        profileImage = view.findViewById(R.id.profile_image);
        profileName = view.findViewById(R.id.profile_name);
        profileRole = view.findViewById(R.id.profile_role);
        profileShowcase = view.findViewById(R.id.profile_showcase);
        personalInfoCard = view.findViewById(R.id.personal_info_card);
        loginSecurityCard = view.findViewById(R.id.login_security_card);
        roleSwitch = view.findViewById(R.id.role_switch);
        userRepository = new UserRepository(requireContext());
        logoutButton = view.findViewById(R.id.logout_button);
    }

    private void setupClickListeners() {
        profileShowcase.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ProfileInfoActivity.class);
            startActivity(intent);
        });

        personalInfoCard.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PersonalInfoActivity.class);
            startActivity(intent);
        });

        loginSecurityCard.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Login & Security Settings", Toast.LENGTH_SHORT).show();
        });

        // Role switch listener
        roleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) { // Only respond to user interactions, not programmatic changes
                switchRole(isChecked);
            }
        });

        // Logout button
        logoutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", (dialog, which) -> signOut())
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void switchRole(boolean isHost) {
        roleSwitch.setEnabled(false); // Disable switch to prevent further changes during processing
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Please log in first", Toast.LENGTH_SHORT).show();
            return;
        }

        Role newRole = isHost ? Role.HOST : Role.USER;

        // Show loading state
        Toast.makeText(requireContext(), "Changing role...", Toast.LENGTH_SHORT).show();

        // Update user role in database
        userRepository.updateUserRole(currentUser.getUid(), newRole,
                task -> {
                    roleSwitch.setEnabled(true); // Re-enable switch after processing
                    if (task.isSuccessful()) {
                        // Role updated successfully, navigate to appropriate activity
                        if (isHost) {
                            Intent intent = new Intent(requireContext(), HostMainActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        } else {
                            Intent intent = new Intent(requireContext(), MainActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    } else {
                        // Failed to update role
                        Toast.makeText(requireContext(),
                                "Failed to change role: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();

                        // Reset switch to previous state
                        roleSwitch.setChecked(!isHost);
                    }
                }
        );
    }

    // Sign out method
    private void signOut() {
        FirebaseAuth.getInstance().signOut();

        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Please log in first", Toast.LENGTH_SHORT).show();
            return;
        }

        userRepository.getUserByUid(currentUser.getUid(),
                user -> {
                    if (user != null) {
                        profileName.setText(user.full_name);
                        profileRole.setText(user.role != null ? user.role.toString() : "USER");

                        // Set switch state based on current role without triggering the listener
                        boolean isHost = user.role == Role.HOST;
                        roleSwitch.setChecked(isHost);
                        roleSwitch.setText(isHost ? "Trở lại người dùng" : "Chuyển sang host");

                        if (user.avatar_link != null && !user.avatar_link.isEmpty()) {
                            Glide.with(requireContext())
                                    .load(user.avatar_link)
                                    .placeholder(R.drawable.photo1)
                                    .into(profileImage);
                        }
                    }
                },
                e -> Toast.makeText(requireContext(),
                        "Failed to load profile: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show()
        );
    }
}