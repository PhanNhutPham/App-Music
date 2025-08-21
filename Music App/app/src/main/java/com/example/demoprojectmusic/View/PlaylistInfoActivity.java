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
import com.example.demoprojectmusic.Model.TrackData;
import com.example.demoprojectmusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PlaylistInfoActivity extends AppCompatActivity {

    ImageView imgPlaylist;
    TextView titlePlaylist, description, durationPlaylist, nbOfTracks;
    RecyclerView rcv_listTracks, rcv_listPlaylistRCM;
    ImageButton btnBack;

    private PlaylistAdapter playlistAdapter;
    private TrackAdapter trackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_info);


        addControls();
        addEvents();
    }

    private void addEvents() {
        long playlistId = getIntent().getLongExtra("playlist_id", -1);
        if (playlistId != -1) {
            showTracksInPlaylist(playlistId);
            getRamdomPlaylist(playlistId);
        } else {
            Log.e("PlaylistInfoActivity", "Khong nhan duoc playlist ID");
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void getRamdomPlaylist(long playlistId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("playlists")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Playlist> allPlaylists = new ArrayList<>();
                        List<Playlist> randomPlaylists = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Playlist playlist = document.toObject(Playlist.class);
                            if (playlist.getId() != playlistId) {
                                allPlaylists.add(playlist);
                            }
                        }
                        // Kiểm tra xem có đủ nghệ sĩ để chọn ngẫu nhiên không
                        if (allPlaylists.size() >= 5) {
                            // Lấy 5 nghệ sĩ ngẫu nhiên
                            Set<Integer> indexes = new HashSet<>();
                            Random random = new Random();

                            while (indexes.size() < 5) {
                                int randomIndex = random.nextInt(allPlaylists.size());
                                indexes.add(randomIndex);
                            }

                            for (int index : indexes) {
                                randomPlaylists.add(allPlaylists.get(index));
                            }
                        } else {
                            // Trường hợp số lượng nghệ sĩ ít hơn 5
                            randomPlaylists.addAll(allPlaylists);
                        }
                        if (randomPlaylists.isEmpty()) {
                            TextView albumsTitle = findViewById(R.id.txtPhoBien);
                            albumsTitle.setVisibility(View.GONE); // Ẩn tiêu đề album
                            rcv_listTracks.setVisibility(View.GONE); // Ẩn RecyclerView chứa danh sách album

                        } else {
                            // Hiển thị danh sách album trên RecyclerView
                            displayRandomPlaylists(randomPlaylists, rcv_listPlaylistRCM);
                        }

                    } else {
                        // Xử lý khi không lấy được dữ liệu
                    }
                });
    }

    private void displayRandomPlaylists(List<Playlist> playlists, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        playlistAdapter = new PlaylistAdapter(playlists);
        recyclerView.setAdapter(playlistAdapter);

        playlistAdapter.setOnItemClickListener(new PlaylistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Playlist playlist) {
                Intent intent = new Intent(getApplicationContext(), PlaylistInfoActivity.class);
                intent.putExtra("playlist_id", playlist.getId());
                startActivity(intent);
            }
        });
    }

    private void showTracksInPlaylist(long playlistId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("playlists")
                .document(String.valueOf(playlistId))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Playlist playlist = document.toObject(Playlist.class);
                            if (playlist != null) {
                                displayTracks(playlist.getTrackData());
                                displayPlaylistDetail(playlist);
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

                    Intent intent = new Intent(this, PlayMusicActivity.class);
                    intent.putExtra("track_id", trackId);
                    startActivity(intent);
                });
            } else {
                Log.d("Track", "No tracks found");
            }
        }
    }

    private void displayPlaylistDetail(Playlist playlist) {
        Glide.with(this).load(playlist.getPictureExtraLarge()).into(imgPlaylist);
        titlePlaylist.setText(playlist.getTitle());
        if (playlist.getDescription().isEmpty()) {
            description.setVisibility(View.GONE); // Ẩn tiêu đề album
        }
        else {
            description.setText(playlist.getDescription());
        }
        nbOfTracks.setText("| " + playlist.getNumberOfTracks() + " bài hát");
        int duration = playlist.getDuration();
        durationPlaylist.setText("| " + duration / 3600 + " giờ " + (duration %3600) / 60 + " phút");

    }

    private void addControls() {
        imgPlaylist = findViewById(R.id.imgPlaylist);
        titlePlaylist = findViewById(R.id.titlePlaylist);
        description = findViewById(R.id.description);
        durationPlaylist = findViewById(R.id.durationPlaylist);
        nbOfTracks = findViewById(R.id.nbOfTracks);
        rcv_listTracks = findViewById(R.id.rcv_listTracks);
        rcv_listPlaylistRCM = findViewById(R.id.rcv_listPlaylistRCM);
        btnBack = findViewById(R.id.btnBack);
    }
}