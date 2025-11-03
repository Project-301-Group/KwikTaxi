package com.example.kwiktaxi.models;

public class RankResponse {
    private int id;
    private String name;
    private String city;
    private String province;
    private double latitude;
    private double longitude;

    // Getters and setters
    public String getId() { return String.valueOf(id); }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getProvince() { return province; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
