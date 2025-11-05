package com.example.kwiktaxi;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kwiktaxi.models.AdminDetailsResponse;
import com.example.kwiktaxi.models.TaxiResponse;
import com.example.kwiktaxi.network.ApiService;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.network.TaxiApi;
import com.example.kwiktaxi.utils.AuthManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageTaxisActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaxiAdapter adapter;
    private TextView rankNameText;
    private TextView tvEmptyTaxis;
    private FloatingActionButton addTaxiBtn;
    private TaxiApi taxiApi;
    private ApiService apiService;
    private String rankId;
    private String rankName;
    private AuthManager authManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_taxis);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        if (topAppBar != null) {
            topAppBar.setNavigationOnClickListener(v -> finish());
        }

        rankNameText = findViewById(R.id.tvRankName);
        tvEmptyTaxis = findViewById(R.id.tvEmptyTaxis);
        recyclerView = findViewById(R.id.rvTaxis);
        addTaxiBtn = findViewById(R.id.fabAddTaxi);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taxiApi = RetrofitClient.getInstance().getTaxiApi();
        apiService = RetrofitClient.getInstance().getApiService();
        authManager = new AuthManager(this);

        // Load admin details to show rank name
        loadAdminDetails();
        
        // Load taxis
        loadTaxis();

        addTaxiBtn.setOnClickListener(v -> openCreateFragment());
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
                rankNameText.setText("Rank: Loading...");
            }
        });
    }

    public void loadTaxis() {
        int userId = authManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Authentication required. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }
        taxiApi.getTaxisByRank(userId).enqueue(new Callback<List<TaxiResponse>>() {
            @Override
            public void onResponse(Call<List<TaxiResponse>> call, Response<List<TaxiResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TaxiResponse> taxis = response.body();
                    adapter = new TaxiAdapter(taxis);
                    recyclerView.setAdapter(adapter);
                    if (taxis.isEmpty()) {
                        tvEmptyTaxis.setVisibility(View.VISIBLE);
                    } else {
                        tvEmptyTaxis.setVisibility(View.GONE);
                    }
                } else {
                    String errorMsg = "No taxis found";
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
                    Toast.makeText(ManageTaxisActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    tvEmptyTaxis.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<TaxiResponse>> call, Throwable t) {
                Toast.makeText(ManageTaxisActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openCreateFragment() {
        CreateTaxiFragment fragment = CreateTaxiFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fragment.show(fm, "create_taxi_fragment");
        
        // Refresh list when fragment is dismissed
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (fragment.isRemoving() || !fragment.isAdded()) {
                loadTaxis(); // Reload taxis when fragment closes
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh taxis when returning to activity
        loadTaxis();
    }
}