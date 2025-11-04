package com.example.kwiktaxi.models;

public class TaxiRequest {
    private String registration_number;
    private int capacity;
    private String status;
    private Integer user_id;
    private Integer driver_id;

    public TaxiRequest(String registration_number, int capacity, String status, Integer user_id, Integer driver_id) {
        this.registration_number = registration_number;
        this.capacity = capacity;
        this.status = status;
        this.user_id = user_id;
        this.driver_id = driver_id;
    }

    public String getRegistrationNumber() {
        return registration_number;
    }

    public void setRegistrationNumber(String registration_number) {
        this.registration_number = registration_number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getDriverId() {
        return driver_id;
    }

    public void setDriverId(Integer driver_id) {
        this.driver_id = driver_id;
    }
}

