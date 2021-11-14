package com.example.git.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.git.repositories.MovieDetailsRepository;
import com.example.git.responses.MovieDetailsResponse;

public class MovieDetailsViewModel extends ViewModel {

    private MovieDetailsRepository movieDetailsRepository;

    public MovieDetailsViewModel(){
        movieDetailsRepository = new MovieDetailsRepository();
    }

    public LiveData<MovieDetailsResponse> getMovieDetails(String movieId) {
        return movieDetailsRepository.getMovieDetails(movieId);
    }
}
