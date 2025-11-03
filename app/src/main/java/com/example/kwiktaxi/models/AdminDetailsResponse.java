package com.example.kwiktaxi.models;

public class AdminDetailsResponse {
    private AdminInfo admin;
    private String error;

    public AdminInfo getAdmin() {
        return admin;
    }

    public void setAdmin(AdminInfo admin) {
        this.admin = admin;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static class AdminInfo {
        private String firstname;
        private String lastname;
        private String phone;
        private String rank_name;
        private String province;
        private String city;

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

        public String getRankName() {
            return rank_name;
        }

        public void setRankName(String rank_name) {
            this.rank_name = rank_name;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getFullName() {
            String firstName = firstname != null ? firstname : "";
            String lastName = lastname != null ? lastname : "";
            return (firstName + " " + lastName).trim();
        }
    }
}

