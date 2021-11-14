package com.example.git.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.git.R;
import com.example.git.adapters.MovieShowsAdapter;
import com.example.git.databinding.ActivityMainBinding;
import com.example.git.listeners.MovieShowsListener;
import com.example.git.models.MovieShow;
import com.example.git.viewmodels.MostPopularMovieShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieShowsListener {
    private ActivityMainBinding activityMainBinding;
    private MostPopularMovieShowsViewModel viewModel;
    private List<MovieShow> movieShows = new ArrayList<>();
    private MovieShowsAdapter movieShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization(){
        activityMainBinding.movieShowsRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularMovieShowsViewModel.class);
        movieShowsAdapter = new MovieShowsAdapter(movieShows, this);
        activityMainBinding.movieShowsRecyclerView.setAdapter(movieShowsAdapter);
        activityMainBinding.movieShowsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(activityMainBinding.movieShowsRecyclerView.canScrollVertically(1)){
                    if (currentPage <= totalAvailablePages){
                        currentPage += 1;
                        getMostPopularMovieShows();;
                    }
                }

            }
        });
        getMostPopularMovieShows();
    }

    private void getMostPopularMovieShows() {
        toogleLoading();
        viewModel.getMostPopularMovieShows(currentPage).observe(this, mostPopularMovieShowsResponse ->{
            toogleLoading();
            if (mostPopularMovieShowsResponse != null){
                totalAvailablePages = mostPopularMovieShowsResponse.getPage();
                if (mostPopularMovieShowsResponse.getMovieShows() != null){
                    int oldCount = movieShows.size();
                    movieShows.addAll(mostPopularMovieShowsResponse.getMovieShows());
                    movieShowsAdapter.notifyItemRangeInserted(oldCount, movieShows.size());
                }
            }
        });
    }

    private void toogleLoading() {
        if (currentPage == 1){
            if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()){
                activityMainBinding.setIsLoading(false);
            }else{
                activityMainBinding.setIsLoading(true);
            }
        }else{
            if (activityMainBinding.getIsLoadingMore() != null && activityMainBinding.getIsLoadingMore()){
                activityMainBinding.setIsLoadingMore(false);
            }else{
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onMovieShowsClicked(MovieShow movieShow) {
        Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
        intent.putExtra("id", movieShow.getId());
        intent.putExtra("name", movieShow.getName());
        intent.putExtra("StartDate", movieShow.getStartDate());
        intent.putExtra("country", movieShow.getCountry());
        intent.putExtra("network", movieShow.getNetwork());
        intent.putExtra("status", movieShow.getStatus());
        startActivity(intent);
    }
}