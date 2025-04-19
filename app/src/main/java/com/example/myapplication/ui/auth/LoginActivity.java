package com.example.myapplication.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Model.Auth.AuthLogin;
import com.example.myapplication.data.Repository.Auth.AuthRepository;
import com.example.myapplication.ui.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.myapplication.R;
import com.example.myapplication.data.Repository.User.UserRepository;


public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvForgotPassword;
    private FirebaseAuth firebaseAuth;
    private UserRepository userRepository;
    private AuthRepository authRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authRepository = new AuthRepository(this);
        userRepository = new UserRepository(this);

        if (authRepository.checkLogin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Không hiển thị màn login nữa
            return;
        }

        // Initialize views
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        // Set click listeners
        btnLogin.setOnClickListener(v -> loginUser());
        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator
        btnLogin.setEnabled(false);

        AuthLogin authLogin = new AuthLogin(email, password);

        // Authenticate with Firebase
        authRepository.login(authLogin,
                unused -> {
                    String uid = authRepository.getUserUid();
                    if (uid != null) {
                        userRepository.getUserByUid(uid,
                                userData -> {
                                    // Login thành công, chuyển sang MainActivity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                },
                                e -> {
                                    btnLogin.setEnabled(true);
                                    Toast.makeText(LoginActivity.this,
                                            "Lấy thông tin người dùng thất bại: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        btnLogin.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Không tìm thấy UID người dùng", Toast.LENGTH_SHORT).show();
                    }
                },
                e -> {
                    btnLogin.setEnabled(true);
                    Toast.makeText(LoginActivity.this,
                            "Đăng nhập thất bại: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void handleForgotPassword() {
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        authRepository.resetPassword(email,
                unused -> Toast.makeText(LoginActivity.this,
                        "Password reset email sent", Toast.LENGTH_SHORT).show(),
                e -> Toast.makeText(LoginActivity.this,
                        "Failed to send reset email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
