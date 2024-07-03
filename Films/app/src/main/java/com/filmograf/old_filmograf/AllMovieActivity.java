package com.filmograf.old_filmograf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.filmograf.old_filmograf.models.Movie;
import com.filmograf.old_filmograf.models.Movies;

import retrofit2.Call;
import retrofit2.Callback;

public class AllMovieActivity extends BasicMoviesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movie);

        ln = (LinearLayout) findViewById(R.id.all_movies_lin);

        Manager apiManager = getManager();
        Call <Movies> getFilmCall = apiManager.getAll(api_key, "ru", "popularity.desc");

        Callback<Movies> filmcallback = new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, retrofit2.Response<Movies> response) {
                Movies movies = response.body();

                if (movies!=null) {
                    setMovies(movies, R.layout.popular_movie);
                } else Log.e("mytag", "Results Not Found");
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.e("mytag", "fail:" + t.getLocalizedMessage());
            }
        };

        getFilmCall.enqueue(filmcallback);
    }

    public void goToFilm(View v) {
        Intent intent = new Intent(AllMovieActivity.this, MovieActivity.class);
        int movie_id = getMovieId(v);

        intent.putExtra("movie_id", movie_id);
        startActivity(intent);
    }

    public void setMovies(Movies movies, int layout_id){
        for (Movie movie: movies.results) {
            View movie_view = getMovieView(movie, layout_id);
            ln.addView(movie_view);
        }
    }

}