package com.mypersonalapp.androidjunittest;
/**
 * CAN ZENG
 * This app is intended for the unit test for the hw01
 * Tested in Nexus 5
 * Contains 15 Test Cases on single tap, text visibility, text entirety, double tap, long press
 */
//import com.mypersonalapp.androidjunittest.MainActivity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.InputDevice;
import android.view.View;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;
    private RelativeLayout mRelativeLayout;
    private TextView mTextView;
    private float centerX, centerY;
    private String mDefaultString = "CAN ZENG" + "\n" + "ECE573 hw01";
    private String mCurrentString = "This is the unit test of hw01";

    /**
     * Constructor for ApplicationTest
     */
    public ApplicationTest() {
        super(MainActivity.class);
    }

    /**
     * setUp() with a toast
     */
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = this.getActivity();
        //get the default string find by id
        mTextView = (TextView) mMainActivity.findViewById(R.id.the_assignment_text);
        mRelativeLayout = (RelativeLayout) mMainActivity.findViewById(R.id.mainlayout);
        //set the initialized value of center coordinates
        centerX = mTextView.getX();
        centerY = mTextView.getY();
        Toast.makeText(this.mMainActivity, mCurrentString, Toast.LENGTH_SHORT).show();
    }

    /**
     * Test case 1: text invisible when starting the app
     */
    public void test_1_Text_Visibility_On_Start_App() {
        assertEquals(View.INVISIBLE, mTextView.getVisibility());
        //assert if not equals
    }
    /**
     * On single tap
     * Test case 2 -> Test case 6
     * More Details specified in the "Test Case.txt" file
     */
    public void test_2_Text_Visibility_On_SingleTap_Confirmed() {
        /**
         * tapView(InstrumentationTestCase test, View v) is deprecated in this project,
         * it only simulates the tap on the center position
         * and clickView is deprecated as well
         * Random() is used to generate random coordinate
         */
        Random mRandom = new Random();
        float x_position = mRandom.nextFloat()*(mRelativeLayout.getWidth() - 1) + 1;
        float y_position = mRandom.nextFloat()*(mRelativeLayout.getHeight() - 1) + 1;
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        //10ms delay between press down and lift
        eventTime += 10;
        mInstrumentation.sendPointerSync(MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();;
        }
        assertEquals(View.VISIBLE, mTextView.getVisibility());
        //assert if the textView is invisible
    }

    public void test_3_Text_Content_On_SingleTap_Confirmed() {
        /**
         * tapView(InstrumentationTestCase test, View v) is deprecated in this project,
         * it only simulates the tap on the center position
         * and clickView is deprecated as well
         * Random() is used to generate random coordinate
         */
        Random mRandom = new Random();
        float x_position = mRandom.nextFloat()*(mRelativeLayout.getWidth() - 1) + 1;
        float y_position = mRandom.nextFloat()*(mRelativeLayout.getHeight() - 1) + 1;
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        //10ms delay between press down and lift
        eventTime += 10;
        mInstrumentation.sendPointerSync(MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();;
        }
        assertEquals(mDefaultString, mTextView.getText());
        //asset if content is not equal
    }

    public void test_4_Text_Position_On_SingleTap_Confirmed() {
        /**
         * tapView(InstrumentationTestCase test, View v) is deprecated in this project,
         * it only simulates the tap on the center position
         * and clickView is deprecated as well
         * Random() is used to generate random coordinate
         */
        Random mRandom = new Random();
        float x_position = mRandom.nextFloat()*(mRelativeLayout.getWidth() - 1) + 1;
        float y_position = mRandom.nextFloat()*(mRelativeLayout.getHeight() - 1) + 1;
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        //10ms delay between press down and lift
        eventTime += 10;
        mInstrumentation.sendPointerSync(MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();;
        }
        assertEquals(centerX, mTextView.getX());
        //assert if the x coordinate does not match the default value
        assertEquals(centerY, mTextView.getY());
        //assert if the y coordinate does not match the default value
    }

    public void test_5_Text_Duration_On_SingleTap_Confirmed() {
        /**
         * tapView(InstrumentationTestCase test, View v) is deprecated in this project,
         * it only simulates the tap on the center position
         * and clickView is deprecated as well
         * Random() is used to generate random coordinate
         */
        Random mRandom = new Random();
        float x_position = mRandom.nextFloat()*(mRelativeLayout.getWidth() - 1) + 1;
        float y_position = mRandom.nextFloat()*(mRelativeLayout.getHeight() - 1) + 1;
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        //10ms delay between press down and lift
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis()+10, MotionEvent.ACTION_UP, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(4000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        assertEquals(View.INVISIBLE, mTextView.getVisibility());
        //assert if the text is still visible after 3 seconds
    }

    public void test_6_Text_Disappear_On_Two_SingleTap() {
        /**
         * tapView(InstrumentationTestCase test, View v) is deprecated in this project,
         * it only simulates the tap on the center position
         * and clickView is deprecated as well
         * Random() is used to generate random coordinate
         */
        //where bad happens, the text is still visible
        Random mRandom = new Random();
        float x_position = mRandom.nextFloat()*(mRelativeLayout.getWidth() - 1) + 1;
        float y_position = mRandom.nextFloat()*(mRelativeLayout.getHeight() - 1) + 1;
        Instrumentation mInstrumentation = new Instrumentation();
        //First single tap
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        //10ms delay between press down and lift
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis()+10, MotionEvent.ACTION_UP, x_position, y_position, 1));
        //set 1500ms interval between two single taps
        try {
            synchronized (this) {
                wait(1500);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        assertEquals(View.VISIBLE, mTextView.getVisibility());
        //Second single tap
        float x_position_2 = mRandom.nextFloat()*(mRelativeLayout.getWidth() - 1) + 1;
        float y_position_2 = mRandom.nextFloat()*(mRelativeLayout.getHeight() - 1) + 1;
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,
                x_position_2, y_position_2, 1));
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis()+10, MotionEvent.ACTION_UP,
                x_position_2, y_position_2, 1));
        try {
            synchronized (this) {
                wait(500);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        assertEquals(View.INVISIBLE, mTextView.getVisibility());
        //assert if the text is still visible after two single tap
    }

    /**
     * On long press
     * Test Case 7 -> Test Case 10
     * More Details specified in the "Test Case.txt" file
     */

    public void test_7_Text_Visibility_On_LongPress () {

        Random mRandom = new Random();
        float x_position = mRandom.nextFloat()*(mRelativeLayout.getWidth() - 1) + 1;
        float y_position = mRandom.nextFloat()*(mRelativeLayout.getHeight() - 1) + 1;
        //simulate long press
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 1000, MotionEvent.ACTION_UP, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(1500);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        assertEquals(View.VISIBLE, mTextView.getVisibility());
        //assert if the text is not visible
    }

    public void test_8_Text_Duration_On_LongPress() {

        float y_position = mRelativeLayout.getHeight() - mTextView.getMeasuredHeight();
        float x_position = mRelativeLayout.getWidth() - mTextView.getMeasuredWidth();
        //simulate long press
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis()+1000, MotionEvent.ACTION_UP, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(4000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        assertEquals(View.INVISIBLE, mTextView.getVisibility());
        //assert if the text is not invisible
    }

    public void test_9_SingleTap_Functionality_On_LongPress() {

        float y_position = mRelativeLayout.getHeight() - mTextView.getMeasuredHeight();
        float x_position = mRelativeLayout.getWidth() - mTextView.getMeasuredWidth();
        //simulate long press and wait for 1 second, and simulate single tap to disappear the text
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis()+1000, MotionEvent.ACTION_UP, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();;
        }
        //perform single tap, and just tap on the text will disappear the text
        //the comment out function is left for single tap on any part of the image

        Random mRandom = new Random();
        float x_position_singleTap = mRandom.nextFloat()*(mRelativeLayout.getWidth() - 1) + 1;
        float y_position_singleTap = mRandom.nextFloat()*(mRelativeLayout.getHeight() - 1) + 1;
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x_position_singleTap,
                y_position_singleTap, 1));
        //10ms delay between press down and lift
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 10, MotionEvent.ACTION_UP, x_position_singleTap,
                y_position_singleTap, 1));
        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        assertEquals(View.INVISIBLE, mTextView.getVisibility());
        //assert if the text is not invisible
    }


    public void test_10_Text_Position_On_LongPress() {
        /**
         * Entirety of Text is not checked here, checked in other test cases
         */
        float y_position = mRelativeLayout.getHeight() - mTextView.getMeasuredHeight();
        float x_position = mRelativeLayout.getWidth() - mTextView.getMeasuredWidth();
        //simulate long press and wait for two seconds
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(2000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        //check x coordinate
        assertEquals(x_position - (mTextView.getMeasuredWidth()/2), mTextView.getX(), 1);
        //check y coordinate
        assertEquals(y_position - (mTextView.getMeasuredHeight()/2), mTextView.getY(), 1);
    }

    /**
     * test the text entirety
     * Test Case 11 -> Test Case 14
     * More Details specified in the "Test Case.txt" file
     */
    public void test_11_Text_Entirety_SouthWest() {

        float x_position = 0;
        float y_position = mRelativeLayout.getHeight();
        //simulate long press and wait for 2 seconds
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis()+1000, MotionEvent.ACTION_UP, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        assertTrue(x_position <= mTextView.getX());
        assertTrue((y_position - mTextView.getMeasuredHeight()) >= mTextView.getY());
        assertEquals(View.VISIBLE, mTextView.getVisibility());
    }

    public void test_12_Text_Entirety_SouthEast() {

        float x_position = mRelativeLayout.getWidth();
        float y_position = mRelativeLayout.getHeight();
        //simulate long press and wait for 2 seconds
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis()+1000, MotionEvent.ACTION_UP, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        assertTrue((x_position - mTextView.getMeasuredWidth()) >= mTextView.getX());
        assertTrue((y_position - mTextView.getMeasuredHeight()) >= mTextView.getY());

    }


    public void test_13_Text_Entirety_NorthEast() {

        float x_position = mRelativeLayout.getWidth();
        float y_position = 0;
        //simulate long press and wait for 2 seconds
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis()+1000, MotionEvent.ACTION_UP, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        assertTrue((x_position - mTextView.getMeasuredWidth()) >= mTextView.getX());
        assertTrue(y_position <= mTextView.getY());

    }

    /**
     * On double tap
     * Test Case 15 -> Test Case 16
     * More Details specified in the "Test Case.txt" file
     */
    public void test_14_Text_Visibility_On_DoubleTap() {

        //get position
        float x_position = mRelativeLayout.getWidth();
        float y_position = mRelativeLayout.getHeight();
        // simulate long press and wait for 1 second
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis()+1000, MotionEvent.ACTION_UP, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        //perform double tap anywhere to reset the position of the textview to the center of the image
        Random mRandom = new Random();
        float x_position_singleTap = mRandom.nextFloat()*(mRelativeLayout.getWidth() - 1) + 1;
        float y_position_singleTap = mRandom.nextFloat()*(mRelativeLayout.getHeight() - 1) + 1;
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        //perform double tap
        mInstrumentation.sendPointerSync(MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x_position_singleTap, y_position_singleTap, 1));
        //10ms delay between press down and lift
        eventTime += 10;
        mInstrumentation.sendPointerSync(MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, x_position_singleTap, y_position_singleTap, 1));
        //set minimal interval
        try {
            synchronized (this) {
                wait(50);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        //reset the time
        downTime = SystemClock.uptimeMillis();
        eventTime = SystemClock.uptimeMillis();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x_position_singleTap, y_position_singleTap, 1));
        //10ms delay between press down and lift
        eventTime += 10;
        mInstrumentation.sendPointerSync(MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, x_position_singleTap, y_position_singleTap, 1));
        try {
            synchronized (this) {
                wait(500);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        assertEquals(View.VISIBLE, mTextView.getVisibility());
    }

    public void test_15_Text_Position_On_DoubleTap() {

        //get position
        float x_position = mRelativeLayout.getWidth();
        float y_position = mRelativeLayout.getHeight();
        // simulate long press and wait for 1 second
        Instrumentation mInstrumentation = new Instrumentation();
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x_position, y_position, 1));
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis()+1000, MotionEvent.ACTION_UP, x_position, y_position, 1));
        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        //perform double tap to reset the postion of the textview to the center of the image
        Random mRandom = new Random();
        float x_position_singleTap = mRandom.nextFloat()*(mRelativeLayout.getWidth() - 1) + 1;
        float y_position_singleTap = mRandom.nextFloat()*(mRelativeLayout.getHeight() - 1) + 1;

        //perform double tap
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),MotionEvent.ACTION_DOWN,
                x_position_singleTap, y_position_singleTap, 1));
        //10ms delay between press down and lift
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 10, MotionEvent.ACTION_UP,
                x_position_singleTap, y_position_singleTap, 1));
        try {
            synchronized (this) {
                wait(50);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,
                x_position_singleTap, y_position_singleTap, 1));
        //10ms delay between press down and lift
        mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 10, MotionEvent.ACTION_UP,
                x_position_singleTap, y_position_singleTap, 1));
        try {
            synchronized (this) {
                wait(500);
            }
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
        //check x coordinate
        assertEquals(centerX, mTextView.getX(), 1);
        //check y coordinate
        assertEquals(centerY, mTextView.getY(), 1);
    }

}