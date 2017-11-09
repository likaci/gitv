package com.gala.video.app.player.albumdetail.data.job;

import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.lib.framework.core.job.JobController;

public class EmptyJob extends AlbumJob {
    public EmptyJob(AlbumInfo albumInfo, AlbumJobListener listener) {
        super("Albumdetail/EmptyJob", albumInfo, listener);
    }

    public void onRun(JobController controller) {
        notifyJobSuccess(controller);
    }
}
