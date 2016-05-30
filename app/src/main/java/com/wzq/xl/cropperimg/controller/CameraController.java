package com.wzq.xl.cropperimg.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wzq.xl.cropperimg.imgtool.ImgCompressTool;
import com.wzq.xl.cropperimg.ui.CropImgActivity;
import com.wzq.xl.cropperimg.utils.IntentDataUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author wzq
 * @createtime 20160215
 */


public class CameraController  {
    private Context context;
    private ImageView choose_pic_img_view = null;
    public Uri photoUri = null;
    public String store_pic_path =  Environment.getExternalStorageDirectory().toString() + File.separator + "cropperImage" + File.separator;
    private String broadTag = "default";
    private boolean crop_img_typ_squ = true;

    public CameraController(Context context, boolean crop_img_typ_squ) {
        this.context = context;
        setCrop_img_typ_squ(crop_img_typ_squ);
    }

    public CameraController(Context context, boolean crop_img_typ_squ, ImageView choose_pic_img_view) {
        this(context,crop_img_typ_squ);
        this.choose_pic_img_view = choose_pic_img_view;
    }



    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }


    public String getStore_pic_path() {//获取保存图片路径
        File file = new File(store_pic_path);
        if (!file.exists())
            file.mkdirs();
        return store_pic_path;
    }

    public void setStore_pic_path(String store_pic_path) {//设置保存图片路径
        File file = new File(store_pic_path);
        if (!file.exists())
            file.mkdirs();
        this.store_pic_path = store_pic_path;
    }

    public String getBroadTag() {
        return broadTag;
    }

    public void setBroadTag(String broadTag) {
        this.broadTag = broadTag;
    }

    public Intent getTakePicIntent()//获取照相的Intent
    {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoUri = Uri.fromFile(new File(getStore_pic_path(), String.valueOf(System.currentTimeMillis()) + ".jpg"));
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        return openCameraIntent;
    }


    public Intent getChoosePicIntent()//获取选择图片的Intent
    {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return openAlbumIntent;
    }


    public Intent getCropPicIntent(Uri uri, int outputX, int outputY)
    {

        Intent intent = new Intent();
        intent.setClass(context, CropImgActivity.class);
        intent.putExtra(IntentDataUtils.CROP_IMG_URI, uri);
        intent.putExtra(IntentDataUtils.SAVE_IMG_PATH, getStore_pic_path());
        intent.putExtra(IntentDataUtils.CROP_IMG_TAG, getBroadTag());
        intent.putExtra(IntentDataUtils.CROP_IMG_TYPE_SQU, isCrop_img_typ_squ());
        return intent;
    }

    public boolean isCrop_img_typ_squ() {
        return crop_img_typ_squ;
    }

    public void setCrop_img_typ_squ(boolean crop_img_typ_squ) {
        this.crop_img_typ_squ = crop_img_typ_squ;
    }

    public void setCropPicUri(Intent data) {
        Uri uri = null;
        if (data != null) {
            uri = data.getData();
            photoUri = uri;
        } else {
            photoUri = Uri.fromFile(new File(getStore_pic_path(), readPicName()));
        }
    }

    public String readPicName()//读取保存的图片名字
    {
        File file = new File(getStore_pic_path());
        if (!file.exists())
            return "";
        String name = "";
        for (File f : file.listFiles()) {
            if (f.getName().toString().trim().endsWith(".jpg") || f.getName().toString().trim().endsWith(".png"))
                if (f.getName().trim().compareTo(name) > 0)
                    name = f.getName();
        }
        if (!name.equals(""))
            return name;
        String tmpFileName = System.currentTimeMillis() + ".jpg";
        file = new File(getStore_pic_path() + File.separator + tmpFileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpFileName;

    }


    public void setBitMap(Uri uri) {//设置图片
        if (uri == null)
            return;
        if (choose_pic_img_view != null) {
            try {
                photoUri = uri;
                ImageLoader.getInstance().displayImage(uri.toString(), choose_pic_img_view);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void setBitMap(String uri) {//设置图片
        if (uri == null)
            return;
        if (choose_pic_img_view != null) {

            try {
                ImageLoader.getInstance().displayImage(uri, choose_pic_img_view);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void doTakePicture()//将保存在本地的图片取出并缩小后显示在界面上
    {
        String filePath = getStore_pic_path() + File.separator + readPicName();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        Uri uri = ImgCompressTool.getInstance().compressAndGenImageRetUrl(bitmap, filePath,  500);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        ImageLoader.getInstance().displayImage(uri.toString(), choose_pic_img_view, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

    }

    public void doChoosePicture(Intent data)
    {
        Uri originalUri = data.getData();
        setPhotoUri(originalUri);
    }


}
