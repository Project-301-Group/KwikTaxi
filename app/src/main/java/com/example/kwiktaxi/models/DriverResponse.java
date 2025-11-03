package com.example.kwiktaxi.models;

public class DriverResponse {
    private int id;
    private String firstname;
    private String lastname;
    private String phone;
    private String license_number;
    private boolean has_taxi;
    private TaxiInfo taxi;

    public DriverResponse() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicenseNumber() {
        return license_number;
    }

    public void setLicenseNumber(String license_number) {
        this.license_number = license_number;
    }

    public boolean isHasTaxi() {
        return has_taxi;
    }

    public void setHasTaxi(boolean has_taxi) {
        this.has_taxi = has_taxi;
    }

    public TaxiInfo getTaxi() {
        return taxi;
    }

    public void setTaxi(TaxiInfo taxi) {
        this.taxi = taxi;
    }

    public String getFullName() {
        String firstName = firstname != null ? firstname : "";
        String lastName = lastname != null ? lastname : "";
        return (firstName + " " + lastName).trim();
    }

    public static class TaxiInfo {
        private int id;
        private String registration_number;
        private String status;
        private int rank_id;

        public TaxiInfo() {}

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRegistrationNumber() {
            return registration_number;
        }

        public void setRegistrationNumber(String registration_number) {
            this.registration_number = registration_number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getRankId() {
            return rank_id;
        }

        public void setRankId(int rank_id) {
            this.rank_id = rank_id;
        }
    }
}

