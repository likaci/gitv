package com.gala.video.app.player.albumdetail.data.job;

import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.lib.framework.core.job.Job;

public class AlbumJob extends Job<AlbumInfo> {
    private static final String TAG = "AlbumJob";

    public AlbumJob(String name, AlbumInfo data, AlbumJobListener listener) {
        super(name, data, listener);
    }

    public AlbumJobListener getListener() {
        return (AlbumJobListener) super.getListener();
    }
}
