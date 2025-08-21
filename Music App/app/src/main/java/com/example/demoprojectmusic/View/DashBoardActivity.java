package com.example.demoprojectmusic.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoprojectmusic.Controler.AlbumAdapter;
import com.example.demoprojectmusic.Controler.ArtistAdapter;
import com.example.demoprojectmusic.Controler.PlaylistAdapter;
import com.example.demoprojectmusic.Model.Album;
import com.example.demoprojectmusic.Model.Artist;
import com.example.demoprojectmusic.Model.Contributor;
import com.example.demoprojectmusic.Model.DeezerApiService;
import com.example.demoprojectmusic.Model.Genre;
import com.example.demoprojectmusic.Model.GenreData;
import com.example.demoprojectmusic.Model.Playlist;
import com.example.demoprojectmusic.Model.Track;
import com.example.demoprojectmusic.Model.TrackData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;


import com.example.demoprojectmusic.R;
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
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class DashBoardActivity extends AppCompatActivity {

    TextView  tvTitle, tvHomeSongNV;
    ImageView btnHome, btnSearch, btnLibrary, btnProfile;
    String jsonListIdMusic = "listIdMusic.json"; //N: tên file assets lưu id
    int idAlbum = 231552772;
    int idGenreKpop = 23;
    private RecyclerView rvListAlbum, rvListArtist, rvListGenreKpop, rv_listPlaylists, rv_listAlbumsRCM;
    private AlbumAdapter albumAdapter;
    private ArtistAdapter artistAdapter;
    private PlaylistAdapter playlistAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        //N: Kết nối với firebase lần đầu nhớ chạy dòng dưới, xong thì cmt lại
        //FirebaseApp.initializeApp(this);

        addControls();
        addEvents();





    }
    private void addEvents() {
        int userId = getIntent().getIntExtra("user_id", -1);
        Log.e("userid", String.valueOf(userId));


        //N: đọc data từ assets -> có id, loaị id -> lấy endpoint từ API theo id -> lưu data vào firebase
        //readJsonFromAssets(jsonListIdMusic);

        //N: lấy album từ fire -> show trong listview
        showArtistsList();
        showAlbumsList();
        showPlaylistsList();
        showAlbumsByGenreId(idGenreKpop);
        showAlbumsRecommentToday();

        //showTracksInAlbum(idAlbum);

        /*tvHomeSongNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, PlayMusicActivity.class);
                startActivity(intent);
            }
        });*/

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, ProfileActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });

        btnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, List_Like.class);
                startActivity(intent);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btnSearch.setImageResource(R.drawable.new_search_image);
                //btnHome.setImageResource(R.drawable.home);
                Intent intent = new Intent(DashBoardActivity.this, SearchTheLoai.class);
                startActivity(intent);
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
    private void displayPlaylistsOnRecyclerView(List<Playlist> playlists, RecyclerView recyclerView) {
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

    private void displayArtistsOnRecyclerView(List<Artist> artistList, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        artistAdapter = new ArtistAdapter(artistList);
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
    private void showAlbumsList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                            }
                            displayAlbumsOnRecyclerView(albumInfoList, rvListAlbum);

                        } else {
                            Log.d("showAlbumsList", "Lấy data album không thành công");
                        }
                    }
                });
    }
    private void showAlbumsByGenreId(int idGenreKpop) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("albums")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Album> albums = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Album album = document.toObject(Album.class);
                            if (album != null) {

                                //so sánh điều kiện
                                GenreData genreData = album.getGenreData();
                                if (genreData != null) {
                                    List<Genre> genres = genreData.getData();
                                    if (genres != null) {
                                        Integer genreId = Integer.parseInt(String.valueOf(idGenreKpop));
                                        for (Genre genre : genres) {
                                            if (genre.getId() == genreId) {
                                                albums.add(album);
                                                displayAlbumsOnRecyclerView(albums, rvListGenreKpop);
                                            }
                                        }
                                    }
                                } else {
                                    Log.d("showAlbumsByGenreId", "Không tìm thấy dữ liệu về thể loại này");
                                }
                            }
                        }
                    } else {
                        Log.e("Firestore", "Error getting albums by genre ID: ", task.getException());
                    }
                });
    }
    private List<String> getRandomAlbumIds(List<String> albumIds, int count) {
        List<String> randomIds = new ArrayList<>();
        Random random = new Random();

        if (albumIds.size() <= count) {
            return albumIds;// Nếu số lượng album ít hơn hoặc bằng count, lấy toàn bộ danh sách album
        }

        while (randomIds.size() < count) {
            int index = random.nextInt(albumIds.size());
            String randomId = albumIds.get(index);

            if (!randomIds.contains(randomId)) {
                randomIds.add(randomId);
            }
        }
        return randomIds;
    }

    private void showAlbumsRecommentToday() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("albums")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> albumIds = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String albumId = document.getId();
                            albumIds.add(albumId);
                        }
                        Log.e("getAlbumsDataRandom", "Lỗi khi lấy dadà: ", task.getException());
                        // Lấy 5 ID ngẫu nhiên từ danh sách ID album
                        List<String> randomAlbumIds = getRandomAlbumIds(albumIds, 5);
                        Log.e("getAlbumsDataRandom", "Lỗi ", task.getException());
                        // Lấy dữ liệu của 5 album từ Firestore
                        getAlbumsData(randomAlbumIds);
                    } else {
                        Log.e("showAlbumsRecommentToday", "Lỗi khi lấy random tracks: ", task.getException());
                    }
                });
    }

    private void getAlbumsData(List<String> albumIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference albumsRef = db.collection("albums");

        List<Album> albums = new ArrayList<>();
        for (String albumId : albumIds) {
            albumsRef.document(albumId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Album album = document.toObject(Album.class);
                                if (album != null) {
                                    albums.add(album); // Thêm album vào danh sách
                                    if (albums.size() == albumIds.size()) {
                                        displayAlbumsOnRecyclerView(albums, rv_listAlbumsRCM);
                                    }
                                }
                            }
                        } else {
                            Log.e("getAlbumsDataRandom", "Lỗi khi lấy data random tracks: ", task.getException());
                        }
                    });
        }
    }



    private void showTracksInAlbum(int idAlbum) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy album có id = idAlbum từ Firestore
        db.collection("albums")
                .document(String.valueOf(idAlbum))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Album album = document.toObject(Album.class);
                            if (album != null) {
                                TrackData trackData = album.getTrackData();
                                GenreData genreData = album.getGenreData();

                                if (trackData != null) {
                                    List<Track> tracks = trackData.getData();
                                    if (tracks != null) {
                                        for (Track track : tracks) {
                                            Log.d("Track", track.toString());
                                        }
                                    } else {
                                        Log.d("Track", "No tracks found");
                                    }
                                } else {
                                    Log.d("Track", "No track data found in the album");
                                }

                                if (genreData != null) {
                                    List<Genre> genres = genreData.getData();
                                    if (genres != null) {
                                        for (Genre genre : genres) {
                                            Log.d("Genre", genre.toString());
                                        }
                                    } else {
                                        Log.d("Genre", "No genres found");
                                    }
                                } else {
                                    Log.d("Genre", "No genre data found in the album");
                                }

                                List<Contributor> contributors = album.getContributors();
                                if (contributors != null) {
                                    for (Contributor contributor : contributors) {
                                        Log.d("Contributor", contributor.toString());
                                    }
                                } else {
                                    Log.d("Contributor", "No contributors found");
                                }
                            } else {
                                Log.d("Album", "Album with specific id not found");
                            }
                        } else {
                            Log.e("Firestore", "Error getting album document: ", task.getException());
                        }
                    }
                });
    }


    private void showPlaylistsList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("playlists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Playlist> playlists = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Playlist playlist = document.toObject(Playlist.class);
                                if (playlist.getId() != 0) {
                                    playlists.add(playlist);
                                }
                            }
                            displayPlaylistsOnRecyclerView(playlists, rv_listPlaylists);

                        } else {
                            Log.e("showPlaylistsList", "Lỗi khi lấy data playlist: ", task.getException());
                        }
                    }
                });
    }


    private void showArtistsList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("artists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Artist> artistList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Artist artist = document.toObject(Artist.class);

                                if (artist.getId() != 0) {
                                    artistList.add(artist);
                                }
                            }
                            displayArtistsOnRecyclerView(artistList, rvListArtist);

                        } else {
                            Log.e("showArtistsList", "Lỗi khi lấy data artist: ", task.getException());
                        }
                    }
                });
    }




    private void getAllDatafromIdAlbum() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvListAlbum.setLayoutManager(layoutManager);
        db.collection("albums")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Album> albumInfoList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Album album = document.toObject(Album.class);
                                if(album!= null){
                                    TrackData trackData = album.getTrackData();
                                    if (trackData != null) {
                                        List<Track> tracks = trackData.getData();
                                        if (tracks != null) {
                                            for(Track track: tracks){
                                                String trackId = String.valueOf(track.getId());
                                                saveDataToFirestore(trackId, "tracks");
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // Handle error getting albums
                        }
                    }
                });
    }


    //N: lấy id từ assets -> theo key (loại id) và value (là id)
    private void readJsonFromAssets(String filename) {
        AssetManager assetManager = getAssets();
        try (InputStream inputStream = assetManager.open(filename);
             Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Long>>>(){}.getType();
            Map<String, List<Long>> data = gson.fromJson(reader, type);

            if (data != null && !data.isEmpty()) {
                for (Map.Entry<String, List<Long>> entry : data.entrySet()) {
                    String key = entry.getKey();
                    List<Long> values = entry.getValue();

                    for (long value : values) {
                        String apiId = String.valueOf(value);
                        //N: Gọi endpoint từ API dựa trên loại id và id
                        if (key.equals("albums")) {
                            readDataFormAPI(apiId, "albums");
                        } else if (key.equals("artists")) {
                            readDataFormAPI(apiId, "artists");
                        } else if (key.equals("tracks")) {
                            readDataFormAPI(apiId, "tracks");
                        } else if (key.equals("playlists")) {
                            readDataFormAPI(apiId, "playlists");
                        }

                    }
                }
            }
        } catch (IOException e) {
            //N: show lỗi tại log
            Log.e("readJsonFromAssets", "Error: " + e.getMessage());
        }
    }

    //N: gọi các endpoint từ API bằng Retrofit truyền vào id và loại id
    private void readDataFormAPI(String apiId, String dataType) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DeezerApiService apiService = retrofit.create(DeezerApiService.class);

        switch (dataType) {
            case "albums":
                Call<Album> callAlbum = apiService.getAlbumData(apiId);
                callAlbum.enqueue(new Callback<Album>() {
                    @Override
                    public void onResponse(Call<Album> call, Response<Album> response) {
                        if (response.isSuccessful()) {
                            Album album = response.body();
                            if (album != null) {
                                saveDataToFirestore(album, "albums");
                                //getArtistAndTrackFromAlbum(album);
                                Log.d("readDataAlbum", "Lấy Album data từ api OK");
                            } else {
                                Log.e("readDataAlbum", "Album data là null");
                            }
                        } else {
                            Log.e("readDataAlbum", "Không thể truy xuất Album data");
                        }
                    }

                    @Override
                    public void onFailure(Call<Album> call, Throwable t) {
                        Log.e("readDataAlbum", "Error khi lấy Album data: " + t.getMessage());
                    }
                });
                break;
            case "artists":
                Call<Artist> callArtist = apiService.getArtistData(apiId);
                callArtist.enqueue(new Callback<Artist>() {
                    @Override
                    public void onResponse(Call<Artist> call, Response<Artist> response) {
                        if (response.isSuccessful()) {
                            Artist artist = response.body();
                            if (artist != null) {
                                saveDataToFirestore(artist, "artists");
                                Log.d("readDataArtist", "Lấy Artist data từ api OK");
                            } else {
                                Log.e("readDataArtist", "Artist data là null");
                            }
                        } else {
                            Log.e("readDataArtist", "Không thể truy xuất Artist data");
                        }
                    }

                    @Override
                    public void onFailure(Call<Artist> call, Throwable t) {
                        Log.e("readDataArtist", "Error khi lấy Artist data: " + t.getMessage());
                    }
                });
                break;
            case "tracks":
                Call<Track> callTrack = apiService.getTrackData(apiId);
                callTrack.enqueue(new Callback<Track>() {
                    @Override
                    public void onResponse(Call<Track> call, Response<Track> response) {
                        if (response.isSuccessful()) {
                            Track track = response.body();
                            if (track != null) {
                                saveDataToFirestore(track, "tracks");
                                Log.d("readDataTrack", "Lấy Track data từ api Ok");
                            } else {
                                Log.e("readDataTrack", "Track data is null");
                            }
                        } else {
                            Log.e("readDataTrack", "Không thể truy xuất Track data " + apiId);
                            Log.e("loi khi lay bai hat", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Track> call, Throwable t) {
                        Log.e("readDataTrack", "Error fetching track data: " + t.getMessage());
                    }
                });
                break;
            case "playlists":
                Call<Playlist> callPlaylist = apiService.getPlaylistData(apiId);
                callPlaylist.enqueue(new Callback<Playlist>() {
                    @Override
                    public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                        if(response.isSuccessful()) {
                            Playlist playlist = response.body();
                            if(playlist != null){
                                saveDataToFirestore(playlist, "playlists");
                                //getArtistAndTrackFromPlaylist(playlist);
                                Log.d("readDataPlaylist", "Lấy Playlist data từ api OK");
                            } else{
                                Log.e("readDataPlaylist", "Playlist data là null");
                            }
                        } else {
                            Log.e("readDataPlaylist", "Không thể truy xuất Playlist data");
                        }
                    }
                    @Override
                    public void onFailure(Call<Playlist> call, Throwable t) {
                        Log.e("readDataPlaylist", "Error khi lấy Playlist: " + t.getMessage());
                    }
                });
                break;
            default:
                return;
        }
    }

    private void getArtistAndTrackFromPlaylist(Playlist playlist) {
        TrackData trackData = playlist.getTrackData();
        if (trackData != null) {
            List<Track> tracks = trackData.getData();
            if (tracks != null && !tracks.isEmpty()) {
                for (Track track : tracks) {
                    Log.d("TrackID: ", String.valueOf(track.getId()));
                    readDataFormAPI(String.valueOf(track.getId()), "tracks");
                }
            }
        }
// xu ly luu artist
    }

    //N:  xử lý dữ liệu trả về từ API
    private void getArtistAndTrackFromAlbum(Album album) {
        /*GenreData genreData = album.getGenreData();
        if (genreData != null) {
            List<Genre> genres = genreData.getData();
            if (genres != null && !genres.isEmpty()) {
                for (Genre genre : genres) {
                    Log.d("Genre: ", genre.toString());
                }
            }
        }
        List<Contributor> contributors = album.getContributors();
        if (contributors != null && !contributors.isEmpty()) {
            // Xử lý dữ liệu contributors ở đây
            for (Contributor contributor : contributors) {
                Log.d("Contributor: ", contributor.toString());
            }
        }*/
        TrackData trackData = album.getTrackData();
        if (trackData != null) {
            List<Track> tracks = trackData.getData();
            if (tracks != null && !tracks.isEmpty()) {
                for (Track track : tracks) {
                    Log.d("TrackID: ", String.valueOf(track.getId()));
                    readDataFormAPI(String.valueOf(track.getId()), "tracks");
                }
            }
        }
        readDataFormAPI(String.valueOf(album.getArtist().getId()), "artists");

    }

    //N:  xử lý dữ liệu trả về từ API
    private void handleArtistResponse(Artist artist) {
        // Xử lý dữ liệu artist ở đây
        saveDataToFirestore(artist, "artists");
    }

    //N:  xử lý dữ liệu trả về từ API
    private void handleTrackResponse(Track track) {
        List<Contributor> contributors = track.getContributors();
        if (contributors != null && !contributors.isEmpty()) {
            // Xử lý dữ liệu contributors ở đây
            for (Contributor contributor : contributors) {
                Log.d("Contributor: ", contributor.toString());
            }
        }
        saveDataToFirestore(track, "tracks");
    }

    //N: Lấy ID từ đối tượng phản hồi dựa trên loại của nó
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

    //N: lưu data vào firebase firestore
    private void saveDataToFirestore(Object dataObject, String collectionName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(collectionName);

        long objectId = getDataId(dataObject);

        if (objectId != 0) {
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
        } else {
            Log.d("Firestore", "objectId là 0, bỏ qua lưu trữ" + dataObject + "  " + collectionName);
        }


    }

    private void addControls() {
        //tvHomeSongNV = findViewById(R.id.homeSongNameView);
        tvTitle = findViewById(R.id.title);
        rvListAlbum = findViewById(R.id.rv_listAlbums);
        rvListArtist = findViewById(R.id.rv_listArtists);
        rvListGenreKpop = findViewById(R.id.rv_listGenreKpop);
        rv_listPlaylists = findViewById(R.id.rv_listPlaylists);
        rv_listAlbumsRCM = findViewById(R.id.rv_listAlbumsRCM);
        btnHome = findViewById(R.id.btnHome);
        btnLibrary = findViewById(R.id.btnLibrary);
        btnProfile = findViewById(R.id.btnProfile);
        btnSearch = findViewById(R.id.btnSearch);

    }

}