package com.qiyi.tv.client.feature.playstate;

import com.qiyi.tv.client.data.Media;

public interface OnPlayStateChangedListener {
    public static final int STATE_COMPLETED = 6;
    public static final int STATE_ERROR = 8;
    public static final int STATE_PAUSED = 5;
    public static final int STATE_PREPARED = 4;
    public static final int STATE_PREPARING = 3;
    public static final int STATE_STARTED = 1;
    public static final int STATE_STOPPED = 2;
    public static final int STATE_STOPPING = 7;

    void onPlayStateChanged(int i, Media media);
}
