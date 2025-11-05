package com.example.kwiktaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kwiktaxi.models.DriverTaxiInfoResponse;
import com.example.kwiktaxi.network.DriverApi;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.utils.AuthManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class DriverDashboardActivity extends AppCompatActivity {

    private MaterialTextView tvTaxiStatus;
    private MaterialTextView tvDriverName;
    private MaterialTextView tvDriverPhone;
    private MaterialButton btnGoOnline, btnGoOffline, btnLogout;
    private AuthManager authManager;
    private boolean isOnline = false;
    private DriverApi driverApi;
    private DriverTaxiInfoResponse.Taxi myTaxi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);

        initializeViews();
        setupClickListeners();
        loadDriverStatus();
        loadMyTaxi();
    }

    private void initializeViews() {
        tvTaxiStatus = findViewById(R.id.tvTaxiStatus);
        tvDriverName = findViewById(R.id.tvDriverName);
        tvDriverPhone = findViewById(R.id.tvDriverPhone);
        btnGoOnline = findViewById(R.id.btnGoOnline);
        btnGoOffline = findViewById(R.id.btnGoOffline);
        btnLogout = findViewById(R.id.btnLogout);
        
        authManager = new AuthManager(this);
        driverApi = RetrofitClient.getInstance().getDriverApi();
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> performLogout());
        btnGoOnline.setOnClickListener(v -> goOnline());
        btnGoOffline.setOnClickListener(v -> goOffline());
        tvTaxiStatus.setOnClickListener(v -> {
            if (myTaxi != null) {
                Intent intent = new Intent(this, TaxiDetailActivity.class);
                intent.putExtra("taxi_id", myTaxi.getId());
                startActivity(intent);
            }
        });
    }

    private void loadDriverStatus() {
        // Check if driver has a taxi assigned
        // For now, show no taxis assigned as per requirement
        tvTaxiStatus.setText("No taxis assigned");
        
        // Disable online/offline buttons since no taxi is assigned
        btnGoOnline.setEnabled(false);
        btnGoOffline.setEnabled(false);
    }

    private void loadMyTaxi() {
        int userId = authManager.getUserId();
        if (userId == -1) return;
        driverApi.getDriverTaxi(userId).enqueue(new retrofit2.Callback<DriverTaxiInfoResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DriverTaxiInfoResponse> call, retrofit2.Response<DriverTaxiInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getTaxi() != null) {
                    myTaxi = response.body().getTaxi();
                    DriverTaxiInfoResponse.Driver d = response.body().getDriver();
                    if (d != null) {
                        tvDriverName.setText(d.getFullName());
                        tvDriverPhone.setText(d.getPhone());
                    }
                    String status = myTaxi.getRegistration_number() + " (" + myTaxi.getStatus() + ")\n" +
                            (myTaxi.getRank() != null ? myTaxi.getRank().getName() : "");
                    tvTaxiStatus.setText(status + "\nTap for details");
                    btnGoOnline.setEnabled(true);
                } else {
                    tvTaxiStatus.setText("No taxis assigned");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<DriverTaxiInfoResponse> call, Throwable t) {
                Toast.makeText(DriverDashboardActivity.this, "Failed to load taxi", Toast.LENGTH_SHORT).show();
            }
        });
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
