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

package com.example.kwiktaxi.network;

import com.example.kwiktaxi.models.DriverTaxiInfoResponse;
import com.example.kwiktaxi.models.TripCountResponse;
import com.example.kwiktaxi.models.TripCreateResponse;
import com.example.kwiktaxi.models.TripPassengersResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DriverApi {

    // 1️⃣ Get taxi info (including RankDestinations) for logged-in driver
    @GET("driver/taxi")
    Call<DriverTaxiInfoResponse> getDriverTaxiInfo(@Query("user_id") int userId);

    // 2️⃣ Get total number of trips for the taxi
    @GET("driver/taxi/trips/count")
    Call<TripCountResponse> getDriverTripCount(@Query("user_id") int userId);

    // 3️⃣ Create a new trip and generate QR code
    @POST("driver/trip")
    Call<TripCreateResponse> createTrip(@Body TripCreateRequest request);

    // 4️⃣ Get passengers of active trip for a taxi
    @GET("driver/taxi/passengers")
    Call<TripPassengersResponse> getActiveTripPassengers(@Query("taxi_id") int taxiId);
}


