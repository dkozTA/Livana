package com.example.myapplication.data.Repository.Auth;

import android.content.Context;

import com.example.myapplication.data.Model.Auth.AuthLogin;
import com.example.myapplication.data.Model.Auth.AuthRegister;
import com.example.myapplication.data.Model.User.User;
import com.example.myapplication.data.Repository.FirebaseService;
import com.example.myapplication.data.Repository.User.UserRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final UserRepository userRepository;

    public AuthRepository(Context context) {
        this.firebaseAuth = FirebaseService.getInstance(context).getAuth();
        this.userRepository = new UserRepository(context);
    }

    // Đăng ký
    public void register(AuthRegister authInformation, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseAuth.createUserWithEmailAndPassword(authInformation.email, authInformation.password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();
                            User newUser = new User(uid, authInformation.full_name, authInformation.phone_number);

                            userRepository.createUser(
                                    newUser,
                                    unused -> onSuccess.onSuccess(null),
                                    e -> onFailure.onFailure(new Exception("Đăng ký thành công nhưng lưu user thất bại: " + e.getMessage()))
                            );
                        } else {
                            onFailure.onFailure(new Exception("Đăng ký thành công nhưng không lấy được FirebaseUser"));
                        }
                    } else {
                        onFailure.onFailure(task.getException());
                    }
                });
    }

    //Đăng nhập
    public void login(AuthLogin authInformation, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseAuth.signInWithEmailAndPassword(authInformation.email, authInformation.password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onSuccess.onSuccess(null); // Đăng nhập không có dữ liệu trả về
                    } else {
                        onFailure.onFailure(task.getException());
                    }
                });
    }

    public void resetPassword(String email, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public String getUserUid() {
        return this.firebaseAuth.getUid();
    }

    public boolean checkLogin() {
        return this.firebaseAuth.getCurrentUser() != null;
    }

    // Dang nhap bang Google


    public void logout() {
        firebaseAuth.signOut();
    }
}
