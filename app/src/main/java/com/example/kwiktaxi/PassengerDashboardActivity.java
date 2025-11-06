package com.example.kwiktaxi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.PassengerRanksResponse;
import com.example.kwiktaxi.models.PassengerRankDestinationsResponse;
import com.example.kwiktaxi.network.PassengerApi;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.models.PassengerInfoResponse;
import com.example.kwiktaxi.utils.AuthManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class PassengerDashboardActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private TextInputEditText etSearch;
    private RecyclerView rvList;
    private MaterialTextView tvEmptyState;
    private MaterialButton btnLogout, btnScanQr, btnActiveTrips;
    private MaterialTextView tvPassengerName, tvPassengerPhone, tvPassengerAddress, tvTripCount;
    private android.widget.ImageButton btnTripHistory;
    private AuthManager authManager;
    private PassengerApi passengerApi;

    private int currentTab = 0; // 0 = Ranks, 1 = Destinations
    private List<PassengerRanksResponse.Rank> ranksList = new ArrayList<>();
    private List<PassengerRankDestinationsResponse.Destination> destinationsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_dashboard);

        initializeViews();
        setupClickListeners();
        setupTabs();
        loadRanks();
    }

    private void initializeViews() {
        tabLayout = findViewById(R.id.tabLayout);
        etSearch = findViewById(R.id.etSearch);
        rvList = findViewById(R.id.rvList);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        btnLogout = findViewById(R.id.btnLogout);
        btnScanQr = findViewById(R.id.btnScanQr);
        btnActiveTrips = findViewById(R.id.btnActiveTrips);
        tvPassengerName = findViewById(R.id.tvPassengerName);
        tvPassengerPhone = findViewById(R.id.tvPassengerPhone);
        tvPassengerAddress = findViewById(R.id.tvPassengerAddress);
        tvTripCount = findViewById(R.id.tvTripCount);
        btnTripHistory = findViewById(R.id.btnTripHistory);

        authManager = new AuthManager(this);
        passengerApi = RetrofitClient.getInstance().getPassengerApi();

        rvList.setLayoutManager(new LinearLayoutManager(this));

        // Load passenger personal info and trip count
        loadPassengerInfo();
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> performLogout());
        btnScanQr.setOnClickListener(v -> openQrScanner());
        btnTripHistory.setOnClickListener(v -> openTripHistory());
        btnActiveTrips.setOnClickListener(v -> openActiveTrips());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadPassengerInfo() {
        int userId = authManager.getUserId();
        if (userId <= 0) {
            return;
        }
        passengerApi.getPassengerInfo(userId).enqueue(new retrofit2.Callback<PassengerInfoResponse>() {
            @Override
            public void onResponse(retrofit2.Call<PassengerInfoResponse> call, retrofit2.Response<PassengerInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getPassenger() != null) {
                    PassengerInfoResponse.Passenger p = response.body().getPassenger();
                    String fullName = (p.getFirstname() != null ? p.getFirstname() : "") +
                            (p.getLastname() != null ? (p.getFirstname() != null ? " " : "") + p.getLastname() : "");
                    tvPassengerName.setText(fullName.trim());
                    tvPassengerPhone.setText(p.getPhone() == null ? "" : p.getPhone());
                    tvPassengerAddress.setText(p.getAddress() == null ? "" : p.getAddress());
                    int count = response.body().getTrip_count();
                    tvTripCount.setText(count + (count == 1 ? " trip" : " trips"));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PassengerInfoResponse> call, Throwable t) { }
        });
    }

    private void openTripHistory() {
        Intent intent = new Intent(this, PassengerTripsActivity.class);
        intent.putExtra("filter_type", "history");
        intent.putExtra("user_id", authManager.getUserId());
        startActivity(intent);
    }

    private void openActiveTrips() {
        ActiveTripsFragment fragment = ActiveTripsFragment.newInstance();
        fragment.show(getSupportFragmentManager(), "active_trips");
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                etSearch.setText("");
                if (currentTab == 0) {
                    loadRanks();
                } else {
                    loadAllDestinations();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadRanks() {
        passengerApi.getRanks(null).enqueue(new retrofit2.Callback<PassengerRanksResponse>() {
            @Override
            public void onResponse(retrofit2.Call<PassengerRanksResponse> call, retrofit2.Response<PassengerRanksResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ranksList = response.body().getRanks();
                    updateRanksList();
                } else {
                    showEmptyState(true);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PassengerRanksResponse> call, Throwable t) {
                Toast.makeText(PassengerDashboardActivity.this, "Failed to load ranks", Toast.LENGTH_SHORT).show();
                showEmptyState(true);
            }
        });
    }

    private void loadAllDestinations() {
        // Load all destinations directly (no rank_id needed)
        passengerApi.getRankDestinations(null, null).enqueue(new retrofit2.Callback<PassengerRankDestinationsResponse>() {
            @Override
            public void onResponse(retrofit2.Call<PassengerRankDestinationsResponse> call, retrofit2.Response<PassengerRankDestinationsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    destinationsList = response.body().getDestinations();
                    updateDestinationsList();
                } else {
                    showEmptyState(true);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PassengerRankDestinationsResponse> call, Throwable t) {
                Toast.makeText(PassengerDashboardActivity.this, "Failed to load destinations", Toast.LENGTH_SHORT).show();
                showEmptyState(true);
            }
        });
    }

    private void performSearch(String query) {
        if (currentTab == 0) {
            List<PassengerRanksResponse.Rank> filtered = new ArrayList<>();
            for (PassengerRanksResponse.Rank r : ranksList) {
                if (r.getName().toLowerCase().contains(query.toLowerCase()) ||
                    (r.getCity() != null && r.getCity().toLowerCase().contains(query.toLowerCase())) ||
                    (r.getProvince() != null && r.getProvince().toLowerCase().contains(query.toLowerCase()))) {
                    filtered.add(r);
                }
            }
            rvList.setAdapter(new PassengerRanksAdapter(filtered, this::onRankClick));
            showEmptyState(filtered.isEmpty());
        } else {
            List<PassengerRankDestinationsResponse.Destination> filtered = new ArrayList<>();
            String queryLower = query.toLowerCase();
            for (PassengerRankDestinationsResponse.Destination d : destinationsList) {
                boolean matches = false;
                // Search origin rank name
                if (d.getOrigin_rank() != null && d.getOrigin_rank().getName() != null &&
                    d.getOrigin_rank().getName().toLowerCase().contains(queryLower)) {
                    matches = true;
                }
                // Search destination rank name
                if (!matches && d.getDestination_rank() != null && d.getDestination_rank().getName() != null &&
                    d.getDestination_rank().getName().toLowerCase().contains(queryLower)) {
                    matches = true;
                }
                // Search origin city/province
                if (!matches && d.getOrigin_rank() != null) {
                    if ((d.getOrigin_rank().getCity() != null && d.getOrigin_rank().getCity().toLowerCase().contains(queryLower)) ||
                        (d.getOrigin_rank().getProvince() != null && d.getOrigin_rank().getProvince().toLowerCase().contains(queryLower))) {
                        matches = true;
                    }
                }
                // Search destination city/province
                if (!matches && d.getDestination_rank() != null) {
                    if ((d.getDestination_rank().getCity() != null && d.getDestination_rank().getCity().toLowerCase().contains(queryLower)) ||
                        (d.getDestination_rank().getProvince() != null && d.getDestination_rank().getProvince().toLowerCase().contains(queryLower))) {
                        matches = true;
                    }
                }
                // Search fare or distance
                if (!matches && (String.valueOf(d.getFare()).contains(query) || String.valueOf(d.getDistance_km()).contains(query))) {
                    matches = true;
                }
                if (matches) {
                    filtered.add(d);
                }
            }
            rvList.setAdapter(new PassengerDestinationsAdapter(filtered, this::onDestinationClick));
            showEmptyState(filtered.isEmpty());
        }
    }

    private void updateRanksList() {
        rvList.setAdapter(new PassengerRanksAdapter(ranksList, this::onRankClick));
        showEmptyState(ranksList.isEmpty());
    }

    private void updateDestinationsList() {
        rvList.setAdapter(new PassengerDestinationsAdapter(destinationsList, this::onDestinationClick));
        showEmptyState(destinationsList.isEmpty());
    }

    private void onRankClick(PassengerRanksResponse.Rank rank) {
        Intent intent = new Intent(this, PassengerTripsActivity.class);
        intent.putExtra("filter_type", "rank");
        intent.putExtra("rank_id", rank.getId());
        startActivity(intent);
    }

    private void onDestinationClick(PassengerRankDestinationsResponse.Destination destination) {
        Intent intent = new Intent(this, PassengerTripsActivity.class);
        intent.putExtra("filter_type", "destination");
        intent.putExtra("rank_destination_id", destination.getId());
        startActivity(intent);
    }

    private void openQrScanner() {
        QrScannerFragment fragment = QrScannerFragment.newInstance();
        fragment.show(getSupportFragmentManager(), "qr_scanner");
    }

    private void showEmptyState(boolean show) {
        tvEmptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        rvList.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void performLogout() {
        authManager.clearAuthData();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
