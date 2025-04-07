package com.example.myapplication.data.Model.Property;

public class Amenities {
    public int tv;
    public int wifi;
    public int petAllowance;
    public int pool;
    public int washingMachine;
    public int breakfast;
    public int airConditioner;
    public int bbq;
    public String more;

    // Constructors
    public Amenities() {}

    public Amenities(int tv, int wifi, int petAllowance, int pool, int washingMachine,
                     int breakfast, int airConditioner, int bbq, String more) {
        this.tv = tv;
        this.wifi = wifi;
        this.petAllowance = petAllowance;
        this.pool = pool;
        this.washingMachine = washingMachine;
        this.breakfast = breakfast;
        this.airConditioner = airConditioner;
        this.bbq = bbq;
        this.more = more;
    }
}
