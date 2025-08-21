package com.example.demoprojectmusic.Model;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.annotations.SerializedName;

public class Artist {

    //N
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("picture_xl")
    private String pictureXL;

    @SerializedName("nb_album")
    private int numberOfAlbums;

    @SerializedName("type")
    private String type;


    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pictureXL='" + pictureXL + '\'' +
                ", numberOfAlbums=" + numberOfAlbums +
                ", type='" + type + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureXL() {
        return pictureXL;
    }

    public void setPictureXL(String pictureXL) {
        this.pictureXL = pictureXL;
    }

    public int getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public void setNumberOfAlbums(int numberOfAlbums) {
        this.numberOfAlbums = numberOfAlbums;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
