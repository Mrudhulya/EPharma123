package com.example.pharm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import javax.mail.MessagingException;

public class ForgotPassword extends AppCompatActivity {

    EditText emailInput, otpInput;
    Button getOtpBtn, verifyOtpBtn;
    DatabaseHelper databaseHelper;
    String generatedOTP;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.emailField);
        otpInput = findViewById(R.id.otpField);
        getOtpBtn = findViewById(R.id.getOtpBtn);
        verifyOtpBtn = findViewById(R.id.verifyOtpBtn);

        databaseHelper = new DatabaseHelper(this);

        getOtpBtn.setOnClickListener(v -> sendOtp());
        verifyOtpBtn.setOnClickListener(v -> verifyOtp());
    }

    private void sendOtp() {
        userEmail = emailInput.getText().toString().trim();

        // Check if email exists in SQLite database
        if (databaseHelper.checkEmailExists(userEmail)) {
            generatedOTP = generateOTP();
            new Thread(() -> {
                try {
                    GMailSender sender = new GMailSender();
                    sender.sendMail(userEmail, "Password Reset OTP", "Your OTP is: " + generatedOTP);
                    runOnUiThread(() -> Toast.makeText(ForgotPassword.this, "OTP Sent!", Toast.LENGTH_SHORT).show());
                } catch (MessagingException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(ForgotPassword.this, "Error Sending OTP: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(ForgotPassword.this, "Unexpected Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();
        } else {
            Toast.makeText(this, "Email not registered!", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyOtp() {
        String enteredOtp = otpInput.getText().toString().trim();

        if (enteredOtp.equals(generatedOTP)) {
            // Retrieve password from database
            String password = databaseHelper.getPasswordByEmail(userEmail);

            // Send the password to the registered email
            new Thread(() -> {
                try {
                    GMailSender sender = new GMailSender();
                    sender.sendMail(userEmail, "Your Recovered Password", "Your Password is: " + password);
                    runOnUiThread(() -> Toast.makeText(ForgotPassword.this, "Password sent to your email!", Toast.LENGTH_SHORT).show());
                } catch (MessagingException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(ForgotPassword.this, "Error Sending Password: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(ForgotPassword.this, "Unexpected Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();
        } else {
            Toast.makeText(this, "Invalid OTP!", Toast.LENGTH_SHORT).show();
        }
    }


    private String generateOTP() {
        return String.valueOf(new Random().nextInt(899999) + 100000); // 6-digit OTP
    }
}