package com.example.pharm;


public class PharmacyItem {
    private String id;
    private String name;
    private String contact;
    private String email;
    private String address;
    private double latitude;
    private double longitude;


    public PharmacyItem(String id, String name, String contact, String email, String address, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;

    }


    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

}