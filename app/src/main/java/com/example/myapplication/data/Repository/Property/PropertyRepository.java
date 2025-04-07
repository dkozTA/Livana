package com.example.myapplication.data.Repository.Property;

import android.content.Context;

import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Repository.FirebaseService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PropertyRepository {
    private final FirebaseFirestore db;
    private final String COLLECTION_NAME = "properties"; // Tên collection trong Firestore

    public PropertyRepository(Context context) {
        this.db = FirebaseService.getInstance(context).getFireStore();
    }

    public void addProperty(Property property, OnSuccessListener<DocumentReference> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
                .add(property)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
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
                        property.id = document.getId(); // set id
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
                            property.id = documentSnapshot.getId(); // set id
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
}
