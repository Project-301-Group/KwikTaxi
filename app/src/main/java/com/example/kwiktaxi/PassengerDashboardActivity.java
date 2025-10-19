package com.example.kwiktaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.utils.AuthManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class PassengerDashboardActivity extends AppCompatActivity {

    private TextInputEditText etSearch;
    private RecyclerView rvTaxiRanks;
    private MaterialTextView tvEmptyState;
    private MaterialButton btnLogout;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_dashboard);

        initializeViews();
        setupClickListeners();
        loadTaxiRanks();
    }

    private void initializeViews() {
        etSearch = findViewById(R.id.etSearch);
        rvTaxiRanks = findViewById(R.id.rvTaxiRanks);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        btnLogout = findViewById(R.id.btnLogout);
        
        authManager = new AuthManager(this);
        
        // Setup RecyclerView
        rvTaxiRanks.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> performLogout());
        
        etSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // Perform search when focus is lost
                performSearch();
            }
        });
    }

    private void loadTaxiRanks() {
        // Mock data for taxi ranks
        List<String> taxiRanks = new ArrayList<>();
        taxiRanks.add("Central Station Taxi Rank");
        taxiRanks.add("Airport Taxi Rank");
        taxiRanks.add("Mall Taxi Rank");
        taxiRanks.add("Hospital Taxi Rank");
        taxiRanks.add("University Taxi Rank");
        
        if (taxiRanks.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvTaxiRanks.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvTaxiRanks.setVisibility(View.VISIBLE);
            // Set adapter with taxi ranks data
            // For now, just show a simple list
        }
    }

    private void performSearch() {
        String searchQuery = etSearch.getText().toString().trim();
        if (!searchQuery.isEmpty()) {
            // Implement search functionality
            Toast.makeText(this, "Searching for: " + searchQuery, Toast.LENGTH_SHORT).show();
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
