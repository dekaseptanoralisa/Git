package com.example.git.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.git.database.MovieShowsDatabase;
import com.example.git.models.MovieShow;
import com.example.git.repositories.MovieDetailsRepository;
import com.example.git.responses.MovieDetailsResponse;

import io.reactivex.Completable;

public class MovieDetailsViewModel extends AndroidViewModel {

    private MovieDetailsRepository movieDetailsRepository;
    private MovieShowsDatabase movieShowsDatabase;

    public MovieDetailsViewModel(@NonNull Application application){
        super(application);
        movieDetailsRepository = new MovieDetailsRepository();
        movieShowsDatabase = MovieShowsDatabase.getMovieShowsDatabase(application);
    }

    public LiveData<MovieDetailsResponse> getMovieDetails(String movieId) {
        return movieDetailsRepository.getMovieDetails(movieId);
    }

    public Completable addToWatchlist(MovieShow movieShow){
        return  movieShowsDatabase.movieShowDao().addToWatchlist(movieShow);
    }
}
