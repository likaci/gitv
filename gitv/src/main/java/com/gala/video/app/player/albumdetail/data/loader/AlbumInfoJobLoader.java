package com.gala.video.app.player.albumdetail.data.loader;

import android.content.Context;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.DetailPageManage;
import com.gala.video.app.player.albumdetail.data.job.AlbumJob;
import com.gala.video.app.player.albumdetail.data.job.AlbumJobSwitcher;
import com.gala.video.app.player.albumdetail.data.job.AlbumJobSwitcher.ISwitchCondition;
import com.gala.video.app.player.albumdetail.data.job.AuthDetailVipVideoJob;
import com.gala.video.app.player.albumdetail.data.job.EmptyJob;
import com.gala.video.app.player.albumdetail.data.job.FetchDetailEpisodeForSource;
import com.gala.video.app.player.albumdetail.data.job.FetchDetailFavData;
import com.gala.video.app.player.albumdetail.data.job.FetchDetailInfoJob;
import com.gala.video.app.player.albumdetail.data.job.FetchEpisodeFromCacheJob;
import com.gala.video.app.player.albumdetail.data.job.FetchEpisodeJob;
import com.gala.video.app.player.albumdetail.data.job.FetchFullEpisodeJob;
import com.gala.video.app.player.albumdetail.data.job.FetchGroupDetailJob;
import com.gala.video.app.player.albumdetail.data.job.FetchSourceDetailInfoJob;
import com.gala.video.app.player.albumdetail.data.job.PackageContentJob;
import com.gala.video.app.player.albumdetail.data.job.VodInfoJob;
import com.gala.video.app.player.albumdetail.data.loader.DetailAlbumLoader.LoadType;
import com.gala.video.app.player.albumdetail.data.loader.DetailAlbumLoader.MyVideoJobListener;
import com.gala.video.app.player.ui.config.AlbumDetailUiConfig;
import com.gala.video.app.player.ui.config.IAlbumDetailUiConfig;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class AlbumInfoJobLoader extends DetailAlbumLoader {
    private static final String TAG = "AlbumDetail/Data/AlbumInfoJobLoader";
    private DetailPageManage mDetailPageManage;
    private AlbumJob mHeadJob;
    private ISwitchCondition mSourceCondition = new SwitchCondition(this, ConditionType.SourceType);
    private ISwitchCondition mTVSeriesCondition = new SwitchCondition(this, ConditionType.TvSeries);
    private IAlbumDetailUiConfig mUiConfig = new AlbumDetailUiConfig();

    static class SwitchCondition extends BaseWeakListener<DetailAlbumLoader> implements ISwitchCondition {
        ConditionType mConditionType;

        public enum ConditionType {
            TvSeries,
            SourceType
        }

        public SwitchCondition(DetailAlbumLoader outer, ConditionType conditionType) {
            super(outer);
            this.mConditionType = conditionType;
        }

        public boolean checkPass(AlbumInfo albumInfo) {
            switch (this.mConditionType) {
                case TvSeries:
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(AlbumInfoJobLoader.TAG, ">> mTVSeriesCondition=" + albumInfo);
                    }
                    return albumInfo.isTvSeries();
                case SourceType:
                    return albumInfo.isSourceType();
                default:
                    return false;
            }
        }
    }

    public void cancelJobLoad() {
        super.cancelJobLoad();
    }

    public AlbumInfoJobLoader(Context context, AlbumInfo albumInfo, DetailPageManage detailPageManage) {
        super(context, albumInfo);
        this.mDetailPageManage = detailPageManage;
    }

    public void dataLoad(LoadType type) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> dataLoad=" + type);
        }
        this.mHeadJob = new EmptyJob(getInfo(), null);
        AlbumJob detailJob = new FetchDetailInfoJob(getInfo(), new MyVideoJobListener(this, 5));
        AlbumJob favJob = new FetchDetailFavData(getInfo(), new MyVideoJobListener(this, 7));
        AlbumJob authDetailVipVideoJob = new AuthDetailVipVideoJob(getInfo(), new MyVideoJobListener(this, 6));
        AlbumJob pkgJob = new PackageContentJob(getInfo(), new MyVideoJobListener(this, 21));
        AlbumJob vodJob = new VodInfoJob(getInfo(), new MyVideoJobListener(this, 22));
        AlbumJob refreshSourceInfoJob = new FetchSourceDetailInfoJob(getInfo(), new MyVideoJobListener(this, 8));
        AlbumJob groupDetailJob = new FetchGroupDetailJob(getInfo(), null, false, this.mDetailPageManage);
        AlbumJobSwitcher sourceRefreshHeadJob = new AlbumJobSwitcher(getInfo(), null);
        sourceRefreshHeadJob.link(this.mSourceCondition, refreshSourceInfoJob);
        sourceRefreshHeadJob.link(null, groupDetailJob);
        refreshSourceInfoJob.link(groupDetailJob);
        AlbumJobSwitcher episodeHeadJob = new AlbumJobSwitcher(getInfo(), null);
        AlbumJob episodeCurrentJob = new FetchEpisodeJob(getInfo(), new MyVideoJobListener(this, 9));
        AlbumJob episodeFullJob = new FetchFullEpisodeJob(getInfo(), new MyVideoJobListener(this, 10), this.mUiConfig.isEnableWindowPlay());
        AlbumJob episodeSourceJob = new FetchDetailEpisodeForSource(getInfo(), new MyVideoJobListener(this, 10));
        episodeHeadJob.link(this.mTVSeriesCondition, episodeCurrentJob);
        episodeHeadJob.link(this.mSourceCondition, episodeSourceJob);
        episodeHeadJob.link(null, new EmptyJob(getInfo(), null));
        episodeCurrentJob.link(episodeFullJob);
        AlbumJob switchRefreshSourceInfoJob = new FetchSourceDetailInfoJob(getInfo(), new MyVideoJobListener(this, 8));
        AlbumJob switchRefreshGroupDetailJob = new FetchGroupDetailJob(getInfo(), null, true, this.mDetailPageManage);
        switchRefreshSourceInfoJob.link(switchRefreshGroupDetailJob);
        switch (type) {
            case FULLLOAD_NORMAL:
                this.mHeadJob.link(detailJob);
                detailJob.link(favJob, authDetailVipVideoJob, episodeHeadJob, sourceRefreshHeadJob);
                authDetailVipVideoJob.link(vodJob, pkgJob);
                break;
            case FULLLOAD_QUICK:
            case NO_CREATE_PLAYER:
            case TOTAL_SWITCH_LOAD:
                if (getInfo() != null) {
                    getInfo().clearEpisodeVideos();
                }
                this.mHeadJob.link(detailJob);
                detailJob.link(favJob, authDetailVipVideoJob, episodeHeadJob, sourceRefreshHeadJob);
                authDetailVipVideoJob.link(vodJob, pkgJob);
                break;
            case RESUME_LOAD:
                this.mHeadJob.link(favJob, authDetailVipVideoJob, vodJob, pkgJob);
                if (this.mSourceCondition.checkPass(getInfo()) && !this.mUiConfig.isEnableWindowPlay()) {
                    pkgJob.link(switchRefreshSourceInfoJob);
                    break;
                }
            case SWITCH_LOAD:
                this.mHeadJob.link(favJob);
                if (this.mSourceCondition.checkPass(getInfo())) {
                    favJob.link(switchRefreshSourceInfoJob);
                    break;
                }
                break;
        }
        submit(this.mHeadJob);
    }

    public void cacheEpisodeLoad() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> cacheEpisodeLoad, isTvSeries=" + getInfo().isTvSeries());
        }
        if (getInfo().isTvSeries()) {
            submit(new FetchEpisodeFromCacheJob(getInfo(), new MyVideoJobListener(this, 17)), false);
        }
    }
}
