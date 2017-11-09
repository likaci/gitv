package com.gala.video.app.epg.home.data.provider;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class RecommendQuitApkProvider {
    private static final RecommendQuitApkProvider sInstance = new RecommendQuitApkProvider();
    private final String TAG = "RecommendQuitApkProvider";

    private RecommendQuitApkProvider() {
    }

    public static RecommendQuitApkProvider getInstance() {
        return sInstance;
    }

    public void writeCache(List<Album> albumList) {
        try {
            SerializableUtils.write(albumList, HomeDataConfig.HOME_RECOMMEND_LIST_QUIT_DIR);
        } catch (IOException e) {
            LogUtils.e("RecommendQuitApkProvider", "write recommend failed");
        }
    }

    public void clearCache() {
        new File(AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + HomeDataConfig.HOME_RECOMMEND_LIST_QUIT_DIR).delete();
    }

    public List<Album> getRecommendList() {
        List<Album> albumList = null;
        try {
            return (List) SerializableUtils.read(HomeDataConfig.HOME_RECOMMEND_LIST_QUIT_DIR);
        } catch (Exception e) {
            LogUtils.e("RecommendQuitApkProvider", "read recommend failed");
            return albumList;
        }
    }
}
