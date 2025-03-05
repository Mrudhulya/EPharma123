package com.example.pharm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ManagePharmacy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_pharmacy);

        Button buttonAddPharmacy = findViewById(R.id.buttonAddPharmacy);
        Button buttonViewPharmacies = findViewById(R.id.buttonViewPharmacies);

        buttonAddPharmacy.setOnClickListener(v -> {
            Toast.makeText(this, "Add Pharmacies", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManagePharmacy.this, StorePharmacy.class);
            startActivity(intent);
            // Navigate to Add Pharmacy Activity (to be implemented)
            // startActivity(new Intent(this, AddPharmacyActivity.class));
        });
    }
}
