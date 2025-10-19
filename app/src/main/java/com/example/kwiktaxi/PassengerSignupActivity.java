package com.example.kwiktaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kwiktaxi.R;
import com.example.kwiktaxi.models.PassengerSignupRequest;
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

public class PassengerSignupActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etLastName, etPhone, etPassword, etAddress;
    private TextInputEditText etNextOfKinName, etNextOfKinRelationship, etNextOfKinPhone;
    private MaterialButton btnRegister;
    private MaterialTextView tvDriverSignUpLink;
    private ProgressBar progressBar;
    private ApiService apiService;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_signup);

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
        etAddress = findViewById(R.id.etAddress);
        etNextOfKinName = findViewById(R.id.etNextOfKinName);
        etNextOfKinRelationship = findViewById(R.id.etNextOfKinRelationship);
        etNextOfKinPhone = findViewById(R.id.etNextOfKinPhone);
        btnRegister = findViewById(R.id.btnRegister);
        tvDriverSignUpLink = findViewById(R.id.tvDriverSignUpLink);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> performPassengerSignup());
        tvDriverSignUpLink.setOnClickListener(v -> navigateToDriverSignup());
    }

    private void performPassengerSignup() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String nextOfKinName = etNextOfKinName.getText().toString().trim();
        String nextOfKinRelationship = etNextOfKinRelationship.getText().toString().trim();
        String nextOfKinPhone = etNextOfKinPhone.getText().toString().trim();

        if (validateInput(firstName, lastName, phone, password, address, nextOfKinName, nextOfKinRelationship, nextOfKinPhone)) {
            showProgress(true);
            
            PassengerSignupRequest signupRequest = new PassengerSignupRequest(
                firstName, lastName, phone, password, address,
                nextOfKinName, nextOfKinRelationship, nextOfKinPhone
            );
            
            Call<SignupResponse> call = apiService.signupPassenger(signupRequest);
            
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
                            
                            Toast.makeText(PassengerSignupActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            
                            // Navigate to passenger dashboard
                            Intent intent = new Intent(PassengerSignupActivity.this, PassengerDashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            String message = signupResponse.getMessage();
                            if (message == null || message.trim().isEmpty()) {
                                message = "Registration failed. Please try again.";
                            }
                            Toast.makeText(PassengerSignupActivity.this, message, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PassengerSignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SignupResponse> call, Throwable t) {
                    showProgress(false);
                    Toast.makeText(PassengerSignupActivity.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInput(String firstName, String lastName, String phone, String password,
                                String address, String nextOfKinName, String nextOfKinRelationship, String nextOfKinPhone) {
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

        if (address.isEmpty()) {
            etAddress.setError("Address is required");
            isValid = false;
        }

        if (nextOfKinName.isEmpty()) {
            etNextOfKinName.setError("Next of kin name is required");
            isValid = false;
        }

        if (nextOfKinRelationship.isEmpty()) {
            etNextOfKinRelationship.setError("Relationship is required");
            isValid = false;
        }

        if (nextOfKinPhone.isEmpty()) {
            etNextOfKinPhone.setError("Next of kin phone is required");
            isValid = false;
        }

        return isValid;
    }

    private void navigateToDriverSignup() {
        Intent intent = new Intent(this, DriverSignupActivity.class);
        startActivity(intent);
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
    }
}
