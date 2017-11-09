package com.qiyi.tv.client.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class AppInfo implements Parcelable, Serializable {
    public static final Creator<AppInfo> CREATOR = new C19831();
    private static final String KEY_DOWNLOAD_URL = "app.download_url";
    private static final String KEY_FLAG = "app.flag";
    private static final String KEY_ID = "app.id";
    private static final String KEY_IMAGE_URL = "app.image_url";
    private static final String KEY_NAME = "app.name";
    private static final String KEY_PACKAGE_NAME = "app.package_name";
    private static final long serialVersionUID = 1;
    private int mCategory;
    private String mDownloadUrl;
    private String mId;
    private String mImageUrl;
    private String mName;
    private String mPackageName;

    static class C19831 implements Creator<AppInfo> {
        C19831() {
        }

        public final AppInfo createFromParcel(Parcel parcel) {
            Bundle bundle = (Bundle) parcel.readParcelable(AppInfo.class.getClassLoader());
            bundle.setClassLoader(AppInfo.class.getClassLoader());
            AppInfo appInfo = new AppInfo();
            appInfo.readBundle(bundle);
            return appInfo;
        }

        public final AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    }

    public String getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getImageUrl() {
        return this.mImageUrl;
    }

    public String getDownloadUrl() {
        return this.mDownloadUrl;
    }

    public int getCategory() {
        return this.mCategory;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.mDownloadUrl = downloadUrl;
    }

    public void setCategory(int category) {
        this.mCategory = category;
    }

    protected void writeBundle(Bundle bundle) {
        bundle.putString(KEY_ID, this.mId);
        bundle.putString(KEY_NAME, this.mName);
        bundle.putString(KEY_PACKAGE_NAME, this.mPackageName);
        bundle.putString(KEY_IMAGE_URL, this.mImageUrl);
        bundle.putString(KEY_DOWNLOAD_URL, this.mDownloadUrl);
        bundle.putInt(KEY_FLAG, this.mCategory);
    }

    protected void readBundle(Bundle bundle) {
        this.mId = bundle.getString(KEY_ID);
        this.mName = bundle.getString(KEY_NAME);
        this.mPackageName = bundle.getString(KEY_PACKAGE_NAME);
        this.mImageUrl = bundle.getString(KEY_IMAGE_URL);
        this.mDownloadUrl = bundle.getString(KEY_DOWNLOAD_URL);
        this.mCategory = bundle.getInt(KEY_FLAG);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int i) {
        Parcelable bundle = new Bundle();
        writeBundle(bundle);
        dest.writeParcelable(bundle, 0);
    }
}
