package com.wzq.xl.cropperimg.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wzq.xl.cropperimg.imgtool.ImageLoaderConfig;
import com.wzq.xl.cropperimg.toasttool.ShowToastTool;
import com.wzq.xl.cropperimg.utils.ImageCacheUtils;
import com.wzq.xl.cropperimg.widget.LoadingDialog;



/*
* @author wzq
* @createtime 20160219
*
* */

public class BaseActivity extends AppCompatActivity {
    private LoadingDialog loadingDialog;//loading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfig.initImageLoader(this, ImageCacheUtils.getBaseImageCache());
        }

    }
    public void showToast(String str) {//提示框
        ShowToastTool.Short(BaseActivity.this, str);
    }
    public void buildLoadingDialog()
    {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show();
    }

    public void cancelLoadingDialog() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    public void startNextActivity(Bundle bundle, Class<?> pClass) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    public void startNextActivityForResult(Bundle bundle, Class<?> pClass, int intentFlag) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, intentFlag);
    }

    public void startNextActivityForResult(Bundle bundle, Class<?> pClass, int intentFlag, boolean finishFlag) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, intentFlag);
        if (finishFlag)
            super.finish();
    }


    public void startNextActivity(Bundle bundle, Class<?> pClass,
                                  boolean finishFlag) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (finishFlag) {
            super.finish();
        }
    }

}
