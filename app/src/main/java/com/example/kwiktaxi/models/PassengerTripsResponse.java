package com.example.kwiktaxi.models;

import java.util.List;

public class PassengerTripsResponse {
    private List<Trip> trips;

    public List<Trip> getTrips() { return trips; }

    public static class Trip {
        private int trip_id;
        private String status;
        private RankDestination rank_destination;
        private Taxi taxi;
        private Driver driver;

        public int getTrip_id() { return trip_id; }
        public String getStatus() { return status; }
        public RankDestination getRank_destination() { return rank_destination; }
        public Taxi getTaxi() { return taxi; }
        public Driver getDriver() { return driver; }
    }

    public static class RankDestination {
        private int id;
        private String destination_name;
        private double distance_km;
        private int estimated_duration;
        private double fare;

        public int getId() { return id; }
        public String getDestination_name() { return destination_name; }
        public double getDistance_km() { return distance_km; }
        public int getEstimated_duration() { return estimated_duration; }
        public double getFare() { return fare; }
    }

    public static class Taxi {
        private Integer id;
        private String registration_number;

        public Integer getId() { return id; }
        public String getRegistration_number() { return registration_number; }
    }

    public static class Driver {
        private Integer id;
        private String name;
        private String phone_number;

        public Integer getId() { return id; }
        public String getName() { return name; }
        public String getPhone_number() { return phone_number; }
    }
}

