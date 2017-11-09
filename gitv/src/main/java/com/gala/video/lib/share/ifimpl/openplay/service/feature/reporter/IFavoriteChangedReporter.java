package com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter;

import com.qiyi.tv.client.data.Media;

public interface IFavoriteChangedReporter {
    void reportFavoriteChanged(int i, Media media);
}
