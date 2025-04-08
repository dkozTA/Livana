package com.example.myapplication.data.Repository.Property;

import android.content.Context;
import android.net.Uri;

import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Repository.FirebaseService;
import com.example.myapplication.data.Repository.Storage.StorageRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PropertyRepository {
    private final FirebaseFirestore db;
    private final StorageRepository storageRepository;
    private final String COLLECTION_NAME = "properties"; // Tên collection trong Firestore

    public PropertyRepository(Context context) {
        this.db = FirebaseService.getInstance(context).getFireStore();
        this.storageRepository = new StorageRepository(context);
    }

    public void addProperty(Property property, Uri main_image ,List<Uri> sub_images , OnSuccessListener<DocumentReference> onSuccess, OnFailureListener onFailure) {
        storageRepository.uploadMainImage(main_image, mainImageUrl -> {
            property.setMainPhoto(mainImageUrl);

            // 2. Upload các ảnh phụ
            storageRepository.uploadHouseSubImages(sub_images, subImageUrls -> {
                property.setSub_photos(subImageUrls);

                // 3. Khi cả hai xong thì mới add vào Firestore
                db.collection(COLLECTION_NAME)
                        .add(property)
                        .addOnSuccessListener(onSuccess)
                        .addOnFailureListener(onFailure);

            }, onFailure);

        }, onFailure);
    }

    public void updateProperty(String id, Property updatedProperty, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .set(updatedProperty)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
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
}
