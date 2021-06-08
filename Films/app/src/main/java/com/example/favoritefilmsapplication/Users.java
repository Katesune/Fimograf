package com.example.favoritefilmsapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity(tableName = "users")
public class Users {
    @PrimaryKey
    @NonNull
    int _id;
    @NonNull
    String login;

    public Users(int _id, @NonNull String login) {
        this._id = _id;
        this.login = login;
    }

    @Override
    public String toString() { return String.format(Locale.getDefault(), "%d , %s", _id, login ); }
}

