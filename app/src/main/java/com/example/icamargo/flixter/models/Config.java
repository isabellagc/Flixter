package com.example.icamargo.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by icamargo on 6/23/17.
 */

public class Config {
    //base url from configuration endpoint in API
    String imageBaseUrl;
    //the poster size to use when fetching images, part of the URL
    String posterSize;
    //backdrop size when implementing landscape view
    String backdropSize;

    public Config(JSONObject object) throws JSONException {
        //note: need to parse into images json object first to be able to aprse the other key:val pairs!
        JSONObject images = object.getJSONObject("images");
        //now talk to images to see the secure_base_url since this key is within images not response
        imageBaseUrl = images.getString("secure_base_url");
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //try to get index 3 if not use "w342"
        posterSize = posterSizeOptions.optString(3, "w342");
        //parse the backdrop size and use the option at index 1 or the second object which should be w780
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    //helper method for creating urls
    public String getImageUrl(String size, String path){
        return String.format("%s%s%s", imageBaseUrl, size, path); //concantate all three portions! 
    }


    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
