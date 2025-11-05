package com.example.kwiktaxi.models;

import java.util.List;

public class PassengerRankDestinationsResponse {
    private List<Destination> destinations;

    public List<Destination> getDestinations() { return destinations; }

    public static class Destination {
        private int id;
        private Integer origin_rank_id;
        private String origin_rank_name;
        private String destination_rank_id;
        private String destination_name;
        private double distance_km;
        private int estimated_duration;
        private double fare;
        private boolean active;

        public int getId() { return id; }
        public Integer getOrigin_rank_id() { return origin_rank_id; }
        public String getOrigin_rank_name() { return origin_rank_name; }
        public String getDestination_rank_id() { return destination_rank_id; }
        public String getDestination_name() { return destination_name; }
        public double getDistance_km() { return distance_km; }
        public int getEstimated_duration() { return estimated_duration; }
        public double getFare() { return fare; }
        public boolean isActive() { return active; }
    }
}

