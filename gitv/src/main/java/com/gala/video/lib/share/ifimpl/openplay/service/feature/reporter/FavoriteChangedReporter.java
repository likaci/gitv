package com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiManager;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Params.Extras;

public class FavoriteChangedReporter implements IFavoriteChangedReporter {
    private static final String TAG = "FavoriteChangedActionReporter";
    private Context mContext;

    public FavoriteChangedReporter(Context context) {
        this.mContext = context;
    }

    public void reportFavoriteChanged(int action, Media media) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "reportFavoriteChanged()");
        }
        if (OpenApiManager.instance().isAuthSuccess()) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "reportFavoriteChanged(" + action + ", " + media + ")");
            }
            Bundle bundle = new Bundle();
            ServerParamsHelper.setFavoriteChangedAction(bundle, action);
            ServerParamsHelper.setMedia(bundle, media);
            Intent intent = new Intent(Extras.ACTION_FAVORITE_CHANGED_ACTION);
            intent.putExtras(bundle);
            this.mContext.sendBroadcast(intent);
        }
    }
}
