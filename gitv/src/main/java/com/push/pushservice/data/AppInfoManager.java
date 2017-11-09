package com.push.pushservice.data;

import android.content.Context;
import android.text.TextUtils;
import com.push.pushservice.constants.DataConst;
import com.push.pushservice.sharepreference.PushPrefUtils;
import com.push.pushservice.utils.LogUtils;
import com.push.pushservice.utils.PushUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppInfoManager {
    public static final String TAG = "AppInfoManager";
    private static AppInfoManager instance = null;
    private static Context mContext = null;
    private AppListInfo mInfoList = null;

    public AppInfoManager(Context context) {
        mContext = context;
        if (mContext != null) {
        }
    }

    public static AppInfoManager getInstance(Context context) {
        if (instance == null) {
            instance = new AppInfoManager(context);
        }
        return instance;
    }

    public void saveInfo(Context context, AppListInfo info) {
        if (info != null && context != null) {
            try {
                String jsonStr = encodeJson(info);
                if (!TextUtils.isEmpty(jsonStr)) {
                    String strMd5 = PushUtils.encodeMD5(jsonStr);
                    if (!TextUtils.isEmpty(strMd5)) {
                        PushPrefUtils.setAppInfoCheck(context, strMd5);
                        PushPrefUtils.setAppInfoList(context, jsonStr);
                    }
                }
            } catch (Exception e) {
                LogUtils.loge(TAG, "saveInfo Exception : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public AppListInfo getInfo(Context context) {
        try {
            String strMd5 = PushPrefUtils.getAppInfoCheck(context);
            String strAppList = PushPrefUtils.getAppInfoList(context);
            if (!(TextUtils.isEmpty(strMd5) || TextUtils.isEmpty(strAppList))) {
                if (TextUtils.equals(PushUtils.encodeMD5(strAppList), strMd5)) {
                    LogUtils.logd(TAG, "getInfo  md5 check success: " + strAppList);
                    this.mInfoList = parseJson(strAppList);
                    LogUtils.logd(TAG, "getInfo parse json : " + this.mInfoList);
                } else {
                    LogUtils.loge(TAG, "getInfo parse: md5 check fails");
                }
            }
        } catch (Exception e) {
            LogUtils.loge(TAG, "getInfo Exception : " + e.getMessage());
            e.printStackTrace();
        }
        if (this.mInfoList == null) {
            this.mInfoList = new AppListInfo();
        }
        return this.mInfoList;
    }

    private AppListInfo parseJson(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        try {
            int idx;
            AppListInfo listInfo = new AppListInfo();
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray jsonHostList = jsonObj.getJSONArray(DataConst.APP_INFO_HOST_LIST);
            List<String> hostList = new ArrayList();
            for (idx = 0; idx < jsonHostList.length(); idx++) {
                hostList.add(jsonHostList.getString(idx));
            }
            listInfo.setHostList(hostList);
            JSONArray jsonAppList = jsonObj.getJSONArray("app_list");
            List<AppInfo> appInfoList = new ArrayList();
            for (idx = 0; idx < jsonAppList.length(); idx++) {
                JSONObject app = jsonAppList.getJSONObject(idx);
                appInfoList.add(new AppInfo((short) app.getInt(DataConst.APP_INFO_APP_ID), app.getString("device_id"), app.getString(DataConst.APP_INFO_PKG_NAME), app.getString(DataConst.APP_INFO_APP_VER), app.getBoolean(DataConst.APP_INFO_REGISTER)));
            }
            listInfo.setAppList(appInfoList);
            return listInfo;
        } catch (JSONException e) {
            LogUtils.logd(TAG, "parseJson JSONException e = " + e);
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            LogUtils.logd(TAG, "parseJson Exception e = " + e2);
            e2.printStackTrace();
            return null;
        }
    }

    private String encodeJson(AppListInfo info) {
        String str = null;
        if (info != null) {
            JSONObject jsonObj = new JSONObject();
            JSONArray appList = new JSONArray();
            JSONArray hostList = new JSONArray();
            try {
                for (String host : info.getHostList()) {
                    hostList.put(host);
                }
                jsonObj.put(DataConst.APP_INFO_HOST_LIST, hostList);
                for (AppInfo app : info.getAppList()) {
                    JSONObject appInfo = new JSONObject();
                    appInfo.put(DataConst.APP_INFO_APP_ID, app.getAppid());
                    appInfo.put("device_id", app.getDeviceId());
                    appInfo.put(DataConst.APP_INFO_PKG_NAME, app.getPackageName());
                    appInfo.put(DataConst.APP_INFO_APP_VER, app.getAppVer());
                    appInfo.put(DataConst.APP_INFO_REGISTER, app.getIsRegister());
                    appList.put(appInfo);
                }
                jsonObj.put("app_list", appList);
                str = jsonObj.toString();
            } catch (JSONException e) {
                LogUtils.logd(TAG, "encodeJson JSONException e = " + e);
                e.printStackTrace();
            } catch (Exception e2) {
                LogUtils.logd(TAG, "encodeJson Exception e = " + e2);
                e2.printStackTrace();
            }
        }
        return str;
    }
}
