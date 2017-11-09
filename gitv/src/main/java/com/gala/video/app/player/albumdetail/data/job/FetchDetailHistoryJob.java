package com.gala.video.app.player.albumdetail.data.job;

import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.data.job.VideoJob;
import com.gala.sdk.player.data.job.VideoJobListener;
import com.gala.sdk.utils.job.JobController;
import com.gala.tvapi.type.ContentType;
import com.gala.video.app.player.utils.VideoChecker;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo;

public class FetchDetailHistoryJob extends VideoJob {
    private static final String TAG = "AlbumDetail/Data/FetchDetailHistoryJob";
    private boolean hasHistory;
    private String mFrom;

    public FetchDetailHistoryJob(IVideo video, VideoJobListener listener, String from, boolean history) {
        super(TAG, video, listener);
        this.mFrom = from;
        this.hasHistory = history;
    }

    public void onRun(JobController controller) {
        IVideo video = (IVideo) getData();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> onRun: qpId=" + video.getAlbumId() + "tvid=" + video.getTvId() + this.hasHistory);
        }
        if (video.getPlayOrder() < 1) {
            video.setPlayOrder(1);
        }
        if (StringUtils.equals("carousel_rec", this.mFrom)) {
            notifyJobSuccess(controller);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "<< onRun from carousel_rec");
            }
        } else if (this.hasHistory) {
            HistoryInfo historyInfo = GetInterfaceTools.getIHistoryCacheManager().getAlbumHistory(video.getAlbumId());
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "onRun: local history info=" + historyInfo);
            }
            if (historyInfo == null || historyInfo.getAlbum().getContentType() != ContentType.FEATURE_FILM) {
                video.getAlbum().time = "";
                if (LogUtils.mIsDebug) {
                    LogUtils.i(TAG, " onRun -end, historyInfo != ContentType.FEATURE_FILM");
                }
            } else {
                int playOrder = historyInfo.getPlayOrder();
                if (playOrder < 1) {
                    playOrder = 1;
                }
                String tvName = historyInfo.getAlbum().tvName;
                video.setPlayOrder(playOrder);
                String tvQid = historyInfo.getTvId();
                if (VideoChecker.isValidTvId(tvQid)) {
                    video.getAlbum().tvQid = tvQid;
                    video.getAlbum().tvName = tvName;
                    video.getAlbum().time = historyInfo.getAlbum().time;
                    video.getAlbum().playTime = historyInfo.getAlbum().playTime;
                    video.getAlbum().drm = historyInfo.getAlbum().drm;
                }
            }
            notifyJobSuccess(controller);
            if (LogUtils.mIsDebug) {
                LogUtils.i(TAG, " onRun -end, video=" + video.toStringBrief());
            }
        } else {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "onRun hasHistory = false.");
            }
            notifyJobSuccess(controller);
        }
    }
}
