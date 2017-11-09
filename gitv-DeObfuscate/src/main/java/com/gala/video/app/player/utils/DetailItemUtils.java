package com.gala.video.app.player.utils;

import android.content.Context;
import android.content.Intent;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.PayMarkType;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;

public class DetailItemUtils {
    private static final String TAG = "DetailItemUtils";

    public static void startPlayerFromDetailVideo(Context context, Album album, String from, String tabSource, Album oriAlbum) {
        BasePlayParamBuilder builder = new BasePlayParamBuilder();
        PlayParams playParam = new PlayParams();
        playParam.sourceType = SourceType.COMMON;
        playParam.isDetailEpisode = true;
        builder.setPlayParams(playParam);
        builder.setAlbumInfo(album);
        builder.setFrom(from);
        builder.setTabSource(tabSource);
        builder.setDetailOriAlbum(oriAlbum);
        GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
    }

    public static void startBuyPage(Context context, Intent intent, IVideo video, int buyVip, int enterType) {
        if (video == null) {
            LogRecordUtils.logd(TAG, "startBuyPage, video is null.");
            return;
        }
        String buySource = intent.getStringExtra("buy_source");
        String eventId = intent.getStringExtra("eventId");
        String from = intent.getStringExtra("from");
        String buyFrom = "";
        WebIntentParams params = new WebIntentParams();
        params.pageType = 3;
        params.enterType = enterType;
        params.from = from;
        params.buySource = buySource;
        params.albumInfo = video.getAlbum();
        params.requestCode = 1001;
        params.eventId = eventId;
        params.buyVip = buyVip;
        params.state = getState(video);
        params.incomesrc = PingBackCollectionFieldUtils.getIncomeSrc();
        if (enterType == 4) {
            buyFrom = "detail_buy";
        } else if (enterType == 21) {
            buyFrom = WebConstants.RFR_DETAIL_EQUITY;
        }
        params.buyFrom = buyFrom;
        LogRecordUtils.logd(TAG, "onBuyAlbumClicked params.from=" + params.from + ", buyFrom -> " + buyFrom + ", params.incomesrc = " + params.incomesrc);
        GetInterfaceTools.getWebEntry().startPurchasePage(context, params);
    }

    private static String getState(IVideo video) {
        if (video.isAlbumCoupon()) {
            return parseInt(video.getCouponCount()) > 0 ? "vod" : "vodalbum";
        } else {
            if (video.getAlbum().getPayMarkType() == PayMarkType.PAY_ON_DEMAND_MARK) {
                return "album";
            }
            if (video.isAlbumVip()) {
                return "vip";
            }
            return "free";
        }
    }

    private static int parseInt(String s) {
        if (!StringUtils.isEmpty((CharSequence) s)) {
            try {
                return Integer.valueOf(s).intValue();
            } catch (Exception e) {
                LogUtils.m1568d(TAG, "parseInt(): error" + s);
            }
        }
        return 0;
    }
}
