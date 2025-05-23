package com.example.myapplication.data.Repository.Property;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Repository.FirebaseService;
import com.example.myapplication.data.Repository.Storage.StorageRepository;
import com.example.myapplication.data.Repository.User.UserRepository;
import com.example.myapplication.ui.fragments.LinkValidator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

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

    /*
    public void addProperty(Property property, String main_image, List<String> sub_images, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        String propertyID = property.id;
        property.setMainPhoto(main_image);
        property.setSub_photos(sub_images);
        db.collection(COLLECTION_NAME).document(property.id)
                .set(property)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }
     */

    // Nếu main img là url rồi thì không cần làm gì
    // Nếu main img là uri thì phải up lên storage
    public void addProperty(Property property, Context context, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        String ID = UUID.randomUUID().toString();
        property.id = ID;
        List<String> sub_img = property.sub_photos;
        String main_images = property.main_photo;

        Uri main_img_uri = null;

        // kiểm tra xem main img có phải uri không
        if(LinkValidator.isValidUri(context, main_images)) {
            main_img_uri = Uri.parse(main_images);
        }

        List<Uri> sub_photos_uri = new ArrayList<>();
        List<String> sub_photos_url = new ArrayList<>();

        for(String uri : sub_img) {
            if(LinkValidator.isValidUri(context, uri)) {
               sub_photos_uri.add(Uri.parse(uri));
            } else {
                sub_photos_url.add(uri);
            }
        }

        if(main_img_uri != null) {
            this.storageRepository.uploadMainImage(ID, main_img_uri, main_img_url -> {
                property.setMainPhoto(main_img_url);
                this.storageRepository.uploadHouseSubImages(ID, sub_photos_uri, sub_img_urls -> {
                    sub_img_urls.addAll(sub_photos_url);
                    property.setSub_photos(sub_img_urls);
                    this.db.collection(COLLECTION_NAME).document(ID)
                            .set(property)
                            .addOnSuccessListener(onSuccess)
                            .addOnFailureListener(e -> {
                                onFailure.onFailure(new Exception("Can not add property into db"));
                            });
                }, e -> {
                    onFailure.onFailure(new Exception("Can not upload Sub images to Storage"));
                });
            }, e-> {
                onFailure.onFailure(new Exception("Can not upload main image to Storage"));
            });
        } else {
            this.storageRepository.uploadHouseSubImages(ID, sub_photos_uri, sub_img_urls -> {
                sub_img_urls.addAll(sub_photos_url);
                property.setSub_photos(sub_img_urls);
                this.db.collection(COLLECTION_NAME).document(ID)
                        .set(property)
                        .addOnSuccessListener(onSuccess)
                        .addOnFailureListener(onFailure);
            }, e -> {
                onFailure.onFailure(new Exception("Can not upload Sub images to Storage"));
            });
        }
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

    /*
    public void removeBookedDate(String propertyID, String startDate, String endDate, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        List<String> dates = this.getDateRange(startDate, endDate);
        this.db.collection("properties")
                .document(propertyID)
                .update("booked_date", FieldValue.arrayRemove(dates.toArray()))
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }
    */

    public void clearBookedDates(String propertyId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection("properties").document(propertyId)
                .update("booked_date", new ArrayList<String>())
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void removeBookedDates(String propertyId, String checkIn, String checkOut, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        // Fetch property, remove dates in range [checkIn, checkOut] from booked_date, and update
        db.collection("properties").document(propertyId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> bookedDates = (List<String>) documentSnapshot.get("booked_date");
                    if (bookedDates == null) bookedDates = new ArrayList<>();
                    List<String> toRemove = getDateRange(checkIn, checkOut); // implement this utility
                    bookedDates.removeAll(toRemove);
                    db.collection("properties").document(propertyId)
                            .update("booked_date", bookedDates)
                            .addOnSuccessListener(onSuccess)
                            .addOnFailureListener(onFailure);
                })
                .addOnFailureListener(onFailure);
    }

    // Utility to get all dates between checkIn and checkOut (inclusive) in dd-MM-yyyy format
    private List<String> getDateRange(String start, String end) {
        List<String> dates = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            while (!cal.getTime().after(endDate)) {
                dates.add(sdf.format(cal.getTime()));
                cal.add(Calendar.DATE, 1);
            }
        } catch (Exception e) {
            // handle parse error
        }
        return dates;
    }


}
