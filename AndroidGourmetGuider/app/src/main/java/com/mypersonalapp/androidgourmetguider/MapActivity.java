package com.mypersonalapp.androidgourmetguider;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Locale;


public class MapActivity extends Activity implements LocationListener, OnClickListener, OnCheckedChangeListener {

    //declare
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private LatLng mPosition;
    private Location mLocation;
    private Circle defaultCircle, newCircle;
    private MapFragment mMapFragment;
    private Spinner mSpinnerPlaceType;

    private Button mButtonReturn, mButtonDstConfirm;
    private RadioGroup mRadioGroup;
    private Button mButtonSet, mButtonFind;
    private EditText mEditDiameter, mEditDst;
    final static int RETURN_MAP_DATA = 999;

    //declare two empty string array
    String[] mPlaceType = null;
    String[] mPlaceTypeName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        startMapService();

        mEditDiameter = (EditText) findViewById(R.id.edit_diameter);
        mEditDst = (EditText) findViewById(R.id.edit_destination);
        mButtonSet = (Button) findViewById(R.id.button_set);
        mButtonSet.setOnClickListener(this);
        mButtonFind = (Button) findViewById(R.id.button_find);
        mButtonFind.setOnClickListener(this);
        mButtonReturn = (Button) findViewById(R.id.button_returnLocation);
        mButtonReturn.setOnClickListener(this);
        mButtonDstConfirm = (Button) findViewById(R.id.button_dstConfirm);
        mButtonDstConfirm.setOnClickListener(this);
        mRadioGroup = (RadioGroup)findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(this);

        // Array of place types
        mPlaceType = getResources().getStringArray(R.array.place_type);

        // Array of place type names
        mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);

        // Creating an array adapter with an array of Place types
        // to populate the spinner
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mPlaceTypeName);

        // Getting reference to the Spinner
        mSpinnerPlaceType = (Spinner) findViewById(R.id.spinner_placeType);

        // Setting adapter on Spinner to set place types
        mSpinnerPlaceType.setAdapter(adapter);

        // Getting Google Play availability status
        //int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
    }

    /**
     * Google Map Services Instantiation
     */
    public void startMapService() {
        try {
            mMapFragment = (MapFragment) getFragmentManager().
                    findFragmentById(R.id.map);
            mGoogleMap = mMapFragment.getMap();
            //default mapView is terrain
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setBuildingsEnabled(true);
            mGoogleMap.setIndoorEnabled(true);
            mGoogleMap.setTrafficEnabled(true);

            //get my location and update every 2 seconds
            mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            Criteria mCriteria = new Criteria();
            String provider = mLocationManager.getBestProvider(mCriteria, true);
            mLocation = mLocationManager.getLastKnownLocation(provider);
            mLocationManager.requestLocationUpdates(provider, 2000, 0, this);
            //get my position coordinate
            mPosition = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            //zoom in
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mPosition, 16));
            mGoogleMap.addMarker(new MarkerOptions().position(mPosition)
                    .title("Me")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.foodie_launcher)));
            //set the diameter of searching radius by default value of 5000 meters

            defaultCircle = mGoogleMap.addCircle(new CircleOptions().center(mPosition)
                    .radius(5000)
                    .strokeColor(Color.RED)
                    .fillColor(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        //change mapView by switching between radio button
        if (checkedId == R.id.radio_normal) {
            mGoogleMap.setMapType(mGoogleMap.MAP_TYPE_NORMAL);
        }
        if (checkedId == R.id.radio_satellite) {
            mGoogleMap.setMapType(mGoogleMap.MAP_TYPE_SATELLITE);
        }
        if (checkedId == R.id.radio_terrain) {
            mGoogleMap.setMapType(mGoogleMap.MAP_TYPE_TERRAIN);
        }
    }

    /**
     * Dispatch Click Event by comparing the arg0 value with the buttonName.
     * @param arg0
     */
    @Override
    public void onClick(View arg0) {
        if (arg0 == mButtonReturn) {
            Intent myIntent = new Intent(MapActivity.this, LocationMapActivity.class);
            startActivityForResult(myIntent, RETURN_MAP_DATA);
        }
        if (arg0 == mButtonDstConfirm) {
            String Dst = getDestination();
            if ((Dst != null) && (!Dst.equals(""))) {
                //execute
            }

        }
        if (arg0 == mButtonSet) {
            //remove the default circle and the previous circle
            if (defaultCircle != null){
                defaultCircle.remove();
            }
            if (newCircle != null) {
                newCircle.remove();
            }
            newCircle = mGoogleMap.addCircle(new CircleOptions().center(mPosition)
                    .radius(getDiameter())
                    .strokeColor(Color.RED)
                    .fillColor(Color.TRANSPARENT));
        }
        if (arg0 == mButtonFind) {
            //find the place specified in the spinner
            int selectedPosition = mSpinnerPlaceType.getSelectedItemPosition();
            String type = mPlaceType[selectedPosition];
            StringBuilder mStringBuilder = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            mStringBuilder.append("Location=" + mLocation.getLatitude() + "," +
                        mLocation.getLongitude());
            mStringBuilder.append("&radius=5000");
            mStringBuilder.append("&types" + type);
            mStringBuilder.append("&sensor=true");
            mStringBuilder.append("&key=AIzaSyD2NqPvLmoB3jOeUjLw2V3cLm_nDbk7kXM");

            Toast.makeText(getBaseContext(), "Starting finding...", Toast.LENGTH_LONG).show();
            PlacesTask mPlaceTask = new PlacesTask();
            mPlaceTask.execute(mStringBuilder.toString());
        }
    }

    /**
     * Get the Diameter value specified by user
     * @return
     */
    public int getDiameter() {
        String mDiameter = null;
        int Diameter;
        mDiameter = mEditDiameter.getText().toString();
        if (mDiameter == null) {
            Toast.makeText(getBaseContext(), "The Diameter must be larger than Zero",
                    Toast.LENGTH_LONG);
            return -1;
        }
        Diameter = Integer.parseInt(mDiameter);
        return Diameter;
    }

    /**
     * Get the Destination value specified by user
     * @return
     */
    public String getDestination() {
        String Destination = null;
        Destination = mEditDst.getText().toString();
        if (Destination == null) {
            Toast.makeText(getBaseContext(), "The Destination is null", Toast.LENGTH_LONG);
            return null;
        }
        return Destination;
    }

    /**
     * An AsyncTask for doing GeoCoding Task
     */
    private class GeoCoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... LocationName) {
            Geocoder mGeocoder = new Geocoder(getBaseContext());

            List<Address> addresses = null;
            try {
                addresses = mGeocoder.getFromLocationName(LocationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            if ((addresses == null) || (addresses.size() == 0)) {
                Toast.makeText(getBaseContext(), "Location Not Found",
                        Toast.LENGTH_LONG).show();
            }
            //clear all existing markers
            mGoogleMap.clear();

            //Adding markers on Google Map for each matching address
            for (int i = 0; i < addresses.size(); i++) {
                Address address = (Address) addresses.get(i);
                //creating instance of GeoPoint
                LatLng newLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                String addressString = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());
                MarkerOptions newMarkerOptions = new MarkerOptions();
                newMarkerOptions.position(newLatLng);
                newMarkerOptions.title(addressString);
                mGoogleMap.addMarker(newMarkerOptions);

                // locate the first location
                if (i == 0) {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLng));
                }
            }
        }
    }

    /**
     * A method to download json data from url
     *
     */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();

            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * A class, to download Google Places
     *
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;
        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     *
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;
        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {
            List<HashMap<String, String>> places = null;
            PlaceParser placeJsonParser = new PlaceParser();
            try{
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){

            // Clears all the existing markers
            mGoogleMap.clear();
            for(int i=0;i<list.size();i++){
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);
                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));
                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));
                // Getting name
                String name = hmPlace.get("place_name");
                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");
                LatLng latLng = new LatLng(lat, lng);
                // Setting the position for the marker
                markerOptions.position(latLng);
                // Setting the title for the marker.
                //This will be displayed on taping the marker
                markerOptions.title(name + " : " + vicinity);
                // Placing a marker on the touched position
                mGoogleMap.addMarker(markerOptions);
            }
        }
    }

    //Bonus Method auto generated by the IDE
    @Override
    public void onLocationChanged(Location location) {
        //TODO Auto generated
    }

    @Override
    public void onProviderDisabled(String provider) {
        //TODO Auto generated
    }

    @Override
    public void onProviderEnabled(String provider) {
        //TODO Auto generated
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //TODO Auto generated
    }
}
