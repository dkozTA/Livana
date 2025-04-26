package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Model.Conversation.Message;
import com.example.myapplication.data.Repository.Conversation.ConversationRepository;
import com.example.myapplication.ui.adapters.MessageAdapter;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerMessages;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private ConversationRepository conversationRepository;

    private String conversationID = "ZRw1V7w6VdW6Uafb7DMC"; // <-- thay id thật vào đây

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerMessages = findViewById(R.id.recyclerMessages);
        conversationRepository = new ConversationRepository(this);

        // Setup RecyclerView
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));

        // Step 1: Lấy Conversation từ Firestore
        conversationRepository.getConversationById(conversationID, conversation -> {
            // Step 2: Lấy danh sách tin nhắn từ Conversation
            if (conversation != null && conversation.messages != null) {
                messageList = conversation.messages; // Lấy danh sách tin nhắn
                messageAdapter = new MessageAdapter(messageList);
                recyclerMessages.setAdapter(messageAdapter);
            } else {
                Toast.makeText(this, "No messages found", Toast.LENGTH_SHORT).show();
            }
        }, e -> {
            Toast.makeText(this, "Failed to load conversation!", Toast.LENGTH_SHORT).show();
        });

        // Lắng nghe tin nhắn mới
        conversationRepository.listenForNewMessages(conversationID,
                newMessage -> {
                    // Cập nhật danh sách tin nhắn khi có tin nhắn mới
                    if (messageList != null) {
                        messageList.add(newMessage); // Thêm tin nhắn mới vào danh sách
                        messageAdapter.notifyItemInserted(messageList.size() - 1); // Thông báo cho adapter về tin nhắn mới
                        recyclerMessages.smoothScrollToPosition(messageList.size() - 1); // Cuộn xuống cuối danh sách
                    }
                },
                error -> {
                    // Xử lý lỗi
                    Log.e("Firestore", "Error listening for new messages", error);
                }
        );
    }
}
