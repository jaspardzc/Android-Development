package com.mypersonalapp.androidgourmetguider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.opencv.core.Mat;

/**
 * Created by jaspe_000 on 3/22/2015.
 */
public class CropView extends View {

    private Paint mCropPaint;
    private TextPaint mTextPaint;
    private float x_start, y_start;
    private float x_end, y_end;
    private boolean startDraw = false;

    private OnUpCallBack mCallBack = null;


    public interface OnUpCallBack {
        void onCropFinished(Rect mRect);

    }
    public CropView (final Context context) {
        super(context);
        x_start = 0;
        y_start = 0;
        x_end = 0;
        y_end = 0;
        setPaint();
    }

    public CropView (final Context context, final AttributeSet attributeSet) {
        super(context, attributeSet);
        x_start = 0;
        y_start = 0;
        x_end = 0;
        y_end = 0;
        setPaint();
    }

    public CropView (final Context context, final AttributeSet attributeSet, final int style) {
        super(context, attributeSet, style);
        x_start = 0;
        y_start = 0;
        x_end = 0;
        y_end = 0;
        setPaint();
    }

    public void setOnUpCallBack (OnUpCallBack onUpCallBack) {
        mCallBack = onUpCallBack;
    }

    public void setPaint() {
        mCropPaint = new Paint();
        mCropPaint.setColor(getContext().getResources().getColor(R.color.wallet_holo_blue_light));
        mCropPaint.setStyle(Paint.Style.STROKE);
        mCropPaint.setStrokeWidth(5);
        mTextPaint = new TextPaint();
        mTextPaint.setColor(getContext().getResources().getColor(R.color.wallet_holo_blue_light));
        mTextPaint.setTextSize(20);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startDraw = false;
                x_start = event.getX();
                y_start = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float x_temp = event.getX();
                float y_temp = event.getY();
                if (!startDraw
                        ||(Math.abs(x_temp - x_start) > 3)
                        ||(Math.abs(y_temp - y_start) > 3)) {
                    x_end = x_temp;
                    y_end = y_temp;
                    invalidate();
                }
                startDraw = true;
                break;
            case MotionEvent.ACTION_UP:
                if (mCallBack !=  null) {
                    //left, top, right, bottom, integer type
                    mCallBack.onCropFinished(new Rect((int)Math.min(x_start,x_end),
                            (int)Math.min(y_start, y_end),
                            (int)Math.max(x_start, x_end),
                            (int)Math.max(y_start, y_end)));
                }
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (startDraw) {
            canvas.drawRect(Math.min(x_start, x_end), Math.min(y_start, y_end),
                    Math.max(x_start, x_end), Math.max(y_start, y_end), mCropPaint);
            canvas.drawText("( " + Math.abs(x_start - x_end) + "," + Math.abs(y_start - y_end),
                    Math.max(x_start, x_end), Math.max(y_start, y_end), mTextPaint);
        }
    }

    public float getCropLeft() {
        return Math.min(x_start, x_end);
    }

    public float getCropTop() {
        return Math.min(y_start, y_end);
    }

    public float getCropWidth() {
        return Math.abs(x_start - x_end);
    }

    public float getCropHeight() {
        return Math.abs(y_start - y_end);
    }
}
