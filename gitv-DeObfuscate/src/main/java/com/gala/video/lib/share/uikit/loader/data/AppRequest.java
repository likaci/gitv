package com.gala.video.lib.share.uikit.loader.data;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gala.appmanager.GalaAppManager;
import com.gala.appmanager.appinfo.AppInfo;
import com.gala.video.api.ApiFactory;
import com.gala.video.api.ICommonApiCallback;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.cache.UikitSourceDataCache;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.CardInfoBuildTool;
import com.gala.video.lib.share.uikit.loader.IUikitDataFetcherCallback;
import com.gala.video.lib.share.utils.Precondition;
import com.gitv.tvappstore.AppStoreManager;
import com.gitv.tvappstore.AppStoreManager.OnGetUrlListener;
import com.mcto.ads.CupidAd;
import com.push.pushservice.constants.DataConst;
import com.tvos.appdetailpage.config.APIConstants;
import java.util.ArrayList;
import java.util.List;
import retrofit.RetrofitError;

public class AppRequest {
    private static final String TAG = "AppRequest";

    public static class AppType {
        public static final int APPSTORE = 3;
        public static final int LOCAL = 1;
        public static final int OPERATOR = 2;
    }

    public static void callApp(CardInfoModel model, IUikitDataFetcherCallback callback) {
        int i = checkApp();
        if (i == 0) {
            return;
        }
        if (i == 2) {
            Log.d(TAG, "AppType.OPERATOR");
            model.setTitle("应用推荐");
            final IUikitDataFetcherCallback iUikitDataFetcherCallback = callback;
            final CardInfoModel cardInfoModel = model;
            ApiFactory.getCommonApi().callSync("http://store." + Project.getInstance().getBuild().getDomainName() + "/apis/tv/launcher/app_index.action?agent_type=5202", new ICommonApiCallback() {
                public void onSuccess(String s) {
                    int i;
                    JSONObject o;
                    if (StringUtils.isEmpty((CharSequence) s)) {
                        iUikitDataFetcherCallback.onFailed();
                    }
                    List<AppStore> applist = new ArrayList(10);
                    JSONObject app = (JSONObject) JSON.parse(s);
                    if (app == null) {
                        iUikitDataFetcherCallback.onFailed();
                    }
                    JSONObject data = (JSONObject) app.get("data");
                    if (data == null) {
                        iUikitDataFetcherCallback.onFailed();
                    }
                    JSONObject appStore = data.getJSONObject(CupidAd.CREATIVE_TYPE_APPSTORE);
                    if (appStore != null) {
                        AppStore appStore1 = new AppStore();
                        appStore1.app_name = appStore.getString("app_name");
                        appStore1.app_type = 4;
                        appStore1.app_download_url = appStore.getString("app_download_url");
                        appStore1.app_id = appStore.getString(DataConst.APP_INFO_APP_ID);
                        appStore1.app_package_name = appStore.getString(APIConstants.BROADCAST_EXTRA_PKG_NAME);
                        appStore1.app_image_url = "share_app_store_icon";
                        applist.add(appStore1);
                    }
                    JSONArray forcusList = data.getJSONArray("focus_info_list");
                    if (forcusList != null && forcusList.size() > 0) {
                        for (i = 0; i < forcusList.size(); i++) {
                            o = (JSONObject) forcusList.get(i);
                            if (o != null) {
                                AppStore store = new AppStore();
                                store.app_name = o.getString("appTitle");
                                store.app_type = 2;
                                store.app_id = o.getString(DataConst.APP_INFO_APP_ID);
                                store.app_image_url = o.getString("logoUrl");
                                store.app_download_url = o.getString("downloadUrl");
                                store.app_package_name = o.getString(SettingConstants.ACTION_TYPE_PACKAGE_NAME);
                                applist.add(store);
                            }
                        }
                    }
                    JSONArray collectionList = data.getJSONArray("collection_list");
                    if (collectionList != null && collectionList.size() > 0) {
                        for (i = 0; i < collectionList.size(); i++) {
                            o = (JSONObject) collectionList.get(i);
                            if (o != null) {
                                JSONArray apps = o.getJSONArray("appList");
                                if (apps != null && apps.size() > 0) {
                                    store = new AppStore();
                                    JSONObject appObject = (JSONObject) apps.get(0);
                                    store.app_type = 2;
                                    store.app_id = appObject.getString(DataConst.APP_INFO_APP_ID);
                                    store.app_image_url = appObject.getString("app_logo");
                                    store.app_download_url = appObject.getString("app_download_url");
                                    store.app_package_name = appObject.getString(APIConstants.BROADCAST_EXTRA_PKG_NAME);
                                    store.app_name = appObject.getString("app_name");
                                    applist.add(store);
                                }
                            }
                        }
                    }
                    JSONArray detailList = data.getJSONArray("app_detail_list");
                    if (detailList != null && detailList.size() > 0) {
                        for (i = 0; i < detailList.size(); i++) {
                            o = (JSONObject) detailList.get(i);
                            if (o != null) {
                                store = new AppStore();
                                store.app_type = 2;
                                store.app_id = o.getString(DataConst.APP_INFO_APP_ID);
                                store.app_name = o.getString("app_name");
                                store.app_image_url = o.getString("app_logo");
                                store.app_download_url = o.getString("app_download_url");
                                store.app_package_name = o.getString(APIConstants.BROADCAST_EXTRA_PKG_NAME);
                                applist.add(store);
                            }
                        }
                    }
                    UikitSourceDataCache.writeAppDataList(applist);
                    CardInfoModel appCard = CardInfoBuildTool.buildAppCard(cardInfoModel, applist);
                    List<CardInfoModel> appCardList = new ArrayList(1);
                    appCardList.add(appCard);
                    iUikitDataFetcherCallback.onSuccess(appCardList, "");
                }

                public void onException(Exception e, String s) {
                    iUikitDataFetcherCallback.onFailed();
                }
            }, false, "appStore");
        } else if (i == 3) {
            Log.d(TAG, "AppType.APPSTORE");
            model.setTitle("应用");
            try {
                applist = new ArrayList(10);
                AppStore allEnter = new AppStore();
                allEnter.app_type = 3;
                allEnter.app_name = "我的应用";
                allEnter.app_image_url = "share_item_allapps_icon";
                applist.add(allEnter);
                AppStoreManager mAppStoreManager = AppStoreManager.getInstance();
                mAppStoreManager.init(AppRuntimeEnv.get().getApplicationContext(), Project.getInstance().getBuild().getAppStorePkgName(), AppRuntimeEnv.get().getDefaultUserId());
                final CardInfoModel cardInfoModel2 = model;
                final IUikitDataFetcherCallback iUikitDataFetcherCallback2 = callback;
                mAppStoreManager.fetchDownloadUrl(new OnGetUrlListener() {
                    public void onSuccess(String s) {
                        if (!(s == null || s.isEmpty())) {
                            AppStore appStore = new AppStore();
                            appStore.app_type = 4;
                            appStore.app_name = "应用商店";
                            appStore.app_image_url = "share_app_store_icon";
                            appStore.app_download_url = s;
                            appStore.app_package_name = "com.gitv.tvappstore";
                            applist.add(appStore);
                        }
                        List<AppInfo> list = GalaAppManager.getInstance(AppRuntimeEnv.get().getApplicationContext()).getAllApps();
                        if (list == null || list.size() <= 0) {
                            CardInfoModel appCard = CardInfoBuildTool.buildAppCard(cardInfoModel2, applist);
                            UikitSourceDataCache.writeAppDataList(applist);
                            List<CardInfoModel> appCardList = new ArrayList(1);
                            appCardList.add(appCard);
                            iUikitDataFetcherCallback2.onSuccess(appCardList, "");
                            return;
                        }
                        for (AppInfo info : list) {
                            appStore = new AppStore();
                            appStore.app_type = 1;
                            appStore.app_name = info.getAppName();
                            appStore.app_download_url = info.getApkAbsolutePath();
                            appStore.app_package_name = info.getAppPackageName();
                            applist.add(appStore);
                        }
                        appCard = CardInfoBuildTool.buildAppCard(cardInfoModel2, applist);
                        UikitSourceDataCache.writeAppDataList(applist);
                        appCardList = new ArrayList(1);
                        appCardList.add(appCard);
                        iUikitDataFetcherCallback2.onSuccess(appCardList, "");
                    }

                    public void onFailure(RetrofitError retrofitError) {
                        List<AppInfo> list = GalaAppManager.getInstance(AppRuntimeEnv.get().getApplicationContext()).getAllApps();
                        if (list == null || list.size() <= 0) {
                            CardInfoModel appCard = CardInfoBuildTool.buildAppCard(cardInfoModel2, applist);
                            UikitSourceDataCache.writeAppList(appCard);
                            List<CardInfoModel> appCardList = new ArrayList(1);
                            appCardList.add(appCard);
                            iUikitDataFetcherCallback2.onSuccess(appCardList, "");
                            return;
                        }
                        for (AppInfo info : list) {
                            AppStore appStore = new AppStore();
                            appStore.app_name = info.getAppName();
                            appStore.app_download_url = info.getApkAbsolutePath();
                            appStore.app_package_name = info.getAppPackageName();
                            applist.add(appStore);
                        }
                        appCard = CardInfoBuildTool.buildAppCard(cardInfoModel2, applist);
                        UikitSourceDataCache.writeAppList(appCard);
                        appCardList = new ArrayList(1);
                        appCardList.add(appCard);
                        iUikitDataFetcherCallback2.onSuccess(appCardList, "");
                    }
                });
            } catch (Exception e) {
                LogUtils.m1572e(TAG, "QAppStoreDataRequest() -> mAppStoreManager e:", e);
                callback.onFailed();
            }
        } else if (i == 1) {
            Log.d(TAG, "AppType.LOCAL");
            model.setTitle("全部应用");
            applist = new ArrayList(10);
            AppStore appStore = new AppStore();
            appStore.app_type = 3;
            appStore.app_name = "全部应用";
            appStore.app_image_url = "share_item_allapps_icon";
            applist.add(appStore);
            List<AppInfo> list = GalaAppManager.getInstance(AppRuntimeEnv.get().getApplicationContext()).getAllApps();
            if (list == null || list.size() <= 0) {
                callback.onFailed();
                return;
            }
            for (AppInfo info : list) {
                AppStore appStore1 = new AppStore();
                appStore1.app_type = 1;
                appStore1.app_name = info.getAppName();
                appStore1.app_download_url = info.getApkAbsolutePath();
                appStore1.app_package_name = info.getAppPackageName();
                applist.add(appStore1);
            }
            CardInfoModel appCard = CardInfoBuildTool.buildAppCard(model, applist);
            UikitSourceDataCache.writeAppDataList(applist);
            List<CardInfoModel> appCardList = new ArrayList(1);
            appCardList.add(appCard);
            callback.onSuccess(appCardList, "");
        }
    }

    public static int checkApp() {
        IDynamicResult result = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (!Precondition.isNull(result)) {
            int app = result.getAppCard();
            if (app == 2) {
                return 2;
            }
            if (app == 3) {
                return 3;
            }
            if (app == 1) {
                return 1;
            }
        }
        if (Project.getInstance().getBuild().isHomeVersion()) {
            return 1;
        }
        return 0;
    }
}
