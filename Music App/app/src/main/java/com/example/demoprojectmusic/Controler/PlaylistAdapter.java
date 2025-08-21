package com.example.demoprojectmusic.Controler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demoprojectmusic.Model.Album;
import com.example.demoprojectmusic.Model.Playlist;
import com.example.demoprojectmusic.R;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private List<Playlist> playlists;
    private OnItemClickListener listener;

    public PlaylistAdapter(List<Playlist> playlistList) {
        this.playlists = playlistList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }
    public void setOnItemClickListener(PlaylistAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);

        holder.title_playlist.setText(playlist.getTitle());
        holder.nb_track.setText(playlist.getNumberOfTracks() + " tracks");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(playlist);
                    //Log.d("JFVBU", String.valueOf(playlist));
                }
            }
        });

        Glide.with(holder.itemView.getContext()).load(playlist.getPictureExtraLarge()).into(holder.img_playlist);

    }


    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Playlist playlist);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_playlist;
        TextView title_playlist, nb_track;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_playlist = itemView.findViewById(R.id.img_album);
            title_playlist = itemView.findViewById(R.id.title_album);
            nb_track = itemView.findViewById(R.id.artist_album);
        }

    }
}
