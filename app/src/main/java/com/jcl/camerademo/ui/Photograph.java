package com.jcl.camerademo.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MotionEventCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jcl.camerademo.R;
import com.jcl.camerademo.Util.CoordinateTransformer;
import com.jcl.camerademo.Util.ImageSaver;
import com.jcl.camerademo.Util.MyImageSaver;
import com.jcl.camerademo.Util.Utils;
import com.jcl.camerademo.method.SetPreviewAndCapture;
import com.jcl.camerademo.method.VideoRecordUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


public class Photograph extends AppCompatActivity implements View.OnClickListener{

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private LinearLayout camera;
    private Uri imageUri;
    private HandlerThread handlerThread;
    private Handler handler;
    private SetPreviewAndCapture setPreviewAndCapture;
    private CameraDevice cameraDevice;
    private VideoRecordUtils videoRecordUtils;
    private SurfaceView surfaceView;
    private SurfaceHolder S_holder;
    private int currentCameraId = CameraCharacteristics.LENS_FACING_FRONT;//????????????????????????
    private Size previewSize;//????????????
    private Size mWinSize;//?????????????????????
    private ImageReader imageReader;//??????????????????
    public static final int TAKE_PHOTO = 1;
    private int flash_switch = 1;
    private CameraManager cameraManager;
    private ImageView camera_switch;
    private ImageView camera_flash;
    private ImageView photo;
    private ImageView cancel;
    private ImageView save;
    private int mSensorOrientation;
    private int mPhotoRotation;
    private RelativeLayout PhotoSave;
    private Rect mPreviewRect;
    private Rect mFocusRect;
    private CoordinateTransformer mCoordinateTransformer;
    private TextureView mTextureView;
    private MeteringRectangle[] AFRegions;
    private MeteringRectangle[] AERegions;
    private CameraCaptureSession mPreviewSession;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private ImageView video_fouces;

    private Runnable mImageFoucesRunnable = new Runnable() {
        @Override
        public void run() {
            video_fouces.setVisibility(View.GONE);
        }
    };

    /**
     * ??????????????????????????????
     */
    private Rect mZoomRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photograph);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mWinSize = Utils.loadWinSize(this);
        camera = findViewById(R.id.camera);
        camera_switch = findViewById(R.id.camera_switch);
        camera_flash = findViewById(R.id.camera_flash);
        photo = findViewById(R.id.photo);
        PhotoSave = findViewById(R.id.PhotoSave);
        cancel = findViewById(R.id.cancel);
        save = findViewById(R.id.savePhone);
        video_fouces = findViewById(R.id.video_fouces);
        surfaceView = findViewById(R.id.surfaceView);

        initView();
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        camera_switch.setOnClickListener(this);
        camera.setOnClickListener(this);
        camera_flash.setOnClickListener(this);



        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println(event.getX() + event.getY());
                int actionMasked = MotionEventCompat.getActionMasked(event);

                int fingerX, fingerY;
                int length = (int) (getResources().getDisplayMetrics().density * 80);
                switch (actionMasked) {
                    case MotionEvent.ACTION_DOWN:
                        fingerX = (int) event.getX();
                        fingerY = (int) event.getY();
                        System.out.println("????????????");
                        moveFouces(fingerX,fingerY);
                        break;
                    case MotionEvent.ACTION_UP:
                        System.out.println("??????");
                }

                return false;
            }
        });
    }

    /**
     * ???????????? ????????????
     */
    private void removeImageFoucesRunnable() {
        video_fouces.removeCallbacks(mImageFoucesRunnable);
    }

    /**
     * ?????? ??????????????????
     */
    private void imageFoucesDelayedHind() {
        video_fouces.postDelayed(mImageFoucesRunnable, 500);
    }
    private void moveFouces(int x, int y) {
        video_fouces.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) video_fouces.getLayoutParams();
        video_fouces.setLayoutParams(layoutParams);
        FoucesAnimation mFoucesAnimation = new FoucesAnimation();
        mFoucesAnimation.setDuration(500);
        mFoucesAnimation.setRepeatCount(0);
        mFoucesAnimation.setOldMargin(x, y);
        video_fouces.startAnimation(mFoucesAnimation);
        requestFocus(x,y);
    }
    public void requestFocus(float x, float y) {
        MeteringRectangle rect = calcTapAreaForCamera2(
                surfaceView.getWidth() / 5,
                1000, x, y);

        AFRegions = new MeteringRectangle[]{rect};
        AERegions = new MeteringRectangle[]{rect};

        setPreviewAndCapture = new SetPreviewAndCapture(cameraDevice, S_holder,
                imageReader, handler, Photograph.this, previewSize,AFRegions,AERegions);
        setPreviewAndCapture.startPreview();
    }


    /**
     * camera ??????????????????
     */
    private class FoucesAnimation extends Animation {

        private int width =  (int) (150 * getResources().getDisplayMetrics().density + 0.5f);
        private int W = (int) (65 * getResources().getDisplayMetrics().density + 0.5f);

        private int oldMarginLeft;
        private int oldMarginTop;

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) video_fouces.getLayoutParams();
            int w = (int) (width * (1 - interpolatedTime));
            if (w < W) {
                w = W;
            }
            layoutParams.width = w;
            layoutParams.height = w;
            if (w == W) {
                video_fouces.setLayoutParams(layoutParams);
                return;
            }
            layoutParams.leftMargin = oldMarginLeft - (w / 2);
            layoutParams.topMargin = oldMarginTop + (w / 8);
            video_fouces.setLayoutParams(layoutParams);
        }

        public void setOldMargin(int oldMarginLeft, int oldMarginTop) {
            this.oldMarginLeft = oldMarginLeft;
            this.oldMarginTop = oldMarginTop;
            removeImageFoucesRunnable();
            imageFoucesDelayedHind();
        }
    }
    /**
     * ??????????????????
     */
    public void startBackgroundThread() {
        mBackgroundThread = new HandlerThread(Photograph.class.getSimpleName());
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    private void toFocusRect(RectF rectF) {
        mFocusRect.left = Math.round(rectF.left);
        mFocusRect.top = Math.round(rectF.top);
        mFocusRect.right = Math.round(rectF.right);
        mFocusRect.bottom = Math.round(rectF.bottom);
    }

    private MeteringRectangle calcTapAreaForCamera2(int areaSize, int weight, float x, float y) {
        int left = clamp((int) x - areaSize / 2,
                mPreviewRect.left, mPreviewRect.right - areaSize);
        int top = clamp((int) y - areaSize / 2,
                mPreviewRect.top, mPreviewRect.bottom - areaSize);
        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
        toFocusRect(mCoordinateTransformer.toCameraSpace(rectF));
        return new MeteringRectangle(mFocusRect, weight);
    }
    public static void setBarTransparent(){

    }
    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    public void startAlbum(){
        Intent intent = new Intent(Photograph.this, Stickers.class);
        startActivity(intent);
    }
    public void startSlimming(){
        Intent intent = new Intent(Photograph.this, Slimming.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera:
                setPreviewAndCapture.takePhoto(flash_switch,mPhotoRotation);
                break;
            case R.id.camera_switch:
                switchCamera();
                break;
            case R.id.camera_flash:
                Object o = camera_flash.getTag();
                if (o == null || ((int) o) == 0) {
                    camera_flash.setImageResource(R.mipmap.icon_camera_on);
                    flash_switch = 2;
                    //camera_flash.setBackgroundResource(R.mipmap.icon_camera_on);
                    camera_flash.setTag(1);
                    //cameraHelper.flashSwitchState(ICamera2.FlashState.AUTO);
                } else if (((int) o) == 1) {
                    camera_flash.setImageResource(R.mipmap.icon_camera_a);
                    flash_switch = 3;
                    camera_flash.setTag(2);
                    // cameraHelper.flashSwitchState(ICamera2.FlashState.OPEN);
                }else if ((int)o == 2){
                    camera_flash.setImageResource(R.mipmap.icon_camera_off);
                    flash_switch = 1;
                    camera_flash.setTag(0);
                }
                break;
            case R.id.savePhone:
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PhotoSave.setVisibility(View.GONE);
                Toast.makeText(Photograph.this,"The photo is saved",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancel:
                PhotoSave.setVisibility(View.GONE);

        }
    }

    public void initView(){
        mFocusRect = new Rect();
        S_holder = surfaceView.getHolder();

        S_holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                Log.d("????????????", "surfaceCreated: " + surfaceView.getHeight() + ".." + surfaceView.getWidth());
                //??????????????????????????????
                System.out.println(surfaceView.getWidth()+"??????"+surfaceView.getHeight());
                mPreviewRect = new Rect(0, 0, surfaceView.getWidth(), surfaceView.getHeight());
                setAndOpenCamera();

                CameraCharacteristics cs = null;
                try {
                    cs = cameraManager.getCameraCharacteristics(String.valueOf(currentCameraId));
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                mCoordinateTransformer = new CoordinateTransformer(cs, new RectF(mPreviewRect));
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                //????????????????????????
                closeCamera();
            }
        });
        //??????????????????
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        //??????????????????????????????????????????
        handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }
    /**
     * ??????????????????????????????
     */
    private void switchCamera(){
        try{
            for (String cameraId : cameraManager.getCameraIdList()){
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                //??????????????????????????????????????????????????????????????????
                if (currentCameraId == CameraCharacteristics.LENS_FACING_FRONT &&
                        cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK){
                    //???????????????
                    currentCameraId = CameraCharacteristics.LENS_FACING_BACK;
                    //??????????????????
                    cameraDevice.close();
                    setAndOpenCamera();
                    break;
                }else if (currentCameraId == CameraCharacteristics.LENS_FACING_BACK &&
                        cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT){
                    //???????????????
                    currentCameraId = CameraCharacteristics.LENS_FACING_FRONT;
                    cameraDevice.close();
                    setAndOpenCamera();
                    break;
                }
            }
        }catch(CameraAccessException e){
            e.printStackTrace();
        }
    }
    public void setAndOpenCamera(){
        CameraCharacteristics cameraCharacteristics = null;
        try {
            //???????????????????????????
            cameraCharacteristics = cameraManager.getCameraCharacteristics(String.valueOf(currentCameraId));
          //  mCoordinateTransformer = new CoordinateTransformer(cameraCharacteristics, new RectF(mPreviewRect));
            mSensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            mPhotoRotation = getOrientation(getWindowManager().getDefaultDisplay().getRotation());
            //????????????
            float maxZoom = (cameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM))*10;
            Log.d("??????????????????", "switchCamera: "+maxZoom);
            //?????????????????????????????????????????????
            StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            previewSize = Utils.fitPhotoSize(map,mWinSize);
            //?????????imageReader
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????2??????ImageReader????????????????????????????????????
            imageReader = ImageReader.newInstance(previewSize.getWidth(), previewSize.getHeight(), ImageFormat.JPEG,2);
            imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    System.out.println("????????????????????????");
                    Image image = reader.acquireNextImage();
                    handler.post(new MyImageSaver(image, Photograph.this));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.capacity()];
                            buffer.get(bytes);
                            Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
                            Bitmap bitmap = adjustPhotoRotation(bitmapImage, 90);
                            photo.setImageBitmap(bitmap);
                            PhotoSave.setVisibility(View.VISIBLE);
                        }
                    });

                }

            },handler);
            //???????????????
            openCamera();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void openCamera(){
        //??????????????????????????????
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            return;
        }
        try {
            //???????????????
            cameraManager.openCamera(String.valueOf(currentCameraId), stateCallback,null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    // ??????????????????
    public Bitmap adjustPhotoRotation(Bitmap bm, int orientationDegree) {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

            return bm1;

        } catch (OutOfMemoryError ex) {
        }
        return null;

    }

    private int getOrientation(int rotation) {
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }
    /**
     * ???????????????????????????????????????CameraDevice??????
     */
    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            //??????????????????????????????????????????????????????,????????????CameraDevice????????????????????????
            setPreviewAndCapture = new SetPreviewAndCapture(cameraDevice, S_holder,
                    imageReader, handler, Photograph.this, previewSize,null,null);
            setPreviewAndCapture.startPreview();
            //???????????????????????????
            videoRecordUtils = new VideoRecordUtils();
            videoRecordUtils.create(surfaceView, cameraDevice, VideoRecordUtils.WH_720X480);

        }
        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            cameraDevice = null;
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handlerThread = new HandlerThread("Camera");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        Bitmap bitmap = null;
        byte[] bytes = new byte[1024];
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        bytes = inputStreamToByteArray(inputStream);
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    System.out.println("????????????");
                    handler.post(new ImageSaver(bitmap,this,bytes));

                    Intent intent = new Intent(Photograph.this, Photograph.class);
                    startActivity(intent);
                    Toast.makeText(this, "The picture is saved", Toast.LENGTH_SHORT).show();
                    finish();

                }
                break;
            default:
                break;
        }
    }

    private void startCamera() {
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        // ????????????????????????
        try {
            // ??????????????????????????????????????????
            if (outputImage.exists()) {
                outputImage.delete();
            }
            // ????????????????????????
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ??????Android??????????????????7.0
        if (Build.VERSION.SDK_INT >= 24) {
            // ???File?????????????????????????????????Uri??????
            imageUri = FileProvider.getUriForFile(this, "com.example.lenovo.cameraalbumtest.fileprovider", outputImage);
            Log.d("MainActivity", outputImage.toString() + "????????????????????????Android7.0");
        } else {
            // ???File???????????????Uri???????????????Uri?????????output_image.jpg?????????????????????????????????
            Log.d("MainActivity", outputImage.toString() + "????????????????????????Android7.0");
            imageUri = Uri.fromFile(outputImage);
        }

        Intent intent4 = new Intent("android.media.action.IMAGE_CAPTURE");
        // ??????????????????????????????imageUri
        intent4.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent4, TAKE_PHOTO);

    }
    /**
     * inputStream???byte??????
     *
     * @param inputStream ???????????????
     * @return byte??????
     */
    public static byte[] inputStreamToByteArray(InputStream inputStream) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int num;
            while ((num = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, num);
            }
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
    /**
     * ????????????
     */
    private void closeCamera() {

        //????????????
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
        //?????????????????????
        if (imageReader != null) {
            imageReader.close();
            imageReader = null;
        }
    }
}