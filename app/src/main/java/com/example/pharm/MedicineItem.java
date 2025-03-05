package com.example.pharm;

public class MedicineItem {
    private int id;
    private String name;
    private double price;
    private String expiryDate;

    public MedicineItem(int id, String name, double price, String expiryDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getExpiryDate() { return expiryDate; }
}
