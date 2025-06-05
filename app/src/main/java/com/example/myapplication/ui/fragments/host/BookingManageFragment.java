package com.example.myapplication.ui.fragments.host;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.ui.adapters.HostBookingAdapter;
import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Enum.Booking_status;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class BookingManageFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_host_booking_manage, container, false);

        recyclerCompleted = view.findViewById(R.id.recycler_completed);
        recyclerInProgress = view.findViewById(R.id.recycler_inprogress);
        recyclerUpcoming = view.findViewById(R.id.recycler_upcoming);
        recyclerCancelled = view.findViewById(R.id.recycler_cancelled);

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

        return view;
    }

    private final HostBookingAdapter.OnBookingActionListener bookingActionListener = new HostBookingAdapter.OnBookingActionListener() {
        @Override
        public void onActionClick(Booking booking) {
            handleBookingAction(booking);
        }

        @Override
        public void onViewDetailsClick(Booking booking, Property property) {
            try {
                if (property != null && property.getId() != null && !property.getId().isEmpty()) {
                    Intent intent = new Intent(requireContext(), Class.forName("com.example.myapplication.ui.activities.HouseDetailActivity"));
                    intent.putExtra("propertyId", property.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Property details unavailable", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Navigation error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };

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
                    }
                },
                e -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleBookingAction(Booking booking) {
        if (booking.status == null) return;
        switch (booking.status) {
            case ACCEPTED:
                Toast.makeText(getContext(), "Contacting guest...", Toast.LENGTH_SHORT).show();
                break;
            case IN_PROGRESS:
                Toast.makeText(getContext(), "Marking as completed...", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getContext(), "No action available", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchHostBookings();
    }
}