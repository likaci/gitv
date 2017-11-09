package com.gala.video.app.player.albumdetail.data.job;

import com.gala.sdk.player.data.IFetchPlaylistBySourceTask;
import com.gala.tvapi.tv2.model.Episode;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.app.player.utils.debug.DetailDebugHelper;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import java.util.ArrayList;
import java.util.List;

public class FetchDetailEpisodeForSource extends AlbumJob {
    private static final String TAG = "AlbumDetail/Data/FetchDetailEpisodeForSource";

    public FetchDetailEpisodeForSource(AlbumInfo albumInfo, AlbumJobListener listener) {
        super(TAG, albumInfo, listener);
    }

    public void onRun(final JobController controller) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> onRun");
        }
        final AlbumInfo albumInfo = (AlbumInfo) getData();
        ThreadUtils.execute(new Runnable() {
            public void run() {
                IFetchPlaylistBySourceTask task = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getFetchPlayListBySourceTaskFactory().getInstance(albumInfo.getAlbumId());
                List<Episode> firstList = task.getFirstEpisodes(albumInfo.getSourceCode(), albumInfo.getTvId());
                int total = task.getTotal();
                FetchDetailEpisodeForSource.this.notifyTaskSuccess(controller, firstList, total);
                List secondList = task.getSecondList();
                if (!ListUtils.isEmpty(secondList)) {
                    List<Episode> list = new ArrayList();
                    list.addAll(firstList);
                    list.addAll(secondList);
                    FetchDetailEpisodeForSource.this.notifyTaskSuccess(controller, list, total);
                }
            }
        });
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> onRun end");
        }
    }

    private void notifyTaskSuccess(JobController controller, List<Episode> playlist, int total) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> notifyTaskSuccess ");
        }
        AlbumInfo albumInfo = (AlbumInfo) getData();
        if (!ListUtils.isEmpty((List) playlist)) {
            DetailDebugHelper.modifyForEpisodeLostError(playlist);
            albumInfo.setEpisodes(playlist, total);
            albumInfo.setEpisodeMaxOrder(total);
        }
        notifyJobSuccess(controller);
    }
}
