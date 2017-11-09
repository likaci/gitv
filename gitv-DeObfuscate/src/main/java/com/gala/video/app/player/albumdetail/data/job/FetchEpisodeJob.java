package com.gala.video.app.player.albumdetail.data.job;

import com.gala.sdk.player.data.IFetchEpisodeListTask;
import com.gala.tvapi.tv2.model.Episode;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import java.util.List;

public class FetchEpisodeJob extends AlbumJob {
    private static final int MAX_VALID_ORDER = 10000;
    private static final String TAG = "AlbumDetail/AlbumDetail/FetchEpisodeJob";

    public FetchEpisodeJob(AlbumInfo albumInfo, AlbumJobListener listener) {
        super(TAG, albumInfo, listener);
    }

    public void onRun(final JobController controller) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onRun() albumId=" + ((AlbumInfo) getData()).getAlbumId() + "getPlayOrder()" + ((AlbumInfo) getData()).getPlayOrder());
        }
        ThreadUtils.execute(new Runnable() {
            public void run() {
                AlbumInfo albumInfo = (AlbumInfo) FetchEpisodeJob.this.getData();
                IFetchEpisodeListTask task = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getFetchEpisodeTaskFactory().getInstance(albumInfo.getAlbumId(), albumInfo.getTvCount());
                FetchEpisodeJob.this.modifyVideoData(task.getCurrentEpisodeList(albumInfo.getPlayOrder()), task.getTotal());
                FetchEpisodeJob.this.notifyJobSuccess(controller);
            }
        });
    }

    private int getMaxOrder(List<Episode> episodes) {
        int result = 0;
        for (Episode ep : episodes) {
            if (ep.order > result && ep.order < 10000) {
                result = ep.order;
            }
        }
        return result;
    }

    private void modifyVideoData(List<Episode> episodes, int total) {
        AlbumInfo albumInfo = (AlbumInfo) getData();
        int maxOrder = Math.max(getMaxOrder(episodes), total);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "modifyVideoData(), episodes size=" + episodes.size() + "maxOrder=" + maxOrder);
        }
        albumInfo.setEpisodeMaxOrder(maxOrder);
        if (!ListUtils.isEmpty((List) episodes)) {
            albumInfo.setEpisodes(episodes, total);
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "just modify maxOrder and notifyJobSuccess(), and go FetchFullEpisodeJob");
        }
    }
}
