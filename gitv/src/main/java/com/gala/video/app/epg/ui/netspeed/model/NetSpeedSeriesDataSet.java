package com.gala.video.app.epg.ui.netspeed.model;

import com.gala.sdk.player.constants.SdkConstants;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.pingback.HomeAdPingbackModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NetSpeedSeriesDataSet {
    public static final int FLUENCY_DEFINITION = 400;
    public static final int HIGH_DEFINITION = 1024;
    private static final int INIT_SIZE = 16;
    private static final int MAX_X_RANGE = 100;
    private static final int MAX_Y_RANGE = 30720;
    public static final int P1080_DEFINITION = 5120;
    public static final int P4K_DEFINITION = 10240;
    public static final int SUPER_DEFINITION = 3072;
    private static final String TAG = "XYSeriesDataSet";
    private Map<Integer, Integer> mDataSet = new LinkedHashMap(16);
    private List<Integer> mXList = new ArrayList(16);

    public void add(int x, int y) {
        this.mXList.add(Integer.valueOf(x));
        this.mDataSet.put(Integer.valueOf(x), Integer.valueOf(y));
    }

    public int getSize() {
        return this.mXList.size();
    }

    public void clear() {
        this.mDataSet.clear();
        this.mXList.clear();
    }

    public int getXByIndex(int index) {
        if (index >= 0 && index < this.mXList.size()) {
            return ((Integer) this.mXList.get(index)).intValue();
        }
        LogUtils.e(TAG, "index is invalid, index= " + index + " data set size = " + this.mXList.size());
        return -1;
    }

    public int getYByIndex(int index) {
        if (index < 0 || index >= this.mXList.size()) {
            LogUtils.e(TAG, "index is invalid, index= " + index + " data set size = " + this.mXList.size());
            return -1;
        }
        return ((Integer) this.mDataSet.get(Integer.valueOf(((Integer) this.mXList.get(index)).intValue()))).intValue();
    }

    public static NetSpeedSeriesDataSet normalizeSeries(int width, int height, NetSpeedSeriesDataSet seriesData, boolean is4KSurpport) {
        NetSpeedSeriesDataSet data = new NetSpeedSeriesDataSet();
        int itemHeight = is4KSurpport ? height / 6 : height / 5;
        if (seriesData != null) {
            int xStartPosition = 0;
            if (seriesData.getSize() > 0) {
                xStartPosition = seriesData.getXByIndex(0);
            }
            for (int index = 0; index < seriesData.getSize(); index++) {
                int x = seriesData.getXByIndex(index);
                if (100 - xStartPosition > 0) {
                    x = ((x - xStartPosition) * width) / (100 - xStartPosition);
                }
                int y = seriesData.getYByIndex(index);
                if (y > MAX_Y_RANGE) {
                    y = MAX_Y_RANGE;
                }
                if (y <= 400) {
                    y = (y * itemHeight) / 400;
                } else if (y > 400 && y <= 1024) {
                    y = itemHeight + (((y + SdkConstants.ERROR_INIT_PARAM_INSUFFICIENT) * itemHeight) / 624);
                } else if (y > 1024 && y <= 3072) {
                    y = (itemHeight * 2) + (((y + HomeAdPingbackModel.DEFAULT_H5_ENTER_TYPE) * itemHeight) / 2048);
                } else if (y > 3072 && y <= 5120) {
                    y = (itemHeight * 3) + (((y - 3072) * itemHeight) / 2048);
                } else if (!is4KSurpport) {
                    y = (itemHeight * 4) + (((y - 5120) * itemHeight) / 25600);
                } else if (y <= 5120 || y > P4K_DEFINITION) {
                    y = (itemHeight * 5) + (((y - 10240) * itemHeight) / 20480);
                } else {
                    y = (itemHeight * 4) + (((y - 5120) * itemHeight) / 5120);
                }
                data.add(x, y);
            }
        }
        return data;
    }
}
