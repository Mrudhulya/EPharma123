package com.example.pharm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class UserRegistration extends AppCompatActivity {

    private EditText inputPassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_registration);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.navigation_bar_color));

        inputPassword = findViewById(R.id.inputPassword);

        // Toggle password visibility
        inputPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable drawableEnd = inputPassword.getCompoundDrawablesRelative()[2];
                if (drawableEnd != null) {
                    int drawableWidth = drawableEnd.getBounds().width();
                    if (event.getRawX() >= (inputPassword.getRight() - drawableWidth - inputPassword.getPaddingRight())) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
            }
            return false;
        });

        Button b1 = findViewById(R.id.buttonReg);
        b1.setOnClickListener(view -> {
            if (validateForm(view)) {
                // Retrieve user input
                EditText nameInput = findViewById(R.id.inputUsername);
                EditText phoneInput = findViewById(R.id.inputPhone);
                EditText emailInput = findViewById(R.id.inputEmail);

                String name = nameInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Initialize DatabaseHelper
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                boolean isInserted = dbHelper.registerUser(name, phone, email, password);

                if (isInserted) {
                    // Display success message and navigate to login
                    Snackbar.make(view, "Registration Successful", Snackbar.LENGTH_LONG)
                            .setAction("Login", v -> {
                                Intent intent = new Intent(UserRegistration.this, UserLogin.class);
                                startActivity(intent);
                                finish();
                            }).show();
                } else {
                    // Display failure message
                    Snackbar.make(view, "Username already exists", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            inputPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.baseline_lock_24, 0, R.drawable.eye_close, 0); // Set 'eye_close' icon
        } else {
            // Show password
            inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            inputPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.baseline_lock_24, 0, R.drawable.eye, 0); // Set 'eye' icon
        }
        isPasswordVisible = !isPasswordVisible;

        // Move cursor to the end of the text
        inputPassword.setSelection(inputPassword.getText().length());
    }

    private boolean validateForm(View view) {
        String username = ((EditText) findViewById(R.id.inputUsername)).getText().toString().trim();
        String email = ((EditText) findViewById(R.id.inputEmail)).getText().toString().trim();
        String phone = ((EditText) findViewById(R.id.inputPhone)).getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Validating fields

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Snackbar.make(view, "Please enter all fields", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(username)) {
            Snackbar.make(view, "Please enter username", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(view, "Please enter a valid email address", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(phone) || phone.length() != 10 || !TextUtils.isDigitsOnly(phone)) {
            Snackbar.make(view, "Please enter a valid 10-digit phone number", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Snackbar.make(view, "Please enter password", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        // Password validation: 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special character
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";
        if (!password.matches(passwordPattern)) {
            Snackbar.make(view, "Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, a digit, and a special character", Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
