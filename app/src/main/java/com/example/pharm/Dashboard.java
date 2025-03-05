package com.example.pharm;

import static com.example.pharm.DatabaseHelper.COLUMN_MAPPING_MEDICINE_ID;
import static com.example.pharm.DatabaseHelper.COLUMN_MAPPING_PHARMACY_ID;
import static com.example.pharm.DatabaseHelper.COLUMN_MEDICINE_ID;
import static com.example.pharm.DatabaseHelper.COLUMN_MEDICINE_NAME;
import static com.example.pharm.DatabaseHelper.COLUMN_PHARMACY_ID;
import static com.example.pharm.DatabaseHelper.TABLE_MEDICINES;
import static com.example.pharm.DatabaseHelper.TABLE_PHARMACIES;
import static com.example.pharm.DatabaseHelper.TABLE_PHARMACY_MEDICINE_MAPPING;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle;
    Button searchButton;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private TextView locationText;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private SQLiteOpenHelper dbHelper;

    //cart manager
    // Inside Dashboard.java, add the following code
    private CartManager cartManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //cart manager
        // Initialize CartManager
        cartManager = new CartManager(this);

        // View Cart button click listener
        LinearLayout btnViewCart = findViewById(R.id.btn_view_cart);
        btnViewCart.setOnClickListener(v -> {

            Intent intent = new Intent(Dashboard.this, ViewCartActivity.class);
            startActivity(intent);

        });


        // Location
        locationText = findViewById(R.id.location_text);

        // Initialize the location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Create a LocationCallback to receive location updates
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult.getLocations().size() > 0) {
                    Location location = locationResult.getLastLocation();
                    updateLocation(location);
                }
            }
        };

        // Fetch the last known location and start updates
        fetchLocation();


        drawerLayout = findViewById(R.id.drawer_layout);
        buttonDrawerToggle = findViewById(R.id.buttonDrawerToggle);

        buttonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            Log.e("Dashboard", "Toolbar is null");
        } else {
            setSupportActionBar(toolbar);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // View more button
        Button viewMoreButton = findViewById(R.id.viewMore);
        viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Medicine.class);
                startActivity(intent);
            }
        });

        Button searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Medicines page
                Intent intent = new Intent(Dashboard.this, Medicine.class);
                startActivity(intent);
            }
        });

        // Inside onCreate() method
        LinearLayout paracetamol = findViewById(R.id.paracetamol);
        LinearLayout ceterizine = findViewById(R.id.ceterizine);

        paracetamol.setOnClickListener(view -> openNearbyPharmacy(101)); // Assuming ID 1 for Paracetamol
        ceterizine.setOnClickListener(view -> openNearbyPharmacy(102));  // Assuming ID 2 for Ceterizine



        LinearLayout logout = findViewById(R.id.logoutbtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display a Snackbar with a logout message
                Snackbar.make(drawerLayout, "Logging out...", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, UserLogin.class);

                startActivity(intent);
            }
        });

        LinearLayout imageView2 = findViewById(R.id.medicine);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Medicine.class);
                startActivity(intent);
            }
        });
    }

    private void openNearbyPharmacy(int medicineId) {
        Intent intent = new Intent(Dashboard.this, NearbyPharmacy.class);
        intent.putExtra("MEDICINE_ID", medicineId);
        startActivity(intent);
    }

    //location access methods
    private void fetchLocation() {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Fetch last known location first
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                updateLocation(location);
            } else {
                // Start continuous location updates if last known location is null
                requestLocationUpdates();
            }
        });
    }

    private void requestLocationUpdates() {
        // Create a location request to get location updates
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // Update location every 5 seconds
        locationRequest.setFastestInterval(2000); // Get location updates as quickly as possible

        // Start requesting location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void updateLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Use Geocoder to fetch the address
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                // Fetch locality, state, and postal code
                String locality = address.getLocality();
                String state = address.getAdminArea();
                String postalCode = address.getPostalCode();

                // Set default values if fields are null
                locality = locality != null ? locality : "Unknown Locality";
                state = state != null ? state : "Unknown State";
                postalCode = postalCode != null ? postalCode : "Unknown Pincode";

                // Combine them into a single formatted address
                String formattedAddress = locality + ", " + state + " - " + postalCode;

                // Display the formatted address
                locationText.setText(formattedAddress);
            } else {
                locationText.setText("Unable to fetch address.");
            }
        } catch (IOException e) {
            locationText.setText("Error fetching address: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                locationText.setText("Permission denied.");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


    private void setSupportActionBar(Toolbar toolbar) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navHome) {
            // Handle Home navigation
        } else if (id == R.id.navMedicines) {
            // Handle Medicines navigation
            medicine();
        } else if (id == R.id.navShare) {
            // Handle Share navigation
            shareContent();
        } else if (id == R.id.navLogout) {
            // Handle Logout
            logoutUser();
        }
        
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Share content
    private void shareContent() {
        String message = "Check out this app!";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.setPackage("com.whatsapp");
        try {
            startActivity(shareIntent); // Launch the sharing intent
        } catch (Exception e) {
            e.printStackTrace();
            // If WhatsApp is not installed, display a message
            Intent fallbackIntent = new Intent(Intent.ACTION_SEND);
            fallbackIntent.setType("text/plain");
            fallbackIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(fallbackIntent, "Share via"));
        }
    }

    // Logout user
    private void logoutUser() {
        // Display a Snackbar with a logout message
        Snackbar.make(drawerLayout, "Logging out...", Snackbar.LENGTH_SHORT).show();

        // Use a Handler to add a 2-second delay before navigating to UserLogin
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Dashboard.this, UserLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears activity stack
            startActivity(intent);
            finish(); // Close the current activity
        }, 2000); // 2-second delay
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void medicine() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Dashboard.this, Medicine.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears activity stack
            startActivity(intent);
            finish(); // Close the current activity
        }, 1000);
    }
}
