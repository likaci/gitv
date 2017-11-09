package com.gala.video.app.player.ui.overlay;

import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.C2;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.NOW_C2;
import com.gala.pingback.PingbackStore.NOW_QPID;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.R;
import com.gala.pingback.PingbackStore.RPAGE;
import com.gala.pingback.PingbackStore.RSEAT;
import com.gala.pingback.PingbackStore.STATE;
import com.gala.sdk.player.BaseAdData;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.IAdDataProvider;
import com.gala.sdk.player.ITip;
import com.gala.sdk.player.ITrunkAdController;
import com.gala.sdk.player.OnUserBitStreamChangeListener;
import com.gala.sdk.player.OnUserChangeVideoRatioListener;
import com.gala.sdk.player.OnUserPlayPauseListener;
import com.gala.sdk.player.OnUserSkipHeadTailChangeListener;
import com.gala.sdk.player.OnUserVideoChangeListener;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.VideoSurfaceView;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.ui.OnPageAdvertiseStateChangeListener;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.pingback.PingbackCommonFieldUtils;
import com.gala.video.app.player.ui.IScreenUISwitcher;
import com.gala.video.app.player.ui.overlay.panels.AbsMenuPanel;
import com.gala.video.app.player.ui.overlay.panels.PlayerErrorPanel;
import com.gala.video.app.player.ui.widget.AbsFullScreenHint;
import com.gala.video.app.player.ui.widget.GalaPlayerView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.DlnaKeyEvent;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.ISuperPlayerOverlay;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsPlayerOverlay implements ISuperPlayerOverlay {
    protected static final int ADPLAYING = 1004;
    protected static final int DEFAULT = 1002;
    protected static final int PAUSE = 1001;
    protected static final int PLAYING = 1000;
    private static final String RPAGE_DEFAULT = "player";
    private static final String RPAGE_UGCTHROW = "ugcplayer";
    private static final String SEAT_KEY_BACK = "back";
    private static final String SEAT_KEY_DOWN = "down";
    private static final String SEAT_KEY_LEFT = "left";
    private static final String SEAT_KEY_MENU = "menu";
    private static final String SEAT_KEY_OK = "ok";
    private static final String SEAT_KEY_RIGHT = "right";
    private static final String SEAT_KEY_UP = "up";
    private static final String SEAT_KEY_VOLDOWN = "voldown";
    private static final String SEAT_KEY_VOLUP = "volup";
    protected static final int STOP = 1003;
    private static final String TAG = "Player/Ui/AbsPlayerOverlay";
    protected ITrunkAdController mAdController;
    protected IAdDataProvider mAdProdiver = new IAdDataProvider() {
        public void setAdData(int type, BaseAdData baseAdData) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(AbsPlayerOverlay.TAG, "setAdData()");
            }
            if (AbsPlayerOverlay.this.mMenuPanel != null) {
                AbsPlayerOverlay.this.mMenuPanel.setAdData(baseAdData);
            }
        }
    };
    protected OnUserBitStreamChangeListener mBitStreamListener = new OnUserBitStreamChangeListener() {
        public void onBitStreamChange(View view, BitStream bitStream) {
            AbsPlayerOverlay.this.notifyBitStreamSelected(view, bitStream);
        }

        public void onHDRToggleChanged(View view, boolean isOpen) {
            AbsPlayerOverlay.this.notifyHDRToggleChanged(view, isOpen);
        }
    };
    protected OnUserChangeVideoRatioListener mChangeVideoRatioListener = new OnUserChangeVideoRatioListener() {
        public void onVideoRatioChange(int ratio) {
            AbsPlayerOverlay.this.notifyVideoRatioSelected(ratio);
        }
    };
    protected Context mContext;
    protected PlayerErrorPanel mErrorPanel;
    protected AbsFullScreenHint mFullScreenHint;
    protected Handler mHandler;
    protected boolean mIsFullScreenMode = false;
    protected boolean mIsNetworkConnected = true;
    protected AbsMenuPanel mMenuPanel;
    protected OnAdStateListener mOnAdStateListener = new OnAdStateListener() {
        public void onHide(int i, Object object) {
        }

        public void onShow(int i, Object object) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(AbsPlayerOverlay.TAG, "onShow():" + i);
            }
            if (AbsPlayerOverlay.this.mPageAdvertiseStateChangeListener != null) {
                AbsPlayerOverlay.this.mPageAdvertiseStateChangeListener.onPageAdShow(i, object);
            }
        }

        public void onRequest(int type) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(AbsPlayerOverlay.TAG, "onRequest()");
            }
            if (AbsPlayerOverlay.this.mAdController != null) {
                AbsPlayerOverlay.this.mAdController.requestAd(type, AbsPlayerOverlay.this.mAdProdiver);
            }
        }
    };
    private OnUserChangeVideoRatioListener mOutUserChangeVideoRatioListener;
    private OnUserBitStreamChangeListener mOuterBitStreamListener;
    private OnUserSkipHeadTailChangeListener mOuterSkipHeadTailListener;
    private OnUserVideoChangeListener mOuterVideoChangeListener;
    private OnPageAdvertiseStateChangeListener mPageAdvertiseStateChangeListener;
    protected GalaPlayerView mPlayerView;
    protected ArrayList<IScreenUISwitcher> mScreenSwitchers;
    protected OnUserSkipHeadTailChangeListener mSkipHeadTailListener = new OnUserSkipHeadTailChangeListener() {
        public void onSkipChange(View view, boolean skip) {
            AbsPlayerOverlay.this.notifySkipHeadAndTail(view, skip);
        }
    };
    protected int mState;
    protected TipWrapper mTip;
    private TipUIHelper mTipHelper;
    protected IVideo mVideo;
    protected OnUserVideoChangeListener mVideoChangeListener = new OnUserVideoChangeListener() {
        public void onVideoChange(View view, IVideo movie) {
            AbsPlayerOverlay.this.notifyVideoChange(view, movie);
        }
    };

    protected abstract String getBlock(boolean z);

    protected abstract String getSubRSeatByKeyEvent(KeyEvent keyEvent);

    protected void initOverlay(GalaPlayerView playerView) {
        this.mHandler = new Handler();
        this.mContext = playerView.getContext();
        this.mErrorPanel = playerView.getPlayerErrorPanel();
        this.mMenuPanel = playerView.getMenuPanel();
        this.mMenuPanel.setOnUserVideoChangeListener(this.mVideoChangeListener);
        this.mMenuPanel.setOnUserBitStreamChangeListener(this.mBitStreamListener);
        this.mMenuPanel.setOnUserSkipHeaderTailChangeListener(this.mSkipHeadTailListener);
        this.mMenuPanel.setOnUserSetDisplayModeListener(this.mChangeVideoRatioListener);
        this.mMenuPanel.setAdStateListener(this.mOnAdStateListener);
        this.mMenuPanel.hide();
        this.mFullScreenHint = this.mPlayerView.getFullScreenHint();
        this.mScreenSwitchers = new ArrayList();
        this.mTipHelper = new TipUIHelper(this.mContext);
    }

    public void setCurrentVideo(IVideo video) {
        if (video != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "setVideo(" + video + ")");
            }
            IVideo oldVideo = this.mVideo;
            this.mVideo = video;
            this.mMenuPanel.setVideo(video);
            this.mTipHelper.setVideo(video);
            if (!video.equalVideo(oldVideo)) {
                clearAd();
                this.mTipHelper.setAdData(null);
            }
            if (video.getSourceType() == SourceType.CAROUSEL && oldVideo != null && video.getLiveChannelId() != oldVideo.getLiveChannelId()) {
                clearAd();
                this.mTipHelper.setAdData(null);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setMovie, movie is null, return");
        }
    }

    public boolean isInFullScreenMode() {
        return this.mIsFullScreenMode;
    }

    public void menuPanelEnableShow(boolean enable) {
        this.mMenuPanel.enableShow(enable);
    }

    public boolean onDlnaKeyEvent(DlnaKeyEvent event, KeyKind key) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onDLNAKeyEvent: {" + event + "}, {" + key + "}");
        }
        return this.mMenuPanel.onDlnaKeyEvent(event, key);
    }

    public void notifyVideoDataChanged(int dataType) {
    }

    public boolean isMenuPanelShowing() {
        return this.mMenuPanel.isShown();
    }

    public void clearError() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "clearError()");
        }
        this.mErrorPanel.hide();
    }

    public void onNetworkChange(int netState) {
        if (netState == 1 || netState == 2) {
            this.mIsNetworkConnected = true;
        } else {
            this.mIsNetworkConnected = false;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onNetworkChange: network connected=" + this.mIsNetworkConnected);
        }
        if (!this.mIsNetworkConnected) {
            this.mMenuPanel.hide();
        }
    }

    public void onDestroy() {
        this.mFullScreenHint.clearBackgroundBitmap();
    }

    public void setOnUserVideoChangeListener(OnUserVideoChangeListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setOnUserVideoChangeListener(" + listener + ")");
        }
        this.mOuterVideoChangeListener = listener;
    }

    protected void notifyVideoChange(View view, IVideo movie) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifyVideoChange(" + movie + ")");
        }
        if (this.mOuterVideoChangeListener != null) {
            this.mOuterVideoChangeListener.onVideoChange(view, movie);
        }
    }

    public void setOnUserBitStreamChangeListener(OnUserBitStreamChangeListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setOnUserDefinitionChangeListener(" + listener + ")");
        }
        this.mOuterBitStreamListener = listener;
    }

    protected void notifyBitStreamSelected(View view, BitStream bitStream) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifyBitStreamSelected(" + view + ", " + bitStream + ")");
        }
        if (this.mOuterBitStreamListener != null) {
            this.mOuterBitStreamListener.onBitStreamChange(view, bitStream);
        }
    }

    protected void notifyHDRToggleChanged(View view, boolean isOpen) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifyHDRToggleChanged(" + view + ", " + isOpen + ")");
        }
        if (this.mOuterBitStreamListener != null) {
            this.mOuterBitStreamListener.onHDRToggleChanged(view, isOpen);
        }
    }

    public void setOnUserSkipHeadTailChangeListener(OnUserSkipHeadTailChangeListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setOnUserSkipHeadTailChangeListener(" + listener + ")");
        }
        this.mOuterSkipHeadTailListener = listener;
    }

    protected void notifySkipHeadAndTail(View view, boolean skip) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifySkipHeadAndTail(" + view + ", " + skip + ")");
        }
        if (this.mOuterSkipHeadTailListener != null) {
            this.mOuterSkipHeadTailListener.onSkipChange(view, skip);
        }
    }

    public void setOnUserChangeVideoRatioListener(OnUserChangeVideoRatioListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setOnUserSetDisplayModeListner(" + listener + ")");
        }
        this.mOutUserChangeVideoRatioListener = listener;
    }

    protected void notifyVideoRatioSelected(int ratio) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifyVideoRatioSelected(" + ratio + ")");
        }
        if (this.mOutUserChangeVideoRatioListener != null) {
            this.mOutUserChangeVideoRatioListener.onVideoRatioChange(ratio);
        }
    }

    public void updateSkipHeadAndTail(boolean isSkipTail) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "updateSkipHeadAndTail(" + isSkipTail + ")");
        }
        this.mMenuPanel.updateSkipHeadAndTail(isSkipTail);
    }

    public void updateBitStream(List<BitStream> list, BitStream bitStream) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "updateBitStream(" + bitStream + ", list " + list + ")");
        }
        this.mMenuPanel.updateBitStream(list, bitStream);
    }

    public VideoSurfaceView getVideoSurfaceView() {
        return this.mPlayerView.getVideoView();
    }

    protected void sendKeyEventPingback(KeyEvent event) {
        if (event.getAction() != 1 && event.getRepeatCount() <= 0) {
            if (this.mVideo != null) {
                String resouceId = "";
                String qpid = "";
                resouceId = this.mVideo.getTvId();
                qpid = this.mVideo.getAlbumId();
                CharSequence rblock = getBlock(false);
                if (!StringUtils.isEmpty(rblock)) {
                    CharSequence rseat = getRSeatByKeyEvent(event);
                    if (!StringUtils.isEmpty(rseat)) {
                        String rpage = "player";
                        if (null != null) {
                            rpage = RPAGE_UGCTHROW;
                        }
                        PingbackFactory.instance().createPingback(27).addItem(R.R_TYPE(resouceId)).addItem(BLOCK.BLOCK_TYPE(rblock)).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(rseat)).addItem(RPAGE.RPAGE_ID(rpage)).addItem(C1.C1_TYPE(getC1())).addItem(C2.C2_TYPE(getC2())).addItem(NOW_C1.NOW_C1_TYPE(getC1())).addItem(NOW_QPID.NOW_QPID_TYPE(qpid)).addItem(NOW_C2.NOW_C2_TYPE(getC2())).addItem(STATE.STATE_TYPE(getState())).post();
                    } else if (LogUtils.mIsDebug) {
                        LogUtils.d(TAG, "<<onKeyEvent rseat is invalid");
                    }
                } else if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "<<onKeyEvent invalid block");
                }
            } else if (LogUtils.mIsDebug) {
                LogUtils.e(TAG, "sendKeyEventPingback mCurrentVideo is null");
            }
        }
    }

    protected String getC1() {
        String c1 = "";
        Album album = this.mVideo.getAlbum();
        SourceType type = this.mVideo.getSourceType();
        if (type == SourceType.CAROUSEL) {
            return "101221";
        }
        if (type == SourceType.LIVE) {
            if (this.mVideo.isTrailer()) {
                return String.valueOf(album.chnId);
            }
            return "101221";
        } else if (album == null || album.chnId == 0) {
            return c1;
        } else {
            return String.valueOf(album.chnId);
        }
    }

    protected String getC2() {
        SourceType type = this.mVideo.getSourceType();
        boolean isTrailer = this.mVideo.isTrailer();
        String c2 = "";
        if (type == SourceType.LIVE) {
            if (!isTrailer) {
                c2 = this.mVideo.getLiveChannelId();
            }
        } else if (type == SourceType.CAROUSEL) {
            c2 = this.mVideo.getLiveChannelId();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getC2() :" + c2);
        }
        return c2;
    }

    private String getState() {
        return PingbackCommonFieldUtils.getStateField(this.mVideo);
    }

    private String getRSeatByKeyEvent(KeyEvent event) {
        String rseat = "";
        switch (event.getKeyCode()) {
            case 4:
                rseat = "back";
                break;
            case 19:
                rseat = "up";
                break;
            case 20:
                rseat = "down";
                break;
            case 21:
                rseat = "left";
                break;
            case 22:
                rseat = "right";
                break;
            case 23:
                rseat = "ok";
                break;
            case 24:
                rseat = "volup";
                break;
            case 25:
                rseat = "voldown";
                break;
            case 82:
                rseat = "menu";
                break;
        }
        if (getSubRSeatByKeyEvent(event) != null) {
            return getSubRSeatByKeyEvent(event);
        }
        return rseat;
    }

    public void changeParent(ViewGroup arg0) {
    }

    public void setAdController(ITrunkAdController adController) {
        this.mAdController = adController;
    }

    public void showTip(ITip origintip) {
        if (!(this.mTipHelper == null || origintip == null)) {
            if (this.mAdController != null) {
                this.mTipHelper.setAdData(this.mAdController.getAdData(1));
            }
            this.mTip = this.mTipHelper.decorateTip(new TipWrapper(origintip));
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showTip:" + (this.mTip == null ? null : this.mTip.toString()));
        }
    }

    protected void setAdData() {
        if (this.mAdController != null) {
            this.mMenuPanel.setAdData(this.mAdController.getAdData(2));
        }
    }

    protected void clearAd() {
    }

    public void setOnPageAdvertiseStateChangeListener(OnPageAdvertiseStateChangeListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setOnPageAdvertiseStateChangeListener():" + listener);
        }
        this.mPageAdvertiseStateChangeListener = listener;
    }

    public boolean handleJsKeyEvent(KeyEvent event) {
        if (this.mAdController == null || getClickThroughAdType() != 100) {
            return false;
        }
        switch (event.getKeyCode()) {
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 66:
                if (!this.mAdController.handleJsKeyEvent(event)) {
                    return false;
                }
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "handleJsKeyEvent: handled");
                }
                return true;
            default:
                return false;
        }
    }

    public int getClickThroughAdType() {
        int type = -1;
        if (this.mAdController != null) {
            List adTypes = this.mAdController.getShowThroughType();
            if (!ListUtils.isEmpty(adTypes)) {
                type = ((Integer) adTypes.get(0)).intValue();
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getClickThroughAdType:" + type);
        }
        return type;
    }

    public void setOnUserPlayPauseListener(OnUserPlayPauseListener onUserPlayPauseListener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setOnUserPlayPauseListener()" + onUserPlayPauseListener);
        }
        this.mMenuPanel.setOnUserPlayPauseListener(onUserPlayPauseListener);
    }
}
