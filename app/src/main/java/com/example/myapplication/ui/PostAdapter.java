package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.utils.DialogUtils;

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

        // 1. Cập nhật icon trái tim theo trạng thái wishlist
        boolean isInWishlist = WishlistManager.getInstance().isPostInAnyWishlist(post);
        holder.heartButton.setImageResource(isInWishlist ?
                R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);

        // 2. Xử lý khi người dùng nhấn vào trái tim
        holder.heartButton.setOnClickListener(v -> {
            boolean isCurrentlySaved = WishlistManager.getInstance().isPostInAnyWishlist(post);

            if (!isCurrentlySaved) {
                // Nếu post chưa có trong wishlist nào → mở dialog tạo folder
                DialogUtils.showCreateWishlistDialog(context, post, () -> {
                    notifyItemChanged(holder.getAdapterPosition());
                });

            } else {
                // Nếu đã có trong wishlist → xoá khỏi tất cả folder (trừ Recently Viewed)
                WishlistManager.getInstance().removePostFromWishlists(post);
                notifyItemChanged(holder.getAdapterPosition()); // Cập nhật lại icon
            }
        });

        // Xử lý click vào item để xem chi tiết
        holder.itemView.setOnClickListener(v -> {
            WishlistManager.getInstance().addToRecentlyViewed(post);

            Intent intent = new Intent(context, HouseDetailActivity.class);
            intent.putExtra("post", post);
            context.startActivity(intent);
        });
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
