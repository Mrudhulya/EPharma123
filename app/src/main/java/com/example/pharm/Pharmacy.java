package com.example.pharm;

public class Pharmacy {
    private String name;
    private String phone;
    private String address;
    private double latitude;
    private double longitude;
    private double distance;

    public Pharmacy(String name, String phone, String address, double latitude, double longitude) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }



    // Getters
    public String getName() {
        return name;
    }
    public String getContact() {
        return phone;
    }
    public String getAddress() {
        return address;
    }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public void setDistance(double distance) { this.distance = distance; }
    public double getDistance() { return distance; }
}
