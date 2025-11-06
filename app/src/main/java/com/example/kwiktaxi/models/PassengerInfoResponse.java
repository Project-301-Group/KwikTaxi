package com.example.kwiktaxi.models;

public class PassengerInfoResponse {
    private Passenger passenger;
    private int trip_count;

    public Passenger getPassenger() { return passenger; }
    public int getTrip_count() { return trip_count; }

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


