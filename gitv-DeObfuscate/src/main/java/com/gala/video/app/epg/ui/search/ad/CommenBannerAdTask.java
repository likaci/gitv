package com.gala.video.app.epg.ui.search.ad;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.UrlUtils;

public class CommenBannerAdTask extends BaseBannerAdTask {
    private static String TAG = "CommenBannerAdTask";

    public CommenBannerAdTask(BaseAdUrlConfig adUrlConfigParams, IFetchBannerAdListener listener) {
        super(adUrlConfigParams, listener);
    }

    protected String getBannerUrl() {
        String url = UrlUtils.urlFormat(this.mAdUrlConfigParams.getKey(Keys.AD_URL), this.mAdUrlConfigParams.getKey("device_id"), this.mAdUrlConfigParams.getKey(Keys.TV_ID), this.mAdUrlConfigParams.getKey(Keys.V_ID), this.mAdUrlConfigParams.getKey(Keys.PLAYER_ID), this.mAdUrlConfigParams.getKey(Keys.APP_VERSION), this.mAdUrlConfigParams.getKey(Keys.RES_INDEX), this.mAdUrlConfigParams.getKey(Keys.f1948G), this.mAdUrlConfigParams.getKey(Keys.ALBUM_ID), this.mAdUrlConfigParams.getKey(Keys.CLIENT_TYPE), this.mAdUrlConfigParams.getKey(Keys.BROWSER_INFO), this.mAdUrlConfigParams.getKey("channel_id"), this.mAdUrlConfigParams.getKey(Keys.MDOEL_KEY), this.mAdUrlConfigParams.getKey(Keys.USER_AGENT), this.mAdUrlConfigParams.getKey("udid"), this.mAdUrlConfigParams.getKey(Keys.VIDEO_EVENT_ID), this.mAdUrlConfigParams.getKey(Keys.PREROLL_LIMIT), this.mAdUrlConfigParams.getKey(Keys.SCREEN_INDEX), this.mAdUrlConfigParams.getKey(Keys.SDK_VERSION), this.mAdUrlConfigParams.getKey(Keys.IS_VIP), this.mAdUrlConfigParams.getKey(Keys.AD_TYPE), this.mAdUrlConfigParams.getKey(Keys.APP_ID), this.mAdUrlConfigParams.getKey(Keys.VIDEO_DURATION), this.mAdUrlConfigParams.getKey(Keys.E_A), this.mAdUrlConfigParams.getKey(Keys.NW), this.mAdUrlConfigParams.getKey(Keys.TOTAL_VIEWED_DURATION), this.mAdUrlConfigParams.getKey(Keys.TOTAL_VIEWED_VIDEO_NUMBER), this.mAdUrlConfigParams.getKey(Keys.PASSPORT_ID), this.mAdUrlConfigParams.getKey(Keys.PASSPORT_COOKIE), this.mAdUrlConfigParams.getKey(Keys.AZT));
        LogUtils.m1568d(TAG, "getBannerUrl ---url is " + url);
        return url;
    }
}
