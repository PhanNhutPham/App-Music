package com.example.demoprojectmusic.Controler;

import android.content.Context;
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

public class adapterAD extends RecyclerView.Adapter<adapterAD.ViewHolder> {
    private static List<Album> albumList;
    private Context context;
    public interface OnAlbumClickListener {
        void onAlbumClick(Album album);
    }
    private static OnAlbumClickListener albumClickListener;

    public void setAlbumClickListener(adapterAD.OnAlbumClickListener listener) {
        this.albumClickListener = listener;
    }
    public adapterAD(List<Album> albumList, Context context) {
        this.albumList = albumList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_ad, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.titleTextView.setText(album.getTitle());
        Glide.with(context).load(album.getCover_xl()).into(holder.imgV);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public ImageView imgV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgV = itemView.findViewById(R.id.adIMG);
            titleTextView = itemView.findViewById(R.id.adTitleTextView);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && albumClickListener != null) {
                    albumClickListener.onAlbumClick(albumList.get(position));
                }
            });
        }
    }
}