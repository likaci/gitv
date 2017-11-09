package com.gala.video.app.player.pingback.detail;

import android.content.Context;
import android.content.pm.PackageInfo;
import com.gala.pingback.IPingbackContext;
import com.gala.video.app.player.R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.IPromotionCache;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionMessage;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.tvos.appdetailpage.client.Constants;

public class DetailPingBackUtils {
    private static final String TAG = "DetailPingBackUtils";

    public static void sendEquityClickPingBack(AlbumInfo albumInfo, IPingbackContext pingbackContext) {
        if (albumInfo == null) {
            LogRecordUtils.logd(TAG, "albumInfo is null");
        } else if (pingbackContext == null) {
            LogRecordUtils.logd(TAG, "pingbackContext is null");
        } else {
            String e = pingbackContext.getItem("e").getValue();
            String block = getBlock(albumInfo);
            String rseat = getEquityImageClickRseat(albumInfo);
            String r = albumInfo.getAlbumId();
            String c1 = String.valueOf(albumInfo.getChannelId());
            String now_c1 = String.valueOf(albumInfo.getChannelId());
            String s2 = pingbackContext.getItem("s2").getValue();
            String rfr = pingbackContext.getItem("rfr").getValue();
            String now_qpid = albumInfo.getAlbumId();
            PingBackParams params = new PingBackParams();
            PingBackCollectionFieldUtils.setNow_c1(now_c1);
            PingBackCollectionFieldUtils.setNow_qpid(now_qpid);
            params.add(Keys.T, "20").add("e", e).add("rpage", "detail").add("block", block).add("rseat", rseat).add("c1", c1).add("now_c1", now_c1).add("r", r).add("s2", s2).add("rt", "i").add("rfr", rfr).add("tabid", "").add("now_qpid", now_qpid);
            LogRecordUtils.logd(TAG, "sendEquityClickPingBack(): rseat -> " + rseat + ", block -> " + block);
            PingBack.getInstance().postPingBackToLongYuan(params.build());
        }
    }

    public static String getBlock(AlbumInfo albumInfo) {
        if (albumInfo == null) {
            return "";
        }
        String block = "detail_";
        boolean isUserVip = GetInterfaceTools.getIGalaAccountManager().isVip();
        if (albumInfo.isAlbumCoupon()) {
            return block + ResourceUtil.getStr(R.string.share_detail_pingback_coupon) + "_" + ResourceUtil.getStr(R.string.share_detail_pingback_coupon_see);
        }
        if (albumInfo.isAlbumSinglePay()) {
            return block + ResourceUtil.getStr(R.string.share_detail_pingback_tvod) + "_" + ResourceUtil.getStr(R.string.share_detail_pingback_tvod_buy);
        }
        if (albumInfo.isAlbumVip()) {
            if (isUserVip) {
                return block + ResourceUtil.getStr(R.string.share_detail_pingback_vip) + "_" + ResourceUtil.getStr(R.string.share_detail_pingback_vip_renewal);
            }
            return block + ResourceUtil.getStr(R.string.share_detail_pingback_vip) + "_" + ResourceUtil.getStr(R.string.share_detail_pingback_vip_join);
        } else if (isUserVip) {
            return block + ResourceUtil.getStr(R.string.share_detail_pingback_free) + "_" + ResourceUtil.getStr(R.string.share_detail_pingback_vip_renewal);
        } else {
            return block + ResourceUtil.getStr(R.string.share_detail_pingback_free) + "_" + ResourceUtil.getStr(R.string.share_detail_pingback_vip_join);
        }
    }

    public static String getEquityImageClickRseat(AlbumInfo albumInfo) {
        if (albumInfo == null) {
            return "";
        }
        String rseat = ResourceUtil.getStr(R.string.share_detail_pingback_equity_image) + "_";
        boolean isUserVip = GetInterfaceTools.getIGalaAccountManager().isVip();
        if (albumInfo.isAlbumCoupon()) {
            if (albumInfo.isVipAuthorized() || !isUserVip) {
                return rseat + ResourceUtil.getStr(R.string.share_detail_pingback_vip_join);
            }
            return rseat + ResourceUtil.getStr(R.string.share_detail_pingback_coupon_see);
        } else if (!albumInfo.isAlbumSinglePay()) {
            return rseat + ResourceUtil.getStr(R.string.share_detail_pingback_vip_join);
        } else {
            if (albumInfo.isVipAuthorized() || !isUserVip) {
                return rseat + ResourceUtil.getStr(R.string.share_detail_pingback_vip_join);
            }
            return rseat + ResourceUtil.getStr(R.string.share_detail_pingback_tvod_buy);
        }
    }

    public static String getRecommendAppState(Context context, String key) {
        PromotionAppInfo promotionAppInfo = null;
        PromotionMessage promotionMessage = null;
        IPromotionCache promotionCache = CreateInterfaceTools.createPromotionCache();
        if (promotionCache == null) {
            LogRecordUtils.logd(TAG, "getRecommendAppState: IPromotionCache is null.");
            return "";
        }
        if (key.equals("chinapokerapp")) {
            promotionMessage = promotionCache.getChinaPokerAppPromotion();
            if (promotionMessage != null) {
                promotionAppInfo = promotionMessage.getDocument().getAppInfo();
            }
        } else if (key.equals("childapp")) {
            promotionMessage = promotionCache.getChildPromotion();
            if (promotionMessage != null) {
                promotionAppInfo = promotionMessage.getDocument().getAppInfo();
            }
        }
        if (promotionMessage != null) {
            LogRecordUtils.logd(TAG, "OnItemClick: promotionMessage -> " + promotionMessage.toString());
        }
        if (promotionAppInfo == null) {
            return "";
        }
        if (isInstalled(context, promotionAppInfo.getAppPckName())) {
            return Constants.PINGBACK_ACTION_INSTALL_DONE;
        }
        return Constants.PINGBACK_ACTION_UNINSTALL_DONE;
    }

    private static boolean isInstalled(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (Exception e) {
            LogRecordUtils.logd(TAG, "isInstalled: packageName -> " + packageName + ", this app not install.");
        }
        if (packageInfo != null) {
            return true;
        }
        return false;
    }
}
