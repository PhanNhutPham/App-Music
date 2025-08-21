package com.example.demoprojectmusic.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface DeezerApiService {

    //N
    @GET("album/{id}")
    @Headers({
            "X-RapidAPI-Key: 4aaeb78d56mshb9ec521b37d856bp1feef0jsn4d0fea9acb79",
            "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com",
            "Accept: application/json"
    })
    Call<Album> getAlbumData(@Path("id") String AlbumId);

    @GET("artist/{id}")
    @Headers({
            "X-RapidAPI-Key: 4aaeb78d56mshb9ec521b37d856bp1feef0jsn4d0fea9acb79",
            "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com",
            "Accept: application/json"
    })
    Call<Artist> getArtistData(@Path("id") String ArtistId);

    @GET("track/{id}")
    @Headers({
            "X-RapidAPI-Key: 4aaeb78d56mshb9ec521b37d856bp1feef0jsn4d0fea9acb79",
            "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com",
            "Accept: application/json"
    })
    Call<Track> getTrackData(@Path("id") String TrackId);

    @GET("playlist/{id}")
    @Headers({
            "X-RapidAPI-Key: 4aaeb78d56mshb9ec521b37d856bp1feef0jsn4d0fea9acb79",
            "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com",
            "Accept: application/json"
    })
    Call<Playlist> getPlaylistData(@Path("id") String PlaylistId);
}
