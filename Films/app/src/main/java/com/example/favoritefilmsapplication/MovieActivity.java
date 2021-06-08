package com.example.favoritefilmsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieActivity extends AppCompatActivity {

    LinearLayout ln;

    Gson gson = new GsonBuilder().create();
    String api_key = "2afab14a5f39728f8f613d627e5dd9bb";
    int movie_id;
    Trailer trailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ln = (LinearLayout) findViewById(R.id.moviie_lin);

        movie_id = getIntent().getIntExtra("movie_id", 0);
        //movie_id = 337404 ;

        getImagesTask task = new getImagesTask();
        task.execute();
    }

    public void gotoProfile (View v) {
        Intent intent = new Intent(MovieActivity.this, Profile.class);
        startActivity(intent);
    }

    public void gotoTrailer (View v) {
        String url_video =  "https://www.themoviedb.org/video/play?key="+trailer.results[0].key;

        Log.d("mytag",url_video);
        Uri uri = Uri.parse(url_video);
        String action = Intent.ACTION_VIEW;

        Intent videoIntent = new Intent(action, uri);
        startActivity(videoIntent);
    }

    public Manager getManager () {
        String url = "https://api.themoviedb.org/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Manager api = retrofit.create(Manager.class);

        return api;
    }

    public void setFilm(Movie m) {
        LayoutInflater ltInflater = getLayoutInflater();

            String url = "https://www.themoviedb.org/t/p/w1280/";
            View new_movie = ltInflater.inflate(R.layout.movie, ln, false);

            TextView title = new_movie.findViewById(R.id.title);
            TextView original_title = new_movie.findViewById(R.id.original_title);
            TextView release_date = new_movie.findViewById(R.id.release_date);
            TextView popularity = new_movie.findViewById(R.id.popularity);
            TextView overview = new_movie.findViewById(R.id.overview);
            TextView genres = new_movie.findViewById(R.id.genres);

            ImageView poster = new_movie.findViewById(R.id.poster);

            title.setText("Название: " + m.title);
            original_title.setText("Полное Название: " + m.original_title);
            release_date.setText("Дата релиза: " + m.release_date);
            overview.setText("Описание: " + m.overview);
            popularity.setText("Популярность: " + String.valueOf(m.popularity));

            if (m.genres!=null) {
                String genres_s = "";
                for (int i = 0; i < m.genres.length; i++) {
                    genres_s += m.genres[i].name + " ";
                }
                genres.setText("Жанры: " + genres_s);
            }

            if (m.backdrop_path != null) {
                Uri uri_pic = Uri.parse(url + m.backdrop_path);
                Picasso p = new Picasso.Builder(getApplicationContext()).build();
                p.load(uri_pic).into(poster);
            }

            if (m.poster_path != null) {
                Uri uri_pic = Uri.parse(url + m.poster_path);
                Picasso p = new Picasso.Builder(getApplicationContext()).build();
                p.load(uri_pic).into(poster);
            }

            ln.addView(new_movie);
    }

    public class getImagesTask extends AsyncTask<Void , Void, Movie> {

        @Override
        protected Movie doInBackground(Void... voids) {
            Manager api = getManager();
            Call <Movie> call_film = api.getFilm(movie_id, api_key, "ru");
            return getImageTask(call_film);
        }

        @Override
        protected void onPostExecute(Movie result) { setFilm(result); }

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