package com.example.myapplication.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private int imageResId;
    private String title, location, distance, dateRange, price, detail;

    public Post(String title, int imageResId, String location, String detail, String distance, String dateRange, String price) {
        this.imageResId = imageResId;
        this.location = location;
        this.distance = distance;
        this.dateRange = dateRange;
        this.price = price;
        this.title = title;
        this.detail = detail;
    }

    protected Post(Parcel in) {
        title = in.readString();
        imageResId = in.readInt();
        location = in.readString();
        detail = in.readString();
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
        dest.writeString(title);
        dest.writeInt(imageResId);
        dest.writeString(location);
        dest.writeString(detail);
        dest.writeString(distance);
        dest.writeString(dateRange);
        dest.writeString(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() { return imageResId; }
    public String getLocation() { return location; }
    public String getDistance() { return distance; }
    public String getDateRange() { return dateRange; }
    public String getPrice() { return price; }

    public String getDetail() {
        return detail;
    }
}