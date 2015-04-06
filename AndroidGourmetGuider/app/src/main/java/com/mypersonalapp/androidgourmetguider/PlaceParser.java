package com.mypersonalapp.androidgourmetguider;

/**
 * Created by jaspe_000 on 3/18/2015.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceParser {

    /**
     * Parsing the Google Places in JSON format, Return a list on receiving a JSONObject
     */
    public List<HashMap<String,String>> parse(JSONObject jsonObject) {

        JSONArray jsonPlaces = null;
        try {
            jsonPlaces = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //each JSONObject represents a place
        return getPlace(jsonPlaces);
    }

    private List<HashMap<String, String>> getPlace(JSONArray jsonPlaces) {
        int placeCount = jsonPlaces.length();
        List<HashMap<String, String>> placeList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> place = null;

        //get each place, parse and insert to the list object
        for (int i = 0; i < placeCount; i++) {
            try {
                place = getPlace((JSONObject)jsonPlaces.get(i));
                placeList.add(place);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placeList;
    }

    private HashMap<String, String> getPlace(JSONObject jsonPlace) {
        HashMap<String, String> place = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longtitude = "";

        try {
            //extract place name, if available
            if (!jsonPlace.isNull("name")) {
                placeName = jsonPlace.getString("name");
            }
            //extract place vicinity
            if (!jsonPlace.isNull("vicinity")) {
                vicinity = jsonPlace.getString("vicinity");
            }
            latitude = jsonPlace.getJSONObject("geometry").
                    getJSONObject("location").getString("lat");
            longtitude = jsonPlace.getJSONObject("geometry").
                    getJSONObject("location").getString("lng");
            place.put("place_name", placeName);
            place.put("vicinity", vicinity);
            place.put("lat", latitude);
            place.put("lng", longtitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}
