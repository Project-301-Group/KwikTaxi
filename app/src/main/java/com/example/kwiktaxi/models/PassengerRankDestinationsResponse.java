package com.example.kwiktaxi.models;

import java.util.List;

public class PassengerRankDestinationsResponse {
    private List<Destination> destinations;

    public List<Destination> getDestinations() { return destinations; }

    public static class Destination {
        private int id;
        private Rank origin_rank;
        private Rank destination_rank;
        private double distance_km;
        private int estimated_duration;
        private double fare;
        private boolean active;

        public int getId() { return id; }
        public Rank getOrigin_rank() { return origin_rank; }
        public Rank getDestination_rank() { return destination_rank; }
        public double getDistance_km() { return distance_km; }
        public int getEstimated_duration() { return estimated_duration; }
        public double getFare() { return fare; }
        public boolean isActive() { return active; }
    }

    public static class Rank {
        private Integer id;
        private String name;
        private String city;
        private String province;

        public Integer getId() { return id; }
        public String getName() { return name; }
        public String getCity() { return city; }
        public String getProvince() { return province; }
    }
}

