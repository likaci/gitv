package com.gala.video.app.player.data;

import com.gala.albumprovider.model.QLayoutKind;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.lib.share.common.base.DataMakeupFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;

public class VideoDataMakeupFactory extends DataMakeupFactory {
    public static VideoDataMakeupFactory get() {
        return new VideoDataMakeupFactory();
    }

    protected IData dataMakeup(Object item, QLayoutKind layout, int locationPage, Object model) {
        if (item instanceof IVideo) {
            return dataMakeup((IVideo) item, layout);
        }
        return null;
    }

    private static IData<IVideo> dataMakeup(IVideo video, QLayoutKind layout) {
        return new VideoData(video, layout);
    }
}
