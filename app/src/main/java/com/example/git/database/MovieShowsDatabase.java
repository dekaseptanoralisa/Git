package com.example.git.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.git.dao.MovieShowDao;
import com.example.git.models.MovieShow;

@Database(entities = MovieShow.class, version = 1, exportSchema = false)
public abstract class MovieShowsDatabase extends RoomDatabase {

    private static MovieShowsDatabase movieShowsDatabase;

    public static synchronized MovieShowsDatabase getMovieShowsDatabase(Context context){
        if (movieShowsDatabase == null){
            movieShowsDatabase = Room.databaseBuilder(
                    context,
                    MovieShowsDatabase.class,
                    "movie_shows_db"
            ).build();
        }
        return movieShowsDatabase;
    }

    public abstract MovieShowDao movieShowDao();

}
