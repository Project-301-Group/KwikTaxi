package com.example.kwiktaxi.models;

public class PassengerSignupRequest {
    private String firstname;
    private String lastname;
    private String phone;
    private String password;
    private String address;
    private String nextOfKinName;
    private String nextOfKinRelationship;
    private String nextOfKinPhone;

    public PassengerSignupRequest() {}

    public PassengerSignupRequest(String firstname, String lastname, String phone, String password,
                                 String address, String nextOfKinName, String nextOfKinRelationship,
                                 String nextOfKinPhone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.nextOfKinName = nextOfKinName;
        this.nextOfKinRelationship = nextOfKinRelationship;
        this.nextOfKinPhone = nextOfKinPhone;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNextOfKinName() {
        return nextOfKinName;
    }

    public void setNextOfKinName(String nextOfKinName) {
        this.nextOfKinName = nextOfKinName;
    }

    public String getNextOfKinRelationship() {
        return nextOfKinRelationship;
    }

    public void setNextOfKinRelationship(String nextOfKinRelationship) {
        this.nextOfKinRelationship = nextOfKinRelationship;
    }

    public String getNextOfKinPhone() {
        return nextOfKinPhone;
    }

    public void setNextOfKinPhone(String nextOfKinPhone) {
        this.nextOfKinPhone = nextOfKinPhone;
    }
}
