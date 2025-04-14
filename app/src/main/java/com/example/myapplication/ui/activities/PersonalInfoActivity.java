package com.example.myapplication.ui.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.Repository.User.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PersonalInfoActivity extends AppCompatActivity {
    private TextView nameValue;
    private TextView phoneValue;
    private TextView emailValue;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        initViews();
        setupClickListeners();
        loadUserInfo();
    }

    private void initViews() {
        nameValue = findViewById(R.id.name_value);
        phoneValue = findViewById(R.id.phone_value);
        emailValue = findViewById(R.id.email_value);
        userRepository = new UserRepository(this);
    }

    private void setupClickListeners() {
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        findViewById(R.id.btn_edit_name).setOnClickListener(v -> {
            // TODO: Navigate to name edit screen
            Toast.makeText(this, "Edit name", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_edit_phone).setOnClickListener(v -> {
            // TODO: Navigate to phone edit screen
            Toast.makeText(this, "Edit phone", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_edit_email).setOnClickListener(v -> {
            // TODO: Navigate to email edit screen
            Toast.makeText(this, "Edit email", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserInfo() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userRepository.getUserByUid(currentUser.getUid(),
                user -> {
                    if (user != null) {
                        nameValue.setText(user.full_name);
                        phoneValue.setText(user.phone_number);
                        emailValue.setText(currentUser.getEmail());
                    }
                },
                e -> Toast.makeText(this,
                        "Failed to load user info: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show()
        );
    }
}