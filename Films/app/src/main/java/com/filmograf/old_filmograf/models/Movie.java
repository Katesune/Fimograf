package com.filmograf.old_filmograf.models;


public class Movie {
    public int id;
    public String title;
    public String original_title;
    public String overview;
    public String poster_path = null;
    public String backdrop_path = null;
    public String release_date;
    public double popularity;
    public Genre[] genres = null;
    public Boolean video = null;

    public class Genre {
        int id;
        public String name;
    }
}
