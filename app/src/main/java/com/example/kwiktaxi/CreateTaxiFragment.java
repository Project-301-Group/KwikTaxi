package com.example.kwiktaxi;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kwiktaxi.models.AdminDetailsResponse;
import com.example.kwiktaxi.models.DriverResponse;
import com.example.kwiktaxi.models.RankDestinationResponse;
import com.example.kwiktaxi.models.TaxiRequest;
import com.example.kwiktaxi.models.TaxiResponse;
import com.example.kwiktaxi.network.ApiService;
import com.example.kwiktaxi.network.RankDestinationApi;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.network.TaxiApi;
import com.example.kwiktaxi.utils.AuthManager;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTaxiFragment extends DialogFragment
        implements RankSelectionAdapter.OnRankSelectedListener,
                   DriverSelectionAdapter.OnDriverSelectedListener {

    private TextView tvSelectedDestination;
    private TextView tvSelectedDriver;
    private EditText etRegistrationNumber, etCapacity, etStatus;
    private Button createBtn;
    private TaxiApi taxiApi;
    private RankDestinationApi rankApi;
    private ApiService apiService;
    private AuthManager authManager;

    private Integer selectedDestinationId = null;
    private Integer selectedDriverId = null;
    private Integer rankId = null;
    private RankDestinationResponse selectedDestination = null;
    private DriverResponse selectedDriver = null;

    private List<RankDestinationResponse> destinationList = new ArrayList<>();
    private List<DriverResponse> driverList = new ArrayList<>();
    private List<DriverResponse> filteredDriverList = new ArrayList<>();

    public static CreateTaxiFragment newInstance() {
        return new CreateTaxiFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_create_taxi, null);

        tvSelectedDestination = view.findViewById(R.id.tvSelectedDestination);
        tvSelectedDriver = view.findViewById(R.id.tvSelectedDriver);
        etRegistrationNumber = view.findViewById(R.id.etRegistrationNumber);
        etCapacity = view.findViewById(R.id.etCapacity);
        etStatus = view.findViewById(R.id.etStatus);
        createBtn = view.findViewById(R.id.createTaxiBtn);

        taxiApi = RetrofitClient.getInstance().getTaxiApi();
        rankApi = RetrofitClient.getInstance().getRankDestinationApi();
        apiService = RetrofitClient.getInstance().getApiService();
        authManager = new AuthManager(requireContext());

        // Load rank ID first
        loadRankId();

        // Setup click listeners for selections
        tvSelectedDestination.setOnClickListener(v -> showDestinationSelectionDialog());
        tvSelectedDriver.setOnClickListener(v -> showDriverSelectionDialog());

        createBtn.setOnClickListener(v -> createTaxi());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view);
        return builder.create();
    }

    private void loadRankId() {
        int userId = authManager.getUserId();
        if (userId == -1) {
            Toast.makeText(getContext(), "Authentication required", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getAdminDetails(userId).enqueue(new Callback<AdminDetailsResponse>() {
            @Override
            public void onResponse(Call<AdminDetailsResponse> call, Response<AdminDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getAdmin() != null) {
                    // Rank ID will be set when creating taxi - we'll get it from admin details
                    // For now, just load destinations
                    loadDestinations();
                    loadDrivers("");
                }
            }

            @Override
            public void onFailure(Call<AdminDetailsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load rank info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDestinations() {
        int userId = authManager.getUserId();
        if (userId == -1) return;

        rankApi.getRankDestinations(userId).enqueue(new Callback<List<RankDestinationResponse>>() {
            @Override
            public void onResponse(Call<List<RankDestinationResponse>> call, Response<List<RankDestinationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    destinationList = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<RankDestinationResponse>> call, Throwable t) {
                // Silently fail
            }
        });
    }

    private void loadDrivers(String search) {
        taxiApi.getDrivers(search.isEmpty() ? null : search).enqueue(new Callback<List<DriverResponse>>() {
            @Override
            public void onResponse(Call<List<DriverResponse>> call, Response<List<DriverResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    driverList = response.body();
                    filteredDriverList = new ArrayList<>(driverList);

                    // Update adapter if dialog is showing
                    if (driverAdapter != null && driverDialog != null && driverDialog.isShowing()) {
                        driverAdapter = new DriverSelectionAdapter(filteredDriverList, driverId -> {
                            onDriverSelected(driverId);
                            if (driverDialog != null) {
                                driverDialog.dismiss();
                            }
                        });
                        RecyclerView rvDrivers = driverDialog.findViewById(R.id.rvDrivers);
                        if (rvDrivers != null) {
                            rvDrivers.setAdapter(driverAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DriverResponse>> call, Throwable t) {
                // Silently fail
            }
        });
    }

    private AlertDialog destinationDialog;

    private void showDestinationSelectionDialog() {
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_create_rank_destination, null);
        RecyclerView rvDestinations = dialogView.findViewById(R.id.rvAvailableRanks);
        rvDestinations.setLayoutManager(new LinearLayoutManager(getContext()));

        RankDestinationSelectionAdapter adapter = new RankDestinationSelectionAdapter(destinationList, rankId -> {
            onRankSelected(rankId);
            if (destinationDialog != null) {
                destinationDialog.dismiss();
            }
        });
        rvDestinations.setAdapter(adapter);

        destinationDialog = new AlertDialog.Builder(requireActivity())
                .setTitle("Select Destination")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .create();
        destinationDialog.show();
    }

    private AlertDialog driverDialog;
    private DriverSelectionAdapter driverAdapter;

    private void showDriverSelectionDialog() {
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_driver, null);
        EditText etSearch = dialogView.findViewById(R.id.etSearchDriver);
        RecyclerView rvDrivers = dialogView.findViewById(R.id.rvDrivers);
        rvDrivers.setLayoutManager(new LinearLayoutManager(getContext()));

        driverAdapter = new DriverSelectionAdapter(filteredDriverList, driverId -> {
            onDriverSelected(driverId);
            if (driverDialog != null) {
                driverDialog.dismiss();
            }
        });
        rvDrivers.setAdapter(driverAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().trim();
                loadDrivers(search);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        driverDialog = new AlertDialog.Builder(requireActivity())
                .setTitle("Select Driver")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .create();
        driverDialog.show();
    }

    @Override
    public void onRankSelected(String destinationIdStr) {
        // This is for destination selection - find the destination by ID
        for (RankDestinationResponse dest : destinationList) {
            if (String.valueOf(dest.getId()).equals(destinationIdStr)) {
                selectedDestination = dest;
                selectedDestinationId = dest.getId();
                tvSelectedDestination.setText("Selected: " + dest.getDestinationName());
                tvSelectedDestination.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    @Override
    public void onDriverSelected(int driverId) {
        for (DriverResponse driver : driverList) {
            if (driver.getId() == driverId) {
                selectedDriver = driver;
                selectedDriverId = driverId;
                tvSelectedDriver.setText("Selected: " + driver.getFullName() + " (" + driver.getLicenseNumber() + ")");
                tvSelectedDriver.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void createTaxi() {
        if (selectedDestinationId == null) {
            Toast.makeText(getContext(), "Please select a destination", Toast.LENGTH_SHORT).show();
            return;
        }

        String registrationNumber = etRegistrationNumber.getText().toString().trim();
        if (registrationNumber.isEmpty()) {
            Toast.makeText(getContext(), "Registration number is required", Toast.LENGTH_SHORT).show();
            return;
        }

        int capacity = 4; // default
        try {
            String capacityStr = etCapacity.getText().toString().trim();
            if (!capacityStr.isEmpty()) {
                capacity = Integer.parseInt(capacityStr);
            }
        } catch (NumberFormatException e) {
            // Keep default
        }

        String status = "available"; // default
        String statusStr = etStatus.getText().toString().trim();
        if (!statusStr.isEmpty()) {
            status = statusStr;
        }

        int userId = authManager.getUserId();
        if (userId == -1) {
            Toast.makeText(getContext(), "Authentication required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get rank_id from admin details
        int finalCapacity = capacity;
        String finalStatus = status;
        apiService.getAdminDetails(userId).enqueue(new Callback<AdminDetailsResponse>() {
            @Override
            public void onResponse(Call<AdminDetailsResponse> call, Response<AdminDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getAdmin() != null) {
                    // Create taxi request
                    // rank_id is optional - backend will infer it from admin's user_id
                    TaxiRequest request = new TaxiRequest(
                            registrationNumber,
                            finalCapacity,
                            finalStatus,
                            null, // rank_id will be auto-assigned by backend from admin
                            selectedDriverId
                    );

                    taxiApi.createTaxi(request).enqueue(new Callback<TaxiResponse>() {
                        @Override
                        public void onResponse(Call<TaxiResponse> call, Response<TaxiResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Taxi created successfully!", Toast.LENGTH_SHORT).show();
                                dismiss();
                            } else {
                                String errorMsg = "Failed to create taxi";
                                if (response.errorBody() != null) {
                                    try {
                                        String errorBody = response.errorBody().string();
                                        if (errorBody != null && !errorBody.trim().isEmpty()) {
                                            errorMsg = "Error: " + errorBody;
                                        }
                                    } catch (Exception e) {
                                        errorMsg = "Error code: " + response.code();
                                    }
                                }
                                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<TaxiResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AdminDetailsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get rank info", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

