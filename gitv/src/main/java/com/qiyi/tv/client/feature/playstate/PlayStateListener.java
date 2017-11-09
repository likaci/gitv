package com.qiyi.tv.client.feature.playstate;

import com.qiyi.tv.client.data.Media;

public interface PlayStateListener {
    void onStart(Media media);

    void onStop(Media media);
}
