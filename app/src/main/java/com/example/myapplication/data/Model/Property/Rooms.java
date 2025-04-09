package com.example.myapplication.data.Model.Property;

public class Rooms {
    public int bedRooms; // co bao nhieu phong ngu
    public AmenityStatus livingRooms;
    public AmenityStatus kitchen;

    // Constructors
    public Rooms() {}

    public Rooms(int bedRooms, AmenityStatus livingRooms, AmenityStatus kitchen) {
        this.bedRooms = bedRooms;
        this.livingRooms = livingRooms;
        this.kitchen = kitchen;
    }

}
