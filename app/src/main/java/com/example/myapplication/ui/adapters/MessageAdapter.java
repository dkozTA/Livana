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
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final String hostId;
    private final String guestId;

    public MessageAdapter(List<Message> messageList, String hostId, String guestId) {
        this.messageList = messageList;
        this.hostId = hostId;
        this.guestId = guestId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        // Xác định vai trò người gửi
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
            holder.textTime.setText(timeFormat.format(message.time));
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