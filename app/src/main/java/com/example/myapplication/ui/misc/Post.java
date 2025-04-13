package com.example.myapplication.ui.misc;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.data.Model.Property.Amenities;

public class Post implements Parcelable {
    private String title, imageResId, location, distance, dateRange, price, detail;
    private int total_review;
    private double avg_ratings;
    private Amenities amenities;

    public Post(String title, String imageResId, String location, String detail,
                String distance, String dateRange, String price,
                int total_review, double avg_ratings, Amenities amenities) {
        this.title = title;
        this.imageResId = imageResId;
        this.location = location;
        this.detail = detail;
        this.distance = distance;
        this.dateRange = dateRange;
        this.price = price;
        this.total_review = total_review;
        this.avg_ratings = avg_ratings;
        this.amenities = amenities;
    }

    protected Post(Parcel in) {
        title = in.readString();
        imageResId = in.readString();
        location = in.readString();
        detail = in.readString();
        distance = in.readString();
        dateRange = in.readString();
        price = in.readString();
        total_review = in.readInt();
        avg_ratings = in.readDouble();
        amenities = (Amenities) in.readSerializable(); // đảm bảo Amenities implements Serializable
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageResId);
        dest.writeString(location);
        dest.writeString(detail);
        dest.writeString(distance);
        dest.writeString(dateRange);
        dest.writeString(price);
        dest.writeInt(total_review);
        dest.writeDouble(avg_ratings);
        //dest.writeSerializable(amenities);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getImageResId() {
        return imageResId;
    }

    public String getLocation() {
        return location;
    }

    public String getDistance() {
        return distance;
    }

    public String getDateRange() {
        return dateRange;
    }

    public String getPrice() {
        return price;
    }

    public String getDetail() {
        return detail;
    }

    public int getTotalReview() {
        return total_review;
    }

    public double getAvgRatings() {
        return avg_ratings;
    }

    public Amenities getAmenities() {
        return amenities;
    }
}
