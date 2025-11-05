package com.example.kwiktaxi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.kwiktaxi.models.PassengerJoinTripRequest;
import com.example.kwiktaxi.models.PassengerJoinTripResponse;
import com.example.kwiktaxi.network.PassengerApi;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.utils.AuthManager;

public class QrScannerFragment extends DialogFragment {

    private EditText etRegistrationNumber;
    private Button btnJoin, btnCancel;
    private PassengerApi passengerApi;
    private AuthManager authManager;

    public static QrScannerFragment newInstance() {
        return new QrScannerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Dialog);
        passengerApi = RetrofitClient.getInstance().getPassengerApi();
        authManager = new AuthManager(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);
        
        etRegistrationNumber = view.findViewById(R.id.etRegistrationNumber);
        btnJoin = view.findViewById(R.id.btnJoin);
        btnCancel = view.findViewById(R.id.btnCancel);

        btnJoin.setOnClickListener(v -> joinTrip());
        btnCancel.setOnClickListener(v -> dismiss());

        return view;
    }

    private void joinTrip() {
        String registrationNumber = etRegistrationNumber.getText().toString().trim();
        if (registrationNumber.isEmpty()) {
            Toast.makeText(getContext(), "Please enter registration number", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = authManager.getUserId();
        if (userId == -1) {
            Toast.makeText(getContext(), "Authentication required", Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        PassengerJoinTripRequest request = new PassengerJoinTripRequest(userId, registrationNumber);
        passengerApi.joinTrip(request).enqueue(new retrofit2.Callback<PassengerJoinTripResponse>() {
            @Override
            public void onResponse(retrofit2.Call<PassengerJoinTripResponse> call, retrofit2.Response<PassengerJoinTripResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    String errorMsg = "Failed to join trip";
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            if (errorBody != null && !errorBody.trim().isEmpty()) {
                                errorMsg = errorBody;
                            }
                        } catch (Exception e) {
                            errorMsg = "Error code: " + response.code();
                        }
                    }
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PassengerJoinTripResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
