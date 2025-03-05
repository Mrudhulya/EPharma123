package com.example.pharm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MedicineDetails extends AppCompatActivity {

    private TextView medicineNameTextView, medicinePriceTextView, medicineExpiryTextView, tvQuantity;
    private Button btnPlus, btnMinus;
    private SQLiteDatabase db;

    private int quantity = 1; // Default quantity
    private double pricePerUnit = 0.0; // Variable to store the price per unit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine_details);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.navigation_bar_color));

        // Initialize Views
        btnPlus = findViewById(R.id.btn_plus);
        btnMinus = findViewById(R.id.btn_minus);
        tvQuantity = findViewById(R.id.tv_quantity);
        medicineNameTextView = findViewById(R.id.medicineName); // Corrected: TextView, not String
        medicinePriceTextView = findViewById(R.id.medicinePrice); // Corrected: TextView, not String
        medicineExpiryTextView = findViewById(R.id.medicineExpiry); // Corrected: TextView, not String

        // Open database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();


        // Get medicine ID from intent
        int medicineId = getIntent().getIntExtra("MEDICINE_ID", -1); // Default value -1 if not found
        Log.d("MedicineDetails", "Received medicine ID: " + medicineId); // Debug log

        if (medicineId != -1) {
            loadMedicineDetails(medicineId); // Load medicine details based on the ID
        } else {
            // Handle the case where no medicine ID is passed
            Log.d("MedicineDetails", "No medicine ID passed in intent"); // Debug log
            medicineNameTextView.setText("Medicine Name: N/A");
            medicineExpiryTextView.setText("Expiry Date: N/A");
            medicinePriceTextView.setText("Price: ₹ 0.00");
        }

        // Plus button click listener
        btnPlus.setOnClickListener(v -> {
            quantity++;
            updateQuantityAndPrice();
        });

        // Minus button click listener
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) { // Prevent going below 1
                quantity--;
                updateQuantityAndPrice();
            }
        });
    }

    private void loadMedicineDetails(int medicineId) {
        Log.d("MedicineDetails", "Fetching details for medicine ID: " + medicineId);

        // Correct query with proper parameter formatting
        Cursor cursor = db.rawQuery("SELECT * FROM medicines WHERE medicine_id=?", new String[]{String.valueOf(medicineId)});

        if (cursor.moveToFirst()) {
            // Fetch data from the cursor
            String name = cursor.getString(cursor.getColumnIndexOrThrow("medicine_name"));
            pricePerUnit = cursor.getDouble(cursor.getColumnIndexOrThrow("medicine_price"));
            String expiry = cursor.getString(cursor.getColumnIndexOrThrow("medicine_expiry_date"));

            Log.d("MedicineDetails", "Medicine found: " + name);
            Log.d("MedicineDetails", "Price: " + pricePerUnit);
            Log.d("MedicineDetails", "Expiry Date: " + expiry);

            // Update the UI with the fetched data
            medicineNameTextView.setText("Medicine Name: " + name);
            medicineExpiryTextView.setText("Expiry Date: " + expiry);
            updateQuantityAndPrice();
        } else {
            Log.d("MedicineDetails", "No medicine found with ID: " + medicineId);
            medicineNameTextView.setText("Medicine not found");
            medicineExpiryTextView.setText("Expiry Date: N/A");
            medicinePriceTextView.setText("Price: ₹ 0.00");
        }
        cursor.close(); // Close the cursor to avoid memory leaks
    }

    private void updateQuantityAndPrice() {
        tvQuantity.setText(String.valueOf(quantity));
        double totalPrice = pricePerUnit * quantity;
        medicinePriceTextView.setText("Price: ₹ " + totalPrice);
    }
}