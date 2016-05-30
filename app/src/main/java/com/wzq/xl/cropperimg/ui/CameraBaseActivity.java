package com.wzq.xl.cropperimg.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;


import com.wzq.xl.cropperimg.controller.CameraController;
import com.wzq.xl.cropperimg.imgtool.ImageTool;
import com.wzq.xl.cropperimg.utils.BroadcastUtils;
import com.wzq.xl.cropperimg.utils.CameraStausUtils;
import com.wzq.xl.cropperimg.utils.ImageCacheUtils;
import com.wzq.xl.cropperimg.utils.IntentDataUtils;
import com.wzq.xl.cropperimg.widget.ActionSheetDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class CameraBaseActivity extends BaseActivity {

    public CameraController cameraController = null;
    public ImageView choose_pic_img_view;
    public String store_pic_path = ImageCacheUtils.getImageCachePath();
    public Uri VideoFileUri = null;
    private int pic_width = 500;
    private int pic_height = 500;
    private boolean isChange = false;
    private UpdatePicBroadcastReceiver updatePicBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadCast();
    }


    protected void registerBroadCast() {
        IntentFilter cameraFilter = new IntentFilter();
        cameraFilter.addAction(BroadcastUtils.TAKE_PHOTO);
        updatePicBroadcastReceiver = new UpdatePicBroadcastReceiver();
        registerReceiver(updatePicBroadcastReceiver, cameraFilter);
    }

    /**
     *
     * @param pic_height
     * @param pic_width
     */
    public void setPicWidthAndHeight(int pic_height, int pic_width) {

        this.pic_height = pic_height;
        this.pic_width = pic_width;
        this.isChange = true;
    }

    public void initCameraController(String _store_pic_path, boolean crop_img_type)
    {
        cameraController = new CameraController(this, crop_img_type, choose_pic_img_view);
        cameraController.setStore_pic_path(_store_pic_path);
    }

    public void initCameraController(boolean crop_img_type)
    {
        cameraController = new CameraController(this, crop_img_type, choose_pic_img_view);
    }


    public void pic_dialog_appear()
    {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("open camera", ActionSheetDialog.
                                SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                doHandlerPhoto(CameraStausUtils.TAKE_PICTURE);// 用户点击了从照相机获取
                            }
                        })
                .addSheetItem("choose local", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                doHandlerPhoto(CameraStausUtils.CHOOSE_PICTURE);
                            }
                        }).show();
    }

    public void video_dialog_appear() {//弹出拍摄视频或者选择视频
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("open camera", ActionSheetDialog.
                                SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                dohandlerVideo(CameraStausUtils.VIDEO_SHOOT);// 用户点击了从照相机获取
                            }
                        })
                .addSheetItem("open local", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                dohandlerVideo(CameraStausUtils.VIDEO_LOCAL);//本地选取视频
                            }
                        }).show();
    }


    public void doHandlerPhoto(int type) {//处理图片
        switch (type) {
            case CameraStausUtils.TAKE_PICTURE:
                Intent intent = cameraController.getTakePicIntent();
                if (intent != null)
                    startActivityForResult(intent, CameraStausUtils.CROP);
                break;
            case CameraStausUtils.CHOOSE_PICTURE:
                startActivityForResult(cameraController.getChoosePicIntent(), CameraStausUtils.CROP);
                break;
            default:
                break;
        }

    }

    /**
     * 处理视频
     */
    public void dohandlerVideo(int type) {
        switch (type) {
            case CameraStausUtils.VIDEO_SHOOT:
                //拍摄
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);//create a intent to record video
                VideoFileUri = getOutputMediaFileUri(); // create a file Uri to save the video
                // set the video file name
                intent.putExtra(MediaStore.EXTRA_OUTPUT, VideoFileUri);
                // set the video quality high
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                // start the video capture Intent
                startActivityForResult(intent, IntentDataUtils.intent_to_video);
                break;
            case CameraStausUtils.VIDEO_LOCAL:
                //本地
                Intent intent_local = new Intent(Intent.ACTION_GET_CONTENT);
                intent_local.setType("video/*");
                Intent wrapperIntent = Intent.createChooser(intent_local, null);
                startActivityForResult(wrapperIntent, IntentDataUtils.intent_to_video_choose);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case CameraStausUtils.TAKE_PICTURE:
                cameraController.doTakePicture();
                break;
            case CameraStausUtils.CHOOSE_PICTURE:
                cameraController.doChoosePicture(data);
                break;
            case CameraStausUtils.CROP:
                cameraController.setCropPicUri(data);
                if (isChange) {
                    startActivityForResult(cameraController.getCropPicIntent(cameraController.photoUri, pic_width, pic_height), CameraStausUtils.CROP_PICTURE);
                    isChange = false;
                } else
                    startActivityForResult(cameraController.getCropPicIntent(cameraController.photoUri, 500, 500), CameraStausUtils.CROP_PICTURE);
                break;

            case IntentDataUtils.intent_to_edit_choosepic:
                showPic(data);
                break;

        }
    }

    private void showPic(Intent data) {
        Uri uri;
        if (data != null)
            uri = data.getExtras().getParcelable(IntentDataUtils.SAVE_IMG_PATH);
        else {
            uri = getIntent().getExtras().getParcelable(IntentDataUtils.SAVE_IMG_PATH);
        }
        cameraController.setBitMap(uri);
    }

    @Override
    protected void onDestroy() {
        ImageTool.recycleImageView(choose_pic_img_view);
        cameraController = null;
        choose_pic_img_view = null;
        unregisterReceiver(updatePicBroadcastReceiver);
        System.gc();
        super.onDestroy();

    }

    public void updateTakePhoto(Uri url, String tag)
    {
    }


    private class UpdatePicBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BroadcastUtils.TAKE_PHOTO.equals(intent.getAction().toString())) {
                Uri url = intent.getExtras().getParcelable(IntentDataUtils.SAVE_IMG_PATH);
                String tag = intent.getExtras().getString(IntentDataUtils.CROP_IMG_TAG);
                if (url == null)
                    return;
                updateTakePhoto(url, tag);
            }
        }

    }

    /**
     * Create a File Uri for saving a video
     */
    private static Uri getOutputMediaFileUri() {
        //get the mobile Pictures directory
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //get the current time
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File videoFile = new File(picDir.getPath() + File.separator + "VIDEO_" + timeStamp + ".mp4");
        if (videoFile.exists()) {
            videoFile.delete();
        }
        return Uri.fromFile(videoFile);
    }
}
