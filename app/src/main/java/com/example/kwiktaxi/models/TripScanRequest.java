package com.example.kwiktaxi.models;

public class TripScanRequest {
    private String qr_code;
    private Passenger passenger;

    public TripScanRequest(String qr_code, Passenger passenger) {
        this.qr_code = qr_code;
        this.passen ger = passenger;
    }

    public static class Passenger {
        private String firstname;
        private String lastname;
        private String phone;
        private String address;

        public Passenger(String firstname, String lastname, String phone, String address) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.phone = phone;
            this.address = address;
        }
    }
}


