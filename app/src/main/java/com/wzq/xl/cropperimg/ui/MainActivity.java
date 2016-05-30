package com.wzq.xl.cropperimg.ui;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.wzq.xl.cropperimg.R;
import com.wzq.xl.cropperimg.controller.CameraController;
import com.wzq.xl.cropperimg.imgtool.ImageTool;
import com.wzq.xl.cropperimg.utils.ImageCacheUtils;
import com.wzq.xl.cropperimg.utils.IntentDataUtils;

import java.io.File;


public class MainActivity extends CameraBaseActivity{
    ImageView imageView;
    Button choosebutton;
    boolean crop_img_type = true;//cut image type       true ：cut square      false：rect

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        imageView = (ImageView)findViewById(R.id.cropimageView);
        choosebutton = (Button) findViewById(R.id.choosebutton);
        cameraController = new CameraController(this, crop_img_type, imageView);
        cameraController.setStore_pic_path(ImageCacheUtils.getImageCachePath());
        cameraController.setBitMap(cameraController.getPhotoUri());
        cameraController.setBroadTag(IntentDataUtils.CROP_IMAGE);


        choosebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pkm = getPackageManager();
                boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", getPackageName()));
                if (has_permission) {
                    pic_dialog_appear();
                }else {
                    showToast("you don\'t have permission to write, please register permission");
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateTakePhoto(Uri url, String tag) {
        if (!IntentDataUtils.CROP_IMAGE.equals(tag))
            return;
        ImageTool.recycleImageView(choose_pic_img_view);
        cameraController.setBitMap(url);
    }


}
