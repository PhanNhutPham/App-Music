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
import com.example.demoprojectmusic.Model.Playlist;
import com.example.demoprojectmusic.R;

import java.util.ArrayList;
import java.util.List;

public class PlayListAdapter1 extends RecyclerView.Adapter<PlayListAdapter1.ViewHolder> implements Filterable {
    private Context context;
    private List<Playlist> originalList;
    private List<Playlist> filteredList;
    private Filter filter;

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Playlist playlist);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }
    public PlayListAdapter1(Context context, List<Playlist> playlistList) {
        this.context = context;
        this.originalList = new ArrayList<>(playlistList);
        this.filteredList = new ArrayList<>(playlistList);
        this.filter = new PlaylistFilter();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = filteredList.get(position);
        if (playlist.getTitle() != null) {
            holder.title_album.setText(playlist.getTitle());
        } else {
            holder.title_album.setText("Unknown");
        }
        holder.artist_album.setText(playlist.getNumberOfTracks() + " tracks");

        // Cải thiện xử lý null cho hình ảnh
        if (playlist.getPictureExtraLarge() != null) {
            Glide.with(context).load(playlist.getPictureExtraLarge()).into(holder.img_album);
        } else {
            // Đặt hình ảnh thay thế hoặc ẩn ImageView
            holder.img_album.setImageResource(R.drawable.btn_3);
            // hoặc
            holder.img_album.setVisibility(View.GONE);
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

    private class PlaylistFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Playlist> filteredPlaylists = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredPlaylists.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Playlist playlist : originalList) {

                    if (playlist.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredPlaylists.add(playlist);
                    }
                }
            }

            results.count = filteredPlaylists.size();
            results.values = filteredPlaylists;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList.clear();
            if (results.values != null) {
                filteredList.addAll((List<Playlist>) results.values);
            }
            notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_album;
        TextView title_album, artist_album;

        public ViewHolder(View itemView) {
            super(itemView);
            img_album = itemView.findViewById(R.id.img_album);
            title_album = itemView.findViewById(R.id.title_album);
            artist_album = itemView.findViewById(R.id.artist_album);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener.onItemClick(filteredList.get(position));
                }
            });
        }
    }
}