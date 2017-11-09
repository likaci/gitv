package com.gala.video.app.epg.ui.netspeed;

import com.gala.speedrunner.speedrunner.IOneAlbumProvider;
import com.gala.speedrunner.speedrunner.albumprovider.AlbumProvider;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.uikit.data.provider.NetCheckDataProvider;
import java.util.List;

public class NetSpeedUtils {
    private static String TAG = "NetSpeedUtils";

    public static IOneAlbumProvider getOneAlbumProvider(List<Album> list) {
        if (list == null || list.size() <= 0) {
            LogUtils.m1568d(TAG, "pick recommend");
            ChannelLabel label = NetCheckDataProvider.getInstance().getData();
            if (label != null) {
                return new AlbumProvider(label.getVideo());
            }
            return null;
        }
        LogUtils.m1568d(TAG, "pick history");
        return new AlbumProvider((Album) list.get(0));
    }
}
