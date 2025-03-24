package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity {
    private Button buttonExplore;
    private Button buttonWishlists;
    private Button buttonTrips;
    private Button buttonMessages;
    private Button buttonProfile;
    private List<Button> allButtons;
    EditText searchBar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize search bar
        searchBar = findViewById(R.id.search_bar);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Just show a toast for now
                Toast.makeText(Home.this, "Search functionality coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PlaceholderAdapter());

        // Initialize footer buttons
        buttonExplore = findViewById(R.id.button_explore);
        buttonWishlists = findViewById(R.id.button_wishlists);
        buttonTrips = findViewById(R.id.button_trips);
        buttonMessages = findViewById(R.id.button_messages);
        buttonProfile = findViewById(R.id.button_profile);

        // Set default selection
        buttonExplore.setSelected(true);

        // List of all buttons for easier management
        allButtons = Arrays.asList(buttonExplore, buttonWishlists, buttonTrips, buttonMessages, buttonProfile);

        // Set up click listeners for each button
        buttonExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonSelection(buttonExplore);
                // Navigate to Explore screen
                // startActivity(new Intent(Home.this, ExploreActivity.class));
            }
        });

        buttonWishlists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonSelection(buttonWishlists);
                // Navigate to Wishlists screen
                // startActivity(new Intent(Home.this, WishlistsActivity.class));
            }
        });

        buttonTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonSelection(buttonTrips);
                // Navigate to Trips screen
                // startActivity(new Intent(Home.this, TripsActivity.class));
            }
        });

        buttonMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonSelection(buttonMessages);
                // Navigate to Messages screen
                // startActivity(new Intent(Home.this, MessagesActivity.class));
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonSelection(buttonProfile);
                // Navigate to Profile screen
                // startActivity(new Intent(Home.this, ProfileActivity.class));
            }
        });
    }

    private void updateButtonSelection(Button selectedButton) {
        // Clear selection state for all buttons
        for (Button button : allButtons) {
            button.setSelected(false);
        }
        // Set selected state on the clicked button
        selectedButton.setSelected(true);
    }

    private class PlaceholderAdapter extends RecyclerView.Adapter<PlaceholderAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setPadding(32, 32, 32, 32);
            textView.setTextSize(16);
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ((TextView)holder.itemView).setText("Placeholder item " + (position + 1));
        }

        @Override
        public int getItemCount() {
            return 10; // Show 10 placeholder items
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}