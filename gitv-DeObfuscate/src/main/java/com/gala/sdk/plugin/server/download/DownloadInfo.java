package com.gala.sdk.plugin.server.download;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.gala.sdk.plugin.server.utils.Util;

public class DownloadInfo implements Parcelable {
    public static Creator<DownloadInfo> CREATOR = new C01741();
    private static final String KEY_MD5 = "md5";
    private static final String KEY_PROGRESS = "progress";
    private static final String KEY_SAVEPATH = "save_path";
    private static final String KEY_SIZE = "size";
    private static final String KEY_URL = "url";
    private String mMd5;
    private int mProgress;
    private String mSavePath;
    private int mSize;
    private String mUrl;

    static class C01741 implements Creator<DownloadInfo> {
        C01741() {
        }

        public DownloadInfo createFromParcel(Parcel source) {
            return new DownloadInfo(source.readBundle(DownloadInfo.class.getClassLoader()));
        }

        public DownloadInfo[] newArray(int size) {
            return new DownloadInfo[size];
        }
    }

    public DownloadInfo(String url, String savePath) {
        this.mUrl = url;
        this.mSavePath = savePath;
    }

    public DownloadInfo(DownloadInfo info) {
        if (info != null) {
            this.mUrl = info.getUrl();
            this.mSavePath = info.getSavePath();
            this.mMd5 = info.getMd5();
            this.mProgress = info.getProgress();
            this.mSize = info.getSize();
        }
    }

    DownloadInfo(Bundle bundle) {
        if (bundle != null) {
            this.mUrl = bundle.getString("url", "");
            this.mSavePath = bundle.getString(KEY_SAVEPATH, "");
            this.mMd5 = bundle.getString("md5", "");
            this.mProgress = bundle.getInt(KEY_PROGRESS, 0);
            this.mSize = bundle.getInt("size", 0);
        }
    }

    public String getUrl() {
        return this.mUrl;
    }

    public String getSavePath() {
        return this.mSavePath;
    }

    public String getMd5() {
        return this.mMd5;
    }

    public void setMd5(String md5) {
        this.mMd5 = md5;
    }

    public boolean needCheckMd5() {
        return !Util.isEmpty(this.mMd5);
    }

    public int getProgress() {
        return this.mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }

    public int getSize() {
        return this.mSize;
    }

    public void setSize(int size) {
        this.mSize = size;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DownloadInfo(").append("url=").append(this.mUrl).append(", savePath=").append(this.mSavePath).append(")");
        return builder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString("url", this.mUrl);
        bundle.putString(KEY_SAVEPATH, this.mSavePath);
        bundle.putString("md5", this.mMd5);
        bundle.putInt(KEY_PROGRESS, this.mProgress);
        bundle.putInt("size", this.mSize);
        dest.writeParcelable(bundle, 0);
    }
}
