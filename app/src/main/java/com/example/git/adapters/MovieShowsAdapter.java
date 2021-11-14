package com.example.git.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.git.R;
import com.example.git.databinding.ItemContainerMovieShowBinding;
import com.example.git.listeners.MovieShowsListener;
import com.example.git.models.MovieShow;

import java.util.List;

public class MovieShowsAdapter extends RecyclerView.Adapter<MovieShowsAdapter.MovieShowViewHolder>{
    private List<MovieShow> movieShows;
    private LayoutInflater layoutInflater;
    private MovieShowsListener movieShowsListener;

    public MovieShowsAdapter(List<MovieShow> movieShows, MovieShowsListener movieShowsListener) {

        this.movieShows = movieShows;
        this.movieShowsListener = movieShowsListener;
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
            itemContainerMovieShowBinding.getRoot().setOnClickListener(view -> movieShowsListener.onMovieShowsClicked(movieShow));

        }
    }
}
