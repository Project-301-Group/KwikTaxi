package com.example.kwiktaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kwiktaxi.R;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.models.AdminDetailsResponse;
import com.example.kwiktaxi.models.RankDestinationResponse;
import com.example.kwiktaxi.network.ApiService;
import com.example.kwiktaxi.network.RankDestinationApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.kwiktaxi.utils.AuthManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageRankDestinationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RankDestinationAdapter adapter;
    private TextView rankNameText;
    private FloatingActionButton addDestinationBtn;
    private RankDestinationApi rankApi;
    private ApiService apiService;
    private String rankId;
    private String rankName;
    private AuthManager authManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_rank_destinations);

        rankNameText = findViewById(R.id.tvRankName);
        recyclerView = findViewById(R.id.rvRankDestinations);
        addDestinationBtn = findViewById(R.id.fabAddRoute);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rankApi = RetrofitClient.getInstance().getRankDestinationApi();
        apiService = RetrofitClient.getInstance().getApiService();
        authManager = new AuthManager(this);

        // Load admin details to show rank name
        loadAdminDetails();
        
        // Load rank destinations
        loadRankDestinations();

        addDestinationBtn.setOnClickListener(v -> openCreateFragment());
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
                    
                    // Update rank name display
                    String rankInfo = "Rank: " + admin.getRankName();
                    if (admin.getCity() != null && !admin.getCity().isEmpty()) {
                        rankInfo += " - " + admin.getCity();
                    }
                    if (admin.getProvince() != null && !admin.getProvince().isEmpty()) {
                        rankInfo += ", " + admin.getProvince();
                    }
                    rankNameText.setText(rankInfo);
                    
                    // Store rank info for later use
                    rankName = admin.getRankName();
                }
            }

            @Override
            public void onFailure(Call<AdminDetailsResponse> call, Throwable t) {
                // Show placeholder if API fails
                rankNameText.setText("Rank: Loading...");
            }
        });
    }

    private void loadRankDestinations() {
        int userId = authManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Authentication required. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }
        rankApi.getRankDestinations(userId).enqueue(new Callback<List<RankDestinationResponse>>() {
            @Override
            public void onResponse(Call<List<RankDestinationResponse>> call, Response<List<RankDestinationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new RankDestinationAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    String errorMsg = "No destinations found";
                    if (response.code() == 400) {
                        errorMsg = "Invalid request. Please check your input.";
                    } else if (response.code() == 404) {
                        errorMsg = "Admin rank not found.";
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
                    Toast.makeText(ManageRankDestinationsActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<RankDestinationResponse>> call, Throwable t) {
                Toast.makeText(ManageRankDestinationsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openCreateFragment() {
        // rankId might be null if not set yet, but CreateRankDestination will handle it
        CreateRankDestination fragment = CreateRankDestination.newInstance(rankId != null ? rankId : "");
        FragmentManager fm = getSupportFragmentManager();
        fragment.show(fm, "create_rank_dest_fragment");
    }
}
