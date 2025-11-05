package com.example.kwiktaxi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.kwiktaxi.models.PassengerJoinTripRequest;
import com.example.kwiktaxi.models.PassengerJoinTripResponse;
import com.example.kwiktaxi.network.PassengerApi;
import com.example.kwiktaxi.network.RetrofitClient;
import com.example.kwiktaxi.utils.AuthManager;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QrScannerFragment extends DialogFragment {

    private PreviewView previewView;
    private Button btnClose;
    private PassengerApi passengerApi;
    private AuthManager authManager;
    private ExecutorService cameraExecutor;
    private Camera camera;
    private boolean isScanning = true;

    public static QrScannerFragment newInstance() {
        return new QrScannerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        passengerApi = RetrofitClient.getInstance().getPassengerApi();
        authManager = new AuthManager(requireContext());
        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public android.view.View onCreateView(@NonNull android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        previewView = view.findViewById(R.id.previewView);
        btnClose = view.findViewById(R.id.btnClose);
        
        btnClose.setOnClickListener(v -> dismiss());

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            Toast.makeText(getContext(), "Camera permission required", Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
                
                imageAnalysis.setAnalyzer(cameraExecutor, imageProxy -> {
                    if (!isScanning) {
                        imageProxy.close();
                        return;
                    }
                    
                    InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());
                    
                    com.google.mlkit.vision.barcode.BarcodeScanner scanner = BarcodeScanning.getClient();
                    scanner.process(image)
                            .addOnSuccessListener(barcodes -> {
                                if (!isScanning) return;
                                for (Barcode barcode : barcodes) {
                                    String rawValue = barcode.getRawValue();
                                    if (rawValue != null && !rawValue.isEmpty()) {
                                        isScanning = false;
                                        joinTrip(rawValue);
                                        imageProxy.close();
                                        return;
                                    }
                                }
                                imageProxy.close();
                            })
                            .addOnFailureListener(e -> {
                                imageProxy.close();
                            });
                });
                
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                
                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
                
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(getContext(), "Failed to start camera", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void joinTrip(String registrationNumber) {
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
                    isScanning = true; // Resume scanning on error
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PassengerJoinTripResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                isScanning = true; // Resume scanning on error
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
        isScanning = false;
    }
}
