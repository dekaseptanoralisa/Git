package com.example.git.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.git.models.MovieShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface MovieShowDao {

    @Query("SELECT * FROM movieshows")
    Flowable<List<MovieShow>> getWatchlist();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToWatchlist (MovieShow movieShow);

    @Delete
    Completable removeFromWatchlist(MovieShow movieShow);
    
    @Query("SELECT * FROM movieShows WHERE id = :movieShowId")
    Flowable<MovieShow> getMovieShowFromWatchlist(String movieShowId);
}
