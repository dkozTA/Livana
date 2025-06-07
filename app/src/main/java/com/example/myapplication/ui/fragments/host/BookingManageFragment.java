package com.example.myapplication.ui.fragments.host;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.Repository.User.UserRepository;
import com.example.myapplication.ui.adapters.HostBookingAdapter;
import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Enum.Booking_status;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookingManageFragment extends Fragment {
    private View rootView;

    private RecyclerView recyclerCompleted, recyclerInProgress, recyclerUpcoming, recyclerCancelled;
    private HostBookingAdapter adapterCompleted, adapterInProgress, adapterUpcoming, adapterCancelled;
    private List<Booking> listCompleted = new ArrayList<>();
    private List<Booking> listInProgress = new ArrayList<>();
    private List<Booking> listUpcoming = new ArrayList<>();
    private List<Booking> listCancelled = new ArrayList<>();
    private BookingRepository bookingRepository;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingRepository = new BookingRepository(requireContext());
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_host_booking_manage, container, false);

        recyclerCompleted = rootView.findViewById(R.id.recycler_completed);
        recyclerCompleted = rootView.findViewById(R.id.recycler_completed);
        recyclerInProgress = rootView.findViewById(R.id.recycler_inprogress);
        recyclerUpcoming = rootView.findViewById(R.id.recycler_upcoming);
        recyclerCancelled = rootView.findViewById(R.id.recycler_cancelled);

        recyclerCompleted.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerInProgress.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCancelled.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterCompleted = new HostBookingAdapter(listCompleted, bookingActionListener, getContext());
        adapterInProgress = new HostBookingAdapter(listInProgress, bookingActionListener, getContext());
        adapterUpcoming = new HostBookingAdapter(listUpcoming, bookingActionListener, getContext());
        adapterCancelled = new HostBookingAdapter(listCancelled, bookingActionListener, getContext());

        recyclerCompleted.setAdapter(adapterCompleted);
        recyclerInProgress.setAdapter(adapterInProgress);
        recyclerUpcoming.setAdapter(adapterUpcoming);
        recyclerCancelled.setAdapter(adapterCancelled);

        fetchHostBookings();

        return rootView;
    }

    private final HostBookingAdapter.OnBookingActionListener bookingActionListener = new HostBookingAdapter.OnBookingActionListener() {

        @Override
        public void onViewDetailsClick(Booking booking, Property property) {
            showBookingDetailDialog(booking, property);
        }
    };

    private void showBookingDetailDialog(Booking booking, Property property) {
        if (booking == null || property == null) {
            Toast.makeText(getContext(), "Booking details unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create dialog with custom style
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_host_booking_detail);

        // Make dialog background transparent
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Get screen width
            int screenWidth = getResources().getDisplayMetrics().widthPixels;

            // Set dialog width to 90% of screen width
            int dialogWidth = (int) (screenWidth * 0.9);

            dialog.getWindow().setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // Initialize dialog views
        TextView propertyName = dialog.findViewById(R.id.host_detail_property_name);
        TextView bookingStatus = dialog.findViewById(R.id.host_detail_status);
        TextView bookingDates = dialog.findViewById(R.id.host_detail_booking_dates);
        TextView guestName = dialog.findViewById(R.id.host_detail_guest_name);
        TextView totalPrice = dialog.findViewById(R.id.host_detail_total_price);
        TextView bookingId = dialog.findViewById(R.id.host_detail_booking_id);
        com.google.android.material.button.MaterialButton closeButton = dialog.findViewById(R.id.btn_close_host_dialog);

        // Set data to views
        propertyName.setText(property.getName());
        bookingStatus.setText(getStatusText(booking.status));

        String dateRange = booking.check_in_day + " - " + booking.check_out_day;
        bookingDates.setText(dateRange);

        // Show guest name
        if (booking.guest_id != null) {
            // Fetch the guest name from the UserRepository
            fetchGuestName(booking.guest_id, guestName);
        } else {
            guestName.setText("Khách: Unknown");
        }

        // Format price properly without $ sign and avoid scientific notation
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedPrice = numberFormat.format(booking.total_price) + " VND";
        totalPrice.setText(formattedPrice);

        bookingId.setText("Mã đặt phòng: " + booking.id);

        // Set up close button
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void fetchGuestName(String userId, TextView textView) {
        UserRepository userRepository = new UserRepository(requireContext());
        userRepository.getUserNameByUid(userId,
                name -> {
                    if (name != null && isAdded()) {
                        textView.setText("Khách: " + name);
                    }
                },
                error -> {
                    if (isAdded()) {
                        Log.e("BookingManageFragment", "Error fetching user: " + error.getMessage());
                        textView.setText("Khách: " + userId);
                    }
                });
    }

    private String getStatusText(Booking_status status) {
        if (status == null) return "Pending";

        switch (status) {
            case IN_PROGRESS:
                return "Đang tiến hành";
            case ACCEPTED:
                return "Đã xác nhận";
            case COMPLETED:
                return "Đã hoàn thành";
            case CANCELLED:
                return "Đã hủy";
            case REVIEWED:
                return "Đã đánh giá";
            default:
                return status.toString();
        }
    }

    private void fetchHostBookings() {
        if (firebaseAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }
        String hostId = firebaseAuth.getCurrentUser().getUid();
        Log.d("hostId", hostId);
        bookingRepository.getAllBookingsByHostID(hostId,
                bookings -> {
                    if (isAdded()) {
                        listCompleted.clear();
                        listInProgress.clear();
                        listUpcoming.clear();
                        listCancelled.clear();
                        Log.d("booking", bookings.size() + "");
                        if (bookings != null) {
                            for (Booking booking : bookings) {
                                if (booking.status == Booking_status.COMPLETED || booking.status == Booking_status.REVIEWED) {
                                    listCompleted.add(booking);
                                } else if (booking.status == Booking_status.IN_PROGRESS) {
                                    listInProgress.add(booking);
                                } else if (booking.status == Booking_status.ACCEPTED) {
                                    listUpcoming.add(booking);
                                } else if (booking.status == Booking_status.CANCELLED) {
                                    listCancelled.add(booking);
                                }
                            }
                        }
                        adapterCompleted.notifyDataSetChanged();
                        adapterInProgress.notifyDataSetChanged();
                        adapterUpcoming.notifyDataSetChanged();
                        adapterCancelled.notifyDataSetChanged();

                        showEmptyStateIfNeeded(rootView);
                    }
                },
                e -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showEmptyStateIfNeeded(View view) {
        // Get the empty state TextViews
        TextView emptyCompleted = view.findViewById(R.id.empty_completed);
        TextView emptyInProgress = view.findViewById(R.id.empty_inprogress);
        TextView emptyUpcoming = view.findViewById(R.id.empty_upcoming);
        TextView emptyCancelled = view.findViewById(R.id.empty_cancelled);

        // Set visibility based on list size
        if (emptyCompleted != null) {
            emptyCompleted.setVisibility(listCompleted.isEmpty() ? View.VISIBLE : View.GONE);
            emptyCompleted.setText("Không có đặt phòng nào");
        }

        if (emptyInProgress != null) {
            emptyInProgress.setVisibility(listInProgress.isEmpty() ? View.VISIBLE : View.GONE);
            emptyInProgress.setText("Không có đặt phòng nào");
        }

        if (emptyUpcoming != null) {
            emptyUpcoming.setVisibility(listUpcoming.isEmpty() ? View.VISIBLE : View.GONE);
            emptyUpcoming.setText("Không có đặt phòng nào");
        }

        if (emptyCancelled != null) {
            emptyCancelled.setVisibility(listCancelled.isEmpty() ? View.VISIBLE : View.GONE);
            emptyCancelled.setText("Không có đặt phòng nào");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchHostBookings();
    }
}