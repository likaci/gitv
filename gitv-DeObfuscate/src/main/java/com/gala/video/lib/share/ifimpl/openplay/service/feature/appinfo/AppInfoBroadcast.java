package com.gala.video.lib.share.ifimpl.openplay.service.feature.appinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.MD5Util;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.project.Project;
import com.qiyi.tv.client.impl.Params.Extras;

public class AppInfoBroadcast extends BroadcastReceiver {
    private static final String API_KEY = "28A689606AB77E47CCE47A58E6630FE1";
    private static final String TAG = "AppInfoBroadcast";

    public void onReceive(Context context, Intent intent) {
        int code;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onReceive() intent=" + intent);
        }
        String packageName = null;
        String uuid = null;
        if (intent == null || intent.getAction() == null || intent.getExtras() == null || !Extras.ACTION_REQUEST_APPINFO_ACTION.equals(intent.getAction())) {
            code = 6;
        } else {
            if (API_KEY.equals(ServerParamsHelper.parseApiKey(intent.getExtras()))) {
                packageName = Project.getInstance().getBuild().getPackageName();
                uuid = MD5Util.MD5(Project.getInstance().getBuild().getVrsUUID());
                code = 0;
            } else {
                code = 2;
            }
        }
        Intent reIntent = new Intent(Extras.ACTION_RESPONSE_APPINFO_ACTION);
        Bundle result = new Bundle();
        ServerParamsHelper.setResultCode(result, code);
        ServerParamsHelper.setTvPackageName(result, packageName);
        ServerParamsHelper.setUUID(result, uuid);
        reIntent.putExtras(result);
        context.sendBroadcast(reIntent);
    }
}
