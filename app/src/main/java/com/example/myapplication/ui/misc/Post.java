package com.example.myapplication.ui.misc;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.myapplication.data.Model.Property.Amenities;

import java.util.List;

public class Post implements Parcelable {
    private String title, imageResId, location, distance, dateRange, price, detail;
    private int total_review;
    private double avg_ratings;
    private Amenities amenities;

    private List<String> sub_photos; // ảnh phụ (URLs)

    public Post(String title, String imageResId, String location, String detail,
                String distance, String dateRange, String price,
                int total_review, double avg_ratings, Amenities amenities, List<String> sub_photos) {
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
        this.sub_photos = sub_photos;
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
        amenities = in.readParcelable(Amenities.class.getClassLoader());
        sub_photos = in.createStringArrayList();
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
        dest.writeParcelable(amenities, flags);
        dest.writeStringList(sub_photos);
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

    // Getter
    public List<String> getSub_photos() {
        Log.d("Post", "getSub_photos size: " + (sub_photos != null ? sub_photos.size() : 0));
        return sub_photos;
    }
}
