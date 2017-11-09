package com.gala.sdk.player.data.job;

import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.utils.job.Job;

public abstract class VideoJob extends Job<IVideo> {
    protected final int PLAYLIST_SIZE = 21;

    public VideoJob(String name, IVideo video, VideoJobListener listener) {
        super(name, video, listener);
    }

    public VideoJobListener getListener() {
        return (VideoJobListener) super.getListener();
    }
}
