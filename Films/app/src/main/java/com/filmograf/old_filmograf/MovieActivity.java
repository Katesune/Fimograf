package com.filmograf.old_filmograf;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.filmograf.old_filmograf.models.Movie;
import com.filmograf.old_filmograf.models.Trailer;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieActivity extends BasicMoviesActivity {

    int movie_id;
    Trailer trailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ln = (LinearLayout) findViewById(R.id.moviie_lin);

        movie_id = getIntent().getIntExtra("movie_id", 0);

        getImagesTask task = new getImagesTask();
        task.execute();
    }

    public void gotoProfile (View v) {
        Intent intent = new Intent(MovieActivity.this, Profile.class);
        startActivity(intent);
    }

    public void gotoTrailer (View v) {
        String url_video =  "https://www.themoviedb.org/video/play?key="+trailer.results[0].key;

        Uri uri = Uri.parse(url_video);
        String action = Intent.ACTION_VIEW;

        Intent videoIntent = new Intent(action, uri);
        startActivity(videoIntent);
    }

    public void setMovie(Movie m) {
        View movie_view = getMovieView(m, R.layout.movie);
        ln.addView(movie_view);
    }

    public class getImagesTask extends AsyncTask<Void , Void, Movie> {

        @Override
        protected Movie doInBackground(Void... voids) {
            Manager api = getManager();
            Call <Movie> call_film = api.getFilm(movie_id, api_key, "ru");
            return getImageTask(call_film);
        }

        @Override
        protected void onPostExecute(Movie result) { setMovie(result); }

        private Movie getImageTask(Call<Movie> getFilmCall) {
            Manager api = getManager();
            Call <Trailer> call_trailer = api.getTrailer(movie_id, api_key, "ru");
            try {
                retrofit2.Response<Movie> response =  getFilmCall.execute();
                trailer =  call_trailer.execute().body();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}