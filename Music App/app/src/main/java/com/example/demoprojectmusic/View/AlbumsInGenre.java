package com.example.demoprojectmusic.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.demoprojectmusic.Model.Album;
import com.example.demoprojectmusic.R;

import java.util.List;

public class AlbumsInGenre extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums_in_genre);
        List<Album> filteredAlbums = getIntent().<Album>getParcelableArrayListExtra("filteredAlbums");
    }
}