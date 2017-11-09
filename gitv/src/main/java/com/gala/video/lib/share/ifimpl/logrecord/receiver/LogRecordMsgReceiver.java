package com.gala.video.lib.share.ifimpl.logrecord.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.gala.report.msghandler.MsgHanderEnum.HOSTMODULE;
import com.gala.report.msghandler.MsgHanderEnum.HOSTSTATUS;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class LogRecordMsgReceiver extends BroadcastReceiver {
    private final String TAG = "LogRecordMsgReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.v("LogRecordMsgReceiver", "onReceive");
        String msg = intent.getStringExtra("content");
        int type = intent.getIntExtra("type", 0);
        Log.v("LogRecordMsgReceiver", "msg = " + msg);
        Log.v("LogRecordMsgReceiver", "type = " + type);
        if (type != 61) {
            Log.v("LogRecordMsgReceiver", "type = 61 is logtype , type = " + type);
        } else if (!LogRecordUtils.isLogRecordInit()) {
            Log.v("LogRecordMsgReceiver", "isLogRecordInit() =  false");
            LogRecordUtils.setMsg(msg);
        } else if (GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore() != null) {
            GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore().sendPushMessage(msg);
            GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore().sendHostStatus(HOSTMODULE.LOGMSGPUSH, HOSTSTATUS.END);
        }
    }
}
