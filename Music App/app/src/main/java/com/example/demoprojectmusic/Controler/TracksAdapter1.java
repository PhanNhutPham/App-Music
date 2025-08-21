package com.example.demoprojectmusic.Controler;
import android.annotation.SuppressLint;

import android.content.Context;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.Filter;

import android.widget.Filterable;

import android.widget.ImageView;

import android.widget.TextView;



import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;



import com.bumptech.glide.Glide;

import com.example.demoprojectmusic.Model.Track;

import com.example.demoprojectmusic.R;



import java.util.ArrayList;

import java.util.List;


public class TracksAdapter1 extends RecyclerView.Adapter<TracksAdapter1.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Track> originalList;
    private ArrayList<Track> filteredList;
    ArrayList<Track> fullList;
    private Filter filter;

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Track track);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }
    public TracksAdapter1(Context context, ArrayList<Track> tracksList) {
        this.context = context;
        this.originalList = new ArrayList<>(tracksList);
        this.filteredList = new ArrayList<>(tracksList);
        this.fullList = new ArrayList<>(tracksList);
        this.filter = new TrackFilter();
    }

    public TracksAdapter1(List<Track> artistList) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tracks_from_album, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = filteredList.get(position);

        // Set album information into the views within the item
        holder.title_album.setText(track.getTitle());
        holder.artist_album.setText("by " + (track.getArtist() != null && track.getArtist().getName() != null ? track.getArtist().getName() : "Unknown"));
        // Load album image into the ImageView using Glide (if available)
        //Glide.with(context).load(track.getAlbum().getCover_xl()).into(holder.img_album);

        if (track.getArtist() != null && track.getArtist().getName() != null) {
            holder.artist_album.setText("by " + track.getArtist().getName());
        } else {
            holder.artist_album.setText("by Unknown");
        }

        // Load album image into the ImageView using Glide (if available)
        if (track.getAlbum() != null && track.getAlbum().getCover_xl() != null) {
            Glide.with(context).load(track.getAlbum().getCover_xl()).into(holder.img_album);
        } else {
            // Handle the case where the album object or the cover_xl URL is null
            // For example, you can set a placeholder image or hide the ImageView
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }



    private class TrackFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Track> filteredTracks = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredTracks.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Track track : fullList) {
                    if (track.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredTracks.add(track);
                    }
                }
            }

            results.count = filteredTracks.size();
            results.values = filteredTracks;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredList.clear();
            if (constraint != null && constraint.length() > 0) {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Track track : fullList) {
                    if (track.getTitle() != null && track.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(track);
                    }
                }
            } else {
                filteredList.addAll(fullList);
            }
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_album;
        TextView title_album, artist_album;

        public ViewHolder(View itemView) {
            super(itemView);
            img_album = itemView.findViewById(R.id.imgTrack);
            title_album = itemView.findViewById(R.id.title_track);
            artist_album = itemView.findViewById(R.id.name_artist);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener.onItemClick(filteredList.get(position));
                }
            });
        }
    }
}
