package com.example.pharm;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodingHelper {
    private Geocoder geocoder;

    public GeocodingHelper(Context context) {
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    public double[] getLatLngFromAddress(String address) {
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                Log.d("GEO_DEBUG", "Address: " + address + " -> Lat: " + latitude + ", Lng: " + longitude);
                return new double[]{latitude, longitude};
            } else {
                Log.e("GEO_DEBUG", "No coordinates found for address: " + address);
            }
        } catch (IOException e) {
            Log.e("GEO_DEBUG", "Geocoding failed: " + e.getMessage());
        }
        return null;
    }

}
