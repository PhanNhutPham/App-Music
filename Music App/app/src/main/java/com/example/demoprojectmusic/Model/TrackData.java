package com.example.demoprojectmusic.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackData {
    //N
    @SerializedName("data")
    private List<Track> data;

    public TrackData(List<Track> tracks) {
    }

    public TrackData() {
    }

    public List<Track> getData() {
        return data;
    }

    public void setData(List<Track> data) {
        this.data = data;
    }
}

