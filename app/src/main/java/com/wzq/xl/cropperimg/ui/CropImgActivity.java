
package com.wzq.xl.cropperimg.ui;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.*;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.edmodo.cropper.CropImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import com.wzq.xl.cropperimg.R;
import com.wzq.xl.cropperimg.imgtool.ImageLoaderConfig;
import com.wzq.xl.cropperimg.imgtool.ImageTool;
import com.wzq.xl.cropperimg.imgtool.ImgCompressTool;
import com.wzq.xl.cropperimg.utils.BroadcastUtils;
import com.wzq.xl.cropperimg.utils.EditDataUtils;
import com.wzq.xl.cropperimg.utils.ImageCacheUtils;
import com.wzq.xl.cropperimg.utils.IntentDataUtils;

import java.io.File;

/*
* @author wzq
* @createtime 20160314
* */
public class CropImgActivity extends BaseActivity implements View.OnClickListener {

    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";

    private ImageButton ib_back;

    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

    private String save_crop_path = "";
    private CropImageView cropImageView;
    private Button cropButton;
    private String cropImgTag;
    private Thread thread;
    private boolean CROP_IMG_TYPE = true;
    private ViewGroup root;

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }


    private void initView() {
        root = (ViewGroup) findViewById(R.id.mylayout);
        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);
        cropButton = (Button) findViewById(R.id.Button_crop);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
    }





    private void initData() {
        Uri url = getIntent().getExtras().getParcelable(IntentDataUtils.CROP_IMG_URI);
        cropImgTag = getIntent().getExtras().getString(IntentDataUtils.CROP_IMG_TAG);
        save_crop_path = getIntent().getExtras().getString(IntentDataUtils.SAVE_IMG_PATH);
        CROP_IMG_TYPE = getIntent().getExtras().getBoolean(IntentDataUtils.CROP_IMG_TYPE_SQU);
        cropImageView.setCropImgTypeSqu(CROP_IMG_TYPE);
        if (url != null && !url.equals("")) {
            try {

                ImageLoader.getInstance().displayImage(url.toString(), cropImageView.getImageView(), ImageLoaderConfig.getSpecialOption(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        cropImageView.display(bitmap);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            } catch (Exception e) {

            }
        }
    }

    private void initListener() {
        cropButton.setOnClickListener(this);
        ib_back.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_img);
        initView();
        initData();
        initListener();
    }


    public void setFont(ViewGroup group, Typeface font) {//设置字体
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Button_crop:
                cropPic();
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }



    private void cropPic()//切图啦
    {

        if (cropImageView.getmBitmap() == null) {
            showToast("cut image error");
            cropImageView.recycleBitMap();
            return;
        }
        buildLoadingDialog();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri save_path_uri = null;
                Bitmap bitmap = cropImageView.getCroppedImage();
                try {
                    save_path_uri = ImgCompressTool.getInstance().compressAndGenImageRetUrl(bitmap, save_crop_path + File.separator + System.currentTimeMillis() + ".png",  EditDataUtils.picMaxSize);
                } catch (Exception e) {
                    save_path_uri = null;
                }
                if (save_path_uri == null) {
                    save_path_uri = ImageTool.savePhotoToSDCard(bitmap, save_crop_path, System.currentTimeMillis() + "");
                }

                if (save_path_uri == null) {
                    showToast("save image error");
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();

                    }
                    System.gc();
                    cancelLoadingDialog();
                    return;
                }
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                ImageLoader.getInstance().destroy();
                if (!ImageLoader.getInstance().isInited()) {
                    ImageLoaderConfig.initImageLoader(CropImgActivity.this, ImageCacheUtils.getBaseImageCache());
                }
                System.gc();
                Intent intent = new Intent();
                intent.setAction(BroadcastUtils.TAKE_PHOTO);
                intent.putExtra(IntentDataUtils.SAVE_IMG_PATH, save_path_uri);
                intent.putExtra(IntentDataUtils.CROP_IMG_TAG, cropImgTag);
                sendBroadcast(intent);
                cancelLoadingDialog();
                finish();
            }
        });
        thread.start();
    }


    private void destroyMemery() {
        ib_back = null;
        cropButton = null;
        cropImageView.recycleBitMap();
        cropImageView.setmCropOverlayView(null);
        cropImageView.destroyDrawingCache();
        cropImageView.removeAllViews();
        cropImageView = null;
        thread = null;
    }

    @Override
    protected void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
      //  destroyMemery();
        super.onDestroy();
    }
}
