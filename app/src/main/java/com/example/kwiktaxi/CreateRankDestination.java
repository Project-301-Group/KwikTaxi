package com.example.kwiktaxi;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.RankDestinationResponse;
import com.example.kwiktaxi.network.RankDestinationApi;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.models.RankDestinationRequest;
import com.example.kwiktaxi.models.RankResponse;
import com.example.kwiktaxi.utils.AuthManager;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRankDestination extends DialogFragment implements RankSelectionAdapter.OnRankSelectedListener {

    private static final String ARG_RANK_ID = "rank_id";

    private RecyclerView rvRanks;
    private EditText etDistance, etDuration, etFare;
    private Button createBtn;
    private RankDestinationApi rankApi;
    private String rankId;
    private List<RankResponse> rankList = new ArrayList<>();
    private String selectedRankId = null; // will hold the rank selected from RecyclerView
    private AuthManager authManager;

    public static CreateRankDestination newInstance(String rankId) {
        CreateRankDestination fragment = new CreateRankDestination();
        Bundle args = new Bundle();
        args.putString(ARG_RANK_ID, rankId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_create_rank_destination, null);

        rvRanks = view.findViewById(R.id.rvAvailableRanks);
        etDistance = view.findViewById(R.id.etDistance);
        etDuration = view.findViewById(R.id.etDuration);
        etFare = view.findViewById(R.id.etFare);
        createBtn = view.findViewById(R.id.createRankDestBtn);

        rvRanks.setLayoutManager(new LinearLayoutManager(requireContext()));
        rankApi = RetrofitClient.getInstance().getRankDestinationApi();
        authManager = new AuthManager(requireContext());

        if (getArguments() != null) {
            rankId = getArguments().getString(ARG_RANK_ID);
        }

        loadAvailableRanks();

        createBtn.setOnClickListener(v -> createRankDestination());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view);
        return builder.create();
    }

    private void loadAvailableRanks() {
        String token = authManager.getAuthToken();
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(getContext(), "Authentication required. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Ensure token doesn't already have "Bearer " prefix
        String cleanToken = token.trim();
        if (cleanToken.startsWith("Bearer ")) {
            cleanToken = cleanToken.substring(7).trim();
        }
        String authHeader = "Bearer " + cleanToken;
        rankApi.getOtherRanks(authHeader).enqueue(new Callback<List<RankResponse>>() {
            @Override
            public void onResponse(Call<List<RankResponse>> call, Response<List<RankResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rankList = response.body();
                    // Remove current admin rank from selection list
                    final String currentRankId = rankId;
                    rankList.removeIf(r -> currentRankId.equals(r.getId()));

                    RankSelectionAdapter adapter = new RankSelectionAdapter(rankList, CreateRankDestination.this);
                    rvRanks.setAdapter(adapter);
                } else {
                    String errorMsg = "Could not load ranks";
                    if (response.code() == 401) {
                        errorMsg = "Authentication failed. Please login again.";
                    } else if (response.code() == 422) {
                        errorMsg = "Invalid token format. Please login again.";
                    } else if (response.errorBody() != null) {
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
            public void onFailure(Call<List<RankResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createRankDestination() {
        if (selectedRankId == null) {
            Toast.makeText(getContext(), "Please select a destination rank", Toast.LENGTH_SHORT).show();
            return;
        }

        String distance = etDistance.getText().toString();
        String duration = etDuration.getText().toString();
        String fare = etFare.getText().toString();

        if (distance.isEmpty() || duration.isEmpty() || fare.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all route details", Toast.LENGTH_SHORT).show();
            return;
        }

        RankDestinationRequest request = new RankDestinationRequest(selectedRankId,
                Double.parseDouble(distance), Integer.parseInt(duration), Double.parseDouble(fare));

        String token = authManager.getAuthToken();
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(getContext(), "Authentication required. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Ensure token doesn't already have "Bearer " prefix
        String cleanToken = token.trim();
        if (cleanToken.startsWith("Bearer ")) {
            cleanToken = cleanToken.substring(7).trim();
        }
        String authHeader = "Bearer " + cleanToken;
        rankApi.createRankDestination(authHeader, request).enqueue(new Callback<RankDestinationResponse>() {

            @Override
            public void onResponse(Call<RankDestinationResponse> call, Response<RankDestinationResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Rank destination added!", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Failed to add destination", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RankDestinationResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRankSelected(String rankId) {
        this.selectedRankId = rankId; // store the clicked rank ID
    }
}
