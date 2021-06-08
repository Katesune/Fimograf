package com.example.favoritefilmsapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={Users.class}, version=2)
public abstract class UsersDB extends RoomDatabase {
    abstract UserManager user_manager();

    private static final String DB_NAME = "myusers7.db";
    private static volatile com.example.favoritefilmsapplication.UsersDB INSTANCE = null;

    synchronized static com.example.favoritefilmsapplication.UsersDB get(Context ctxt) {

        if (INSTANCE == null) {
            INSTANCE = create(ctxt, false);
        }
        return (INSTANCE);
    }

    static com.example.favoritefilmsapplication.UsersDB create(Context ctxt, boolean memoryOnly ) {
        RoomDatabase.Builder<com.example.favoritefilmsapplication.UsersDB> b;
        if (memoryOnly) {
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(),
                    com.example.favoritefilmsapplication.UsersDB.class);
        } else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(), com.example.favoritefilmsapplication.UsersDB.class,
                    DB_NAME);
        }
        return (b.createFromAsset("movies.db").build());

    }
}
