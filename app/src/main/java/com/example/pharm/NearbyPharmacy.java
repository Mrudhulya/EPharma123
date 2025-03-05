package com.example.pharm;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NearbyPharmacy extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private TextView tvCurrentLocation;
    private Button btnRefreshLocation;
    private LinearLayout nearbyPharmaciesContainer;
    private DatabaseHelper databaseHelper;
    private double userLatitude = 0.0;
    private double userLongitude = 0.0;

    //Medicine
    private TextView medicineName, medicinePrice, medicineExpiry, tvQuantity;
    private LinearLayout quantityInput;
    private Button btnPlus, btnMinus, addToCart, viewCart;
    private Spinner spinnerPharmacyName;
    private String selectedPharmacyName;
    private ArrayAdapter<String> pharmacyAdapter;
    private List<String> pharmacyNames = new ArrayList<>();

    private int quantity = 1; // Default quantity
    private double pricePerUnit = 0.0; // Variable to store the price per unit



    // Add this list to store cart items
    private List<CartItem> cartItems = new ArrayList<>();

    private int medicineId; // Declare medicineId as a class-level variable


    //cart manager
    // Inside NearbyPharmacy.java, replace the cart logic with CartManager
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nearby_pharmacy);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.navigation_bar_color));

        //Initialize medicine id
        // Initialize medicineId from the intent
        medicineId = getIntent().getIntExtra("MEDICINE_ID", -1);


        //Cart
        // Initialize CartManager
        cartManager = new CartManager(this);


        addToCart = findViewById(R.id.add_to_cart);
        // Add to cart button click listener
        // Modify the addToCart button click listener
        // Modify the addToCart button click listener in NearbyPharmacy.java
        addToCart.setOnClickListener(v -> {
            String selectedMedicineName = medicineName.getText().toString();
            double pricePerUnit = this.pricePerUnit;
            int quantity = this.quantity;
            String selectedPharmacyName = spinnerPharmacyName.getSelectedItem().toString(); // Get selected pharmacy name

            CartItem item = new CartItem(selectedMedicineName, pricePerUnit, quantity, medicineId, selectedPharmacyName);
            cartManager.addToCart(item);

            Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show();
        });


        // Medicine Initialization
        // Initialize Views
        btnPlus = findViewById(R.id.btn_plus);
        btnMinus = findViewById(R.id.btn_minus);
        tvQuantity = findViewById(R.id.tv_quantity);
        medicineName = findViewById(R.id.medicineName);
        medicinePrice = findViewById(R.id.medicinePrice);
        medicineExpiry = findViewById(R.id.medicineExpiry);
        addToCart = findViewById(R.id.add_to_cart);

        //Initialize spinner
        spinnerPharmacyName = findViewById(R.id.spinnerPharmacy);
        pharmacyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pharmacyNames);
        pharmacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPharmacyName.setAdapter(pharmacyAdapter);


        // Inside onCreate() method
        spinnerPharmacyName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPharmacyName = pharmacyNames.get(position); // Store the selected pharmacy name
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        // Initialize views
        tvCurrentLocation = findViewById(R.id.tv_current_location);

        nearbyPharmaciesContainer = findViewById(R.id.nearbyPharmaciesContainer);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getReadableDatabase();

        // Get medicine ID from intent
        int medicineId = getIntent().getIntExtra("MEDICINE_ID", -1);

        if (medicineId != -1) {
            loadMedicineDetails(medicineId);
        }

        // Set click listener for the Button
        btnPlus.setOnClickListener(v -> {
            // Handle button click
            quantity++;
            updateQuantityAndPrice();
        });

        // Minus button click listener
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityAndPrice();
            }
        });





        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        // Create location request for real-time updates
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); // Update location every 10 seconds
        locationRequest.setFastestInterval(5000); // Fastest update interval
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Check permissions and request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            startLocationUpdates();
        }


        // Debugging: Log the medicineId
        Log.d("MedicineIdDebug", "Medicine ID: " + medicineId);
        if (medicineId == -1) {
            Toast.makeText(this, "Medicine ID not found", Toast.LENGTH_SHORT).show();
        }

        int pharmacyId = getSelectedPharmacyId();
        Log.d("PharmacyIdDebug", "Pharmacy ID: " + pharmacyId);
        if (pharmacyId == -1) {
            Toast.makeText(this, "Pharmacy not selected", Toast.LENGTH_SHORT).show();
        }


    }


    // Method to get the current logged-in user's ID
    private int getCurrentUserId() {
        // Retrieve the logged-in user's ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1); // Return -1 if no user is logged in
    }




    // Method to get the selected pharmacy's ID
    private int getSelectedPharmacyId() {
        // Retrieve the selected pharmacy's ID from the spinner
        Spinner spinnerPharmacy = findViewById(R.id.spinnerPharmacy);
        return (int) spinnerPharmacy.getSelectedItemId(); // Assuming the spinner uses pharmacy IDs
    }




    //Medicine fetch
    private void loadMedicineDetails(int medicineId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM medicines WHERE medicine_id=?", new String[]{String.valueOf(medicineId)});

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("medicine_name"));
            pricePerUnit = cursor.getDouble(cursor.getColumnIndexOrThrow("medicine_price"));
            String expiry = cursor.getString(cursor.getColumnIndexOrThrow("medicine_expiry_date"));

            medicineName.setText(name);
            medicinePrice.setText("Price: ₹" + pricePerUnit);
            medicineExpiry.setText("Expiry Date: " + expiry);
            updateQuantityAndPrice();
        }
        cursor.close();
    }

    private void updateQuantityAndPrice() {
        tvQuantity.setText(String.valueOf(quantity));
        double totalPrice = pricePerUnit * quantity;
        medicinePrice.setText("Price: ₹" + totalPrice);
    }





    private void startLocationUpdates() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    Location location = locationResult.getLastLocation();
                    userLatitude = location.getLatitude();
                    userLongitude = location.getLongitude();

                    // Use Geocoder to get the full address from latitude and longitude
                    String locationName = getLocationDetails(userLatitude, userLongitude);
                    tvCurrentLocation.setText("Location:  " + locationName);

                    // Fetch and display nearby blood banks ONLY AFTER location is retrieved
                    fetchNearbyPharmacies();
                }
            }
        };

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void refreshLocation() {
        // Request a one-time location update to refresh the location when the button is pressed
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            userLatitude = location.getLatitude();
                            userLongitude = location.getLongitude();

                            // Use Geocoder to get the full address from latitude and longitude
                            String locationName = getLocationDetails(userLatitude, userLongitude);
                            tvCurrentLocation.setText("Your Location: " + locationName);

                            // Fetch and display nearby blood banks ONLY AFTER location is retrieved
                            fetchNearbyPharmacies();
                        } else {
                            tvCurrentLocation.setText("Unable to get location.");
                        }
                    }
                });
    }

    private String getLocationDetails(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this);
        String locationDetails = "Unknown Location";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();

                // Get locality, admin area, country, and postal code
                String city = address.getLocality();
                String state = address.getAdminArea();
                String country = address.getCountryName();
                String postalCode = address.getPostalCode();

                if (city != null) sb.append(city).append(", ");
                if (state != null) sb.append(state).append(", ");
                if (country != null) sb.append(country).append(", ");
                if (postalCode != null) sb.append(postalCode);

                locationDetails = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationDetails;
    }

    private void fetchNearbyPharmacies() {


        // Check if the user's location is valid
        if (userLatitude == 0.0 || userLongitude == 0.0) {
            Toast.makeText(this, "Waiting for valid location...", Toast.LENGTH_SHORT).show();
            return;
        }


        List<PharmacyItem> pharmacies = databaseHelper.getAllPharmaciesList();
        List<PharmacyItem> nearbyPharmacies = new ArrayList<>();

        for (PharmacyItem pharmacy : pharmacies) {
            double distance = calculateDistance(userLatitude, userLongitude, pharmacy.getLatitude(), pharmacy.getLongitude());
            if (distance <= 10.0) { // Show blood banks within 10 km
                nearbyPharmacies.add(pharmacy);
            }
        }

        // Clear the existing list and add new pharmacy names
        pharmacyNames.clear();
        for (PharmacyItem pharmacy : nearbyPharmacies) {
            pharmacyNames.add(pharmacy.getName());
        }

        // Notify the adapter that the data has changed
        pharmacyAdapter.notifyDataSetChanged();



        // Display nearby pharmacy
        displayNearbyPharmacies(nearbyPharmacies);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in kilometers
    }

    private void displayNearbyPharmacies(List<PharmacyItem> pharmacies) {
        nearbyPharmaciesContainer.removeAllViews();

        if (pharmacies.isEmpty()) {
            TextView textView = new TextView(this);
            textView.setText("No nearby pharmacies found.");
            textView.setTextSize(16);
            nearbyPharmaciesContainer.addView(textView);
        } else {

            for (PharmacyItem pharmacyItem : pharmacies) {
                // Create a container layout for each pharmacy
                LinearLayout pharmacyLayout = new LinearLayout(this);
                pharmacyLayout.setOrientation(LinearLayout.VERTICAL);
                pharmacyLayout.setPadding(20, 20, 20, 20);

                // Pharmacy Name TextView (Black, Bold, Clickable → Navigates to Another Activity)
                TextView nameTextView = new TextView(this);
                nameTextView.setText(pharmacyItem.getName());
                nameTextView.setTextSize(18);
                nameTextView.setTypeface(null, Typeface.BOLD);
                nameTextView.setTextColor(Color.BLACK);
                nameTextView.setPadding(0, 0, 0, 10);

                // Click listener to navigate to another activity
                nameTextView.setOnClickListener(v -> {
                    Intent intent = new Intent(this, MedicineDetails.class);
                    intent.putExtra("pharmacy_name", pharmacyItem.getName());
                    intent.putExtra("pharmacy_address", pharmacyItem.getAddress());
                    intent.putExtra("pharmacy_contact", pharmacyItem.getContact());

                    startActivity(intent);
                });

                // Address TextView (Dark Blue, Clickable → Opens Google Maps)
                TextView addressTextView = new TextView(this);
                addressTextView.setText(pharmacyItem.getAddress());
                addressTextView.setTextSize(16);
                addressTextView.setTextColor(Color.parseColor("#00008B")); // Dark Blue
                addressTextView.setPadding(0, 0, 0, 10);

                // Click listener to open Google Maps
                addressTextView.setOnClickListener(v -> {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(pharmacyItem.getAddress()));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                });

                // Contact TextView (Green, Clickable → Opens Dialer)
                TextView contactTextView = new TextView(this);
                contactTextView.setText(pharmacyItem.getContact());
                contactTextView.setTextSize(16);
                contactTextView.setTextColor(Color.parseColor("#006400"));
                contactTextView.setPadding(0, 0, 0, 10);

                // Click listener to open dialer
                contactTextView.setOnClickListener(v -> {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:" + pharmacyItem.getContact()));
                    startActivity(dialIntent);
                });

                // Add views to the container
                pharmacyLayout.addView(nameTextView);
                pharmacyLayout.addView(addressTextView);
                pharmacyLayout.addView(contactTextView);

                // Set margins for spacing
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 10, 0, 10);
                pharmacyLayout.setLayoutParams(params);

                // Add the pharmacy layout to the main container
                nearbyPharmaciesContainer.addView(pharmacyLayout);
            }


        }

    }



    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates when the app is paused
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start location updates again when the app is resumed
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            Toast.makeText(this, "Location permission is required to show nearby blood banks.", Toast.LENGTH_SHORT).show();
        }
    }
}