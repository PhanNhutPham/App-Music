package com.example.demoprojectmusic.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.demoprojectmusic.Controler.AlbumAdapter;
import com.example.demoprojectmusic.Controler.ArtistAdapter;
import com.example.demoprojectmusic.Controler.PlaylistAdapter;
import com.example.demoprojectmusic.Controler.TrackAdapter;
import com.example.demoprojectmusic.Model.Album;
import com.example.demoprojectmusic.Model.Contributor;
import com.example.demoprojectmusic.Model.Genre;
import com.example.demoprojectmusic.Model.GenreData;
import com.example.demoprojectmusic.Model.Track;
import com.example.demoprojectmusic.Model.TrackData;
import com.example.demoprojectmusic.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class AlbumInfoActivity extends AppCompatActivity {

    RecyclerView rcv_listTracks;
    ImageView cover_album, picture_artist;
    TextView title_album, name_artist, type_album;
    ImageButton btnBack;
    private TrackAdapter trackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_info);

        addControls();
        addEvents();

    }


    private void addEvents() {
        // Lấy album_id từ Intent
        long albumId = getIntent().getLongExtra("album_id", -1);
        if (albumId != -1) {
            showTracksInAlbum(albumId);
        } else {
            Log.e("AlbumInfoActivity", "Invalid album_id received");
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showTracksInAlbum(long idAlbum) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("albums")
                .document(String.valueOf(idAlbum))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Album album = document.toObject(Album.class);
                            if (album != null) {
                                displayAlbumDetails(album);
                                displayTracks(album.getTrackData());
                            }
                        } else {
                            Log.e("Firestore", "Error getting album document: ", task.getException());
                        }
                    }
                });
    }

    private void displayTracks(TrackData trackData) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcv_listTracks.setLayoutManager(layoutManager);

        if (trackData != null) {
            List<Track> tracks = trackData.getData();
            if (tracks != null) {
                trackAdapter = new TrackAdapter(tracks);
                rcv_listTracks.setAdapter(trackAdapter);

                trackAdapter.setOnItemClickListener(position -> {
                    Track selectedTrack = tracks.get(position);
                    long trackId = selectedTrack.getId();

                    // Tạo danh sách trackIds
                    List<Long> trackIds = new ArrayList<>();
                    for (Track track : tracks) {
                        trackIds.add(track.getId());
                    }

                    Intent intent = new Intent(AlbumInfoActivity.this, PlayMusicActivity.class);
                    intent.putExtra("track_id", trackId);
                    long[] trackIdsArray = convertLongListToArray(trackIds);
                    intent.putExtra("track_ids", trackIdsArray);
                    intent.putExtra("type", "ALBUM");
                    startActivity(intent);
                });
            } else {
                Log.d("Track", "No tracks found");
            }
        }
    }

    private long[] convertLongListToArray(List<Long> trackIds) {
        long[] longArray = new long[trackIds.size()];
        for (int i = 0; i < trackIds.size(); i++) {
            longArray[i] = trackIds.get(i);
        }
        return longArray;
    }

    private void displayAlbumDetails(Album album) {
        if (album.getCover_xl() != null) {
            Glide.with(this).load(album.getCover_xl()).into(cover_album);
        }
        if (album.getArtist() != null && album.getArtist().getPictureXL() != null) {
            Glide.with(this).load(album.getArtist().getPictureXL()).into(picture_artist);
        }
        title_album.setText(album.getTitle());
        name_artist.setText(album.getArtist().getName());
        String type = album.getType();
        type = type.substring(0, 1).toUpperCase() + type.substring(1);
        type_album.setText(type + " . " + album.getRelease_date());
    }

    private void addControls() {
        rcv_listTracks = findViewById(R.id.rcv_listTracks);
        cover_album = findViewById(R.id.cover_album);
        picture_artist = findViewById(R.id.picture_artist);
        title_album = findViewById(R.id.title_album);
        name_artist = findViewById(R.id.name_artist);
        type_album = findViewById(R.id.type_album);
        btnBack = findViewById(R.id.btnBack);
    }
}