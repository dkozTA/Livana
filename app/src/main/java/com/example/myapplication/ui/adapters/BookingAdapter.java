package com.example.myapplication.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Enum.Booking_status;
import com.example.myapplication.data.Model.Booking.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookings;
    private OnBookingActionListener listener;

    public interface OnBookingActionListener {
        void onActionClick(Booking booking);
    }

    public BookingAdapter(List<Booking> bookings, OnBookingActionListener listener) {
        this.bookings = bookings;
        this.listener = listener;
    }

    @Override
    public BookingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {
        private Button actionButton;
        private TextView statusText;

        BookingViewHolder(View itemView) {
            super(itemView);
            actionButton = itemView.findViewById(R.id.action_button);
            statusText = itemView.findViewById(R.id.status_text);
        }

        void bind(Booking booking) {
            // Access status directly since fields are public
            statusText.setText(booking.status.toString());

            if (booking.status == Booking_status.ACCEPTED) {
                actionButton.setText("Hủy đặt phòng");
                actionButton.setVisibility(View.VISIBLE);
            } else if (booking.status == Booking_status.COMPLETED) {
                actionButton.setText("Đánh giá");
                actionButton.setVisibility(View.VISIBLE);
            } else {
                actionButton.setVisibility(View.GONE);
            }

            actionButton.setOnClickListener(v -> listener.onActionClick(booking));
        }
    }
}