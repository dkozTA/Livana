package com.example.myapplication.data.Model.Property;

import com.example.myapplication.data.Enum.PropertyStatus;
import com.example.myapplication.data.Enum.PropertyType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Property {
    public String id;
    public String host_id;
    public String name;
    public PropertyType property_type;
    public PropertyStatus status;
    public Address address;
    public Rooms rooms;
    public int max_guess;
    public Amenities amenities;
    public String main_photo;
    public List<String> sub_photos;
    public double normal_price;
    public double weekend_price;
    public double holiday_price;
    public double deposit;
    public List<String> booked_date;
    public int total_reviews;
    public double avg_ratings;
    public Date created_at;
    public Date updated_at;

    public Property() {};

    public Property(String host_id, String name, PropertyType property_type, PropertyStatus status,
                    Address address, Rooms rooms, int max_guess, Amenities amenities,
                    double normal_price, double weekend_price, double holiday_price, double deposit) {
        this.id = UUID.randomUUID().toString();
        this.host_id = host_id;
        this.name = name;
        this.property_type = property_type;
        this.status = status;
        this.address = address;
        this.rooms = rooms;
        this.max_guess = max_guess;
        this.amenities = amenities;
        this.main_photo = "";
        this.sub_photos =  new ArrayList<>();
        this.normal_price = normal_price;
        this.weekend_price = weekend_price;
        this.holiday_price = holiday_price;
        this.deposit = deposit;
        this.booked_date = new ArrayList<>();
        this.total_reviews = 0;
        this.avg_ratings = 0;
        this.created_at = new Date();
        this.updated_at = new Date();
    }

    // --- GETTERS ---
    public String getId() {
        return id;
    }

    public String getHost_id() {
        return host_id;
    }

    public String getName() {
        return name;
    }

    public PropertyType getProperty_type() {
        return property_type;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public Address getAddress() {
        return address;
    }

    public Rooms getRooms() {
        return rooms;
    }

    public int getMax_guess() {
        return max_guess;
    }

    public Amenities getAmenities() {
        return amenities;
    }

    public String getMainPhoto() {
        return main_photo;
    }

    public List<String> getSub_photos() {
        return sub_photos;
    }

    public double getNormal_price() {
        return normal_price;
    }

    public double getWeekend_price() {
        return weekend_price;
    }

    public double getHoliday_price() {
        return holiday_price;
    }

    public double getDeposit() {
        return deposit;
    }

    public List<String> getBooked_date() {
        return booked_date;
    }

    public int getTotal_reviews() {
        return total_reviews;
    }

    public double getAvg_ratings() {
        return avg_ratings;
    }

    public void setMainPhoto(String main_photo) {
        this.main_photo = main_photo;
    }

    public void setSub_photos(List<String> sub_photos) {
        this.sub_photos = sub_photos;
    }
}
