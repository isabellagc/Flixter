package com.example.icamargo.flixter.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by icamargo on 6/22/17.
 */

//annotation indicates class is Parcelable
@Parcel

//track all info associated with a movie to display
public class Movie {
    String title;
    String overview;
    String posterPath; //only the path, not url
    String backdropPath; //partial path!
    Double voteAverage;

    public Movie(){}

    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
