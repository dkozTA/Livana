package com.example.myapplication.data.Repository.User;

import android.content.Context;

import com.example.myapplication.data.Model.User.User;
import com.example.myapplication.data.Repository.FirebaseService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {
    private final FirebaseFirestore db;

    public UserRepository(Context context) {
        db = FirebaseService.getInstance(context).getFireStore();
    }

    // ➕ Tạo user mới
    public void createUser(User user, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        String uid = user.uid;
        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // 🔍 Lấy user theo UID
    public void getUserByUid(String uid, OnSuccessListener<User> onSuccess, OnFailureListener onFailure) {
        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            user.uid = documentSnapshot.getId(); // gán lại UID nếu cần
                            onSuccess.onSuccess(user);
                        } else {
                            onFailure.onFailure(new Exception("User data is null"));
                        }
                    } else {
                        onFailure.onFailure(new Exception("User not found"));
                    }
                })
                .addOnFailureListener(onFailure);
    }
}
