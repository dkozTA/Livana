package com.example.myapplication.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.data.Model.Conversation.Conversation;
import com.example.myapplication.data.Repository.Auth.AuthRepository;
import com.example.myapplication.data.Repository.Conversation.ConversationRepository;
import com.example.myapplication.ui.adapters.ConversationListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class ConversationListActivity extends AppCompatActivity implements ConversationListAdapter.OnConversationClickListener {

    private RecyclerView recyclerView;
    private ConversationListAdapter adapter;
    private ConversationRepository conversationRepository;
    private List<Conversation> conversationList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String currentUserId; // Should be obtained from authentication
    private boolean isHost; // Flag to determine if user is host or guest

    ImageView menuIcon;
    ImageView searchIcon;
    ImageView moreIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewConversations);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Initialize repository and data list
        conversationRepository = new ConversationRepository(this);
        conversationList = new ArrayList<>();

        // Get current user ID (would normally come from authentication)
        // For demo purposes, you might pass this as an intent extra or use Firebase Auth
        AuthRepository auth = new AuthRepository(this);
        currentUserId = auth.getUserUid(); // Replace with actual user ID

        // For demo purposes, set isHost. In production, this might be determined by user role
        isHost = true; // Set to false, assuming we're in guest mode as per your request

        // Setup RecyclerView
        setupRecyclerView();

        // Setup refresh listener
        swipeRefreshLayout.setOnRefreshListener(this::loadConversations);

        // Load conversations
        loadConversations();

        menuIcon = findViewById(R.id.menuIcon);
        searchIcon = findViewById(R.id.searchIcon);
        moreIcon = findViewById(R.id.moreIcon);

        menuIcon.setOnClickListener(v -> {
            // Xử lý khi click vào icon menu
        });

        searchIcon.setOnClickListener(v -> {
            // Xử lý khi click vào icon tìm kiếm
        });

        moreIcon.setOnClickListener(v -> {
            // Xử lý khi click vào icon more options
        });
    }

    private void setupRecyclerView() {
        adapter = new ConversationListAdapter(conversationList, currentUserId, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadConversations() {
        swipeRefreshLayout.setRefreshing(true);

        OnSuccessListener<List<Conversation>> onSuccess = conversations -> {
            conversationList.clear();
            conversationList.addAll(conversations);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);

            if (conversationList.isEmpty()) {
                findViewById(R.id.emptyStateView).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.emptyStateView).setVisibility(View.GONE);
            }
        };

        OnFailureListener onFailure = e -> {
            Toast.makeText(this, "Failed to load conversations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        };

        // Get conversations based on user role (host or guest)
        if (isHost) {
            conversationRepository.getAllConversationByHostID(currentUserId, onSuccess, onFailure);
        } else {
            conversationRepository.getAllConversationByGuestID(currentUserId, onSuccess, onFailure);
        }
    }

    @Override
    public void onConversationClick(Conversation conversation) {
        // Forward to chat details screen with conversation ID
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("CONVERSATION_ID", conversation.id);
        startActivity(intent);
    }
}
