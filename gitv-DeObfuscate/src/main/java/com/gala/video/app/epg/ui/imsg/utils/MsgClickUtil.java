package com.gala.video.app.epg.ui.imsg.utils;

import android.content.Context;
import android.content.Intent;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.ui.imsg.MsgCenterDetailActivity;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.model.player.AlbumDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.utils.PageIOUtils;

public class MsgClickUtil {
    private static final String LOG_TAG = "MsgClickUtil";
    public static final String MSG_CONTENT = "content";
    public static String mBuySource = "";
    public static int mEnterType = 14;
    public static String mFrom = "msg";

    public static void jumpTo(Context context, IMsgContent content) {
        jumpTo(context, content, false);
    }

    public static void jumpTo(Context context, IMsgContent content, boolean fromMsgDetailPage) {
        LogUtils.m1576i(LOG_TAG, "jumpTo --- content = ", content, " fromMsgDetailPage = ", Boolean.valueOf(fromMsgDetailPage));
        if (context == null || content == null) {
            LogUtils.m1571e(LOG_TAG, "jumpTo -- context is null or content is null");
        } else if (content.msg_template_id != 1 && content.msg_template_id != 2) {
            LogUtils.m1571e(LOG_TAG, "jumpTo -- msg_template_id is not support!!");
        } else if (content.isHasDetail() && !fromMsgDetailPage) {
            Intent intent = new Intent(context, MsgCenterDetailActivity.class);
            intent.putExtra("content", content);
            PageIOUtils.activityIn(context, intent);
        } else if (content.page_jumping == 1) {
            params = new WebIntentParams();
            params.pageUrl = content.url;
            params.from = mFrom;
            params.enterType = mEnterType;
            params.buyFrom = "msg";
            GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, params);
        } else if (content.page_jumping != 6) {
            Album album = getConvertAlbum(content);
            String tabSrc = PingBackUtils.getTabSrc();
            if (content.page_jumping == 3) {
                AlbumDetailPlayParamBuilder builder = new AlbumDetailPlayParamBuilder();
                builder.setAlbumInfo(album);
                builder.setFrom(mFrom);
                builder.setBuySource(mBuySource);
                builder.setTabSource(tabSrc);
                builder.setIsComplete(false);
                GetInterfaceTools.getPlayerPageProvider().startAlbumDetailPlayerPage(context, builder);
            } else if (content.page_jumping == 4) {
                BasePlayParamBuilder builder2 = new BasePlayParamBuilder();
                PlayParams playParam = new PlayParams();
                playParam.sourceType = SourceType.OUTSIDE;
                builder2.setPlayParams(playParam);
                builder2.setAlbumInfo(album);
                builder2.setPlayOrder(album.order);
                builder2.setFrom(mFrom);
                builder2.setTabSource(tabSrc);
                builder2.setBuySource(mBuySource);
                GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder2);
            } else if (content.page_jumping == 2) {
                ItemUtils.gotoSubject(context, content.related_plids, content.msg_title, mFrom, mBuySource);
            } else if (content.page_jumping != 5) {
            }
        } else if (GetInterfaceTools.getIGalaAccountManager().isLogin(context)) {
            params = new WebIntentParams();
            params.from = mFrom;
            params.enterType = mEnterType;
            params.buyFrom = "msg";
            params.couponActivityCode = content.coupon_key;
            params.couponSignKey = content.coupon_sign;
            params.incomesrc = PingBackCollectionFieldUtils.getIncomeSrc();
            GetInterfaceTools.getWebEntry().startCouponActivity(context, params);
        } else {
            GetInterfaceTools.getLoginProvider().startLoginActivityForCoupon(context, content.coupon_key, content.coupon_sign, mFrom, "msg", PingBackCollectionFieldUtils.getIncomeSrc(), mEnterType);
        }
    }

    private static Album getConvertAlbum(IMsgContent content) {
        Album album = new Album();
        album.qpId = content.related_aids;
        album.tvQid = content.related_vids;
        album.type = content.tv_type;
        album.isSeries = content.isSeries ? 1 : 0;
        album.sourceCode = content.sourceCode;
        album.chnId = content.channelId;
        return album;
    }
}
