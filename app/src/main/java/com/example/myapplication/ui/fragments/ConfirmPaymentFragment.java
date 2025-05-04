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
    private TextView paymentScheduleText;
    private String paymentTiming;
    private String guestNote;

    public ConfirmPaymentFragment() {
        super(R.layout.fragment_confirm_payment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookingRepository = new BookingRepository(requireContext());
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
        paymentScheduleText = view.findViewById(R.id.payment_schedule);
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
            paymentTiming = args.getString("paymentTiming", "");
            guestNote = args.getString("guestNote", "");
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
        paymentScheduleText.setText(paymentTiming);

        if (!propertyImage.isEmpty()) {
            Glide.with(requireContext())
                    .load(propertyImage)
                    .into(propertyImageView);
        }
    }

    private void handlePayment() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Booking booking = new Booking(
                propertyId,      // property_id
                currentUserId,   // guest_id
                hostId,         // host_id
                checkInDay,      // check_in_day
                checkOutDay,     // check_out_day
                totalPrice,      // total_price
                guestNote        // guest_note
        );

        // Use BookingRepository to create the booking
        bookingRepository.createBooking(booking,
                unused -> {
                    Toast.makeText(requireContext(),
                            "Đặt phòng thành công!",
                            Toast.LENGTH_SHORT).show();
                    new android.os.Handler().postDelayed(
                            () -> requireActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.slide_in_right,
                                            R.anim.slide_out_left,
                                            R.anim.slide_in_left,
                                            R.anim.slide_out_right
                                    )
                                    .replace(R.id.fragment_container, new com.example.myapplication.ui.fragments.TripsFragment())
                                    .addToBackStack(null)
                                    .commit(),
                            1000 // 1 second delay
                    );
                },
                e -> Toast.makeText(requireContext(),
                        "Đặt phòng thất bại: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show()
        );
    }
}