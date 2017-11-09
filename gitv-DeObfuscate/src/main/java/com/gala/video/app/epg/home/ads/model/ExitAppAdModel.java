package com.gala.video.app.epg.home.ads.model;

import android.graphics.Bitmap;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.CupidAdModel;
import com.gala.video.utils.QRUtils;

public class ExitAppAdModel extends CupidAdModel {
    private static final String TAG = "ads/model/ExitAppAdModel";
    private String mNeedQr = "";
    private String mQrDesc = "";
    private String mQrTitle = "";
    private String mQrUrl = "";

    public boolean shouldShowQr() {
        return "true".equals(this.mNeedQr);
    }

    public String getmQrUrl() {
        return this.mQrUrl;
    }

    public void setmQrUrl(String mQrUrl) {
        this.mQrUrl = mQrUrl;
    }

    public Bitmap getQrBitmap() {
        LogUtils.m1568d(TAG, "getQrBitmap, shouldShowQr: " + shouldShowQr() + " is need qr : " + this.mNeedQr);
        if (!shouldShowQr() || StringUtils.isEmpty(this.mQrUrl)) {
            return null;
        }
        int dimension = (int) AppRuntimeEnv.get().getApplicationContext().getResources().getDimension(C0508R.dimen.dimen_255dp);
        LogUtils.m1568d(TAG, "getQrBitmap, the predefined bitmap dimension = " + dimension);
        try {
            Bitmap qrBitmap = QRUtils.createQRImage(this.mQrUrl, dimension, dimension);
            if (qrBitmap == null) {
                LogUtils.m1577w(TAG, "getQrBitmap, the generated bitmap is null");
            }
            LogUtils.m1568d(TAG, "getQrBitmap, the width of bitmap generated : " + (qrBitmap != null ? qrBitmap.getWidth() : -1));
            return qrBitmap;
        } catch (Exception e) {
            LogUtils.m1571e(TAG, "getQrBitmap, Exception : " + e);
            return null;
        }
    }

    public String getmQrTitle() {
        return this.mQrTitle;
    }

    public void setmQrTitle(String mQrTitle) {
        this.mQrTitle = mQrTitle;
    }

    public String getmNeedQr() {
        return this.mNeedQr;
    }

    public void setmNeedQr(String mNeedQr) {
        this.mNeedQr = mNeedQr;
    }

    public String getmQrDesc() {
        return this.mQrDesc;
    }

    public void setmQrDesc(String mQrDesc) {
        this.mQrDesc = mQrDesc;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ExitAppAdModel{");
        sb.append("mQrUrl='").append(this.mQrUrl).append('\'');
        sb.append(", mQrTitle='").append(this.mQrTitle).append('\'');
        sb.append(", mNeedQr='").append(this.mNeedQr).append('\'');
        sb.append(", mQrDesc='").append(this.mQrDesc).append('\'');
        sb.append('}');
        return super.toString() + sb.toString();
    }
}
