package com.example.demoprojectmusic.Model;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Track {

    //N: lấy track từ firebase
    /*public static Track trackFromDocumentSnapshot(DocumentSnapshot document) {
        Track track = new Track();
        track.setId(document.getLong("id").intValue());
        track.setTitle(document.getString("title"));
        track.setDuration(document.getLong("duration").intValue());
        track.setTrackPosition(document.getLong("track_position").intValue());
        track.setDiskNumber(document.getLong("disk_number").intValue());
        track.setReleaseDate(document.getString("release_date"));
        track.setPreview(document.getString("preview"));

        // Lấy thông tin về nghệ sĩ của track từ document snapshot
        Object artistObj = document.get("artist");
        if (artistObj instanceof Map) {
            Map<String, Object> artistMap = (Map<String, Object>) artistObj;
            Artist artist = new Artist();
            artist.setId((int) (long) artistMap.get("id"));
            artist.setName((String) artistMap.get("name"));
            artist.setPictureXL((String) artistMap.get("picture_xl"));
            track.setArtist(artist);
        }

        // Lấy thông tin về album của track từ document snapshot
        Object albumObj = document.get("album");
        if (albumObj instanceof Map) {
            Map<String, Object> albumMap = (Map<String, Object>) albumObj;
            Album album = new Album();
            album.setId((int) (long) albumMap.get("id"));
            album.setTitle((String) albumMap.get("title"));
            album.setCover_xl((String) albumMap.get("cover_xl"));
            // Set các thông tin khác của album tương tự như trên

            track.setAlbum(album);
        }

        return track;
    }*/


    //N
    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("duration")
    private int duration;

    @SerializedName("track_position")
    private int trackPosition;

    @SerializedName("disk_number")
    private int diskNumber;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("preview")
    private String preview;

    @SerializedName("contributors")
    private List<Contributor> contributors;

    @SerializedName("artist")
    private Artist artist;

    @SerializedName("album")
    private Album album;

    @SerializedName("type")
    private String type;



    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Track{");
        stringBuilder.append("id=").append(id);
        stringBuilder.append(", title='").append(title).append('\'');
        stringBuilder.append(", duration=").append(duration);
        stringBuilder.append(", trackPosition=").append(trackPosition);
        stringBuilder.append(", diskNumber=").append(diskNumber);
        stringBuilder.append(", releaseDate='").append(releaseDate).append('\'');
        stringBuilder.append(", preview='").append(preview).append('\'');

        // Hiển thị thông tin về nghệ sĩ của track
        if (artist != null) {
            stringBuilder.append(", artist=").append(artist.toString());
        }

        // Hiển thị thông tin về album của track
        if (album != null) {
            stringBuilder.append(", album=").append(album.toString());
        }

        if (contributors != null) {
            stringBuilder.append(", contributors=").append(contributors.toString());
        }

        stringBuilder.append('}');
        return stringBuilder.toString();
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTrackPosition() {
        return trackPosition;
    }

    public void setTrackPosition(int trackPosition) {
        this.trackPosition = trackPosition;
    }

    public int getDiskNumber() {
        return diskNumber;
    }

    public void setDiskNumber(int diskNumber) {
        this.diskNumber = diskNumber;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
