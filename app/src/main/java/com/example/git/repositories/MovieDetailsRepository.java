package com.example.git.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.git.network.ApiClient;
import com.example.git.network.ApiService;
import com.example.git.responses.MovieDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsRepository {

    private ApiService apiService;

    public MovieDetailsRepository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<MovieDetailsResponse> getMovieDetails(String movieId) {
        MutableLiveData<MovieDetailsResponse> data = new MutableLiveData<>();
        apiService.getMovieShowDetails(movieId).enqueue(new Callback<MovieDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetailsResponse> call,@NonNull Response<MovieDetailsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetailsResponse> call,@NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

}
