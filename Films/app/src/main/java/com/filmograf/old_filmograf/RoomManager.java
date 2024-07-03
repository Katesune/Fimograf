package com.filmograf.old_filmograf;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RoomManager {
    @Query("SELECT * FROM movies ORDER BY _id")
    List<MovieList> selectAll();

    @Query("SELECT EXISTS(SELECT _id FROM movies WHERE _id=:id AND category=:collection_id)")
    Boolean filmExistsInCategory(int id, int collection_id);

    @Query("SELECT EXISTS(SELECT _id FROM movies WHERE _id=:id AND category=1)")
    Boolean filmExistsInFav(int id);

    @Query("SELECT EXISTS(SELECT _id FROM movies WHERE _id=:id AND category=2)")
    Boolean filmExistsInWatch(int id);

    @Query("SELECT * FROM movies WHERE user_id=:user AND category=:collection_id")
    List<MovieList> selectMoviesFromCollection(int user, int collection_id);

    @Query("SELECT COUNT(_id) FROM movies")
    int getNumberOfRows();

    @Query("DELETE FROM movies")
    void deleteTable();

    @Query("DELETE FROM movies WHERE _id=:id AND category=:collection_id")
    void deleteItem(int id, int collection_id);

    @Insert
    void insert(MovieList... movies);

    @Update
    void update(MovieList... movies);
}