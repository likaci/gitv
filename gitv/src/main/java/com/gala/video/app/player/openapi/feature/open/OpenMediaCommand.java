package com.gala.video.app.player.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.openapi.OpenApiItemUtil;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.activity.OpenApiLoadingActivity;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.push.mqttv3.internal.ClientDefaults;
import com.qiyi.tv.client.data.Album;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.data.Video;
import com.qiyi.tv.client.feature.common.PlayParams;
import com.qiyi.tv.client.impl.Params.TargetType;

public class OpenMediaCommand extends ServerCommand<Intent> {
    private static final int Loading_Delay = 1900;
    private static final int OPEN_ALBUM = 3;
    private static final int OPEN_ALBUM_PARAMS = 2;
    private static final int OPEN_PLAYLIST = 1;
    private static final String TAG = "OpenMediaCommand";
    Handler mHandler = new Handler(getContext().getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LogUtils.d(OpenMediaCommand.TAG, " Loading end openPlayList");
                    OpenMediaCommand.this.openPlayList();
                    return;
                case 2:
                    LogUtils.d(OpenMediaCommand.TAG, " Loading end OPEN_ALBUM_PARAMS");
                    OpenMediaCommand.this.openAlbumWithPlayParams();
                    return;
                case 3:
                    LogUtils.d(OpenMediaCommand.TAG, " Loading end OPEN_ALBUM");
                    OpenMediaCommand.this.openAlbum();
                    return;
                default:
                    return;
            }
        }
    };
    private ChannelLabel mLabel;
    private Media mMedia;
    private PlayParams mOpenPlayParams;
    private com.gala.sdk.player.PlayParams mPlayParams;

    public OpenMediaCommand(Context context) {
        super(context, TargetType.TARGET_MEDIA, 20001, 30000);
    }

    public Bundle onProcess(Bundle params) {
        PingBackCollectionFieldUtils.setIncomeSrc("openapi");
        this.mMedia = ServerParamsHelper.parseMedia(params);
        this.mOpenPlayParams = ServerParamsHelper.parsePlayParams(params);
        if (this.mMedia == null) {
            return OpenApiResultCreater.createResultBundle(6);
        }
        Bundle result;
        this.mLabel = OpenApiUtils.createChannelLabel(this.mMedia);
        boolean isShowLoading = PlayerAppConfig.enableExtraPage();
        if (this.mLabel != null) {
            if (isShowLoading) {
                showLoadingAndOpenPageDelay(1);
            } else {
                openPlayList();
            }
            result = OpenApiResultCreater.createResultBundle(0);
            increaseAccessCount();
        } else if ((this.mMedia instanceof Album) || (this.mMedia instanceof Video)) {
            this.mPlayParams = new com.gala.sdk.player.PlayParams();
            this.mPlayParams.from = "openAPI";
            this.mPlayParams.sourceType = SourceType.OUTSIDE;
            if (this.mOpenPlayParams != null) {
                if (isShowLoading) {
                    showLoadingAndOpenPageDelay(2);
                } else {
                    openAlbumWithPlayParams();
                }
            } else if (isShowLoading) {
                showLoadingAndOpenPageDelay(3);
            } else {
                openAlbum();
            }
            result = OpenApiResultCreater.createResultBundle(0);
            increaseAccessCount();
        } else {
            result = OpenApiResultCreater.createResultBundle(6);
        }
        ServerParamsHelper.setCommandContinue(result, false);
        return result;
    }

    private void showLoadingAndOpenPageDelay(int type) {
        Intent intent = new Intent(getContext(), OpenApiLoadingActivity.class);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        getContext().startActivity(intent);
        this.mHandler.sendEmptyMessageDelayed(type, 1900);
    }

    private void openPlayList() {
        PingBackCollectionFieldUtils.setIncomeSrc("openapi");
        OpenApiItemUtil.openDetailOrPlay(getContext(), this.mLabel, "openAPI", this.mLabel.channelId, true, false);
    }

    private void openAlbumWithPlayParams() {
        PingBackCollectionFieldUtils.setIncomeSrc("openapi");
        OpenApiItemUtil.openAlbum(getContext(), OpenApiUtils.createAlbum(this.mMedia), "openAPI", this.mPlayParams, true, false, this.mOpenPlayParams.isContinuePlay(), this.mMedia instanceof Album);
    }

    private void openAlbum() {
        PingBackCollectionFieldUtils.setIncomeSrc("openapi");
        OpenApiItemUtil.openAlbum(getContext(), OpenApiUtils.createAlbum(this.mMedia), "openAPI", this.mPlayParams, true, false);
    }
}
