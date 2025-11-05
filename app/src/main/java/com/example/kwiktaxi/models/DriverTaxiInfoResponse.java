package com.example.kwiktaxi.models;

import java.util.List;

public class DriverTaxiInfoResponse {
    private Taxi taxi;
    private Driver driver;

    public Taxi getTaxi() {
        return taxi;
    }

    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
    }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public static class Taxi {
        private int id;
        private String registration_number;
        private int capacity;
        private String status;
        private Rank rank;
        private List<RankDestination> rank_destinations;

        public int getId() { return id; }
        public String getRegistration_number() { return registration_number; }
        public int getCapacity() { return capacity; }
        public String getStatus() { return status; }
        public Rank getRank() { return rank; }
        public List<RankDestination> getRank_destinations() { return rank_destinations; }
    }

    public static class Rank {
        private int id;
        private String name;
        private String address;
        private String city;
        private String province;

        public int getId() { return id; }
        public String getName() { return name; }
        public String getAddress() { return address; }
        public String getCity() { return city; }
        public String getProvince() { return province; }
    }

    public static class RankDestination {
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

    public static class Driver {
        private int id;
        private String firstname;
        private String lastname;
        private String phone;

        public int getId() { return id; }
        public String getFirstname() { return firstname; }
        public String getLastname() { return lastname; }
        public String getPhone() { return phone; }
        public String getFullName() {
            String fn = firstname == null ? "" : firstname;
            String ln = lastname == null ? "" : lastname;
            return (fn + " " + ln).trim();
        }
    }
}


