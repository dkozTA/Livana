package com.example.myapplication.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Model.Review.Review;
import com.example.myapplication.data.Model.Review.ReviewWithReviewerName;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<ReviewWithReviewerName> reviewList;

    public ReviewAdapter(List<ReviewWithReviewerName> reviewList) {
        this.reviewList = reviewList;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewerName, reviewDate, reviewContent, reviewerActivity;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewerName = itemView.findViewById(R.id.txt_username);
            //reviewerActivity = itemView.findViewById(R.id.txt_active_years);
            reviewDate = itemView.findViewById(R.id.txt_review_date);
            reviewContent = itemView.findViewById(R.id.txt_review_content);
            ratingBar = itemView.findViewById(R.id.review_rating_bar);
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_2, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewWithReviewerName review = reviewList.get(position);
        Log.d("ReviewAdapter", "Binding review: " + review.booking_id + ", " + review.content);
        holder.reviewerName.setText(review.reviewer_name);
        holder.reviewDate.setText(review.property_id);
        holder.reviewContent.setText(review.content);
        holder.ratingBar.setRating(review.point);
        //holder.reviewerActivity.setText(review.property_id);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}

