package com.example.myapplication.data.Repository.Booking;

import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Repository.FirebaseService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class BookingRepository {
    private final FirebaseFirestore db;
    private final String COLLECTION_NAME = "bookings"; // Tên collection trong Firestore

    public BookingRepository(Context context) {
        this.db = FirebaseService.getInstance(context).getFireStore();
    }

    public void createBooking(Booking booking, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.db.collection(COLLECTION_NAME).document(booking.id).set(booking)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public void getAllBookingsByUserID(String userID, OnSuccessListener<List<Booking>> onSuccess, OnFailureListener onFailure) {
        this.db.collection(COLLECTION_NAME).whereEqualTo("host_id", userID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Booking> bookingList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Booking property = document.toObject(Booking.class);
                        bookingList.add(property);
                    }
                    onSuccess.onSuccess(bookingList);
                })
                .addOnFailureListener(onFailure);
    }

    public void acceptBooking(String bookingId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {

    }

    public void rejectBooking(String bookingId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {

    }

    public void completeBooking(String bookingId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {

    }

    public void cancelBooking(String bookingId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {

    }
}
