package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Explore extends AppCompatActivity {
    private Button buttonExplore;
    private Button buttonWishlists;
    private Button buttonTrips;
    private Button buttonMessages;
    private Button buttonProfile;
    private List<Button> allButtons;
    EditText searchBar;
    private RecyclerView recyclerView;

    private PostAdapter postAdapter;

    private List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);  // This is your main activity layout

        // Initialize search bar
        searchBar = findViewById(R.id.search_bar);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Just show a toast for now
                Toast.makeText(Explore.this, "Search functionality coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(new PlaceholderAdapter());
        // Dummy Data
        postList = new ArrayList<>();
        postList.add(new Post(R.drawable.photo1, "Tambon Huai Sat Yai, Thailand",
                "1,165 kilometres away", "6–11 Apr", "₫5,795,858 for 5 nights"));
        postList.add(new Post(R.drawable.avatar_icon, "Bali, Indonesia",
                "2,400 kilometres away", "10–15 Apr", "₫8,500,000 for 5 nights"));

        // 🔹 Gán Adapter vào RecyclerView
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);
        // Set up footer navigation
        Footer.setupFooterNavigation(this);
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