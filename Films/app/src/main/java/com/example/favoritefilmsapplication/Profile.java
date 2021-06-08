package com.example.favoritefilmsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Profile extends AppCompatActivity {

    LinearLayout ln;
    TextView name, mail;

    UserMoviesDB db;
    UsersDB users_db;

    //GoogleSignInAccount signInAccount;
    Cursor c;
    Gson gson = new GsonBuilder().create();
    String api_key = "2afab14a5f39728f8f613d627e5dd9bb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = UserMoviesDB.create(this, false);
        users_db = UsersDB.create(this, false);

        ln = (LinearLayout) findViewById(R.id.profile_lin);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);

        signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            name.setText(signInAccount.getDisplayName());
            mail.setText(signInAccount.getEmail());
        }
//        Intent intent = new Intent(Profile.this, MainActivity.class);
//        startActivity(intent);
        getFavMovies();


//        Intent intent = new Intent(Profile.this, PopularMovieListActivity.class);
//        startActivity(intent);

    }

    public void addToFav(View v) {
        Button butt = (Button)v;
        String str_id = butt.getText().toString();
        int id = Integer.parseInt(str_id);
        addNewItem(id, 1);
    }

    public void addNewItem(int id, int category) {
        UserMoviesDB db = UserMoviesDB.create(this, false);
        UsersDB users_db;

        new Thread() {
            @Override
            public void run() {
                int film_id = db.movie_manager().getFilmId(id);
                if (film_id==0) {
                    int count = db.movie_manager().getNumberOfRows() + 2;
                    db.movie_manager().insert(new MovieList(count, id,signInAccount.getEmail, category, "06.06.2021"));
                    Log.d("mytag", "insert");
                } else {
                    db.movie_manager().delete(new MovieList(film_id, id, signInAccount.getEmail, category, "06.06.2021"));
                    Intent intent = new Intent(Profile.this, Profile.class);
                    startActivity(intent);
                    Log.d("mytag", "delete");
                }
            }
        }.start();
    }



    public void logOut(View v) {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Profile.this,MainActivity.class);
        startActivity(intent);
    }

    public void goToList(View v) {
        Intent intent = new Intent(Profile.this, PopularMovieListActivity.class);
        startActivity(intent);
    }

    public void goToFilm(View v) {
        Intent intent = new Intent(Profile.this, MovieActivity.class);
        TextView butt_v = (TextView)v;
        String movie_id = butt_v.getText().toString();
        intent.putExtra("movie_id", Integer.parseInt(movie_id));
        startActivity(intent);
    }

    public void showFavMovies(View v) {
        getFavMovies();
    }

    public void showWatchMovies(View v) { getWatchMovies(); }

    public void showWaitMovies(View v) { getWaitMovies(); }


    public void getFavMovies() {

        new Thread() {
            @Override
            public void run() {
                int user_id = users_db.user_manager().findByMail(signInAccount.getEmail);
                Log.d("mytag", String.valueOf(db.movie_manager().getNumberOfRows()));
                Log.d("mytag", String.valueOf(user_id));
                if (user_id!=0) {
                   c = db.query("SELECT * FROM movies WHERE user_id="+String.valueOf(user_id)
                         +" AND category=1", null);
                    if (c!=null) {
                       c.moveToFirst();
                        for (int i = 0; i < c.getCount(); i++) {
                            c.moveToPosition(i);
                            int movie_id = c.getInt(1);
                            new FilmTask().execute(movie_id);
                        }
                    }
                    c.close();
                } else {
                    int count = users_db.user_manager().getNumberOfRows()+1;
                    users_db.user_manager().insert(new Users(count, signInAccount.getEmail));
                }
            }
        }.start();
    }

    public void getWatchMovies() {

        new Thread() {
            @Override
            public void run() {

                int user_id = users_db.user_manager().findByMail(signInAccount.getEmail);

                if (user_id!=0) {
                    c = db.query("SELECT * FROM movies WHERE user_id="+String.valueOf(user_id)
                            +" AND category=2", null);
                    if (c!=null) {
                        c.moveToFirst();
                        for (int i = 0; i < c.getCount(); i++) {
                            c.moveToPosition(i);
                            int movie_id = c.getInt(1);
                            new FilmTask().execute(movie_id);
                        }
                    }
                    c.close();
                } else {
                    int count = users_db.user_manager().getNumberOfRows()+1;
                    users_db.user_manager().insert(new Users(count,signInAccount.getEmail));
                }
            }
        }.start();
    }

    public void getWaitMovies() {

        new Thread() {
            @Override
            public void run() {

                int user_id = users_db.user_manager().findByMail(signInAccount.getEmail);

                if (user_id!=0) {
                    c = db.query("SELECT * FROM movies WHERE user_id="+String.valueOf(user_id)
                            +" AND category=3", null);
                    if (c!=null) {
                        c.moveToFirst();
                        for (int i = 0; i < c.getCount(); i++) {
                            c.moveToPosition(i);
                            int movie_id = c.getInt(1);
                            new FilmTask().execute(movie_id);
                        }
                    }
                    c.close();
                } else {
                    int count = users_db.user_manager().getNumberOfRows()+1;
                    users_db.user_manager().insert(new Users(count,signInAccount.getEmail));
                }
            }
        }.start();
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

    public class FilmTask extends AsyncTask<Integer , Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... movie_id) {
            Manager api = getManager();
            Call<Movie> call_film = api.getFilm(movie_id[0], api_key, "ru");
            return getImageTask(call_film);
        }

        @Override
        protected void onPostExecute(Movie result) { setFilm(result); }

        private Movie getImageTask(Call<Movie> getFilmCall) {
            Manager api = getManager();
            try {
                retrofit2.Response<Movie> response =  getFilmCall.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void setFilm(Movie m) {
        if (m!=null) {
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


            TextView id = new_movie.findViewById(R.id.id);
            id.setText(Integer.toString(m.id));

            Button fav = new_movie.findViewById(R.id.star);

            fav.setText(Integer.toString(m.id));

            title.setText("Название: " + m.title);
            original_title.setText("Полное Название: " + m.original_title);
            release_date.setText("Дата релиза: " + m.release_date);
            overview.setText("Описание: " + m.overview);
            popularity.setText("Популярность: " + String.valueOf(m.popularity));

            if (m.genres != null) {
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
    }

}
