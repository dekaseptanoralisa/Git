package com.example.git.responses;

import com.example.git.models.MovieDetails;
import com.google.gson.annotations.SerializedName;

public class MovieDetailsResponse {

    @SerializedName("tvShow")
    private MovieDetails movieDetails;

    public MovieDetails getMovieDetails() {
        return movieDetails;
    }
}
