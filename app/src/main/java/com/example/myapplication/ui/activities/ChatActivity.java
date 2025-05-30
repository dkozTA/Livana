package com.example.myapplication.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.Model.Conversation.Message;
import com.example.myapplication.data.Repository.Auth.AuthRepository;
import com.example.myapplication.data.Repository.Conversation.ConversationRepository;
import com.example.myapplication.ui.adapters.MessageAdapter;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerMessages;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private ConversationRepository conversationRepository;
    private ListenerRegistration messageListener;

    private EditText editMessage;
    private Button buttonSend;
    private TextView title;

    private String conversationID; // = "ZRw1V7w6VdW6Uafb7DMC"; // <-- thay id thật vào đây
    private String sender_ID;
    private AuthRepository authRepository;
    private CircleImageView conversationAvatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        conversationID = getIntent().getStringExtra("CONVERSATION_ID");

        authRepository = new AuthRepository(this);
        this.sender_ID = authRepository.getUserUid();

        recyclerMessages = findViewById(R.id.recyclerMessages);
        conversationRepository = new ConversationRepository(this);

        title = findViewById(R.id.textTitle);
        conversationAvatar = findViewById(R.id.conversationAvatar);

        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));

        conversationRepository.getConversationById(conversationID, conversation -> {
            if (conversation != null && conversation.messages != null) {
                messageList = conversation.messages;
                messageAdapter = new MessageAdapter(messageList, sender_ID , conversation.host_id, conversation.guest_id, conversation.avatar_url, this);
                recyclerMessages.setAdapter(messageAdapter);
                recyclerMessages.smoothScrollToPosition(messageList.size() - 1);
                title.setText(conversation.name);
                // Set avatar using Glide
                if (conversation.avatar_url != null && !conversation.avatar_url.isEmpty()) {
                    Glide.with(conversationAvatar.getContext())
                            .load(conversation.avatar_url)
                            .placeholder(R.drawable.avatar_placeholder)
                            .error(R.drawable.avatar_placeholder) // Fallback image khi load lỗi
                            .circleCrop() // Optional: Crop ảnh thành hình tròn
                            .into(conversationAvatar);
                } else {
                    conversationAvatar.setImageResource(R.drawable.avatar_placeholder);
                }
            } else {
                Toast.makeText(this, "No messages found", Toast.LENGTH_SHORT).show();
            }
        }, e -> {
            Toast.makeText(this, "Failed to load conversation!", Toast.LENGTH_SHORT).show();
        });

        ImageButton backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> finish());


        messageListener = conversationRepository.listenForNewMessages(
                conversationID,
                newMessage -> {
                    if (messageList != null) {
                        messageList.add(newMessage);
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        recyclerMessages.smoothScrollToPosition(messageList.size() - 1);
                    }
                },
                error -> {
                    Log.e("Firestore", "Error listening for new messages", error);
                }
        );
        editMessage = findViewById(R.id.editMessage);
        buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(v -> {
            String content = editMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                Message newMessage = new Message(content, sender_ID);
                conversationRepository.sendMessage(conversationID, newMessage,
                        unused -> {
                            // Gửi thành công
                            editMessage.setText(""); // Xóa ô nhập sau khi gửi
                        },
                        e -> {
                            // Gửi thất bại
                            Toast.makeText(this, "Gửi tin nhắn thất bại", Toast.LENGTH_SHORT).show();
                        }
                );
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messageListener != null) {
            messageListener.remove();
            messageListener = null;
        }
    }
}