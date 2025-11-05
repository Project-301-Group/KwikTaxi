package com.example.kwiktaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kwiktaxi.models.DriverTaxiInfoResponse;
import com.example.kwiktaxi.models.TripCreateRequest;
import com.example.kwiktaxi.models.TripCreateResponse;
import com.example.kwiktaxi.network.DriverApi;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.utils.AuthManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class TripCreateActivity extends AppCompatActivity {

    private Spinner spinnerDestinations;
    private MaterialButton btnCreate;
    private int selectedDestinationId = -1;
    private DriverApi driverApi;
    private AuthManager authManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_create);

        driverApi = RetrofitClient.getInstance().getDriverApi();
        authManager = new AuthManager(this);

        spinnerDestinations = findViewById(R.id.spinnerDestinations);
        btnCreate = findViewById(R.id.btnCreateTripConfirm);

        loadDestinations();

        spinnerDestinations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object tag = view.getTag();
                if (tag instanceof Integer) {
                    selectedDestinationId = (Integer) tag;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnCreate.setOnClickListener(v -> doCreateTrip());
    }

    private void loadDestinations() {
        int userId = authManager.getUserId();
        driverApi.getDriverTaxi(userId).enqueue(new retrofit2.Callback<com.example.kwiktaxi.models.DriverTaxiInfoResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DriverTaxiInfoResponse> call, retrofit2.Response<DriverTaxiInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getTaxi() != null) {
                    List<String> labels = new ArrayList<>();
                    List<Integer> ids = new ArrayList<>();
                    for (DriverTaxiInfoResponse.RankDestination rd : response.body().getTaxi().getRank_destinations()) {
                        if (rd.isActive()) {
                            labels.add(rd.getDestination_name() + " • R" + rd.getFare());
                            ids.add(rd.getId());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TripCreateActivity.this, android.R.layout.simple_spinner_item, labels) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            view.setTag(ids.get(position));
                            return view;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            view.setTag(ids.get(position));
                            return view;
                        }
                    };
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDestinations.setAdapter(adapter);
                    if (!ids.isEmpty()) {
                        selectedDestinationId = ids.get(0);
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<DriverTaxiInfoResponse> call, Throwable t) {
                Toast.makeText(TripCreateActivity.this, "Failed to load destinations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doCreateTrip() {
        if (selectedDestinationId <= 0) {
            Toast.makeText(this, "Select a destination", Toast.LENGTH_SHORT).show();
            return;
        }
        int userId = authManager.getUserId();
        TripCreateRequest request = new TripCreateRequest(userId, selectedDestinationId);
        driverApi.createTrip(request).enqueue(new retrofit2.Callback<TripCreateResponse>() {
            @Override
            public void onResponse(retrofit2.Call<TripCreateResponse> call, retrofit2.Response<TripCreateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(TripCreateActivity.this, TripQrActivity.class);
                    intent.putExtra("qr_base64", response.body().getQr_code());
                    intent.putExtra("taxi_id", response.body().getTaxi_id());
                    intent.putExtra("trip_id", response.body().getTrip_id());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(TripCreateActivity.this, "Failed to create trip", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TripCreateResponse> call, Throwable t) {
                Toast.makeText(TripCreateActivity.this, "Failed to create trip", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


