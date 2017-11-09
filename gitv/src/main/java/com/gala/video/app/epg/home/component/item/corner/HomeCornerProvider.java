package com.gala.video.app.epg.home.component.item.corner;

import android.text.TextUtils;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.ItemData;
import com.gala.video.app.epg.ui.albumlist.data.type.ChannelLabelData;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.AlbumKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.ICornerProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetType;
import com.gala.video.lib.share.utils.ResourceUtil;

public class HomeCornerProvider {
    private static final String TAG = "HomeCornerProvider";

    public static void handleItemCorner(ItemData itemData, ItemModel itemModel) {
        if (itemData != null && itemModel != null) {
            int widgetType = itemModel.getWidgetType();
            if (widgetType == WidgetType.ITEM_TITLE_IN || widgetType == WidgetType.ITEM_TITLE_OUT || widgetType == 270) {
                ChannelLabel channelLabel = marketTitleItemCorner(itemData, itemModel);
                Album album = GetInterfaceTools.getCornerProvider().getRealAlbum(channelLabel);
                itemData.rankedNum = itemModel.getRank();
                boolean z = itemData.rankedNum > 0 && itemData.rankedNum < 11;
                itemData.isRanked = z;
                if ((channelLabel == null || !ResourceType.COLLECTION.equals(channelLabel.getType())) && widgetType != 270) {
                    itemData.isToBeOnline = itemModel.isToBeOnline();
                    marketTitleItemDesc(itemData, itemModel, channelLabel, album);
                }
            }
        }
    }

    public static String getCornerDesc(ChannelLabel mLabel) {
        Album album = GetInterfaceTools.getCornerProvider().getRealAlbum(mLabel);
        AlbumKind albumType = GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album);
        String str = "";
        if (albumType.equals(AlbumKind.SERIES_ALBUM)) {
            if (album.tvsets != album.tvCount && album.tvCount != 0) {
                return ResourceUtil.getStr(R.string.album_item_tvcount, Integer.valueOf(album.tvCount));
            } else if (album.tvsets != album.tvCount || album.tvsets == 0) {
                return str;
            } else {
                return ResourceUtil.getStr(R.string.album_item_tvset, Integer.valueOf(album.tvsets));
            }
        } else if (!albumType.equals(AlbumKind.SOURCE_ALBUM) && !albumType.equals(AlbumKind.SIGLE_SERIES)) {
            return str;
        } else {
            if (StringUtils.isEmpty(getConerDateShort(album))) {
                return str;
            }
            return ResourceUtil.getStr(R.string.album_item_update, format);
        }
    }

    private static String getConerDateShort(Album album) {
        String date = album.time;
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        date = date.trim();
        if (TextUtils.isEmpty(date) || date.length() <= 4) {
            LogUtils.e(TAG, "getConerDateShort --- date.length() <= 4");
            LogUtils.e(TAG, "getConerDateShort --- album = ", album);
            return "";
        }
        StringBuilder format = new StringBuilder(5);
        char[] dateArr = date.substring(4, date.length()).toCharArray();
        String mark = "-";
        int length = dateArr.length;
        for (int i = 0; i < length; i++) {
            format.append(dateArr[i]);
            if (i == 1) {
                format.append("-");
            }
        }
        if (format.length() < 5) {
            return "";
        }
        return format.toString();
    }

    public static float getScore(Album album) {
        float f = -1.0f;
        if (album != null) {
            String score = album.score;
            if (!(TextUtils.isEmpty(score) || "0.0".equals(score))) {
                try {
                    f = Float.parseFloat(score);
                } catch (Exception e) {
                }
            }
        }
        return f;
    }

    private static ChannelLabel marketTitleItemCorner(ItemData itemData, ItemModel itemModel) {
        if (itemData == null || itemModel == null) {
            return null;
        }
        ChannelLabel channelLabel = itemModel.getData();
        ICornerProvider provider = GetInterfaceTools.getCornerProvider();
        itemData.isVip = provider.getCornerInfo(channelLabel, 0);
        itemData.isCharge = provider.getCornerInfo(channelLabel, 1);
        itemData.isCoupons = provider.getCornerInfo(channelLabel, 7);
        itemData.isDuJia = provider.getCornerInfo(channelLabel, 3);
        itemData.isDuBo = provider.getCornerInfo(channelLabel, 2);
        itemData.isSubject = provider.getCornerInfo(channelLabel, 6);
        return channelLabel;
    }

    private static void marketTitleItemDesc(ItemData itemData, ItemModel itemModel, ChannelLabel channelLabel, Album album) {
        if (itemData == null || itemModel == null) {
            LogUtils.i(TAG, "marketTitleItemDesc --- itemData == null || itemModel == null");
            return;
        }
        ChannelLabelData info = new ChannelLabelData(channelLabel, QLayoutKind.PORTRAIT, 0, null);
        IAlbumInfoHelper helper = GetInterfaceTools.getAlbumInfoHelper();
        if (helper.getAlbumType(album) == AlbumKind.SIGLE_VIDEO && itemModel.getWidgetType() == WidgetType.ITEM_TITLE_OUT) {
            int channelId = StringUtils.parse(info.getField(2), 0);
            ICornerProvider provider = GetInterfaceTools.getCornerProvider();
            if (provider.isSpecialChannel(channelId)) {
                float score = getScore(album);
                if (score > 0.0f && score <= 10.0f) {
                    itemData.isShowScore = true;
                    itemData.score = String.valueOf(score);
                }
                itemData.is3D = provider.getCornerInfo(channelLabel, 5);
                itemData.isDolby = provider.getCornerInfo(channelLabel, 4);
            }
        }
        if (helper.getAlbumType(album) == AlbumKind.SIGLE_SERIES || GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album) == AlbumKind.SOURCE_ALBUM) {
            itemData.isShowRBDes1 = true;
            itemData.desL1RBString = info.getText(8);
        }
        if (helper.getAlbumType(album) == AlbumKind.SERIES_ALBUM) {
            itemData.isShowRBDes1 = true;
            itemData.desL1RBString = info.getText(8);
        }
        if (helper.getAlbumType(album) == AlbumKind.SIGLE_UNIT) {
            itemData.isShowRBDes1 = true;
            itemData.desL1RBString = info.getText(11);
        }
    }
}
