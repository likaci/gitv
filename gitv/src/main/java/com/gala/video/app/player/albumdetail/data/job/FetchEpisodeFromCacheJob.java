package com.gala.video.app.player.albumdetail.data.job;

import com.gala.sdk.player.data.IFetchEpisodeListTask;
import com.gala.sdk.utils.ListUtils;
import com.gala.tvapi.tv2.model.Episode;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FetchEpisodeFromCacheJob extends AlbumJob {
    private static final int MAX_VALID_ORDER = 10000;
    private static final String TAG = "AlbumDetail/AlbumDetail/FetchEpisodeFromCacheJob";

    public FetchEpisodeFromCacheJob(AlbumInfo albumInfo, AlbumJobListener listener) {
        super(TAG, albumInfo, listener);
    }

    public void onRun(JobController controller) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onRun() albumId=" + ((AlbumInfo) getData()).getAlbumId());
        }
        AlbumInfo albumInfo = (AlbumInfo) getData();
        IFetchEpisodeListTask task = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getFetchEpisodeTaskFactory().getInstance(albumInfo.getAlbumId(), albumInfo.getTvCount());
        List cacheEpisodes = new CopyOnWriteArrayList(task.getAllEpisodesFromCache());
        int total = task.getCacheEpisodesTotal();
        if (!ListUtils.isEmpty(cacheEpisodes)) {
            modifyVideoData(cacheEpisodes, total);
        }
        notifyJobSuccess(controller);
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
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "modifyVideoData(), episodes size=" + episodes.size());
        }
        AlbumInfo albumInfo = (AlbumInfo) getData();
        albumInfo.setEpisodeMaxOrder(Math.max(getMaxOrder(episodes), total));
        albumInfo.setEpisodes(episodes, total);
    }
}
