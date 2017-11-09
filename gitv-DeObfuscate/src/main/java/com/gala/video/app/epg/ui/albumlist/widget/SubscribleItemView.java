package com.gala.video.app.epg.ui.albumlist.widget;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;

public class SubscribleItemView extends FavoriteHistoryItemView {
    private static final String TAG = "EPG/SubscribleItemView";

    public SubscribleItemView(Context context) {
        super(context);
    }

    public SubscribleItemView(Context context, AlbumViewType type) {
        super(context, type);
    }

    public void setCorner(IData info) {
        super.setCorner(info);
        boolean isVip = info.getCornerStatus(0);
        boolean isSingleBuy = info.getCornerStatus(1);
        boolean isCoupons = info.getCornerStatus(7);
        boolean isDubo = info.getCornerStatus(2);
        boolean isZhuanTi = info.getCornerStatus(6);
        boolean isDujia = info.getCornerStatus(3);
        boolean isSubscrible = info.getCornerStatus(8);
        LogUtils.m1568d(TAG, "content:" + getContentDescription() + "isVip:" + isVip + ",isSingleBuy:" + isSingleBuy + ",isCoupons:" + isCoupons + ",isDubo:" + isDubo + ",isZhuanTi:" + isZhuanTi + ",isDujia:" + isDujia + ",isSubscrible:" + isSubscrible);
        if (!isSubscrible) {
            clearLiveCorner();
            setCornerNoticeVisible();
        }
    }
}
