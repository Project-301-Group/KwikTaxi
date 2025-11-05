package com.example.kwiktaxi.models;

public class PassengerJoinTripRequest {
    private int user_id;
    private String registration_number;

    public PassengerJoinTripRequest(int user_id, String registration_number) {
        this.user_id = user_id;
        this.registration_number = registration_number;
    }

    public int getUser_id() { return user_id; }
    public String getRegistration_number() { return registration_number; }
}

