package com.filmograf.old_filmograf;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={Users.class}, version=1)
public abstract class UsersDB extends RoomDatabase {
    abstract UserManager user_manager();

    private static final String DB_NAME = "users.db";
    private static volatile UsersDB INSTANCE = null;

    synchronized static UsersDB get(Context ctxt) {

        if (INSTANCE == null) {
            INSTANCE = create(ctxt, false);
        }
        return (INSTANCE);
    }

    public static UsersDB create(Context ctxt, boolean memoryOnly) {
        RoomDatabase.Builder<UsersDB> b;
        if (memoryOnly) {
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(),
                    UsersDB.class);
        } else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(), UsersDB.class,
                    DB_NAME);
        }

        return (b.build());

    }
}
