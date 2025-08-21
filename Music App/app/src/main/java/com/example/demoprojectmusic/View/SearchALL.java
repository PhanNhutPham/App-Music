package com.example.demoprojectmusic.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoprojectmusic.Controler.AdapterPlus;
import com.example.demoprojectmusic.Controler.AlbumAdapter;
import com.example.demoprojectmusic.Controler.PlayListAdapter1;
import com.example.demoprojectmusic.Controler.TracksAdapter1;
import com.example.demoprojectmusic.Model.Album;
import com.example.demoprojectmusic.Model.Artist;
import com.example.demoprojectmusic.Model.Contributor;
import com.example.demoprojectmusic.Model.DeezerApiService;
import com.example.demoprojectmusic.Model.Genre;
import com.example.demoprojectmusic.Model.GenreData;
import com.example.demoprojectmusic.Model.Playlist;
import com.example.demoprojectmusic.Model.Track;
import com.example.demoprojectmusic.Model.TrackData;
import com.example.demoprojectmusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchALL extends AppCompatActivity {
    private boolean isRecyclerViewVisible = false;
    //private boolean isRecyclerViewVisible = false;
    TextView tvHomeSongNV, tvTitle;
    EditText edtV ;

    String jsonListIdMusic = "listIdMusic.json"; //N: tên file assets lưu id
    int idAlbum = 231552772;
    int idGenreKpop = 23;
    RecyclerView rvListAlbum, rvListTrack, rvPlayList;
    private AdapterPlus albumAdapter;
    private TracksAdapter1 tracksAdapter1;
    private PlayListAdapter1 playListAdapter1;
    //    private ArtistAdapter artistAdapter;
//    private GenreKpopAdapter genreKpopAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all);
        addControls();
        addEvents();

    }
    private  void addEvents(){
        showTrack();
        showAlbumsList();
        showPlayList();
    }
    private void showAlbumsList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvListAlbum.setLayoutManager(layoutManager);
        db.collection("albums")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Album> albumInfoList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Album album = document.toObject(Album.class);
                            albumInfoList.add(album);
                        }

                        albumAdapter = new AdapterPlus(getApplicationContext(), (ArrayList<Album>) albumInfoList);
                        rvListAlbum.setAdapter(albumAdapter);

                        albumAdapter.setAlbumClickListener((new AdapterPlus.OnAlbumClickListener() {
                            @Override
                            public void onAlbumClick(Album album) {
                                Intent intent = new Intent(getApplicationContext(), AlbumInfoActivity.class);
                                intent.putExtra("album_id", album.getId()); // Truyền thông tin cần thiết nếu cần
                                startActivity(intent);
                            }
                        }));
                        edtV.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                                albumAdapter.getFilter().filter(s);

                                if (s.length() == 0) {
                                    if (isRecyclerViewVisible) {
                                        rvListAlbum.setVisibility(View.GONE);
                                        isRecyclerViewVisible = false;
                                    }
                                } else {
                                    if (!isRecyclerViewVisible) {
                                        rvListAlbum.setVisibility(View.VISIBLE);
                                        isRecyclerViewVisible = true;
                                    }
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    } else {
                        // Handle error getting albums
                    }
                });
    }
    private void showTrack() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvListTrack.setLayoutManager(layoutManager);
        TextView txt = (TextView) findViewById(R.id.txtTracks);
        db.collection("tracks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Track> tracksInfo = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Track track = document.toObject(Track.class);
                            tracksInfo.add(track);
                        }

                        if (tracksInfo != null) {
                            // Now that you have the tracksInfo, create the adapter and set it to the RecyclerView
                            tracksAdapter1 = new TracksAdapter1(getApplicationContext(), tracksInfo);
                            rvListTrack.setAdapter(tracksAdapter1);

                            tracksAdapter1.setOnItemClickListener(new TracksAdapter1.OnItemClickListener() {
                                @Override
                                public void onItemClick(Track track) {
                                    Intent intent = new Intent(getApplicationContext(), PlayMusicActivity.class);
                                    intent.putExtra("track_id", track.getId()); // Truyền thông tin cần thiết nếu cần
                                    startActivity(intent);
                                }
                            });

                            edtV.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                                    String searchText = s.toString().trim();

                                    if (searchText.isEmpty()) {
                                        rvListTrack.setVisibility(View.GONE);
                                    } else {
                                        tracksAdapter1.getFilter().filter(searchText);

                                        if (tracksAdapter1.getItemCount() == 0) {
                                            rvListTrack.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_SHORT).show();
                                        } else {
                                            rvListTrack.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
//                                    if (tracksAdapter1.getItemCount() == 0) {
//                                        rvListTrack.setVisibility(View.GONE);
//                                        txt.setVisibility(View.GONE);
//
//                                    } else {
//                                        rvListTrack.setVisibility(View.VISIBLE);
//                                        txt.setVisibility(View.VISIBLE);
//                                    }
                                }
                            });

                        } else {
                            // Handle the case where tracksInfo is null
                        }
                    } else {
                        // Handle error getting tracks
                    }
                });
    }
    private void showPlayList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        GridLayoutManager layoutManager = new GridLayoutManager(SearchALL.this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        rvPlayList.setLayoutManager(layoutManager);
        db.collection("playlists")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Playlist> playlistsinfo = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Playlist playlist = document.toObject(Playlist.class);
                            playlistsinfo.add(playlist);
                        }

                        if (playlistsinfo != null) {
                            // Now that you have the tracksInfo, create the adapter and set it to the RecyclerView
                            playListAdapter1 = new PlayListAdapter1(getApplicationContext(), playlistsinfo);
                            rvPlayList.setAdapter(playListAdapter1);

                            playListAdapter1.setOnItemClickListener(new PlayListAdapter1.OnItemClickListener() {
                                @Override
                                public void onItemClick(Playlist playlist) {
                                    Intent intent = new Intent(getApplicationContext(), PlaylistInfoActivity.class);
                                    intent.putExtra("playlist_id", playlist.getId()); // Truyền thông tin cần thiết nếu cần
                                    startActivity(intent);
                                }
                            });

                        } else {
                            // Handle the case where playlistsinfo is null
                        }
                    } else {
                        // Handle error getting playlists
                    }
                });
    }
    @SuppressLint("WrongViewCast")
    private void addControls() {
        //tvHomeSongNV = (TextView) findViewById(R.id.homeSongNameView);
        tvTitle = findViewById(R.id.title);
        rvListAlbum = findViewById(R.id.rvSearch);
        rvListTrack = findViewById(R.id.rvTracks);
        rvPlayList = findViewById(R.id.rvPlayLists);
        edtV = (EditText) findViewById(R.id.ED_search);
//        rvListGenreKpop = findViewById(R.id.rv_listGenreKpop);

    }
}