package com.example.git.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.git.R;
import com.example.git.adapters.MovieShowsAdapter;
import com.example.git.databinding.ActivitySearchBinding;
import com.example.git.listeners.MovieShowsListener;
import com.example.git.models.MovieShow;
import com.example.git.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements MovieShowsListener {

    private ActivitySearchBinding activitySearchBinding;
    private SearchViewModel viewModel;
    private List<MovieShow> movieShows = new ArrayList<>();
    private MovieShowsAdapter movieShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        doInitialization();
    }


    private void doInitialization(){
        activitySearchBinding.imageBack.setOnClickListener(view -> onBackPressed());
        activitySearchBinding.movieShowsRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        movieShowsAdapter = new MovieShowsAdapter(movieShows, this);
        activitySearchBinding.movieShowsRecyclerView.setAdapter(movieShowsAdapter);
        activitySearchBinding.inputSearch.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(timer != null){
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()){
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                currentPage = 1;
                                totalAvailablePages =1;
                                searchMovieShow(editable.toString());
                            });
                        }
                    }, 800);
                } else {
                    movieShows.clear();
                    movieShowsAdapter.notifyDataSetChanged();
                }
            }
        });
        activitySearchBinding.movieShowsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activitySearchBinding.movieShowsRecyclerView.canScrollVertically(1)) {
                    if (!activitySearchBinding.inputSearch.getText().toString().isEmpty()){
                        if(currentPage < totalAvailablePages){
                            currentPage +=1;
                            searchMovieShow(activitySearchBinding.inputSearch.getText().toString());
                        }
                    }
                }
            }
        });
        activitySearchBinding.inputSearch.requestFocus();
    }

    private void searchMovieShow(String query){
        toogleLoading();
        viewModel.searchMovieShow(query,currentPage).observe(this,movieShowResponse -> {
            toogleLoading();
            if (movieShowResponse != null){
                totalAvailablePages = movieShowResponse.getTotalPages();
                if(movieShowResponse.getMovieShows() != null){
                    int oldCount = movieShows.size();
                    movieShows.addAll(movieShowResponse.getMovieShows());
                    movieShowsAdapter.notifyItemRangeInserted(oldCount,movieShows.size());
                }
            }
        });
    }

    private void toogleLoading() {
        if (currentPage == 1){
            activitySearchBinding.getIsLoading();
            if (activitySearchBinding.getIsLoading()){
                activitySearchBinding.setIsLoading(false);
            }else{
                activitySearchBinding.setIsLoading(true);
            }
        }else{
            activitySearchBinding.getIsLoadingMore();
            if (activitySearchBinding.getIsLoadingMore()){
                activitySearchBinding.setIsLoadingMore(false);
            }else{
                activitySearchBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onMovieShowsClicked(MovieShow movieShow) {
        Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
        intent.putExtra("movieShow", movieShow);
        startActivity(intent);
    }
}