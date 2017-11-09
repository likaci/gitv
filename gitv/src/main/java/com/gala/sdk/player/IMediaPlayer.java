package com.gala.sdk.player;

import android.view.Surface;
import java.util.List;

public interface IMediaPlayer {
    public static final int AD_INFO_CLICKTHROUGH_AD_HIDE = 501;
    public static final int AD_INFO_CLICKTHROUGH_AD_SHOW = 500;
    public static final int AD_INFO_FRONT_AD_CHANGE = 200;
    public static final int AD_INFO_FUTURE_AD_INFO = 400;
    public static final int AD_INFO_ITEM = 100;
    public static final int AD_INFO_MIDDLE_AD_CHANGE = 300;
    public static final int AD_INFO_MIDDLE_AD_READY = 301;
    public static final int AD_INFO_MIDDLE_AD_SKIPPED = 302;
    public static final int AD_INFO_OVERLAY_LOGIN_SUCCESS = 800;
    public static final int AD_INFO_OVERLAY_PURCHASE_SUCCESS = 801;
    public static final int AD_INFO_QUESTIONNAIRE_AD_READY = 700;
    public static final int AD_INFO_TIP_CLICKED = 502;
    public static final int AD_INFO_VIP_NO_AD = 600;
    public static final int MEDIA_INFO_DEGRADE_BITSTREAM_DELAY = 100;

    public interface OnAdInfoListener {
        void onAdInfo(IMediaPlayer iMediaPlayer, int i, Object obj);
    }

    public interface OnBitStreamChangedListener {
        void OnBitStreamChanged(IMediaPlayer iMediaPlayer, IMedia iMedia, BitStream bitStream);

        void OnBitStreamChanging(IMediaPlayer iMediaPlayer, IMedia iMedia, BitStream bitStream, BitStream bitStream2);
    }

    public interface OnBitStreamInfoListener {
        void onBitStreamListUpdated(IMediaPlayer iMediaPlayer, IMedia iMedia, List<BitStream> list);

        void onBitStreamSelected(IMediaPlayer iMediaPlayer, IMedia iMedia, BitStream bitStream);
    }

    public interface OnBufferChangedListener {
        void onBufferEnd(IMediaPlayer iMediaPlayer, IMedia iMedia);

        void onBufferStarted(IMediaPlayer iMediaPlayer, IMedia iMedia);
    }

    public interface OnHeaderTailerInfoListener {
        void onHeaderTailerInfoReady(IMediaPlayer iMediaPlayer, IMedia iMedia, int i, int i2);
    }

    public interface OnInfoListener {
        void onInfo(IMediaPlayer iMediaPlayer, IMedia iMedia, int i, Object obj);
    }

    public interface OnPlayNextListener {
        void onPlayNext(IMediaPlayer iMediaPlayer, IMedia iMedia);
    }

    public interface OnPreviewInfoListener {
        void onPreviewInfoReady(IMediaPlayer iMediaPlayer, IMedia iMedia, int i, int i2);
    }

    public interface OnSeekChangedListener {
        void onSeekCompleted(IMediaPlayer iMediaPlayer, IMedia iMedia, int i);

        void onSeekStarted(IMediaPlayer iMediaPlayer, IMedia iMedia, int i);
    }

    public interface OnStateChangedListener {
        void onAdEnd(IMediaPlayer iMediaPlayer, IMedia iMedia, int i);

        void onAdStarted(IMediaPlayer iMediaPlayer, IMedia iMedia, int i, boolean z);

        void onCompleted(IMediaPlayer iMediaPlayer, IMedia iMedia);

        boolean onError(IMediaPlayer iMediaPlayer, IMedia iMedia, ISdkError iSdkError);

        void onPaused(IMediaPlayer iMediaPlayer, IMedia iMedia);

        void onPrepared(IMediaPlayer iMediaPlayer, IMedia iMedia);

        void onPreparing(IMediaPlayer iMediaPlayer, IMedia iMedia);

        void onSleeped(IMediaPlayer iMediaPlayer, IMedia iMedia);

        void onStarted(IMediaPlayer iMediaPlayer, IMedia iMedia, boolean z);

        void onStopped(IMediaPlayer iMediaPlayer, IMedia iMedia);

        void onStopping(IMediaPlayer iMediaPlayer, IMedia iMedia);

        void onWakeuped(IMediaPlayer iMediaPlayer, IMedia iMedia);
    }

    public interface OnSubtitleUpdateListener {
        void onSubtitleUpdate(IMediaPlayer iMediaPlayer, IMedia iMedia, String str);
    }

    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(IMediaPlayer iMediaPlayer, IMedia iMedia, int i, int i2);
    }

    public interface OnVideoStartRenderingListener {
        void onVideoStartRendering(IMediaPlayer iMediaPlayer, IMedia iMedia);
    }

    public static final class PlayerType {
        public static final int ANDROID = 1;
        public static final int LITCHI = 5;
        public static final int NATIVE = 2;
        public static final int UNKNOWN = 0;
    }

    void cancelBitStreamAutoDegrade();

    IAdController getAdController();

    int getAdCountDownTime();

    int getCachePercent();

    int getCurrentPosition();

    IMedia getDataSource();

    int getDuration();

    IMedia getNextDataSource();

    int getPlayerType();

    int getStoppedPosition();

    void invokeOperation(int i, Parameter parameter);

    boolean isAdPlaying();

    boolean isPaused();

    boolean isPlaying();

    boolean isSleeping();

    void pause();

    void prepareAsync();

    void release();

    void seekTo(long j);

    void setDataSource(IMedia iMedia);

    void setDisplay(Surface surface);

    void setDisplay(IVideoOverlay iVideoOverlay);

    void setNextDataSource(IMedia iMedia);

    void setOnAdInfoListener(OnAdInfoListener onAdInfoListener);

    void setOnBitStreamChangedListener(OnBitStreamChangedListener onBitStreamChangedListener);

    void setOnBitStreamInfoListener(OnBitStreamInfoListener onBitStreamInfoListener);

    void setOnBufferChangedListener(OnBufferChangedListener onBufferChangedListener);

    void setOnHeaderTailerInfoListener(OnHeaderTailerInfoListener onHeaderTailerInfoListener);

    void setOnInfoListener(OnInfoListener onInfoListener);

    void setOnPlayNextListener(OnPlayNextListener onPlayNextListener);

    void setOnPreviewInfoListener(OnPreviewInfoListener onPreviewInfoListener);

    void setOnSeekChangedListener(OnSeekChangedListener onSeekChangedListener);

    void setOnStateChangedListener(OnStateChangedListener onStateChangedListener);

    void setOnSubtitleUpdateListener(OnSubtitleUpdateListener onSubtitleUpdateListener);

    void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onVideoSizeChangedListener);

    void setOnVideoStartRenderingListener(OnVideoStartRenderingListener onVideoStartRenderingListener);

    void setSkipHeadAndTail(boolean z);

    void setVideoRatio(int i);

    void sleep();

    void start();

    void stop();

    void switchBitStream(BitStream bitStream);

    void wakeUp();
}
