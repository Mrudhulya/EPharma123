package com.example.pharm;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddMedicine extends AppCompatActivity {

    private EditText etMedicineId, etMedicineName, etMedicineType, etMedicineQuantity, etMedicinePrice, etMedicineExpiryDate, etMedicineDescription;
    private Button btnAddMedicine, btnRetrieveMedicine, btnUpdateMedicine;

    private DatabaseHelper databaseHelper; // Assuming you have a helper class for database operations.

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        etMedicineId = findViewById(R.id.etMedicineId);
        etMedicineName = findViewById(R.id.etMedicineName);
        etMedicineType = findViewById(R.id.etMedicineType);
        etMedicineQuantity = findViewById(R.id.etMedicineQuantity);
        etMedicinePrice = findViewById(R.id.etMedicinePrice);
        etMedicineExpiryDate = findViewById(R.id.etMedicineExpiryDate);
        etMedicineDescription = findViewById(R.id.etMedicineDescription);
        btnAddMedicine = findViewById(R.id.btnAddMedicine);
        btnRetrieveMedicine = findViewById(R.id.btnRetrieveMedicine);
        btnUpdateMedicine = findViewById(R.id.btnUpdateMedicine);

        databaseHelper = new DatabaseHelper(this);

        // Set button click listener
        btnAddMedicine.setOnClickListener(v -> {
            String medicine_id = etMedicineId.getText().toString().trim();
            String name = etMedicineName.getText().toString().trim();
            String type = etMedicineType.getText().toString().trim();
            String quantity = etMedicineQuantity.getText().toString().trim();
            String price = etMedicinePrice.getText().toString().trim();
            String expiryDate = etMedicineExpiryDate.getText().toString().trim();
            String description = etMedicineDescription.getText().toString().trim();

            // Check if any field is empty
            if (medicine_id.isEmpty() || name.isEmpty() || type.isEmpty() || quantity.isEmpty() || price.isEmpty() || expiryDate.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }

            if (!isValidQuantity(quantity) || !isValidPrice(price)) {
                Toast.makeText(this, "Please enter valid quantity and price", Toast.LENGTH_SHORT).show();
                return;
            }

            else {
                // Add medicine to database
                boolean isInserted = databaseHelper.addMedicine(medicine_id, name, type, quantity, price, expiryDate, description);

                if (isInserted) {
                    Toast.makeText(this, "Medicine added successfully!", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(this, "Error adding medicine.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Retrieve Medicine Button Listener
        btnRetrieveMedicine.setOnClickListener(v -> {
            String id = etMedicineId.getText().toString().trim();
            if (id.isEmpty()) {
                Toast.makeText(this, "Please enter Medicine ID to retrieve data", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor = databaseHelper.getMedicinesById(id);
            if (cursor != null && cursor.moveToFirst()) {
                etMedicineName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEDICINE_NAME)));
                etMedicineType.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEDICINE_TYPE)));
                etMedicineQuantity.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEDICINE_QUANTITY)));
                etMedicinePrice.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEDICINE_PRICE)));
                etMedicineExpiryDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEDICINE_EXPIRY_DATE)));
                etMedicineDescription.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEDICINE_DESCRIPTION)));
                cursor.close();
            } else {
                Toast.makeText(this, "No medicine found with the given ID", Toast.LENGTH_SHORT).show();
            }

            if (cursor != null) {
                cursor.close(); // Close the cursor to avoid memory leaks
            }
        });

        // Update Medicine Button Listener
        btnUpdateMedicine.setOnClickListener(v -> {
            String id = etMedicineId.getText().toString().trim();
            String name = etMedicineName.getText().toString().trim();
            String type = etMedicineType.getText().toString().trim();
            String quantity = etMedicineQuantity.getText().toString().trim();
            String price = etMedicinePrice.getText().toString().trim();
            String expiryDate = etMedicineExpiryDate.getText().toString().trim();
            String description = etMedicineDescription.getText().toString().trim();

            // Validate Fields
            if (id.isEmpty() || name.isEmpty() || type.isEmpty() || quantity.isEmpty() || price.isEmpty() || expiryDate.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidQuantity(quantity) || !isValidPrice(price)) {
                Toast.makeText(this, "Please enter valid quantity and price", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update Medicine
            int rowsUpdated = databaseHelper.updateMedicine(
                    Integer.parseInt(id),
                    name,
                    type,
                    Integer.parseInt(quantity),
                    Double.parseDouble(price),
                    expiryDate,
                    description
            );

            if (rowsUpdated > 0) {
                Toast.makeText(this, "Medicine updated successfully!", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Error updating medicine or ID not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Validate quantity
    private boolean isValidQuantity(String quantity) {
        try {
            int quantityInt = Integer.parseInt(quantity);
            return quantityInt > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Validate price
    private boolean isValidPrice(String price) {
        try {
            double priceDouble = Double.parseDouble(price);
            return priceDouble > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearFields() {
        etMedicineId.setText("");
        etMedicineName.setText("");
        etMedicineType.setText("");
        etMedicineQuantity.setText("");
        etMedicinePrice.setText("");
        etMedicineExpiryDate.setText("");
        etMedicineDescription.setText("");
    }
}
