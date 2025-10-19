package com.example.kwiktaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kwiktaxi.R;
import com.example.kwiktaxi.models.LoginRequest;
import com.example.kwiktaxi.models.LoginResponse;
import com.example.kwiktaxi.network.ApiService;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.utils.AuthManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etPhone, etPassword;
    private MaterialButton btnLogin;
    private MaterialTextView tvSignUpLink;
    private ProgressBar progressBar;
    private ApiService apiService;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupClickListeners();
        
        apiService = RetrofitClient.getInstance().getApiService();
        authManager = new AuthManager(this);
    }

    private void initializeViews() {
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> performLogin());
        tvSignUpLink.setOnClickListener(v -> navigateToPassengerSignup());
    }

    private void performLogin() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (validateInput(phone, password)) {
            showProgress(true);
            
            LoginRequest loginRequest = new LoginRequest(phone, password);
            Call<LoginResponse> call = apiService.login(loginRequest);
            
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    showProgress(false);
                    
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        // Check if we have token and user data (backend doesn't send success field)
                        if (loginResponse.getToken() != null && !loginResponse.getToken().isEmpty() && 
                            loginResponse.getUser() != null) {
                            // Save auth data
                            String token = loginResponse.getToken();
                            String role = loginResponse.getUser().getRole();
                            int userId = loginResponse.getUser().getId();
                            String firstName = loginResponse.getUser().getFirstname();
                            String lastName = loginResponse.getUser().getLastname();
                            
                            String fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
                            
                            authManager.saveAuthData(token, role, userId, fullName.trim());
                            
                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            
                            // Navigate to appropriate dashboard based on role
                            navigateToDashboard(role);
                        } else {
                            String message = loginResponse.getMessage();
                            if (message == null || message.trim().isEmpty()) {
                                message = "Login failed. Please try again.";
                            }
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String errorMessage = "Login failed. Please try again.";
                        if (response.errorBody() != null) {
                            try {
                                String errorBody = response.errorBody().string();
                                if (errorBody != null && !errorBody.trim().isEmpty()) {
                                    errorMessage = errorBody;
                                }
                            } catch (Exception e) {
                                // Keep default error message
                            }
                        }
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInput(String phone, String password) {
        if (phone.isEmpty()) {
            etPhone.setError("Phone number is required");
            return false;
        }
        
        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            return false;
        }
        
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }
        
        return true;
    }

    private void navigateToPassengerSignup() {
        Intent intent = new Intent(this, PassengerSignupActivity.class);
        startActivity(intent);
    }

    private void navigateToDashboard(String role) {
        Intent intent;
        switch (role.toLowerCase()) {
            case "passenger":
                intent = new Intent(this, PassengerDashboardActivity.class);
                break;
            case "driver":
                intent = new Intent(this, DriverDashboardActivity.class);
                break;
            case "admin":
                intent = new Intent(this, AdminDashboardActivity.class);
                break;
            default:
                Toast.makeText(this, "Unknown user role", Toast.LENGTH_SHORT).show();
                return;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
    }
}
