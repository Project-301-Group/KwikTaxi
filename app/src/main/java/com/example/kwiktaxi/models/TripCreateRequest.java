package com.example.kwiktaxi.models;

public class TripCreateRequest {
    private int user_id;
    private int rank_destination_id;

    public TripCreateRequest(int user_id, int rank_destination_id) {
        this.user_id = user_id;
        this.rank_destination_id = rank_destination_id;
    }

    public int getUser_id() { return user_id; }
    public int getRank_destination_id() { return rank_destination_id; }
}

package com.example.kwiktaxi.models;

public class TripCreateRequest {
    private int user_id;
    private int rank_destination_id;

    public TripCreateRequest(int user_id, int rank_destination_id) {
        this.user_id = user_id;
        this.rank_destination_id = rank_destination_id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public int getRankDestinationId() {
        return rank_destination_id;
    }

    public void setRankDestinationId(int rank_destination_id) {
        this.rank_destination_id = rank_destination_id;
    }
}


