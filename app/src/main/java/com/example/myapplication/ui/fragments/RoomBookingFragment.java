package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.example.myapplication.ui.misc.Post;
import com.google.firebase.auth.FirebaseAuth;

public class RoomBookingFragment extends Fragment {
    private Button nextButton;
    private RadioGroup paymentOptionsGroup;
    private TextView totalPriceText;
    private BookingRepository bookingRepository;
    private String propertyId;
    private String hostId;
    private String checkInDay;
    private String checkOutDay;
    private double totalPrice;
    private Post post;

    public RoomBookingFragment() {
        super(R.layout.fragment_room_booking);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        getArgumentData();
        setupListeners();
    }

    private void initializeViews(View view) {
        nextButton = view.findViewById(R.id.next_button);
        paymentOptionsGroup = view.findViewById(R.id.payment_options_group);
        totalPriceText = view.findViewById(R.id.total_price);
        bookingRepository = new BookingRepository(requireContext());
    }

    private void getArgumentData() {
        Bundle args = getArguments();
        if (args != null) {
            // Get property details
            propertyId = args.getString("propertyId", "");
            hostId = args.getString("hostId", "");
            String propertyTitle = args.getString("propertyTitle", "");
            String propertyLocation = args.getString("propertyLocation", "");
            String price = args.getString("price", "0đ");
            double avgRating = args.getDouble("propertyRating", 4.5);
            int totalReviews = args.getInt("totalReviews", 0);

            // Get check-in/out dates
            checkInDay = args.getString("checkInDay", "");
            checkOutDay = args.getString("checkOutDay", "");

            // Update UI
            TextView titleView = getView().findViewById(R.id.property_title);
            TextView locationView = getView().findViewById(R.id.property_location);
            TextView priceView = getView().findViewById(R.id.total_price);
            RatingBar ratingBar = getView().findViewById(R.id.rating_bar);
            TextView ratingText = getView().findViewById(R.id.rating_text);
            ImageView propertyImageView = getView().findViewById(R.id.property_image);

            titleView.setText(propertyTitle);
            locationView.setText(propertyLocation);
            priceView.setText(price);
            ratingBar.setRating((float) avgRating);
            ratingText.setText(String.format("%.1f (%d đánh giá)", avgRating, totalReviews));

            // Load and tag the image URL
            String imageUrl = args.getString("propertyImage", "");
            if (!imageUrl.isEmpty()) {
                Glide.with(requireContext())
                        .load(imageUrl)
                        .into(propertyImageView);
                propertyImageView.setTag(imageUrl);
            }

            // Update radio button text
            RadioButton fullPaymentOption = getView().findViewById(R.id.full_payment_option);
            fullPaymentOption.setText("Thanh toán toàn bộ (" + price + ")");

            // For later use in booking
            this.totalPrice = Double.parseDouble(price.replaceAll("[^\\d]", ""));
        }
    }

    private void updatePropertyInfo(String title, String location, String image, String detail) {
        // Find and update the views that display property information
        TextView propertyTitleView = getView().findViewById(R.id.property_title);
        TextView propertyLocationView = getView().findViewById(R.id.property_location);
        ImageView propertyImageView = getView().findViewById(R.id.property_image);

        propertyTitleView.setText(title);
        propertyLocationView.setText(location);
        if (image != null && !image.isEmpty()) {
            Glide.with(requireContext())
                    .load(image)
                    .into(propertyImageView);
        }
    }

    private void setupListeners() {
        nextButton.setOnClickListener(v -> navigateToPaymentMethod());
    }

    private void navigateToPaymentMethod() {
        if (paymentOptionsGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(requireContext(), "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle args = new Bundle();
        args.putString("propertyId", propertyId);  // Using existing field
        args.putString("hostId", hostId);          // Using existing field

        // Get existing views' content
        TextView titleView = getView().findViewById(R.id.property_title);
        TextView locationView = getView().findViewById(R.id.property_location);
        ImageView imageView = getView().findViewById(R.id.property_image);

        args.putString("propertyTitle", titleView.getText().toString());
        args.putString("propertyLocation", locationView.getText().toString());
        args.putString("propertyImage", imageView.getTag() != null ? imageView.getTag().toString() : "");
        args.putString("checkInDay", checkInDay);
        args.putString("checkOutDay", checkOutDay);
        args.putDouble("totalPrice", totalPrice);

        // If you need guests count, add it as a class field or get it from a view
        args.putString("guestsInfo", "2 khách"); // Default value

        PaymentMethodFragment paymentFragment = new PaymentMethodFragment();
        paymentFragment.setArguments(args);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                )
                .replace(R.id.fragment_container, paymentFragment)
                .addToBackStack(null)
                .commit();
    }
}