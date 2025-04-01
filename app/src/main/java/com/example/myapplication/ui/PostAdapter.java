package com.example.myapplication.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.location.setText(post.getLocation());
        holder.distance.setText(post.getDistance());
        holder.dateRange.setText(post.getDateRange());
        holder.price.setText(post.getPrice());

        // 🔹 Hiển thị ảnh
        holder.postImage.setImageResource(post.getImageResId());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView location, distance, dateRange, price;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
            location = itemView.findViewById(R.id.location);
            distance = itemView.findViewById(R.id.distance);
            dateRange = itemView.findViewById(R.id.date_range);
            price = itemView.findViewById(R.id.price);
        }
    }
}
