package com.example.demoprojectmusic.View;

import androidx.annotation.NonNull;
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
import com.example.demoprojectmusic.Model.Artist;
import com.example.demoprojectmusic.Model.Playlist;
import com.example.demoprojectmusic.Model.Track;
import com.example.demoprojectmusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.Set;

public class ArtistInfoActivity extends AppCompatActivity {

    RecyclerView rcv_listTracks, rv_listAlbums, rv_listArtists;
    ImageView imgArtist;
    TextView nameArtist;
    ImageButton btnBack;
    private AlbumAdapter albumAdapter;
    private ArtistAdapter artistAdapter;
    private TrackAdapter trackAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_info);
        addControls();
        addEvents();

    }
    private void addEvents() {
        long artistId = getIntent().getLongExtra("artist_id", -1);
        if (artistId != -1) {
            showArtistInfo(artistId);
            getTrackByArtistId(artistId);
            getAllAlbumsByArtistId(artistId);
            getRamdomArtist(artistId);
        } else {
            Log.e("ArtistInfoActivity", "Invalid artist_id received");
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void showArtistInfo(long artistId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("artists")
                .document(String.valueOf(artistId))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Artist artist = document.toObject(Artist.class);
                            if (artist != null) {
                                if (artist.getPictureXL() != null) {
                                    Glide.with(this).load(artist.getPictureXL()).into(imgArtist);
                                }
                                nameArtist.setText(artist.getName());
                            }
                        }
                    }
                });
    }
    private void getTrackByArtistId(long artistId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tracks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Track> tracks = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Track track = document.toObject(Track.class);
                                if (track.getArtist() != null && track.getArtist().getId() == artistId) {
                                    tracks.add(track);
                                }
                            }
                            // Sắp xếp danh sách theo releaseDate giảm dần
                            Collections.sort(tracks, (t1, t2) -> t2.getReleaseDate().compareTo(t1.getReleaseDate()));

                            // Lấy 5 bản ghi mới nhất nếu có đủ 5 bài hát, nếu không, lấy toàn bộ danh sách
                            List<Track> latestTracks = tracks.size() > 5 ? tracks.subList(0, 5) : tracks;

                            if (tracks.isEmpty()) {
                                TextView albumsTitle = findViewById(R.id.txtPhoBien);
                                albumsTitle.setVisibility(View.GONE); // Ẩn tiêu đề album
                                rcv_listTracks.setVisibility(View.GONE); // Ẩn RecyclerView chứa danh sách album

                            } else {
                                displayTracksOnRecyclerView(latestTracks, rcv_listTracks);
                            }
                        } else {
                            // Xử lý khi không lấy được dữ liệu
                        }
                    }
                });
    }
    private void getRamdomArtist(long artistId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("artists")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Artist> allArtists = new ArrayList<>();
                        List<Artist> randomArtists = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Artist artist = document.toObject(Artist.class);
                            if (artist.getId() != artistId && artist.getId() != 0) {
                                allArtists.add(artist);
                            }
                        }
                        // Kiểm tra xem có đủ nghệ sĩ để chọn ngẫu nhiên không
                        if (allArtists.size() >= 5) {
                            // Lấy 5 nghệ sĩ ngẫu nhiên
                            Set<Integer> indexes = new HashSet<>();
                            Random random = new Random();

                            while (indexes.size() < 5) {
                                int randomIndex = random.nextInt(allArtists.size());
                                indexes.add(randomIndex);
                            }

                            for (int index : indexes) {
                                randomArtists.add(allArtists.get(index));
                            }
                        } else {
                            // Trường hợp số lượng nghệ sĩ ít hơn 5
                            randomArtists.addAll(allArtists);
                        }
                        if (randomArtists.isEmpty()) {
                            TextView albumsTitle = findViewById(R.id.txtPhoBien);
                            albumsTitle.setVisibility(View.GONE); // Ẩn tiêu đề album
                            rcv_listTracks.setVisibility(View.GONE); // Ẩn RecyclerView chứa danh sách album

                        } else {
                            // Hiển thị danh sách album trên RecyclerView
                            displayRandomArtists(randomArtists, rv_listArtists);
                        }

                    } else {
                        // Xử lý khi không lấy được dữ liệu
                    }
                });
    }

    private void displayRandomArtists(List<Artist> randomArtists, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        artistAdapter = new ArtistAdapter(randomArtists);
        recyclerView.setAdapter(artistAdapter);
        artistAdapter.setOnItemClickListener(new ArtistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Artist artist) {
                Intent intent = new Intent(getApplicationContext(), ArtistInfoActivity.class);
                intent.putExtra("artist_id", artist.getId());
                startActivity(intent);
            }
        });

    }


    private void displayTracksOnRecyclerView(List<Track> tracks, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        trackAdapter = new TrackAdapter(tracks);
        recyclerView.setAdapter(trackAdapter);
        trackAdapter.setOnItemClickListener(position -> {
            Track selectedTrack = tracks.get(position);
            long trackId = selectedTrack.getId();

            Intent intent = new Intent(this, PlayMusicActivity.class);
            intent.putExtra("track_id", trackId);
            startActivity(intent);
        });
    }


    private void getAllAlbumsByArtistId(long artistId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("albums")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Album> albums = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Album album = document.toObject(Album.class);
                                if (album.getArtist() != null && album.getArtist().getId() == artistId) {
                                    if (album.getId() != 0) {
                                        albums.add(album);
                                    }
                                }
                            }
                            if (albums.isEmpty()) {
                                TextView albumsTitle = findViewById(R.id.albumsTitle);
                                albumsTitle.setVisibility(View.GONE); // Ẩn tiêu đề album
                                rv_listAlbums.setVisibility(View.GONE); // Ẩn RecyclerView chứa danh sách album

                            } else {
                                // Hiển thị danh sách album trên RecyclerView
                                displayAlbumsOnRecyclerView(albums, rv_listAlbums);
                            }

                        } else {
                            // Xử lý khi không lấy được dữ liệu
                        }
                    }

                });
    }

    private void displayAlbumsOnRecyclerView(List<Album> albums, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        albumAdapter = new AlbumAdapter(albums);
        recyclerView.setAdapter(albumAdapter);
        albumAdapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Album album) {
                Intent intent = new Intent(getApplicationContext(), AlbumInfoActivity.class);
                intent.putExtra("album_id", album.getId());
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        rcv_listTracks = findViewById(R.id.rcv_listTracks);
        rv_listAlbums = findViewById(R.id.rv_listAlbums);
        rv_listArtists = findViewById(R.id.rv_listArtists);
        imgArtist = findViewById(R.id.imgArtist);
        nameArtist = findViewById(R.id.nameArtist);
        btnBack = findViewById(R.id.btnBack);
    }
}