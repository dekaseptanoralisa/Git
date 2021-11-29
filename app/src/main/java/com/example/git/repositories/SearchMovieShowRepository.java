package com.example.git.repositories;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.git.network.ApiClient;
import com.example.git.network.ApiService;
import com.example.git.responses.MovieShowResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMovieShowRepository {

    private ApiService apiService;

    public SearchMovieShowRepository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<MovieShowResponse> searchMovieShow(String query, int page) {
        MutableLiveData<MovieShowResponse> data = new MutableLiveData<>();
        apiService.searchMovieShow(query, page).enqueue(new Callback<MovieShowResponse>(){
            @Override
            public void onResponse(@NonNull Call<MovieShowResponse> call, @NonNull Response<MovieShowResponse> response){
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MovieShowResponse> call, @NonNull Throwable t){
                data.setValue(null);
            }
        });
        return data;
    }
}
