package com.gala.video.lib.share.utils;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class MemoryLevelInfo {
    public static final int LOWER_DEVICE = 1;
    public static final int LOWEST_DEVICE = 0;
    public static final int NORMAL_DEVICE = 2;
    private static final String TAG = "MemoryLevelInfo";
    private static byte isHighConfig = (byte) -1;
    private static byte isLowConfig = (byte) -1;
    private static byte isLowMemory = (byte) -1;
    private static byte memoryLevel = (byte) -1;

    public static boolean isLowMemoryDevice() {
        if (isLowMemory < (byte) 0) {
            int totalMemory = AppRuntimeEnv.get().getTotalMemory();
            LogUtils.i(TAG, "is Low Memory Device:" + totalMemory);
            if (totalMemory > 0 && totalMemory <= 512) {
                if (getMemoryLevelDevice() == 2 && totalMemory <= 368) {
                    GetInterfaceTools.getIJSConfigDataProvider().setMemoryLevel(0);
                } else if (getMemoryLevelDevice() == 2) {
                    GetInterfaceTools.getIJSConfigDataProvider().setMemoryLevel(1);
                }
                isLowMemory = (byte) 1;
                return true;
            } else if (getMemoryLevelDevice() < 2) {
                isLowMemory = (byte) 1;
                return true;
            } else {
                isLowMemory = (byte) 0;
                return false;
            }
        } else if (isLowMemory > (byte) 0) {
            return true;
        } else {
            return false;
        }
    }

    public static int getMemoryLevelDevice() {
        memoryLevel = (byte) GetInterfaceTools.getIJSConfigDataProvider().getMemoryLevel();
        return memoryLevel;
    }

    public static boolean isLowConfigDevice() {
        int i = 1;
        if (isLowConfig < (byte) 0) {
            boolean isLowConfigDev;
            int cpuNum = AppRuntimeEnv.get().getCpuCores();
            if (isLowMemoryDevice() || cpuNum == 1) {
                isLowConfigDev = true;
            } else {
                isLowConfigDev = false;
            }
            LogUtils.i(TAG, "is Low Config Device, cpuNum: " + cpuNum + ", isLowConfigDevice:" + isLowConfig);
            if (!isLowConfigDev) {
                i = 0;
            }
            isLowConfig = (byte) i;
            return isLowConfigDev;
        } else if (isLowConfig > (byte) 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isHighConfigDevice() {
        int i = 1;
        if (isHighConfig < (byte) 0) {
            boolean isHighConfigDev;
            int cpuNum = AppRuntimeEnv.get().getCpuCores();
            if (!isHighMemoryDevice() || cpuNum <= 2) {
                isHighConfigDev = false;
            } else {
                isHighConfigDev = true;
            }
            LogUtils.i(TAG, "is high Config Device, cpuNum: " + cpuNum + ", isHighConfig:" + isHighConfig);
            if (!isHighConfigDev) {
                i = 0;
            }
            isHighConfig = (byte) i;
            return isHighConfigDev;
        } else if (isHighConfig > (byte) 0) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isHighMemoryDevice() {
        return AppRuntimeEnv.get().getTotalMemory() >= 786;
    }
}
