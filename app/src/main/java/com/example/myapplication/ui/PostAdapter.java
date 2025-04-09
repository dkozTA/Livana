package com.example.myapplication.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

//    private void showCreateWishlistDialog(Context context, Post post) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_wishlist, null);
//        builder.setView(dialogView);
//
//        EditText inputName = dialogView.findViewById(R.id.editWishlistName);
//        Button btnCreate = dialogView.findViewById(R.id.btnCreate);
//        TextView btnClear = dialogView.findViewById(R.id.btnClear);
//        TextView counter = dialogView.findViewById(R.id.textCounter); // thêm dòng này
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//        // Đếm số ký tự đã nhập
//        inputName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                counter.setText(s.length() + "/50 characters");
//            }
//
//            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override public void afterTextChanged(Editable s) {}
//        });
//
//        btnClear.setOnClickListener(v -> inputName.setText(""));
//
//        btnCreate.setOnClickListener(v -> {
//            String name = inputName.getText().toString().trim();
//            if (!name.isEmpty()) {
//                WishlistManager.getInstance().createFolder(name);
//                WishlistManager.getInstance().getFolders()
//                        .get(WishlistManager.getInstance().getFolders().size() - 1)
//                        .addPost(post);
//                dialog.dismiss();
//            } else {
//                inputName.setError("Please enter a name");
//            }
//        });
//    }

    private void showCreateWishlistDialog(Context context, Post post, Runnable onComplete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_wishlist, null);
        builder.setView(dialogView);

        EditText inputName = dialogView.findViewById(R.id.editWishlistName);
        Button btnCreate = dialogView.findViewById(R.id.btnCreate);
        TextView btnClear = dialogView.findViewById(R.id.btnClear);
        TextView counter = dialogView.findViewById(R.id.textCounter);
        ImageView btnClose = dialogView.findViewById(R.id.btnClose);

        AlertDialog dialog = builder.create();
        dialog.show();


        btnClose.setOnClickListener(v -> dialog.dismiss());


        // Đếm ký tự nhập vào
        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                counter.setText(s.length() + "/50 characters");
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        btnClear.setOnClickListener(v -> inputName.setText(""));

        btnCreate.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            if (!name.isEmpty()) {
                WishlistManager.getInstance().createFolder(name);
                WishlistManager.getInstance().getFolders()
                        .get(WishlistManager.getInstance().getFolders().size() - 1)
                        .addPost(post);

                dialog.dismiss();

                if (onComplete != null) {
                    onComplete.run();  // Gọi callback sau khi tạo xong
                }
            } else {
                inputName.setError("Please enter a name");
            }
        });
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

//        boolean isInWishlist = WishlistManager.getInstance().isPostSaved(post);
//        holder.heartButton.setImageResource(isInWishlist ?
//                R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
//
//        holder.heartButton.setOnClickListener(v -> {
//            toggleWishlist(post, holder);
//        });

        // 1. Cập nhật icon trái tim theo trạng thái có trong wishlist hay không
        boolean isInWishlist = WishlistManager.getInstance().isPostInAnyWishlist(post);
        holder.heartButton.setImageResource(isInWishlist ?
                R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);

// 2. Xử lý khi người dùng nhấn vào trái tim
        holder.heartButton.setOnClickListener(v -> {
            boolean isCurrentlySaved = WishlistManager.getInstance().isPostInAnyWishlist(post);

            if (!isCurrentlySaved) {
                // Nếu post chưa có trong wishlist nào -> mở dialog tạo folder
                showCreateWishlistDialog(context, post, () -> {
                    // Sau khi thêm post vào wishlist → cập nhật lại icon trái tim
                    notifyItemChanged(holder.getAdapterPosition());
                });
            } else {
                // Nếu đã có trong wishlist → xoá khỏi tất cả folder (trừ Recently Viewed)
                WishlistManager.getInstance().removePostFromWishlists(post);
                notifyItemChanged(holder.getAdapterPosition()); // Cập nhật lại icon trái tim
            }
        });


        holder.itemView.setOnClickListener(v -> {
            // 1. Thêm bài đăng vào folder mặc định của Wishlist
            WishlistManager.getInstance().addToRecentlyViewed(post);

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