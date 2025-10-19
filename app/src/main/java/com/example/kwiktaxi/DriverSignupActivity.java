package com.example.kwiktaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kwiktaxi.R;
import com.example.kwiktaxi.models.DriverSignupRequest;
import com.example.kwiktaxi.models.SignupResponse;
import com.example.kwiktaxi.network.ApiService;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.utils.AuthManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverSignupActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etLastName, etPhone, etPassword, etLicenseNumber;
    private MaterialButton btnRegister;
    private MaterialTextView tvBackToPassenger;
    private ProgressBar progressBar;
    private ApiService apiService;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signup);

        initializeViews();
        setupClickListeners();
        
        apiService = RetrofitClient.getInstance().getApiService();
        authManager = new AuthManager(this);
    }

    private void initializeViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etLicenseNumber = findViewById(R.id.etLicenseNumber);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToPassenger = findViewById(R.id.tvBackToPassenger);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> performDriverSignup());
        tvBackToPassenger.setOnClickListener(v -> navigateBackToPassengerSignup());
    }

    private void performDriverSignup() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String licenseNumber = etLicenseNumber.getText().toString().trim();

        if (validateInput(firstName, lastName, phone, password, licenseNumber)) {
            showProgress(true);
            
            DriverSignupRequest signupRequest = new DriverSignupRequest(
                firstName, lastName, phone, password, licenseNumber
            );
            
            Call<SignupResponse> call = apiService.signupDriver(signupRequest);
            
            call.enqueue(new Callback<SignupResponse>() {
                @Override
                public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                    showProgress(false);
                    
                    if (response.isSuccessful() && response.body() != null) {
                        SignupResponse signupResponse = response.body();
                        // Check if we have token and user data (backend doesn't send success field)
                        if (signupResponse.getToken() != null && !signupResponse.getToken().isEmpty() && 
                            signupResponse.getUser() != null) {
                            // Save auth data
                            String token = signupResponse.getToken();
                            String role = signupResponse.getUser().getRole();
                            int userId = signupResponse.getUser().getId();
                            String firstName = signupResponse.getUser().getFirstname();
                            String lastName = signupResponse.getUser().getLastname();
                            
                            String fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
                            
                            authManager.saveAuthData(token, role, userId, fullName.trim());
                            
                            Toast.makeText(DriverSignupActivity.this, "Driver registration successful!", Toast.LENGTH_SHORT).show();
                            
                            // Navigate to driver dashboard
                            Intent intent = new Intent(DriverSignupActivity.this, DriverDashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            String message = signupResponse.getMessage();
                            if (message == null || message.trim().isEmpty()) {
                                message = "Registration failed. Please try again.";
                            }
                            Toast.makeText(DriverSignupActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String errorMessage = "Registration failed. Please try again.";
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
                        Toast.makeText(DriverSignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SignupResponse> call, Throwable t) {
                    showProgress(false);
                    Toast.makeText(DriverSignupActivity.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInput(String firstName, String lastName, String phone, String password, String licenseNumber) {
        boolean isValid = true;

        if (firstName.isEmpty()) {
            etFirstName.setError("First name is required");
            isValid = false;
        }

        if (lastName.isEmpty()) {
            etLastName.setError("Last name is required");
            isValid = false;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Phone number is required");
            isValid = false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }

        if (licenseNumber.isEmpty()) {
            etLicenseNumber.setError("License number is required");
            isValid = false;
        }

        return isValid;
    }

    private void navigateBackToPassengerSignup() {
        Intent intent = new Intent(this, PassengerSignupActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
    }
}
