package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private List<Post> postList;
    private boolean isWishlistView;

    public PostAdapter(Context context, List<Post> postList, boolean isWishlistView) {
        this.context = context;
        this.postList = postList;
        this.isWishlistView = isWishlistView;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                isWishlistView ? R.layout.item_wishlist_house : R.layout.item_explore,
                parent,
                false
        );
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.postImage.setImageResource(post.getImageResId());
        holder.location.setText(post.getLocation());
        holder.dateRange.setText(post.getDateRange());
        holder.price.setText(post.getPrice());

        if (!isWishlistView) {
            holder.distance.setText(post.getDistance());
        }

        boolean isInWishlist = WishlistManager.getInstance().isPostSaved(post);
        holder.heartButton.setImageResource(isInWishlist ?
                R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);

        holder.heartButton.setOnClickListener(v -> {
            toggleWishlist(post, holder);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HouseDetailActivity.class);
            intent.putExtra("post", post);
            context.startActivity(intent);
        });
    }

    private void toggleWishlist(Post post, PostViewHolder holder) {
        WishlistManager.getInstance().togglePost(post);
        boolean isNowInWishlist = WishlistManager.getInstance().isPostSaved(post);
        holder.heartButton.setImageResource(isNowInWishlist ?
                R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView location;
        TextView distance;
        TextView dateRange;
        TextView price;
        ImageButton heartButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
            location = itemView.findViewById(R.id.location);
            distance = itemView.findViewById(R.id.distance);
            dateRange = itemView.findViewById(R.id.date_range);
            price = itemView.findViewById(R.id.price);
            heartButton = itemView.findViewById(R.id.heart_button);
        }
    }
}