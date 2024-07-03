package com.filmograf.old_filmograf;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.filmograf.old_filmograf.models.Movie;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BasicMoviesActivity extends AppCompatActivity implements MovieManipulative {
    LinearLayout ln;
    Gson gson = new GsonBuilder().create();
    String api_key = "#";

    UserMoviesDB db;
    int user_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = UserMoviesDB.create(this, false);
        UsersDB users_db = UsersDB.create(this, false);

        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

        String email = mAuth.getEmail();
        user_id = getUserId(users_db, email)[0];
    }

    public void addToFav(View v) {
        changeButtonState(v);
        int movie_id = getMovieId(v);
        db.addNewItem(movie_id, 1, user_id);
    }

    public void addToWatch(View v) {
        changeButtonState(v);
        int movie_id = getMovieId(v);
        db.addNewItem(movie_id, 2, user_id);
    }

    public void changeButtonState(View v) {
        Button butt = (Button) v;
        boolean butt_press = butt.isSelected();
        butt.setSelected(!butt_press);
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

    public View getMovieView(Movie movie, int layout_id) {
        LayoutInflater ltInflater = getLayoutInflater();

        String url = "https://www.themoviedb.org/t/p/w1280/";
        View new_movie = ltInflater.inflate(layout_id, ln, false);

        TextView title = new_movie.findViewById(R.id.title);
        TextView original_title = new_movie.findViewById(R.id.original_title);
        TextView release_date = new_movie.findViewById(R.id.release_date);
        TextView popularity = new_movie.findViewById(R.id.popularity);
        TextView overview = new_movie.findViewById(R.id.overview);

        ImageView poster = new_movie.findViewById(R.id.poster);

        TextView id = new_movie.findViewById(R.id.movie_id);
        id.setText(String.valueOf(movie.id));

        Button favButt = new_movie.findViewById(R.id.star);
        Button watchButt = new_movie.findViewById(R.id.watched);

        Boolean[] existsInCollections = db.checkExistsInCollections(movie.id);
        changeButtonColor(favButt, existsInCollections[0]);
        changeButtonColor(watchButt, existsInCollections[1]);

        title.setText("Название: "+ movie.title);
        original_title.setText("Полное Название: "+ movie.original_title);
        release_date.setText("Дата релиза: "+ movie.release_date);
        overview.setText("Описание: "+ movie.overview);
        popularity.setText("Популярность: "+ movie.popularity);

        if (movie.backdrop_path != null) {
            Uri uri_pic = Uri.parse(url + movie.backdrop_path);
            Picasso p = new Picasso.Builder(getApplicationContext()).build();
            p.load(uri_pic).into(poster);
        }

        if (movie.poster_path != null) {
            Uri uri_pic = Uri.parse(url + movie.poster_path);
            Picasso p = new Picasso.Builder(getApplicationContext()).build();
            p.load(uri_pic).into(poster);
        }

        return new_movie;
    }

    public void changeButtonColor(Button butt, Boolean exists) {
        butt.setSelected(exists);
    }
}
