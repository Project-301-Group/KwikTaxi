package com.example.kwiktaxi.network;

import com.example.kwiktaxi.models.PassengerRanksResponse;
import com.example.kwiktaxi.models.PassengerTripsResponse;
import com.example.kwiktaxi.models.PassengerRankDestinationsResponse;
import com.example.kwiktaxi.models.PassengerJoinTripRequest;
import com.example.kwiktaxi.models.PassengerJoinTripResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PassengerApi {

    @GET("passenger/ranks")
    Call<PassengerRanksResponse> getRanks(@Query("search") String search);

    @GET("passenger/trips")
    Call<PassengerTripsResponse> getTrips(@Query("destination") String destination);

    @GET("passenger/rank/destinations")
    Call<PassengerRankDestinationsResponse> getRankDestinations(@Query("search") String search, @Query("rank_id") Integer rankId);

    @GET("passenger/trips/filter")
    Call<PassengerTripsResponse> getFilteredTrips(@Query("taxi_id") Integer taxiId, @Query("rank_destination_id") Integer rankDestinationId);

    @POST("passenger/trip/join")
    Call<PassengerJoinTripResponse> joinTrip(@Body PassengerJoinTripRequest request);
}

