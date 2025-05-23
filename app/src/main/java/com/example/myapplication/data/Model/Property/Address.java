package com.example.myapplication.data.Model.Property;


public class Address {
    public int city_code;
    public int district_code;
    public int ward_code;
    public String city_name;
    public String district_name;
    public String ward_name;
    public String detailed_address;

    public Address() {}

    public Address(int city, int district, int ward, String detailed_add) {
        this.city_code = city;
        this.district_code = district;
        this.ward_code = ward;
        this.detailed_address = detailed_add;
        this.district_name = "";
        this.city_name = "";
        this.ward_name = "";
    }

    public Address(int city_code, int district_code, int ward_code, String city_name, String district_name, String ward_name, String detailed_add) {
        this.city_code = city_code;
        this.district_code = district_code;
        this.ward_code = ward_code;
        this.city_name = city_name;
        this.district_name = district_name;
        this.ward_name = ward_name;
        this.detailed_address = detailed_add;
    }
    
    public String getDetailAddress() {
        return this.detailed_address;
    }

    // luư địa chỉ city district và ward theo dạng code nhé để dễ search
    // https://provinces.open-api.vn/
}
