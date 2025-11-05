package com.example.kwiktaxi.network;

import com.example.kwiktaxi.models.DriverTaxiInfoResponse;
import com.example.kwiktaxi.models.TripCreateRequest;
import com.example.kwiktaxi.models.TripCreateResponse;
import com.example.kwiktaxi.models.TripPassengersResponse;
import com.example.kwiktaxi.models.TripScanRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DriverApi {

    @GET("driver/taxi")
    Call<DriverTaxiInfoResponse> getDriverTaxi(@Query("user_id") int userId);

    @GET("driver/taxi/trips/count")
    Call<TripCreateResponse.TripCount> getDriverTripCount(@Query("user_id") int userId);

    @POST("driver/trip")
    Call<TripCreateResponse> createTrip(@Body TripCreateRequest request);

    @POST("trip/scan")
    Call<Void> scanQr(@Body TripScanRequest request);

    @GET("driver/taxi/passengers")
    Call<TripPassengersResponse> getActiveTripPassengers(@Query("taxi_id") int taxiId);
}

