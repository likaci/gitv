package com.gala.sdk.player;

import android.content.Context;
import android.view.ViewParent;

public abstract class PlayerSdk {
    private static PlayerSdk sInstance;

    public interface OnInitializedListener {
        void onFailed(ISdkError iSdkError);

        void onSuccess();
    }

    public abstract IMedia correctMedia(IMedia iMedia);

    public abstract IMediaPlayer createPlayer(Parameter parameter);

    public abstract IVideoOverlay createVideoOverlay(ViewParent viewParent);

    public abstract IVideoOverlay createVideoOverlay(ViewParent viewParent, VideoSurfaceView videoSurfaceView);

    public abstract AccountManager getAccountManager();

    public abstract AdCacheManager getAdCacheManager();

    public abstract DataManager getDataManager();

    public abstract FeedBackManager getFeedBackManager();

    public abstract IMediaProfile getMediaProfile();

    public abstract String getVersion();

    public abstract void initialize(Context context, Parameter parameter, OnInitializedListener onInitializedListener);

    public abstract void invokeParams(int i, Parameter parameter);

    public abstract void release();

    public static synchronized PlayerSdk getInstance() {
        PlayerSdk playerSdk;
        synchronized (PlayerSdk.class) {
            if (sInstance == null) {
                sInstance = new PlayerSdkProxy();
            }
            playerSdk = sInstance;
        }
        return playerSdk;
    }
}
