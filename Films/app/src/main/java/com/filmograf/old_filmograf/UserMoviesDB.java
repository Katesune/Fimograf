package com.filmograf.old_filmograf;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Database(entities={MovieList.class}, version=1)
public abstract class UserMoviesDB extends RoomDatabase {
    abstract RoomManager movie_manager();

    private static final String DB_NAME = "userlist.db";
    private static volatile UserMoviesDB INSTANCE = null;

    synchronized static UserMoviesDB get(Context context) {

        if (INSTANCE == null) {
            INSTANCE = create(context, false);
        }
        return (INSTANCE);
    }

    public static UserMoviesDB create(Context context, boolean memoryOnly) {
        RoomDatabase.Builder<UserMoviesDB> b;
        if (memoryOnly) {
            b = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                    UserMoviesDB.class);
        } else {
            b = Room.databaseBuilder(context.getApplicationContext(), UserMoviesDB.class,
                    DB_NAME);
        }

        return (b.build());
    }

    public void addNewItem(int id, int category, int user_id) {
        new Thread() {
            @Override
            public void run() {

                int movie_id = id * 10 + category;
                Boolean movie_exists = movie_manager().filmExistsInCategory(movie_id, category);

                if (movie_exists) {
                    movie_manager().deleteItem(movie_id, category);
                } else {
                    movie_manager().insert(new MovieList(movie_id, user_id, category, "06.06.2021"));
                }
            }
        }.start();
    }

    public Boolean[] checkExistsInCollections(int movie_id) {
        ExistsInCollectionTask collectionTask = new ExistsInCollectionTask();
        collectionTask.execute(movie_id);

        Boolean[] result = {false, false};
        try {
            result = collectionTask.get();
        }
        catch (ExecutionException e) {}
        catch (InterruptedException e) {}

        return result;
    }

    class ExistsInCollectionTask extends AsyncTask<Integer, Boolean[], Boolean[]> {

        @Override
        protected Boolean[] doInBackground(Integer... ints) {
            Boolean[] result = {false, false};

            result[0] = movie_manager().filmExistsInFav(ints[0] * 10 + 1);
            result[1] = movie_manager().filmExistsInWatch(ints[0] * 10 + 2);

            return result;
        }

        @Override
        protected void onPostExecute(Boolean[] result) {
            super.onPostExecute(result);
        }
    }

}
