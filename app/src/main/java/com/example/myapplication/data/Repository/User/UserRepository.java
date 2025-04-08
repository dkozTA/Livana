package com.example.myapplication.data.Repository.User;

import android.content.Context;
import android.net.Uri;

import com.example.myapplication.data.Model.User.User;
import com.example.myapplication.data.Repository.FirebaseService;
import com.example.myapplication.data.Repository.Storage.StorageRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {
    private final FirebaseFirestore db;
    private final StorageRepository storageRepository;
    public UserRepository(Context context) {
        db = FirebaseService.getInstance(context).getFireStore();
        storageRepository = new StorageRepository(context);
    }

    // ➕ Tạo user mới
    public void createUser(User user, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        String uid = user.uid;
        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // 🔍 Lấy thông tin 1 user theo UID
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

    public void updateUser(User user, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        String uid = user.uid;
        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void updateAvatar(String uid, Uri avatar_img, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.storageRepository.uploadUserAvatar(uid, avatar_img,
                avatar_url -> {
                    db.collection("users").document(uid).update("avatar_link", avatar_url)
                    .addOnSuccessListener(onSuccess)
                    .addOnFailureListener(onFailure);
                },
                onFailure
        );
    }
}
