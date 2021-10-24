package com.example.git.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.git.repositories.MostPopularMovieShowsRepository;
import com.example.git.responses.MovieShowResponse;

public class MostPopularMovieShowsViewModel extends ViewModel {
    private MostPopularMovieShowsRepository mostPopularMovieShowsRepository;

    public  MostPopularMovieShowsViewModel(){
        mostPopularMovieShowsRepository = new MostPopularMovieShowsRepository();
    }

    public LiveData<MovieShowResponse> getMostPopularMovieShows(int page){
        return mostPopularMovieShowsRepository.getMostPopularMovieShows(page);
    }
}
