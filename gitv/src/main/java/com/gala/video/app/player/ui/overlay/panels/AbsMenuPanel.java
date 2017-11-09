package com.gala.video.app.player.ui.overlay.panels;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore.AID;
import com.gala.pingback.PingbackStore.ALBUMLIST;
import com.gala.pingback.PingbackStore.AREA;
import com.gala.pingback.PingbackStore.BKT;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.C2;
import com.gala.pingback.PingbackStore.CID;
import com.gala.pingback.PingbackStore.EVENTID;
import com.gala.pingback.PingbackStore.ISPREVUE;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.NOW_C2;
import com.gala.pingback.PingbackStore.NOW_EPISODE;
import com.gala.pingback.PingbackStore.NOW_QPID;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.PPUID;
import com.gala.pingback.PingbackStore.R;
import com.gala.pingback.PingbackStore.RANK;
import com.gala.pingback.PingbackStore.RPAGE;
import com.gala.pingback.PingbackStore.RSEAT;
import com.gala.pingback.PingbackStore.SOURCE;
import com.gala.pingback.PingbackStore.TAID;
import com.gala.pingback.PingbackStore.TCID;
import com.gala.sdk.player.BaseAdData;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.OnUserBitStreamChangeListener;
import com.gala.sdk.player.OnUserChangeVideoRatioListener;
import com.gala.sdk.player.OnUserPlayPauseListener;
import com.gala.sdk.player.OnUserSkipHeadTailChangeListener;
import com.gala.sdk.player.OnUserVideoChangeListener;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ContentType;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.config.style.IEpisodeListUIStyle;
import com.gala.video.app.player.ui.config.style.IPlayerMenuPanelUIStyle;
import com.gala.video.app.player.ui.config.style.common.IGalleryUIStyle;
import com.gala.video.app.player.ui.overlay.OnAdStateListener;
import com.gala.video.app.player.ui.overlay.contents.BitStreamContent;
import com.gala.video.app.player.ui.overlay.contents.ContentHolder;
import com.gala.video.app.player.ui.overlay.contents.EpisodeListContent;
import com.gala.video.app.player.ui.overlay.contents.GalleryListContent;
import com.gala.video.app.player.ui.overlay.contents.GalleryListContent.OnHorizontalScrollListener;
import com.gala.video.app.player.ui.overlay.contents.IContent;
import com.gala.video.app.player.ui.overlay.contents.IContent.IItemListener;
import com.gala.video.app.player.ui.overlay.contents.ScreenRatioContent;
import com.gala.video.app.player.ui.overlay.contents.SkipHeadTailContent;
import com.gala.video.app.player.ui.overlay.contents.Support2Dto3DContent;
import com.gala.video.app.player.ui.widget.tabhost.SimpleTabHost;
import com.gala.video.app.player.ui.widget.tabhost.SimpleTabHost.IndicatorView;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.NetworkUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.DlnaKeyEvent;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbsMenuPanel extends RelativeLayout {
    protected static final int AUTO_HIDE_DELAY = 10000;
    protected static final String BLOCK_PLAYLIST = "playlist";
    protected static final String BLOCK_RATIO = "ratio";
    protected static final String BLOCK_REC = "rec";
    protected static final String BLOCK_SKIP = "skip";
    protected static final String BLOCK_VIDEOLIST = "videolist";
    protected static final boolean IS_ZOOM_ENABLED = Project.getInstance().getControl().isOpenAnimation();
    protected static final String LIVE_C1 = "101221";
    protected static final int MSG_AUTO_HIDE = 20000;
    private static final String SEAT_FULLSCREEN = "fullscreen";
    private static final String SEAT_ORIGINAL = "original";
    private static final String SEAT_SKIPOFF = "skipoff";
    private static final String SEAT_SKIPON = "skipon";
    private static final String TAG_S = "Player/Ui/AbsMenuPanel";
    private final String TAG;
    private boolean campatibleMode;
    private boolean clipChildren;
    protected BaseAdData mAdData;
    protected ContentHolder mAssociativeContentHolder;
    private OnUserBitStreamChangeListener mBitStreamListener;
    private OnUserChangeVideoRatioListener mChangeVideoRatioListener;
    protected List<ContentHolder> mContentHolderList;
    protected Context mContext;
    protected IVideo mCurrentVideo;
    protected boolean mDataChanged;
    protected boolean mDataFetched;
    protected boolean mEnableShow;
    protected final IEpisodeListUIStyle mEpisodeStyle;
    protected String mHDRBlock;
    protected MyHandler mHandler;
    protected boolean mInited;
    protected boolean mIsFirstSetData;
    protected boolean mIsShowAssociatives;
    protected int mKey;
    protected final IGalleryUIStyle mLandGalleryUIStyle;
    protected final IPlayerMenuPanelUIStyle mMenuPanelUIStyle;
    protected boolean mNeedAutoRequestFocus;
    protected boolean mNeedUpdate;
    private OnAdStateListener mOnAdStateListener;
    public OnHorizontalScrollListener mOnHorizontalScrollListener;
    protected IPingbackContext mPingbackContext;
    private OnUserPlayPauseListener mPlayPauseListener;
    private OnUserSkipHeadTailChangeListener mSkipHeadTailListener;
    protected SimpleTabHost mTabHost;
    protected OnUserVideoChangeListener mVideoChangeListener;
    protected AtomicBoolean mVideoChanged;

    public class MyContentItemListener<T> implements IItemListener<T> {
        String TAG_M = "AbsMenuPanel/MyContentItemListener";
        int mType;

        public MyContentItemListener(int type) {
            this.mType = type;
        }

        public void onItemClicked(T data, int index) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG_M, ">> onItemClicked, mType=" + this.mType + ", data=" + data + ", index=" + index);
            }
            switch (this.mType) {
                case 1:
                    AbsMenuPanel.this.handleVideoClicked((IVideo) data, index, this.mType);
                    break;
                case 2:
                    AbsMenuPanel.this.handleVideoClicked((IVideo) data, index, this.mType);
                    break;
                case 3:
                    AbsMenuPanel.this.handleVideoClicked((IVideo) data, index, this.mType);
                    break;
                case 7:
                    AbsMenuPanel.this.handleVideoClicked((IVideo) data, index, this.mType);
                    break;
            }
            AbsMenuPanel.this.notifyUserInteractionBegin(UserInteractionType.INTIME);
        }

        public void onItemSelected(T data, int index) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG_M, ">> onItemSelected, mType=" + this.mType + ", data=" + data + ", index=" + index);
            }
            switch (this.mType) {
                case 8:
                    if (data != null) {
                        AbsMenuPanel.this.handleBitstreamChanged((BitStream) data, index);
                        return;
                    } else if (index == 3) {
                        AbsMenuPanel.this.notifyHDRToggle(true);
                        return;
                    } else {
                        AbsMenuPanel.this.handleHDRToggleChanged(index);
                        AbsMenuPanel.this.handlePause(index);
                        return;
                    }
                case 9:
                    AbsMenuPanel.this.handleSkipHeaderChanged((Boolean) data, index);
                    return;
                case 10:
                    AbsMenuPanel.this.handleScreenRatioChanged(((Integer) data).intValue(), index);
                    return;
                case 11:
                    AbsMenuPanel.this.handleDimensionChanged((Boolean) data, index);
                    return;
                default:
                    return;
            }
        }

        public void onItemFilled() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG_M, ">> onItemSelected, mType=" + this.mType);
            }
            switch (this.mType) {
                case 1:
                case 2:
                case 3:
                case 7:
                    if (AbsMenuPanel.this.mNeedAutoRequestFocus) {
                        AbsMenuPanel.this.handleContentRequestFocus();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    static class MyHandler extends Handler {
        private WeakReference<AbsMenuPanel> mAbsMenuPanelRef;

        MyHandler(AbsMenuPanel absMenuPanel) {
            this.mAbsMenuPanelRef = new WeakReference(absMenuPanel);
        }

        public void handleMessage(Message msg) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(AbsMenuPanel.TAG_S, "HideHandler.handleMessage(" + msg + " )");
            }
            AbsMenuPanel mp = (AbsMenuPanel) this.mAbsMenuPanelRef.get();
            if (PlayerDebugUtils.menuPanelAutoHideOverride() && msg.what == 20000 && mp != null) {
                mp.hide();
            }
        }
    }

    public enum UserInteractionType {
        INDEFINITE,
        ONETIME,
        INTIME
    }

    protected abstract void doShow();

    protected abstract void initViews(Context context);

    public AbsMenuPanel(Context context) {
        this(context, null);
    }

    public AbsMenuPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsMenuPanel(Context context, AttributeSet attrs, int defStyle) {
        boolean z;
        super(context, attrs, defStyle);
        this.mMenuPanelUIStyle = PlayerAppConfig.getUIStyle().getPlayerMenuPanelUIStyle();
        this.mEpisodeStyle = PlayerAppConfig.getUIStyle().getEpisodeListUIStyleForMenuPanel();
        this.mLandGalleryUIStyle = PlayerAppConfig.getUIStyle().getMenuLandGalleryUIStyle();
        this.mAssociativeContentHolder = null;
        this.mContentHolderList = new ArrayList();
        this.mVideoChanged = new AtomicBoolean(true);
        this.mEnableShow = false;
        this.mInited = false;
        this.mIsFirstSetData = true;
        this.mIsShowAssociatives = true;
        this.mNeedAutoRequestFocus = true;
        this.mHDRBlock = "";
        this.clipChildren = true;
        this.campatibleMode = false;
        this.mHandler = new MyHandler(this);
        this.mOnHorizontalScrollListener = new OnHorizontalScrollListener() {
            public void onScrollStopped(List<Integer> curItems) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(AbsMenuPanel.this.TAG, "onScrollStopped:" + curItems);
                }
                if (!ListUtils.isEmpty((List) curItems)) {
                    AbsMenuPanel.this.notifyRecommendShow(((Integer) curItems.get(0)).intValue(), ((Integer) curItems.get(curItems.size() - 1)).intValue());
                }
            }

            public void onScrollStarted() {
            }
        };
        this.mNeedUpdate = false;
        this.TAG = "Player/Ui/AbsMenuPanel@" + Integer.toHexString(hashCode());
        this.mContext = context;
        this.mPingbackContext = (IPingbackContext) this.mContext;
        if (VERSION.SDK_INT < 19) {
            z = true;
        } else {
            z = false;
        }
        this.campatibleMode = z;
    }

    public void setClipChildren(boolean clipChildren) {
        super.setClipChildren(clipChildren);
        this.clipChildren = clipChildren;
    }

    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> invalidateChildInParent, campatibleMode=" + this.campatibleMode + ", clipChildren=" + this.clipChildren);
        }
        if (this.campatibleMode && !this.clipChildren) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "invalidateChildInParent, location=[" + location[0] + ", " + location[1] + "], dirty=" + dirty);
            }
            dirty.offset(location[0] - getScrollX(), location[1] - getScrollY());
            dirty.union(0, 0, getWidth(), getHeight());
            dirty.offset(getScrollX() - location[0], getScrollY() - location[1]);
        }
        return super.invalidateChildInParent(location, dirty);
    }

    protected final void notifyRecommendShow(int start, int end) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> notifyRecommendShow() :" + start + "/" + end);
        }
        if (this.mCurrentVideo != null) {
            List associatives = this.mCurrentVideo.getBodanVideos();
            if (!ListUtils.isEmpty(associatives)) {
                StringBuilder sb = new StringBuilder();
                StringBuilder source = new StringBuilder();
                end = Math.min(end, associatives.size());
                for (int i = start; i <= end; i++) {
                    if (i == end) {
                        sb.append(((IVideo) associatives.get(i)).getAlbum().qpId);
                        source.append(((IVideo) associatives.get(i)).getAlbum().getSource());
                    } else {
                        sb.append(((IVideo) associatives.get(i)).getAlbum().qpId);
                        sb.append(",");
                        source.append(((IVideo) associatives.get(i)).getAlbum().getSource());
                        source.append(",");
                    }
                }
                Album sourceAlbum = this.mCurrentVideo.getAlbum();
                Album showAlbum = ((IVideo) associatives.get(0)).getAlbum();
                PingbackFactory.instance().createPingback(12).addItem(PPUID.PPUID_TYPE(getUid())).addItem(AID.AID_TYPE(sourceAlbum.qpId)).addItem(EVENTID.EVENT_ID(showAlbum.eventId)).addItem(CID.CID_TYPE(String.valueOf(sourceAlbum.chnId))).addItem(BKT.BKT_TYPE(showAlbum.bkt)).addItem(AREA.AREA_TYPE(showAlbum.area)).addItem(ALBUMLIST.ALBUMLIST_TYPE(sb.toString())).addItem(SOURCE.SOURCE_TYPE(source.toString())).post();
            } else if (LogUtils.mIsDebug) {
                LogUtils.w(this.TAG, "notifyRecommendShow: list is empty!");
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(this.TAG, "notifyRecommendShow: current video is null!");
        }
    }

    private String getUid() {
        String uid;
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(this.mContext)) {
            uid = GetInterfaceTools.getIGalaAccountManager().getUID();
        } else {
            uid = "NA";
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "<<getUid" + uid + ",context=" + this.mContext);
        }
        return uid;
    }

    private void sendRecommendClickPingback(IVideo clickVideo) {
        if (clickVideo != null && this.mCurrentVideo != null) {
            Album clickedAlbum = clickVideo.getAlbum();
            int index = findIndexByTvIdFromVideoList(clickVideo.getTvId(), this.mCurrentVideo.getBodanVideos());
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "sendRecommendClickPingback rseat=" + (index + 1));
            }
            PingbackFactory.instance().createPingback(7).addItem(R.R_TYPE(clickVideo.getTvId())).addItem(BLOCK.BLOCK_TYPE(getProgramBlock())).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(String.valueOf(index + 1))).addItem(RPAGE.RPAGE_ID(getRpage())).addItem(C1.C1_TYPE(String.valueOf(clickedAlbum.chnId))).addItem(NOW_C1.NOW_C1_TYPE(getC1())).addItem(NOW_QPID.NOW_QPID_TYPE(getNowQpid())).addItem(NOW_C2.NOW_C2_TYPE(getC2())).addItem(C2.C2_TYPE(getC2())).post();
        }
    }

    private void sendGuessYouLikeClickedPingback(IVideo clickVideo) {
        if (clickVideo == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.e(this.TAG, "sendGuessYouLikeClickedPingback clickvideo is null");
            }
        } else if (this.mCurrentVideo != null) {
            Album clickedAlbum = clickVideo.getAlbum();
            Album sourceAlbum = this.mCurrentVideo.getAlbum();
            if (clickedAlbum != null) {
                PingbackFactory.instance().createPingback(6).addItem(PPUID.PPUID_TYPE(getUid())).addItem(RANK.RANK_TYPE(String.valueOf(findIndexByTvIdFromVideoList(clickVideo.getTvId(), this.mCurrentVideo.getBodanVideos())))).addItem(AID.AID_TYPE(sourceAlbum.qpId)).addItem(EVENTID.EVENT_ID(clickedAlbum.eventId)).addItem(CID.CID_TYPE(String.valueOf(sourceAlbum.chnId))).addItem(BKT.BKT_TYPE(clickedAlbum.bkt)).addItem(AREA.AREA_TYPE(clickedAlbum.area)).addItem(TAID.TAID_TYPE(clickedAlbum.qpId)).addItem(TCID.TCID_TYPE(String.valueOf(clickedAlbum.chnId))).addItem(SOURCE.SOURCE_TYPE(clickedAlbum.getSource())).post();
            } else if (LogUtils.mIsDebug) {
                LogUtils.e(this.TAG, "sendGuessYouLikeClickedPingback: null == clickedAlbum!");
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(this.TAG, "sendGuessYouLikeClickedPingback: current video is null!");
        }
    }

    private int findIndexByTvIdFromVideoList(String tvId, List<IVideo> list) {
        LogUtils.d(this.TAG, "findIndexByTvIdFromVideoList() tvId:" + tvId);
        if (StringUtils.isEmpty((CharSequence) tvId) || ListUtils.isEmpty((List) list)) {
            return -1;
        }
        int index = -1;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (tvId.equals(((IVideo) list.get(i)).getTvId())) {
                index = i;
                break;
            }
        }
        LogUtils.d(this.TAG, "<<findIndexByTvId ret=" + index);
        return index;
    }

    public void setOnUserVideoChangeListener(OnUserVideoChangeListener listener) {
        this.mVideoChangeListener = listener;
    }

    protected void notifyVideoChanged(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> notifyEpisodeClick, video=" + video.toStringBrief());
        }
        if (this.mVideoChangeListener != null) {
            this.mVideoChangeListener.onVideoChange(this, video);
        }
    }

    public void setOnUserPlayPauseListener(OnUserPlayPauseListener listener) {
        this.mPlayPauseListener = listener;
    }

    public void setOnUserBitStreamChangeListener(OnUserBitStreamChangeListener listener) {
        this.mBitStreamListener = listener;
    }

    protected void notifyBitStreamSelected(BitStream bitStream) {
        if (this.mBitStreamListener != null) {
            this.mBitStreamListener.onBitStreamChange(this, bitStream);
        }
    }

    protected void notifyBitStreamToggle(boolean isOpen) {
        if (!isOpen) {
            notifyHDRToggle(false);
        }
        if (this.mBitStreamListener != null) {
            this.mBitStreamListener.onHDRToggleChanged(this, isOpen);
        }
    }

    public void setOnUserSetDisplayModeListener(OnUserChangeVideoRatioListener listener) {
        this.mChangeVideoRatioListener = listener;
    }

    protected void notifyVideoRatioSelected(int ratio) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "notifyVideoRatioSelected(" + ratio + ", listener =" + this.mChangeVideoRatioListener + ")");
        }
        if (this.mChangeVideoRatioListener != null) {
            this.mChangeVideoRatioListener.onVideoRatioChange(ratio);
        }
        sendVideoRatioClickPingback(ratio);
    }

    public void setOnUserSkipHeaderTailChangeListener(OnUserSkipHeadTailChangeListener listener) {
        this.mSkipHeadTailListener = listener;
    }

    public void setVideo(IVideo video) {
        if (video != null) {
            IVideo oldVideo = this.mCurrentVideo;
            this.mCurrentVideo = video;
            if (!video.equalVideo(oldVideo)) {
                clearAd();
            }
            if (!(video.getSourceType() != SourceType.CAROUSEL || oldVideo == null || video.getLiveChannelId() == oldVideo.getLiveChannelId())) {
                clearAd();
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "setVideo: oldVideo=" + oldVideo + ", new video=" + this.mCurrentVideo);
            }
            if (!video.equalVideo(oldVideo) || video.getProvider().getSourceType() == SourceType.CAROUSEL) {
                this.mDataFetched = false;
                this.mDataChanged = false;
                this.mVideoChanged.set(true);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setVideo, video is null, return");
        }
    }

    protected void clearAd() {
    }

    public void enableShow(boolean enable) {
        if (LogUtils.mIsDebug) {
            LogUtils.i(this.TAG, "enableShow(" + enable + ")");
        }
        if (!enable) {
            hide();
        }
        this.mEnableShow = enable;
    }

    public final void show(int key) {
        if (LogUtils.mIsDebug) {
            LogUtils.i(this.TAG, ">> show: enabled=" + this.mEnableShow + ", inited=" + this.mInited + ", videoChanged=" + this.mVideoChanged.get());
        }
        if (this.mEnableShow) {
            this.mKey = key;
            if (NetworkUtils.isNetworkAvaliable()) {
                if (!this.mInited) {
                    initViews(this.mContext);
                    this.mInited = true;
                }
                setVisibility(0);
                if (LogUtils.mIsDebug) {
                    LogUtils.i(this.TAG, "<< show");
                }
                notifyUserInteractionBegin(UserInteractionType.ONETIME);
                doShow();
            }
        }
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.i(this.TAG, "hide()");
        }
        this.mIsFirstSetData = true;
        if (getVisibility() != 8) {
            setVisibility(8);
            doHide();
            if (LogUtils.mIsDebug) {
                LogUtils.i(this.TAG, "hide() mNeedUpdate=" + this.mNeedUpdate);
            }
            this.mHandler.removeMessages(20000);
        }
    }

    protected void doHide() {
    }

    public void setIsShowAssociatives(boolean isShow) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setIsShowAssociatives(" + isShow + ")");
        }
        this.mIsShowAssociatives = isShow;
    }

    public boolean onDlnaKeyEvent(DlnaKeyEvent event, KeyKind key) {
        notifyUserInteractionBegin(UserInteractionType.ONETIME);
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.e(TAG_S, "AbsMenupanel dispatchKeyEvent(" + event + " )");
        }
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 19:
                    if (this.mTabHost == null) {
                        return false;
                    }
                    IndicatorView indicatorView = this.mTabHost.getIndicatorView();
                    if (indicatorView == null || !indicatorView.dispatchKeyEvent(event)) {
                        notifyUserInteractionBegin(UserInteractionType.ONETIME);
                        break;
                    }
                    hide();
                    return true;
                case 21:
                case 22:
                    if (this.mKey == 20) {
                        this.mNeedAutoRequestFocus = false;
                    }
                    notifyUserInteractionBegin(UserInteractionType.ONETIME);
                    break;
                case 24:
                case 25:
                case 164:
                    if (LogUtils.mIsDebug) {
                        LogUtils.e(TAG_S, "volume is invalid");
                        break;
                    }
                    break;
                default:
                    notifyUserInteractionBegin(UserInteractionType.ONETIME);
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    protected void notifyUserInteractionBegin(UserInteractionType type) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "notifyUserInteractionBegin(" + type + ")");
        }
        this.mHandler.removeMessages(20000);
        if (type == UserInteractionType.ONETIME) {
            this.mHandler.sendEmptyMessageDelayed(20000, 10000);
        } else if (type == UserInteractionType.INTIME) {
            this.mHandler.sendEmptyMessage(20000);
        }
    }

    protected void setContentListener(ContentHolder holder) {
        IContent<?, ?> content = holder.getWrappedContent();
        int type = holder.getType();
        switch (type) {
            case 1:
                ((EpisodeListContent) content).setItemListener(new MyContentItemListener(type));
                ((EpisodeListContent) content).setOnAdStateListener(this.mOnAdStateListener);
                return;
            case 2:
                ((GalleryListContent) content).setItemListener(new MyContentItemListener(type));
                ((GalleryListContent) content).setOnAdStateListener(this.mOnAdStateListener);
                return;
            case 3:
                ((GalleryListContent) content).setItemListener(new MyContentItemListener(type));
                ((GalleryListContent) content).setOnHorizontalScrollListener(this.mOnHorizontalScrollListener);
                ((GalleryListContent) content).setOnAdStateListener(this.mOnAdStateListener);
                return;
            case 7:
                ((GalleryListContent) content).setItemListener(new MyContentItemListener(type));
                ((GalleryListContent) content).setOnAdStateListener(this.mOnAdStateListener);
                return;
            case 8:
                ((BitStreamContent) content).setItemListener(new MyContentItemListener(type));
                ((BitStreamContent) content).setOnAdStateListener(this.mOnAdStateListener);
                return;
            case 9:
                ((SkipHeadTailContent) content).setItemListener(new MyContentItemListener(type));
                return;
            case 10:
                ((ScreenRatioContent) content).setItemListener(new MyContentItemListener(type));
                return;
            case 11:
                ((Support2Dto3DContent) content).setItemListener(new MyContentItemListener(type));
                return;
            default:
                return;
        }
    }

    protected void handleContentRequestFocus() {
    }

    private void handleVideoClicked(IVideo video, int index, int contentType) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleVideoClicked, index=" + index + ", video=" + video);
        }
        List<IVideo> list = new ArrayList();
        switch (contentType) {
            case 1:
                list.addAll(this.mCurrentVideo.getEpisodeVideos());
                sendEpisodeClickPingback(list, video, index, contentType);
                break;
            case 2:
                list.addAll(this.mCurrentVideo.getEpisodeVideos());
                sendEpisodeClickPingback(list, video, index, contentType);
                break;
            case 3:
                list.addAll(this.mCurrentVideo.getBodanVideos());
                sendRecommendClickPingback(video);
                sendGuessYouLikeClickedPingback(video);
                break;
            case 7:
                list.addAll(this.mCurrentVideo.getBodanVideos());
                sendEpisodeClickPingback(list, video, index, contentType);
                break;
            default:
                if (LogUtils.mIsDebug) {
                    LogUtils.d(this.TAG, "handleVideoClicked, unhandled content type=" + contentType);
                    break;
                }
                break;
        }
        if (!video.getTvId().equals(this.mCurrentVideo.getTvId())) {
            notifyVideoChanged(video);
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "click the same episode from menu panel.");
        }
    }

    private void handleBitstreamChanged(BitStream bitStream, int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleBitstreamChanged, index=" + index + ", bitStream=" + bitStream);
        }
        notifyBitStreamSelected(bitStream);
        sendClickBitStreamPingback(bitStream);
    }

    private void sendClickBitStreamPingback(BitStream bitStream) {
        if (this.mCurrentVideo != null) {
            PingbackFactory.instance().createPingback(31).addItem(R.R_TYPE(getTvqID())).addItem(BLOCK.BLOCK_TYPE(this.mHDRBlock)).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE("ra_" + String.valueOf(BitStream.getBid(bitStream)))).addItem(RPAGE.RPAGE_ID(getRpage())).addItem(C1.C1_TYPE(getC1())).addItem(C2.C2_TYPE(getC2())).addItem(NOW_C1.NOW_C1_TYPE(getC1())).addItem(NOW_QPID.NOW_QPID_TYPE(getNowQpid())).addItem(NOW_C2.NOW_C2_TYPE(getC2())).post();
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(this.TAG, "mCurrentVideo is null");
        }
    }

    protected String getC1() {
        return "";
    }

    protected String getNowQpid() {
        if (this.mCurrentVideo != null) {
            return this.mCurrentVideo.getAlbum().qpId;
        }
        return "";
    }

    protected String getTvqID() {
        if (this.mCurrentVideo != null) {
            return this.mCurrentVideo.getTvId();
        }
        return "";
    }

    private void handleHDRToggleChanged(int index) {
        if (index <= 1) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "handleHDRToggleChanged, index=" + index);
            }
            Boolean isOpen = Boolean.valueOf(false);
            switch (index) {
                case 0:
                    isOpen = Boolean.valueOf(true);
                    break;
            }
            notifyBitStreamToggle(isOpen.booleanValue());
        }
    }

    private void handlePause(int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handlePause, index=" + index);
        }
        switch (index) {
            case 4:
                if (this.mPlayPauseListener != null) {
                    this.mPlayPauseListener.onPause(this);
                    return;
                }
                return;
            case 5:
                if (this.mPlayPauseListener != null) {
                    this.mPlayPauseListener.onPlay(this);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void handleSkipHeaderChanged(Boolean skip, int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleSkipHeaderChanged, index=" + index + ", skip=" + skip);
        }
        if (this.mSkipHeadTailListener != null) {
            this.mSkipHeadTailListener.onSkipChange(this, skip.booleanValue());
        }
        sendSkipHeadTailPingback(skip.booleanValue());
    }

    private void handleScreenRatioChanged(int ratio, int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleScreenRatioChanged, index=" + index + ", ratio=" + ratio);
        }
        notifyVideoRatioSelected(ratio);
        SettingPlayPreference.setStretchPlaybackToFullScreen(this.mContext, ratio == 4);
    }

    private void handleDimensionChanged(Boolean is3D, int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleDimensionChanged, index=" + index + ", is3D=" + is3D);
        }
    }

    private void sendVideoRatioClickPingback(int ratio) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "sendVideoRatioChangePingback()");
        }
        String rseat = "";
        if (ratio == 1) {
            rseat = SEAT_ORIGINAL;
        } else if (ratio == 4) {
            rseat = SEAT_FULLSCREEN;
        }
        if (this.mCurrentVideo != null) {
            PingbackFactory.instance().createPingback(19).addItem(R.R_TYPE(getTvqID())).addItem(BLOCK.BLOCK_TYPE(BLOCK_RATIO)).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(rseat)).addItem(RPAGE.RPAGE_ID(getRpage())).addItem(C1.C1_TYPE(getC1())).addItem(C2.C2_TYPE(getC2())).addItem(NOW_C1.NOW_C1_TYPE(getC1())).addItem(NOW_QPID.NOW_QPID_TYPE(getNowQpid())).addItem(NOW_C2.NOW_C2_TYPE(getC2())).post();
        }
    }

    private void sendEpisodeClickPingback(List<IVideo> list, IVideo clickVideo, int index, int contentType) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "sendEpisodeClickPingback()");
        }
        if (clickVideo != null && this.mCurrentVideo != null) {
            int rseat = 0;
            String isPrevue = "";
            String clickedc2 = "";
            String curPlayIndex = "";
            switch (contentType) {
                case 1:
                    curPlayIndex = Integer.toString(this.mCurrentVideo.getPlayOrder());
                    rseat = index + 1;
                    if (clickVideo.getAlbum().getContentType() != ContentType.PREVUE) {
                        isPrevue = "0";
                        break;
                    } else {
                        isPrevue = "1";
                        break;
                    }
                case 2:
                    curPlayIndex = Integer.toString(DataHelper.findVideoIndexByTvid(list, this.mCurrentVideo) + 1);
                    rseat = index + 1;
                    break;
                case 7:
                    rseat = index + 1;
                    curPlayIndex = Integer.toString(DataHelper.findVideoIndexByTvid(list, this.mCurrentVideo) + 1);
                    break;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "sendEpisodeClickPingback, contentType=" + contentType + ", rseat=" + rseat + ", isPrevue=" + isPrevue + ", clickedc2=" + clickedc2);
            }
            PingbackFactory.instance().createPingback(3).addItem(R.R_TYPE(clickVideo.getTvId())).addItem(BLOCK.BLOCK_TYPE(getProgramBlock())).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(String.valueOf(rseat))).addItem(RPAGE.RPAGE_ID(getRpage())).addItem(ISPREVUE.ISPREVUE_TYPE(isPrevue)).addItem(C1.C1_TYPE(String.valueOf(clickVideo.getChannelId()))).addItem(C2.C2_TYPE(getC2())).addItem(NOW_C1.NOW_C1_TYPE(String.valueOf(this.mCurrentVideo.getChannelId()))).addItem(NOW_QPID.NOW_QPID_TYPE(getNowQpid())).addItem(NOW_C2.NOW_C2_TYPE(getC2())).addItem(NOW_EPISODE.ITEM(curPlayIndex)).post();
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "sendEpisodeClickPingback video is null");
        }
    }

    private void sendSkipHeadTailPingback(boolean skip) {
        if (this.mCurrentVideo != null) {
            String rseat = "";
            if (skip) {
                rseat = SEAT_SKIPON;
            } else {
                rseat = SEAT_SKIPOFF;
            }
            PingbackFactory.instance().createPingback(20).addItem(R.R_TYPE(getTvqID())).addItem(BLOCK.BLOCK_TYPE("skip")).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(rseat)).addItem(RPAGE.RPAGE_ID(getRpage())).addItem(C1.C1_TYPE(getC1())).addItem(NOW_C1.NOW_C1_TYPE(getC1())).addItem(NOW_QPID.NOW_QPID_TYPE(getNowQpid())).addItem(NOW_C2.NOW_C2_TYPE(getC2())).addItem(C2.C2_TYPE(getC2())).post();
        }
    }

    protected String getC2() {
        SourceType type = this.mCurrentVideo.getSourceType();
        boolean isFlower = this.mCurrentVideo.isTrailer();
        String c2 = "";
        if ((type != SourceType.LIVE || isFlower) && type != SourceType.CAROUSEL) {
            return c2;
        }
        return this.mCurrentVideo.getLiveChannelId();
    }

    protected String getProgramBlock() {
        if (this.mAssociativeContentHolder == null) {
            return "";
        }
        String block = "";
        switch (this.mAssociativeContentHolder.getType()) {
            case 1:
            case 2:
                return "videolist";
            case 3:
                return "rec";
            case 7:
                if (DataHelper.isBodanOrDailyNews(this.mCurrentVideo)) {
                    return "playlist";
                }
                return "videolist";
            default:
                return block;
        }
    }

    protected String getQyPrv() {
        String qyprv = "";
        if (this.mCurrentVideo == null || !this.mCurrentVideo.isVip()) {
            return qyprv;
        }
        if (this.mCurrentVideo.isPreview()) {
            return "1";
        }
        return "0";
    }

    protected String getRpage() {
        String rpage = "";
        if (this.mKey == 20) {
            return "downpanel";
        }
        if (this.mKey == 82) {
            return "menupanel";
        }
        return rpage;
    }

    public List<Integer> getVisibleList() {
        int index = findIndexByTvIdFromVideoList(this.mCurrentVideo.getTvId(), this.mCurrentVideo.getBodanVideos());
        List associatives = this.mCurrentVideo.getBodanVideos();
        int maxSize = -1;
        if (!ListUtils.isEmpty(associatives)) {
            maxSize = associatives.size();
        }
        int end;
        if (index <= 3) {
            if (maxSize <= 5) {
                end = maxSize - 1;
            } else {
                end = 5;
            }
            List<Integer> list = new ArrayList();
            list.add(Integer.valueOf(0));
            list.add(Integer.valueOf(end));
            return list;
        }
        int start = index - 3;
        if (maxSize <= index + 3) {
            end = maxSize - 1;
        } else {
            end = index + 3;
        }
        list = new ArrayList();
        list.add(Integer.valueOf(start));
        list.add(Integer.valueOf(end));
        return list;
    }

    public void updateSkipHeadAndTail(boolean isSkipTail) {
    }

    public void updateBitStream(List<BitStream> list, BitStream bitStream) {
    }

    public void notifyHDRToggle(boolean isOpen) {
    }

    public void onActivityDestroyed() {
    }

    public void setAdData(BaseAdData data) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setAdData() data=" + data);
        }
        if (data == null) {
            return;
        }
        if (data.getType() == 3) {
            setPrecisionAdData(data);
        } else {
            this.mAdData = data;
        }
    }

    private void setPrecisionAdData(BaseAdData data) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setPrecisionAdData()");
        }
        if (this.mAssociativeContentHolder != null) {
            IContent<?, ?> content = this.mAssociativeContentHolder.getWrappedContent();
            if (content instanceof GalleryListContent) {
                ((GalleryListContent) content).setPercisionAdData(data);
            } else if (content instanceof EpisodeListContent) {
                ((EpisodeListContent) content).setPercisionAdData(data);
            }
        }
    }

    public void setAdStateListener(OnAdStateListener listener) {
        this.mOnAdStateListener = listener;
    }
}
