package com.example.kwiktaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.AdminDetailsResponse;
import com.example.kwiktaxi.network.ApiService;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.utils.AuthManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardActivity extends AppCompatActivity {

    private MaterialButton btnAddTaxi, btnLogout;
    private RecyclerView rvDrivers;
    private MaterialTextView tvEmptyState;
    private MaterialTextView tvAdminName, tvAdminPhone, tvAdminRank;
    private AuthManager authManager;
    private ApiService apiService;
    private MaterialButton btnManageTaxis, btnManageRanks, btnManageDrivers, btnViewAnalytics;

    private MaterialCardView cardViewAnalytics, cardManageTaxis, cardManageRanks, cardManageDrivers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        initializeViews();
        setupClickListeners();
        loadAdminDetails();
        loadDrivers();
    }

    private void initializeViews() {
        cardViewAnalytics = findViewById(R.id.cardViewAnalytics);
        cardManageTaxis = findViewById(R.id.cardManageTaxis);
        cardManageRanks = findViewById(R.id.cardManageRanks);
        cardManageDrivers = findViewById(R.id.cardManageDrivers);

        btnLogout = findViewById(R.id.btnLogout);
        rvDrivers = findViewById(R.id.rvDrivers);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        tvAdminName = findViewById(R.id.tvAdminName);
        tvAdminPhone = findViewById(R.id.tvAdminPhone);
        tvAdminRank = findViewById(R.id.tvAdminRank);
        
        authManager = new AuthManager(this);
        apiService = RetrofitClient.getInstance().getApiService();
        
        // Setup RecyclerView
        rvDrivers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> performLogout());

        cardManageTaxis.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageTaxisActivity.class);
            startActivity(intent);
        });

        cardManageRanks.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageRankDestinationsActivity.class);
            startActivity(intent);
        });

        cardManageDrivers.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageDriversActivity.class);
            startActivity(intent);
        });

        cardViewAnalytics.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewAnalyticsActivity.class);
            startActivity(intent);
        });
    }


    private void loadAdminDetails() {
        int userId = authManager.getUserId();
        if (userId == -1) {
            return;
        }

        apiService.getAdminDetails(userId).enqueue(new Callback<AdminDetailsResponse>() {
            @Override
            public void onResponse(Call<AdminDetailsResponse> call, Response<AdminDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getAdmin() != null) {
                    AdminDetailsResponse.AdminInfo admin = response.body().getAdmin();
                    
                    // Update admin name
                    String fullName = admin.getFullName();
                    if (!fullName.isEmpty()) {
                        tvAdminName.setText(fullName);
                    }
                    
                    // Update phone
                    if (admin.getPhone() != null && !admin.getPhone().isEmpty()) {
                        tvAdminPhone.setText(admin.getPhone());
                    }
                    
                    // Update rank name
                    String rankInfo = admin.getRankName();
                    if (rankInfo != null && !rankInfo.isEmpty()) {
                        String location = "";
                        if (admin.getCity() != null && !admin.getCity().isEmpty()) {
                            location = admin.getCity();
                        }
                        if (admin.getProvince() != null && !admin.getProvince().isEmpty()) {
                            location += (location.isEmpty() ? "" : ", ") + admin.getProvince();
                        }
                        if (!location.isEmpty()) {
                            rankInfo += " (" + location + ")";
                        }
                        tvAdminRank.setText(rankInfo);
                    } else {
                        tvAdminRank.setText("No rank assigned");
                    }
                }
            }

            @Override
            public void onFailure(Call<AdminDetailsResponse> call, Throwable t) {
                // Silently fail - keep placeholder text
            }
        });
    }

    private void loadDrivers() {
        // Mock data for drivers
        List<String> drivers = new ArrayList<>();
        drivers.add("John Doe - License: ABC123");
        drivers.add("Jane Smith - License: XYZ789");
        drivers.add("Mike Johnson - License: DEF456");
        
        if (drivers.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvDrivers.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvDrivers.setVisibility(View.VISIBLE);
            // Set adapter with drivers data
            // For now, just show a simple list
        }
    }

    private void addTaxiToSystem() {
        // Show dialog or navigate to add taxi activity
        Toast.makeText(this, "Add Taxi functionality - To be implemented", Toast.LENGTH_SHORT).show();
        
        // For now, just show a message
        // In a real app, this would open a form to add taxi details
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
