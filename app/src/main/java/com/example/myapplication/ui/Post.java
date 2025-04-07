package com.example.myapplication.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private int imageResId;
    private String location, distance, dateRange, price;

    public Post(int imageResId, String location, String distance, String dateRange, String price) {
        this.imageResId = imageResId;
        this.location = location;
        this.distance = distance;
        this.dateRange = dateRange;
        this.price = price;
    }

    protected Post(Parcel in) {
        imageResId = in.readInt();
        location = in.readString();
        distance = in.readString();
        dateRange = in.readString();
        price = in.readString();
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
        dest.writeInt(imageResId);
        dest.writeString(location);
        dest.writeString(distance);
        dest.writeString(dateRange);
        dest.writeString(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getImageResId() { return imageResId; }
    public String getLocation() { return location; }
    public String getDistance() { return distance; }
    public String getDateRange() { return dateRange; }
    public String getPrice() { return price; }
}