package com.example.git.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.git.repositories.SearchMovieShowRepository;
import com.example.git.responses.MovieShowResponse;

public class SearchViewModel extends ViewModel {

    private SearchMovieShowRepository searchMovieShowRepository;

    public SearchViewModel(){
        searchMovieShowRepository = new SearchMovieShowRepository();
    }

    public LiveData<MovieShowResponse> searchMovieShow(String query, int page){
        return searchMovieShowRepository.searchMovieShow(query, page);
    }
}
