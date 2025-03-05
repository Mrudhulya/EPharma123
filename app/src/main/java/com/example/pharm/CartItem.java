package com.example.pharm;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private String medicineName;
    private double pricePerUnit;
    private int quantity;
    private int medicineId;
    private String pharmacyName; // Add pharmacy name

    public CartItem(String medicineName, double pricePerUnit, int quantity, int medicineId, String pharmacyName) {
        this.medicineName = medicineName;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.medicineId = medicineId;
        this.pharmacyName = pharmacyName; // Initialize pharmacy name
    }

    protected CartItem(Parcel in) {
        medicineName = in.readString();
        pricePerUnit = in.readDouble();
        quantity = in.readInt();
        medicineId = in.readInt();
        pharmacyName = in.readString(); // Read pharmacy name from parcel
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    public String getMedicineName() {
        return medicineName;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public String getPharmacyName() {
        return pharmacyName; // Getter for pharmacy name
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return pricePerUnit * quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(medicineName);
        dest.writeDouble(pricePerUnit);
        dest.writeInt(quantity);
        dest.writeInt(medicineId);
        dest.writeString(pharmacyName); // Write pharmacy name to parcel
    }
}