package com.example.git.listeners;

import com.example.git.models.MovieShow;

public interface WatchlistListener {

    void onMovieShowClicked(MovieShow movieShow);

    void removeMovieShowFromWatchlist(MovieShow movieShow, int position);
}
