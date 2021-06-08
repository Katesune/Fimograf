package com.example.favoritefilmsapplication;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface RoomManager {
    @Query("SELECT * FROM movies ORDER BY _id")
    List<MovieList> selectAll();

//    @Query("SELECT * FROM movies WHERE _id=:_id")
//    MovieList findById(int _id);

    @Query("SELECT COUNT(_id) FROM movies WHERE film_id=:id")
    int getNumberOfId(int id);

    @Query("SELECT _id FROM movies WHERE film_id=:id")
    int getFilmId(int id);

    @Query("SELECT film_id FROM movies WHERE film_id=:id")
    int getFilmDate(int id);

    @Query("SELECT COUNT(_id) FROM movies")
    int getNumberOfRows();

    @Query("DELETE FROM movies")
    void deleteTable();

    @Query("DELETE FROM movies WHERE _id=:id")
    void deleteItem(int id);

    @Insert
    void insert(MovieList... movies);

    @Delete
    void delete(MovieList... movies);

    @Update
    void update(MovieList... movies);
}