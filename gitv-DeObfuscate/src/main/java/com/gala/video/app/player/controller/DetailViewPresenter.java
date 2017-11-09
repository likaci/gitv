package com.gala.video.app.player.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.data.IVideo;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumVideoItem;
import com.gala.video.app.player.albumdetail.data.DetailActionPolicy;
import com.gala.video.app.player.albumdetail.data.DetailAllViewActionPolicy;
import com.gala.video.app.player.albumdetail.data.DetailPageManage;
import com.gala.video.app.player.albumdetail.ui.IDetailOverlay;
import com.gala.video.app.player.data.DetailConstants;
import com.gala.video.app.player.data.DetailDataProvider;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.OnNetStateChangedListener;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import java.lang.ref.WeakReference;
import java.util.List;

public class DetailViewPresenter {
    private final String TAG = ("Detail/Controller/DetailViewPresenter@" + Integer.toHexString(hashCode()));
    private UcenterActivityEventListener mActivityEventListener = new UcenterActivityEventListener();
    private final OnNetStateChangedListener mAlbumNetStateChangedListener = new C13633();
    private Context mContext;
    private ScreenMode mCurrentScreenMode = ScreenMode.WINDOWED;
    private IDataCallback mDataCallback = new C13611();
    private DetailDataProvider mDataProvider;
    private DetailPageManage mDetailPageManage;
    private Intent mIntent;
    private boolean mIsOnInit = true;
    private Handler mMainHandler = new Handler(Looper.myLooper());
    private IUIEventListener mUIEventListener = new C13622();
    private IDetailOverlay mView;

    class C13611 implements IDataCallback {
        C13611() {
        }

        public void onDataReady(int msg, Object data) {
            if (DetailViewPresenter.this.mView == null) {
                LogRecordUtils.logd(DetailViewPresenter.this.TAG, "mDataCallback.onDataReady msg= " + msg + " but the mView is null");
                return;
            }
            LogRecordUtils.logd(DetailViewPresenter.this.TAG, "mDataCallback.onDataReady msg=" + msg);
            switch (msg) {
                case 1:
                    DetailViewPresenter.this.mView.showLoading();
                    return;
                case 2:
                    DetailViewPresenter.this.mView.showError(data);
                    return;
                case 4:
                    ((AlbumVideoItem) data).setDataProvider(DetailViewPresenter.this.mDataProvider);
                    DetailViewPresenter.this.mView.notifyVideoDataCreated((AlbumVideoItem) data);
                    return;
                case 5:
                    DetailViewPresenter.this.mView.updateBasicInfo((AlbumInfo) data);
                    DetailViewPresenter.this.updateCachedAlbum(data);
                    return;
                case 6:
                    DetailViewPresenter.this.mView.updateVIPInfo((AlbumInfo) data);
                    return;
                case 7:
                    DetailViewPresenter.this.mView.updateFavInfo((AlbumInfo) data);
                    return;
                case 8:
                    DetailViewPresenter.this.mView.updateBasicInfo((AlbumInfo) data);
                    DetailViewPresenter.this.updateCachedAlbum(data);
                    return;
                case 9:
                    DetailViewPresenter.this.mView.updateEpisodeList((AlbumInfo) data);
                    return;
                case 10:
                    DetailViewPresenter.this.mView.updateEpisodeList((AlbumInfo) data);
                    return;
                case 11:
                    DetailViewPresenter.this.mView.updateEpisodeList((AlbumInfo) data);
                    return;
                case 12:
                case 13:
                case 14:
                    return;
                case 17:
                    DetailViewPresenter.this.mView.updateEpisodeList((AlbumInfo) data);
                    return;
                case 19:
                    DetailViewPresenter.this.mView.notifyVideoSwitched((IVideo) data);
                    return;
                case 20:
                    DetailViewPresenter.this.mDataProvider.resumeLoad();
                    return;
                case 21:
                    DetailViewPresenter.this.mView.updateTvod((AlbumInfo) data);
                    return;
                case 22:
                    DetailViewPresenter.this.mView.updateCoupon((AlbumInfo) data);
                    return;
                default:
                    LogRecordUtils.logd(DetailViewPresenter.this.TAG, "mDataCallback.onDataReady, unhandled msg=" + msg);
                    return;
            }
        }
    }

    class C13622 implements IUIEventListener {
        C13622() {
        }

        public void onEvent(int eventType, Object data) {
            LogRecordUtils.logd(DetailViewPresenter.this.TAG, "mUIEventListener.onEvent, eventType=" + eventType);
            switch (eventType) {
                case 1:
                case 3:
                case 4:
                    return;
                case 2:
                    DetailViewPresenter.this.mView.showFullDescPanel((AlbumInfo) data);
                    return;
                case 5:
                    DetailViewPresenter.this.mDataProvider.updateFavState(((Boolean) data).booleanValue());
                    return;
                case 6:
                    ScreenMode mode = (ScreenMode) data;
                    DetailViewPresenter.this.mView.notifyScreenModeSwitched(mode, false);
                    DetailViewPresenter.this.updateScreenMode(mode);
                    return;
                case 7:
                    DetailViewPresenter.this.mView.notifyVideoSwitched((IVideo) data);
                    DetailViewPresenter.this.mDataProvider.switchLoad((IVideo) data, false);
                    return;
                case 8:
                    DetailViewPresenter.this.mView.notifyVideoPlayFinished();
                    return;
                case 9:
                    ScreenMode modeOnError = (ScreenMode) data;
                    DetailViewPresenter.this.mView.notifyScreenModeSwitched(modeOnError, true);
                    DetailViewPresenter.this.updateScreenMode(modeOnError);
                    return;
                case 10:
                    DetailViewPresenter.this.mView.updateAlbumDetailTotally((IVideo) data);
                    DetailViewPresenter.this.mDataProvider.switchLoad((IVideo) data, true);
                    return;
                case 11:
                    DetailViewPresenter.this.gotoLogin();
                    return;
                case 12:
                    DetailViewPresenter.this.mView.updateAlbumDetailTrailers((IVideo) data);
                    return;
                case 13:
                    DetailViewPresenter.this.mView.updateAlbumDetailTrailers((IVideo) data);
                    DetailViewPresenter.this.mView.clearAlbumListDefaultSelectedTextColor();
                    return;
                default:
                    LogRecordUtils.logd(DetailViewPresenter.this.TAG, "mUIEventListener.onEvent, unhandled eventType=" + eventType);
                    return;
            }
        }
    }

    class C13633 implements OnNetStateChangedListener {
        C13633() {
        }

        public void onStateChanged(int oldState, int newState) {
            switch (newState) {
                case 1:
                case 2:
                    if (oldState != newState) {
                        DetailViewPresenter.this.mDataProvider.loadOnNetworkChanged();
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(DetailViewPresenter.this.TAG, "onNetworkState- change -state" + oldState + " >>> newState " + newState);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    static class ClearRunnable implements Runnable {
        WeakReference<DetailPageManage> mOuter;

        public ClearRunnable(DetailPageManage outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void run() {
            DetailPageManage outer = (DetailPageManage) this.mOuter.get();
            if (outer != null) {
                LogRecordUtils.logd("DetailViewPresenter", "clear and release uikit");
                outer.pageStop();
                outer.pageDestroy();
            }
        }
    }

    private class UcenterActivityEventListener extends AbsActivityEventListener {
        UcenterActivityEventListener() {
        }

        public void onStarted() {
            super.onStarted();
            DetailViewPresenter.this.mView.onActivityStarted();
        }

        public void onResumed(int resultCode) {
            LogRecordUtils.logd(DetailViewPresenter.this.TAG, ">> onResumed" + DetailViewPresenter.this.mIsOnInit);
            super.onResumed(resultCode);
            if (DetailViewPresenter.this.mIsOnInit) {
                DetailViewPresenter.this.mIsOnInit = false;
            } else {
                DetailViewPresenter.this.mDataProvider.resumeLoad();
            }
            DetailViewPresenter.this.mView.onActivityResumed(resultCode);
        }

        public void onFinishing() {
            LogRecordUtils.logd(DetailViewPresenter.this.TAG, ">> onFinishing");
            super.onFinishing();
            DataDispatcher.instance().unregister(DetailViewPresenter.this.mContext);
            UIEventDispatcher.instance().unregister(DetailViewPresenter.this.mContext);
            NetWorkManager.getInstance().unRegisterStateChangedListener(DetailViewPresenter.this.mAlbumNetStateChangedListener);
            DetailViewPresenter.this.mDataProvider.stopLoad();
            DetailViewPresenter.this.mDataProvider.release();
            DetailViewPresenter.this.mView.onActivityFinishing();
            DetailViewPresenter.this.mDataProvider = null;
            DetailViewPresenter.this.mView = null;
            DetailViewPresenter.this.mMainHandler.removeCallbacksAndMessages(null);
            DetailViewPresenter.this.mDetailPageManage.pageStop();
            DetailViewPresenter.this.mDetailPageManage.pageDestroy();
            ActivityEventDispatcher.instance().unregister(DetailViewPresenter.this.mContext);
        }

        public void onPaused() {
            LogRecordUtils.logd(DetailViewPresenter.this.TAG, ">> onPaused");
            super.onPaused();
            DetailViewPresenter.this.mView.onActivityPaused();
        }

        public void onStopped() {
            super.onStopped();
            DetailViewPresenter.this.mView.onActivityStopped();
            LogRecordUtils.logd(DetailViewPresenter.this.TAG, ">> onStopped");
        }
    }

    public DetailViewPresenter(Context context, IDetailOverlay view, Intent intent) {
        this.mView = view;
        this.mIntent = intent;
        this.mContext = context;
    }

    public void initialize() {
        this.mDetailPageManage = new DetailPageManage(this.mContext, this.mView);
        this.mDetailPageManage.initialize();
        this.mDetailPageManage.setActionPolicy(new DetailActionPolicy(this.mDetailPageManage, this.mView));
        this.mDetailPageManage.setAllViewActionPolicy(new DetailAllViewActionPolicy(this.mDetailPageManage, this.mView));
        this.mDataProvider = new DetailDataProvider(this.mContext, this.mDetailPageManage, this.mIntent);
        UIEventDispatcher.instance().register(this.mContext, this.mUIEventListener);
        ActivityEventDispatcher.instance().register(this.mContext, this.mActivityEventListener);
        DataDispatcher.instance().register(this.mContext, this.mDataCallback);
        this.mDataProvider.initialize();
        NetWorkManager.getInstance().registerStateChangedListener(this.mAlbumNetStateChangedListener);
    }

    public IVideo getCurrentVideo() {
        return this.mDataProvider != null ? this.mDataProvider.getCurrentVideo() : null;
    }

    public List<AbsVoiceAction> getSupportedVoices(List<AbsVoiceAction> actions) {
        if (this.mView != null) {
            return this.mView.getSupportedVoices(actions);
        }
        return actions;
    }

    private void gotoLogin() {
        LogRecordUtils.logd(this.TAG, "gotoLogin");
        QToast.makeTextAndShow(this.mContext, this.mContext.getResources().getText(C1291R.string.album_detail_login_toast), 5000);
        GetInterfaceTools.getLoginProvider().startLoginActivity(this.mContext, LoginConstant.S1_FROM_FAVORITEBTN, 4, 1);
    }

    private void updateCachedAlbum(Object data) {
        if (data instanceof AlbumInfo) {
            DetailConstants.sCacheAlbum = ((AlbumInfo) data).getAlbum();
        } else {
            LogRecordUtils.logd(this.TAG, "updateCachedAlbum, data is not a IVideo type.");
        }
    }

    private void updateScreenMode(ScreenMode mode) {
        LogRecordUtils.logd(this.TAG, ">> updateScreenMode, mode=" + mode);
        this.mCurrentScreenMode = mode;
    }
}
