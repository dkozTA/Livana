package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.Enum.Booking_status;
import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.example.myapplication.ui.misc.Post;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.os.Parcel;
import com.google.android.material.datepicker.CalendarConstraints.DateValidator;

public class RoomBookingFragment extends Fragment {
    private Button nextButton;
    private RadioGroup paymentOptionsGroup;
    private TextView totalPriceText;
    private BookingRepository bookingRepository;
    private String propertyId = "";
    private String hostId;
    private String checkInDay;
    private String checkOutDay;
    private double totalPrice;
    private Post post;
    private String selectedPaymentTiming;
    private TextView datesGuestsText;
    private Button changeButton;
    private EditText guestNoteInput;
    private int guestCount = 1;
    private TextView guestsCountText;
    private java.util.List<String> bookedDates = new java.util.ArrayList<>();

    public RoomBookingFragment() {
        super(R.layout.fragment_room_booking);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            propertyId = savedInstanceState.getString("propertyId", "");
        }
        getArgumentData();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("propertyId", propertyId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        updateUI(view);
        setupListeners();
    }

    private void initializeViews(View view) {
        nextButton = view.findViewById(R.id.next_button);
        paymentOptionsGroup = view.findViewById(R.id.payment_options_group);
        totalPriceText = view.findViewById(R.id.total_price);
        bookingRepository = new BookingRepository(requireContext());
        paymentOptionsGroup = view.findViewById(R.id.payment_options_group);
        datesGuestsText = view.findViewById(R.id.dates_guests);
        changeButton = view.findViewById(R.id.change_button);
        guestNoteInput = view.findViewById(R.id.guest_note_input);
    }

    private void getArgumentData() {
        Bundle args = getArguments();
        if (args != null) {
            propertyId = args.getString("propertyId", "");
            hostId = args.getString("hostId", "");
            checkInDay = args.getString("checkInDay", "");
            checkOutDay = args.getString("checkOutDay", "");
            guestCount = args.getInt("guestCount", 1);
            totalPrice = Double.parseDouble(args.getString("price", "0").replaceAll("[^\\d]", ""));
        }
    }

    private void updateUI(View view) {
        Bundle args = getArguments();
        if (args != null) {
            // Update property details
            TextView titleView = view.findViewById(R.id.property_title);
            TextView locationView = view.findViewById(R.id.property_location);
            TextView priceView = view.findViewById(R.id.total_price);
            RatingBar ratingBar = view.findViewById(R.id.rating_bar);
            TextView ratingText = view.findViewById(R.id.rating_text);
            ImageView propertyImageView = view.findViewById(R.id.property_image);

            titleView.setText(args.getString("propertyTitle", ""));
            locationView.setText(args.getString("propertyLocation", ""));
            String price = args.getString("price", "0đ");
            totalPrice = Double.parseDouble(price.replaceAll("\\D", ""));
            priceView.setText(price);

            double avgRating = args.getDouble("propertyRating", 4.5);
            int totalReviews = args.getInt("totalReviews", 0);
            ratingBar.setRating((float) avgRating);
            ratingText.setText(String.format("%.1f (%d đánh giá)", avgRating, totalReviews));

            // Load image
            String imageUrl = args.getString("propertyImage", "");
            if (!imageUrl.isEmpty()) {
                Glide.with(requireContext())
                        .load(imageUrl)
                        .into(propertyImageView);
                propertyImageView.setTag(imageUrl);
            }

            // Update payment options
            RadioButton fullPaymentOption = view.findViewById(R.id.full_payment_option);
            fullPaymentOption.setText("Thanh toán toàn bộ (" + price + ")");

            // Update dates and guests if available
            if (!checkInDay.isEmpty() && !checkOutDay.isEmpty()) {
                updateDatesAndGuestsDisplay();
            }
        }
    }

    private void setupListeners() {
        changeButton.setOnClickListener(v -> showDateAndGuestPicker());
        paymentOptionsGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = getView().findViewById(checkedId);
            if (selectedButton != null) {
                selectedPaymentTiming = selectedButton.getId() == R.id.full_payment_option ?
                        "Thanh toán toàn bộ hôm nay" : "Thanh toán một phần bây giờ, phần còn lại sau";
            }
        });

        nextButton.setOnClickListener(v -> {
            if (checkInDay.isEmpty() || checkOutDay.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
                return;
            }
            if (paymentOptionsGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(requireContext(), "Vui lòng chọn cách thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Calendar cal = Calendar.getInstance();
            boolean overlap = false;
            try {
                Date start = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(checkInDay);
                Date end = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(checkOutDay);
                for (long date = start.getTime(); date <= end.getTime(); date += 24 * 60 * 60 * 1000) {
                    cal.setTimeInMillis(date);
                    String dateStr = sdf.format(cal.getTime());
                    // Fetch bookedDates from property (cache it after picker)
                    if (bookedDates != null && bookedDates.contains(dateStr)) {
                        overlap = true;
                        break;
                    }
                }
            } catch (Exception e) {
                overlap = true;
            }
            if (overlap) {
                Toast.makeText(requireContext(), "Khoảng ngày đã chọn có ngày đã được đặt. Vui lòng chọn lại.", Toast.LENGTH_SHORT).show();
                return;
            }
            navigateToPaymentMethod();
        });
    }

    private String convertDateFormat(String dateStr, String fromFormat, String toFormat) {
        try {
            SimpleDateFormat src = new SimpleDateFormat(fromFormat, Locale.getDefault());
            SimpleDateFormat dest = new SimpleDateFormat(toFormat, Locale.getDefault());
            Date date = src.parse(dateStr);
            return dest.format(date);
        } catch (Exception e) {
            Log.e("RoomBookingFragment", "Date format conversion error: " + e.getMessage());
            return dateStr;
        }
    }

    private void showDateAndGuestPicker() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_date_guest_picker, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(dialogView);

        Button datePickerBtn = dialogView.findViewById(R.id.date_picker_button);
        TextView dateRangeText = dialogView.findViewById(R.id.date_range_text);
        if (!checkInDay.isEmpty() && !checkOutDay.isEmpty()) {
            dateRangeText.setText(String.format("%s - %s", checkInDay, checkOutDay));
        }

        TextView guestCountText = dialogView.findViewById(R.id.guest_count_text);
        Button decreaseBtn = dialogView.findViewById(R.id.decrease_guest_button);
        Button increaseBtn = dialogView.findViewById(R.id.increase_guest_button);
        guestCountText.setText(String.valueOf(guestCount));

        // Use PropertyRepository to get property and booked_date
        com.example.myapplication.data.Repository.Property.PropertyRepository propertyRepository =
                new com.example.myapplication.data.Repository.Property.PropertyRepository(requireContext());

        datePickerBtn.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long today = calendar.getTimeInMillis();

            propertyRepository.getPropertyById(propertyId, property -> {
                bookedDates = property.booked_date != null ? property.booked_date : new java.util.ArrayList<>(); // dd-MM-yyyy

                CalendarConstraints.DateValidator bookedDatesValidator = new DateValidator() {
                    @Override
                    public boolean isValid(long date) {
                        if (date < today) return false;
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(date);
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());
                        String dateStr = sdf.format(cal.getTime());
                        return bookedDates == null || !bookedDates.contains(dateStr);
                    }
                    @Override public int describeContents() { return 0; }
                    @Override public void writeToParcel(android.os.Parcel dest, int flags) {}
                };

                constraintsBuilder.setStart(today);
                constraintsBuilder.setValidator(bookedDatesValidator);
                builder.setCalendarConstraints(constraintsBuilder.build());

                MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
                picker.addOnPositiveButtonClickListener(selection -> {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());
                    Calendar cal = Calendar.getInstance();
                    boolean overlap = false;
                    for (long date = selection.first; date <= selection.second; date += 24 * 60 * 60 * 1000) {
                        cal.setTimeInMillis(date);
                        String dateStr = sdf.format(cal.getTime());
                        if (bookedDates != null && bookedDates.contains(dateStr)) {
                            overlap = true;
                            break;
                        }
                    }
                    if (overlap) {
                        Toast.makeText(requireContext(), "Khoảng ngày đã chọn có ngày đã được đặt. Vui lòng chọn lại.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Only update if no overlap
                        sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
                        checkInDay = sdf.format(new java.util.Date(selection.first));
                        checkOutDay = sdf.format(new java.util.Date(selection.second));
                        dateRangeText.setText(String.format("%s - %s", checkInDay, checkOutDay));
                    }
                });

                picker.show(getChildFragmentManager(), picker.toString());
            }, e -> Toast.makeText(requireContext(), "Failed to load booked dates", Toast.LENGTH_SHORT).show());
        });

        decreaseBtn.setOnClickListener(v -> {
            if (guestCount > 1) {
                guestCount--;
                guestCountText.setText(String.valueOf(guestCount));
            }
        });

        increaseBtn.setOnClickListener(v -> {
            if (guestCount < 10) {
                guestCount++;
                guestCountText.setText(String.valueOf(guestCount));
            }
        });

        Button confirmButton = dialogView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            updateDatesAndGuestsDisplay();
            dialog.dismiss();
        });

        dialog.show();
    }


    // kiểm tra xem ngày đã được đặt hay chưa và k cho chọn ngày trong quá khứ
    private void checkDateAvailability(Long startDate, Long endDate, TextView dateRangeText) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String checkIn = sdf.format(new Date(startDate));
        String checkOut = sdf.format(new Date(endDate));

        // First check if selected start date is before today
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        try {
            Date selectedStart = sdf.parse(checkIn);
            if (selectedStart.before(today.getTime())) {
                Toast.makeText(requireContext(),
                        "Không thể chọn ngày trong quá khứ",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Continue with availability check
            bookingRepository.getBookingsByPropertyId(propertyId,
                    bookings -> {
                        boolean isAvailable = true;
                        for (Booking booking : bookings) {
                            if (booking.status == Booking_status.ACCEPTED ||
                                    booking.status == Booking_status.IN_PROGRESS) {
                                try {
                                    Date bookedStart = sdf.parse(booking.check_in_day);
                                    Date bookedEnd = sdf.parse(booking.check_out_day);
                                    Date selectedEnd = sdf.parse(checkOut);

                                    // Check for any overlap
                                    boolean overlap = !(selectedEnd.before(bookedStart) || selectedStart.after(bookedEnd));
                                    boolean containsBookedDates = selectedStart.before(bookedStart) && selectedEnd.after(bookedEnd);

                                    if (overlap || containsBookedDates) {
                                        isAvailable = false;
                                        break;
                                    }
                                } catch (ParseException e) {
                                    Log.e("RoomBookingFragment", "Date parsing error: " + e.getMessage());
                                }
                            }
                        }

                        if (isAvailable) {
                            checkInDay = checkIn;
                            checkOutDay = checkOut;
                            dateRangeText.setText(String.format("%s - %s", checkInDay, checkOutDay));
                        } else {
                            Toast.makeText(requireContext(),
                                    "Ngày đã được đặt. Vui lòng chọn ngày khác.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    },
                    e -> Toast.makeText(requireContext(),
                            "Lỗi kiểm tra ngày: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show()
            );
        } catch (ParseException e) {
            Log.e("RoomBookingFragment", "Date parsing error: " + e.getMessage());
        }
    }

    private void updateDatesAndGuestsDisplay() {
        String guestText = guestCount == 1 ? "1 khách" : guestCount + " khách";
        datesGuestsText.setText(String.format("%s - %s • %s", checkInDay, checkOutDay, guestText));

        // Calculate total price based on nights
        if (!checkInDay.isEmpty() && !checkOutDay.isEmpty()) {
            long nights = calculateNights(checkInDay, checkOutDay);
            double newTotalPrice = totalPrice * nights;
            TextView priceView = getView().findViewById(R.id.total_price);
            String formattedPrice = "₫" + String.format("%,.0f", newTotalPrice);
            priceView.setText(formattedPrice);

            // Update payment option text
            RadioButton fullPaymentOption = getView().findViewById(R.id.full_payment_option);
            fullPaymentOption.setText("Thanh toán toàn bộ (" + formattedPrice + ")");
        }
    }

    private void navigateToPaymentMethod() {
        Bundle args = new Bundle();
        TextView titleView = getView().findViewById(R.id.property_title);
        TextView locationView = getView().findViewById(R.id.property_location);
        ImageView imageView = getView().findViewById(R.id.property_image);

        long nights = calculateNights(checkInDay, checkOutDay);
        double finalPrice = totalPrice * nights;

        // Convert date format before passing to next fragment
        String checkInDayFormatted = convertDateFormat(checkInDay, "dd/MM/yyyy", "dd-MM-yyyy");
        String checkOutDayFormatted = convertDateFormat(checkOutDay, "dd/MM/yyyy", "dd-MM-yyyy");

        args.putString("propertyId", propertyId);
        args.putString("hostId", hostId);
        args.putString("propertyTitle", titleView != null ? titleView.getText().toString() : "");
        args.putString("propertyLocation", locationView != null ? locationView.getText().toString() : "");
        args.putString("propertyImage", imageView != null && imageView.getTag() != null ? imageView.getTag().toString() : "");
        args.putString("checkInDay", checkInDayFormatted);
        args.putString("checkOutDay", checkOutDayFormatted);
        args.putDouble("totalPrice", finalPrice);
        args.putString("paymentTiming", selectedPaymentTiming);
        args.putInt("guestCount", guestCount);
        args.putString("guestNote", guestNoteInput != null ? guestNoteInput.getText().toString().trim() : "");

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

    private long calculateNights(String checkIn, String checkOut) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date checkInDate = sdf.parse(checkIn);
            Date checkOutDate = sdf.parse(checkOut);

            long diffInMillies = Math.abs(checkOutDate.getTime() - checkInDate.getTime());
            return diffInMillies / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            Log.e("RoomBookingFragment", "Date parsing error: " + e.getMessage());
            return 0;
        }
    }
}