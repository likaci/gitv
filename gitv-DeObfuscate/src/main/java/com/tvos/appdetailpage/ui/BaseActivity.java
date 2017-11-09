package com.tvos.appdetailpage.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import com.tvos.appdetailpage.utils.CommonUtils;
import com.tvos.appdetailpage.utils.ResourcesUtils;

public class BaseActivity extends Activity {
    private Bitmap mBgBimap;
    private ImageView mBgView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
    }

    protected void onStart() {
        super.onStart();
        if (this.mBgView == null) {
            this.mBgView = getContainerView();
        }
        if (this.mBgView != null) {
            this.mBgBimap = CommonUtils.decodeResourceToBitmap(this, "apps_bg_wallpaper");
            if (this.mBgBimap != null) {
                this.mBgView.setImageBitmap(this.mBgBimap);
            }
        }
    }

    protected void onStop() {
        super.onStop();
        if (this.mBgView != null) {
            this.mBgView.setImageBitmap(null);
        }
        if (!(this.mBgBimap == null || this.mBgBimap.isRecycled())) {
            this.mBgBimap.recycle();
        }
        this.mBgBimap = null;
    }

    protected int getDimen(int dimenId) {
        return (int) getResources().getDimension(dimenId);
    }

    protected ImageView getContainerView() {
        return (ImageView) findViewById(getResId("id", "apps_bgView"));
    }

    protected int getResId(String className, String name) {
        return ResourcesUtils.getResourceId(this, className, name);
    }
}
