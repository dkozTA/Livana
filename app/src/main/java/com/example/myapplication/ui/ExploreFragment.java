package com.example.myapplication.ui;

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
import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {
    private EditText searchBar;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        searchBar = view.findViewById(R.id.search_bar);
        searchBar.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Search functionality coming soon", Toast.LENGTH_SHORT).show();
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = createFakeData();
        postAdapter = new PostAdapter(getContext(), postList, false);
        recyclerView.setAdapter(postAdapter);

        return view;
    }

    private List<Post> createFakeData() {
        List<Post> posts = new ArrayList<>();

        posts.add(new Post(
                R.drawable.photo1,
                "Luxury Villa in Bali",
                "2,400 kilometers away",
                "Apr 15-20",
                "₫8,500,000 per night"
        ));

        posts.add(new Post(
                R.drawable.photo1,
                "Beachfront Resort Phuket",
                "1,500 kilometers away",
                "Apr 20-25",
                "₫6,200,000 per night"
        ));

        posts.add(new Post(
                R.drawable.photo1,
                "Mountain View Cottage",
                "800 kilometers away",
                "May 1-6",
                "₫4,800,000 per night"
        ));

        posts.add(new Post(
                R.drawable.photo1,
                "City Center Apartment",
                "500 kilometers away",
                "May 10-15",
                "₫3,500,000 per night"
        ));

        return posts;
    }
}