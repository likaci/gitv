package com.gala.video.app.player.config;

import android.os.Build;
import android.os.Build.VERSION;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.project.Project;

public class DeviceInfoParams {
    private static volatile DeviceInfoParams sDeviceInfoParams;
    private String mAndroidVersion;
    private String mApkVersion;
    private String mCpuInfo;
    private String mModel;
    private String mProduct;
    private int mTotalMemory;
    private String mUuid;

    private DeviceInfoParams() {
        init();
    }

    public static DeviceInfoParams instance() {
        if (sDeviceInfoParams == null) {
            synchronized (DeviceInfoParams.class) {
                if (sDeviceInfoParams == null) {
                    sDeviceInfoParams = new DeviceInfoParams();
                }
            }
        }
        return sDeviceInfoParams;
    }

    public String getCpuInfo() {
        return this.mCpuInfo;
    }

    public String getProductName() {
        return this.mProduct;
    }

    public String getUuid() {
        return this.mUuid;
    }

    public String getModelName() {
        return this.mModel;
    }

    public int getTotalMemory() {
        return this.mTotalMemory;
    }

    public String getAndroidVersion() {
        return this.mAndroidVersion;
    }

    public String getApkVersion() {
        return this.mApkVersion;
    }

    public String[] getParams() {
        return new String[]{this.mCpuInfo, this.mProduct, this.mModel, String.valueOf(this.mTotalMemory), this.mAndroidVersion, this.mApkVersion, this.mUuid};
    }

    private void init() {
        this.mCpuInfo = DeviceUtils.getCpuInfo();
        this.mProduct = Build.PRODUCT;
        this.mModel = Build.MODEL;
        this.mTotalMemory = AppRuntimeEnv.get().getTotalMemory();
        this.mUuid = Project.getInstance().getBuild().getVrsUUID();
        this.mAndroidVersion = VERSION.SDK;
        this.mApkVersion = AppClientUtils.getClientVersion();
    }
}
