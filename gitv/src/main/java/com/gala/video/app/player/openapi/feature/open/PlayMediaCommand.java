package com.gala.video.app.player.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.gala.sdk.player.SourceType;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.utils.VideoChecker;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.activity.OpenApiLoadingActivity;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.push.mqttv3.internal.ClientDefaults;
import com.qiyi.tv.client.data.Album;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.data.Playlist;
import com.qiyi.tv.client.data.Video;
import com.qiyi.tv.client.feature.common.PlayParams;
import com.qiyi.tv.client.impl.Params.TargetType;

public class PlayMediaCommand extends ServerCommand<Intent> {
    private static final int Loading_Delay = 1900;
    private static final int PLAY_ALBUM = 3;
    private static final int PLAY_ALBUM_PARAMS = 2;
    private static final int PLAY_PLAYLIST = 1;
    private static final String TAG = "PlayMediaCommand";
    Handler mHandler = new Handler(getContext().getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LogUtils.d(PlayMediaCommand.TAG, " Loading end openPlayList");
                    PlayMediaCommand.this.playPlaylist();
                    return;
                case 2:
                    PlayMediaCommand.this.playAlbumWithParams();
                    return;
                case 3:
                    PlayMediaCommand.this.playAlbumOrVideo();
                    return;
                default:
                    return;
            }
        }
    };
    private int mIntentFlags;
    private Media mMedia;
    private PlayParams mOpenPlayParams;
    private com.gala.sdk.player.PlayParams mPlayParams;

    public PlayMediaCommand(Context context) {
        super(context, TargetType.TARGET_MEDIA, 20004, 30000);
    }

    public Bundle onProcess(Bundle params) {
        this.mMedia = ServerParamsHelper.parseMedia(params);
        this.mIntentFlags = ServerParamsHelper.parseIntentFlag(params);
        this.mOpenPlayParams = ServerParamsHelper.parsePlayParams(params);
        if (this.mMedia == null) {
            return OpenApiResultCreater.createResultBundle(6);
        }
        if (this.mMedia.getType() == 3) {
            return OpenApiResultCreater.createResultBundle(5);
        }
        Bundle result;
        boolean isShowLoading = PlayerAppConfig.enableExtraPage();
        if ((this.mMedia instanceof Album) || (this.mMedia instanceof Video)) {
            if (this.mOpenPlayParams != null) {
                if (this.mMedia instanceof Album) {
                    this.mMedia = putHistoryInAlbum(this.mMedia);
                }
                if (isShowLoading) {
                    showLoadingAndPlayMediaDelay(2);
                } else {
                    playAlbumWithParams();
                }
            } else if (isShowLoading) {
                showLoadingAndPlayMediaDelay(3);
            } else {
                playAlbumOrVideo();
            }
            result = OpenApiResultCreater.createResultBundle(0);
            increaseAccessCount();
        } else if (this.mMedia instanceof Playlist) {
            this.mPlayParams = new com.gala.sdk.player.PlayParams();
            this.mPlayParams.playListId = this.mMedia.getId();
            this.mPlayParams.from = "openAPI";
            if (isShowLoading) {
                showLoadingAndPlayMediaDelay(1);
            } else {
                playPlaylist();
            }
            result = OpenApiResultCreater.createResultBundle(0);
            increaseAccessCount();
        } else {
            result = OpenApiResultCreater.createResultBundle(6);
        }
        ServerParamsHelper.setCommandContinue(result, false);
        return result;
    }

    private void showLoadingAndPlayMediaDelay(int type) {
        Intent intent = new Intent(getContext(), OpenApiLoadingActivity.class);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        getContext().startActivity(intent);
        this.mHandler.sendEmptyMessageDelayed(type, 1900);
    }

    private void playAlbumWithParams() {
        PingBackCollectionFieldUtils.setIncomeSrc("openapi");
        BasePlayParamBuilder builder = new BasePlayParamBuilder();
        com.gala.sdk.player.PlayParams playParam = new com.gala.sdk.player.PlayParams();
        playParam.sourceType = SourceType.OUTSIDE;
        builder.setPlayParams(playParam);
        builder.setAlbumInfo(OpenApiUtils.createAlbum(this.mMedia));
        builder.setFrom("openAPI");
        builder.setClearTaskFlag((this.mIntentFlags & 32768) != 0);
        builder.setBuySource("openAPI");
        builder.setTabSource("其他");
        builder.setContinueNextVideo(this.mOpenPlayParams.isContinuePlay());
        GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(getContext(), builder);
    }

    private void playAlbumOrVideo() {
        PingBackCollectionFieldUtils.setIncomeSrc("openapi");
        BasePlayParamBuilder builder = new BasePlayParamBuilder();
        com.gala.sdk.player.PlayParams playParam = new com.gala.sdk.player.PlayParams();
        playParam.sourceType = SourceType.OUTSIDE;
        builder.setPlayParams(playParam);
        builder.setAlbumInfo(OpenApiUtils.createAlbum(this.mMedia));
        builder.setFrom("openAPI");
        builder.setClearTaskFlag((this.mIntentFlags & 32768) != 0);
        builder.setBuySource("openAPI");
        builder.setTabSource("其他");
        GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(getContext(), builder);
    }

    private void playPlaylist() {
        PingBackCollectionFieldUtils.setIncomeSrc("openapi");
        BasePlayParamBuilder builder = new BasePlayParamBuilder();
        this.mPlayParams.sourceType = SourceType.OUTSIDE;
        builder.setPlayParams(this.mPlayParams);
        builder.setFrom("openAPI");
        builder.setClearTaskFlag((this.mIntentFlags & 32768) != 0);
        builder.setBuySource("openAPI");
        builder.setTabSource("其他");
        GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(getContext(), builder);
    }

    private Media putHistoryInAlbum(Media media) {
        PingBackCollectionFieldUtils.setIncomeSrc("openapi");
        HistoryInfo historyInfo = GetInterfaceTools.getIHistoryCacheManager().getAlbumHistory(media.getId());
        if (historyInfo != null) {
            int playOrder = historyInfo.getPlayOrder();
            if (playOrder < 1) {
                playOrder = 1;
            }
            String tvName = historyInfo.getAlbum().tvName;
            String tvQid = historyInfo.getTvId();
            if (VideoChecker.isValidTvId(tvQid)) {
                ((Album) media).setVideoId(tvQid);
                ((Album) media).setName(tvName);
                ((Album) media).setPlayOrder(playOrder);
            }
        }
        return media;
    }
}
