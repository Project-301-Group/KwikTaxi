package com.example.kwiktaxi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kwiktaxi.utils.AuthManager;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        authManager = new AuthManager(this);
        
        // Delay navigation to show splash screen
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            checkAuthAndNavigate();
        }, SPLASH_DELAY);
    }

    private void checkAuthAndNavigate() {
        if (authManager.isLoggedIn() && authManager.isTokenValid()) {
            // User is logged in, navigate to appropriate dashboard
            String userRole = authManager.getUserRole();
            navigateToDashboard(userRole);
        } else {
            // User is not logged in or token is invalid, go to login
            navigateToLogin();
        }
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
                // Unknown role, go to login
                navigateToLogin();
                return;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}