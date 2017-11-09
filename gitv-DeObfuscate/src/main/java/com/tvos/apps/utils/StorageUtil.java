package com.tvos.apps.utils;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class StorageUtil {
    private static final String TAG = "StorageUtil";

    public static class StorageUsage {
        private long RomAvailableSize = 0;
        private long RomTotalSize = 0;
        private long SDAvailableSize = 0;
        private long SDTotalSize = 0;

        public long getSDTotalSize() {
            return this.SDTotalSize;
        }

        public void setSDTotalSize(long sDTotalSize) {
            this.SDTotalSize = sDTotalSize;
        }

        public long getSDAvailableSize() {
            return this.SDAvailableSize;
        }

        public void setSDAvailableSize(long sDAvailableSize) {
            this.SDAvailableSize = sDAvailableSize;
        }

        public long getRomTotalSize() {
            return this.RomTotalSize;
        }

        public void setRomTotalSize(long romTotalSize) {
            this.RomTotalSize = romTotalSize;
        }

        public long getRomAvailableSize() {
            return this.RomAvailableSize;
        }

        public void setRomAvailableSize(long romAvailableSize) {
            this.RomAvailableSize = romAvailableSize;
        }
    }

    public static StorageUsage getStorageUsage() {
        StorageUsage status = new StorageUsage();
        getSDCardStatus(status);
        getRomStatus(status);
        return status;
    }

    @TargetApi(18)
    private static void getSDCardStatus(StorageUsage storageUsage) {
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
                storageUsage.setSDTotalSize(blockSize * totalBlocks);
                storageUsage.setSDAvailableSize(blockSize * availableBlocks);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(18)
    private static void getRomStatus(StorageUsage storageUsage) {
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
            storageUsage.setRomTotalSize(blockSize * totalBlocks);
            storageUsage.setRomAvailableSize(blockSize * availableBlocks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
