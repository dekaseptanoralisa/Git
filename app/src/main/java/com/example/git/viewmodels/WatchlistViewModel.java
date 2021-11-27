package com.example.git.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.git.database.MovieShowsDatabase;
import com.example.git.models.MovieShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchlistViewModel extends AndroidViewModel {

    private MovieShowsDatabase movieShowsDatabase;

    public WatchlistViewModel(@NonNull Application application){
        super(application);
        movieShowsDatabase = MovieShowsDatabase.getMovieShowsDatabase(application);
    }

    public Flowable<List<MovieShow>> loadWatchlist(){
        return movieShowsDatabase.movieShowDao().getWatchlist();
    }
    
    public Completable removeMovieShowFromWatchlist(MovieShow movieShow){
        return movieShowsDatabase.movieShowDao().removeFromWatchlist(movieShow);
    }
}
