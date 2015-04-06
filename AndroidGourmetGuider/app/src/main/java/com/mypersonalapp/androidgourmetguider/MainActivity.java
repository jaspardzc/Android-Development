package com.mypersonalapp.androidgourmetguider;




import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Throwable;
import java.text.SimpleDateFormat;
import java.util.Date;



public class MainActivity extends Activity implements OnClickListener {

    private ImageButton mButtonSearch, mButtonVoiceSearch;
    private Button mButtonUpdatePreview;
    private Button mButtonCamera, mButtonOpenFile, mButtonPS, mButtonSend, mButtonHistory;
    private Button mAboutMe;
    private LinearLayout mLinearLayout;
    private OnQueryTextListener mSearchQueryTextListener;
    private EditText mEditEmail, mEditMessage;
    ImageView mImageView;
    private String emailString, attachmentFile, searchString;

    private PopupWindow popupView;

    Uri URI = null;
    final Uri sourceUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final int PICK_FROM_GALLERY = 101;
    int columnIndex;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        //instantiate all buttons, and set click listener
        mButtonSearch = (ImageButton) findViewById(R.id.button_search);
        mButtonSearch.setOnClickListener(this);
        mButtonVoiceSearch = (ImageButton) findViewById(R.id.button_voice);
        mButtonVoiceSearch.setOnClickListener(this);
        mButtonCamera = (Button) findViewById(R.id.button_camera);
        mButtonCamera.setOnClickListener(this);
        mButtonOpenFile = (Button) findViewById(R.id.button_fileUpLoader);
        mButtonOpenFile.setOnClickListener(this);
        mButtonPS = (Button) findViewById(R.id.button_imageProcess);
        mButtonPS.setOnClickListener(this);
        mButtonUpdatePreview = (Button) findViewById(R.id.button_updatePreview);
        mButtonUpdatePreview.setOnClickListener(this);
        mButtonSend = (Button) findViewById(R.id.button_sendEmail);
        mButtonSend.setOnClickListener(this);
        mButtonHistory = (Button) findViewById(R.id.button_history);
        mButtonHistory.setOnClickListener(this);
        mAboutMe = (Button) findViewById(R.id.button_aboutMe);
        mAboutMe.setOnClickListener(this);
        mEditEmail = (EditText) findViewById(R.id.edit_emailAddress);
        mEditMessage = (EditText) findViewById(R.id.edit_message);
        mImageView = (ImageView) findViewById(R.id.image_preview);
        mImageView.setBackgroundColor(Color.rgb(255,255,255));
    }

    /**
     * This function can load image from local storage, and attach the image to the email
     * @param requestCode: to indicate the activity result is received from which intent
     * @param resultCode: set default
     * @param data: get the image
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            /**
             * Find path
             */
            Uri selectedImage = data.getData(); //get the URI that points to the selected contact
            //return "image:x", requires the api 19
            String wholeID = DocumentsContract.getDocumentId(selectedImage);
            //split the colon, use second item in the array
            String id = wholeID.split(":")[1];
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            //where the id is equal to
            String selected = MediaStore.Images.Media._ID + "=?";
            Cursor mCursor = this.getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            filePathColumn, selected, new String[]{id}, null);
            columnIndex = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //previous method is getColumnIndex
            mCursor.moveToFirst();
            //Retrieve the image from the image column
            attachmentFile = mCursor.getString(columnIndex);
            Log.e("Attachment Path: ", attachmentFile); //solved
            //System.out.println(attachmentFile);
            URI = Uri.parse("file://" + attachmentFile);
            //System.out.println(attachmentFile);
            mCursor.close();
        }
    }

    @Override
    public void onClick(View arg0) {
        if (arg0 == mButtonSearch) {
            searchString = mEditMessage.getText().toString();
            Intent messageIntent = new Intent(Intent.ACTION_WEB_SEARCH);
            messageIntent.putExtra(SearchManager.QUERY, searchString);
            startActivity(messageIntent);
        }
        if (arg0 == mButtonVoiceSearch) {
            Intent voiceIntent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
            voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now Man");
            startActivity(voiceIntent);
        }
        if (arg0 == mButtonCamera) {
            /**
             * Take photo and set the thumbnail of the image as the preview
             * Save the image path, leaving for further image handling, such as image processing
             */
            Intent cameraIntent = new Intent(this, CameraActivity.class);
            startActivity(cameraIntent);
        }
        if (arg0 == mButtonOpenFile) {
            /**
             * Pick image from local storage, set the request code
             */
            Intent curIntent = new Intent();
            curIntent.setType("image/*");
            curIntent.setAction(Intent.ACTION_GET_CONTENT);
            curIntent.putExtra("return-data", true);
            startActivityForResult(Intent.createChooser(curIntent, "Complete"),
                    PICK_FROM_GALLERY);
        }
        if (arg0 == mButtonUpdatePreview) {
            //Method 1: get image callback
            //Method 2: currently used, load from storage
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imagePath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).getAbsolutePath();
            imagePath += File.separator + "Camera" + File.separator +
                    "IMG_" + timeStamp + ".jpg";

            //String imagePath = mCameraActivity.getImagePath();
            File imgFile = new File(imagePath);

            if (imgFile.exists()){
                Bitmap mBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mImageView.setImageBitmap(mBitmap);
            }
        }
        if (arg0 == mButtonPS) {
            //Press this button will jump to imageProcess activitys
            //Use Bilateral Filter to make image feature more easier to be identified
            //Use Gaussian Blur to make image more smooth when too much noise occur
            try {
                mImageView.buildDrawingCache();
                Bitmap bitmap = mImageView.getDrawingCache();
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bs);
                Bundle bundle = new Bundle();
                bundle.putByteArray("image", bs.toByteArray());
                Intent myIntent = new Intent(MainActivity.this, ImageProcessActivity.class);
                myIntent.putExtras(bundle);
                MainActivity.this.startActivityForResult(myIntent, 0);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (arg0 == mButtonSend) {
            /**
             * Send Email with attachment
             */
            try {
                emailString = mEditEmail.getText().toString();
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {emailString});
                if (URI!=null){emailIntent.putExtra(Intent.EXTRA_STREAM, URI);}
                this.startActivity(Intent.createChooser(emailIntent, "Start Sending Email"));
            } catch (Throwable error) {
                Toast.makeText(this,"Reuqest Failed, try again!"+ error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
        if (arg0 == mButtonHistory) {
            //press this button will show a pop up window
            final LinearLayout tempLinearLayout = (LinearLayout) findViewById(R.id.main_layout);
            tempLinearLayout.setAlpha(0.5F);
            View popupWindow = getLayoutInflater().inflate(R.layout.popout_history, null);
            //LinearLayout.LayoutParams.MATCH_PARENT
            popupView = new PopupWindow(popupWindow, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
            popupView.setAnimationStyle(R.style.anim_list_bottom);
            popupView.setTouchable(true);
            popupView.setOutsideTouchable(false);
            popupView.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            popupView.getContentView().setFocusableInTouchMode(true);
            popupView.getContentView().setFocusable(true);
            Button mButtonDismiss;
            mButtonDismiss = (Button) popupWindow.findViewById(R.id.button_closeHistory);
            TextView mTextHistory;
            mTextHistory = (TextView) popupWindow.findViewById(R.id.text_history);
            try {
                String historyFilePath;
                File textFile = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM).getAbsolutePath());
                //Open the history.txt file that stores the image process history and search history
                historyFilePath = textFile.getPath() + File.separator + "Camera" +
                        File.separator +"History" + File.separator + "History.txt";
                FileReader mFileReader = new FileReader(historyFilePath);
                BufferedReader mBufferedReader = new BufferedReader(mFileReader);
                String line = null;
                try {
                    while((line = mBufferedReader.readLine())!=null) {
                        mTextHistory.append(line);
                        mTextHistory.append("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mButtonDismiss.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((popupView!=null) && (popupView.isShowing())) {
                        popupView.dismiss();
                        //close the popupView by click on the button
                    }
                }
            });
            /*
            //This alternative method is to close the popup window by tap anywhere
            popupView.getContentView().setOnClickListener( new OnClickListener());
            */
            if ((popupView!=null) && (!popupView.isShowing())) {
                popupView.showAtLocation(findViewById(R.id.main_layout), Gravity.CENTER, 0, 0);
                //set back the original alpha
                tempLinearLayout.setAlpha(255);
            }
        }
        if (arg0 == mAboutMe) {
            //display a popup window show the basic developer info
            final LinearLayout tempLinearLayout = (LinearLayout) findViewById(R.id.main_layout);
            tempLinearLayout.setAlpha(0.5F);
            View popupWindow = getLayoutInflater().inflate(R.layout.popout_about, null);
            //LinearLayout.LayoutParams.MATCH_PARENT
            popupView = new PopupWindow(popupWindow, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
            popupView.setAnimationStyle(R.style.anim_list_bottom);
            popupView.setTouchable(true);
            popupView.setOutsideTouchable(false);
            popupView.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            popupView.getContentView().setFocusableInTouchMode(true);
            popupView.getContentView().setFocusable(true);
            Button mButtonDismiss;
            mButtonDismiss = (Button) popupWindow.findViewById(R.id.button_closeAboutMe);

            mButtonDismiss.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((popupView!=null) && (popupView.isShowing())) {
                        popupView.dismiss();
                        //close the popupView by click on the button
                    }
                }
            });
            /*
            //This alternative method is to close the popup window by tap anywhere
            popupView.getContentView().setOnClickListener( new OnClickListener());
            */
            if ((popupView!=null) && (!popupView.isShowing())) {
                popupView.showAtLocation(findViewById(R.id.main_layout), Gravity.CENTER, 0, 0);
                //set back the original alpha
                tempLinearLayout.setAlpha(255);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu mMenu) {
        super.onCreateOptionsMenu(mMenu);
        //Inflate the menu items for use in the action bar
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.menu_main, mMenu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_search:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //static { System.loadLibrary("opencv_java");}
}



