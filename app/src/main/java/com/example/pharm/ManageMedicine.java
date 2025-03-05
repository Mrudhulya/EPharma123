package com.example.pharm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ManageMedicine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicine);

        Button buttonAddMedicine = findViewById(R.id.buttonAddMedicine);
        Button buttonViewMedicines = findViewById(R.id.buttonViewMedicines);

        buttonAddMedicine.setOnClickListener(v -> {
            Toast.makeText(this, "Add medicines", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManageMedicine.this, AddMedicine.class);
            startActivity(intent);
            // Navigate to Add Medicine Activity (to be implemented)
            // startActivity(new Intent(this, AddMedicineActivity.class));
        });
    }
}
