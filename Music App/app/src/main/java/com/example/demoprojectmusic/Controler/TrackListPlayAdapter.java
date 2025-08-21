package com.example.demoprojectmusic.Controler;

import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demoprojectmusic.Model.Track;
import com.example.demoprojectmusic.R;
import com.example.demoprojectmusic.View.PlayMusicActivity;

import java.util.List;

public class TrackListPlayAdapter extends RecyclerView.Adapter<TrackListPlayAdapter.ViewHolder> {
    private TrackAdapter.OnItemClickListener listener;
    public List<Track> trackList;
    private PlayMusicActivity playMusicActivity;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    @NonNull
    @Override
    public TrackListPlayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tracks_in_play_music, parent, false);
        return new ViewHolder(view);
    }
    public void setOnItemClickListener(TrackAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
    public TrackListPlayAdapter(List<Track> trackList) {
        this.trackList = trackList;
    }


    @Override
    public void onBindViewHolder(@NonNull TrackListPlayAdapter.ViewHolder holder, int position) {
        Track track = trackList.get(position);

        // Set album information into the views within the item
        holder.title_track.setText(track.getTitle());
        holder.name_artist.setText((track.getArtist() != null ? track.getArtist().getName() : "Unknown"));
        Glide.with(holder.itemView.getContext()).load(track.getAlbum().getCover_xl()).into(holder.imgTrack);

        // Set the animation drawable to the ImageView and start the animation
        holder.anim.setBackgroundResource(R.drawable.play_music_anim);
        AnimationDrawable animation = (AnimationDrawable) holder.anim.getBackground();
        animation.start();

        // Set item click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION && playMusicActivity != null) {
                    playMusicActivity.changeToTrackAtIndex(currentPosition);
                    Toast.makeText(view.getContext(), "ok", Toast.LENGTH_SHORT).show();
                    Log.e("selectedTrack", String.valueOf(currentPosition));
                } else {
                    // Xử lý trong trường hợp đối tượng là null hoặc vị trí không hợp lệ
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_track;
        TextView name_artist;
        ImageView imgTrack, anim;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            anim = itemView.findViewById(R.id.anim);
            title_track = itemView.findViewById(R.id.title_track);
            name_artist = itemView.findViewById(R.id.name_artist);
            imgTrack = itemView.findViewById(R.id.imgTrack);

        }
    }
}
