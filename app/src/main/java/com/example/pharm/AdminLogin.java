package com.example.pharm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// In your login activity
public class AdminLogin extends AppCompatActivity {

    // Hardcoded admin credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private DatabaseHelper databaseHelper;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);


        databaseHelper = new DatabaseHelper(this);

        buttonLogin.setOnClickListener(view -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Check for admin login
            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                // Admin login successful
                Toast.makeText(AdminLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminLogin.this, AdminDashboard.class);
                startActivity(intent);
                finish();
            } else {
                // Invalid credentials
                Toast.makeText(AdminLogin.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable drawableEnd = editTextPassword.getCompoundDrawablesRelative()[2];
                    if (drawableEnd != null) {
                        int drawableWidth = drawableEnd.getBounds().width();
                        if (event.getRawX() >= (editTextPassword.getRight() - drawableWidth - editTextPassword.getPaddingRight())) {
                            togglePasswordVisibility();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.baseline_lock_24, 0, R.drawable.eye_close, 0); // Set 'eye_close' icon
            isPasswordVisible = false;
        } else {
            // Show password
            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.baseline_lock_24, 0, R.drawable.eye, 0); // Set 'eye' icon
            isPasswordVisible = true;
        }

        // Move cursor to the end of the text
        editTextPassword.setSelection(editTextPassword.getText().length());
    }
}