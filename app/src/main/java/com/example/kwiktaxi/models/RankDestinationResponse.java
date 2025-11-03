package com.example.kwiktaxi.models;

public class RankDestinationResponse {
    private int id;
    private String destination_name;
    private String city;
    private String province;
    private double distance_km;
    private int estimated_duration;
    private double fare;
    private boolean active;
    private String created_at;
    private String message;
    private String error;

    public int getId() { return id; }
    public String getDestinationName() { return destination_name; }
    public String getCity() { return city; }
    public String getProvince() { return province; }
    public double getDistanceKm() { return distance_km; }
    public int getEstimatedDuration() { return estimated_duration; }
    public double getFare() { return fare; }
    public boolean isActive() { return active; }
    public String getCreatedAt() { return created_at; }
    public String getMessage() { return message; }
    public String getError() { return error; }
}
