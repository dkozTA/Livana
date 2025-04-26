package com.example.myapplication.data.Repository.Booking;

import com.example.myapplication.data.Enum.Booking_status;
import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Repository.FirebaseService;
import com.example.myapplication.data.Repository.Property.PropertyRepository;
import com.example.myapplication.data.Repository.User.UserRepository;
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
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public BookingRepository(Context context) {
        this.db = FirebaseService.getInstance(context).getFireStore();
        this.propertyRepository = new PropertyRepository(context);
        this.userRepository = new UserRepository(context);
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

    public void getBookingById(String bookingID, OnSuccessListener<Booking> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
                .document(bookingID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Booking booking = documentSnapshot.toObject(Booking.class);
                        if (booking != null) {
                            onSuccess.onSuccess(booking);
                        } else {
                            onFailure.onFailure(new Exception("Property is null"));
                        }
                    } else {
                        onFailure.onFailure(new Exception("Property not found"));
                    }
                })
                .addOnFailureListener(onFailure);
    }

    // host accept booking
    public void acceptBooking(String userID, String bookingId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.getBookingById(bookingId,
                booking -> {
                    if(booking.status == Booking_status.PENDING && booking.host_id.equals(userID)) {
                        this.propertyRepository.updateBookedDate(booking.property_id, booking.check_in_day, booking.check_out_day,
                                unused -> {
                                    this.db.collection(COLLECTION_NAME).document(bookingId).update("status", Booking_status.ACCEPTED)
                                            .addOnSuccessListener(onSuccess)
                                            .addOnFailureListener(e -> {
                                                onFailure.onFailure(new Exception("Added booked date into property but can not update status" + e.getMessage()));
                                            });
                                },
                                e -> {
                                    onFailure.onFailure(new Exception("Can not update booked date in property: " + e.getMessage()));
                                });
                    } else {
                        onFailure.onFailure(new Exception("Booking status must be PENDING"));
                    }
                },
                e -> {
                    onFailure.onFailure(new Exception("Can not get Booking by ID"));
                });
    }

    // host reject booking
    public void rejectBooking(String userID, String bookingId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.getBookingById(bookingId,
                booking -> {
                    if(booking.status == Booking_status.PENDING && booking.host_id.equals(userID)) {
                        this.db.collection(COLLECTION_NAME).document(bookingId).update("status", Booking_status.REJECTED)
                                .addOnSuccessListener(onSuccess)
                                .addOnFailureListener(onFailure);
                    } else {
                        onFailure.onFailure(new Exception("Booking status must be PENDING"));
                    }
                },
                onFailure);
    }

    // booking complete: unknown who to confirm host
    public void completeBooking(String bookingId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.getBookingById(bookingId,
                booking -> {
                    if(booking.status == Booking_status.IN_PROGRESS) {
                        this.userRepository.addRentingHistory(booking.guest_id, booking.id,
                                onSuccess,
                                e -> {
                                    onFailure.onFailure(new Exception("Can not add to user Renting History: " + e.getMessage()));
                                });

                    } else {
                        onFailure.onFailure(new Exception("Booking status must be ACCEPTED"));
                    }
                },
                onFailure);
    }

    // host or guest cancel booking
    public void cancelBooking(String userID, String bookingId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.getBookingById(bookingId,
                booking -> {
                    if(booking.status != Booking_status.COMPLETED
                            && booking.status != Booking_status.IN_PROGRESS
                            && (booking.host_id.equals(userID) || booking.guest_id.equals(userID))) {
                        this.db.collection(COLLECTION_NAME).document(bookingId).update("status", Booking_status.CANCELLED)
                                .addOnSuccessListener(onSuccess)
                                .addOnFailureListener(onFailure);
                    } else {
                        onFailure.onFailure(new Exception("Booking status must be PENDING"));
                    }
                },
                onFailure);
    }

    // Guest start their holiday Host confirm
    public void InProgressBooking(String bookingId, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        this.getBookingById(bookingId,
                booking -> {
                    if(booking.status != Booking_status.ACCEPTED) {
                        this.db.collection(COLLECTION_NAME).document(bookingId).update("status", Booking_status.IN_PROGRESS)
                                .addOnSuccessListener(onSuccess)
                                .addOnFailureListener(onFailure);
                    } else {
                        onFailure.onFailure(new Exception("Booking status must be PENDING"));
                    }
                },
                onFailure);
    }
}
