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
import java.util.ArrayList;
import java.util.List;

public class FetchFullEpisodeJob extends AlbumJob {
    private static final int FETCH_PAGE_SIZE = 120;
    private static final int MAX_VALID_ORDER = 10000;
    private static final String TAG = "AlbumDetail/AlbumDetail/FetchFullEpisodeJob";
    private boolean mIsSupportWindowPlay;

    public FetchFullEpisodeJob(AlbumInfo albumInfo, AlbumJobListener listener, boolean isSupportWindowPlay) {
        super(TAG, albumInfo, listener);
        this.mIsSupportWindowPlay = isSupportWindowPlay;
    }

    public void onRun(final JobController controller) {
        final int episodeSize = ListUtils.getCount(((AlbumInfo) getData()).getEpisodeVideos());
        final AlbumInfo albumInfo = (AlbumInfo) getData();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onRun tvCount = " + albumInfo.getTvCount());
        }
        ThreadUtils.execute(new Runnable() {
            public void run() {
                IFetchEpisodeListTask task = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getFetchEpisodeTaskFactory().getInstance(albumInfo.getAlbumId(), albumInfo.getTvCount());
                int total = task.getTotal();
                int maxOrder = ((AlbumInfo) FetchFullEpisodeJob.this.getData()).getEpisodeMaxOrder();
                if (LogUtils.mIsDebug) {
                    LogUtils.d(FetchFullEpisodeJob.TAG, "total = " + total + "ListUtils.getCount(video.getEpisodeVideos())=" + episodeSize + "maxOrder=" + maxOrder);
                }
                if (total > 120 || maxOrder > 120 || episodeSize < total || episodeSize == 0) {
                    FetchFullEpisodeJob.this.modifyVideoData(new ArrayList(task.getFullEpisodeList(albumInfo.getAlbum(), FetchFullEpisodeJob.this.mIsSupportWindowPlay)), total);
                }
                FetchFullEpisodeJob.this.notifyJobSuccess(controller);
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

    public void modifyVideoData(List<Episode> episodes, int total) {
        int maxOrder = Math.max(getMaxOrder(episodes), total);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifyJobSuccess(), episodes size=" + episodes.size() + "maxOrder" + maxOrder);
        }
        AlbumInfo albumInfo = (AlbumInfo) getData();
        albumInfo.setEpisodeMaxOrder(maxOrder);
        albumInfo.setEpisodes(episodes, total);
    }
}
