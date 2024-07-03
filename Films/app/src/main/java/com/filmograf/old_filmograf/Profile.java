package com.filmograf.old_filmograf;

import android.os.AsyncTask;
import android.os.Bundle;


import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.filmograf.old_filmograf.models.Movie;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;

public class Profile extends BasicMoviesActivity {

    TextView name, mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);;

        ln = (LinearLayout) findViewById(R.id.profile_lin);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);

        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

        name.setText(mAuth.getDisplayName());
        mail.setText(mAuth.getEmail());

        getMovieCollection(0);
    }

    public void logOut(View v) {
        FirebaseAuth.getInstance().signOut();
        googleAuthentication().signOut();

        Intent intent = new Intent(Profile.this,MainActivity.class);
        startActivity(intent);
    }

    private GoogleSignInClient googleAuthentication() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return GoogleSignIn.getClient(this, gso);
    }

    public void goToPopular(View v) {
        Intent intent = new Intent(Profile.this, PopularMovieListActivity.class);
        startActivity(intent);
    }

    public void goToFilm(View v) {
        Intent intent = new Intent(Profile.this, MovieActivity.class);
        intent.putExtra("movie_id", getMovieId(v));
        startActivity(intent);
    }

    public void showFavMovies(View v) {
        clearView();
        getMovieCollection(1);
    }

    public void showWatchMovies(View v) {
        clearView();
        getMovieCollection(2);
    }

    public void clearView() {
        LinearLayout current_movies = ln.findViewById(R.id.profile_listview);
        if (current_movies.getChildCount() != 0) current_movies.removeAllViews();
    }

    public void getMovieCollection(int collection_id) {
        new Thread() {
            @Override
            public void run() {
                List<MovieList> movies = db.movie_manager().selectMoviesFromCollection(user_id, collection_id);

                for (int i = 0; i < movies.size(); i++) {
                    int movie_id = movies.get(i)._id / 10;
                    new FilmTask().execute(movie_id);
                }
            }
        }.start();
    }

    public class FilmTask extends AsyncTask<Integer , Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... movie_id) {
            Manager api = getManager();
            Call<Movie> call_film = api.getFilm(movie_id[0], api_key, "ru");
            return getImageTask(call_film);
        }

        @Override
        protected void onPostExecute(Movie result) { setMovie(result); }

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

    public void setMovie(Movie m) {
        if (m != null) {
            View movie_view = getMovieView(m, R.layout.movie);
            LinearLayout movie_list = ln.findViewById(R.id.profile_listview);
            movie_list.addView(movie_view);
        }
    }
}
