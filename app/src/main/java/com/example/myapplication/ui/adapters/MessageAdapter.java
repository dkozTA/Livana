package com.example.myapplication.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Model.Conversation.Message;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<Message> messageList;
    private final String currentUserId; // userId hiện tại
    private final String hostId;
    private final String guestId;
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

    public MessageAdapter(List<Message> messageList, String currentUserId, String hostId, String guestId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
        this.hostId = hostId;
        this.guestId = guestId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.sender_id != null && message.sender_id.equals(currentUserId)) {
            return 1; // Tin nhắn mình gửi
        } else {
            return 2; // Tin nhắn người khác gửi
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        // Xác định người gửi là Host hay Guest
        String senderRole;
        if (message.sender_id != null) {
            if (message.sender_id.equals(hostId)) {
                senderRole = "Host";
            } else if (message.sender_id.equals(guestId)) {
                senderRole = "Guest";
            } else {
                senderRole = "Unknown";
            }
        } else {
            senderRole = "Unknown";
        }

        holder.textSender.setText(senderRole);
        holder.textMessage.setText(message.message_content);

        if (message.time != null) {
            holder.textTime.setText(dateTimeFormat.format(message.time));
        } else {
            holder.textTime.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textSender, textMessage, textTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textSender = itemView.findViewById(R.id.textSender);
            textMessage = itemView.findViewById(R.id.textMessage);
            textTime = itemView.findViewById(R.id.textTime);
        }
    }
}
