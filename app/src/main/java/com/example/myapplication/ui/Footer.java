package com.example.myapplication.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

public class Footer {

    public static void setupFooterNavigation(Activity activity) {
        Button buttonExplore = activity.findViewById(R.id.button_explore);
        Button buttonWishlists = activity.findViewById(R.id.button_wishlists);
        Button buttonTrips = activity.findViewById(R.id.button_trips);
        Button buttonMessages = activity.findViewById(R.id.button_messages);
        Button buttonProfile = activity.findViewById(R.id.button_profile);

        buttonExplore.setOnClickListener(v -> {
            if (!(activity instanceof Explore)) {
                activity.startActivity(new Intent(activity, Explore.class));
            }
        });

        buttonWishlists.setOnClickListener(v -> {
            if (!(activity instanceof WishlistsActivity)) {
                activity.startActivity(new Intent(activity, WishlistsActivity.class));
            }
        });

        buttonTrips.setOnClickListener(v -> {
            if (!(activity instanceof TripsActivity)) {
                activity.startActivity(new Intent(activity, TripsActivity.class));
            }
        });

        buttonMessages.setOnClickListener(v -> {
            if (!(activity instanceof MessagesActivity)) {
                activity.startActivity(new Intent(activity, MessagesActivity.class));
            }
        });

        buttonProfile.setOnClickListener(v -> {
            if (!(activity instanceof ProfileActivity)) {
                activity.startActivity(new Intent(activity, ProfileActivity.class));
            }
        });
    }
}