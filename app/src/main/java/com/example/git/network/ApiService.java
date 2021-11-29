package com.example.git.network;

import com.example.git.models.MovieDetails;
import com.example.git.responses.MovieDetailsResponse;
import com.example.git.responses.MovieShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET ("most-popular")
    Call<MovieShowResponse> getMostPopularMovieShows(@Query("page")int page);

    @GET ("show-details")
    Call<MovieDetailsResponse> getMovieShowDetails(@Query("q") String movieId);

    @GET("search")
    Call<MovieShowResponse> searchMovieShow(@Query("q") String query, @Query("page") int page);
}
