package com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model;

import android.graphics.Bitmap;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.utils.QRUtils;

public class ScreenSaverAdModel extends CupidAdModel {
    private static final int MIN_SHOW_DURATION = 5000;
    private static final String TAG = "screensaver/ScreenSaverAdModel";
    private String mDuration = "";
    private String mImageLocalPath = "";
    private String mImageName = "";
    private String mNeedAdBadge = "";
    private String mNeedQR = "";
    private String mQrDescription = "";
    private String mQrPosition = "";
    private String mQrTitle = "";
    private String mQrUrl = "";

    public String getDuration() {
        return this.mDuration;
    }

    public void setDuration(String duration) {
        this.mDuration = duration;
    }

    public String getNeedQR() {
        return this.mNeedQR;
    }

    public void setNeedQR(String needQR) {
        this.mNeedQR = needQR;
    }

    public String getQrUrl() {
        return this.mQrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.mQrUrl = qrUrl;
    }

    public String getQrTitle() {
        return this.mQrTitle;
    }

    public void setQrTitle(String qrTitle) {
        this.mQrTitle = qrTitle;
    }

    public String getQrPosition() {
        return this.mQrPosition;
    }

    public void setQrPosition(String qrPosition) {
        this.mQrPosition = qrPosition;
    }

    public String getQrDescription() {
        return this.mQrDescription;
    }

    public void setQrDescription(String qrDescription) {
        this.mQrDescription = qrDescription;
    }

    public String getNeedAdBadge() {
        return this.mNeedAdBadge;
    }

    public boolean isNeedAdBadge() {
        return "true".equals(this.mNeedAdBadge);
    }

    public boolean shouldShowQr() {
        return "true".equals(this.mNeedQR);
    }

    public Bitmap getQrBitmap() {
        LogUtils.d(TAG, "getQrBitmap, is should show QR : " + shouldShowQr());
        if (!shouldShowQr() || StringUtils.isEmpty(this.mQrUrl)) {
            return null;
        }
        int dimension = ResourceUtil.getDimensionPixelSize(R.dimen.dimen_124dp);
        LogUtils.d(TAG, "getQrBitmap, dimension = " + dimension);
        Bitmap qrBitmap = null;
        try {
            return QRUtils.createQRImage(this.mQrUrl, dimension, dimension);
        } catch (Exception e) {
            LogUtils.e(TAG, "getQrBitmap, Exception : " + e);
            return qrBitmap;
        }
    }

    public void setNeedAdBadge(String needAdBadge) {
        this.mNeedAdBadge = needAdBadge;
    }

    public String getImageLocalPath() {
        return this.mImageLocalPath;
    }

    public void setImageLocalPath(String imageLocalPath) {
        this.mImageLocalPath = imageLocalPath;
    }

    public String getImageName() {
        return this.mImageName;
    }

    public void setImageName(String imageName) {
        this.mImageName = imageName;
    }

    public int getAdDuration() {
        int showDuration = 0;
        String duration = getDuration();
        LogUtils.d(TAG, "getAdDuration, duration = " + duration);
        try {
            showDuration = Integer.parseInt(duration) * 1000;
        } catch (NumberFormatException e) {
            LogUtils.e(TAG, "getAdDurationByPos e = " + e);
        }
        if (showDuration < 5000) {
            return 5000;
        }
        return showDuration;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ScreenSaverAdModel{");
        sb.append("mDuration='").append(this.mDuration).append('\'');
        sb.append(", mNeedQR='").append(this.mNeedQR).append('\'');
        sb.append(", mQrUrl='").append(this.mQrUrl).append('\'');
        sb.append(", mQrTitle='").append(this.mQrTitle).append('\'');
        sb.append(", mQrPosition='").append(this.mQrPosition).append('\'');
        sb.append(", mQrDescription='").append(this.mQrDescription).append('\'');
        sb.append(", mNeedAdBadge='").append(this.mNeedAdBadge).append('\'');
        sb.append(", mImageLocalPath='").append(this.mImageLocalPath).append('\'');
        sb.append(", mImageName='").append(this.mImageName).append('\'');
        sb.append('}');
        return super.toString() + sb.toString();
    }
}
