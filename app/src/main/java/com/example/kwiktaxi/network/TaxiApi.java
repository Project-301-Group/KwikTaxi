package com.example.kwiktaxi.network;

import com.example.kwiktaxi.models.DriverResponse;
import com.example.kwiktaxi.models.TaxiRequest;
import com.example.kwiktaxi.models.TaxiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TaxiApi {

    // 1️⃣ Get all taxis for the admin's rank
    @GET("taxis")
    Call<List<TaxiResponse>> getTaxisByRank(@Query("user_id") int userId);

    // 2️⃣ Create new taxi
    @POST("taxis")
    Call<TaxiResponse> createTaxi(@Body TaxiRequest request);

    // 3️⃣ Get all drivers with optional search
    @GET("drivers")
    Call<List<DriverResponse>> getDrivers(@Query("search") String search);
}

