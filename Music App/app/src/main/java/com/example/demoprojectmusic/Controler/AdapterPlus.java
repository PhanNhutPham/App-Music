package com.example.demoprojectmusic.Controler;
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
import com.example.demoprojectmusic.Model.Album;
import com.example.demoprojectmusic.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterPlus extends RecyclerView.Adapter<AdapterPlus.ViewHolder> implements Filterable{
    private Context context;
    private ArrayList<Album> originalList;
    private ArrayList<Album> filteredList;
    private Filter filter;

    public interface OnAlbumClickListener {
        void onAlbumClick(Album album);
    }
    private OnAlbumClickListener albumClickListener;

    public void setAlbumClickListener(OnAlbumClickListener listener) {
        this.albumClickListener = listener;
    }
    public AdapterPlus(Context context, ArrayList<Album> albumList) {
        this.context = context;
        this.originalList = new ArrayList<>(albumList);
        this.filteredList = new ArrayList<>(albumList);
        this.filter = new AlbumFilter();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = filteredList.get(position);

        // Set album information into the views within the item
        holder.title_album.setText(album.getTitle());
        holder.artist_album.setText("by " + (album.getArtist() != null ? album.getArtist().getName() : "Unknown"));

        // Load album image into the ImageView using Glide (if available)
        Glide.with(context).load(album.getCover_xl()).into(holder.img_album);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class AlbumFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Album> filteredAlbums = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredAlbums.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Album album : originalList) {
                    if (album.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredAlbums.add(album);
                    }
                }
            }

            results.count = filteredAlbums.size();
            results.values = filteredAlbums;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList.clear();
            filteredList.addAll((ArrayList<Album>) results.values);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_album;
        TextView title_album, artist_album;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_album = itemView.findViewById(R.id.img_album);
            title_album = itemView.findViewById(R.id.title_album);
            artist_album = itemView.findViewById(R.id.artist_album);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && albumClickListener != null) {
                    albumClickListener.onAlbumClick(filteredList.get(position));
                }
            });
        }
    }
}
