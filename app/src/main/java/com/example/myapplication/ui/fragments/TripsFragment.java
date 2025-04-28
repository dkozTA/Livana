package com.example.myapplication.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Enum.Booking_status;
import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.example.myapplication.ui.activities.HouseDetailActivity;
import com.example.myapplication.ui.adapters.BookingAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class TripsFragment extends Fragment {
    private RecyclerView bookingRecyclerView;
    private BookingAdapter bookingAdapter;
    private BookingRepository bookingRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);
        bookingRecyclerView = view.findViewById(R.id.booking_recycler_view);
        bookingRepository = new BookingRepository(requireContext());
        loadBookings();
        return view;
    }

    private void loadBookings() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bookingRepository.getAllBookingByGuestID(userId,
                bookings -> {
                    bookingAdapter = new BookingAdapter(bookings, booking -> {
                        // Access status directly
                        if (booking.status == Booking_status.ACCEPTED) {
                            showCancelDialog(booking);
                        } else if (booking.status == Booking_status.COMPLETED) {
                            navigateToReview(booking);
                        }
                    });
                    bookingRecyclerView.setAdapter(bookingAdapter);
                },
                e -> Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void showCancelDialog(Booking booking) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Hủy đặt phòng")
                .setMessage("Bạn có chắc muốn hủy đặt phòng này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    bookingRepository.cancelBooking(userId, booking.getId(),
                            unused -> {
                                Toast.makeText(requireContext(), "Đã hủy đặt phòng", Toast.LENGTH_SHORT).show();
                                loadBookings(); // Reload the list
                            },
                            e -> Toast.makeText(requireContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void navigateToReview(Booking booking) {
        Intent intent = new Intent(requireContext(), HouseDetailActivity.class);
        intent.putExtra("property_id", booking.property_id);
        intent.putExtra("show_review", true);
        intent.putExtra("booking_id", booking.id);
        startActivity(intent);
    }
}