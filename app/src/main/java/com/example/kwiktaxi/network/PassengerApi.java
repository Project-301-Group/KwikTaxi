package com.example.kwiktaxi.network;

import com.example.kwiktaxi.models.PassengerRanksResponse;
import com.example.kwiktaxi.models.PassengerTripsResponse;
import com.example.kwiktaxi.models.PassengerRankDestinationsResponse;
import com.example.kwiktaxi.models.PassengerJoinTripRequest;
import com.example.kwiktaxi.models.PassengerJoinTripResponse;
import com.example.kwiktaxi.models.PassengerInfoResponse;
import com.example.kwiktaxi.models.ActiveTripsPassengerCountResponse;
import com.example.kwiktaxi.models.TripPassengerCountResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PassengerApi {

    @GET("passenger/ranks")
    Call<PassengerRanksResponse> getRanks(@Query("search") String search);

    @GET("passenger/trips")
    Call<PassengerTripsResponse> getTrips(@Query("destination") String destination);

    @GET("passenger/rank/destinations")
    Call<PassengerRankDestinationsResponse> getRankDestinations(@Query("search") String search, @Query("rank_id") Integer rankId);

    @GET("passenger/trips/filter")
    Call<PassengerTripsResponse> getTripsByTaxi(@Query("rank_id") Integer rankId);

    @GET("passenger/trips/filter")
    Call<PassengerTripsResponse> getTripsByDestination(@Query("rank_destination_id") Integer destinationId);

    @POST("passenger/trip/join")
    Call<PassengerJoinTripResponse> joinTrip(@Body PassengerJoinTripRequest request);

    // 6 Get passenger info with trip count
    @GET("passenger/info")
    Call<PassengerInfoResponse> getPassengerInfo(@Query("user_id") int userId);

    // 7 Get passenger's trip list
    @GET("passenger/trips/target")
    Call<PassengerTripsResponse> getPassengerTrips(@Query("user_id") int userId);

    // 8 Get number of passengers for active trips
    @GET("passenger/active-trips/passenger-count")
    Call<ActiveTripsPassengerCountResponse> getActiveTripsPassengerCount();

    // 8 Get passenger count for a specific active trip
    @GET("passenger/active-trips/{trip_id}/passenger-count")
    Call<TripPassengerCountResponse> getTripPassengerCount(@Path("trip_id") int tripId);
}

