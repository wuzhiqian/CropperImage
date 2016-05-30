package com.wzq.xl.cropperimg.imgtool;

import android.content.Context;
import android.graphics.Bitmap;


import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.wzq.xl.cropperimg.R;

import java.io.File;

/*
*
*@author wzq
*@create time 20160203
* */
public class ImageLoaderConfig {

	/**
	 *
	 * 
	 * @param isDefaultShow
	 *
	 * @return
	 */
	public static DisplayImageOptions initDisplayOptions(boolean isShowDefault) {
		DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
		displayImageOptionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
		if (isShowDefault) {
			displayImageOptionsBuilder.showStubImage(R.drawable.no_image);
			displayImageOptionsBuilder
					.showImageForEmptyUri(R.drawable.no_image);
			displayImageOptionsBuilder.showImageOnFail(R.drawable.no_image);
		}
		displayImageOptionsBuilder.cacheInMemory(true);
		displayImageOptionsBuilder.cacheOnDisc(true);
		displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
		return displayImageOptionsBuilder.build();
	}


	public static DisplayImageOptions getSpecialOption() {//获取不缩放图片的设置
		DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
		displayImageOptionsBuilder.imageScaleType(ImageScaleType.NONE);
		displayImageOptionsBuilder.showStubImage(R.drawable.no_image);
		displayImageOptionsBuilder.showImageForEmptyUri(R.drawable.no_image);
		displayImageOptionsBuilder.showImageOnFail(R.drawable.no_image);
		displayImageOptionsBuilder.cacheInMemory(false);
		displayImageOptionsBuilder.cacheOnDisc(false);
		displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
		return displayImageOptionsBuilder.build();
	}





	/**
	 * 返回修改图片大小的加载参数配置
	 * 
	 * @return
	 */
	public static DisplayImageOptions initDisplayOptions(int targetWidth,
			boolean isShowDefault) {
		DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
		displayImageOptionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
		if (isShowDefault) {
			displayImageOptionsBuilder.showStubImage(R.drawable.no_image);
			displayImageOptionsBuilder
					.showImageForEmptyUri(R.drawable.no_image);
			displayImageOptionsBuilder
					.showImageOnFail(R.drawable.no_image);
		}
		displayImageOptionsBuilder.cacheInMemory(true);
		displayImageOptionsBuilder.cacheOnDisc(true);
		displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
		displayImageOptionsBuilder.displayer(new SimpleImageDisplayer(
				targetWidth));

		return displayImageOptionsBuilder.build();
	}

	public static void initImageLoader(Context context, String cacheDisc) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, cacheDisc);
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				context);
		builder.threadPoolSize(3);
		builder.threadPriority(Thread.NORM_PRIORITY - 1);
		builder.memoryCache(new WeakMemoryCache());
		builder.memoryCacheExtraOptions(480, 800);
		builder.denyCacheImageMultipleSizesInMemory();
		builder.discCache(new UnlimitedDiscCache(cacheDir));
		builder.discCacheFileNameGenerator(new HashCodeFileNameGenerator());
		builder.imageDownloader(new BaseImageDownloader(context, 10000, 60000));
		builder.discCacheFileCount(500);
		builder.discCacheSize(200 * 1024 * 1024);
		builder.defaultDisplayImageOptions(initDisplayOptions(false));
		ImageLoader.getInstance().init(builder.build());
	}


	public static void initImageLoader(Context context, String cacheDisc, boolean cacheFlag) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, cacheDisc);
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				context);
		builder.threadPoolSize(3);
		builder.threadPriority(Thread.NORM_PRIORITY);
		builder.memoryCache(new WeakMemoryCache());
		if(cacheFlag)
			builder.memoryCacheExtraOptions(480, 800);
		builder.denyCacheImageMultipleSizesInMemory();
		builder.discCache(new UnlimitedDiscCache(cacheDir));
		builder.discCacheFileNameGenerator(new HashCodeFileNameGenerator());
		builder.imageDownloader(new BaseImageDownloader(context, 10000, 60000));
		builder.discCacheFileCount(500);
		builder.discCacheSize(200 * 1024 * 1024);
		builder.defaultDisplayImageOptions(initDisplayOptions(false));
		ImageLoader.getInstance().init(builder.build());
	}
}
