package com.example.myapplication.data.Repository.User;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.myapplication.data.Enum.Role;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Model.User.User;
import com.example.myapplication.data.Repository.FirebaseService;
import com.example.myapplication.data.Repository.Property.PropertyRepository;
import com.example.myapplication.data.Repository.Storage.StorageRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserRepository {
    private final FirebaseFirestore db;
    private final StorageRepository storageRepository;
    private final PropertyRepository propertyRepository;

    public UserRepository(Context context) {
        db = FirebaseService.getInstance(context).getFireStore();
        storageRepository = new StorageRepository(context);
        propertyRepository = new PropertyRepository(context);
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

    // Không sử dụng cho avatar nhé
    public void updateUser(String uid, String field_name, String new_field_value, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection("users").document(uid)
                .update(field_name, new_field_value)
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

    public void addRentingHistory(String userUID, String bookingID, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        if (userUID == null || userUID.isEmpty() || bookingID == null || bookingID.isEmpty()) {
            onFailure.onFailure(new IllegalArgumentException("userUID and propertyID cannot be null or empty"));
            return;
        }

        this.db.collection("users").document(userUID).update("rentingHistory", FieldValue.arrayUnion(bookingID))
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void addRecentList(String userUID, String propertyID, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        if (userUID == null || userUID.isEmpty() || propertyID == null || propertyID.isEmpty()) {
            onFailure.onFailure(new IllegalArgumentException("userUID and propertyID cannot be null or empty"));
            return;
        }
        List<String> recent_list = new ArrayList<>();
        this.getUserByUid(userUID,
                user -> {
                    recent_list.addAll(user.recent_list);
                    recent_list.remove(propertyID);
                    recent_list.add(propertyID);
                    this.db.collection("users").document(userUID).update("recent_list", recent_list)
                            .addOnSuccessListener(onSuccess)
                            .addOnFailureListener(onFailure);
                },
                onFailure);
    }
    public void addToWishList(String userUID, String propertyID, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        if (userUID == null || userUID.isEmpty() || propertyID == null || propertyID.isEmpty()) {
            onFailure.onFailure(new IllegalArgumentException("userUID and propertyID cannot be null or empty"));
            return;
        }
        List<String> wish_list = new ArrayList<>();
        this.getUserByUid(userUID,
                user -> {
                    wish_list.addAll(user.wish_list);
                    wish_list.remove(propertyID);
                    wish_list.add(propertyID);
                    this.db.collection("users").document(userUID).update("wish_list", wish_list)
                            .addOnSuccessListener(onSuccess)
                            .addOnFailureListener(onFailure);
                },
                onFailure);
    }

    public void removeFromWishList(String userUID, String propertyID, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        if (userUID == null || userUID.isEmpty() || propertyID == null || propertyID.isEmpty()) {
            onFailure.onFailure(new IllegalArgumentException("userUID and propertyID cannot be null or empty"));
            return;
        }
        this.db.collection("users").document(userUID).update("wish_list", FieldValue.arrayRemove(propertyID))
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void removeFromRecentList(String userUID, String propertyID, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        if (userUID == null || userUID.isEmpty() || propertyID == null || propertyID.isEmpty()) {
            onFailure.onFailure(new IllegalArgumentException("userUID and propertyID cannot be null or empty"));
            return;
        }
        this.db.collection("users").document(userUID).update("recent_list", FieldValue.arrayRemove(propertyID))
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void getPropertyInUserWishList(String userID, OnSuccessListener<List<Property>> onSuccess, OnFailureListener onFailure) {
        if (userID == null || userID.isEmpty()) {
            onFailure.onFailure(new IllegalArgumentException("userUID cannot be null or empty"));
            return;
        }

        this.getUserByUid(userID,
                user -> {
                    // Xử lý trường hợp danh sách wish_list trống
                    if (user.wish_list == null || user.wish_list.isEmpty()) {
                        onSuccess.onSuccess(new ArrayList<>());
                        return;
                    }

                    // Sử dụng Map để lưu trữ properties theo propertyID
                    final Map<String, Property> propertyMap = new ConcurrentHashMap<>();
                    final Set<String> failedPropertyIDs = ConcurrentHashMap.newKeySet();
                    AtomicInteger pendingCount = new AtomicInteger(user.wish_list.size());

                    for (String propertyID : user.wish_list) {
                        this.propertyRepository.getPropertyById(propertyID,
                                property -> {
                                    propertyMap.put(propertyID, property);

                                    if (pendingCount.decrementAndGet() == 0) {
                                        // Khi tất cả các yêu cầu đã hoàn thành, tạo danh sách theo đúng thứ tự
                                        List<Property> orderedProperties = new ArrayList<>();
                                        for (String id : user.wish_list) {
                                            Property prop = propertyMap.get(id);
                                            if (prop != null) {
                                                orderedProperties.add(prop);
                                            }
                                        }
                                        onSuccess.onSuccess(orderedProperties);

                                        // Log các property bị lỗi nếu có
                                        if (!failedPropertyIDs.isEmpty()) {
                                            Log.e("PropertyWishList", "Failed to fetch properties: " + failedPropertyIDs);
                                        }
                                    }
                                },
                                e -> {
                                    failedPropertyIDs.add(propertyID);

                                    if (pendingCount.decrementAndGet() == 0) {
                                        // Khi tất cả các yêu cầu đã hoàn thành, tạo danh sách theo đúng thứ tự
                                        List<Property> orderedProperties = new ArrayList<>();
                                        for (String id : user.wish_list) {
                                            Property prop = propertyMap.get(id);
                                            if (prop != null) {
                                                orderedProperties.add(prop);
                                            }
                                        }
                                        onSuccess.onSuccess(orderedProperties);

                                        // Log các property bị lỗi
                                        Log.e("PropertyWishList", "Failed to fetch properties: " + failedPropertyIDs);
                                    }
                                });
                    }
                },
                onFailure);
    }

    public void getPropertyInUserRecentList(String userID, OnSuccessListener<List<Property>> onSuccess, OnFailureListener onFailure) {
        if (userID == null || userID.isEmpty()) {
            onFailure.onFailure(new IllegalArgumentException("userUID cannot be null or empty"));
            return;
        }

        this.getUserByUid(userID,
                user -> {
                    // Xử lý trường hợp danh sách wish_list trống
                    if (user.recent_list == null || user.recent_list.isEmpty()) {
                        onSuccess.onSuccess(new ArrayList<>());
                        return;
                    }

                    // Sử dụng Map để lưu trữ properties theo propertyID
                    final Map<String, Property> propertyMap = new ConcurrentHashMap<>();
                    final Set<String> failedPropertyIDs = ConcurrentHashMap.newKeySet();
                    AtomicInteger pendingCount = new AtomicInteger(user.recent_list.size());

                    for (String propertyID : user.recent_list) {
                        this.propertyRepository.getPropertyById(propertyID,
                                property -> {
                                    propertyMap.put(propertyID, property);

                                    if (pendingCount.decrementAndGet() == 0) {
                                        // Khi tất cả các yêu cầu đã hoàn thành, tạo danh sách theo đúng thứ tự
                                        List<Property> orderedProperties = new ArrayList<>();
                                        for (String id : user.recent_list) {
                                            Property prop = propertyMap.get(id);
                                            if (prop != null) {
                                                orderedProperties.add(prop);
                                            }
                                        }
                                        onSuccess.onSuccess(orderedProperties);

                                        // Log các property bị lỗi nếu có
                                        if (!failedPropertyIDs.isEmpty()) {
                                            Log.e("PropertyWishList", "Failed to fetch properties: " + failedPropertyIDs);
                                        }
                                    }
                                },
                                e -> {
                                    failedPropertyIDs.add(propertyID);

                                    if (pendingCount.decrementAndGet() == 0) {
                                        // Khi tất cả các yêu cầu đã hoàn thành, tạo danh sách theo đúng thứ tự
                                        List<Property> orderedProperties = new ArrayList<>();
                                        for (String id : user.recent_list) {
                                            Property prop = propertyMap.get(id);
                                            if (prop != null) {
                                                orderedProperties.add(prop);
                                            }
                                        }
                                        onSuccess.onSuccess(orderedProperties);

                                        // Log các property bị lỗi
                                        Log.e("PropertyWishList", "Failed to fetch properties: " + failedPropertyIDs);
                                    }
                                });
                    }
                },
                onFailure);
    }

    public void setFCMToken(String uid, String token, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document tồn tại, thực hiện update (sẽ thêm nếu chưa có, cập nhật nếu đã có)
                        Map<String, Object> data = new HashMap<>();
                        data.put("fcm_token", token);
                        db.collection("users").document(uid)
                                .set(data, com.google.firebase.firestore.SetOptions.merge())
                                .addOnSuccessListener(onSuccess)
                                .addOnFailureListener(onFailure);
                    } else {
                        // Document không tồn tại, gọi onFailure
                        if (onFailure != null) {
                            onFailure.onFailure(new Exception("User document not found"));
                        }
                    }
                })
                .addOnFailureListener(onFailure);
    }

    public void deleteFCMToken(String uid, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.db.collection("users").document(uid).update("fcm_token", null)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void getHostNameByPropertyID(String propertyID, OnSuccessListener<String> onSuccess, OnFailureListener onFailure) {
        propertyRepository.getPropertyById(propertyID, property -> {
            this.getUserByUid(property.host_id,
                    host -> {
                        onSuccess.onSuccess(host.full_name);
                    }, e -> {
                        onFailure.onFailure(new Exception("Can not get host name"));
                    });
        }, e-> {
            onFailure.onFailure(new Exception("Can not get property"));
        });
    }

    public void updateUserRole(String uid, Role role, OnCompleteListener<Void> onCompleteListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid)
                .update("role", role.toString())
                .addOnCompleteListener(onCompleteListener);
    }
}
