package com.gala.video.lib.share.uikit.loader;

import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.gala.video.lib.share.uikit.cache.UikitDataCache;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.utils.MemoryLevelInfo;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

abstract class BaseUikitDataLoader implements IUikitDataLoader {
    protected static final String TAG = "UikitDataLoader";
    protected static boolean mIsLowMemoryDevice;
    private static HandlerThread[] mLoaderThread;
    protected String mAlbumid = "";
    protected int mBannerAdId = -1;
    protected List<CardInfoModel> mBannerAds;
    protected UikitDataCache mCache = UikitDataCache.getInstance();
    protected int mCacheType = 0;
    protected int mChannelId = 0;
    protected int mCurrentAddPageNo = 0;
    protected boolean mIsNeedUpdate = false;
    protected boolean mIsVipLoader = false;
    protected LoaderHandler mLoaderHandler;
    protected HandlerThread mLoaderThread3 = null;
    protected String mSourceId;
    protected String mTvQid = "";
    protected int mUikitEngineId;
    protected List<CardInfoModel> mUpdateCardInfoModelList;

    protected class LoaderHandler extends Handler {
        public LoaderHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            UikitEvent event = msg.obj;
            if (event != null) {
                BaseUikitDataLoader.this.invokeAction(event);
            }
        }
    }

    protected abstract void invokeAction(UikitEvent uikitEvent);

    static {
        mIsLowMemoryDevice = false;
        boolean z = Runtime.getRuntime().availableProcessors() <= 2 || MemoryLevelInfo.isLowMemoryDevice();
        mIsLowMemoryDevice = z;
        if (mIsLowMemoryDevice) {
            mLoaderThread = new HandlerThread[1];
            mLoaderThread[0] = new HandlerThread("UikitDataLoader-0");
            mLoaderThread[0].start();
            return;
        }
        mLoaderThread = new HandlerThread[2];
        mLoaderThread[0] = new HandlerThread("UikitDataLoader-0");
        mLoaderThread[0].start();
        mLoaderThread[1] = new HandlerThread("UikitDataLoader-1");
        mLoaderThread[1].start();
    }

    protected HandlerThread getHandlerThread(int index) {
        if (mIsLowMemoryDevice) {
            return mLoaderThread[0];
        }
        if (mLoaderThread.length == 2) {
            return mLoaderThread[index % 2];
        }
        return mLoaderThread[0];
    }

    public BaseUikitDataLoader(int cacheType, String sourceId, int uikitEngineId) {
        this.mCacheType = cacheType;
        this.mSourceId = sourceId;
        this.mUikitEngineId = uikitEngineId;
        this.mCache.addUikitEngineAndSourceId(uikitEngineId, sourceId);
    }

    public void register() {
        EventBus.getDefault().register(this);
    }

    public void onPostEvent(UikitEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onGetEvent(UikitEvent event) {
        if (event != null && event.uikitEngineId == this.mUikitEngineId) {
            Log.d(TAG, "onGetEvent: uikitengineid-" + event.uikitEngineId + "-event type-" + event.eventType);
            if (event.eventType == 17) {
                if (this.mCacheType == 0 && UikitDataCache.getInstance().getCurrentUikitEngineId() != this.mUikitEngineId) {
                    Log.d(TAG, "onGetEvent: uikitengineid-" + event.uikitEngineId + "-event type-" + event.eventType + "-uikitengine is not current");
                    return;
                } else if (event.cardInfoModel == null) {
                    Log.d(TAG, "onGetEvent: uikitengineid-" + event.uikitEngineId + "-event type-" + event.eventType + "-cardinfomodel is null");
                    return;
                } else if (this.mCurrentAddPageNo == event.cardInfoModel.mPageNo + 1) {
                    Log.d(TAG, "onGetEvent: UIKIT_ADD_CARDS- pageno-" + this.mCurrentAddPageNo);
                    return;
                }
            }
            UikitEvent event1 = new UikitEvent(event);
            Message msg = Message.obtain();
            msg.obj = event1;
            this.mLoaderHandler.sendMessage(msg);
        }
    }

    public void unregister() {
        Log.d(TAG, "unregister thread id = " + this.mLoaderThread3.getThreadId() + ", uikitengine id = " + this.mUikitEngineId);
        EventBus.getDefault().unregister(this);
        if (this.mCacheType == 3 || this.mCacheType == 1) {
            if (this.mLoaderHandler != null) {
                this.mLoaderHandler.removeCallbacksAndMessages(null);
                this.mLoaderHandler = null;
            }
            if (this.mLoaderThread3 != null) {
                this.mLoaderThread3.interrupt();
                if (VERSION.SDK_INT >= 18) {
                    this.mLoaderThread3.quitSafely();
                } else {
                    this.mLoaderThread3.quit();
                }
                this.mLoaderThread3 = null;
            }
        }
    }

    public void unRegisterThread3() {
        if (this.mLoaderHandler != null) {
            this.mLoaderHandler.removeCallbacksAndMessages(null);
            this.mLoaderHandler = null;
        }
        if (this.mLoaderThread3 != null) {
            this.mLoaderThread3.interrupt();
            if (VERSION.SDK_INT >= 18) {
                this.mLoaderThread3.quitSafely();
            } else {
                this.mLoaderThread3.quit();
            }
            this.mLoaderThread3 = null;
        }
    }

    public void setVipLoader(boolean isVip) {
        this.mIsVipLoader = isVip;
    }

    public void setBannerAdId(int id) {
        this.mBannerAdId = id;
    }

    public void setChannelId(int id) {
        this.mChannelId = id;
    }
}
