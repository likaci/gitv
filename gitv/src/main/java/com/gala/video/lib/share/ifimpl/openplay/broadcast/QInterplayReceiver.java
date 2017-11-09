package com.gala.video.lib.share.ifimpl.openplay.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.report.msghandler.MsgHanderEnum.HOSTMODULE;
import com.gala.report.msghandler.MsgHanderEnum.HOSTSTATUS;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.activity.LoadingActivity;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.utils.OpenPlayIntentUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.utils.PageIOUtils;

public class QInterplayReceiver extends BroadcastReceiver {
    private static long mLastProcessTime = -1;
    private final String TAG = "openplay/broadcast/QInterplayReceiver";

    public void onReceive(Context context, Intent intent) {
        if (isProcess()) {
            String action = intent != null ? intent.getAction() : "";
            LogUtils.d("openplay/broadcast/QInterplayReceiver", "onReceive action : " + action);
            String actionSuffix = OpenPlayIntentUtils.splitAction(context, action);
            if (BroadcastManager.instance().findBroadcastActionByKey(actionSuffix) == null) {
                LogUtils.e("openplay/broadcast/QInterplayReceiver", "[NOT-AUTHORIZED] [action : " + actionSuffix + "] [supportList : " + BroadcastManager.instance().getSupportActionList() + AlbumEnterFactory.SIGN_STR);
                if (GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore() != null) {
                    GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore().sendHostStatus(HOSTMODULE.BROADCAST, HOSTSTATUS.FAIL);
                    return;
                }
                return;
            }
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                LogUtils.e("openplay/broadcast/QInterplayReceiver", "[INVALID-PARAMTER] [reason:missing playInfo bundle] [the bundle of the intent is null.]");
                return;
            }
            bundle.putBoolean("isFromBroadcast", true);
            bundle.putLong("startTime", System.currentTimeMillis());
            intent.putExtras(bundle);
            intent.setClass(context, LoadingActivity.class);
            intent.addFlags(268468224);
            PageIOUtils.activityIn(context, intent);
        }
    }

    private boolean isProcess() {
        boolean isprocess = true;
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - mLastProcessTime;
        if (diff < 1000) {
            LogUtils.w("openplay/broadcast/QInterplayReceiver", "Operation is too frequent, please try again later. return. diff= " + diff);
            isprocess = false;
        }
        mLastProcessTime = currentTime;
        return isprocess;
    }
}
