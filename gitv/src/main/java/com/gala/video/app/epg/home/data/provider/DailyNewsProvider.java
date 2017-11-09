package com.gala.video.app.epg.home.data.provider;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.DailyLabelModel;
import com.gala.video.lib.share.utils.Precondition;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DailyNewsProvider {
    private static final String TAG = "DailyNewsProvider";
    private static final DailyNewsProvider sDailyNewsProvider = new DailyNewsProvider();
    private List<DailyLabelModel> mDailyNewModelList = new ArrayList();

    public static DailyNewsProvider getInstance() {
        return sDailyNewsProvider;
    }

    public synchronized List<DailyLabelModel> getDailyNewModelList() {
        if (Precondition.isEmpty(this.mDailyNewModelList)) {
            try {
                this.mDailyNewModelList = (List) SerializableUtils.read(HomeDataConfig.HOME_DAILY_NEWS_DIR);
            } catch (Exception e) {
                LogUtils.e(TAG, "read daily news data failed" + e);
            }
        }
        return this.mDailyNewModelList;
    }

    public synchronized void writeDailyNewModelListToCache(List<DailyLabelModel> dailyNewModelList) {
        this.mDailyNewModelList = dailyNewModelList;
        try {
            SerializableUtils.write(this.mDailyNewModelList, HomeDataConfig.HOME_DAILY_NEWS_DIR);
        } catch (IOException e) {
            LogUtils.d(TAG, "write daily news data failed" + e);
        }
    }

    public synchronized boolean isCached() {
        boolean z;
        File file = new File(AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + HomeDataConfig.HOME_DAILY_NEWS_DIR);
        if (file == null || !file.exists()) {
            z = false;
        } else {
            z = true;
        }
        return z;
    }
}
