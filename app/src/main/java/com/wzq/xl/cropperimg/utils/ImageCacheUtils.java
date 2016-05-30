package com.wzq.xl.cropperimg.utils;

import android.os.Environment;

import java.io.File;

/**
 * @author wzq
 * @createtime 20160203
 */
public class ImageCacheUtils {

    public final static String IMG_PATH = "image";

    public final static long IMAGE_CACHE_NEED_SIZE = 5 * 1024 * 1024;

    public static String getBaseImageCache() {
        return Environment.getExternalStorageDirectory().toString() + File.separator + IMG_PATH + File.separator + "cache" + File.separator + "images" + File.separator;
    }


    public static String getCutImgPath()
    {
        return Environment.getExternalStorageDirectory().toString() + File.separator + IMG_PATH + File.separator + "cut_img" + File.separator;
    }

    public static String getHeadImgPath()
    {
        return Environment.getExternalStorageDirectory().toString()+ File.separator + IMG_PATH + File.separator + "contenttop" + File.separator;
    }

    public static String getEditCutImgPath()
    {
        return Environment.getExternalStorageDirectory().toString() + File.separator + IMG_PATH + File.separator + "edit_cut_img" + File.separator;
    }

    public static String getEditPreCutImgPath()
    {
        return Environment.getExternalStorageDirectory().toString() + File.separator + IMG_PATH + File.separator + "edit_pre_cut_img" + File.separator;
    }


    public static String getPreCutImgPath()
    {
        return  Environment.getExternalStorageDirectory().toString() + File.separator + IMG_PATH + File.separator + "pre_cut_img" + File.separator;
    }

    public static String getEditImageCachePath() {
        String path = ImageCacheUtils.getEditCutImgPath();
        File file = new File(path);
        if (!file.exists())
            file.mkdir();
        return path;
    }


    public static String getImageCachePath() {//获取图片缓存路径
        String path = ImageCacheUtils.getCutImgPath();
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        return path;
    }


}
