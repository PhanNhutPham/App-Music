package com.example.demoprojectmusic.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoprojectmusic.Controler.AlbumAdapter;
import com.example.demoprojectmusic.Controler.TracksAdapter1;
import com.example.demoprojectmusic.Controler.adapterAD;
import com.example.demoprojectmusic.Controler.adaptertheloai;
import com.example.demoprojectmusic.Model.Album;
import com.example.demoprojectmusic.Model.Artist;
import com.example.demoprojectmusic.Model.DeezerApiService;
import com.example.demoprojectmusic.Model.Genre;
import com.example.demoprojectmusic.Model.GenreData;
import com.example.demoprojectmusic.Model.Playlist;
import com.example.demoprojectmusic.Model.Track;
import com.example.demoprojectmusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchTheLoai extends AppCompatActivity {
    private adaptertheloai albumAdapter;
    private adapterAD adapterAD;
    String jsonListIdMusic = "listIdMusic.json";
    RecyclerView rcv, rcvAD, rcvAlbumIn;
    TextView edtOS;
    ImageView btnHome, btnSearch, btnLibrary, btnProfile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_the_loai);
        btnHome = findViewById(R.id.btnHome);
        btnLibrary = findViewById(R.id.btnLibrary);
        btnProfile = findViewById(R.id.btnProfile);
        btnSearch = findViewById(R.id.btnSearch);
        rcvAlbumIn=(RecyclerView) findViewById(R.id.rctheloaialbum);
        rcvAD = (RecyclerView)findViewById(R.id.adTheLoai);
        rcv = (RecyclerView) findViewById(R.id.rcv_theloai);
        edtOS = (TextView) findViewById(R.id.ED_search13);
        edtOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchTheLoai.this,SearchALL.class);
                startActivity(i);
            }
        });
        showAlbumsList();
        showAlbumsList_ad();
        //readJsonFromAssets(jsonListIdMusic);
        addEvents();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int currentItem = 0;
            @Override
            public void run() {
                if (currentItem < adapterAD.getItemCount()) {
                    rcvAD.smoothScrollToPosition(currentItem);
                    currentItem++;
                } else {
                    currentItem = 0;
                    rcvAD.smoothScrollToPosition(currentItem);
                }
                handler.postDelayed(this, 2000); // Khoảng thời gian chuyển đổi 2 giây
            }
        };
        handler.postDelayed(runnable, 2000);


    }

    private void addEvents() {
        int userId = getIntent().getIntExtra("user_id", -1);
        Log.e("userid", String.valueOf(userId));
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchTheLoai.this, ProfileActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchTheLoai.this, DashBoardActivity.class);
                startActivity(intent);
            }
        });
        btnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchTheLoai.this, List_Like.class);
                startActivity(intent);
            }
        });
    }
    private void showAlbumsList_ad() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcvAD.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rcvAD);
        db.collection("albums")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Album> albumInfoList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Album album = document.toObject(Album.class);
                                albumInfoList.add(album);
                                Collections.shuffle(albumInfoList);
                            }
                            adapterAD = new adapterAD(albumInfoList,getApplicationContext());
                            rcvAD.setAdapter(adapterAD);
                            adapterAD.setAlbumClickListener(new adapterAD.OnAlbumClickListener() {
                                @Override
                                public void onAlbumClick(Album album) {
                                    Intent intent = new Intent(getApplicationContext(), AlbumInfoActivity.class);
                                    intent.putExtra("album_id", album.getId()); // Truyền thông tin cần thiết nếu cần
                                    startActivity(intent);
                                }
                            });

                        } else {
                            Log.d("showAlbumsList", "Lấy data album không thành công");
                        }
                    }
                });
    }

    private void showAlbumsList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("albums")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Album> albumInfoList = new ArrayList<>();
                            Set<String> uniqueNames = new HashSet<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Album album = document.toObject(Album.class);
                                GenreData genreData = album.getGenreData();

                                if (genreData != null) {
                                    List<Genre> genres = genreData.getData();

                                    if (genres != null && !genres.isEmpty()) {
                                        for (Genre genre : genres) {
                                            String name = String.valueOf(genre.getName());

                                            if (name != null && !name.isEmpty() && !uniqueNames.contains(name)) {
                                                uniqueNames.add(name);
                                                albumInfoList.add(album);
                                                Log.d("ddd", "sssss" + name);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            albumAdapter = new adaptertheloai(albumInfoList, uniqueNames);

                            GridLayoutManager layoutManager = new GridLayoutManager(SearchTheLoai.this, 2);
                            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(int position) {
                                    return 1;
                                }
                            });
                            rcv.setLayoutManager(layoutManager);
                            rcv.setAdapter(albumAdapter);
                        } else {
                            // Xử lý lỗi khi truy vấn albums
                        }
                    }
                });
    }
//    private void readJsonFromAssets(String filename) {
//        AssetManager assetManager = getAssets();
//        try (InputStream inputStream = assetManager.open(filename);
//             Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
//
//            Gson gson = new Gson();
//            Type type = new TypeToken<Map<String, List<Long>>>() {
//            }.getType();
//            Map<String, List<Long>> data = gson.fromJson(reader, type);
//
//            if (data != null && !data.isEmpty()) {
//                for (Map.Entry<String, List<Long>> entry : data.entrySet()) {
//                    String key = entry.getKey();
//                    List<Long> values = entry.getValue();
//
//                    for (long value : values) {
//                        String apiId = String.valueOf(value);
//                        //N: Gọi endpoint từ API dựa trên loại id và id
//                        if (key.equals("albums")) {
//                            readDataFormAPI(apiId, "albums");
//                        } else if (key.equals("artists")) {
//                            readDataFormAPI(apiId, "artists");
//                        } else if (key.equals("tracks")) {
//                            readDataFormAPI(apiId, "tracks");
//                        } else if (key.equals("playlists")) {
//                            readDataFormAPI(apiId, "playlists");
//                        }
//
//                    }
//                }
//            }
//        } catch (IOException e) {
//            //N: show lỗi tại log
//            Log.e("readJsonFromAssets", "Error: " + e.getMessage());
//            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//    private void readDataFormAPI(String apiId, String dataType) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        DeezerApiService apiService = retrofit.create(DeezerApiService.class);
//
//        switch (dataType) {
//            case "albums":
//                Call<Album> callAlbum = apiService.getAlbumData(apiId);
//                callAlbum.enqueue(new Callback<Album>() {
//                    @Override
//                    public void onResponse(Call<Album> call, Response<Album> response) {
//                        if (response.isSuccessful()) {
//                            Album album = response.body();
//                            if (album != null) {
//                                saveDataToFirestore(album, "albums");
//                                //getArtistAndTrackFromAlbum(album);
//                                Log.d("readDataAlbum", "Lấy Album data từ api OK");
//                            } else {
//                                Log.e("readDataAlbum", "Album data là null");
//                            }
//                        } else {
//                            Log.e("readDataAlbum", "Không thể truy xuất Album data");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Album> call, Throwable t) {
//                        Log.e("readDataAlbum", "Error khi lấy Album data: " + t.getMessage());
//                    }
//                });
//                break;
//            case "artists":
//                Call<Artist> callArtist = apiService.getArtistData(apiId);
//                callArtist.enqueue(new Callback<Artist>() {
//                    @Override
//                    public void onResponse(Call<Artist> call, Response<Artist> response) {
//                        if (response.isSuccessful()) {
//                            Artist artist = response.body();
//                            if (artist != null) {
//                                saveDataToFirestore(artist, "artists");
//                                Log.d("readDataArtist", "Lấy Artist data từ api OK");
//                            } else {
//                                Log.e("readDataArtist", "Artist data là null");
//                            }
//                        } else {
//                            Log.e("readDataArtist", "Không thể truy xuất Artist data");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Artist> call, Throwable t) {
//                        Log.e("readDataArtist", "Error khi lấy Artist data: " + t.getMessage());
//                    }
//                });
//                break;
//            case "tracks":
//                Call<Track> callTrack = apiService.getTrackData(apiId);
//                callTrack.enqueue(new Callback<Track>() {
//                    @Override
//                    public void onResponse(Call<Track> call, Response<Track> response) {
//                        if (response.isSuccessful()) {
//                            Track track = response.body();
//                            if (track != null) {
//                                saveDataToFirestore(track, "tracks");
//                                Log.d("readDataTrack", "Lấy Track data từ api Ok");
//                            } else {
//                                Log.e("readDataTrack", "Track data is null");
//                            }
//                        } else {
//                            Log.e("readDataTrack", "Không thể truy xuất Track data");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Track> call, Throwable t) {
//                        Log.e("readDataTrack", "Error fetching track data: " + t.getMessage());
//                    }
//                });
//                break;
//            case "playlists":
//                Call<Playlist> callPlaylist = apiService.getPlaylistData(apiId);
//                callPlaylist.enqueue(new Callback<Playlist>() {
//                    @Override
//                    public void onResponse(Call<Playlist> call, Response<Playlist> response) {
//                        if (response.isSuccessful()) {
//                            Playlist playlist = response.body();
//                            if (playlist != null) {
//                                saveDataToFirestore(playlist, "playlists");
//                                //getArtistAndTrackFromPlaylist(playlist);
//                                Log.d("readDataPlaylist", "Lấy Playlist data từ api OK");
//                            } else {
//                                Log.e("readDataPlaylist", "Playlist data là null");
//                            }
//                        } else {
//                            Log.e("readDataPlaylist", "Không thể truy xuất Playlist data");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Playlist> call, Throwable t) {
//                        Log.e("readDataPlaylist", "Error khi lấy Playlist: " + t.getMessage());
//                    }
//                });
//                break;
//            default:
//                return;
//        }
//    }
    private long getDataId(Object dataObject) {
        if (dataObject instanceof Album) {
            return ((Album) dataObject).getId();
        } else if (dataObject instanceof Artist) {
            return ((Artist) dataObject).getId();
        } else if (dataObject instanceof Track) {
            return ((Track) dataObject).getId();
        } else if (dataObject instanceof Playlist) {
            return ((Playlist) dataObject).getId();
        }
        return Long.valueOf(-1);
    }
    private void saveDataToFirestore(Object dataObject, String collectionName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(collectionName);

        long objectId = getDataId(dataObject);

        collectionReference.document(String.valueOf(objectId))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Đối tượng đã tồn tại trong Firestore
                                Log.d("Firestore", collectionName + ": " + objectId + " đã tồn tại");
                            } else {
                                // Đối tượng mới, lưu vào Firestore
                                collectionReference.document(String.valueOf(objectId))
                                        .set(dataObject)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Firestore", "Lưu mới  " + collectionName + ": " + objectId + " vào Firestore");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Firestore", "Error khi lưu " + collectionName + ": " + objectId + " vào Firestore: " + e.getMessage());
                                            }
                                        });
                            }
                        } else {
                            Log.e("Firestore", "Error getting document: " + task.getException());
                        }
                    }
                });
    }
}