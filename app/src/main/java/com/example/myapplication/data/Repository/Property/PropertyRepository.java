package com.example.myapplication.data.Repository.Property;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Repository.FirebaseService;
import com.example.myapplication.data.Repository.Storage.StorageRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PropertyRepository {
    private final FirebaseFirestore db;
    private final StorageRepository storageRepository;
    private final String COLLECTION_NAME = "properties"; // Tên collection trong Firestore

    public PropertyRepository(Context context) {
        this.db = FirebaseService.getInstance(context).getFireStore();
        this.storageRepository = new StorageRepository(context);
    }

    public void addProperty(Property property, Uri main_image ,List<Uri> sub_images , OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        String propertyID = property.id;
        storageRepository.uploadMainImage(propertyID, main_image, mainImageUrl -> {
            property.setMainPhoto(mainImageUrl);

            // 2. Upload các ảnh phụ
            storageRepository.uploadHouseSubImages(propertyID, sub_images, subImageUrls -> {
                property.setSub_photos(subImageUrls);

                // 3. Khi cả hai xong thì mới add vào Firestore
                db.collection(COLLECTION_NAME).document(property.id)
                        .set(property)
                        .addOnSuccessListener(onSuccess)
                        .addOnFailureListener(onFailure);
            }, onFailure);

        }, onFailure);
    }

    public void addProperty(Property property, String main_image, List<String> sub_images, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        String propertyID = property.id;
        property.setMainPhoto(main_image);
        property.setSub_photos(sub_images);
        db.collection(COLLECTION_NAME).document(property.id)
                .set(property)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void updateProperty(String oldProperty_ID, Property updatedProperty, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        updatedProperty.id = oldProperty_ID;
        db.collection(COLLECTION_NAME)
                .document(oldProperty_ID)
                .set(updatedProperty)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void updatePropertyAvgRatings(String id, int point ,OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.getPropertyById(id,
                property -> {
                    double avg_rating = property.avg_ratings;
                    double total_point = avg_rating * property.total_reviews;
                    double new_avg_rating = (total_point + point) / (property.total_reviews + 1);
                    db.collection(COLLECTION_NAME).document(id).update("avg_ratings", new_avg_rating)
                            .addOnSuccessListener(onSuccess)
                            .addOnFailureListener(onFailure);
                },
                onFailure);
    }

    public void updatePropertyAvgRatingWhenReviewModified(String id, int old_point, int new_point ,OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.getPropertyById(id,
                property -> {
                    double avg_rating = property.avg_ratings;
                    double total_point = avg_rating * property.total_reviews;
                    double new_avg_rating = (total_point + new_point - old_point) / (property.total_reviews);
                    db.collection(COLLECTION_NAME).document(id).update("avg_ratings", new_avg_rating)
                            .addOnSuccessListener(onSuccess)
                            .addOnFailureListener(onFailure);
                },
                onFailure);
    }

    public void updatePropertyTotalReviews(String id, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME).document(id).update("total_reviews", FieldValue.increment(1))
                .addOnFailureListener(onFailure)
                .addOnSuccessListener(onSuccess);
    }

    public void deleteProperty(String id, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .delete()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void getAllProperties(OnSuccessListener<List<Property>> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Property> propertyList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Property property = document.toObject(Property.class);
                        propertyList.add(property);
                    }
                    onSuccess.onSuccess(propertyList);
                })
                .addOnFailureListener(onFailure);
    }

    public void getPropertyById(String id, OnSuccessListener<Property> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Property property = documentSnapshot.toObject(Property.class);
                        if (property != null) {
                            onSuccess.onSuccess(property);
                        } else {
                            onFailure.onFailure(new Exception("Property is null"));
                        }
                    } else {
                        onFailure.onFailure(new Exception("Property not found"));
                    }
                })
                .addOnFailureListener(onFailure);
    }

    public void getPropertyByName(String name, OnSuccessListener<List<Property>> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("name", name)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots  -> {
                    List<Property> propertyList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Property property = document.toObject(Property.class);
                        propertyList.add(property);
                    }
                    onSuccess.onSuccess(propertyList);
                })
                .addOnFailureListener(onFailure);
    }

    public void getPropertyByUserID(String userID, OnSuccessListener<List<Property>> onSuccess, OnFailureListener onFailure) {
        this.db.collection(COLLECTION_NAME).whereEqualTo("host_id", userID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Property> propertyList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Property property = document.toObject(Property.class);
                        propertyList.add(property);
                    }
                    onSuccess.onSuccess(propertyList);
                })
                .addOnFailureListener(onFailure);
    }

    private List<String> generateDateSeries(String startDay, String endDate) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        List<String> dateSeries = new ArrayList<>();

        try {
            LocalDate start = LocalDate.parse(startDay, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            // Kiểm tra nếu ngày bắt đầu sau ngày kết thúc
            if (start.isAfter(end)) {
                return dateSeries; // Trả về danh sách rỗng
            }

            // Lặp từ ngày bắt đầu đến ngày kết thúc
            while (!start.isAfter(end)) {
                dateSeries.add(start.format(formatter));
                start = start.plusDays(1); // Tăng thêm 1 ngày
            }
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format. Please use dd-MM-yyyy.");
        }

        return dateSeries;
    }

    private boolean validateBookedDate(List<String> existingDates, List<String> targetDates) {
        // Tạo Set để tối ưu việc kiểm tra tồn tại
        Set<String> existingDateSet = new HashSet<>(existingDates);

        // Nếu danh sách targetDates rỗng (ngày không hợp lệ hoặc startDate > endDate)
        if (targetDates.isEmpty()) {
            return false;
        }

        // Kiểm tra từng ngày trong targetDates
        for (String date : targetDates) {
            if (existingDateSet.contains(date)) {
                return false; // Có ngày đã đặt → không hợp lệ
            }
        }

        return true; // Không có ngày nào trùng → hợp lệ
    }


    // Lưu theo định dạng dd-MM-yyyy - Front end check xem ngày Start có lớn hơn ngày End không
    public void updateBookedDate(String propertyId, String startDate, String endDate, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.getPropertyById(propertyId,
                property -> {
                    List<String> dateSeries = this.generateDateSeries(startDate, endDate);
                    if(dateSeries == null) {
                        onFailure.onFailure(new Exception("Can not generate booked Date"));
                    } else {
                        if(dateSeries.isEmpty()) {
                            onFailure.onFailure(new Exception("Can not generate booked Date"));
                        } else {
                            if (validateBookedDate(property.booked_date, dateSeries)) {
                                this.db.collection(COLLECTION_NAME).document(propertyId)
                                        .update("booked_date", FieldValue.arrayUnion(dateSeries.toArray()))
                                        .addOnSuccessListener(onSuccess)
                                        .addOnFailureListener(onFailure);
                            } else {
                                onFailure.onFailure(new Exception("Booked date is invalid"));
                            }
                        }
                    }
                },
                e -> {
                    onFailure.onFailure(new Exception("Property ID is invalid"));
                });
    }

    public void removeBookedDate(String propertyID, String date, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {

    }
}
