package com.example.kwiktaxi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.kwiktaxi.models.TripPassengerCountResponse;
import com.example.kwiktaxi.network.PassengerApi;
import com.example.kwiktaxi.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class TripPassengerCountFragment extends DialogFragment {

    private static final String ARG_TRIP_ID = "trip_id";

    private MaterialTextView tvTripId, tvRegistration, tvCapacity, tvDriver, tvDriverPhone, tvPassengerCount, tvFullBadge;
    private MaterialButton btnClose;
    private PassengerApi passengerApi;
    private int tripId;

    public static TripPassengerCountFragment newInstance(int tripId) {
        TripPassengerCountFragment fragment = new TripPassengerCountFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRIP_ID, tripId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
        passengerApi = RetrofitClient.getInstance().getPassengerApi();
        if (getArguments() != null) {
            tripId = getArguments().getInt(ARG_TRIP_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_passenger_count, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTripId = view.findViewById(R.id.tvTripId);
        tvRegistration = view.findViewById(R.id.tvRegistration);
        tvCapacity = view.findViewById(R.id.tvCapacity);
        tvDriver = view.findViewById(R.id.tvDriver);
        tvDriverPhone = view.findViewById(R.id.tvDriverPhone);
        tvPassengerCount = view.findViewById(R.id.tvPassengerCount);
        tvFullBadge = view.findViewById(R.id.tvFullBadge);
        btnClose = view.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(v -> dismiss());

        loadTripPassengerCount();
    }

    private void loadTripPassengerCount() {
        passengerApi.getTripPassengerCount(tripId).enqueue(new retrofit2.Callback<TripPassengerCountResponse>() {
            @Override
            public void onResponse(retrofit2.Call<TripPassengerCountResponse> call, retrofit2.Response<TripPassengerCountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TripPassengerCountResponse data = response.body();
                    
                    // Trip ID
                    tvTripId.setText("Trip ID: " + data.getTrip_id());
                    
                    // Vehicle info
                    if (data.getTaxi() != null) {
                        tvRegistration.setText("Taxi: " + data.getTaxi().getRegistration_number());
                        tvCapacity.setText("Capacity: " + data.getTaxi().getCapacity());
                    } else {
                        tvRegistration.setText("Taxi: N/A");
                        tvCapacity.setText("Capacity: N/A");
                    }
                    
                    // Driver info
                    if (data.getDriver() != null) {
                        String driverName = data.getDriver().getName() != null ? data.getDriver().getName() : "Unknown";
                        tvDriver.setText("Driver: " + driverName);
                        if (data.getDriver().getPhone() != null) {
                            tvDriverPhone.setText("Phone: " + data.getDriver().getPhone());
                        } else {
                            tvDriverPhone.setText("Phone: N/A");
                        }
                    } else {
                        tvDriver.setText("Driver: N/A");
                        tvDriverPhone.setText("Phone: N/A");
                    }
                    
                    // Passenger count vs capacity
                    int passengerCount = data.getPassenger_count();
                    int capacity = data.getTaxi() != null ? data.getTaxi().getCapacity() : 0;
                    String passengerInfo = "Passengers: " + passengerCount + " / " + capacity;
                    tvPassengerCount.setText(passengerInfo);
                    
                    // Show "FULL" if passenger_count == capacity
                    if (capacity > 0 && passengerCount >= capacity) {
                        tvFullBadge.setVisibility(View.VISIBLE);
                        tvFullBadge.setText("FULL");
                    } else {
                        tvFullBadge.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load trip information", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TripPassengerCountResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load trip information", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}

