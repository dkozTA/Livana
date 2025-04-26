package com.example.myapplication.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Model.Conversation.Message;
import com.example.myapplication.data.Repository.Conversation.ConversationRepository;
import com.example.myapplication.ui.adapters.MessageAdapter;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ConversationRepository repository;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private ListenerRegistration listenerRegistration;

    private String conversationId = "ZRw1V7w6VdW6Uafb7DMC"; // Bạn truyền id cuộc trò chuyện
    private String currentUserId = "v4528ioquLTQbtmKYieS3quQUsp2"; // Bạn gán ID người gửi hiện tại vào đây

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        repository = new ConversationRepository(this);

        RecyclerView recyclerMessages = findViewById(R.id.recyclerMessages);
        EditText edtMessage = findViewById(R.id.edtMessage);
        Button btnSend = findViewById(R.id.btnSend);

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerMessages.setAdapter(messageAdapter);

        // Gửi tin nhắn
        btnSend.setOnClickListener(v -> {
            String content = edtMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                Message message = new Message(content, currentUserId);
                repository.sendMessage(conversationId, message,
                        unused -> edtMessage.setText(""),
                        e -> e.printStackTrace());
            }
        });

        // Nghe tin nhắn realtime
        listenerRegistration = repository.listenForMessages(conversationId,
                newMessage -> {
                    messages.add(newMessage);
                    messageAdapter.notifyItemInserted(messages.size() - 1);
                    recyclerMessages.scrollToPosition(messages.size() - 1);
                },
                e -> e.printStackTrace());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}