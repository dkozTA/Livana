package com.example.myapplication.data.Model.Property;

import com.example.myapplication.data.Enum.PropertyStatus;
import com.example.myapplication.data.Enum.PropertyType;

import java.util.Date;

public class Property {
    public String id;
    public String host_id;
    public String name;
    public PropertyType property_type;
    public PropertyStatus status;
    public Address address;
    public Rooms rooms;
    public Amenities amenities;
    public String main_photo;
    public String[] sub_photos;
    public double normal_price;
    public double weekend_price;
    public double holiday_price;
    public double deposit;
    public String[] booked_date;
    public int total_reviews;
    public double avg_ratings;
    public Date created_at;
    public Date updated_at;

    public Property() {};
    public Property(String id, String host_id, String name, PropertyType property_type, PropertyStatus status,
                    Address address, Rooms rooms, Amenities amenities, String main_photo, String[] sub_photos,
                    double normal_price, double weekend_price, double holiday_price, double deposit,
                    String[] booked_date, int total_reviews, double avg_ratings,
                    Date created_at, Date updated_at) {
        this.id = id;
        this.host_id = host_id;
        this.name = name;
        this.property_type = property_type;
        this.status = status;
        this.address = address;
        this.rooms = rooms;
        this.amenities = amenities;
        this.main_photo = main_photo;
        this.sub_photos = sub_photos;
        this.normal_price = normal_price;
        this.weekend_price = weekend_price;
        this.holiday_price = holiday_price;
        this.deposit = deposit;
        this.booked_date = booked_date;
        this.total_reviews = total_reviews;
        this.avg_ratings = avg_ratings;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
