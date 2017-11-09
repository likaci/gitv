package com.gala.video.app.epg.ui.solotab;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.utils.PageIOUtils;

public class SoloTabEnterUtils {
    private static final String TAG = "SoloTabEnterUtils";

    public static void start(Context context, TabModel tabModel, String tabSrc, String from) {
        Log.e(TAG, "start solotab activity,context=" + context + ", tabModel=" + tabModel + ",tabSrc=" + tabSrc + "," + "from=" + from);
        if (tabModel != null && context != null) {
            start(context, tabModel.getResourceGroupId(), tabModel.getTitle(), tabModel.isVipTab(), tabModel.getChannelId(), tabSrc, from);
        }
    }

    public static void start(Context context, String sourceId, String tabName, boolean isVip, int channelId, String tabSrc, String from) {
        Log.e(TAG, "start solotab activity, sourceId=" + sourceId);
        Intent intent = new Intent(context, SoloTabActivity.class);
        SoloTabInfoModel infoModel = new SoloTabInfoModel();
        infoModel.setSourceId(sourceId);
        infoModel.setVip(isVip);
        infoModel.setChannelId(channelId);
        infoModel.setTabName(tabName);
        infoModel.setFrom(from);
        infoModel.setTabSrc(tabSrc);
        intent.putExtra("intent_model", infoModel);
        intent.addFlags(67108864);
        PageIOUtils.activityIn(context, intent);
    }
}
