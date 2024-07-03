package com.filmograf.old_filmograf;


import android.util.Log;

import com.filmograf.old_filmograf.models.Movies;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallbackHelper implements Callback {

    public Movies my_movies;

    public Callback<Movies> getPopularMovies(){
            Callback<Movies> filmcallback = new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, retrofit2.Response<Movies> response) {

                    Log.d("mytag","response.raw().request().url();"+response.raw().request().url());
                    Movies movies = response.body();

                    if (movies!=null) {
                        my_movies = movies;
                        Log.d("mytag", movies.results[0].original_title);
                        //showResult(r);
                    } //else error.setText("Results Not Found");
                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    Log.d("mytag", "fail:" + t.getLocalizedMessage());
                }
            };

            return filmcallback;
    }

    @Override
    public void onResponse(Call call, Response response) {

    }

    @Override
    public void onFailure(Call call, Throwable t) {

    }
}
