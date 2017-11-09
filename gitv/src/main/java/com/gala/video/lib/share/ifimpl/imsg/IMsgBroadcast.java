package com.gala.video.lib.share.ifimpl.imsg;

import android.content.Intent;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;

class IMsgBroadcast {
    private static final String IMSG_SERVICE_ACTION = "com.push.pushservice.action.MESSAGE";

    IMsgBroadcast() {
    }

    void sendBroadcast(int type, IMsgContent content) {
        try {
            Intent intent = new Intent();
            intent.setAction("com.tv.system.imsg.action.MESSAGE");
            intent.putExtra("type", type);
            intent.putExtra("content", JSON.toJSONString(content));
            IMsgUtils.sContext.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setDiaLogFlag(boolean isShowDialog) {
        IMsgUtils.setShowDialog(isShowDialog);
        Log.d(IMsgUtils.TAG, "set isShowDialog = " + isShowDialog);
        if (isShowDialog) {
            sendBroadcast("true");
        } else {
            sendBroadcast("false");
        }
    }

    void isAppStart() {
        Log.d(IMsgUtils.TAG, "isAppStart");
        sendBroadcast("start");
    }

    void sendBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction("com.push.pushservice.action.MESSAGE");
        intent.putExtra(IMsgUtils.ACTION_FROM_APP, action);
        IMsgUtils.sContext.sendBroadcast(intent);
    }
}
