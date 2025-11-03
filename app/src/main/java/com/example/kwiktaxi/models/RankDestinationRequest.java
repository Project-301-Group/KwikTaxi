package com.example.kwiktaxi.models;

public class RankDestinationRequest {
    private int user_id;
    private String destination_rank_id;
    private double distance_km;
    private int estimated_duration;
    private double fare;

    // Constructor for creating rank destination
    public RankDestinationRequest(int user_id, String destination_rank_id, double distance_km, int estimated_duration, double fare) {
        this.user_id = user_id;
        this.destination_rank_id = destination_rank_id;
        this.distance_km = distance_km;
        this.estimated_duration = estimated_duration;
        this.fare = fare;
    }

    // Constructor for toggle (only needs user_id)
    public RankDestinationRequest(int user_id) {
        this.user_id = user_id;
    }
}
