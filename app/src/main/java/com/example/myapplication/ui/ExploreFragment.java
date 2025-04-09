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
                "Stunning bamboo tree house in cat garden",
                R.drawable.photo1,
                "Treehouse in Thailand, Thailand",
                "4 guests . 1 bedroom . 1 bathroom",
                "2,400 kilometers away",
                "Apr 15–20",
                "₫8,500,000 per night"
        ));

        posts.add(new Post(
                "Bamboo villa with forest view",
                R.drawable.photo1,
                "Treehouse in Thailand, Thailand",
                "4 guests . 2 bedrooms . 2 bathrooms",
                "2,100 kilometers away",
                "Apr 18–23",
                "₫7,900,000 per night"
        ));

        posts.add(new Post(
                "Hidden jungle treehouse retreat",
                R.drawable.photo1,
                "Treehouse in Thailand, Thailand",
                "2 guests . 1 bedroom . 1 bathroom",
                "2,800 kilometers away",
                "Apr 22–27",
                "₫8,200,000 per night"
        ));

        posts.add(new Post(
                "Eco bamboo lodge in nature",
                R.drawable.photo1,
                "Treehouse in Thailand, Thailand",
                "3 guests . 1 bedroom . 1 bathroom",
                "1,950 kilometers away",
                "Apr 25–30",
                "₫8,000,000 per night"
        ));

        posts.add(new Post(
                "Luxury treehouse in Chiang Mai",
                R.drawable.photo1,
                "Treehouse in Thailand, Thailand",
                "4 guests . 2 bedrooms . 2 bathrooms",
                "2,600 kilometers away",
                "May 1–6",
                "₫9,000,000 per night"
        ));

        posts.add(new Post(
                "Romantic bamboo stay with cats",
                R.drawable.photo1,
                "Treehouse in Thailand, Thailand",
                "2 guests . 1 bedroom . 1 bathroom",
                "2,000 kilometers away",
                "May 5–10",
                "₫7,500,000 per night"
        ));

        posts.add(new Post(
                "Peaceful bamboo house escape",
                R.drawable.photo1,
                "Treehouse in Thailand, Thailand",
                "4 guests . 1 bedroom . 1 bathroom",
                "2,300 kilometers away",
                "May 10–15",
                "₫8,300,000 per night"
        ));

        posts.add(new Post(
                "Treehouse hideout near waterfall",
                R.drawable.photo1,
                "Treehouse in Thailand, Thailand",
                "3 guests . 1 bedroom . 1 bathroom",
                "2,500 kilometers away",
                "May 15–20",
                "₫8,100,000 per night"
        ));

        return posts;
    }

}