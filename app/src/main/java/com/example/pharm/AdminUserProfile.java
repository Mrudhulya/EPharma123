package com.example.pharm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminUserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_user_profile);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.navigation_bar_color));
        ImageButton imageButton1 =findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminUserProfile.this, UserLogin.class);
                startActivity(intent);
            }
        });

        ImageButton imageButton2 =findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminUserProfile.this, AdminLogin.class);
                startActivity(intent);
            }
        });
    }
}