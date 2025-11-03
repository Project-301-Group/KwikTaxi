package com.example.kwiktaxi.network;

import com.example.kwiktaxi.models.RankResponse;
import com.example.kwiktaxi.models.RankDestinationRequest;
import com.example.kwiktaxi.models.RankDestinationResponse;
import com.example.kwiktaxi.models.ToggleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RankDestinationApi {

    // 1️⃣ Get all ranks except admin's own
    @GET("ranks/others")
    Call<List<RankResponse>> getOtherRanks(@Header("Authorization") String token);

    // 2️⃣ Get all destinations for the admin's rank (from JWT)
    @GET("rank_destinations")
    Call<List<RankDestinationResponse>> getRankDestinations(
            @Header("Authorization") String token
    );

    // 3️⃣ Create new rank destination
    @POST("rank_destinations")
    Call<RankDestinationResponse> createRankDestination(
            @Header("Authorization") String token,
            @Body RankDestinationRequest request
    );

    // 4️⃣ Toggle activation
    @PUT("rank_destinations/{id}/toggle")
    Call<ToggleResponse> toggleRankDestination(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    // 5️⃣ Delete destination
    @DELETE("rank_destinations/{id}")
    Call<RankDestinationResponse> deleteRankDestination(
            @Header("Authorization") String token,
            @Path("id") int id
    );
}
