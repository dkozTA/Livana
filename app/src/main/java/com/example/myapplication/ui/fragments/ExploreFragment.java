package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.Model.Property.AmenityStatus;
import com.example.myapplication.data.Model.Property.Property;
import com.example.myapplication.data.Repository.Property.PropertyRepository;
import com.example.myapplication.ui.misc.Post;
import com.example.myapplication.ui.adapters.PostAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {
    private EditText searchBar;
    private RecyclerView recyclerView;
    // Store property data from backend
    private PropertyRepository propertyRepository;
    // List to hold UI post items
    private List<Post> postList;
    // Adapter to display posts in RecyclerView
    private PostAdapter postAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // ... UI code:
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        searchBar = view.findViewById(R.id.search_bar);
        searchBar.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Search functionality coming soon", Toast.LENGTH_SHORT).show();
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize empty list and adapter
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList, false);
        recyclerView.setAdapter(postAdapter);

        // Create repository instance to interact with Firebase
        propertyRepository = new PropertyRepository(requireContext());
        // Start fetching data
        fetchBackendData();

        return view;
    }

    private void fetchBackendData() {
        // Call repository method to get all properties from Firestore
        propertyRepository.getAllProperties(
                // Success callback - receives List<Property> from Firebase
                new OnSuccessListener<List<Property>>() {
                    @Override
                    public void onSuccess(List<Property> properties) {
                        // Clear existing posts
                        postList.clear();

                        // Convert each Property to Post
                        for (Property property : properties) {
                            // Format price to display with $ symbol
                            String formattedPrice = "₫" + String.format("%,.0f", property.getNormal_price()) + " per night";

                            // Handle null address case
                            String title = property.address.getDetailAddress() != null ?
                                    property.address.getDetailAddress() : "No location";

                            String propertyType = property.property_type.toString();
                            int maxGuest = property.max_guess;
                            int bedRooms = property.rooms.bedRooms;
                            String livingRoomStatus = property.rooms.livingRooms.toString();
                            String kitchenStatus = property.rooms.kitchen.toString();

                            // Xử lý số ít/số nhiều
                            String guestText = maxGuest + " guest" + (maxGuest > 1 ? "s" : "");
                            String bedroomText = bedRooms + " bedroom" + (bedRooms > 1 ? "s" : "");

                            // Nếu có phòng khách, chỉ ghi "· living room"
                            String livingRoomText = "";
                            if ("available".equalsIgnoreCase(livingRoomStatus)) {
                                livingRoomText = " · living room";
                            }

                            // Nếu có phòng khách, chỉ ghi "· living room"
                            String kitchenText = "";
                            if ("available".equalsIgnoreCase(kitchenStatus)) {
                                livingRoomText = " · kitchen";
                            }

                            // Ghép chuỗi mô tả chi tiết
                            String detail = propertyType + " · " + guestText + " · " + bedroomText + livingRoomText + kitchenText;


                            // Create new Post object with property data
                            postList.add(new Post(
                                    title,                    // title
                                    property.getMainPhoto(),               // placeholder image
                                    property.name,                        // address string
                                    detail, // property type as detail
                                    "1.200 km",                          // no distance available
                                    "Available now",                 // placeholder date range
                                    formattedPrice,                 // formatted price
                                    property.total_reviews,
                                    property.avg_ratings,
                                    property.amenities
                            ));
                        }
                        // Notify adapter to refresh RecyclerView
                        postAdapter.notifyDataSetChanged();
                    }
                },
                // Failure callback - shows error toast
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(),
                                "Failed to fetch data: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}