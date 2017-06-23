package com.example.icamargo.flixter.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by icamargo on 6/22/17.
 */
//track all info associated with a movie to display
public class Movie {
    private String title;
    private String overview;
    private String posterPath; //only the path, not url

    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
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


}
