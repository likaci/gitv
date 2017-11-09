package com.gala.video.app.epg.ui.imsg.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tv.voice.core.VoiceUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.AppStartMode;
import com.gala.video.app.epg.dependency.Dependencies;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.lib.share.common.model.player.AlbumDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.project.Project;

public class DialogClickUtils {
    private static final String FROM = "msgpush";

    public static void onClick(Context context, IMsgContent... contents) {
        try {
            if (IMsgUtils.isOutApp(context)) {
                gotoMsgCenter(context, true);
            } else if (contents == null) {
            } else {
                if (contents.length == 1) {
                    PingBackCollectionFieldUtils.setIncomeSrc("others");
                    onclick(contents[0], context);
                } else if (contents.length != 0) {
                    gotoMsgCenter(context, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void onclick(IMsgContent content, Context context) {
        WebIntentParams params;
        if (content.page_jumping == 1) {
            params = new WebIntentParams();
            params.pageUrl = content.url;
            params.from = FROM;
            params.enterType = 15;
            GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, params);
        } else if (content.page_jumping == 3) {
            gotoAlbum(context, content);
        } else if (content.page_jumping == 4) {
            gotoPlay(context, content);
        } else if (content.page_jumping == 2) {
            ItemUtils.gotoSubject(context, content.related_plids, content.msg_title, FROM, "");
        } else if (content.page_jumping != 6) {
        } else {
            if (GetInterfaceTools.getIGalaAccountManager().isLogin(context)) {
                params = new WebIntentParams();
                params.from = FROM;
                params.enterType = 15;
                params.couponActivityCode = content.coupon_key;
                params.couponSignKey = content.coupon_sign;
                params.buyFrom = "15";
                params.incomesrc = "others";
                GetInterfaceTools.getWebEntry().startCouponActivity(context, params);
                return;
            }
            GetInterfaceTools.getLoginProvider().startLoginActivityForCoupon(context, content.coupon_key, content.coupon_sign, FROM, FROM, "others", 15);
        }
    }

    private static void gotoMsgCenter(Context context, boolean isOut) {
        String action;
        if (AppStartMode.IS_PLUGIN_MODE) {
            action = Project.getInstance().getPluginEnv().getPackageNameForAction(context) + Dependencies.MSG_CENTER_ACTION;
        } else if (IMsgUtils.sPkgName != null) {
            action = IMsgUtils.sPkgName + Dependencies.MSG_CENTER_ACTION;
        } else {
            action = context.getPackageName() + Dependencies.MSG_CENTER_ACTION;
        }
        Intent intent = new Intent(action);
        if (isOut) {
            intent.addFlags(32);
            intent.addFlags(VoiceUtils.INTENT_FLAG_DEFAULT);
            intent.putExtra("isFromOutside", true);
        } else {
            intent.setFlags(67108864);
        }
        context.startActivity(intent);
    }

    private static void gotoAlbum(Context context, IMsgContent content) {
        Intent intent = new Intent();
        if (!(context instanceof Activity)) {
            intent.setFlags(VoiceUtils.INTENT_FLAG_DEFAULT);
        }
        Album album = new Album();
        album.qpId = content.related_aids;
        album.tvQid = content.related_vids;
        AlbumDetailPlayParamBuilder builder = new AlbumDetailPlayParamBuilder();
        builder.setAlbumInfo(album);
        builder.setIsComplete(false);
        builder.setFrom(FROM);
        builder.setBuySource("");
        builder.setTabSource("其他");
        GetInterfaceTools.getPlayerPageProvider().startAlbumDetailPlayerPage(context, builder);
    }

    private static void gotoPlay(Context context, IMsgContent content) {
        Album albumInfo = new Album();
        albumInfo.tvQid = String.valueOf(content.related_vids);
        BasePlayParamBuilder builder = new BasePlayParamBuilder();
        PlayParams playParam = new PlayParams();
        playParam.sourceType = SourceType.OUTSIDE;
        builder.setPlayParams(playParam);
        builder.setAlbumInfo(albumInfo);
        builder.setPlayOrder(0);
        builder.setFrom(FROM);
        builder.setClearTaskFlag(false);
        builder.setBuySource("");
        builder.setTabSource("其他");
        builder.setContinueNextVideo(true);
        GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
    }
}
