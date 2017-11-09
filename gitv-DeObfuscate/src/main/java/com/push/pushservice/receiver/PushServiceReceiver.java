package com.push.pushservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.push.pushservice.api.PushService;
import com.push.pushservice.sharepreference.PushPrefUtils;
import com.push.pushservice.utils.LogUtils;

public class PushServiceReceiver extends BroadcastReceiver {
    private static final String TAG = "PushServiceReceiver";

    public void onReceive(Context context, Intent arg1) {
        if (arg1 != null) {
            String action = arg1.getAction();
            if (!TextUtils.isEmpty(action)) {
                int appId = PushPrefUtils.getAppId(context);
                LogUtils.logd(TAG, "onReceive appId = " + appId + " action = " + action);
                if (appId > 0) {
                    try {
                        Intent mIntent = new Intent(PushService.class.getName());
                        mIntent.setPackage(context.getPackageName());
                        context.startService(mIntent);
                    } catch (SecurityException e) {
                        LogUtils.logd(TAG, "onReceive SecurityException");
                    } catch (Exception e2) {
                        LogUtils.logd(TAG, "onReceive Exception e");
                    }
                }
            }
        }
    }
}
