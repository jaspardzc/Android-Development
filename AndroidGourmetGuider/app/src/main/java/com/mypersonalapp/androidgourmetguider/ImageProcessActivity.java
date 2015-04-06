package com.mypersonalapp.androidgourmetguider;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.Highgui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ImageProcessActivity extends Activity implements OnClickListener {

    static { System.loadLibrary("opencv_java");}
    private Button mButtonImageSync, mButtonSearching;
    private Button mButtonBlur,mButtonBrighter, mButtonSharpen, mButtonCrop;
    private Button mButtonImageIdentifier, mButtonImageCombiner;
    private Button mButtonConfirm, mButtonError;
    private EditText mEditKeywords;
    private ImageView mImageResult;

    private final static int IMAGE_PICK_CODE = 1000;
    private final static int SEND_KEYWORDS = 666;

    private Bitmap mImageBitmap, mResultBitmap;
    private String timeStamp;
    private String outFilePath;
    //private Mat imageMat, blurMat, bilateralMat, brighterMat, sharpMat;

    enum image {GaussianImg, BilateralImg, BrighterImg, CannyImg};
    private final static String  TAG = ".ImageProcessActivity";

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.w(TAG,"OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);
        mButtonSearching = (Button) findViewById(R.id.button_imageSearch);
        mButtonSearching.setOnClickListener(this);
        mButtonImageSync = (Button) findViewById(R.id.button_imageSync);
        mButtonImageSync.setOnClickListener(this);
        mButtonBlur = (Button) findViewById(R.id.button_blur);
        mButtonBlur.setOnClickListener(this);
        mButtonBrighter = (Button) findViewById(R.id.button_brighter);
        mButtonBrighter.setOnClickListener(this);
        mButtonSharpen = (Button) findViewById(R.id.button_sharpen);
        mButtonSharpen.setOnClickListener(this);
        mButtonCrop = (Button) findViewById(R.id.button_crop);
        mButtonCrop.setOnClickListener(this);
        mButtonImageIdentifier = (Button) findViewById(R.id.button_imageIdentify);
        mButtonImageIdentifier.setOnClickListener(this);
        mButtonImageCombiner = (Button) findViewById(R.id.button_imageCombine);
        mButtonImageCombiner.setOnClickListener(this);
        mButtonConfirm = (Button) findViewById(R.id.button_confirm);
        mButtonConfirm.setOnClickListener(this);
        mButtonError = (Button) findViewById(R.id.button_Error);
        mButtonError.setOnClickListener(this);

        mImageResult = (ImageView) findViewById(R.id.image_result);
        mEditKeywords = (EditText) findViewById(R.id.edit_keywords);
    }

    @Override
    public void onResume() {
        super.onResume();
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, this, mLoaderCallback);
    }

    /**
     * This function specify to receive results from which intent, created by YANG_SONG
     */
    protected  void onActivityResult(int requestCode, int resultCode, Intent data ) {
        if (requestCode == IMAGE_PICK_CODE) {
            if (resultCode == RESULT_OK) {

            }
        }
    }

    @Override
    public void onClick(View arg0) {
        if (arg0 == mButtonSearching) {
            //getKeywords();
            //cannot directly use intent, use startActivity for result
            try {
                Intent myIntent = new Intent(ImageProcessActivity.this, LocationMapActivity.class);
                myIntent.putExtra("Keywords", getKeywords().toString());
                startActivity(myIntent);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (arg0 == mButtonImageSync) {
            //Synchronize the mImageResult with the preview in the MainActivity
            Bundle bundle = this.getIntent().getExtras();
            byte[] b = bundle.getByteArray("image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            mImageResult.setImageBitmap(bitmap);
        }

        if (arg0 == mButtonBlur) {
            //popup window to decide using which filter
            Toast.makeText(getBaseContext(), "Blur filter: Gaussian or Bilateral ?",
                    Toast.LENGTH_SHORT).show();
            //filterGaussian();
            //filterBilateral();
        }
        if (arg0 == mButtonBrighter) {
            Toast.makeText(getBaseContext(), "Starting the brighter filter",
                    Toast.LENGTH_SHORT).show();
            //filterBrighter();
        }
        if (arg0 == mButtonSharpen) {
            Toast.makeText(getBaseContext(), "Starting the sharpen filter",
                    Toast.LENGTH_SHORT).show();
            //filterSharpen();
        }
        if (arg0 == mButtonCrop) {
            Toast.makeText(getBaseContext(), "Choose the crop boundary",
                    Toast.LENGTH_SHORT).show();
            cropImage();
        }

        if (arg0 == mButtonImageIdentifier) {
            //use camFind api to identify the contents, high accuracy, not implemented in Alpha

        }

        if (arg0 == mButtonImageCombiner) {
            //Not implemented in Alpha, can be placed in the same row with button_identify

        }

        if (arg0 == mButtonConfirm) {
            //save image processing history
            String ImgProcPath = outFilePath;
            if (ImgProcPath != null) {
                File ImgHistoryFile = new File(ImgProcPath + File.separator +
                        "History" + File.separator + "History.txt");
                if (!ImgHistoryFile.exists()) {
                    ImgHistoryFile.mkdirs();
                }
                try {
                    String content = "<Img>" + ImgHistoryFile.getPath() + File.separator +
                            "IMG_" + timeStamp + ".jpg" + "<Img>" + "" +
                            "Processed on: " + timeStamp + "\n";
                    FileOutputStream fos = new FileOutputStream(ImgHistoryFile);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    bos.write(content.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(getBaseContext(), "Processing History Sync Success, picture saved"
                    , Toast.LENGTH_LONG).show();
            //store the image result as a new image file, not to override the original src image
            storeKeyWords();
        }
        if (arg0 == mButtonError) {
            //set the imageView preview to default color white
            //TODO Debug
            if (mImageResult.getDrawable() == null) {
                Toast.makeText(getBaseContext(), "!!The imageView is empty",
                        Toast.LENGTH_LONG).show();
            }
            else {
                mImageResult.setBackgroundColor(Color.rgb(255, 255, 255));
                Toast.makeText(getBaseContext(), "So the wrong process result has been deleted"
                        , Toast.LENGTH_LONG).show();
                //delete the image process result in the file dir in the /sdcard/DCIM/Camera
                File deleteFileDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM).getAbsolutePath());
                if (deleteFileDir.exists()) {
                    String deleteFilePath = deleteFileDir + File.separator + "Camera" +
                            File.separator + "IMG_" + timeStamp + ".jpg";
                    File deleteFile = new File(deleteFilePath);
                    if (deleteFile.exists()) {
                        boolean deleted = deleteFile.delete();
                        Log.w("Log_Tag", "Deleted: " + deleteFileDir + "/" + deleteFile + deleted);
                    }
                }
            }
        }
    }

    public void imageViewToBitmap() {
        if (mImageResult.getDrawable() == null) {
            Toast.makeText(ImageProcessActivity.this, "The ImageView is Empty",
                    Toast.LENGTH_LONG).show();
        } else {

            mImageResult.setDrawingCacheEnabled(true);
            //without this, the view will have dimension of 0,0 and bitmap will be null
            mImageResult.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            mImageResult.layout(0, 0, mImageResult.getMeasuredWidth(),
                    mImageResult.getMeasuredHeight());
            mImageResult.buildDrawingCache(true);
            mImageBitmap = Bitmap.createBitmap(mImageResult.getDrawingCache());
            mImageResult.setDrawingCacheEnabled(false);
            //clear the drawing cache
            Toast.makeText(getBaseContext(), "Transfer the imageView to Bitmap",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /*
    public void filterGaussian() {
        //blur the image
        //Imgproc.GaussianBlur(src, dst, Size, Sigmax);
        imageViewToBitmap();
        //TODO
        imageMat = new Mat();
        Utils.bitmapToMat(mImageBitmap, imageMat);
        blurMat = new Mat(imageMat.rows(), imageMat.cols(), imageMat.type());
        Imgproc.GaussianBlur(imageMat, blurMat, new Size(20, 20), 0);
        Utils.matToBitmap(blurMat, mResultBitmap);
        updateImageResult();

    }

    public void filterBilateral() {
        //smooth the image
        imageViewToBitmap();

        imageMat = new Mat();
        Utils.bitmapToMat(mImageBitmap, imageMat);
        bilateralMat = new Mat(imageMat.rows(), imageMat.cols(), imageMat.type());
        Imgproc.bilateralFilter(imageMat, bilateralMat,  9, 75, 75);
        Utils.matToBitmap(bilateralMat, mResultBitmap);
        updateImageResult();

    }

    public void filterBrighter() {
        //enhancing the image brightness by multiplying alpha to the pixel, and add a beta;
        //sourceImage.convertTo(dst, rtype, alpha, beta);
        double alpha = 2;
        double beta = 50;
        imageViewToBitmap();
        imageMat = new Mat();
        Utils.bitmapToMat(mImageBitmap, imageMat);
        brighterMat = new Mat(imageMat.rows(), imageMat.cols(), imageMat.type());
        imageMat.convertTo(brighterMat, -1, alpha, beta);
        //Highgui.imwrite(".jpg", brighterMat);
        Utils.matToBitmap(brighterMat, mResultBitmap);
        updateImageResult();
    }

    public void filterSharpen() {
        //enhance the image sharpness
        //Imgproc.GaussianBlur(src, dst, new Size(0,0), sigmax);
        //Core.addWeighted(InputArray src1, alpha, src2, beta, gama, OutputArray dst);
        imageViewToBitmap();
        imageMat = new Mat();
        Utils.bitmapToMat(mImageBitmap, imageMat);
        sharpMat = new Mat(imageMat.rows(), imageMat.cols(), imageMat.type());
        Imgproc.GaussianBlur(imageMat, sharpMat, new Size(0, 0), 10);
        Core.addWeighted(imageMat, 1.5, sharpMat, -0.5, 0, sharpMat);
        Utils.matToBitmap(sharpMat, mResultBitmap);
        //enhance the image contrast
        //Imgproc.equalizeHist(src, dst);
        updateImageResult();
    }
    */

    public void cropImage() {
        //setting the crop boundary
        final CropView mCropView = (CropView) findViewById(R.id.cropView);
        if (null != mCropView) {
            mCropView.setOnUpCallBack(new CropView.OnUpCallBack() {
                @Override
                public void onCropFinished(final Rect mRect) {
                    Toast.makeText(getBaseContext(), "CropBoundary is set successfully",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        //resize and crop the image
        float cropLeft = mCropView.getCropLeft();
        float cropTop = mCropView.getCropTop();
        float cropRight = mCropView.getCropWidth();
        float cropBottom = mCropView.getCropHeight();
        Bitmap newBitmap = Bitmap.createBitmap(mImageBitmap, (int)cropLeft, (int)cropTop,
                (int)cropRight, (int)cropBottom, null, false);
        mImageResult.setImageBitmap(newBitmap);
    }

    public void identifyImage() {
        //send json request to the camFind api server, and retrieve json results
        //parsing the json data results, set the keywords
        String jsonString = null;

        mEditKeywords.setText(jsonString);
    }

    public void combineImage() {
        //Not implemented in Alpha

    }

    public void updateImageResult() {
        mImageResult.setImageBitmap(mResultBitmap);
        storeImageResult();
    }

    public void storeImageResult() {
        //save the imageview to local storage, a new image file
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        outFilePath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).getAbsolutePath();
        outFilePath += File.separator + "Camera" + File.separator +
                "Proc_IMG_" + timeStamp + ".jpg";
        File outFile = new File(outFilePath);
        if (outFile.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(outFile);
                mResultBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(getBaseContext(), "Your processed result has been currently stored, " +
                "please press confirm or error to continue", Toast.LENGTH_LONG).show();
    }

    public void storeKeyWords() {
        getKeywords();
    }

    public String getKeywords() {
        String keyWords;
        keyWords = mEditKeywords.getText().toString();
        return keyWords;
    }
}
