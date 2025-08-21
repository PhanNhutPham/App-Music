package com.example.demoprojectmusic.Controler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demoprojectmusic.Model.Album;
import com.example.demoprojectmusic.R;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<Album> albumList;
    private OnItemClickListener listener;

    // Constructor to pass the list of albums
    public AlbumAdapter(List<Album> albums) {
        this.albumList = albums;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }
    public interface OnItemClickListener {
        void onItemClick(Album album);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
        Album album = albumList.get(position);

        // Set album information into the views within the item
        holder.title_album.setText(album.getTitle());
        holder.artist_album.setText("by " + (album.getArtist() != null ? album.getArtist().getName() : "Unknown"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(album);
                }
            }
        });
        // Load album image into the ImageView using Glide (if available)
        Glide.with(holder.itemView.getContext()).load(album.getCover_xl()).into(holder.img_album);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_album;
        TextView title_album, artist_album;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_album = itemView.findViewById(R.id.img_album);
            title_album = itemView.findViewById(R.id.title_album);
            artist_album = itemView.findViewById(R.id.artist_album);
        }
    }
}
