package com.wzq.xl.cropperimg.widget;


import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ant.liao.GifView;
import com.wzq.xl.cropperimg.R;


public class LoadingDialog extends AlertDialog {

    public LoadingDialog(Context context) {
        super(context);
        View showview = LayoutInflater.from(context).inflate(
                R.layout.gif_loading, null);
        GifView gv = (GifView) showview.findViewById(R.id.gv_show);
        gv.setGifImage(R.mipmap.loading);
        this.show();
        this.setCancelable(false);
        Window window = ((Dialog)this).getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(showview);
    }

}
