package com.example.pharm;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Medicine extends AppCompatActivity {

    EditText medSearch;  // Reference to the EditText in MedicinesActivity
    LinearLayout vicks, aspirin, voliniSpray, benadrylSyrup;  // References to the medicine items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.navigation_bar_color));

        // Initialize views
        medSearch = findViewById(R.id.medSearch);
        vicks = findViewById(R.id.vicks);
        aspirin = findViewById(R.id.aspirin);
        voliniSpray = findViewById(R.id.voliniSpray);  // Assuming you have an ID for Volini Spray
        benadrylSyrup = findViewById(R.id.benadrylSyrup);  // Assuming you have an ID for Benadryl Syrup

        // Set click listeners for medicine items
        vicks.setOnClickListener(view -> openNearbyPharmacy(103)); // Assuming ID 103 for Vicks
        aspirin.setOnClickListener(view -> openNearbyPharmacy(104));  // Assuming ID 104 for Aspirin
        voliniSpray.setOnClickListener(view -> openNearbyPharmacy(105));  // Assuming ID 105 for Volini Spray
        benadrylSyrup.setOnClickListener(view -> openNearbyPharmacy(106));  // Assuming ID 106 for Benadryl Syrup

        // Add TextWatcher to the search bar
        medSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterMedicines(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed
            }
        });
    }

    private void filterMedicines(String query) {
        // Convert the query to lowercase for case-insensitive search
        query = query.toLowerCase();

        // Check each medicine item and show/hide based on the query
        if (vicks != null) {
            String vicksName = "Vicks Vaporub 50ml".toLowerCase();
            vicks.setVisibility(vicksName.contains(query) ? View.VISIBLE : View.GONE);
        }

        if (aspirin != null) {
            String aspirinName = "Aspirin 50mg strips of 10 tablets".toLowerCase();
            aspirin.setVisibility(aspirinName.contains(query) ? View.VISIBLE : View.GONE);
        }

        if (voliniSpray != null) {
            String voliniName = "Volini Spray 100g".toLowerCase();
            voliniSpray.setVisibility(voliniName.contains(query) ? View.VISIBLE : View.GONE);
        }

        if (benadrylSyrup != null) {
            String benadrylName = "Benadryl Syrup 50ml".toLowerCase();
            benadrylSyrup.setVisibility(benadrylName.contains(query) ? View.VISIBLE : View.GONE);
        }
    }

    private void openNearbyPharmacy(int medicineId) {
        Intent intent = new Intent(Medicine.this, NearbyPharmacy.class);
        intent.putExtra("MEDICINE_ID", medicineId);
        startActivity(intent);
    }
}