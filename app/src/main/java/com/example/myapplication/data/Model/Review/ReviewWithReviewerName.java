package com.example.myapplication.data.Model.Review;

public class ReviewWithReviewerName {
    public String id;
    public String booking_id;
    public int point;
    public String property_id;
    public String content;
    public String reviewer_name;

    public ReviewWithReviewerName() {

    }

    public ReviewWithReviewerName(Review review_content, String host_name) {
        this.id = review_content.id;
        this.booking_id = review_content.booking_id;
        this.point = review_content.point;
        this.property_id = review_content.property_id;
        this.content = review_content.content;
        this.reviewer_name = host_name;
    }

}
