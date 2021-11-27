package com.example.git.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.git.R;
import com.example.git.databinding.ItemContainerMovieShowBinding;
import com.example.git.listeners.WatchlistListener;
import com.example.git.models.MovieShow;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.MovieShowViewHolder>{
    private List<MovieShow> movieShows;
    private LayoutInflater layoutInflater;
    private WatchlistListener watchlistListener;

    public WatchlistAdapter(List<MovieShow> movieShows, WatchlistListener watchlistListener) {

        this.movieShows = movieShows;
        this.watchlistListener = watchlistListener;
    }

    @NonNull
    @Override
    public MovieShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerMovieShowBinding movieShowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_movie_show, parent, false);
        return new MovieShowViewHolder(movieShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieShowViewHolder holder, int position) {
        holder.bindMovieShow(movieShows.get(position));
    }

    @Override
    public int getItemCount() {
        return movieShows.size();
    }

    class MovieShowViewHolder extends RecyclerView.ViewHolder{

        private ItemContainerMovieShowBinding itemContainerMovieShowBinding;

        public MovieShowViewHolder(ItemContainerMovieShowBinding itemContainerMovieShowBinding){
            super(itemContainerMovieShowBinding.getRoot()) ;
            this.itemContainerMovieShowBinding = itemContainerMovieShowBinding;
        }

        public void bindMovieShow(MovieShow movieShow){
            itemContainerMovieShowBinding.setMovieShow(movieShow);
            itemContainerMovieShowBinding.executePendingBindings();
            itemContainerMovieShowBinding.getRoot().setOnClickListener(view -> watchlistListener.onMovieShowClicked(movieShow));
            itemContainerMovieShowBinding.imageDelete.setOnClickListener(view -> watchlistListener.removeMovieShowFromWatchlist(movieShow, getAdapterPosition()));
            itemContainerMovieShowBinding.imageDelete.setVisibility(View.VISIBLE);
        }
    }
 }
