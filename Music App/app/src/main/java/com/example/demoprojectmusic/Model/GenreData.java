package com.example.demoprojectmusic.Model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class GenreData {
    @SerializedName("data")
    private List<Genre> data;

    public List<Genre> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<Genre> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("GenreData{data=[");
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                stringBuilder.append(data.get(i).toString());
                if (i < data.size() - 1) {
                    stringBuilder.append(",");
                }
            }
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }
}
