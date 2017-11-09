package com.tvos.appmanager.util;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.tvos.appmanager.model.StorageStatus;

public class StorageUtil {
    private static final String TAG = "StorageUtil";

    public static StorageStatus getStatus() {
        StorageStatus status = new StorageStatus();
        getSDCardStatus(status);
        getRomStatus(status);
        return status;
    }

    @TargetApi(18)
    private static void getSDCardStatus(StorageStatus storageStatus) {
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                long blockSize;
                long totalBlocks;
                long availableBlocks;
                StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
                if (VERSION.SDK_INT >= 18) {
                    blockSize = stat.getBlockSizeLong();
                    totalBlocks = stat.getBlockCountLong();
                    availableBlocks = stat.getAvailableBlocksLong();
                } else {
                    blockSize = (long) stat.getBlockSize();
                    totalBlocks = (long) stat.getBlockCount();
                    availableBlocks = (long) stat.getAvailableBlocks();
                }
                Log.d(TAG, "sdcard total size:" + (blockSize * totalBlocks) + "availableSize" + (blockSize * availableBlocks));
                storageStatus.setSDTotalSize(blockSize * totalBlocks);
                storageStatus.setSDAvailableSize(blockSize * availableBlocks);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(18)
    private static void getRomStatus(StorageStatus storageStatus) {
        try {
            long blockSize;
            long totalBlocks;
            long availableBlocks;
            StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
            if (VERSION.SDK_INT >= 18) {
                blockSize = stat.getBlockSizeLong();
                totalBlocks = stat.getBlockCountLong();
                availableBlocks = stat.getAvailableBlocksLong();
            } else {
                blockSize = (long) stat.getBlockSize();
                totalBlocks = (long) stat.getBlockCount();
                availableBlocks = (long) stat.getAvailableBlocks();
            }
            Log.d(TAG, "rom total size:" + (blockSize * totalBlocks) + "availableSize" + (blockSize * availableBlocks));
            storageStatus.setRomTotalSize(blockSize * totalBlocks);
            storageStatus.setRomAvailableSize(blockSize * availableBlocks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
