package com.example.icamargo.flixter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.icamargo.flixter.models.Config;
import com.example.icamargo.flixter.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class MovieListActivity extends AppCompatActivity {
    //constants
    public final static String API_Base_URL = "https://api.themoviedb.org/3";
    public final static String API_Key_Param = "api_key";


    //tag for all logging cals
    public final static String TAG = "MovieListActivity";

    //instance fields
    AsyncHttpClient client;

    //list of currently playing movies
    ArrayList<Movie> movies;

    //the recycler view
    RecyclerView rvMovies;
    //the adapter wired to the recycler view
    MovieAdapter adapter;
    //the configurtion object to create image url
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //initialize the client
        client = new AsyncHttpClient();
        //initialize the array of movies...
        movies = new ArrayList<>();
        // intiitalize the adapter -- movie array cannot be reinitialized after this point
        adapter = new MovieAdapter(movies);

        //resolve the recycler view and connect a layout manager and the adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        getConfiguration();
    }

    //get list of currently playing movies from the API
    private void getNowPlaying(){
        String url = API_Base_URL + "/movie/now_playing";
        //set the request params
        RequestParams params = new RequestParams();
        params.put(API_Key_Param, getString(R.string.api_key));

        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into movie list
                try {
                    JSONArray resultMovieList = response.getJSONArray("results");
                    //iterate through result set and craete Movie objects from each
                    for(int i = 0; i < resultMovieList.length(); i++){
                        Movie movie = new Movie(resultMovieList.getJSONObject(i));
                        movies.add(movie);
                        //notify the adapter that a row was added
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", resultMovieList.length()));
                } catch (JSONException e) {
                    logError("Failed to parse response now_playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now_playing endpoint!", throwable, true);
            }
        });
    }




    //get the configuration from the API
    private void getConfiguration(){
        //create the url
        String url = API_Base_URL + "/configuration";
        //set the request params
        RequestParams params = new RequestParams();
        params.put(API_Key_Param, getString(R.string.api_key));
        //execute a GET request expecting a JSON object responese
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //get the image base url
                //the parameter that the getString needs is the key in the API endpoint JSON
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s", config.getImageBaseUrl(), config.getPosterSize()));
                    //pass config to the adapter
                    adapter.setConfig(config);
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failure happened while parsing!", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });

    }

    //error logging helper- user should know something went wrong
    private void logError(String message, Throwable error, boolean alertUser){
        log.e(TAG, message, error);
        //alert the user to avoid silent errors
        if(alertUser){
            //show a long toast so user knows
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}





























//file comment :)