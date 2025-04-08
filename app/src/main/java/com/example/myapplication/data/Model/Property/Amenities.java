package com.example.myapplication.data.Model.Property;

public class Amenities {
    public int tv; // số lượng tv có
    public boolean wifi; // có wifi hay không
    public boolean petAllowance; // có cho phep thu cung hay khong
    public boolean pool; // co hay khong
    public boolean washingMachine; // co may giat hay khong
    public boolean breakfast; // co bua sang hay khong
    public boolean airConditioner; // co dieu hoa hay khong
    public boolean bbq; //co bbq hay khong
    public String more;

    // Constructors
    public Amenities() {}

    public Amenities(int tv, boolean wifi, boolean petAllowance, boolean pool, boolean washingMachine,
                     boolean breakfast, boolean airConditioner, boolean bbq, String more) {
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
