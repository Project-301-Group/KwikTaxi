package com.example.kwiktaxi.network;

import com.example.kwiktaxi.models.DriverSignupRequest;
import com.example.kwiktaxi.models.LoginRequest;
import com.example.kwiktaxi.models.LoginResponse;
import com.example.kwiktaxi.models.PassengerSignupRequest;
import com.example.kwiktaxi.models.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    
    @POST("auth/signup/passenger")
    Call<SignupResponse> signupPassenger(@Body PassengerSignupRequest signupRequest);
    
    @POST("auth/signup/driver")
    Call<SignupResponse> signupDriver(@Body DriverSignupRequest signupRequest);
}
