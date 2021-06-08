package com.example.favoritefilmsapplication;

import androidx.room.Embedded;
import androidx.room.Relation;

public class UserMovie {
    @Embedded
    public Users user;
    @Relation(
            parentColumn = "_id",
            entityColumn = "user_id")
    public MovieList movieList;
}
