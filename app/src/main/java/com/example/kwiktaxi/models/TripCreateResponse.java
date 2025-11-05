package com.example.kwiktaxi.models;

public class TripCreateResponse {
    private int trip_id;
    private int taxi_id;
    private String qr_code; // base64-encoded PNG

    public int getTrip_id() { return trip_id; }
    public int getTaxi_id() { return taxi_id; }
    public String getQr_code() { return qr_code; }

    // Nested type to model the trips/count response
    public static class TripCount {
        private int taxi_id;
        private int trip_count;

        public int getTaxi_id() { return taxi_id; }
        public int getTrip_count() { return trip_count; }
    }
}


