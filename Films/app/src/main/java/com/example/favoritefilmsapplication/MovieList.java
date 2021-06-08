package com.example.favoritefilmsapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity(tableName = "movies")
public class MovieList {

    @PrimaryKey
    @NonNull
    int _id;
    @NonNull
    int film_id, user_id, category;
    @NonNull
    String date_added;

    public MovieList(int _id, @NonNull int film_id,@NonNull int user_id, @NonNull int category, @NonNull String date_added) {
        this._id = _id;
        this.film_id = film_id;
        this.user_id = user_id;
        this.category = category;
        this.date_added = date_added;
    }

    @Override
    public String toString() { return String.format(Locale.getDefault(), "%d , %d, %d, %s", _id, film_id, user_id, date_added ); }

}
