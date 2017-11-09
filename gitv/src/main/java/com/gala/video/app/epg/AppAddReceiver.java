package com.gala.video.app.epg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.gala.video.app.epg.home.promotion.local.PromotionCache;
import com.gala.video.app.epg.home.utils.PromotionUtil;
import com.gala.video.lib.framework.core.utils.NameExecutors;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;

public class AppAddReceiver extends BroadcastReceiver {
    private static final String TAG = "AppAddReceiver";

    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            final String packageName = intent.getData().getEncodedSchemeSpecificPart();
            NameExecutors.newSingleThreadExecutor(TAG).execute(new Runnable() {
                public void run() {
                    PromotionAppInfo appInfo = PromotionUtil.getPromotionAppInfo(PromotionCache.instance().getChinaPokerAppPromotion());
                    if (appInfo != null) {
                        String appPckName = appInfo.getAppPckName();
                        if (!TextUtils.isEmpty(appPckName) && !TextUtils.isEmpty(packageName) && packageName.equals(appPckName)) {
                            PromotionUtil.setAppInstalled(context, PromotionUtil.KEY_POKER_PROMOTION, true);
                        }
                    }
                }
            });
        }
    }
}
