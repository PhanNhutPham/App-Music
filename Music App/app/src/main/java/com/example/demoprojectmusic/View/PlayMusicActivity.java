package com.example.demoprojectmusic.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.demoprojectmusic.Controler.TrackAdapter;
import com.example.demoprojectmusic.Controler.TrackListPlayAdapter;
import com.example.demoprojectmusic.Model.Track;
import com.example.demoprojectmusic.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayMusicActivity extends AppCompatActivity {
    TextView title_album, title_track, name_artist, tvTime, tvTime1;
    ImageView cover_album;
    RecyclerView rcv_listTracks;
    SeekBar seekBar;
    List<Track> trackList;
    int selectedTrackIndex;
    ImageButton btnPlay, btnNextTrack, btnPreTrack, btnRepeat ,btnPlaylist, btnHeart, btnAddPlaylist, btnClose;
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 15000;
    private Handler hdlr = new Handler();
    private MediaPlayer mPlayer;

    private boolean isRepeatMode = false;
    private boolean isAddPlaylist = false;
    private  boolean isAddFavorite = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        addControls();

        // Lấy track_id từ Intent



        addEvents();


    }

    private void getListTrack(long[] trackIds) {
        List<Track> tracks = new ArrayList<>();
        AtomicInteger completedTasks = new AtomicInteger();

        for (long id : trackIds) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("tracks")
                    .document(String.valueOf(id))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Track track = document.toObject(Track.class);
                                if (track != null) {
                                    tracks.add(track);
                                }
                            } else {
                                Log.e("Firestore", "Error getting album document: ", task.getException());
                            }
                        }

                        completedTasks.getAndIncrement();

                        if (completedTasks.get() == trackIds.length) {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                            rcv_listTracks.setLayoutManager(layoutManager);
                            TrackListPlayAdapter trackAdapter = new TrackListPlayAdapter(tracks);
                            rcv_listTracks.setAdapter(trackAdapter);
Log.d("nenenene", tracks.toString());
                            // Gán danh sách bài hát cho biến trackList trong Adapter
                            trackAdapter.trackList = tracks;
                        }
                    });
        }
    }
    public void changeToTrackAtIndex(int index) {
        Track selectedTrack = trackList.get(index); // Lấy bài hát được chọn từ danh sách
        Log.e("selectedTrack", String.valueOf(selectedTrack));
        // Lấy thông tin của bài hát được chọn và phát nhạc
        getInfoAndPlay(selectedTrack.getId(), null);
    }



    private void addEvents() {
        long trackId = getIntent().getLongExtra("track_id", -1);
        long[] trackIds = getIntent().getLongArrayExtra("track_ids");
        if (trackIds != null) {
            getListTrack(trackIds);
        }
        String type = getIntent().getStringExtra("type");
        getInfoAndPlay(trackId, type);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng Activity hiện tại khi nút được click
                finish();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Nếu người dùng đã thay đổi vị trí của seekbar, ta cập nhật vị trí của nhạc
                    mPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không có hành động cụ thể khi người dùng bắt đầu chạm vào seekbar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Cập nhật vị trí của nhạc
                int progress = seekBar.getProgress();
                mPlayer.seekTo(progress);

                // Cập nhật thời gian trên TextView
                String currentTime = createTimeLabel(progress);
                String totalTime = createTimeLabel(mPlayer.getDuration());
                tvTime.setText(currentTime);
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null) {
                    if (mPlayer.isPlaying()) {
                        // Nếu nhạc đang phát, ta tạm dừng
                        mPlayer.pause();
                        stopAnimation();
                        btnPlay.setImageResource(R.drawable.ic_play_arrow); // Đổi icon sang play
                    } else {
                        // Nếu nhạc đang tạm dừng hoặc chưa phát, ta tiếp tục phát
                        mPlayer.start();
                        startAnimation();
                        btnPlay.setImageResource(R.drawable.baseline_pause_24);
                    }
                } else {
                    // Nếu mPlayer không được khởi tạo, ta khởi tạo và bắt đầu phát nhạc
                    getInfoAndPlay(trackId, null);
                }
            }
        });

        btnNextTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tính toán vị trí mới của nhạc
                int currentPosition = mPlayer.getCurrentPosition();
                int newPosition = currentPosition + 2000; // Tua thời gian nhanh hơn 2 giây

                // Kiểm tra và giới hạn vị trí mới nằm trong khoảng hợp lệ
                if (newPosition > mPlayer.getDuration()) {
                    newPosition = mPlayer.getDuration();
                }

                // Cập nhật vị trí của nhạc và seekbar
                mPlayer.seekTo(newPosition);
                seekBar.setProgress(newPosition);

                // Cập nhật thời gian trên TextView
                String currentTime = createTimeLabel(newPosition);
//                String totalTime = createTimeLabel(mPlayer.getDuration());
                tvTime.setText(currentTime);
            }
        });

        btnPreTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tính toán vị trí mới của nhạc
                int currentPosition = mPlayer.getCurrentPosition();
                int newPosition = currentPosition - 2000; // Tua thời gian nhanh hơn 2 giây

                // Kiểm tra và giới hạn vị trí mới nằm trong khoảng hợp lệ
                if (newPosition > mPlayer.getDuration()) {
                    newPosition = mPlayer.getDuration();
                }

                // Cập nhật vị trí của nhạc và seekbar
                mPlayer.seekTo(newPosition);
                seekBar.setProgress(newPosition);

                // Cập nhật thời gian trên TextView
                String currentTime = createTimeLabel(newPosition);
//                String totalTime = createTimeLabel(mPlayer.getDuration());
                tvTime.setText(currentTime);
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đảo ngược trạng thái repeat mode
                isRepeatMode = !isRepeatMode;

                // Cập nhật hình ảnh của repeatModeBtn dựa trên trạng thái
                if (isRepeatMode) {
                    btnRepeat.setImageResource(R.drawable.baseline_repeat_one_24);
                } else {
                    btnRepeat.setImageResource(R.drawable.baseline_repeat_24);
                }
            }
        });

        btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                //changeToNextTrack();
               isAddPlaylist = !isAddPlaylist;

               // Cập nhật hình ảnh của repeatModeBtn dựa trên trạng thái
               if (isAddPlaylist) {
                   btnAddPlaylist.setImageResource(R.drawable.baseline_playlist_add_check_24);
               } else {
                   btnAddPlaylist.setImageResource(R.drawable.baseline_playlist_add_24);
               }

            }
        });

        btnHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đảo ngược trạng thái repeat mode
                isAddFavorite = !isAddFavorite;

                // Cập nhật hình ảnh của repeatModeBtn dựa trên trạng thái
                if (isAddFavorite) {
                    btnHeart.setImageResource(R.drawable.baseline_favorite_24);
                } else {
                    btnHeart.setImageResource(R.drawable.baseline_favorite_border_24);
                }
            }
        });


    }


    private void getInfoAndPlay (long trackId, String type){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tracks")
                .document(String.valueOf(trackId))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Track track = document.toObject(Track.class);
                            if (track != null) {
                                String titleType = type;
                                if (titleType == null) titleType = "PLAYLIST";
                                title_album.setText("ĐANG PHÁT TỪ  " + titleType +"\n"+ track.getAlbum().getTitle());
                                if (track.getAlbum().getCover_xl() != null) {
                                    Glide.with(this).load(track.getAlbum().getCover_xl()).into(cover_album);
                                }
                                title_track.setText(track.getTitle());
                                name_artist.setText(track.getArtist().getName());
                                final String audioUrl = track.getPreview();

                                playAudio(audioUrl);


                            }
                        }
                    } else {
                        Log.e("Firebase", "Error getting track document: ", task.getException());
                    }
                });
    }
    private int pausedPosition = 0;
    private void playAudio(String audioUrl) {
        if (mPlayer != null && mPlayer.isPlaying()) {
            // Nếu nhạc đang phát, ta dừng phát nhạc và thay đổi drawable của nút playBtn thành "play"
            mPlayer.pause();
            btnPlay.setImageResource(R.drawable.ic_play_arrow);
            stopAnimation();

        } else {
            if (mPlayer != null && !mPlayer.isPlaying()) {
                // Nếu có mPlayer và nhạc đã tạm dừng, ta tiếp tục phát nhạc
                mPlayer.start();
                btnPlay.setImageResource(R.drawable.baseline_pause_24);
                startAnimation();

                hdlr.post(new UpdateSeekBar()); // Cập nhật seekbar
            } else {
                mPlayer = new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {
                    mPlayer.setDataSource(audioUrl);
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Bắt đầu cập nhật seekbar
                seekBar.setMax(mPlayer.getDuration());
                hdlr.post(new UpdateSeekBar()); // Cập nhật seekbar
                // Cập nhật drawable của nút playBtn thành "pause"
                btnPlay.setImageResource(R.drawable.baseline_pause_24);
                startAnimation();
            }     }
    }
   /*private void playAudio(String audioUrl) {
       if (mPlayer == null) {
           mPlayer = new MediaPlayer();
           mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
           mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
               @Override
               public void onCompletion(MediaPlayer mp) {
                   changeToNextTrack();
               }
           });
       } else {
           mPlayer.reset(); // Reset the MediaPlayer to its uninitialized state
       }

       try {
           mPlayer.setDataSource(audioUrl);
           mPlayer.prepare();
           mPlayer.start();

           // Bắt đầu cập nhật seekbar
           seekBar.setMax(mPlayer.getDuration());
           hdlr.post(new UpdateSeekBar()); // Cập nhật seekbar

           // Cập nhật drawable của nút playBtn thành "pause"
           btnPlay.setImageResource(R.drawable.baseline_pause_24);
           startAnimation();
       } catch (IOException | IllegalStateException e) {
           e.printStackTrace();
           throw new RuntimeException(e);
       }
   }*/


    /*private void changeToNextTrack() {
        if (trackList != null && selectedTrackIndex < trackList.size() - 1) {
            selectedTrackIndex++; // Increment the current track index

            // Check if the new index is within the bounds of the trackList
            if (selectedTrackIndex >= 0 && selectedTrackIndex < trackList.size()) {
                // Get the next track from the trackList
                Track nextTrack = trackList.get(selectedTrackIndex);


                title_track.setText(nextTrack.getTitle());
                name_artist.setText(nextTrack.getArtist().getName());
                // Play the next track
                getInfoAndPlay(nextTrack.getId(), null);
            }
        }
    }*/


    private void addControls() {
        title_album = findViewById(R.id.title_album);
        name_artist = findViewById(R.id.name_artist);
        cover_album = findViewById(R.id.cover_album);
        title_track = findViewById(R.id.title_track);

        btnPlay = findViewById(R.id.btnPlay);
        btnNextTrack = findViewById(R.id.btnNextTrack);
        btnPreTrack = findViewById(R.id.btnPreTrack);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnHeart = findViewById(R.id.btnHeart);
        btnAddPlaylist = findViewById(R.id.btnAddPlaylist);
        btnClose = findViewById(R.id.playerCloseBtn);
        seekBar = findViewById(R.id.seekBar);

        tvTime = findViewById(R.id.tvTime);
        tvTime1 = findViewById(R.id.tvTime1);

        rcv_listTracks = findViewById(R.id.rcv_listTracks);

    }

    public class UpdateSeekBar implements Runnable {
        @Override
        public void run() {
            int currentPosition = mPlayer.getCurrentPosition();
            seekBar.setProgress(currentPosition);
            int totalDuration = mPlayer.getDuration();
            int remainingTime = currentPosition  ;


            // Cập nhật thời gian trên TextView
            String remainingTimeString = createTimeLabel(remainingTime);
            String totalDurationString = "00:30";
            tvTime.setText(remainingTimeString);
            tvTime1.setText(totalDurationString);

            // Lặp lại việc cập nhật seekbar sau một khoảng thời gian nhất định
            hdlr.postDelayed(this, 100); // Cập nhật seekbar mỗi giây

            // Kiểm tra nếu nhạc đã phát hết
            if (currentPosition >= totalDuration) {
                if (isRepeatMode) {
                    // Nếu đang ở chế độ repeat, ta quay lại thời điểm ban đầu và chơi lại nhạc
                    mPlayer.seekTo(0);
                    mPlayer.start();
                } else {
                    // Nếu không, thực hiện các hành động khi nhạc kết thúc, ví dụ như dừng phát nhạc
                    mPlayer.pause();
                    btnPlay.setImageResource(R.drawable.ic_play_arrow); // Chuyển nút play sang trạng thái play
                }
            }
        }
    }

    private String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = String.format("%02d:%02d", min, sec);
        return timeLabel;
    }

//    private void changeToNextTrack() {
//        // Lấy ID của bài hát hiện tại từ intent
//        long currentTrackId = getIntent().getLongExtra("track_id", -1);
//
//        // Gọi phương thức showTracksInAlbum để lấy danh sách các bài hát trong album
//        showTracksInAlbum(currentTrackId);
//    }

    private void startAnimation(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                cover_album.animate().rotation(360).withEndAction(this).setDuration(40000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        cover_album.animate().rotation(360).withEndAction(runnable).setDuration(40000)
                .setInterpolator(new LinearInterpolator()).start();
    }

    private void  stopAnimation(){
        cover_album.animate().cancel();
    }
}