package com.tvos.appmanager.model;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.io.ByteArrayOutputStream;

public class AppInfo implements IAppInfo, Parcelable {
    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }

        public AppInfo createFromParcel(Parcel source) {
            return new AppInfo(source);
        }
    };
    private static final String TAG = "AppInfo";
    private String appAuthor;
    private Drawable appIcon;
    private byte[] appIconData;
    private String appInstalledTime;
    private String appName;
    private String appName_py;
    private String appPath;
    private long appSize;
    private String appVersion;
    private int appVersionCode;
    private boolean isSystemApp;
    private String pkgName;
    private long runningTime;
    private long startTime;
    private int status = 2;

    public AppInfo(Parcel p) {
        boolean z = true;
        Log.d(TAG, "create appinfo");
        this.pkgName = p.readString();
        Log.d(TAG, this.pkgName);
        this.appName = p.readString();
        Log.d(TAG, this.appName);
        this.appName_py = p.readString();
        Log.d(TAG, this.appName_py);
        this.appPath = p.readString();
        this.appVersion = p.readString();
        this.appVersionCode = p.readInt();
        this.appAuthor = p.readString();
        this.appSize = p.readLong();
        this.appInstalledTime = p.readString();
        if (p.readByte() != (byte) 1) {
            z = false;
        }
        this.isSystemApp = z;
        this.status = p.readInt();
        this.runningTime = p.readLong();
        this.startTime = p.readLong();
        int size = p.readInt();
        if (size < 0) {
            size = 0;
        }
        this.appIconData = new byte[size];
        p.readByteArray(this.appIconData);
    }

    public void setAppIconData(byte[] appIconData) {
        this.appIconData = appIconData;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPath() {
        return this.appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public Drawable getAppIcon() {
        return this.appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
        setAppIconData(drawableToBytes(getAppIcon()));
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getAppVersionCode() {
        return this.appVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppAuthor() {
        return this.appAuthor;
    }

    public void setAppAuthor(String appAuthor) {
        this.appAuthor = appAuthor;
    }

    public long getAppSize() {
        return this.appSize;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
    }

    public String getAppInstalledTime() {
        return this.appInstalledTime;
    }

    public void setAppInstalledTime(String appInstalledTime) {
        this.appInstalledTime = appInstalledTime;
    }

    public boolean isSystemApp() {
        return this.isSystemApp;
    }

    public void setSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRunningTime() {
        return this.runningTime;
    }

    public void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public byte[] getAppIconData() {
        return this.appIconData;
    }

    public String getAppName_py() {
        return this.appName_py;
    }

    public void setAppName_py(String appName_py) {
        this.appName_py = appName_py;
    }

    private byte[] drawableToBytes(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isStarted() {
        return this.status == 2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        Log.d(TAG, "write to parcel");
        dest.writeString(this.pkgName);
        dest.writeString(this.appName);
        dest.writeString(this.appName_py);
        dest.writeString(this.appPath);
        dest.writeString(this.appVersion);
        dest.writeInt(this.appVersionCode);
        dest.writeString(this.appAuthor);
        dest.writeLong(this.appSize);
        dest.writeString(this.appInstalledTime);
        dest.writeByte((byte) (this.isSystemApp ? 1 : 0));
        dest.writeInt(this.status);
        dest.writeLong(this.runningTime);
        dest.writeLong(this.startTime);
        dest.writeInt(this.appIconData.length);
        dest.writeByteArray(this.appIconData);
    }
}
