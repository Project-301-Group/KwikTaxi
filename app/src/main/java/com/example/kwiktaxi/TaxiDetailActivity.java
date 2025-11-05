package com.example.kwiktaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.DriverTaxiInfoResponse;
import com.example.kwiktaxi.models.TripPassengersResponse;
import com.example.kwiktaxi.network.DriverApi;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.utils.AuthManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.appbar.MaterialToolbar;

public class TaxiDetailActivity extends AppCompatActivity {

    private MaterialTextView tvHeader, tvRank, tvCapacity, tvStatus;
    private RecyclerView rvDestinations, rvPassengers, rvActiveTrips;
    private MaterialButton btnCreateTrip, btnRefreshPassengers, btnRefreshActiveTrips;
    private int taxiId;
    private DriverApi driverApi;
    private AuthManager authManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_detail);

        taxiId = getIntent().getIntExtra("taxi_id", -1);
        if (taxiId == -1) {
            Toast.makeText(this, "Missing taxi id", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        driverApi = RetrofitClient.getInstance().getDriverApi();
        authManager = new AuthManager(this);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        if (topAppBar != null) {
            topAppBar.setNavigationOnClickListener(v -> finish());
        }

        tvHeader = findViewById(R.id.tvHeader);
        tvRank = findViewById(R.id.tvRank);
        tvCapacity = findViewById(R.id.tvCapacity);
        tvStatus = findViewById(R.id.tvStatus);
        rvDestinations = findViewById(R.id.rvDestinations);
        rvPassengers = findViewById(R.id.rvPassengers);
        rvActiveTrips = findViewById(R.id.rvActiveTrips);
        btnCreateTrip = findViewById(R.id.btnCreateTrip);
        btnRefreshPassengers = findViewById(R.id.btnRefreshPassengers);
        btnRefreshActiveTrips = findViewById(R.id.btnRefreshActiveTrips);

        rvDestinations.setLayoutManager(new LinearLayoutManager(this));
        rvPassengers.setLayoutManager(new LinearLayoutManager(this));
        rvActiveTrips.setLayoutManager(new LinearLayoutManager(this));

        loadTaxi();

        btnCreateTrip.setOnClickListener(v -> openCreateTrip());
        btnRefreshPassengers.setOnClickListener(v -> loadPassengers());
        btnRefreshActiveTrips.setOnClickListener(v -> loadActiveTrips());
    }

    private void loadTaxi() {
        int userId = authManager.getUserId();
        if (userId == -1) return;
        driverApi.getDriverTaxi(userId).enqueue(new retrofit2.Callback<DriverTaxiInfoResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DriverTaxiInfoResponse> call, retrofit2.Response<DriverTaxiInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getTaxi() != null) {
                    DriverTaxiInfoResponse.Taxi taxi = response.body().getTaxi();
                    if (taxi.getId() != taxiId) {
                        // If multiple taxis ever appear, here we'd select by id. For now, trust this is the same taxi.
                    }
                    tvHeader.setText(taxi.getRegistration_number());
                    if (taxi.getRank() != null) {
                        tvRank.setText(taxi.getRank().getName());
                    }
                    tvCapacity.setText(String.valueOf(taxi.getCapacity()));
                    tvStatus.setText(taxi.getStatus());

                    rvDestinations.setAdapter(new RankDestinationSimpleAdapter(taxi.getRank_destinations()));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<DriverTaxiInfoResponse> call, Throwable t) {
                Toast.makeText(TaxiDetailActivity.this, "Failed to load taxi details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPassengers() {
        driverApi.getActiveTripPassengers(taxiId).enqueue(new retrofit2.Callback<TripPassengersResponse>() {
            @Override
            public void onResponse(retrofit2.Call<TripPassengersResponse> call, retrofit2.Response<TripPassengersResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rvPassengers.setAdapter(new TripPassengersAdapter(response.body().getPassengers()));
                } else {
                    Toast.makeText(TaxiDetailActivity.this, "No active trip found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TripPassengersResponse> call, Throwable t) {
                Toast.makeText(TaxiDetailActivity.this, "Failed to load passengers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadActiveTrips() {
        Integer userId = new AuthManager(this).getUserId();
        driverApi.getActiveTrips(userId).enqueue(new retrofit2.Callback<com.example.kwiktaxi.models.ActiveTripsResponse>() {
            @Override
            public void onResponse(retrofit2.Call<com.example.kwiktaxi.models.ActiveTripsResponse> call, retrofit2.Response<com.example.kwiktaxi.models.ActiveTripsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rvActiveTrips.setAdapter(new ActiveTripsAdapter(response.body().getActive_trips(), () -> fetchAndShowQr()));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.example.kwiktaxi.models.ActiveTripsResponse> call, Throwable t) {
                Toast.makeText(TaxiDetailActivity.this, "Failed to load active trips", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAndShowQr() {
        int userId = authManager.getUserId();
        driverApi.getActiveTripQr(userId).enqueue(new retrofit2.Callback<com.example.kwiktaxi.models.TripQrResponse>() {
            @Override
            public void onResponse(retrofit2.Call<com.example.kwiktaxi.models.TripQrResponse> call, retrofit2.Response<com.example.kwiktaxi.models.TripQrResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(TaxiDetailActivity.this, TripQrActivity.class);
                    intent.putExtra("qr_base64", response.body().getQr_code());
                    intent.putExtra("taxi_id", response.body().getTaxi_id());
                    intent.putExtra("trip_id", response.body().getTrip_id());
                    startActivity(intent);
                } else {
                    Toast.makeText(TaxiDetailActivity.this, "No active trip found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.example.kwiktaxi.models.TripQrResponse> call, Throwable t) {
                Toast.makeText(TaxiDetailActivity.this, "Failed to retrieve QR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openCreateTrip() {
        Intent intent = new Intent(this, TripCreateActivity.class);
        intent.putExtra("taxi_id", taxiId);
        startActivity(intent);
    }
}


