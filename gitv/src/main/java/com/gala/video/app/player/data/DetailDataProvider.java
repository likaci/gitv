package com.gala.video.app.player.data;

import android.content.Context;
import android.content.Intent;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.data.IVideoItemFactory;
import com.gala.sdk.utils.job.JobControllerImpl;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.AlbumType;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumVideoItem;
import com.gala.video.app.player.albumdetail.data.DetailPageManage;
import com.gala.video.app.player.albumdetail.data.job.FetchDetailHistoryJob;
import com.gala.video.app.player.albumdetail.data.loader.AlbumInfoJobLoader;
import com.gala.video.app.player.albumdetail.data.loader.CollectionJobLoader;
import com.gala.video.app.player.albumdetail.data.loader.DetailAlbumLoader.LoadType;
import com.gala.video.app.player.albumdetail.data.task.DetailInfoCompleteTask;
import com.gala.video.app.player.albumdetail.data.task.DetailInfoCompleteTask.IDetailInfoCompleteTaskListener;
import com.gala.video.app.player.controller.DataDispatcher;
import com.gala.video.app.player.utils.MyPlayerProfile;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.DataUtils;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class DetailDataProvider {
    private final String TAG = ("Detail/Data/DetailDataProvider@" + Integer.toHexString(hashCode()));
    private AlbumInfo mAlbumInfo;
    private AlbumInfoJobLoader mAlbumInfoJobLoader;
    private CollectionJobLoader mCollctJobLoader;
    private Context mContext;
    private IDetailInfoCompleteTaskListener mDetailInfoCompleteTaskListener = new IDetailInfoCompleteTaskListener() {
        public void onSuccess(Album album) {
            LogRecordUtils.logd(DetailDataProvider.this.TAG, ">> mDetailInfoCompleteTaskListener onSuccess");
            DetailDataProvider.this.mIntent.putExtra("albumInfo", album);
            DetailDataProvider.this.createVideoItem(album);
        }

        public void onFailed(ApiException e) {
            LogRecordUtils.loge(DetailDataProvider.this.TAG, ">> mDetailInfoCompleteTaskListener onFailed, e=" + e);
            DataDispatcher.instance().postOnMainThread(DetailDataProvider.this.mContext, 2, e);
        }
    };
    private DetailPageManage mDetailPageManage;
    private boolean mEnabledWindowPlay = Project.getInstance().getBuild().isSupportSmallWindowPlay();
    private ScheduledThreadPoolExecutor mExecutor = new ScheduledThreadPoolExecutor(1);
    private Intent mIntent;
    private IVideo mVideo;

    public DetailDataProvider(Context context, DetailPageManage detailPageManage, Intent intent) {
        this.mIntent = intent;
        this.mContext = context;
        this.mDetailPageManage = detailPageManage;
    }

    public void initialize() {
        LogRecordUtils.logd(this.TAG, "initialize");
        initVideoItem();
    }

    public IVideo getCurrentVideo() {
        return this.mVideo;
    }

    public void startLoad() {
        LogRecordUtils.logd(this.TAG, ">> startLoad");
        if (this.mAlbumInfoJobLoader == null) {
            initDataLoader();
        }
        if (this.mCollctJobLoader == null) {
            initCollectLoader();
        }
        this.mAlbumInfoJobLoader.cacheEpisodeLoad();
        this.mAlbumInfoJobLoader.dataLoad(LoadType.FULLLOAD_NORMAL);
    }

    public void resumeLoad() {
        if (!this.mEnabledWindowPlay) {
            refreshHistoryOnResume();
        }
        if (this.mAlbumInfoJobLoader != null) {
            this.mAlbumInfoJobLoader.dataLoad(LoadType.RESUME_LOAD);
        } else {
            LogRecordUtils.logd(this.TAG, ">> mAlbumInfoJobLoader is null ");
        }
    }

    public void switchLoad(IVideo video, boolean isTotallySwitch) {
        LogRecordUtils.logd(this.TAG, ">> switchLoad" + isTotallySwitch);
        if (video != null) {
            this.mVideo = video;
            LogRecordUtils.logd(this.TAG, ">> video.getAlbum()" + video.getAlbum());
            this.mAlbumInfoJobLoader.setAlbum(video.getAlbum());
            if (isTotallySwitch) {
                this.mAlbumInfoJobLoader.dataLoad(LoadType.TOTAL_SWITCH_LOAD);
            } else {
                this.mAlbumInfoJobLoader.dataLoad(LoadType.SWITCH_LOAD);
            }
            this.mCollctJobLoader.setAlbum(video.getAlbum());
        }
    }

    public void loadOnNetworkChanged() {
        if (this.mAlbumInfoJobLoader != null) {
            this.mAlbumInfoJobLoader.dataLoad(LoadType.NO_CREATE_PLAYER);
        }
    }

    public void stopLoad() {
        if (this.mAlbumInfoJobLoader != null) {
            this.mAlbumInfoJobLoader.cancelJobLoad();
            this.mAlbumInfoJobLoader.clearAlbumInfo();
        }
        if (this.mCollctJobLoader != null) {
            this.mCollctJobLoader.cancelJobLoad();
            this.mCollctJobLoader.clearAlbumInfo();
        }
    }

    public void release() {
        LogRecordUtils.logd(this.TAG, ">> release.");
        this.mVideo = null;
        this.mAlbumInfoJobLoader = null;
        this.mCollctJobLoader = null;
    }

    public void updateFavState(boolean wasFavored) {
        LogRecordUtils.logd(this.TAG, ">> onFavClicked" + wasFavored);
        if (wasFavored) {
            this.mCollctJobLoader.collectCancel();
        } else {
            this.mCollctJobLoader.collectUpload();
        }
    }

    private void initVideoItem() {
        boolean isComplete = this.mIntent.getBooleanExtra(IntentConfig2.INTENT_PARAM_ALBUM_INFO_COMPLETE, true);
        Album album = (Album) this.mIntent.getSerializableExtra("albumInfo");
        LogRecordUtils.logd(this.TAG, ">> initVideoItem, isComplete=" + isComplete + ", album=" + DataUtils.albumInfoToString(album));
        if (isComplete) {
            createVideoItem(album);
            return;
        }
        DataDispatcher.instance().postOnMainThread(this.mContext, 1, null);
        completeIncomingData(album);
    }

    private void completeIncomingData(Album album) {
        if (album != null) {
            DetailInfoCompleteTask task = new DetailInfoCompleteTask(album);
            task.setTaskListener(this.mDetailInfoCompleteTaskListener);
            task.execute();
            return;
        }
        throw new IllegalArgumentException("Incoming Album is null!!");
    }

    private void initDataLoader() {
        this.mAlbumInfoJobLoader = new AlbumInfoJobLoader(this.mContext, this.mAlbumInfo, this.mDetailPageManage);
        this.mAlbumInfoJobLoader.setExecutor(this.mExecutor);
    }

    private void initCollectLoader() {
        this.mCollctJobLoader = new CollectionJobLoader(this.mContext, this.mAlbumInfo);
        this.mCollctJobLoader.setExecutor(this.mExecutor);
    }

    private void createVideoItem(Album album) {
        IVideoItemFactory videoItemFactory = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getVideoItemFactory();
        Album detailAlbum = copyCacheAlbumInfo(album);
        PlayParams playParams = getPlayParams();
        this.mVideo = videoItemFactory.createVideoItem(playParams.sourceType, detailAlbum, new MyPlayerProfile());
        this.mAlbumInfo = new AlbumInfo(album);
        String from = this.mIntent.getStringExtra("from");
        LogRecordUtils.logd(this.TAG, "createVideoItem -->" + this.mVideo);
        new FetchDetailHistoryJob(this.mVideo, null, from, this.mVideo.getAlbum().type != AlbumType.VIDEO.getValue()).run(new JobControllerImpl(this.mContext));
        DataDispatcher.instance().postOnMainThread(this.mContext, 4, new AlbumVideoItem(this.mAlbumInfo, this.mVideo, playParams.sourceType));
    }

    private Album copyCacheAlbumInfo(Album ori) {
        Album album;
        if (ori != null) {
            album = ori.copy();
            if (!StringUtils.equals(album.qpId, DetailConstants.sCacheAlbum.qpId) || !StringUtils.equals(album.tvQid, DetailConstants.sCacheAlbum.tvQid)) {
                return album;
            }
            LogRecordUtils.logd(this.TAG, "copyCacheAlbumInfo, copy album info from cache, sCacheAlbum=" + DetailConstants.sCacheAlbum);
            album.key = DetailConstants.sCacheAlbum.key;
            album.score = DetailConstants.sCacheAlbum.score;
            album.pCount = DetailConstants.sCacheAlbum.pCount;
            album.tag = DetailConstants.sCacheAlbum.tag;
            album.cast = DetailConstants.sCacheAlbum.cast;
            album.time = DetailConstants.sCacheAlbum.time;
            album.tvCount = DetailConstants.sCacheAlbum.tvCount;
            album.desc = DetailConstants.sCacheAlbum.desc;
            album.strategy = DetailConstants.sCacheAlbum.strategy;
            return album;
        }
        album = new Album();
        String albumId = this.mIntent.getStringExtra("albumId");
        String tvQid = this.mIntent.getStringExtra("tvid");
        LogRecordUtils.logd(this.TAG, "parseIntent: albumId from intent albumId=" + albumId + ", tvQid=" + tvQid);
        album.qpId = albumId;
        album.tvQid = tvQid;
        album.vid = "Tuxjq6bZNo96JVM8UwN67jRZ6XF4Wbm5";
        return album;
    }

    private PlayParams getPlayParams() {
        PlayParams playParams = (PlayParams) this.mIntent.getSerializableExtra("play_list_info");
        LogRecordUtils.logd(this.TAG, ">> getPlayParams, playParams=" + playParams);
        if (playParams == null) {
            playParams = new PlayParams();
            playParams.sourceType = SourceType.COMMON;
        } else if (SourceType.BO_DAN.equals(playParams.sourceType)) {
            playParams.sourceType = SourceType.COMMON;
        }
        LogRecordUtils.logd(this.TAG, "<< getPlayParams, playParams=" + playParams);
        return playParams;
    }

    private void refreshHistoryOnResume() {
        LogRecordUtils.logd(this.TAG, "refreshHistoryOnResume, mEnabledWindowPlay=" + this.mEnabledWindowPlay);
        new FetchDetailHistoryJob(this.mVideo, null, this.mIntent.getStringExtra("from"), !this.mEnabledWindowPlay).run(new JobControllerImpl(this.mContext));
        DataDispatcher.instance().postOnMainThread(this.mContext, 19, this.mVideo);
    }
}
