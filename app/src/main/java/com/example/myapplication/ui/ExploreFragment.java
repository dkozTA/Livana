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

        postList = new ArrayList<>();
        postList.add(new Post(R.drawable.photo1, "Tambon Huai Sat Yai, Thailand",
                "1,165 kilometres away", "6–11 Apr", "₫5,795,858 for 5 nights"));
        postList.add(new Post(R.drawable.avatar_icon, "Bali, Indonesia",
                "2,400 kilometres away", "10–15 Apr", "₫8,500,000 for 5 nights"));

        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

        return view;
    }
}