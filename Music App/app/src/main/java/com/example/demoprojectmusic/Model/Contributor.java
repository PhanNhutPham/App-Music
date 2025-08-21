package com.example.demoprojectmusic.Model;

import com.google.gson.annotations.SerializedName;

public class Contributor {
    //N
    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("picture_xl")
    private String pictureXL;

    public String getPictureXL() {
        return pictureXL;
    }

    public void setPictureXL(String pictureXL) {
        this.pictureXL = pictureXL;
    }


    @SerializedName("type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @SerializedName("role")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Contributor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pictureXL='" + pictureXL + '\'' +
                ", type='" + type + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
