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
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RankDestinationApi {

    // 1️⃣ Get all ranks except admin's own
    @GET("ranks/others")
    Call<List<RankResponse>> getOtherRanks(@Query("user_id") int userId);

    // 2️⃣ Get all destinations for the admin's rank
    @GET("rank_destinations")
    Call<List<RankDestinationResponse>> getRankDestinations(@Query("user_id") int userId);

    // 3️⃣ Create new rank destination
    @POST("rank_destinations")
    Call<RankDestinationResponse> createRankDestination(@Body RankDestinationRequest request);

    // 4️⃣ Toggle activation
    @PUT("rank_destinations/{id}/toggle")
    Call<ToggleResponse> toggleRankDestination(
            @Path("id") int id,
            @Body RankDestinationRequest request
    );

    // 5️⃣ Delete destination
    @DELETE("rank_destinations/{id}")
    Call<RankDestinationResponse> deleteRankDestination(
            @Path("id") int id,
            @Query("user_id") int userId
    );
}
