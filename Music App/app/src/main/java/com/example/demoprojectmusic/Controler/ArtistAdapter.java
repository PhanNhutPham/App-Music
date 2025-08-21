package com.example.demoprojectmusic.Controler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.demoprojectmusic.Model.Artist;
import com.example.demoprojectmusic.R;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder>{

    private List<Artist> artistList;

    private View.OnClickListener onClickListener;
    private OnItemClickListener listener;

    public ArtistAdapter(List<Artist> artistList) {
        this.artistList = artistList;
    }
    public interface OnItemClickListener {
        void onItemClick (Artist artist);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    @NonNull
    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.ViewHolder holder, int position) {
        Artist artist = artistList.get(position);
        holder.name_artist.setText(artist.getName());
        Glide.with(holder.img_artist.getContext()).load(artist.getPictureXL()).into(holder.img_artist);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onItemClick(artist);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_artist;
        TextView name_artist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_artist = itemView.findViewById(R.id.img_artist);
            name_artist = itemView.findViewById(R.id.name_artist);
        }
    }
}
