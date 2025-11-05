package com.example.kwiktaxi.models;

import java.util.List;

public class PassengerRanksResponse {
    private List<Rank> ranks;

    public List<Rank> getRanks() { return ranks; }

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
}

