package com.gala.video.lib.share.ifimpl.openplay.broadcast.utils;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.stub.MainfestConstants;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;
import org.json.JSONException;
import org.json.JSONObject;

public class OpenPlayIntentUtils {
    public static JSONObject parsePlayInfo(Bundle bundle) {
        if (!(bundle == null || bundle.isEmpty())) {
            String info = bundle.getString("playInfo");
            if (info != null) {
                LogUtils.d(OpenPlayIntentUtils.class.getName(), "parsePlayInfo: " + info);
                try {
                    return new JSONObject(info);
                } catch (JSONException e) {
                    LogUtils.e(OpenPlayIntentUtils.class.getName(), "[UNKNOWN-EXCEPTION] [reason: can not parse the playInfo field to json.][playInfo:]" + info.toString() + "[JSONException:" + e.getMessage() + AlbumEnterFactory.SIGN_STR);
                    e.printStackTrace();
                    return null;
                }
            }
        }
        LogUtils.e(OpenPlayIntentUtils.class.getName(), "[INVALID-PARAMTER] [reason:missing field--playInfo] ");
        return null;
    }

    public static String splitAction(Context context, String action) {
        if (context == null || TextUtils.isEmpty(action)) {
            return "";
        }
        if (action.substring(action.lastIndexOf(".") + 1).equals(MainfestConstants.RCV_POSTFIX)) {
            action = action.substring(0, action.lastIndexOf("."));
        }
        return action.replace(Project.getInstance().getPluginEnv().getPackageNameForAction(context) + ".action.", "");
    }
}
