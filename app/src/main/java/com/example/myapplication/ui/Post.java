package com.example.myapplication.ui;

public class Post {
    private int imageResId;
    private String location, distance, dateRange, price;

    public Post(int imageResId, String location, String distance, String dateRange, String price) {
        this.imageResId = imageResId;
        this.location = location;
        this.distance = distance;
        this.dateRange = dateRange;
        this.price = price;
    }

    public int getImageResId() { return imageResId; }
    public String getLocation() { return location; }
    public String getDistance() { return distance; }
    public String getDateRange() { return dateRange; }
    public String getPrice() { return price; }
}
