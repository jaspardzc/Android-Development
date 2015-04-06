package com.mypersonalapp.androidgourmetguider;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LocationMapActivity extends Activity implements OnClickListener {

    private TextView mTextView;
    private Button mButtonReturn, mButtonMapService, mButtonStore;
    private ImageButton mButtonSearch;
    private EditText mEditSearchMessage;

    private GoogleMap mGoogleMap;
    private PopupWindow popupView;
    String keywords;

    final static int MAP_SYNC_DATA = 888;
    final static int SEARCH_SYNC_DATA = 333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);

        mTextView = (TextView) findViewById(R.id.text_searchList);
        mButtonReturn = (Button) findViewById(R.id.button_returnMain);
        mButtonReturn.setOnClickListener(this);
        mButtonMapService = (Button) findViewById(R.id.button_mapService);
        mButtonMapService.setOnClickListener(this);
        mButtonStore = (Button) findViewById(R.id.button_storeSearchHistory);
        mButtonStore.setOnClickListener(this);
        mButtonSearch = (ImageButton) findViewById(R.id.button_search);
        mButtonSearch.setOnClickListener(this);

        mEditSearchMessage = (EditText) findViewById(R.id.edit_searchMessage);

        //receive the keywords from ImgProcess activity
        Bundle extras = this.getIntent().getExtras();
        if (extras!=null) {
            keywords = extras.getString("Keywords");
            mEditSearchMessage.setText(keywords, TextView.BufferType.EDITABLE);
        }
    }


    @Override
    public void onClick(View arg0) {
        if (arg0 == mButtonReturn) {
            final Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        }
        if (arg0 == mButtonMapService) {
            openMapService();
        }
        if (arg0 == mButtonStore) {
            storeSearchHistory();
        }
        if (arg0 == mButtonSearch) {
            startSearch();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO, sync data from the map activity, sync data from the search activity
        if (resultCode == RESULT_OK) {
            if (requestCode == SEARCH_SYNC_DATA) {
                //retrieve the searching results and display in the list view
                //currently deprecated
            }
            if (requestCode == MAP_SYNC_DATA) {
                //retrieve the map service results
                //display location data, restaurant name,directions, etc. in list view.

            }
        }
    }

    public void openMapService() {
        Intent myIntent = new Intent(LocationMapActivity.this, MapActivity.class);
        startActivityForResult(myIntent, MAP_SYNC_DATA);
    }


    public void startSearch() {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, mEditSearchMessage.getText().toString());
        startActivityForResult(intent, SEARCH_SYNC_DATA);
        //return the result, display in list view, enable scroll view
        //TextView, text_searchList\
        //prograss dialog

    }


    public void storeSearchHistory() {
        //TODO
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File searchHistoryPath = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).getAbsolutePath());
        if(searchHistoryPath.exists()) {
            //getKeywords
            String keywords = mEditSearchMessage.getText().toString();
            File searchHistoryFile = new File(searchHistoryPath.getPath() + File.separator +
                                        "Camera" + File.separator +"History" +
                                        File.separator + "History.txt");
            try {
                String content = "<Search>" + keywords + "<Search>" + "" +
                        "Searched on: " + timeStamp + "." + "\n";
                FileOutputStream fos = new FileOutputStream(searchHistoryFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(content.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(getBaseContext(), "Search History has been saved successfully",
                Toast.LENGTH_SHORT).show();
    }
}
