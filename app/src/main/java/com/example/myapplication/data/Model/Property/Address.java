package com.example.myapplication.data.Model.Property;

public class Address {
    public int city_code;
    public int ward_code;
    public String detailed_address;

    public Address(int city, int ward, String detailed_add) {
        this.city_code = city;
        this.ward_code = ward;
        this.detailed_address = detailed_add;
    }
}
