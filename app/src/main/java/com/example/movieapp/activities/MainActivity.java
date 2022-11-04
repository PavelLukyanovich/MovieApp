package com.example.movieapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.data.MovieAdapter;
import com.example.movieapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText movieTitle;

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movies;
    private RequestQueue requestQueue;
    private String url = "http://www.omdbapi.com/?apikey=998b1e64&s=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieTitle = findViewById(R.id.movieTitleEditText);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movies = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        getMovies();
    }

    private void getMovies() {



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Search");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String title = jsonObject.getString("Title");
                                String year = jsonObject.getString("Year");
                                String posterUrl = jsonObject.getString("Poster");
                                Log.d("getPosterUrl: ", posterUrl);

                                Movie movie = new Movie();
                                movie.setTitle(title);
                                movie.setYear(year);
                                movie.setPosterUrl(posterUrl);
                                Log.d("SetPosterUrl: ", posterUrl);

                                movies.add(movie);
                            }

                            movieAdapter = new MovieAdapter(MainActivity.this, movies);
                            recyclerView.setAdapter(movieAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    public void search(View view) {

        String addMovieTitle = movieTitle.getText().toString();
        Log.d("add", addMovieTitle);
        String addMovieTitleLC = addMovieTitle.toLowerCase();
        Log.d("addToLC", addMovieTitle);
        String addMovieTitleLCR = addMovieTitleLC.replace(" ", "_");
        Log.d("addRepl", addMovieTitle);
                url = url + addMovieTitleLCR;
                Log.d("NewUrl: ", url);
                getMovies();
                url = "http://www.omdbapi.com/?apikey=998b1e64&s=";
                movies.clear();

            }
}