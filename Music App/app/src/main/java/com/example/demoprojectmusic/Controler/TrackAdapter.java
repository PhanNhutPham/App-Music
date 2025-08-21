package com.example.demoprojectmusic.Controler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demoprojectmusic.Model.Track;
import com.example.demoprojectmusic.R;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private List<Track> trackList;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener listener;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tracks_from_album, parent, false);
        return new ViewHolder(view);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public TrackAdapter(List<Track> trackList) {
        this.trackList = trackList;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = trackList.get(position);

        // Set album information into the views within the item
        holder.title_track.setText(track.getTitle());
        holder.name_artist.setText((track.getArtist() != null ? track.getArtist().getName() : "Unknown"));

        Glide.with(holder.itemView.getContext()).load(track.getAlbum().getCover_xl()).into(holder.imgTrack);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int clickedPosition = holder.getAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClick(clickedPosition);
                    }
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
        ImageView imgTrack;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_track = itemView.findViewById(R.id.title_track);
            name_artist = itemView.findViewById(R.id.name_artist);
            imgTrack = itemView.findViewById(R.id.imgTrack);
        }
    }
}
