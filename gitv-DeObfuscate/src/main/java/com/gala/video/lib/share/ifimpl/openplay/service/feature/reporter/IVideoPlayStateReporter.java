package com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter;

import com.qiyi.tv.client.data.Media;

public interface IVideoPlayStateReporter {
    void reportVideoState(int i, Media media);
}
