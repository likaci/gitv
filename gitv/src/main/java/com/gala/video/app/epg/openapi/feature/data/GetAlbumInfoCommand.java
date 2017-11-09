package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.data.Video;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;

public class GetAlbumInfoCommand extends BaseGetMediaInfoCommand {
    public GetAlbumInfoCommand(Context context) {
        super(context, TargetType.TARGET_MEDIA, 20003, DataType.DATA_MEDIA);
    }

    protected String getId(Media media) {
        if (!(media instanceof Video)) {
            return null;
        }
        Video video = (Video) media;
        if (video.isSeries()) {
            return video.getAlbumId();
        }
        return null;
    }
}
