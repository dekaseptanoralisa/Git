package com.example.git.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.git.R;
import com.example.git.adapters.MovieShowsAdapter;
import com.example.git.databinding.ActivityMainBinding;
import com.example.git.models.MovieShow;
import com.example.git.viewmodels.MostPopularMovieShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private MostPopularMovieShowsViewModel viewModel;
    private List<MovieShow> movieShows = new ArrayList<>();
    private MovieShowsAdapter movieShowsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization(){
        activityMainBinding.movieShowsRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularMovieShowsViewModel.class);
        movieShowsAdapter = new MovieShowsAdapter(movieShows);
        activityMainBinding.movieShowsRecyclerView.setAdapter(movieShowsAdapter);
        getMostPopularMovieShows();
    }

    private void getMostPopularMovieShows() {
        activityMainBinding.setIsloading(true);
        viewModel.getMostPopularMovieShows(0).observe(this, mostPopularMovieShowsResponse ->{
            activityMainBinding.setIsloading(false);
            if (mostPopularMovieShowsResponse != null){
                if (mostPopularMovieShowsResponse.getMovieShows() != null){
                    movieShows.addAll(mostPopularMovieShowsResponse.getMovieShows());
                    movieShowsAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}