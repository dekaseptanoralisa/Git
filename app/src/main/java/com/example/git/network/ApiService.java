package com.example.git.network;



import com.example.git.responses.MovieShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET ("most-popular")
    Call<MovieShowResponse> getMostPopularMovieShows(@Query("page")int page);

}
