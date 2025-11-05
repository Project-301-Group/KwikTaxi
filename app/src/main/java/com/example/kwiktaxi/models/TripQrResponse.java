package com.example.kwiktaxi.models;

public class TripQrResponse {
    private int trip_id;
    private int taxi_id;
    private String qr_code;

    public int getTrip_id() { return trip_id; }
    public int getTaxi_id() { return taxi_id; }
    public String getQr_code() { return qr_code; }
}


