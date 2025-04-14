package com.example.myapplication.ui.misc;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.widget.Button;
import com.example.myapplication.R;
import com.example.myapplication.ui.fragments.ExploreFragment;
import com.example.myapplication.ui.fragments.MessagesFragment;
import com.example.myapplication.ui.fragments.ProfileFragment;
import com.example.myapplication.ui.fragments.TripsFragment;
import com.example.myapplication.ui.fragments.WishlistFragment;

public class Footer {
    public static void setupFooterNavigation(FragmentActivity activity) {
        Button buttonExplore = activity.findViewById(R.id.button_explore);
        Button buttonWishlists = activity.findViewById(R.id.button_wishlists);
        Button buttonTrips = activity.findViewById(R.id.button_trips);
        Button buttonMessages = activity.findViewById(R.id.button_messages);
        Button buttonProfile = activity.findViewById(R.id.button_profile);

        buttonExplore.setOnClickListener(v -> loadFragment(activity, new ExploreFragment()));
        buttonWishlists.setOnClickListener(v -> loadFragment(activity, new WishlistFragment()));
        buttonTrips.setOnClickListener(v -> loadFragment(activity, new TripsFragment()));
        buttonMessages.setOnClickListener(v -> loadFragment(activity, new MessagesFragment()));
        buttonProfile.setOnClickListener(v -> loadFragment(activity, new ProfileFragment()));
    }

    private static void loadFragment(FragmentActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}