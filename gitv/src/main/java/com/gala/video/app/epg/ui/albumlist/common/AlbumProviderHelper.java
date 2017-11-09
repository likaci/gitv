package com.gala.video.app.epg.ui.albumlist.common;

import android.util.Log;
import android.util.SparseArray;
import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.model.QChannel;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.video.lib.framework.core.utils.ListUtils;
import java.util.List;

public class AlbumProviderHelper {
    private static boolean sHasSetChannelList;

    public static synchronized void initAlbumProvider(List<Channel> list) {
        synchronized (AlbumProviderHelper.class) {
            Log.e("AlbumProviderHelper", "AlbumProviderHelper,initAlbumProvider, HasSetChannelList=" + sHasSetChannelList + ",list.size=" + ListUtils.getCount((List) list));
            sHasSetChannelList = true;
            AlbumProviderApi.getAlbumProvider().setChannels(list);
            AlbumProviderApi.getAlbumProvider().getProperty().setHasMyMovieTagFlag(false);
            AlbumProviderApi.registerLanguages(new USALanguages());
        }
    }

    public static boolean isHasSetChannelList() {
        return sHasSetChannelList;
    }

    public static QChannel getChannelById(int channelId) {
        SparseArray<QChannel> channels = AlbumProviderApi.getAlbumProvider().getChannels();
        if (channels != null) {
            return (QChannel) channels.get(channelId);
        }
        return null;
    }
}
