package com.mypersonalapp.androidjunittest;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.OnDoubleTapListener;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class MainActivity extends Activity implements OnGestureListener,OnDoubleTapListener{

    private float centerX, centerY; //coordinates of x and y
    private float textViewWidth, textViewHeight; // textview width and height
    private float layoutWidth, layoutHeight;     // layoutwidth and layoutheight
    private TextView assignmentText, CurrentTextView;  //instantiate two textview object
    private RelativeLayout relativeLayout;         //instantiate a relativelayout
    private GestureDetectorCompat gestureDetector;//instantiate a gesturedetector


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set main content
        setContentView(R.layout.activity_main);
        assignmentText  = (TextView) findViewById(R.id.the_assignment_text);
        //new a gesture detector object
        this.gestureDetector = new GestureDetectorCompat(this,this);
        //set double tap listener
        gestureDetector.setOnDoubleTapListener(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.mainlayout);
        //text_display();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //super.onTouchEvent(event)
        //check gesture type
        this.gestureDetector.onTouchEvent(event);
        //call superclass implementation
        //return true;
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event0) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent event0, MotionEvent event1, float event2, float event3) {

        return true;
    }

    @Override
    public void onShowPress(MotionEvent event0) {


    }

    @Override
    public boolean onDoubleTap(MotionEvent event0) {

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event0) {
        //set visibility of the textview
        //assignmentText.setVisibility(TextView.VISIBLE);
        //get the center coordinate of the screen
        centerX = relativeLayout.getRight()/2;
        centerY = relativeLayout.getBottom()/2;
        //throw log information of centerX and centerY
        Log.w("main", "x,y" + centerX + " " + centerY); //540, 850
        //set the center coordinate for assignment textview
        assignmentText.setX(centerX- (assignmentText.getMeasuredWidth()/2));
        assignmentText.setY(centerY-(assignmentText.getMeasuredHeight()/2));
        text_display();
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event0) {
        //check textview visibility

        if (assignmentText.getVisibility() == TextView.INVISIBLE){
            text_display();
        }
        else{
            assignmentText.setVisibility(TextView.INVISIBLE);
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent event0) {
        //does not required in this project
        return true;
    }


    @Override
    public boolean onFling(MotionEvent event0, MotionEvent event1, float event2, float event3) {
        //does not required in this project
        return true;
    }


    @Override
    public void onLongPress(MotionEvent event0) {
        //set the textview width and height
        textViewWidth = assignmentText.getMeasuredWidth();
        textViewHeight = assignmentText.getMeasuredHeight();
        //set the linear layout width and height
        layoutWidth = relativeLayout.getMeasuredWidth();
        layoutHeight = relativeLayout.getMeasuredHeight();
        //set the textview with center coordinate of the touchpoint
        assignmentText.setX(event0.getX() - (textViewWidth/2));
        //check entirety of the textview on the left and right
        if (assignmentText.getX() < 0){
            assignmentText.setX(0);
        }
        if (assignmentText.getX() > (layoutWidth - textViewWidth)){
            assignmentText.setX(layoutWidth - textViewWidth);
        }
        //check entirety of textview on the top and bottom
        assignmentText.setY(event0.getY() - (textViewHeight / 2));
        //check for entirety on the top and bottom
        if (assignmentText.getY() < 0) {
            assignmentText.setY(0);
        }
        if (assignmentText.getY() > (layoutHeight - textViewHeight)) {
            assignmentText.setY(layoutHeight - textViewHeight);
        }
        text_display();
        System.out.println(assignmentText.getX());
        System.out.println(assignmentText.getY());
    }

    //display the text
    public void text_display() {
        assignmentText.setVisibility(TextView.VISIBLE);
        //disappear or become invisible after 3 seconds
        Handler timeDelay = new Handler();

        timeDelay.postDelayed(new Runnable() {
            public void run() {
                assignmentText.setVisibility(TextView.INVISIBLE);
            }
        }, 3000);
    }

}
