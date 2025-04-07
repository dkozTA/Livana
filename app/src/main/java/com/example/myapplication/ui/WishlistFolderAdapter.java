package com.example.myapplication.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;

public class WishlistFolderAdapter extends RecyclerView.Adapter<WishlistFolderAdapter.FolderViewHolder> {
    private List<WishlistFolder> folders;
    private OnFolderClickListener listener;

    public interface OnFolderClickListener {
        void onFolderClick(WishlistFolder folder);
    }

    public WishlistFolderAdapter(List<WishlistFolder> folders, OnFolderClickListener listener) {
        this.folders = folders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wishlist_folder, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        WishlistFolder folder = folders.get(position);
        holder.folderName.setText(folder.getName());

        // Show first 4 house images in a grid
        List<Post> posts = folder.getPosts();
        if (!posts.isEmpty()) {
            for (int i = 0; i < Math.min(posts.size(), 4); i++) {
                holder.previewImages[i].setImageResource(posts.get(i).getImageResId());
                holder.previewImages[i].setVisibility(View.VISIBLE);
            }
            // Hide remaining preview images if less than 4 posts
            for (int i = posts.size(); i < 4; i++) {
                holder.previewImages[i].setVisibility(View.GONE);
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onFolderClick(folder));
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;
        ImageView[] previewImages;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folder_name);
            previewImages = new ImageView[]{
                    itemView.findViewById(R.id.preview_image1),
                    itemView.findViewById(R.id.preview_image2),
                    itemView.findViewById(R.id.preview_image3),
                    itemView.findViewById(R.id.preview_image4)
            };
        }
    }
}