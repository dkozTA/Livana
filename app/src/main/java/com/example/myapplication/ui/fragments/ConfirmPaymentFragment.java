package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.data.Enum.Booking_status;
import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

public class ConfirmPaymentFragment extends Fragment {
    private TextView propertyTitleText;
    private TextView locationText;
    private TextView dateRangeText;
    private TextView totalPriceText;
    private ImageView propertyImageView;
    private TextView selectedPaymentMethodText;
    private Button payButton;
    private BookingRepository bookingRepository;

    private String propertyId;
    private String hostId;
    private String propertyTitle;
    private String propertyImage;
    private String propertyLocation;
    private String checkInDay;
    private String checkOutDay;
    private double totalPrice;
    private String paymentMethod;

    public ConfirmPaymentFragment() {
        super(R.layout.fragment_confirm_payment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        getArgumentData();
        setupViews();
        setupListeners();
    }

    private void initializeViews(View view) {
        propertyTitleText = view.findViewById(R.id.property_title);      // Changed from property_name
        locationText = view.findViewById(R.id.property_location);
        dateRangeText = view.findViewById(R.id.booking_dates);           // Changed from dates_guests
        totalPriceText = view.findViewById(R.id.total_price);
        propertyImageView = view.findViewById(R.id.property_image);
        selectedPaymentMethodText = view.findViewById(R.id.selected_payment_method);
        payButton = view.findViewById(R.id.next_button);
        bookingRepository = new BookingRepository(requireContext());
    }

    private void getArgumentData() {
        Bundle args = getArguments();
        if (args != null) {
            propertyId = args.getString("propertyId", "");
            hostId = args.getString("hostId", "");
            propertyTitle = args.getString("propertyTitle", "");
            propertyLocation = args.getString("propertyLocation", "");
            propertyImage = args.getString("propertyImage", "");
            checkInDay = args.getString("checkInDay", "");
            checkOutDay = args.getString("checkOutDay", "");
            totalPrice = args.getDouble("totalPrice", 0.0);
            paymentMethod = args.getString("paymentMethod", "");
        }
    }

    private void setupListeners() {
        payButton.setOnClickListener(v -> handlePayment());
    }

    private void setupViews() {
        propertyTitleText.setText(propertyTitle);
        locationText.setText(propertyLocation);
        dateRangeText.setText(String.format("%s - %s", checkInDay, checkOutDay));
        totalPriceText.setText(String.format("%,.0fđ", totalPrice));
        selectedPaymentMethodText.setText(paymentMethod);

        if (!propertyImage.isEmpty()) {
            Glide.with(requireContext())
                    .load(propertyImage)
                    .into(propertyImageView);
        }
    }

    private void handlePayment() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String bookingId = FirebaseFirestore.getInstance()
                .collection("bookings")
                .document()
                .getId();

        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("id", bookingId);
        bookingData.put("propertyId", propertyId);
        bookingData.put("guestId", currentUserId);
        bookingData.put("hostId", hostId);
        bookingData.put("checkInDay", checkInDay);
        bookingData.put("checkOutDay", checkOutDay);
        bookingData.put("totalPrice", totalPrice);
        bookingData.put("paymentMethod", paymentMethod);
        bookingData.put("note", "");
        bookingData.put("status", Booking_status.PENDING.toString());

        FirebaseFirestore.getInstance()
                .collection("bookings")
                .document(bookingId)
                .set(bookingData)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(requireContext(),
                            "Đặt phòng thành công!",
                            Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(),
                                "Đặt phòng thất bại: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }
}