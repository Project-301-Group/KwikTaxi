package com.example.kwiktaxi.models;

import java.util.List;

public class PassengerRankDestinationsResponse {
    private Rank rank;
    private List<Destination> destinations;

    public Rank getRank() { return rank; }
    public List<Destination> getDestinations() { return destinations; }

    public static class Rank {
        private int id;
        private String name;
        private String city;
        private String province;

        public int getId() { return id; }
        public String getName() { return name; }
        public String getCity() { return city; }
        public String getProvince() { return province; }
    }

    public static class Destination {
        private int id;
        private String destination_rank_id;
        private String destination_name;
        private double distance_km;
        private int estimated_duration;
        private double fare;
        private boolean active;

        public int getId() { return id; }
        public String getDestination_rank_id() { return destination_rank_id; }
        public String getDestination_name() { return destination_name; }
        public double getDistance_km() { return distance_km; }
        public int getEstimated_duration() { return estimated_duration; }
        public double getFare() { return fare; }
        public boolean isActive() { return active; }
    }
}

