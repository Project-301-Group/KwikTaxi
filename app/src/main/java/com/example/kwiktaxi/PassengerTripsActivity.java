package com.example.kwiktaxi;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.PassengerTripsResponse;
import com.example.kwiktaxi.network.PassengerApi;
import com.example.kwiktaxi.network.RetrofitClient;
import com.google.android.material.textview.MaterialTextView;

public class PassengerTripsActivity extends AppCompatActivity {

    private RecyclerView rvTrips;
    private MaterialTextView tvEmptyState;
    private PassengerApi passengerApi;
    private String filterType;
    private Integer filterId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_trips);

        filterType = getIntent().getStringExtra("filter_type");
        if ("rank".equals(filterType)) {
            filterId = getIntent().getIntExtra("rank_id", -1);
        } else if ("destination".equals(filterType)) {
            filterId = getIntent().getIntExtra("rank_destination_id", -1);
        }

        rvTrips = findViewById(R.id.rvTrips);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        passengerApi = RetrofitClient.getInstance().getPassengerApi();

        rvTrips.setLayoutManager(new LinearLayoutManager(this));

        loadTrips();
    }

    private void loadTrips() {
        if ("destination".equals(filterType)) {
            passengerApi.getFilteredTrips(null, filterId).enqueue(new retrofit2.Callback<PassengerTripsResponse>() {
                @Override
                public void onResponse(retrofit2.Call<PassengerTripsResponse> call, retrofit2.Response<PassengerTripsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        rvTrips.setAdapter(new PassengerTripsAdapter(response.body().getTrips()));
                        showEmptyState(response.body().getTrips().isEmpty());
                    } else {
                        showEmptyState(true);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<PassengerTripsResponse> call, Throwable t) {
                    Toast.makeText(PassengerTripsActivity.this, "Failed to load trips", Toast.LENGTH_SHORT).show();
                    showEmptyState(true);
                }
            });
        } else {
            // For rank, load destinations first then trips
            passengerApi.getRankDestinations(filterId).enqueue(new retrofit2.Callback<com.example.kwiktaxi.models.PassengerRankDestinationsResponse>() {
                @Override
                public void onResponse(retrofit2.Call<com.example.kwiktaxi.models.PassengerRankDestinationsResponse> call, retrofit2.Response<com.example.kwiktaxi.models.PassengerRankDestinationsResponse> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().getDestinations().isEmpty()) {
                        int destId = response.body().getDestinations().get(0).getId();
                        passengerApi.getFilteredTrips(null, destId).enqueue(new retrofit2.Callback<PassengerTripsResponse>() {
                            @Override
                            public void onResponse(retrofit2.Call<PassengerTripsResponse> call, retrofit2.Response<PassengerTripsResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    rvTrips.setAdapter(new PassengerTripsAdapter(response.body().getTrips()));
                                    showEmptyState(response.body().getTrips().isEmpty());
                                } else {
                                    showEmptyState(true);
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<PassengerTripsResponse> call, Throwable t) {
                                showEmptyState(true);
                            }
                        });
                    } else {
                        showEmptyState(true);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<com.example.kwiktaxi.models.PassengerRankDestinationsResponse> call, Throwable t) {
                    showEmptyState(true);
                }
            });
        }
    }

    private void showEmptyState(boolean show) {
        tvEmptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        rvTrips.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}

