package com.example.kwiktaxi.models;

public class RankResponse {
    private int id;
    private String name;
    private String city;
    private String province;
    private double latitude;
    private double longitude;

    // Default constructor for Gson
    public RankResponse() {}

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getIdAsString() { return String.valueOf(id); }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
