package com.gala.video.app.epg.home.data.provider;

import com.gala.video.app.epg.home.data.model.AppDataModel;
import com.gala.video.app.epg.preference.AppStorePreference;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import java.util.ArrayList;
import java.util.List;

public class AppsProvider {
    private static final AppsProvider sAppsProvider = new AppsProvider();
    private List<AppDataModel> mAppsList = new ArrayList();
    private String mDownloadUrl;

    private AppsProvider() {
    }

    public static AppsProvider getInstance() {
        return sAppsProvider;
    }

    public String getDownloadUrl() {
        if (StringUtils.isEmpty(this.mDownloadUrl)) {
            return AppStorePreference.getApkDownloadUrl(AppRuntimeEnv.get().getApplicationContext());
        }
        return this.mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.mDownloadUrl = downloadUrl;
    }

    public void addApp(AppDataModel model) {
        this.mAppsList.add(model);
    }

    public void removeApp(AppDataModel model) {
        this.mAppsList.remove(model);
    }

    public void remove(int index) {
        this.mAppsList.remove(index);
    }

    public List<AppDataModel> getAppsList() {
        if (this.mAppsList.size() == 0) {
            try {
                return (List) SerializableUtils.read(HomeDataConfig.HOME_APP_OPERATOR_LIST_DIR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.mAppsList;
    }

    public void clearAppsList() {
        this.mAppsList.clear();
    }
}
