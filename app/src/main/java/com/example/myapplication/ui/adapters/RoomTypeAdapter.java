package com.example.myapplication.ui.adapters;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Enum.PropertyType;

import java.util.List;

public class RoomTypeAdapter extends RecyclerView.Adapter<RoomTypeAdapter.RoomTypeViewHolder> {

    private List<PropertyType> roomTypeList;
    private int selectedPosition = -1;
    private OnRoomTypeSelectedListener listener;

    public interface OnRoomTypeSelectedListener {
        void onRoomTypeSelected(PropertyType propertyType);
    }

    public RoomTypeAdapter(List<PropertyType> roomTypeList, OnRoomTypeSelectedListener listener) {
        this.roomTypeList = roomTypeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_property_type, parent, false);
        return new RoomTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomTypeViewHolder holder, int position) {
        PropertyType roomType = roomTypeList.get(position);
        holder.bind(roomType, position, selectedPosition, listener);

        View.OnClickListener clickListener = v -> {
            int currentPos = position;
            if (currentPos == RecyclerView.NO_POSITION) return;

            if (selectedPosition != currentPos) {
                int oldPosition = selectedPosition;
                selectedPosition = currentPos;

                if (oldPosition != -1) notifyItemChanged(oldPosition);
                notifyItemChanged(selectedPosition);
            }
        };
        holder.radioButton.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return roomTypeList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    static class RoomTypeViewHolder extends RecyclerView.ViewHolder {
        MotionLayout motionLayout;
        AppCompatRadioButton radioButton;
        ImageView icon;
        TextView title;
        TextView description;
        View border;

        RoomTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            border = itemView.findViewById(R.id.radioButton);
        }

        void bind(PropertyType roomType, int position, int selectedPosition,
                  OnRoomTypeSelectedListener listener) {
            icon.setImageResource(roomType.getIconResId());
            title.setText(roomType.toString());
            description.setText(roomType.getDescription());

            boolean isSelected = position == selectedPosition;

            // Set checked state
            radioButton.setChecked(isSelected);
            description.setVisibility(isSelected ? View.VISIBLE : View.GONE);
            border.setBackgroundResource(isSelected ? R.drawable.bg_button_expandable_selected : R.drawable.bg_button_expandable);

            // Handle click
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRoomTypeSelected(roomType);
                }
            });
        }
    }
}
