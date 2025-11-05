package com.example.kwiktaxi.models;

import java.util.List;

public class TripPassengersResponse {
    private int trip_id;
    private List<Passenger> passengers;

    public int getTrip_id() { return trip_id; }
    public List<Passenger> getPassengers() { return passengers; }

    public static class Passenger {
        private int id;
        private String firstname;
        private String lastname;
        private String phone;
        private String address;

        public int getId() { return id; }
        public String getFirstname() { return firstname; }
        public String getLastname() { return lastname; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
    }
}


