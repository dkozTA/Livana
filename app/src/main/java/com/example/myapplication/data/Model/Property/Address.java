package com.example.myapplication.data.Model.Property;

public class Address {
    public int city_code;
    public int district_code;
    public int ward_code;
    public String detailed_address;

    public Address() {}

    public Address(int city, int district, int ward, String detailed_add) {
        this.city_code = city;
        this.district_code = district;
        this.ward_code = ward;
        this.detailed_address = detailed_add;
    }
    
    public String getDetailAddress() {
        return this.detailed_address;
    }

    // luư địa chỉ city district và ward theo dạng code nhé để dễ search
    // https://provinces.open-api.vn/
}
