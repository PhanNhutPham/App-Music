package com.example.demoprojectmusic.Model;

import com.google.gson.annotations.SerializedName;

public class Playlist {
    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("duration")
    private int duration;

    @SerializedName("nb_tracks")
    private int numberOfTracks;

    @SerializedName("picture_xl")
    private String pictureExtraLarge;

    @SerializedName("creation_date")
    private String creationDate;

    @SerializedName("type")
    private String type;

    @SerializedName("tracks")
    private TrackData trackData;

    public Playlist() {
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", numberOfTracks=" + numberOfTracks +
                ", pictureExtraLarge='" + pictureExtraLarge + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", type='" + type + '\'' +
                ", tracks=" + trackData +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public String getPictureExtraLarge() {
        return pictureExtraLarge;
    }

    public void setPictureExtraLarge(String pictureExtraLarge) {
        this.pictureExtraLarge = pictureExtraLarge;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TrackData getTrackData() {
        return trackData;
    }

    public void setTrackData(TrackData trackData) {
        this.trackData = trackData;
    }
}
