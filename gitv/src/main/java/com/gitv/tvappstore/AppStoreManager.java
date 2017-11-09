package com.gitv.tvappstore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.widget.Toast;
import com.gala.sdk.player.constants.PlayerIntentConfig;
import com.gitv.tvappstore.model.AppCategoryInfo;
import com.gitv.tvappstore.model.AppDetailInfo;
import com.gitv.tvappstore.model.CategoryInfo;
import com.gitv.tvappstore.model.LocalAppInfo;
import com.gitv.tvappstore.model.RemoteAppInfo;
import com.gitv.tvappstore.utils.AppCommUtils;
import com.gitv.tvappstore.utils.AppInfo;
import com.gitv.tvappstore.utils.CommonUtils;
import com.gitv.tvappstore.utils.SqliteManager;
import com.gitv.tvappstore.utils.StringUtils;
import com.gitv.tvappstore.utils.SysUtils;
import com.gitv.tvappstore.utils.download.InstallListener;
import com.gitv.tvappstore.utils.download.InstallManager;
import com.push.mqttv3.internal.ClientDefaults;
import com.tvos.appdetailpage.client.AppStoreClient;
import com.tvos.appdetailpage.client.AppStoreClient.Builder;
import com.tvos.appdetailpage.client.Constants;
import com.tvos.appdetailpage.info.AdverIndexResponse;
import com.tvos.appdetailpage.info.AdverIndexResponse.AdverIndexInfo;
import com.tvos.appdetailpage.info.AppCategoriesResponse;
import com.tvos.appdetailpage.info.AppCategory;
import com.tvos.appdetailpage.info.AppDetail;
import com.tvos.appdetailpage.info.AppDetail.ResInfo;
import com.tvos.appdetailpage.info.AppDetailResponse;
import com.tvos.appmanager.AppManager;
import com.tvos.appmanager.IAppManager;
import com.tvos.apps.utils.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AppStoreManager implements InstallListener {
    private static final String APP_STORE_ENTRY = "normal";
    private static final String CHANNEL_ID = "TVLauncher";
    private static final String PRODUCT = "8003";
    private static List<String> filterPkgNameList = null;
    private static Context mContext = null;
    private static IAppManager mIAppManager = null;
    private static AppStoreManager mInstance = new AppStoreManager();
    private static final int necessaryAppsVersion = 130;
    private final String LOG_TAG = getClass().getSimpleName();
    private List<AppInfo> mAppInfos;
    private AppStoreClient mClient;
    private Map<String, AppDetailInfo> mDetailedAdverMap;
    private Map<String, AppDetailInfo> mDetailedLocalMap;
    private Map<String, AppCategoryInfo> mDetailedRecomMap;
    private InstallManager mInstallManager;
    private List<String> mNewPackages;
    private PackageManager mPackageManager;
    private String mPackageName;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            AppStoreManager.this.mSqliteManager.delete(intent.getStringExtra(com.tvos.appdetailpage.config.APIConstants.BROADCAST_EXTRA_PKG_NAME));
        }
    };
    private List<RemoteAppInfo> mSimpleAdverList;
    private List<LocalAppInfo> mSimpleLocalList;
    private List<CategoryInfo> mSimpleRecomList;
    private SqliteManager mSqliteManager;
    private OnLocalAppInfoListUpdatedListener mUpdatedListener;

    public interface OnGetUrlListener {
        void onFailure(RetrofitError retrofitError);

        void onSuccess(String str);
    }

    public interface APIConstants {
        public static final String ACTION_ALL_APP = ".action.AllApp";
        public static final String ACTION_APP_CATEGORY = ".action.AppCategory";
        public static final String ACTION_APP_DETAIL = ".action.AppDetail";
        public static final String ACTION_CATEGORIES = ".action.Categories";
        public static final String ACTION_SEARCH = ".action.Search";
        public static final String APPBACKEND_ENDPOINT = "http://store.ptqy.gitv.tv";
        public static final String APPDETAIL_FIELDS = "basic,res";
        public static final String APPSTORE_PKGNAME = "com.gitv.tvappstore";
        public static final String BLOCK_ADVER = "banner";
        public static final String BLOCK_ALL_APP_LIST = "all_applist";
        public static final String BLOCK_ALL_GAME = "all_game";
        public static final String BLOCK_ALL_LIST = "all_list";
        public static final String BLOCK_APP = "app";
        public static final String BLOCK_APP_LIST = "app_list";
        public static final String BLOCK_CATE_GAME_LIST = "cate_game_list";
        public static final String BLOCK_CATE_LIST = "cate_list";
        public static final String BLOCK_CLEAR = "dedone";
        public static final String BLOCK_DETAIL = "detail";
        public static final String BLOCK_DOWNLOAD = "download";
        public static final String BLOCK_GAME = "game";
        public static final String BLOCK_GAME_LIST = "game_list";
        public static final String BLOCK_HOME = "home";
        public static final String BLOCK_INSTALL = "install";
        public static final String BLOCK_LAUNCH = "boot";
        public static final String BLOCK_MY_APP = "my_app";
        public static final String BLOCK_OPEN = "open";
        public static final String BLOCK_RECOMMEND = "recommend";
        public static final String BLOCK_REMOVE = "remove";
        public static final String BLOCK_SEARCH_RESULT = "search_result";
        public static final String BLOCK_SEARCH_TEXT = "search_text";
        public static final String BLOCK_SEARCH_VIEW = "search_view";
        public static final String BLOCK_UPDATE = "update";
        public static final String BUNDLE_EXTRA_CATEGORYT = "category";
        public static final String BUNDLE_EXTRA_DETAILAPP = "singleAppInfo";
        public static final int CATEGORY_APP = 2;
        public static final String CATEGORY_FLAG = "category_flag";
        public static final int CATEGORY_GAME = 1;
        public static final String CLOUD_PUSHED_PINGBACK_ENDPOINT = "http://msg.71.am";
        public static final String ID_CATE_APP = "-1";
        public static final String ID_CATE_GAME = "-2";
        public static final String LONGYUAN4_ENDPOINT = "http://msg.ptqy.gitv.tv";
        public static final String LONGYUAN_ENDPOINT = "http://msg.video.ptqy.gitv.tv";
        public static final String NECESSARY_APPS_TITLE = "装机必备";
        public static final int PACKAGE_ADDED = 0;
        public static final int PACKAGE_REMOVED = 1;
        public static final String QISO_ENDPOINT = "http://search.video.ptqy.gitv.tv";
        public static final String QIYU_ENDPOINT = "http://qiyu.ptqy.gitv.tv";
        public static final String RPAGE_ALL_GAME_LIST = "all_gamelist";
        public static final String RPAGE_ALL_LIST = "all_list";
        public static final String RPAGE_CATE_ALL_LIST = "cate_all_list";
        public static final String RPAGE_CATE_GAME_LIST = "cate_game_list";
        public static final String RPAGE_CATE_LIST = "cate_list";
        public static final String RPAGE_CATE_OTHER_LIST = "cate_other_list";
        public static final String RPAGE_DETAIL = "detail";
        public static final String RPAGE_HOME = "home";
        public static final String RPAGE_LAUNCH = "boot";
        public static final String RPAGE_MY_APP = "my_app";
        public static final String RPAGE_SEARCH = "search";
        public static final String RSEAT_ALL_GAME_LIST = "0";
        public static final String RSEAT_ALL_LIST = "0";
        public static final String RSEAT_APP = "1";
        public static final String RSEAT_APP_LIST = "0";
        public static final String RSEAT_CATE_GAME_LIST = "0";
        public static final String RSEAT_CATE_LIST = "0";
        public static final String RSEAT_CLEAR = "6";
        public static final String RSEAT_CLICK_PRE = "1-";
        public static final String RSEAT_DETAIL = "0";
        public static final String RSEAT_DOWNLOAD = "1";
        public static final String RSEAT_GAME = "2";
        public static final String RSEAT_HOME = "0";
        public static final String RSEAT_INSTALL = "5";
        public static final String RSEAT_LAUNCH = "0";
        public static final String RSEAT_MY_APP = "0";
        public static final String RSEAT_OPEN = "2";
        public static final String RSEAT_REMOVE = "4";
        public static final String RSEAT_SEARCH_TEXT = "0";
        public static final String RSEAT_SEARCH_VIEW = "0";
        public static final String RSEAT_UPDATE = "3";
        public static final String TV_LAUNCHER_TOPIC_FLAG = "necessary_apps_flag";
        public static final String TV_UPDATE_ENDPOINT = "http://store.ptqy.gitv.tv";
        public static final String USERGAME_ENDPOINT = "http://usergame.gitv.com/services";
    }

    public interface OnGetAdvAppInfoListListener {
        void onFailure(RetrofitError retrofitError);

        void onSuccess(List<RemoteAppInfo> list);
    }

    public interface OnGetRecomCategoryListListener {
        void onFailure(RetrofitError retrofitError);

        void onSuccess(List<CategoryInfo> list);
    }

    public interface OnLocalAppInfoListUpdatedListener {
        void onLocalAppInfoListReceived(List<LocalAppInfo> list);
    }

    private AppStoreManager() {
    }

    public static AppStoreManager getInstance() {
        return mInstance;
    }

    public static void initAppManager() {
        setFilterPkgNameList();
        mIAppManager = AppManager.createAppManager(mContext);
        mIAppManager.setBlackPkgList(filterPkgNameList);
    }

    public static IAppManager getAppManager() {
        if (mIAppManager == null) {
            initAppManager();
        }
        return mIAppManager;
    }

    private static void setFilterPkgNameList() {
        if (filterPkgNameList == null) {
            filterPkgNameList = new ArrayList();
            filterPkgNameList.add("com.gitv.tvappstore");
            filterPkgNameList.add("com.tvos.tvappstore");
            filterPkgNameList.add(PlayerIntentConfig.URI_AUTH);
        }
    }

    public void init(Context context, String packageName, String deviceID) {
        mContext = context.getApplicationContext();
        this.mPackageName = packageName;
        initSdk(mContext, deviceID);
        initLocalApp();
        registerReceiver();
        this.mClient = AppStoreClient.getInstance();
        this.mInstallManager = new InstallManager(mContext, this);
        this.mInstallManager.register();
    }

    public void setOnLocalAppInfoListUpdatedListener(OnLocalAppInfoListUpdatedListener listener) {
        this.mUpdatedListener = listener;
    }

    private void initSdk(Context context, String deviceId) {
        new Builder().setDeviceID(deviceId).setAppStoreEntry("normal").setAppVersion(new StringBuilder(String.valueOf(AppCommUtils.getMyPackageInfo(context).versionCode)).toString()).setUserID(deviceId, null).setImei(deviceId).setCookie(null).setChannelID(CHANNEL_ID).setMacID(SysUtils.getMacAddr(context)).setOsVersion(new StringBuilder(String.valueOf(VERSION.SDK_INT)).toString()).setOsRooted(Boolean.valueOf(false)).setCharSet("UTF-8").setPlatform("3", Constants.PINGBACK_4_0_P_TV_APP).setProduct("312", PRODUCT).setServers("http://qiyu.ptqy.gitv.tv", "http://search.video.ptqy.gitv.tv", "http://msg.video.ptqy.gitv.tv", "http://msg.ptqy.gitv.tv", "http://store.ptqy.gitv.tv", "http://msg.71.am", "http://store.ptqy.gitv.tv").build();
    }

    private void registerReceiver() {
        mContext.registerReceiver(this.mReceiver, new IntentFilter("com.gala.tvappstore.ACTION_OPEN_APP"));
    }

    public void install(String path) {
        this.mInstallManager.install(path);
    }

    public void fetchDownloadUrl(final OnGetUrlListener listener) {
        this.mClient.getAppDetail("", this.mPackageName, "", "basic,res", new Callback<AppDetailResponse>() {
            public void success(AppDetailResponse resp, Response arg1) {
                AppDetail data = resp.data;
                String url = null;
                if (data != null) {
                    url = data.basic.app_download_url;
                }
                listener.onSuccess(url);
            }

            public void failure(RetrofitError error) {
                listener.onFailure(error);
            }
        });
    }

    public void fetchAdvAppInfoList(final OnGetAdvAppInfoListListener listener) {
        this.mClient.getAdverIndex(new Callback<AdverIndexResponse>() {
            public void success(AdverIndexResponse resp, Response arg1) {
                if (CommonUtils.isListEmpty(resp.data)) {
                    LogUtils.e(AppStoreManager.this.LOG_TAG, new StringBuilder(String.valueOf(AppStoreManager.this.LOG_TAG)).append(">>>>>>>>>>>>>>>>>>>>>>> adv resp.data is null  -  ").append(arg1.getUrl()).toString());
                    if (listener != null) {
                        listener.onSuccess(AppStoreManager.this.mSimpleAdverList);
                        return;
                    }
                    return;
                }
                AppStoreManager.this.transToNativeAdvModel(resp.data);
                if (listener != null) {
                    listener.onSuccess(AppStoreManager.this.mSimpleAdverList);
                }
            }

            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.onFailure(error);
                }
            }
        });
    }

    private List<String> getAppImageUrl(ArrayList<ResInfo> resImageInfos) {
        if (CommonUtils.isListEmpty(resImageInfos)) {
            return null;
        }
        int resImageSize = resImageInfos.size();
        List<String> appImageLists = new ArrayList();
        for (int i = 0; i < resImageSize; i++) {
            appImageLists.add(((ResInfo) resImageInfos.get(i)).media_url);
        }
        return appImageLists;
    }

    private void transToNativeAdvModel(List<AdverIndexInfo> data) {
        this.mDetailedAdverMap = new HashMap();
        this.mSimpleAdverList = new ArrayList();
        for (AdverIndexInfo respInfo : data) {
            AppDetailInfo detailInfo = new AppDetailInfo();
            List<String> mListImage = new ArrayList();
            detailInfo.setAppDescription(respInfo.descriptionDetails);
            detailInfo.setAppDownloadUrl(respInfo.downloadUrl);
            detailInfo.setAppId(respInfo.app_id);
            detailInfo.setAppImageUrl(mListImage);
            detailInfo.setAppLogoUrl(respInfo.logoUrl);
            detailInfo.setAppNum(new StringBuilder(String.valueOf(respInfo.downloadCount)).toString());
            detailInfo.setAppPackageName(respInfo.packageName);
            detailInfo.setApppackageSize(new StringBuilder(String.valueOf(respInfo.packageSize)).toString());
            detailInfo.setAppPublishTime(respInfo.publishTime);
            detailInfo.setAppRating(respInfo.appRating);
            detailInfo.setAppTitle(respInfo.appTitle);
            detailInfo.setAppType(respInfo.appType);
            detailInfo.setAppVersion(respInfo.appVersion);
            detailInfo.setAdverImageUrl(respInfo.img_url);
            if (respInfo.res != null) {
                detailInfo.setAppImageUrl(respInfo.res);
            }
            this.mDetailedAdverMap.put(respInfo.app_id, detailInfo);
            RemoteAppInfo remoteInfo = new RemoteAppInfo();
            remoteInfo.setAdvImgUrl(respInfo.img_url);
            remoteInfo.setAppId(respInfo.app_id);
            this.mSimpleAdverList.add(remoteInfo);
        }
    }

    public void fetchRecomCategoryList(final OnGetRecomCategoryListListener listener) {
        this.mClient.getAppRecomCategiries(new Callback<AppCategoriesResponse>() {
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.onFailure(error);
                }
            }

            public void success(AppCategoriesResponse resp, Response arg1) {
                if (CommonUtils.isListEmpty(resp.data)) {
                    LogUtils.e(AppStoreManager.this.LOG_TAG, new StringBuilder(String.valueOf(AppStoreManager.this.LOG_TAG)).append(">>>>>>>>>>>>>>>>>>>>>>> recom resp.data is null  -  ").append(arg1.getUrl()).toString());
                } else {
                    AppStoreManager.this.transToNativeRecomModel(resp.data);
                }
                if (listener != null) {
                    listener.onSuccess(AppStoreManager.this.mSimpleRecomList);
                }
            }
        });
    }

    private void transToNativeRecomModel(List<AppCategory> data) {
        this.mDetailedRecomMap = new HashMap();
        this.mSimpleRecomList = new ArrayList();
        for (AppCategory respInfo : data) {
            AppCategoryInfo appCateInfo = new AppCategoryInfo();
            appCateInfo.setDes(respInfo.category_desc);
            appCateInfo.setIconUrl(respInfo.category_icon);
            appCateInfo.setId(Integer.valueOf(respInfo.id).intValue());
            appCateInfo.setName(respInfo.category_name);
            appCateInfo.setParentId(respInfo.parent_id);
            this.mDetailedRecomMap.put(respInfo.id, appCateInfo);
            CategoryInfo cateInfo = new CategoryInfo();
            cateInfo.setCategoryId(respInfo.id);
            cateInfo.setCategoryName(respInfo.category_name);
            cateInfo.setCategoryIconUrl(respInfo.category_icon);
            this.mSimpleRecomList.add(cateInfo);
        }
    }

    private void initLocalApp() {
        this.mSqliteManager = new SqliteManager(mContext);
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        this.mPackageManager = mContext.getPackageManager();
        List<ResolveInfo> resolveInfos = this.mPackageManager.queryIntentActivities(intent, 0);
        if (resolveInfos != null) {
            this.mAppInfos = new ArrayList();
            for (ResolveInfo resovleInfo : resolveInfos) {
                AppInfo appInfo = new AppInfo(resovleInfo);
                appInfo.setAppName(resovleInfo.loadLabel(this.mPackageManager).toString());
                this.mAppInfos.add(appInfo);
            }
        }
        if (!CommonUtils.isListEmpty(this.mAppInfos)) {
            transToNativeLocalModel(this.mAppInfos);
        }
    }

    private void transToNativeLocalModel(List<AppInfo> data) {
        this.mDetailedLocalMap = new HashMap();
        for (AppInfo respInfo : data) {
            AppDetailInfo detailInfo = new AppDetailInfo();
            List<String> mListImage = new ArrayList();
            detailInfo.setAppDescription("");
            detailInfo.setAppDownloadUrl("");
            detailInfo.setAppId("");
            detailInfo.setAppImageUrl(mListImage);
            detailInfo.setAppIsSystem(respInfo.getIsSystem());
            detailInfo.setAppLogoUrl("");
            detailInfo.setAppNum("");
            detailInfo.setAppPackageName(respInfo.getAppPackageName());
            detailInfo.setApppackageSize("");
            detailInfo.setAppPublishTime("");
            detailInfo.setAppRating("");
            detailInfo.setAppTitle(respInfo.getAppName());
            detailInfo.setAppType("");
            detailInfo.setAppVersion("");
            detailInfo.setFromMyApp(true);
            this.mDetailedLocalMap.put(respInfo.getAppPackageName(), detailInfo);
        }
    }

    public List<LocalAppInfo> getLocalAppInfoList() {
        List<LocalAppInfo> appInfoList = new ArrayList();
        this.mSimpleLocalList = new ArrayList();
        this.mNewPackages = this.mSqliteManager.queryAll();
        for (AppInfo appInfo : this.mAppInfos) {
            LocalAppInfo localInfo = transToNativeLocalModel(appInfo);
            this.mSimpleLocalList.add(localInfo);
            try {
                localInfo.setAppIcon(this.mPackageManager.getApplicationIcon(localInfo.getPkgName()));
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            appInfoList.add(localInfo);
        }
        return appInfoList;
    }

    private LocalAppInfo transToNativeLocalModel(AppInfo appInfo) {
        final LocalAppInfo localInfo = new LocalAppInfo();
        localInfo.setAppIcon(appInfo.getAppIcon());
        localInfo.setAppName(appInfo.getAppName());
        localInfo.setPkgName(appInfo.getAppPackageName());
        localInfo.setNew(isNewApp(appInfo));
        localInfo.setHasUpdate(false);
        this.mClient.getAppDetail("", localInfo.getPkgName(), "", "basic,res", new Callback<AppDetailResponse>() {
            public void success(AppDetailResponse resp, Response arg1) {
                if (resp.data != null) {
                    String lastVer = resp.data.basic.latest_version;
                    String appPackageName = localInfo.getPkgName();
                    AppDetailInfo detailInfo = (AppDetailInfo) AppStoreManager.this.mDetailedLocalMap.get(appPackageName);
                    if (detailInfo != null) {
                        detailInfo.setAppId(resp.data.basic.app_id);
                        detailInfo.setAppVersion(resp.data.basic.latest_version);
                    }
                    if (AppStoreManager.this.shouldUpgrade(lastVer, appPackageName)) {
                        localInfo.setHasUpdate(true);
                        List<LocalAppInfo> tmpAppInfos = new ArrayList();
                        for (LocalAppInfo appInfo : AppStoreManager.this.mSimpleLocalList) {
                            try {
                                appInfo.setAppIcon(AppStoreManager.this.mPackageManager.getApplicationIcon(appInfo.getPkgName()));
                            } catch (NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            tmpAppInfos.add(appInfo);
                        }
                        if (AppStoreManager.this.mUpdatedListener != null) {
                            AppStoreManager.this.mUpdatedListener.onLocalAppInfoListReceived(tmpAppInfos);
                        }
                    }
                }
            }

            public void failure(RetrofitError arg0) {
            }
        });
        return localInfo;
    }

    public void openSearchActivity() {
        Intent intent = new Intent(this.mPackageName + APIConstants.ACTION_SEARCH);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        mContext.startActivity(intent);
    }

    public void openAllAppAcvitity() {
        Intent intent = new Intent(this.mPackageName + APIConstants.ACTION_ALL_APP);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        mContext.startActivity(intent);
    }

    public void openNecessaryAppsAcvitity() {
        try {
            if (AppCommUtils.getVersion(mContext, "com.gitv.tvappstore").getVerCode() < 130) {
                Toast.makeText(mContext, "应用商店版本太低！", 1).show();
                return;
            }
            Intent intent = new Intent(this.mPackageName + APIConstants.ACTION_APP_CATEGORY);
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            intent.putExtra("necessary_apps_flag", true);
            mContext.startActivity(intent);
        } catch (NameNotFoundException e) {
            Toast.makeText(mContext, "未安装银河应用商店！", 1).show();
            e.printStackTrace();
        }
    }

    public String getNecessaryAppsTitle() {
        return APIConstants.NECESSARY_APPS_TITLE;
    }

    public void openAppStore() {
        getAppManager().startApp("com.gitv.tvappstore");
    }

    public void openCategoryActivity(String categoryId, int rseat) {
        if (categoryId == "-1" || categoryId == "-2") {
            gotoCategories(categoryId);
        } else {
            gotoAppCategory(categoryId, rseat);
        }
    }

    private void gotoCategories(String categoryId) {
        int flag = 0;
        String rpage = "home";
        String block = "";
        String rseat = "";
        if (categoryId == "-1") {
            flag = 2;
            block = "app";
            rseat = "1-1";
        } else if (categoryId == "-2") {
            flag = 1;
            block = "game";
            rseat = "1-2";
        }
        sendPingbackCategoriesClick(rpage, block, rseat);
        Intent intent = new Intent(this.mPackageName + APIConstants.ACTION_CATEGORIES);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        intent.putExtra("category_flag", flag);
        mContext.startActivity(intent);
    }

    private void gotoAppCategory(String categoryId, int rseat) {
        if (this.mDetailedRecomMap != null) {
            AppCategoryInfo info = (AppCategoryInfo) this.mDetailedRecomMap.get(categoryId);
            if (info != null) {
                int flag = 0;
                if (categoryId.startsWith("1")) {
                    flag = 1;
                } else if (categoryId.startsWith("2")) {
                    flag = 2;
                }
                if (flag != 0) {
                    sendRecommendClick("home", "recommend", rseat);
                    Intent intent = new Intent(this.mPackageName + APIConstants.ACTION_APP_CATEGORY);
                    intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("category", info);
                    bundle.putInt("category_flag", flag);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        }
    }

    public void sendLocalAppLaunchPingback(String pkgName, int rseat) {
        AppDetailInfo detailInfo = (AppDetailInfo) this.mDetailedLocalMap.get(pkgName);
        if (detailInfo != null) {
            sendPingbackMyAppClick(rseat, detailInfo);
            if (!detailInfo.getAppId().equals("") && !detailInfo.getAppVersion().equals("")) {
                sendLaunchAppPingBack(detailInfo, "home", "open", "2");
            }
        }
    }

    public void openAppDetailActivity(String appId, String pkgName, int rseat) {
        Intent intent;
        Bundle bundle;
        if (appId == null || appId.equals("")) {
            if (pkgName != null && !pkgName.equals("") && this.mDetailedLocalMap != null) {
                AppDetailInfo detailInfo = (AppDetailInfo) this.mDetailedLocalMap.get(pkgName);
                if (detailInfo != null) {
                    sendPingbackMyAppClick(rseat, detailInfo);
                    intent = new Intent(this.mPackageName + APIConstants.ACTION_APP_DETAIL);
                    intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
                    bundle = new Bundle();
                    bundle.putSerializable("singleAppInfo", detailInfo);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        } else if (this.mDetailedAdverMap != null) {
            AppDetailInfo info = (AppDetailInfo) this.mDetailedAdverMap.get(appId);
            if (info != null) {
                sendPingbackAdverClick(rseat, info);
                intent = new Intent(this.mPackageName + APIConstants.ACTION_APP_DETAIL);
                intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
                bundle = new Bundle();
                bundle.putSerializable("singleAppInfo", info);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        }
    }

    private boolean shouldUpgrade(String lastVer, String mAppPackageName) {
        if (StringUtils.isEmpty(lastVer)) {
            return false;
        }
        int lastVersion = Integer.parseInt(lastVer);
        try {
            int curVersionCode = AppCommUtils.getVersion(mContext, mAppPackageName).getVerCode();
            LogUtils.e(this.LOG_TAG, this.LOG_TAG + "--shouldUpgrade () >>>> curVersionCode" + curVersionCode + "lastVersion" + lastVersion);
            if (lastVersion > curVersionCode) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            LogUtils.e(this.LOG_TAG, this.LOG_TAG + "--shouldUpgrade () >>>>" + " NameNotFoundException, vercode is null" + e.getMessage());
            return false;
        }
    }

    private boolean isNewApp(AppInfo appInfo) {
        if (CommonUtils.isListEmpty(this.mNewPackages)) {
            return false;
        }
        for (String pa : this.mNewPackages) {
            if (pa.equals(appInfo.getAppPackageName())) {
                return true;
            }
        }
        return false;
    }

    private void sendPingbackAdverClick(int position, AppDetailInfo info) {
        this.mClient.onClick(info.getAppId(), String.valueOf(AppCommUtils.getMyPackageInfo(mContext).versionCode), "home", "banner", "1-" + String.valueOf(position), "", new ResponseCallback() {
            public void failure(RetrofitError arg0) {
                LogUtils.e(AppStoreManager.this.LOG_TAG, new StringBuilder(String.valueOf(AppStoreManager.this.LOG_TAG)).append(">>>>>>>>>>>> pingback onAdverClick 发送失败").append(arg0.getMessage()).toString());
            }

            public void success(Response arg0) {
                LogUtils.i(AppStoreManager.this.LOG_TAG, new StringBuilder(String.valueOf(AppStoreManager.this.LOG_TAG)).append(">>>>>>>>>>>> pingback onAdverClick 发送成功").toString());
            }
        });
    }

    private void sendPingbackMyAppClick(int position, AppDetailInfo info) {
        this.mClient.onClick(info.getAppId(), String.valueOf(AppCommUtils.getMyPackageInfo(mContext).versionCode), "home", "my_app", "1-" + String.valueOf(position), "", new ResponseCallback() {
            public void failure(RetrofitError arg0) {
                LogUtils.e(AppStoreManager.this.LOG_TAG, new StringBuilder(String.valueOf(AppStoreManager.this.LOG_TAG)).append(">>>>>>>>>>>> pingback onMyAppClick 发送失败").append(arg0.getMessage()).toString());
            }

            public void success(Response arg0) {
                LogUtils.i(AppStoreManager.this.LOG_TAG, new StringBuilder(String.valueOf(AppStoreManager.this.LOG_TAG)).append(">>>>>>>>>>>> pingback onMyAppClick 发送成功").toString());
            }
        });
    }

    private void sendLaunchAppPingBack(AppDetailInfo detailInfo, String rpage, String block, String rseat) {
        this.mClient.onLaunchApp(detailInfo.getAppId(), detailInfo.getAppVersion(), rpage, block, rseat, new ResponseCallback() {
            public void failure(RetrofitError arg0) {
                LogUtils.e(AppStoreManager.this.LOG_TAG, new StringBuilder(String.valueOf(AppStoreManager.this.LOG_TAG)).append(">>>>>>>>>>>> pingback launchApp 发送失败").append(arg0.getMessage()).toString());
            }

            public void success(Response arg0) {
                LogUtils.i(AppStoreManager.this.LOG_TAG, new StringBuilder(String.valueOf(AppStoreManager.this.LOG_TAG)).append(">>>>>>>>>>>> pingback launcheApp 发送成功").toString());
            }
        });
    }

    private void sendPingbackCategoriesClick(String rpage, String block, String rseat) {
        this.mClient.onClick("", String.valueOf(AppCommUtils.getMyPackageInfo(mContext).versionCode), rpage, block, rseat, "", new ResponseCallback() {
            public void failure(RetrofitError arg0) {
                LogUtils.e(AppStoreManager.this.LOG_TAG, new StringBuilder(String.valueOf(AppStoreManager.this.LOG_TAG)).append(">>>>>>>>>>>> pingback onCategoriesClick 发送失败").append(arg0.getMessage()).toString());
            }

            public void success(Response arg0) {
                LogUtils.i(AppStoreManager.this.LOG_TAG, new StringBuilder(String.valueOf(AppStoreManager.this.LOG_TAG)).append(">>>>>>>>>>>> pingback onCategoriesClick 发送成功").toString());
            }
        });
    }

    private void sendRecommendClick(String rpage, String block, int position) {
        this.mClient.onClick("", String.valueOf(AppCommUtils.getMyPackageInfo(mContext).versionCode), rpage, block, "1-" + String.valueOf(position), "", new ResponseCallback() {
            public void failure(RetrofitError arg0) {
                LogUtils.e(AppStoreManager.this.LOG_TAG, new StringBuilder(String.valueOf(AppStoreManager.this.LOG_TAG)).append(">>>>>>>>>>>> pingback onRecommendClick 发送失败").append(arg0.getMessage()).toString());
            }

            public void success(Response arg0) {
                LogUtils.i(AppStoreManager.this.LOG_TAG, new StringBuilder(String.valueOf(AppStoreManager.this.LOG_TAG)).append(">>>>>>>>>>>> pingback onRecommendClick 发送成功").toString());
            }
        });
    }

    public void update(int index, int type, AppInfo app) {
        appOperation(type, app, index);
        if (this.mSimpleLocalList != null) {
            List<LocalAppInfo> tmpAppInfos = new ArrayList();
            for (LocalAppInfo appInfo : this.mSimpleLocalList) {
                try {
                    appInfo.setAppIcon(this.mPackageManager.getApplicationIcon(appInfo.getPkgName()));
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                tmpAppInfos.add(appInfo);
            }
            if (this.mUpdatedListener != null && !tmpAppInfos.isEmpty()) {
                this.mUpdatedListener.onLocalAppInfoListReceived(tmpAppInfos);
            }
        }
    }

    private void appOperation(int type, AppInfo app, int index) {
        LogUtils.i(this.LOG_TAG, this.LOG_TAG + ">>>>>>>>>>>>>>>>>> appList.size= " + this.mAppInfos.size() + " >>>>>> 前  ");
        switch (type) {
            case 0:
                this.mSqliteManager.insert(app.getAppPackageName());
                if (this.mAppInfos != null) {
                    this.mAppInfos.add(app);
                    transToNativeLocalModel(this.mAppInfos);
                }
                this.mNewPackages = this.mSqliteManager.queryAll();
                if (this.mSimpleLocalList != null) {
                    this.mSimpleLocalList.add(transToNativeLocalModel(app));
                    break;
                }
                break;
            case 1:
                if (index == -1) {
                    LogUtils.e(this.LOG_TAG, this.LOG_TAG + ">>>>>>>>>>>>>>>> AppOperation.PACKAGE_REMOVED is failure sqlite 查询不到数据");
                    break;
                }
                this.mSqliteManager.delete(((AppInfo) this.mAppInfos.get(index)).getAppPackageName());
                if (this.mAppInfos != null && index >= 0 && index < this.mAppInfos.size()) {
                    this.mAppInfos.remove(index);
                    transToNativeLocalModel(this.mAppInfos);
                    if (this.mSimpleLocalList != null) {
                        this.mSimpleLocalList.remove(index);
                        break;
                    }
                }
                break;
        }
        LogUtils.i(this.LOG_TAG, this.LOG_TAG + ">>>>>>>>>>>>>>>>>> appList.size= " + this.mAppInfos.size() + "  >>>>>> 后  ");
    }

    public void onAdd(String packageName) {
        LogUtils.d(this.LOG_TAG, "package added ---  packageName is " + packageName);
        try {
            ApplicationInfo info = this.mPackageManager.getApplicationInfo(packageName, 0);
            AppInfo appInfo = new AppInfo(info);
            appInfo.setAppName(info.loadLabel(this.mPackageManager).toString());
            update(-1, 0, appInfo);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onRemove(String packageName) {
        LogUtils.d(this.LOG_TAG, "package removed packageName is " + packageName);
        if (this.mAppInfos != null) {
            for (int i = 0; i < this.mAppInfos.size(); i++) {
                AppInfo info = (AppInfo) this.mAppInfos.get(i);
                if (packageName.equals(info.getAppPackageName())) {
                    update(i, 1, info);
                    return;
                }
            }
            return;
        }
        LogUtils.e(this.LOG_TAG, "mAppInfos is null");
    }

    public void onReplace(String packageName) {
    }
}
