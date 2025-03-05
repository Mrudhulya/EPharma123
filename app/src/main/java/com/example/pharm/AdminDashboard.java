package com.example.pharm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Card click listeners
        LinearLayout manageMedicine = findViewById(R.id.manageMedicine);
        manageMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, AddMedicine.class);
            startActivity(intent);
        });

        LinearLayout managePharmacies = findViewById(R.id.managePharmacies);
        managePharmacies.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, StorePharmacy.class);
            startActivity(intent);
        });

        LinearLayout mapPharmacyMedicine = findViewById(R.id.mapPharmacyMedicine);
        mapPharmacyMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, MapMedicineActivity.class);
            startActivity(intent);
        });

        LinearLayout orders = findViewById(R.id.orders);
        orders.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, OrderDetailsActivity.class);
            startActivity(intent);
        });

        // Logout button
        findViewById(R.id.buttonLogout).setOnClickListener(view -> {

            // Display a Snackbar with a logout message
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Snackbar.make(view, "Logging out...", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminDashboard.this, UserLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }, 2000);
        });
    }
}
