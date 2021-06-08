package com.example.favoritefilmsapplication;

public class Movie {
    int id;
    String title;
    String original_title;
    String overview;
    String poster_path = null;
    String backdrop_path = null;
    String release_date;
    double popularity;
    Genre[] genres = null;
    Boolean video = null;

    public class Genre {
        int id;
        String name;
    }
}
