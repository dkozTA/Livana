package com.example.myapplication.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy Data
        postList = new ArrayList<>();
        postList.add(new Post(R.drawable.avatar_icon, "Tambon Huai Sat Yai, Thailand",
                "1,165 kilometres away", "6–11 Apr", "₫5,795,858 for 5 nights"));
        postList.add(new Post(R.drawable.avatar_icon, "Bali, Indonesia",
                "2,400 kilometres away", "10–15 Apr", "₫8,500,000 for 5 nights"));

        // 🔹 Gán Adapter vào RecyclerView
        adapter = new PostAdapter(this, postList);
        recyclerView.setAdapter(adapter);
    }
}
