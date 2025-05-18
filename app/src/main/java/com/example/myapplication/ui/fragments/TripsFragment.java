package com.example.myapplication.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Enum.Booking_status;
import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Model.Property.Property;  // Add this import
import com.example.myapplication.data.Model.Review.Review;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.example.myapplication.data.Repository.Review.ReviewRepository;
import com.example.myapplication.ui.activities.HouseDetailActivity;
import com.example.myapplication.ui.adapters.BookingAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.UUID;

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
                    bookingAdapter = new BookingAdapter(bookings,
                            new BookingAdapter.OnBookingActionListener() {
                                @Override
                                public void onActionClick(Booking booking) {
                                    if (booking.status == Booking_status.ACCEPTED) {
                                        showCancelDialog(booking);
                                    } else if (booking.status == Booking_status.COMPLETED) {
                                        navigateToReview(booking);
                                    }
                                }

                                @Override
                                public void onViewDetailsClick(Booking booking, Property property) {
                                    // Handle property details click
                                    Intent intent = new Intent(requireContext(), HouseDetailActivity.class);
                                    intent.putExtra("property_id", property.getId());
                                    startActivity(intent);
                                }
                            },
                            requireContext());

                    // Set adapter to RecyclerView
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
        showReviewDialog(booking);
    }

    private void showReviewDialog(Booking booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_create_review, null);

        RatingBar ratingBar = view.findViewById(R.id.rating_bar);
        EditText reviewContent = view.findViewById(R.id.review_content);

        builder.setView(view)
                .setTitle("Đánh giá chỗ ở")
                .setPositiveButton("Gửi đánh giá", (dialog, which) -> {
                    int rating = Math.round(ratingBar.getRating());
                    String content = reviewContent.getText().toString();

                    if (rating == 0) {
                        Toast.makeText(requireContext(), "Vui lòng chọn số sao đánh giá", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    submitReview(booking, rating, content);
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void submitReview(Booking booking, int rating, String content) {
        // Create a review using the available constructor
        // This constructor already generates a UUID for the id
        Review review = new Review(
                booking.id,
                booking.property_id,
                rating,
                content
        );

        ReviewRepository reviewRepository = new ReviewRepository(requireContext());

        // The guestReviewBooking method will handle converting Review to ReviewWithReviewerName
        reviewRepository.guestReviewBooking(review,
                unused -> {
                    Toast.makeText(requireContext(), "Đánh giá của bạn đã được gửi thành công", Toast.LENGTH_SHORT).show();
                    // Refresh the bookings list to update the UI
                    loadBookings();
                },
                e -> {
                    Toast.makeText(requireContext(), "Không thể gửi đánh giá: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}