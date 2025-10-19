package com.example.kwiktaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kwiktaxi.utils.AuthManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class DriverDashboardActivity extends AppCompatActivity {

    private MaterialTextView tvTaxiStatus;
    private MaterialButton btnGoOnline, btnGoOffline, btnLogout;
    private AuthManager authManager;
    private boolean isOnline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);

        initializeViews();
        setupClickListeners();
        loadDriverStatus();
    }

    private void initializeViews() {
        tvTaxiStatus = findViewById(R.id.tvTaxiStatus);
        btnGoOnline = findViewById(R.id.btnGoOnline);
        btnGoOffline = findViewById(R.id.btnGoOffline);
        btnLogout = findViewById(R.id.btnLogout);
        
        authManager = new AuthManager(this);
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> performLogout());
        btnGoOnline.setOnClickListener(v -> goOnline());
        btnGoOffline.setOnClickListener(v -> goOffline());
    }

    private void loadDriverStatus() {
        // Check if driver has a taxi assigned
        // For now, show no taxis assigned as per requirement
        tvTaxiStatus.setText("No taxis assigned");
        
        // Disable online/offline buttons since no taxi is assigned
        btnGoOnline.setEnabled(false);
        btnGoOffline.setEnabled(false);
    }

    private void goOnline() {
        if (!isOnline) {
            isOnline = true;
            btnGoOnline.setEnabled(false);
            btnGoOffline.setEnabled(true);
            Toast.makeText(this, "You are now online", Toast.LENGTH_SHORT).show();
        }
    }

    private void goOffline() {
        if (isOnline) {
            isOnline = false;
            btnGoOnline.setEnabled(true);
            btnGoOffline.setEnabled(false);
            Toast.makeText(this, "You are now offline", Toast.LENGTH_SHORT).show();
        }
    }

    private void performLogout() {
        // Clear stored token
        authManager.clearAuthData();
        
        // Navigate to login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
