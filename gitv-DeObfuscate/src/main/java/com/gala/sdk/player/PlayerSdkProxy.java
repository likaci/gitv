package com.gala.sdk.player;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.ViewParent;
import com.gala.sdk.player.Parameter.Keys;
import com.gala.sdk.player.PlayerSdk.OnInitializedListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerSdkProxy extends PlayerSdk {
    private Handler mLoadPluginHandler;
    private HandlerThread mLoadPluginThread = new HandlerThread("load-playerplugin");
    private PlayerSdk mPlayerSdk;
    private AtomicBoolean mWorkThreadStarted = new AtomicBoolean(false);

    private class InitRunnable implements Runnable {
        private Context mContext;
        private OnInitializedListener mListener;
        private Parameter mParameter;

        class C01671 implements ISdkError {
            C01671() {
            }

            public String getUniqueId() {
                return null;
            }

            public String getString() {
                return null;
            }

            public String toUniqueCode() {
                return null;
            }

            public String getServerMessage() {
                return null;
            }

            public String getServerCode() {
                return null;
            }

            public int getModule() {
                return 0;
            }

            public String getMessage() {
                return "load plugin fialed.";
            }

            public String getHttpMessage() {
                return null;
            }

            public int getHttpCode() {
                return 0;
            }

            public String getExtra2() {
                return null;
            }

            public String getExtra1() {
                return null;
            }

            public int getCode() {
                return 0;
            }

            public String getBacktrace() {
                return null;
            }
        }

        public InitRunnable(Context context, Parameter params, OnInitializedListener listener) {
            this.mContext = context;
            this.mParameter = params;
            this.mListener = listener;
        }

        public void run() {
            PlayerSdkProvider.getInstance().initialize(this.mContext, this.mParameter);
            PlayerSdkProxy.this.mPlayerSdk = PlayerSdkProvider.getInstance().getPlayerSdkInstance();
            if (PlayerSdkProxy.this.mPlayerSdk != null) {
                PlayerSdkProxy.this.mPlayerSdk.initialize(this.mContext, this.mParameter, this.mListener);
            } else if (this.mListener != null) {
                this.mListener.onFailed(new C01671());
            }
        }
    }

    public void initialize(Context context, Parameter params, OnInitializedListener listener) {
        initWorkThread();
        this.mLoadPluginHandler.postDelayed(new InitRunnable(context, params, listener), params != null ? (long) params.getInt32(Keys.I_DELAY_INIT_MS) : 0);
    }

    public IMediaPlayer createPlayer(Parameter params) {
        return this.mPlayerSdk != null ? this.mPlayerSdk.createPlayer(params) : null;
    }

    public void release() {
        if (this.mPlayerSdk != null) {
            this.mPlayerSdk.release();
        }
        this.mLoadPluginThread.quit();
    }

    public String getVersion() {
        return this.mPlayerSdk != null ? this.mPlayerSdk.getVersion() : "";
    }

    public IVideoOverlay createVideoOverlay(ViewParent parent, VideoSurfaceView view) {
        return this.mPlayerSdk != null ? this.mPlayerSdk.createVideoOverlay(parent, view) : null;
    }

    public IVideoOverlay createVideoOverlay(ViewParent parent) {
        return this.mPlayerSdk != null ? this.mPlayerSdk.createVideoOverlay(parent) : null;
    }

    public IMedia correctMedia(IMedia media) {
        return this.mPlayerSdk != null ? this.mPlayerSdk.correctMedia(media) : null;
    }

    public IMediaProfile getMediaProfile() {
        return this.mPlayerSdk != null ? this.mPlayerSdk.getMediaProfile() : null;
    }

    public AdCacheManager getAdCacheManager() {
        return this.mPlayerSdk != null ? this.mPlayerSdk.getAdCacheManager() : null;
    }

    public void invokeParams(int invokeType, Parameter parameter) {
        if (this.mPlayerSdk != null) {
            this.mPlayerSdk.invokeParams(invokeType, parameter);
        }
    }

    public AccountManager getAccountManager() {
        return this.mPlayerSdk != null ? this.mPlayerSdk.getAccountManager() : null;
    }

    public FeedBackManager getFeedBackManager() {
        return this.mPlayerSdk != null ? this.mPlayerSdk.getFeedBackManager() : null;
    }

    public DataManager getDataManager() {
        return this.mPlayerSdk != null ? this.mPlayerSdk.getDataManager() : null;
    }

    private synchronized void initWorkThread() {
        if (!this.mWorkThreadStarted.get()) {
            try {
                this.mLoadPluginThread.setPriority(10);
            } catch (Throwable th) {
            }
            this.mLoadPluginThread.start();
            this.mLoadPluginHandler = new Handler(this.mLoadPluginThread.getLooper());
            this.mWorkThreadStarted.set(true);
        }
    }
}
