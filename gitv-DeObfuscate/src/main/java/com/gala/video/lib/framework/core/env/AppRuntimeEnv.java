package com.gala.video.lib.framework.core.env;

import android.app.Activity;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class AppRuntimeEnv {
    ArrayList<Activity> mActivityList;
    private Context mAppContext;
    private long mAppStartTime;
    private int mCpuCores;
    private String mDefaultUserId;
    private String mDeviceIp;
    private boolean mIsPlayInHome;
    private int mMemTotal;
    private String mSessionId;

    private static class SingletonHelper {
        private static AppRuntimeEnv instance = new AppRuntimeEnv();

        private SingletonHelper() {
        }
    }

    private AppRuntimeEnv() {
        this.mAppContext = null;
        this.mDeviceIp = "";
        this.mSessionId = "";
        this.mDefaultUserId = "";
        this.mAppStartTime = 0;
        this.mCpuCores = 0;
        this.mMemTotal = 0;
        this.mIsPlayInHome = false;
        this.mActivityList = new ArrayList();
    }

    public static AppRuntimeEnv get() {
        return SingletonHelper.instance;
    }

    public void init(Context context) {
        if (this.mAppContext != null) {
            throw new IllegalStateException("QVideoClient can be setup only once.");
        }
        this.mAppContext = context;
    }

    private void ensureAppContext() {
        if (this.mAppContext == null) {
            throw new IllegalStateException("QVideoClient has not been setup.");
        }
    }

    public Context getApplicationContext() {
        ensureAppContext();
        return this.mAppContext;
    }

    public long getStartTime() {
        return this.mAppStartTime;
    }

    public void setStartTime(long startTime) {
        this.mAppStartTime = startTime;
    }

    public String getDeviceIp() {
        return this.mDeviceIp;
    }

    public void setDeviceIp(String ip) {
        this.mDeviceIp = ip;
    }

    public String getSessionId() {
        return this.mSessionId;
    }

    public void setSessionId(String sessionid) {
        this.mSessionId = sessionid;
    }

    public String getDefaultUserId() {
        return this.mDefaultUserId;
    }

    public void setDefaultUserId(String defaultUserId) {
        this.mDefaultUserId = defaultUserId;
    }

    public void setTotalMemory(int total) {
        this.mMemTotal = total;
    }

    public int getTotalMemory() {
        return this.mMemTotal;
    }

    public void setCpuCores(int cores) {
        this.mCpuCores = cores;
    }

    public int getCpuCores() {
        return this.mCpuCores;
    }

    public void setIsPlayInHome(boolean isPlay) {
        this.mIsPlayInHome = isPlay;
    }

    public boolean isPlayInHome() {
        return this.mIsPlayInHome;
    }

    public void removeActivity(Activity a) {
        if (this.mActivityList != null && this.mActivityList.contains(a)) {
            this.mActivityList.remove(a);
        }
    }

    public void addActivity(Activity a) {
        if (this.mActivityList != null && !this.mActivityList.contains(a)) {
            this.mActivityList.add(a);
        } else if (this.mActivityList == null) {
            this.mActivityList = new ArrayList();
        }
    }

    public List<Activity> getActivityList() {
        return this.mActivityList;
    }
}
