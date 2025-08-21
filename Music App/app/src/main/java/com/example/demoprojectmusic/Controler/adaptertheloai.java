package com.example.demoprojectmusic.Controler;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoprojectmusic.Model.Album;
import com.example.demoprojectmusic.Model.Genre;
import com.example.demoprojectmusic.Model.GenreData;
import com.example.demoprojectmusic.R;

import java.util.List;
import java.util.Set;

public class adaptertheloai extends RecyclerView.Adapter<adaptertheloai.ViewHolder> {
    private List<Album> albumList;
    private Set<String> uniqueNames;
    private OnGenreClickListener genreClickListener;

    public adaptertheloai(List<Album> albumInfoList, Set<String> uniqueNames) {
        this.albumList = albumInfoList;
        this.uniqueNames = uniqueNames;
    }

    public void setAlbumList(List<Album> filteredAlbums) {
    }

    public interface OnGenreClickListener {
        void onGenreClick(Album album, String genreId);
    }

    public void setGenreClickListener(OnGenreClickListener listener) {
        this.genreClickListener = listener;
    }

    public String getGenreId(int position) {
        if (position >= 0 && position < albumList.size()) {
            Album album = albumList.get(position);
            GenreData genreData = album.getGenreData();
            if (genreData != null) {
                List<Genre> genres = genreData.getData();
                if (genres != null && !genres.isEmpty()) {
                    return String.valueOf(genres.get(0).getId());
                }
            }
        }
        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_theloai, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albumList.get(position);
        GenreData genreData = album.getGenreData();

        if (genreData != null) {
            List<Genre> genres = genreData.getData();

            if (genres != null && !genres.isEmpty()) {
                for (Genre genre : genres) {
                    String name = genre.getName();

                    if (name != null && !name.isEmpty() && uniqueNames.contains(name)) {
                        holder.titleTextView.setText(name);
                        break; // Thêm break để thoát khỏi vòng lặp sau khi tìm thấy name duy nhất
                    }
                }
            }
        }

        // Lấy màu xen kẻ từ tài nguyên màu
        int stripeColorIndex = position % holder.randomColors.length;
        int stripeColor = holder.itemView.getContext().getResources().getColor(holder.randomColors[stripeColorIndex]);

        // Thiết lập màu xen kẻ cho TextView
        holder.titleTextView.setBackgroundColor(stripeColor);

        if (TextUtils.isEmpty(album.getTitle())) {
            holder.titleTextView.setVisibility(View.GONE);
        } else {
            holder.titleTextView.setVisibility(View.VISIBLE);
        }

        // Lắng nghe sự kiện nhấp vào album
        holder.itemView.setOnClickListener(v -> {
            if (genreClickListener != null) {
                String genreId = getGenreId(position);
                genreClickListener.onGenreClick(album, genreId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public int[] randomColors = {
                R.color.m1, R.color.m2, R.color.m3, R.color.m4, R.color.m5, R.color.m6
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.btntheloai);
        }
    }
}