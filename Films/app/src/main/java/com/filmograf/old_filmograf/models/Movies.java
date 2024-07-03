package com.filmograf.old_filmograf.models;


import com.filmograf.old_filmograf.models.Movie;

public class Movies {
    public int page;
    public String request_hash;
    public Movie[] results = null;
    public Movie[] movie_results = null;
    public int total_result;
    public int total_pages;
}
