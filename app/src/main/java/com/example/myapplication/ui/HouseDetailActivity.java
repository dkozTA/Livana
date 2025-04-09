package com.example.myapplication.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class
HouseDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);
        getWindow().setBackgroundDrawableResource(android.R.color.white);

        Post post = getIntent().getParcelableExtra("post");
        if (post != null) {
            ImageView postImage = findViewById(R.id.post_image);
            TextView location = findViewById(R.id.location);
            TextView distance = findViewById(R.id.distance);
            TextView dateRange = findViewById(R.id.date_range);
            TextView price = findViewById(R.id.price);
            ImageButton heartButton = findViewById(R.id.heart_button);

            postImage.setImageResource(post.getImageResId());
            location.setText(post.getLocation());
            distance.setText(post.getDistance());
            dateRange.setText(post.getDateRange());
            price.setText(post.getPrice());

            // Set initial heart state
            boolean isInWishlist = WishlistManager.getInstance().isPostSaved(post);
            heartButton.setImageResource(isInWishlist ?
                    R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);

            // Handle heart button clicks
            heartButton.setOnClickListener(v -> {
                WishlistManager.getInstance().togglePost(post);
                boolean newState = WishlistManager.getInstance().isPostSaved(post);
                heartButton.setImageResource(newState ?
                        R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
            });
        }
    }
}