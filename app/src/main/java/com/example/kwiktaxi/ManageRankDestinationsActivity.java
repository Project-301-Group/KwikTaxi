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
import com.example.kwiktaxi.models.RankDestinationResponse;
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
        authManager = new AuthManager(this);

        // Assume rank info passed through intent
        rankId = getIntent().getStringExtra("RANK_ID");
        rankName = getIntent().getStringExtra("RANK_NAME");
        rankNameText.setText(rankName);

        loadRankDestinations();

        addDestinationBtn.setOnClickListener(v -> openCreateFragment());
    }

    private void loadRankDestinations() {
        String token = authManager.getAuthToken();
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Authentication required. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Ensure token doesn't already have "Bearer " prefix
        String cleanToken = token.trim();
        if (cleanToken.startsWith("Bearer ")) {
            cleanToken = cleanToken.substring(7).trim();
        }
        String authHeader = "Bearer " + cleanToken;
        rankApi.getRankDestinations(authHeader).enqueue(new Callback<List<RankDestinationResponse>>() {
            @Override
            public void onResponse(Call<List<RankDestinationResponse>> call, Response<List<RankDestinationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new RankDestinationAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    String errorMsg = "No destinations found";
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
        CreateRankDestination fragment = CreateRankDestination.newInstance(rankId);
        FragmentManager fm = getSupportFragmentManager();
        fragment.show(fm, "create_rank_dest_fragment");
    }
}
