package com.example.pharm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pharm.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StorePharmacy extends AppCompatActivity {

    private EditText etPharmacyId, etPharmacyName, etContact, etEmail, etAddress, etOpeningTime, etClosingTime;
    private Button btnSubmitPharmacy, btnRetrieve, btnUpdate;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_pharmacy);

        // Initialize views
        etPharmacyId = findViewById(R.id.etPharmacyId);
        etPharmacyName = findViewById(R.id.etPharmacyName);
        etContact = findViewById(R.id.etContact);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etOpeningTime = findViewById(R.id.etOpeningTime);
        etClosingTime = findViewById(R.id.etClosingTime);
        btnSubmitPharmacy = findViewById(R.id.btnSubmitPharmacy);
        btnRetrieve = findViewById(R.id.btnRetrieve);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Initialize Database Helper
        databaseHelper = new DatabaseHelper(this);

        // Button listeners
        btnSubmitPharmacy.setOnClickListener(v -> addPharmacyDetails());
        btnRetrieve.setOnClickListener(v -> retrievePharmacyDetails());
        btnUpdate.setOnClickListener(v -> updatePharmacyDetails());

    }

    // Add pharmacy details
    private void addPharmacyDetails() {
        String id = etPharmacyId.getText().toString().trim();
        String name = etPharmacyName.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String openingTime = etOpeningTime.getText().toString().trim();
        String closingTime = etClosingTime.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(email) || TextUtils.isEmpty(address) || TextUtils.isEmpty(openingTime) || TextUtils.isEmpty(closingTime)) {
            Toast.makeText(this, "Please fill all required fields!", Toast.LENGTH_SHORT).show();
            return;
        }
// Convert address to latitude and longitude
        double latitude = 0.0, longitude = 0.0;
        try {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Check if the pharmacy ID already exists
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PHARMACIES +
                " WHERE " + DatabaseHelper.COLUMN_PHARMACY_ID + " = ?", new String[]{id});

        if (cursor.moveToFirst()) {
            Toast.makeText(this, "Pharmacy ID already exists!", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();

        // Insert into pharmacy table
        ContentValues pharmacyValues = new ContentValues();
        pharmacyValues.put(DatabaseHelper.COLUMN_PHARMACY_ID, Integer.parseInt(id));
        pharmacyValues.put(DatabaseHelper.COLUMN_PHARMACY_NAME, name);
        pharmacyValues.put(DatabaseHelper.COLUMN_PHARMACY_PHONE, contact);
        pharmacyValues.put(DatabaseHelper.COLUMN_PHARMACY_EMAIL, email);
        pharmacyValues.put(DatabaseHelper.COLUMN_PHARMACY_ADDRESS, address);
        pharmacyValues.put(DatabaseHelper.COLUMN_OPENING_TIME, openingTime);
        pharmacyValues.put(DatabaseHelper.COLUMN_CLOSING_TIME, closingTime);
        pharmacyValues.put(DatabaseHelper.COLUMN_PHARMACY_LATITUDE,latitude);
        pharmacyValues.put(DatabaseHelper.COLUMN_PHARMACY_LONGITUDE,longitude);
        long bankResult = db.insert(DatabaseHelper.TABLE_PHARMACIES, null, pharmacyValues);

        if (bankResult == -1) {
            Toast.makeText(this, "Failed to add Pharmacy details!", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        db.close();
        Toast.makeText(this, "Pharmacy Added Successfully!", Toast.LENGTH_SHORT).show();
        clearForm();
    }

    // Retrieve pharmacy details
    private void retrievePharmacyDetails() {
        String id = etPharmacyId.getText().toString().trim();

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "Please enter Pharmacy ID!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Retrieve pharmacy table data
        Cursor pharmCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PHARMACIES +
                " WHERE " + DatabaseHelper.COLUMN_PHARMACY_ID + " = ?", new String[]{id});

        if (pharmCursor.moveToFirst()) {
            etPharmacyName.setText(pharmCursor.getString(pharmCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHARMACY_NAME)));
            etContact.setText(pharmCursor.getString(pharmCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHARMACY_PHONE)));
            etEmail.setText(pharmCursor.getString(pharmCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHARMACY_EMAIL)));
            etAddress.setText(pharmCursor.getString(pharmCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHARMACY_ADDRESS)));
            etOpeningTime.setText(pharmCursor.getString(pharmCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OPENING_TIME)));
            etClosingTime.setText(pharmCursor.getString(pharmCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLOSING_TIME)));
        }

        pharmCursor.close();
        db.close();
    }

    // Update pharmacy details
    private void updatePharmacyDetails() {
        String id = etPharmacyId.getText().toString().trim();
        String name = etPharmacyName.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String openingTime = etOpeningTime.getText().toString().trim();
        String closingTime = etClosingTime.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(email) || TextUtils.isEmpty(address) || TextUtils.isEmpty(openingTime) || TextUtils.isEmpty(closingTime)) {
            Toast.makeText(this, "Please fill all required fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Update Pharmacy table
        ContentValues pharmacyValues = new ContentValues();
        pharmacyValues.put(DatabaseHelper.COLUMN_PHARMACY_NAME, name);
        pharmacyValues.put(DatabaseHelper.COLUMN_PHARMACY_PHONE, contact);
        pharmacyValues.put(DatabaseHelper.COLUMN_PHARMACY_EMAIL, email);
        pharmacyValues.put(DatabaseHelper.COLUMN_PHARMACY_ADDRESS, address);
        pharmacyValues.put(DatabaseHelper.COLUMN_OPENING_TIME, openingTime);
        pharmacyValues.put(DatabaseHelper.COLUMN_CLOSING_TIME, closingTime);


        int pharmResult = db.update(DatabaseHelper.TABLE_PHARMACIES, pharmacyValues,
                DatabaseHelper.COLUMN_PHARMACY_ID + " = ?", new String[]{id});

        if (pharmResult == 0) {
            Toast.makeText(this, "Failed to update blood bank details!", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        db.close();
        Toast.makeText(this, "Pharmacy Updated Successfully!", Toast.LENGTH_SHORT).show();
        clearForm();
    }

    // Clear the form after submission
    private void clearForm() {
        etPharmacyId.setText("");
        etPharmacyName.setText("");
        etContact.setText("");
        etEmail.setText("");
        etAddress.setText("");
        etOpeningTime.setText("");
        etClosingTime.setText("");
    }
}
