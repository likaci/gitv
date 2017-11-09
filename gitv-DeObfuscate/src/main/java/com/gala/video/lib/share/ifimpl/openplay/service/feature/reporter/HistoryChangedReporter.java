package com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiManager;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Params.Extras;

public class HistoryChangedReporter implements IHistoryChangedReporter {
    private static final String TAG = "HistoryChangedActionReporter";
    private Context mContext;

    public HistoryChangedReporter(Context context) {
        this.mContext = context;
    }

    public void reportHistoryChanged(int action, Media media) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "reportHistoryChanged()");
        }
        if (OpenApiManager.instance().isAuthSuccess()) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "reportHistoryChanged(" + action + ", " + media + ")");
            }
            Bundle bundle = new Bundle();
            ServerParamsHelper.setHistoryChangedAction(bundle, action);
            ServerParamsHelper.setMedia(bundle, media);
            Intent intent = new Intent(Extras.ACTION_HISTORY_CHANGED_ACTION);
            intent.putExtras(bundle);
            this.mContext.sendBroadcast(intent);
        }
    }
}
