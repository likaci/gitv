package com.push.pushservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.push.pushservice.constants.DataConst;
import com.push.pushservice.utils.LogUtils;

public abstract class PushMessageReceiver extends BroadcastReceiver {
    public static final String TAG = "PushMessageReceiver";

    public abstract void onBind(Context context, int i, int i2, String str);

    public abstract void onMessage(Context context, int i, String str, long j);

    public abstract void onMessageCallBack(Context context, int i, int i2, long j, String str);

    public abstract void onUnBind(Context context, int i, int i2, String str);

    public void onReceive(Context arg0, Intent arg1) {
        if (arg1 != null) {
            String action = arg1.getAction();
            Bundle bundle = arg1.getExtras();
            LogUtils.logd(TAG, "onReceive aciton =" + action);
            LogUtils.logd(TAG, "onReceive bundle =" + bundle);
            if (bundle != null && !TextUtils.isEmpty(action)) {
                String msg;
                int appid;
                if (action.startsWith("com.push.pushservice.action.MESSAGE")) {
                    try {
                        msg = bundle.getString(DataConst.EXTRA_PUSH_MESSAGE);
                        appid = bundle.getInt("appid", -1);
                        long msgID = bundle.getLong(DataConst.EXTRA_PUSH_MESSAGE_ID);
                        LogUtils.logd(TAG, "onReceive appId =" + appid + " msg = " + msg);
                        onMessage(arg0, appid, msg, msgID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (action.startsWith("com.push.pushservice.action.RECEIVE")) {
                    try {
                        int type = bundle.getInt(DataConst.EXTRA_ERROR_TYPE);
                        appid = bundle.getInt("appid");
                        int errorCode = bundle.getInt(DataConst.EXTRA_ERROR_CODE);
                        msg = bundle.getString(DataConst.EXTRA_ERROR_MSG);
                        if (type == 19001) {
                            onBind(arg0, appid, errorCode, msg);
                        } else if (type == 1902) {
                            onUnBind(arg0, appid, errorCode, msg);
                        } else if (type == 1903) {
                            Context context = arg0;
                            int i = appid;
                            onMessageCallBack(context, i, errorCode, bundle.getLong(DataConst.EXTRA_PUSH_MESSAGE_ID), msg);
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }
}
