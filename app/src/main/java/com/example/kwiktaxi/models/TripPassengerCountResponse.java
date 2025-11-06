package com.example.kwiktaxi.models;

public class TripPassengerCountResponse {
    private int trip_id;
    private int passenger_count;
    private Taxi taxi;
    private Driver driver;

    public int getTrip_id() { return trip_id; }
    public int getPassenger_count() { return passenger_count; }
    public Taxi getTaxi() { return taxi; }
    public Driver getDriver() { return driver; }

    public static class Taxi {
        private int id;
        private String registration_number;
        private int capacity;

        public int getId() { return id; }
        public String getRegistration_number() { return registration_number; }
        public int getCapacity() { return capacity; }
    }

    public static class Driver {
        private String name;
        private String phone;

        public String getName() { return name; }
        public String getPhone() { return phone; }
    }
}

