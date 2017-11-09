package com.gala.video.lib.share.ifimpl.imsg;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgOnMsgListener;
import com.push.pushservice.receiver.PushMessageReceiver;

public class IMsgReceiver extends PushMessageReceiver {
    private static final String TAG = "imsg/IMsgReceiver";
    public static IMsgOnMsgListener msgListener;

    public void onReceive(Context arg0, Intent arg1) {
        boolean isMsg = true;
        try {
            String action = arg1.getStringExtra(IMsgUtils.ACTION_FROM_APP);
            if (action != null) {
                if ("false".equals(action)) {
                    IMsgUtils.setShowDialog(false);
                } else if ("true".equals(action)) {
                    IMsgUtils.setShowDialog(true);
                } else if ("start".equals(action)) {
                    IMsgUtils.init();
                }
                isMsg = false;
                Log.d(TAG, "get action = " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isMsg) {
            super.onReceive(arg0, arg1);
        }
    }

    public void onBind(Context context, int i, int i1, String s) {
    }

    public void onUnBind(Context context, int i, int i1, String s) {
    }

    public void onMessage(Context context, int appId, String msg, long l) {
        Log.d(TAG, "IMsgUtils.sAppId=" + IMsgUtils.sAppId + ",msg= " + msg);
        IMsgContent content = new MsgDataProcessor().checkMsg(context, appId, msg);
        if (msgListener != null) {
            msgListener.onMessage(context, content);
        }
    }

    public void onMessageCallBack(Context context, int i, int i1, long l, String s) {
    }
}
