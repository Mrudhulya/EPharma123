package com.example.pharm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class UserLogin extends AppCompatActivity {

    // Hardcoded admin credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private DatabaseHelper databaseHelper;
    private EditText inputUsername, inputPassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.navigation_bar_color));

        databaseHelper = new DatabaseHelper(this);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);

        Button button2 = findViewById(R.id.loginButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Snackbar.make(view, "Please enter both username and password", Snackbar.LENGTH_SHORT).show();
                } else {
                    boolean isValid = databaseHelper.checkUserCredentials(username, password);

                    if (isValid) {
                        Snackbar.make(view, "Login Successful", Snackbar.LENGTH_SHORT).show();
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Intent intent = new Intent(UserLogin.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }, 1000);
                    } else {
                        Snackbar.make(view, "Invalid username or password", Snackbar.LENGTH_SHORT).show();
                    }
                }

                // Check for admin login
                if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                    // Admin login successful
                    Snackbar.make(view, "Login Successful", Snackbar.LENGTH_SHORT).show();
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(UserLogin.this, AdminDashboard.class);
                        startActivity(intent);
                        finish();
                    }, 1000);
                }
            }
        });

        Button button1 = findViewById(R.id.forgotPassword);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLogin.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        Button button3 = findViewById(R.id.signUp);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLogin.this, UserRegistration.class);
                startActivity(intent);
            }
        });

        inputPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
            }
        });
    }


    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            inputPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.baseline_lock_24, 0, R.drawable.eye_close, 0); // Set 'eye_close' icon
            isPasswordVisible = false;
        } else {
            // Show password
            inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            inputPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.baseline_lock_24, 0, R.drawable.eye, 0); // Set 'eye' icon
            isPasswordVisible = true;
        }

        // Move cursor to the end of the text
        inputPassword.setSelection(inputPassword.getText().length());
    }
}
