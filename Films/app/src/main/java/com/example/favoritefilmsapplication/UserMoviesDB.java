package com.example.favoritefilmsapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={MovieList.class}, version=2)
public abstract class UserMoviesDB extends RoomDatabase {
    abstract RoomManager movie_manager();

    private static final String DB_NAME = "userlist7.db";
    private static volatile UserMoviesDB INSTANCE = null;

    synchronized static UserMoviesDB get(Context ctxt) {

        if (INSTANCE == null) {
            INSTANCE = create(ctxt, false);
        }
        return (INSTANCE);
    }

    static UserMoviesDB create(Context ctxt, boolean memoryOnly) {
        RoomDatabase.Builder<UserMoviesDB> b;
        if (memoryOnly) {
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(),
                    UserMoviesDB.class);
        } else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(), UserMoviesDB.class,
                    DB_NAME);
        }
        return (b.createFromAsset("movies.db").build());

    }
}
