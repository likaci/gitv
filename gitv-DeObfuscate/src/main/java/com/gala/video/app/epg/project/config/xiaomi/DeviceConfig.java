package com.gala.video.app.epg.project.config.xiaomi;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import com.gala.video.app.epg.project.config.ConfigInterfaceBaseImpl;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.build.IBuildInterface;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

public class DeviceConfig extends ConfigInterfaceBaseImpl {
    private static final String CHANNEL = "xiaomi_tv_store";
    private static final String GALA_APPID = "2882303761517432546";
    private static final String GALA_APP_KEY = "5881743233546";
    private static final String MY_APPID = "2882303761517349288";
    private static final String MY_APP_KEY = "5761734976288";
    private static final String TAG = "xiaomi";
    private boolean mIsMatchDevice = false;
    private boolean mIsOpen3DMode = false;

    public void initialize(IBuildInterface build) {
        super.initialize(build);
        Log.d(TAG, "initialize");
        this.mIsMatchDevice = build.isMatchDevice();
        MiStatInterface.initialize(AppRuntimeEnv.get().getApplicationContext(), MY_APPID, MY_APP_KEY, CHANNEL);
        if (isLitchi()) {
            MiStatInterface.initialize(AppRuntimeEnv.get().getApplicationContext(), MY_APPID, MY_APP_KEY, CHANNEL);
        } else {
            MiStatInterface.initialize(AppRuntimeEnv.get().getApplicationContext(), GALA_APPID, GALA_APP_KEY, CHANNEL);
        }
        MiStatInterface.setUploadPolicy(0, 0);
    }

    public boolean shouldDuplicateUIForStereo3D() {
        return false;
    }

    public void onStereo3DBegun() {
        Log.d(TAG, "onStereo3DBegun");
        try {
            this.mIsOpen3DMode = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStereo3DFinished() {
        if (this.mIsOpen3DMode) {
            Log.e(TAG, "onStereo3DFinished");
            try {
                this.mIsOpen3DMode = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initHomeLogo(ImageView logo) {
        if (!isHomeVersion() && this.mIsMatchDevice && logo != null) {
            super.initHomeLogo(logo);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case 82:
                return true;
            default:
                return false;
        }
    }

    public boolean isEnableHardwareAccelerated() {
        return true;
    }

    public void initHomeStart(Activity activity) {
        MiStatInterface.recordPageStart(activity, "主页");
        Log.d(TAG, "initHomeStart");
    }

    public void initHomeEnd() {
        MiStatInterface.recordPageEnd();
        Log.e(TAG, "initHomeEnd");
    }
}
