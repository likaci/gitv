package com.tvos.apps.utils.sys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import com.nvidia.market.update.MarketUpdateHelper;
import com.push.mqttv3.internal.ClientDefaults;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class NVMarketProxy {
    private static final String BUNDLE_EXTRA_DETAILAPP_APPID = "appid";
    private static final int SCENE_ID_DETAIL = 1;
    private static final String TAG = NVMarketProxy.class.getSimpleName();
    private static Context mContext;
    private static Map<String, MarketUpdateHelper> mMarketUpdateHelperMap = new HashMap();

    public static void init(Context con) {
        mContext = con.getApplicationContext();
    }

    private static MarketUpdateHelper getHelper(String appName, String pkgName, boolean isGame, String iconUri, long appId, boolean update) {
        if (update) {
            pkgName = new StringBuilder(String.valueOf(pkgName)).append(".update").toString();
        }
        Log.d(TAG, "getHelper, appName = " + appName + " , pkgName = " + pkgName + " , isGame = " + isGame + " , iconUri = " + iconUri + " , appId = " + appId + " , update = " + update);
        if (mMarketUpdateHelperMap.containsKey(pkgName)) {
            return (MarketUpdateHelper) mMarketUpdateHelperMap.get(pkgName);
        }
        Log.d(TAG, "There is no helper, appName is " + appName + " , pkgName is " + pkgName);
        MarketUpdateHelper helper = new MarketUpdateHelper(mContext, pkgName, appName, iconUri, isGame, isGame ? getGameDetailPageIntent(appId) : getAppDetailPageIntent(appId));
        if (update) {
            helper.queueApp("update", true);
        } else {
            helper.queueApp("user_init", true);
        }
        mMarketUpdateHelperMap.put(pkgName, helper);
        return helper;
    }

    private static Intent getGameDetailPageIntent(long gameId) {
        Exception e;
        Intent intent = null;
        try {
            Intent intent2 = new Intent(mContext, Class.forName("org.cocos2dx.lua.AppActivity"));
            try {
                JSONObject obj = new JSONObject();
                obj.put("appId", gameId);
                obj.put("sceneId", 1);
                obj.put("from", "34");
                intent2.addFlags(ClientDefaults.MAX_MSG_SIZE);
                intent2.putExtra("data", obj.toString());
                intent = intent2;
            } catch (Exception e2) {
                e = e2;
                intent = intent2;
                e.printStackTrace();
                Log.d(TAG, "getGameDetailPageIntent, intent is " + intent);
                return intent;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            Log.d(TAG, "getGameDetailPageIntent, intent is " + intent);
            return intent;
        }
        Log.d(TAG, "getGameDetailPageIntent, intent is " + intent);
        return intent;
    }

    private static Intent getAppDetailPageIntent(long appId) {
        Exception e;
        Intent intent = null;
        try {
            Intent intent2 = new Intent(mContext, Class.forName("com.gitv.tvappstore.ui.detail.AppDetailActivity"));
            try {
                intent2.addFlags(ClientDefaults.MAX_MSG_SIZE);
                intent2.addFlags(32768);
                intent2.putExtra("appid", String.valueOf(appId));
                intent2.putExtra("from", "34");
                intent = intent2;
            } catch (Exception e2) {
                e = e2;
                intent = intent2;
                e.printStackTrace();
                Log.d(TAG, "getAppDetailPageIntent, intent is " + intent);
                return intent;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            Log.d(TAG, "getAppDetailPageIntent, intent is " + intent);
            return intent;
        }
        Log.d(TAG, "getAppDetailPageIntent, intent is " + intent);
        return intent;
    }

    public static void updateAppDownloadProgress(String appName, String pkgName, int progress, boolean isGame, String iconUri, long appId, boolean update) {
        Log.d(TAG, "update app download progress, appName is " + appName + " , pkgName is " + pkgName + " , progress is " + progress + " , isGame = " + isGame + " , iconUri = " + iconUri + " , appId = " + appId + " , update = " + update);
        if (ChildrenModeUtils.isGameRestricted(pkgName)) {
            stopUpdateAppStatus(pkgName, false, update);
            return;
        }
        if (update) {
            disableAppOnLauncher(pkgName);
        }
        getHelper(appName, pkgName, isGame, iconUri, appId, update).downloadApp(progress);
    }

    public static void updateAppStatusStartInstall(String appName, String pkgName, boolean isGame, String iconUri, long appId, boolean update) {
        Log.d(TAG, "update app status start install, appName is " + appName + " , pkgName is " + pkgName + " , isGame = " + isGame + " , iconUri = " + iconUri + " , appId = " + appId + " , update = " + update);
        if (ChildrenModeUtils.isGameRestricted(pkgName)) {
            stopUpdateAppStatus(pkgName, false, update);
            return;
        }
        if (update) {
            disableAppOnLauncher(pkgName);
        }
        getHelper(appName, pkgName, isGame, iconUri, appId, update).installApp();
    }

    public static void stopUpdateAppStatus(String pkgName, boolean installed, boolean update) {
        if (update) {
            enableAppOnLauncher(pkgName);
            pkgName = new StringBuilder(String.valueOf(pkgName)).append(".update").toString();
        }
        Log.d(TAG, "update app status start install, pkgName is " + pkgName + " , installed is " + installed + " , update = " + update);
        MarketUpdateHelper helper = (MarketUpdateHelper) mMarketUpdateHelperMap.get(pkgName);
        if (helper != null) {
            if (update) {
                helper.dequeueApp(false);
            } else {
                helper.dequeueApp(installed);
            }
            mMarketUpdateHelperMap.remove(pkgName);
        }
    }

    private static void disableAppOnLauncher(String pkgName) {
        try {
            PackageManager pm = mContext.getPackageManager();
            int currentStatus = pm.getApplicationEnabledSetting(pkgName);
            Log.d(TAG, "disableAppOnLauncher, pkgName = " + pkgName + " , currentStatus = " + currentStatus);
            if (currentStatus != 2) {
                pm.setApplicationEnabledSetting(pkgName, 2, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void enableAppOnLauncher(String pkgName) {
        try {
            PackageManager pm = mContext.getPackageManager();
            int currentStatus = pm.getApplicationEnabledSetting(pkgName);
            Log.d(TAG, "enableAppOnLauncher, pkgName = " + pkgName + " , currentStatus = " + currentStatus);
            if (currentStatus != 1) {
                pm.setApplicationEnabledSetting(pkgName, 1, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
