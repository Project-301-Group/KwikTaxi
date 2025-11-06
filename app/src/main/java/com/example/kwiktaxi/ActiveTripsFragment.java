package com.example.kwiktaxi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.ActiveTripsPassengerCountResponse;
import com.example.kwiktaxi.network.PassengerApi;
import com.example.kwiktaxi.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class ActiveTripsFragment extends DialogFragment {

    private RecyclerView rvActiveTrips;
    private MaterialTextView tvEmptyState;
    private MaterialButton btnClose;
    private PassengerApi passengerApi;

    public static ActiveTripsFragment newInstance() {
        return new ActiveTripsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
        passengerApi = RetrofitClient.getInstance().getPassengerApi();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_active_trips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvActiveTrips = view.findViewById(R.id.rvActiveTrips);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        btnClose = view.findViewById(R.id.btnClose);

        rvActiveTrips.setLayoutManager(new LinearLayoutManager(getContext()));

        btnClose.setOnClickListener(v -> dismiss());

        loadActiveTrips();
    }

    private void loadActiveTrips() {
        passengerApi.getActiveTripsPassengerCount().enqueue(new retrofit2.Callback<ActiveTripsPassengerCountResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ActiveTripsPassengerCountResponse> call, retrofit2.Response<ActiveTripsPassengerCountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getTrips() != null && !response.body().getTrips().isEmpty()) {
                        rvActiveTrips.setAdapter(new ActiveTripsPassengerCountAdapter(response.body().getTrips()));
                        showEmptyState(false);
                    } else {
                        showEmptyState(true);
                    }
                } else {
                    showEmptyState(true);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ActiveTripsPassengerCountResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load active trips", Toast.LENGTH_SHORT).show();
                showEmptyState(true);
            }
        });
    }

    private void showEmptyState(boolean show) {
        tvEmptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        rvActiveTrips.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}

