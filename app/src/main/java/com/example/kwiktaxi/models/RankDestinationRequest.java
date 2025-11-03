package com.example.kwiktaxi.models;

public class RankDestinationRequest {
    private String destination_rank_id;
    private double distance_km;
    private int estimated_duration;
    private double fare;

    public RankDestinationRequest(String destination_rank_id, double distance_km, int estimated_duration, double fare) {
        this.destination_rank_id = destination_rank_id;
        this.distance_km = distance_km;
        this.estimated_duration = estimated_duration;
        this.fare = fare;
    }
}
