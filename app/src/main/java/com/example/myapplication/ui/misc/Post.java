package com.example.myapplication.ui.misc;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.myapplication.data.Enum.PropertyStatus;
import com.example.myapplication.data.Model.Property.Amenities;

import java.util.Date;
import java.util.List;

public class Post implements Parcelable {
    private String id, title, imageResId, location, distance, dateRange, detail;
    private int total_review;
    private double avg_ratings;
    private Amenities amenities;
    public String normal_price;


    private List<String> sub_photos; // ảnh phụ (URLs)

    public Post(String id, String title, String imageResId, String location, String detail,
                String distance, String dateRange, String normal_price,
                int total_review, double avg_ratings, Amenities amenities, List<String> sub_photos) {
        this.title = title;
        this.imageResId = imageResId;
        this.location = location;
        this.detail = detail;
        this.distance = distance;
        this.dateRange = dateRange;
        this.normal_price = normal_price;
        this.total_review = total_review;
        this.avg_ratings = avg_ratings;
        this.amenities = amenities;
        this.sub_photos = sub_photos;
        this.id = id;
    }

    protected Post(Parcel in) {
        title = in.readString();
        imageResId = in.readString();
        location = in.readString();
        detail = in.readString();
        distance = in.readString();
        dateRange = in.readString();
        normal_price = in.readString();
        total_review = in.readInt();
        avg_ratings = in.readDouble();
        amenities = in.readParcelable(Amenities.class.getClassLoader());
        sub_photos = in.createStringArrayList();
        id = in.readString();
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
        dest.writeString(normal_price);

        dest.writeInt(total_review);
        dest.writeDouble(avg_ratings);
        dest.writeParcelable(amenities, flags);
        dest.writeStringList(sub_photos);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return id;
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

    public String getNormal_price() {
        return normal_price;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id.equals(post.id); // Giả sử Post có trường id duy nhất
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
