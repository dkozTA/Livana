package com.example.myapplication.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.myapplication.ui.activities.PersonalInfoActivity;
import com.example.myapplication.ui.activities.ProfileInfoActivity;
import com.google.android.material.button.MaterialButton;
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
import android.app.AlertDialog;
import android.content.Context;
import com.google.android.material.button.MaterialButton;
import com.example.myapplication.ui.auth.LoginActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView profileImage;
    private TextView profileName;
    private TextView profileRole;
    private CardView profileShowcase;
    private LinearLayout personalInfoCard;
    private LinearLayout loginSecurityCard;
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
        userRepository = new UserRepository(requireContext());
        logoutButton = view.findViewById(R.id.logout_button);
    }

    private void setupClickListeners() {
        profileShowcase.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ProfileInfoActivity.class);
            startActivity(intent);
        });

        personalInfoCard.setOnClickListener(v -> {
            // TODO: Navigate to personal info edit screen
            Intent intent = new Intent(requireContext(), PersonalInfoActivity.class);
            startActivity(intent);
        });

        loginSecurityCard.setOnClickListener(v -> {
            // TODO: Navigate to login & security screen
            Toast.makeText(requireContext(), "Login & Security Settings", Toast.LENGTH_SHORT).show();
        });

        // Dang xuat
        logoutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", (dialog, which) -> signOut())
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    // ham dang xuat
    private void signOut() {
        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut();

        // Clear shared preferences
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Navigate to login screen
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