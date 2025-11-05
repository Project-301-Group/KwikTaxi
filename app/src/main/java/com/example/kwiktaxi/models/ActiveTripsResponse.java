package com.example.kwiktaxi.models;

import java.util.List;

public class ActiveTripsResponse {
    private List<ActiveTrip> active_trips;

    public List<ActiveTrip> getActive_trips() { return active_trips; }

    public static class ActiveTrip {
        private int trip_id;
        private String status;
        private Taxi taxi;
        private RankDestination rank_destination;

        public int getTrip_id() { return trip_id; }
        public String getStatus() { return status; }
        public Taxi getTaxi() { return taxi; }
        public RankDestination getRank_destination() { return rank_destination; }
    }

    public static class Taxi {
        private int id;
        private String registration_number;
        private int capacity;
        private String status;

        public int getId() { return id; }
        public String getRegistration_number() { return registration_number; }
        public int getCapacity() { return capacity; }
        public String getStatus() { return status; }
    }

    public static class RankDestination {
        private int id;
        private String destination_name;
        private double fare;
        private double distance_km;
        private int estimated_duration;

        public int getId() { return id; }
        public String getDestination_name() { return destination_name; }
        public double getFare() { return fare; }
        public double getDistance_km() { return distance_km; }
        public int getEstimated_duration() { return estimated_duration; }
    }
}


