package com.gala.video.lib.share.utils;

import android.content.Context;
import android.util.Log;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.webview.utils.WebSDKConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class DevicesInfo {
    public static String getApkVer(Context context) {
        String apkVer = AppClientUtils.getClientVersion();
        Log.v("DevicesInfoJson", " apkVer = " + apkVer);
        StringBuilder sb = new StringBuilder();
        sb.append(apkVer);
        String[] apkVerStr = apkVer.split("\\.");
        if (apkVerStr.length < 4) {
            for (int i = apkVerStr.length; i < 4; i++) {
                sb.append(".0");
            }
        }
        return sb.toString();
    }

    public static String getDevicesInfoJson(Context mContext) {
        JSONObject object = new JSONObject();
        try {
            object.put("platModel", DeviceUtils.getPlatModel());
            object.put("prodModel", DeviceUtils.getProdModel());
            object.put("osVer", DeviceUtils.getOsVer());
            object.put("mem", AppRuntimeEnv.get().getTotalMemory());
            object.put("apkVer", getApkVer(mContext));
            object.put(WebSDKConstants.PARAM_KEY_MAC, DeviceUtils.getMacAddr());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Log.v("DevicesInfoJson", "no UTF8 = " + object.toString());
        return object.toString();
    }
}
