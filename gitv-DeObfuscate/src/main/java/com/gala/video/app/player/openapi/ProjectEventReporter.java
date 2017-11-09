package com.gala.video.app.player.openapi;

import com.gala.sdk.player.IProjectEventReporter;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiManager;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.qiyi.tv.client.data.Media;

public class ProjectEventReporter implements IProjectEventReporter {
    private static final String TAG = "Player/App/ProjectEventReporter";
    private IVideo mVideo;

    public void onPreparing(IVideo video) {
        if (OpenApiManager.instance().isAuthSuccess()) {
            this.mVideo = video;
            reportVideoState(3, this.mVideo == null ? null : this.mVideo.getAlbum());
        }
    }

    public void onPrepared() {
        if (OpenApiManager.instance().isAuthSuccess()) {
            reportVideoState(4, this.mVideo == null ? null : this.mVideo.getAlbum());
        }
    }

    public void onStarted() {
        if (OpenApiManager.instance().isAuthSuccess()) {
            reportVideoState(1, this.mVideo == null ? null : this.mVideo.getAlbum());
        }
    }

    public void onPaused() {
        if (OpenApiManager.instance().isAuthSuccess()) {
            reportVideoState(5, this.mVideo == null ? null : this.mVideo.getAlbum());
        }
    }

    public void onCompleted() {
        if (OpenApiManager.instance().isAuthSuccess()) {
            reportVideoState(6, this.mVideo == null ? null : this.mVideo.getAlbum());
        }
    }

    public void onStopping() {
        if (OpenApiManager.instance().isAuthSuccess()) {
            reportVideoState(7, this.mVideo == null ? null : this.mVideo.getAlbum());
        }
    }

    public void onStopped() {
        if (OpenApiManager.instance().isAuthSuccess()) {
            reportVideoState(2, this.mVideo == null ? null : this.mVideo.getAlbum());
        }
    }

    public void onError() {
        if (OpenApiManager.instance().isAuthSuccess()) {
            reportVideoState(8, this.mVideo == null ? null : this.mVideo.getAlbum());
        }
    }

    private void reportVideoState(int state, Album album) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "reportVideoState(), state = " + state + ", album = " + album);
        }
        if (album != null) {
            Media media = OpenApiUtils.createSdkMedia(album);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "reportVideoState(), state = " + state + ", media = " + media);
            }
            OpenApiManager.instance().getVideoPlayStateReporter().reportVideoState(state, media);
        }
    }
}
