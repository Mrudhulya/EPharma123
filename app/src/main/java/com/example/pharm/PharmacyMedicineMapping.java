package com.example.pharm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PharmacyMedicineMapping extends AppCompatActivity {

    private EditText pharmacyId, medicineId, etMedicineQuantity, etMedicinePrice;
    private Button mapButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_medicine_mapping);

        // Initialize UI components
        pharmacyId = findViewById(R.id.pharmacyId);
        medicineId = findViewById(R.id.medicineId);
        etMedicineQuantity = findViewById(R.id.etMedicineQuantity);
        etMedicinePrice = findViewById(R.id.etMedicinePrice);
        mapButton = findViewById(R.id.mapButton);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Set click listener for mapButton
        mapButton.setOnClickListener(v -> {
            String pharm = pharmacyId.getText().toString().trim();
            String med = medicineId.getText().toString().trim();
            String quantity = etMedicineQuantity.getText().toString().trim();
            String price = etMedicinePrice.getText().toString().trim();

            // Validate input fields
            if (pharm.isEmpty() || med.isEmpty() || quantity.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!isValidQuantity(quantity) || !isValidPrice(price)) {
                Toast.makeText(this, "Please enter valid quantity and price", Toast.LENGTH_SHORT).show();
            } else {
                // Parse valid quantity and price
                int quantityInt = Integer.parseInt(quantity);
                double priceDouble = Double.parseDouble(price);

                // Add medicine to database
                boolean isInserted = databaseHelper.addPharmacyMedicineMapping(pharm, med, quantityInt, priceDouble);

                if (isInserted) {
                    Toast.makeText(this, "Mapped successfully!", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(this, "Error in mapping. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Validate quantity (should be a positive integer)
    private boolean isValidQuantity(String quantity) {
        try {
            int quantityInt = Integer.parseInt(quantity);
            return quantityInt > 0; // Quantity should be positive
        } catch (NumberFormatException e) {
            return false; // Invalid number format
        }
    }

    // Validate price (should be a positive number)
    private boolean isValidPrice(String price) {
        try {
            double priceDouble = Double.parseDouble(price);
            return priceDouble > 0; // Price should be positive
        } catch (NumberFormatException e) {
            return false; // Invalid number format
        }
    }

    // Clear input fields
    private void clearFields() {
        pharmacyId.setText("");
        medicineId.setText("");
        etMedicineQuantity.setText("");
        etMedicinePrice.setText("");
    }
}
