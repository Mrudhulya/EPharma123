package com.example.pharm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MapMedicineActivity extends AppCompatActivity {

    Spinner spinnerPharmacy, spinnerMedicine;
    CheckBox checkBoxAvailability;
    Button btnMapMedicine;
    DatabaseHelper dbHelper;
    List<Integer> pharmacyIds, medicineIds;
    EditText etQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_medicine);

        // Initialize the EditText
        etQuantity = findViewById(R.id.etQuantity);

        // Ensure it's not null before setting text
        if (etQuantity == null) {
            Log.e("MapMedicineActivity", "etQuantity is null! Check layout ID.");
        }

        spinnerPharmacy = findViewById(R.id.spinnerPharmacy);
        spinnerMedicine = findViewById(R.id.spinnerMedicine);
        checkBoxAvailability = findViewById(R.id.checkBoxAvailability);
        btnMapMedicine = findViewById(R.id.btnMapMedicine);
        dbHelper = new DatabaseHelper(this);

        loadPharmacies();
        loadMedicines();

        btnMapMedicine.setOnClickListener(v -> {
            mapMedicineToPharmacy();
        });

        checkBoxAvailability.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etQuantity.setVisibility(View.VISIBLE);  // Show quantity field
            } else {
                etQuantity.setVisibility(View.GONE);     // Hide quantity field
            }
        });


    }

    private void loadPharmacies() {
        List<String> pharmacyNames = new ArrayList<>();
        pharmacyIds = new ArrayList<>();
        Cursor cursor = dbHelper.getAllPharmacies();

        if (cursor.moveToFirst()) {
            do {
                pharmacyIds.add(cursor.getInt(0)); // Pharmacy ID
                pharmacyNames.add(cursor.getString(1)); // Pharmacy Name
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pharmacyNames);
        spinnerPharmacy.setAdapter(adapter);
    }

    private void loadMedicines() {
        List<String> medicineNames = new ArrayList<>();
        medicineIds = new ArrayList<>();
        Cursor cursor = dbHelper.getAllMedicines();

        if (cursor.moveToFirst()) {
            do {
                medicineIds.add(cursor.getInt(0)); // Medicine ID
                medicineNames.add(cursor.getString(1)); // Medicine Name
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, medicineNames);
        spinnerMedicine.setAdapter(adapter);

        // Set a listener to fetch quantity when a medicine is selected
        spinnerMedicine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedMedicineId = medicineIds.get(position);
                int medicine_quantity = dbHelper.getMedicineQuantity(selectedMedicineId); // Fetch quantity from DB
                etQuantity.setText(String.valueOf(medicine_quantity)); // Display quantity
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                etQuantity.setText(""); // Clear if nothing selected
            }
        });

    }

    private void mapMedicineToPharmacy() {
        int selectedPharmacyId = pharmacyIds.get(spinnerPharmacy.getSelectedItemPosition());
        int selectedMedicineId = medicineIds.get(spinnerMedicine.getSelectedItemPosition());
        int availability = checkBoxAvailability.isChecked() ? 1 : 0;
        int medicine_quantity = Integer.parseInt(etQuantity.getText().toString()); // Use the fetched quantity

        boolean success = dbHelper.mapMedicineToPharmacy(selectedPharmacyId, selectedMedicineId, availability, medicine_quantity);
        if (success) {
            Toast.makeText(this, "Medicine Mapped Successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error Mapping Medicine!", Toast.LENGTH_SHORT).show();
        }
    }
}
