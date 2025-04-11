package com.example.myapplication.data.Model.Property;

public class Amenities {
    public AmenityStatus tv; // số lượng tv có
    public AmenityStatus wifi; // có wifi hay không
    public AmenityStatus petAllowance; // có cho phep thu cung hay khong
    public AmenityStatus pool; // co hay khong
    public AmenityStatus washingMachine; // co may giat hay khong
    public AmenityStatus breakfast; // co bua sang hay khong
    public AmenityStatus airConditioner; // co dieu hoa hay khong
    public AmenityStatus bbq; //co bbq hay khong
    public String more;
    public String houseRules;
    // Constructors
    public Amenities() {}

    public Amenities(AmenityStatus tv, AmenityStatus wifi, AmenityStatus petAllowance, AmenityStatus pool, AmenityStatus washingMachine,
                     AmenityStatus breakfast, AmenityStatus airConditioner, AmenityStatus bbq, String more, String houseRules) {
        this.tv = tv;
        this.wifi = wifi;
        this.petAllowance = petAllowance;
        this.pool = pool;
        this.washingMachine = washingMachine;
        this.breakfast = breakfast;
        this.airConditioner = airConditioner;
        this.bbq = bbq;
        this.more = more;
        this.houseRules = houseRules;
    }
}
