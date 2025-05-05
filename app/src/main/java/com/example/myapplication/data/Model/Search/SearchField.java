package com.example.myapplication.data.Model.Search;

import com.google.gson.annotations.SerializedName;

public class SearchField {
    @SerializedName("propertyName")
    private String propertyName;

    @SerializedName("city_code")
    private int city_code;

    @SerializedName("district_code")
    private int district_code;

    @SerializedName("ward_code")
    private int ward_code;

    @SerializedName("max_guest")
    private int max_guest;

    @SerializedName("bed_rooms")
    private int bed_rooms;

    @SerializedName("min_price")
    private double min_price;

    @SerializedName("max_price")
    private double max_price;

    @SerializedName("check_in_date")
    private String check_in_date;

    @SerializedName("check_out_date")
    private String check_out_date;

    @SerializedName("tv")
    private boolean tv;

    @SerializedName("petAllowance")
    private boolean petAllowance;

    @SerializedName("pool")
    private boolean pool;

    @SerializedName("washingMachine")
    private boolean washingMachine;

    @SerializedName("breakfast")
    private boolean breakfast;

    @SerializedName("bbq")
    private boolean bbq;

    @SerializedName("wifi")
    private boolean wifi;

    @SerializedName("airConditioner")
    private boolean airConditioner;

    @SerializedName("page")
    private int page;

    @SerializedName("hitsPerPage")
    private int hitsPerPage;

    // Constructor mặc định
    public SearchField() {
        // Khởi tạo các giá trị mặc định
        this.page = 0;
        this.hitsPerPage = 20;
    }

    // Getters and Setters
    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public int getCity_code() {
        return city_code;
    }

    public void setCity_code(int city_code) {
        this.city_code = city_code;
    }

    public int getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(int district_code) {
        this.district_code = district_code;
    }

    public int getWard_code() {
        return ward_code;
    }

    public void setWard_code(int ward_code) {
        this.ward_code = ward_code;
    }

    public int getMax_guest() {
        return max_guest;
    }

    public void setMax_guest(int max_guest) {
        this.max_guest = max_guest;
    }

    public int getBed_rooms() {
        return bed_rooms;
    }

    public void setBed_rooms(int bed_rooms) {
        this.bed_rooms = bed_rooms;
    }

    public double getMin_price() {
        return min_price;
    }

    public void setMin_price(double min_price) {
        this.min_price = min_price;
    }

    public double getMax_price() {
        return max_price;
    }

    public void setMax_price(double max_price) {
        this.max_price = max_price;
    }

    public String getCheck_in_date() {
        return check_in_date;
    }

    public void setCheck_in_date(String check_in_date) {
        this.check_in_date = check_in_date;
    }

    public String getCheck_out_date() {
        return check_out_date;
    }

    public void setCheck_out_date(String check_out_date) {
        this.check_out_date = check_out_date;
    }

    public boolean isTv() {
        return tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }

    public boolean isPetAllowance() {
        return petAllowance;
    }

    public void setPetAllowance(boolean petAllowance) {
        this.petAllowance = petAllowance;
    }

    public boolean isPool() {
        return pool;
    }

    public void setPool(boolean pool) {
        this.pool = pool;
    }

    public boolean isWashingMachine() {
        return washingMachine;
    }

    public void setWashingMachine(boolean washingMachine) {
        this.washingMachine = washingMachine;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public boolean isBbq() {
        return bbq;
    }

    public void setBbq(boolean bbq) {
        this.bbq = bbq;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isAirConditioner() {
        return airConditioner;
    }

    public void setAirConditioner(boolean airConditioner) {
        this.airConditioner = airConditioner;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getHitsPerPage() {
        return hitsPerPage;
    }

    public void setHitsPerPage(int hitsPerPage) {
        this.hitsPerPage = hitsPerPage;
    }

    // Builder pattern để dễ dàng tạo đối tượng tìm kiếm
    public static class Builder {
        private SearchField searchField;

        public Builder() {
            searchField = new SearchField();
        }

        public Builder propertyName(String propertyName) {
            searchField.setPropertyName(propertyName);
            return this;
        }

        public Builder cityCode(int cityCode) {
            searchField.setCity_code(cityCode);
            return this;
        }

        public Builder districtCode(int districtCode) {
            searchField.setDistrict_code(districtCode);
            return this;
        }

        public Builder wardCode(int wardCode) {
            searchField.setWard_code(wardCode);
            return this;
        }

        public Builder maxGuest(int maxGuest) {
            searchField.setMax_guest(maxGuest);
            return this;
        }

        public Builder bedRooms(int bedRooms) {
            searchField.setBed_rooms(bedRooms);
            return this;
        }

        public Builder priceRange(double minPrice, double maxPrice) {
            searchField.setMin_price(minPrice);
            searchField.setMax_price(maxPrice);
            return this;
        }

        public Builder dateRange(String checkInDate, String checkOutDate) {
            searchField.setCheck_in_date(checkInDate);
            searchField.setCheck_out_date(checkOutDate);
            return this;
        }

        public Builder tv(boolean hasTv) {
            searchField.setTv(hasTv);
            return this;
        }

        public Builder petAllowance(boolean allowsPets) {
            searchField.setPetAllowance(allowsPets);
            return this;
        }

        public Builder pool(boolean hasPool) {
            searchField.setPool(hasPool);
            return this;
        }

        public Builder washingMachine(boolean hasWashingMachine) {
            searchField.setWashingMachine(hasWashingMachine);
            return this;
        }

        public Builder breakfast(boolean hasBreakfast) {
            searchField.setBreakfast(hasBreakfast);
            return this;
        }

        public Builder bbq(boolean hasBbq) {
            searchField.setBbq(hasBbq);
            return this;
        }

        public Builder wifi(boolean hasWifi) {
            searchField.setWifi(hasWifi);
            return this;
        }

        public Builder airConditioner(boolean hasAirConditioner) {
            searchField.setAirConditioner(hasAirConditioner);
            return this;
        }

        public Builder pagination(int page, int hitsPerPage) {
            searchField.setPage(page);
            searchField.setHitsPerPage(hitsPerPage);
            return this;
        }

        public SearchField build() {
            return searchField;
        }
    }
}