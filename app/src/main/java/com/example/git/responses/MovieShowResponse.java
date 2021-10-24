package com.example.git.responses;

import com.example.git.models.MovieShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieShowResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int totalPages;

    @SerializedName("tv_shows")
    private List<MovieShow> movieShows;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<MovieShow> getMovieShows() {
        return movieShows;
    }
}
