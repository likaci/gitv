package com.gala.video.app.epg.home.data.model;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeModel;

public class AppDataModel extends HomeModel {
    private static final String TAG = "AppDataModel";
    private String mDownloadUrl;
    private int mFlag;
    private String mId;
    private String mImageUrl;
    private String mName;
    private String mPackageName;

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public void setDownloadUrl(String mDownloadUrl) {
        this.mDownloadUrl = mDownloadUrl;
    }

    public void setFlag(int mFlag) {
        this.mFlag = mFlag;
    }

    public String getId() {
        return this.mId;
    }

    public String getImageUrl() {
        return this.mImageUrl;
    }

    public String getName() {
        return this.mName;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getDownloadUrl() {
        return this.mDownloadUrl;
    }

    public int getFlag() {
        return this.mFlag;
    }

    public String toString() {
        return "AppDataModel [mId = " + this.mId + ", mName = " + this.mName + ", mImageUrl = " + this.mImageUrl + ", mFlag = " + this.mFlag + AlbumEnterFactory.SIGN_STR;
    }
}
