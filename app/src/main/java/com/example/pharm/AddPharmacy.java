package com.example.pharm;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddPharmacy extends AppCompatActivity {

    private EditText pharmacyId, pharmacyName, pharmacyPhone, pharmacyEmail, pharmacyAddress, pharmacyOpeningTime, pharmacyClosingTime;
    private Button addPharmacyButton, retrievePharmacyButton, updatePharmacyButton;
    private DatabaseHelper databaseHelper;
    private double latitude = 0.0;
    private double longitude = 0.0;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pharmacy);

        // Initialize UI components
        pharmacyId = findViewById(R.id.pharmacyId);
        pharmacyName = findViewById(R.id.pharmacyName);
        pharmacyPhone = findViewById(R.id.pharmacyPhone);
        pharmacyEmail = findViewById(R.id.pharmacyEmail);
        pharmacyAddress = findViewById(R.id.pharmacyAddress);
        pharmacyOpeningTime = findViewById(R.id.pharmacyOpeningTime);
        pharmacyClosingTime = findViewById(R.id.pharmacyClosingTime);
        addPharmacyButton = findViewById(R.id.addPharmacyButton);
        updatePharmacyButton = findViewById(R.id.updatePharmacyButton);
        retrievePharmacyButton = findViewById(R.id.retrievePharmacyButton);

        // Initialize database helper

        databaseHelper = new DatabaseHelper(this);


        // Add Pharmacy Button Listener
        addPharmacyButton.setOnClickListener(v -> {
            String id = pharmacyId.getText().toString().trim();
            String name = pharmacyName.getText().toString().trim();
            String contact = pharmacyPhone.getText().toString().trim();
            String email = pharmacyEmail.getText().toString().trim();
            String address = pharmacyAddress.getText().toString().trim();
            String opening_time = pharmacyOpeningTime.getText().toString().trim();
            String closing_time = pharmacyClosingTime.getText().toString().trim();

            // Check if any field is empty
            if (id.isEmpty() || name.isEmpty() || contact.isEmpty() || email.isEmpty() || address.isEmpty() || opening_time.isEmpty() || closing_time.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Add pharmacy to database
                boolean isInserted = databaseHelper.addPharmacy(Integer.parseInt(id), name, contact, email, address, opening_time, closing_time);

                if (isInserted) {
                    Toast.makeText(this, "Pharmacy added successfully!", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(this, "Error adding pharmacy.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Retrieve Pharmacy Button Listener to store pharmacy page
        retrievePharmacyButton.setOnClickListener(v -> {
            String id = pharmacyId.getText().toString().trim();
            if (id.isEmpty()) {
                Toast.makeText(this, "Please enter Pharmacy ID to retrieve data", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor = databaseHelper.getPharmaciesById(id);
            if (cursor != null && cursor.moveToFirst()) {
                pharmacyName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHARMACY_NAME)));
                pharmacyPhone.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHARMACY_PHONE)));
                pharmacyEmail.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHARMACY_EMAIL)));
                pharmacyAddress.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHARMACY_ADDRESS)));
                pharmacyOpeningTime.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_OPENING_TIME)));
                pharmacyClosingTime.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CLOSING_TIME)));
                cursor.close();
            } else {
                Toast.makeText(this, "No pharmacy found with the given ID", Toast.LENGTH_SHORT).show();
            }

            if (cursor != null) {
                cursor.close(); // Close the cursor to avoid memory leaks
            }

        });

        // Update Pharmacy Button Listener
        updatePharmacyButton.setOnClickListener(v -> {
            String id = pharmacyId.getText().toString().trim();
            String name = pharmacyName.getText().toString().trim();
            String phone = pharmacyPhone.getText().toString().trim();
            String email = pharmacyEmail.getText().toString().trim();
            String address = pharmacyAddress.getText().toString().trim();
            String openingTime = pharmacyOpeningTime.getText().toString().trim();
            String closingTime = pharmacyClosingTime.getText().toString().trim();

            // Validate Fields
            if (id.isEmpty() || name.isEmpty() || phone.isEmpty()  || email.isEmpty() || address.isEmpty() || openingTime.isEmpty() || closingTime.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate numeric fields
            if (!isValidInteger(id)) {
                Toast.makeText(this, "Invalid pharmacy id", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update Pharmacy
            int rowsUpdated = databaseHelper.updatePharmacy(
                    Integer.parseInt(id),
                    name,
                    phone,
                    email,
                    address,
                    openingTime,
                    closingTime
            );

            if (rowsUpdated > 0) {
                Toast.makeText(this, "Pharmacy updated successfully!", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Error updating Pharmacy or ID not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Validate Integer
    private boolean isValidInteger(String id) {
        try {
            return Integer.parseInt(id) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearFields() {
        pharmacyId.setText("");
        pharmacyName.setText("");
        pharmacyPhone.setText("");
        pharmacyEmail.setText("");
        pharmacyAddress.setText("");
        pharmacyOpeningTime.setText("");
        pharmacyClosingTime.setText("");
    }

    private void onClick(View v) {
        String id = pharmacyId.getText().toString().trim();
        String name = pharmacyName.getText().toString().trim();
        String phone = pharmacyPhone.getText().toString().trim();
        String email = pharmacyEmail.getText().toString().trim();
        String address = pharmacyAddress.getText().toString().trim();
        String openingTime = pharmacyOpeningTime.getText().toString().trim();
        String closingTime = pharmacyClosingTime.getText().toString().trim();

        // Check if any field is empty
        if (id.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || openingTime.isEmpty() || closingTime.isEmpty()) {
            Toast.makeText(AddPharmacy.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate numeric fields
        if (!isValidInteger(id)) {
            Toast.makeText(this, "Invalid pharmacy id", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = databaseHelper.addPharmacy(Integer.parseInt(id), name, phone, email, address, openingTime, closingTime);
        if (isInserted) {
            Toast.makeText(AddPharmacy.this, "Pharmacy added successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(AddPharmacy.this, "Error adding pharmacy.", Toast.LENGTH_SHORT).show();
        }
    }
}