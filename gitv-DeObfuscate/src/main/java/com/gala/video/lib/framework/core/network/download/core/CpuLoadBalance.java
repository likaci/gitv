package com.gala.video.lib.framework.core.network.download.core;

import android.util.Log;
import com.gala.sdk.player.IMediaPlayer;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

public class CpuLoadBalance {
    private static final int BASETIME = 100;
    private static final long LIMIT_DELAY_TIME = 5000;
    private static final String TAG = "CpuLoadBalance";
    private static int cpuCores = 0;
    private static final String mCpuStatPath = "/proc/stat";
    private static final int[] mSleepTime = new int[]{200, 400, 400, IMediaPlayer.AD_INFO_VIP_NO_AD, IMediaPlayer.AD_INFO_VIP_NO_AD, IMediaPlayer.AD_INFO_OVERLAY_LOGIN_SUCCESS, IMediaPlayer.AD_INFO_OVERLAY_LOGIN_SUCCESS, 1600, 3200, 6400, 6400};
    private final int GATE = 10;
    private final int INITIAL_PENCENT = 50;
    private long mIOWaitCpuUsge = 0;
    private long mIdleCpuUsge = 0;
    private long mNiceCpuUsge = 0;
    private long mPreIOWaitCpuUsge = 0;
    private long mPreIdleCpuUsge = 0;
    private long mPreSystemTime = 0;
    private long mPreTotalCpuUsge = 0;
    private long mSysCpuUsge = 0;
    private long mTotalCpuUsge = 0;
    private long mTotalDelayTime = 0;
    private long mUserCpuUsge = 0;

    static class C15971 implements FileFilter {
        C15971() {
        }

        public boolean accept(File pathname) {
            if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                return true;
            }
            return false;
        }
    }

    public CpuLoadBalance() {
        Log.d(TAG, "CpuLoadBalance intitial");
        this.mTotalDelayTime = 0;
    }

    private String readCpuState(String path) {
        RandomAccessFile randomAccessFile;
        IOException e;
        try {
            RandomAccessFile cpufile = new RandomAccessFile(path, "r");
            try {
                String load = cpufile.readLine();
                cpufile.close();
                randomAccessFile = cpufile;
                return load;
            } catch (IOException e2) {
                e = e2;
                randomAccessFile = cpufile;
                e.printStackTrace();
                return null;
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return null;
        }
    }

    private String[] getCpustat(String stat) {
        return stat.split(" ");
    }

    private void getCpuUsage(String[] stats) {
        long parseLong;
        long j = -1;
        int len = stats.length;
        this.mUserCpuUsge = len > 2 ? Long.parseLong(stats[2]) : -1;
        if (len > 3) {
            parseLong = Long.parseLong(stats[3]);
        } else {
            parseLong = -1;
        }
        this.mNiceCpuUsge = parseLong;
        if (len > 4) {
            parseLong = Long.parseLong(stats[4]);
        } else {
            parseLong = -1;
        }
        this.mSysCpuUsge = parseLong;
        if (len > 5) {
            parseLong = Long.parseLong(stats[5]);
        } else {
            parseLong = -1;
        }
        this.mIdleCpuUsge = parseLong;
        if (len > 6) {
            j = Long.parseLong(stats[6]);
        }
        this.mIOWaitCpuUsge = j;
    }

    private void storeCpuStats() {
        this.mPreIdleCpuUsge = this.mIdleCpuUsge;
        this.mPreIOWaitCpuUsge = this.mIOWaitCpuUsge;
        this.mPreTotalCpuUsge = this.mTotalCpuUsge;
    }

    private int getIdlePercent() {
        String stat = readCpuState(mCpuStatPath);
        if (stat == null) {
            return 50;
        }
        int percent;
        getCpuUsage(getCpustat(stat));
        this.mTotalCpuUsge = (((this.mUserCpuUsge + this.mNiceCpuUsge) + this.mSysCpuUsge) + this.mIdleCpuUsge) + this.mIOWaitCpuUsge;
        if (this.mTotalCpuUsge - this.mPreTotalCpuUsge > 10) {
            percent = (int) (((this.mIdleCpuUsge - this.mPreIdleCpuUsge) * 100) / (this.mTotalCpuUsge - this.mPreTotalCpuUsge));
        } else {
            percent = 50;
        }
        storeCpuStats();
        percent = getCpuCoreNums() * percent > 100 ? 100 : percent * getCpuCoreNums();
        Log.d(TAG, "getIdlePercent " + percent);
        return percent;
    }

    private int getIOWPercent() {
        String stat = readCpuState(mCpuStatPath);
        if (stat == null) {
            return 50;
        }
        int percent;
        getCpuUsage(getCpustat(stat));
        this.mTotalCpuUsge = (((this.mUserCpuUsge + this.mNiceCpuUsge) + this.mSysCpuUsge) + this.mIdleCpuUsge) + this.mIOWaitCpuUsge;
        if (this.mTotalCpuUsge - this.mPreTotalCpuUsge > 10) {
            percent = (int) (((this.mIOWaitCpuUsge - this.mPreIOWaitCpuUsge) * 100) / (this.mTotalCpuUsge - this.mPreTotalCpuUsge));
        } else {
            percent = 50;
        }
        storeCpuStats();
        percent = getCpuCoreNums() * percent > 100 ? 100 : percent * getCpuCoreNums();
        Log.d(TAG, "getIOWPercent " + percent);
        if (percent > 100) {
            percent = 100;
        }
        return percent;
    }

    public int getSleepTime() {
        long curSystemTime = System.currentTimeMillis();
        if (curSystemTime - this.mPreSystemTime <= 500) {
            return 200;
        }
        this.mPreSystemTime = curSystemTime;
        int iow = getIOWPercent();
        int index = iow > 100 ? 10 : iow / 10;
        if (index < 0 || index > 10) {
            index = 5;
        }
        int sleeptime = mSleepTime[index];
        this.mTotalDelayTime += (long) sleeptime;
        return sleeptime;
    }

    private static int getCpuCoreNums() {
        if (cpuCores == 0) {
            try {
                cpuCores = new File("/sys/devices/system/cpu/").listFiles(new C15971()).length;
            } catch (Exception e) {
                cpuCores = 1;
            }
        }
        Log.d(TAG, "getCpuCoreNums " + cpuCores);
        return cpuCores;
    }
}
