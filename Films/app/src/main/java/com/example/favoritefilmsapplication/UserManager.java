package com.example.favoritefilmsapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserManager {

    @Query("SELECT _id FROM users WHERE login=:login")
    int findByMail(String login);

    @Query("SELECT COUNT(_id) FROM users")
    int getNumberOfRows();

    @Insert
    void insert(Users... users);
}
