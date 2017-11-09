package com.gala.video.app.epg.home.utils;

import com.gala.albumprovider.model.QLayoutKind;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.HomeDebug;
import com.gala.video.app.epg.home.data.ItemData;
import com.gala.video.app.epg.ui.albumlist.data.type.ChannelLabelData;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class HomeDebugUtils {
    private static final String TAG = "home/utils/HomeDebugUtils";

    public static void printItemData(ItemData itemData, String className) {
        if (itemData != null && HomeDebug.DEBUG_LOG) {
            try {
                ChannelLabel channelLabel = itemData.mLabel;
                Album album = GetInterfaceTools.getCornerProvider().getRealAlbum(channelLabel);
                ChannelLabelData info = new ChannelLabelData(channelLabel, QLayoutKind.PORTRAIT, 0, null);
                LogUtils.i(TAG, "className = ", className);
                LogUtils.i(TAG, "itemData = ", itemData);
                LogUtils.i(TAG, "channelLabel = ", channelLabel);
                LogUtils.i(TAG, "album = ", album);
                int channelId = StringUtils.parse(info.getField(2), 0);
                LogUtils.i(TAG, "isSingleType(album) = ", Boolean.valueOf(GetInterfaceTools.getAlbumInfoHelper().isSingleType(album)), " getAlbumType(album) = ", GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album), " channelId = ", Integer.valueOf(channelId), " isVerticalType(channelId) = ", Boolean.valueOf(GetInterfaceTools.getCornerProvider().isSpecialChannel(channelId)), " desL1RBString = ", info.getText(8), " desL3String = ", info.getText(4), " info.getText(IDataConstant.TEXT_TITLE)", info.getText(3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
