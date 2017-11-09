package com.gala.video.app.player.albumdetail.ui.card;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackItem;
import com.gala.pingback.PingbackStore;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.ISPREVUE;
import com.gala.pingback.PingbackStore.LINE;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.NOW_EPISODE;
import com.gala.pingback.PingbackStore.NOW_QPID;
import com.gala.pingback.PingbackStore.PAGE_CLICK.BLOCKTYPE;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RPAGETYPE;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RSEATTYPE;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.PAGE_SHOW;
import com.gala.pingback.PingbackStore.PAGE_SHOW.BTSPTYPE;
import com.gala.pingback.PingbackStore.PAGE_SHOW.QTCURLTYPE;
import com.gala.pingback.PingbackStore.QPLD;
import com.gala.pingback.PingbackStore.RSEAT;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.KeyWordType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ContentType;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.albumlist.widget.BlocksView.LayoutParams;
import com.gala.video.app.player.R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.ui.card.BasicContract.Presenter;
import com.gala.video.app.player.albumdetail.ui.card.BasicContract.View;
import com.gala.video.app.player.albumdetail.ui.overlay.DetailOverlay;
import com.gala.video.app.player.albumdetail.ui.overlay.contents.EpisodeAlbumListContent;
import com.gala.video.app.player.albumdetail.ui.overlay.contents.ProgramCardContent;
import com.gala.video.app.player.albumdetail.ui.overlay.panels.BasicInfoPanel;
import com.gala.video.app.player.albumdetail.ui.overlay.panels.BasicInfoPanel.OnBasicDescVisibilityListener;
import com.gala.video.app.player.albumdetail.ui.overlay.panels.CtrlButtonPanel;
import com.gala.video.app.player.albumdetail.ui.overlay.panels.CtrlButtonPanel.DetailButtonKeyFront;
import com.gala.video.app.player.albumdetail.ui.overlay.panels.CtrlButtonPanel.OnCtrlButtonClickedListener;
import com.gala.video.app.player.albumdetail.ui.overlay.panels.CtrlButtonPanel.OnCtrlFocusChangeListener;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.controller.UIEventDispatcher;
import com.gala.video.app.player.data.DetailConstants;
import com.gala.video.app.player.ui.config.IAlbumDetailUiConfig;
import com.gala.video.app.player.ui.overlay.contents.ContentHolder;
import com.gala.video.app.player.ui.overlay.contents.ContentWrapper;
import com.gala.video.app.player.ui.overlay.contents.IContent;
import com.gala.video.app.player.ui.overlay.contents.IContent.IItemListener;
import com.gala.video.app.player.ui.overlay.panels.PlayWindowPanel;
import com.gala.video.app.player.ui.overlay.panels.PlayWindowPanel.OnPlayWindowClickedListener;
import com.gala.video.app.player.ui.widget.views.VerticalScrollLayout;
import com.gala.video.app.player.utils.AlbumTextHelper;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.app.player.utils.DetailItemUtils;
import com.gala.video.app.player.utils.MyPlayerProfile;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.voice.IVoiceCommon;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.uikit.view.IViewLifecycle;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.mcto.ads.internal.net.SendFlag;
import java.util.ArrayList;
import java.util.List;

public class BasicInfoContent extends VerticalScrollLayout implements IViewLifecycle<Presenter>, View {
    private final String TAG = ("Detail/UI/BasicInfoContent@" + Integer.toHexString(hashCode()));
    private boolean isFirstGainFocus = true;
    private AlbumInfo mAlbumInfo;
    private boolean mAnonymousFav;
    private OnBasicDescVisibilityListener mBasicDescVisibilityListener = new OnBasicDescVisibilityListener() {
        public void OnDescVisibility(int visibility) {
            BasicInfoContent.this.mCtrlButtonPanel.setDescVisibility(visibility);
        }
    };
    private BasicInfoPanel mBasicinfoPanel;
    private android.view.View mCardView;
    private Context mContext;
    private CtrlButtonPanel mCtrlButtonPanel;
    private OnCtrlFocusChangeListener mCtrlFocusChangeListener = new OnCtrlFocusChangeListener() {
        public void onFoucusChanged(DetailButtonKeyFront k, int ResId) {
            switch (k) {
                case UP:
                    BasicInfoContent.this.mCtrlButtonPanel.setDescNextDownId(ResId);
                    return;
                case LEFT:
                    if (BasicInfoContent.this.mUIConfig.isEnableWindowPlay()) {
                        BasicInfoContent.this.mPlayWindowPanel.setPlayWindowNextRightId(ResId);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private DetailOverlay mDetailOverlay;
    private long mEndTime;
    private boolean mFilled;
    private boolean mHasBind = false;
    private ContentHolder mInnerContentHolder;
    private boolean mIsContentVisible = true;
    private boolean mIsInited = true;
    private boolean mIsTimeKeeping = false;
    private LayoutParams mLayoutParams = new LayoutParams(-1, -1);
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private boolean mNeedTryWakeUp = false;
    private OnCtrlButtonClickedListener mOnCtrlButtonClickedListener = new OnCtrlButtonClickedListener() {
        public void onFullButtonClicked() {
            LogRecordUtils.logd(BasicInfoContent.this.TAG, ">> onFullButtonClicked");
            if (!BasicInfoContent.this.mUIConfig.isEnableWindowPlay()) {
                BasicInfoContent.this.sendFullButtonClickedPingback();
                BasicInfoContent.this.mPlayWindowPanel.startPlay(BasicInfoContent.this.mVideo);
            } else if (BasicInfoContent.this.isPlayerSurfaceValidate()) {
                BasicInfoContent.this.sendFullButtonClickedPingback();
                BasicInfoContent.this.mPlayWindowPanel.fullScreenButton();
            }
        }

        public void onVIPButtonClicked() {
            LogRecordUtils.logd(BasicInfoContent.this.TAG, ">> onVIPButtonClicked");
            if (BasicInfoContent.this.mContext instanceof Activity) {
                DetailItemUtils.startBuyPage(BasicInfoContent.this.mContext, ((Activity) BasicInfoContent.this.mContext).getIntent(), BasicInfoContent.this.mVideo, 0, 4);
            }
        }

        public void onFavButtonClicked() {
            if (BasicInfoContent.this.mAlbumInfo == null) {
                LogRecordUtils.logd(BasicInfoContent.this.TAG, "onFavButtonClicked, mAlbumInfo is null.");
            } else {
                BasicInfoContent.this.handlerFavor(BasicInfoContent.this.mAlbumInfo.isFavored());
            }
        }

        public void onDescButtonClicked() {
            LogRecordUtils.logd(BasicInfoContent.this.TAG, ">> onDescButtonClicked");
            BasicInfoContent.this.handleContentVisibilityChanged(false, false);
            UIEventDispatcher.instance().post(BasicInfoContent.this.mContext, 2, BasicInfoContent.this.mAlbumInfo);
        }

        public void onEquityImageClicked() {
            LogRecordUtils.logd(BasicInfoContent.this.TAG, ">> onEquityImageClicked");
            if (BasicInfoContent.this.mContext instanceof Activity) {
                DetailItemUtils.startBuyPage(BasicInfoContent.this.mContext, ((Activity) BasicInfoContent.this.mContext).getIntent(), BasicInfoContent.this.mVideo, GetInterfaceTools.getIGalaAccountManager().isVip() ? 0 : 1, 21);
            }
        }
    };
    private IPingbackContext mPingbackContext;
    private OnPlayWindowClickedListener mPlayWindowClickedListener = new OnPlayWindowClickedListener() {
        public void onPlayWindowClicked() {
            if (BasicInfoContent.this.mAlbumInfo == null) {
                LogRecordUtils.logd(BasicInfoContent.this.TAG, "mPlayWindowClickedListener.onPlayWindowClicked, mCurVideo is null.");
                return;
            }
            LogRecordUtils.logd(BasicInfoContent.this.TAG, "onPlayWindowClicked: PlayWindowClick, view is null -> " + (BasicInfoContent.this.mPlayWindowPanel.getPlayWindowView() == null));
            BasicInfoContent.this.setCurrentFocusView(BasicInfoContent.this.mPlayWindowPanel.getPlayWindowView());
            if (BasicInfoContent.this.mInnerContentHolder == null) {
            }
        }
    };
    private PlayWindowPanel mPlayWindowPanel;
    private int mResultCode;
    private android.view.View mRootView;
    private SourceType mSourceType;
    private long mStartTime;
    private IAlbumDetailUiConfig mUIConfig;
    private IVideo mVideo;

    public interface BasicInitCallBack {
        void initBasicInit();
    }

    class FavorRunnable implements Runnable {
        boolean mIsFav;

        public FavorRunnable(boolean isFav) {
            this.mIsFav = isFav;
        }

        public void run() {
            BasicInfoContent.this.handlerFavor(this.mIsFav);
        }
    }

    class FullScreenRunnable implements Runnable {
        FullScreenRunnable() {
        }

        public void run() {
            if (BasicInfoContent.this.mIsContentVisible) {
                BasicInfoContent.this.mPlayWindowPanel.startPlay(BasicInfoContent.this.mVideo);
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(BasicInfoContent.this.TAG, "FullScreenRunnable, basic content is invisible.");
            }
        }
    }

    private class MyItemListener<T> implements IItemListener<T> {
        private int mType;

        MyItemListener(int type) {
            this.mType = type;
        }

        public void onItemClicked(T data, int index) {
            LogRecordUtils.logd(BasicInfoContent.this.TAG, ">> onItemClicked, mType=" + this.mType + ", index=" + index + ", data=" + data);
            switch (this.mType) {
                case 1:
                    BasicInfoContent.this.handleEpisodeClicked(data, index);
                    BasicInfoContent.this.setCurrentFocusView(null);
                    return;
                case 2:
                    BasicInfoContent.this.handleProgramClicked(data, index);
                    BasicInfoContent.this.setCurrentFocusView(null);
                    return;
                default:
                    return;
            }
        }

        public void onItemSelected(T t, int index) {
        }

        public void onItemFilled() {
            BasicInfoContent.this.mFilled = true;
        }
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public BasicInfoContent(Context context) {
        super(context);
    }

    public void init(Context context, android.view.View root, IAlbumDetailUiConfig config, DetailOverlay detailOverlay) {
        this.mContext = context;
        this.mUIConfig = config;
        this.mRootView = root;
        this.mPingbackContext = (IPingbackContext) this.mContext;
        this.mDetailOverlay = detailOverlay;
        setupLayoutProperties();
    }

    private void setupLayoutProperties() {
        setOrientation(1);
        setClipChildren(false);
        setClipToPadding(false);
        setFocusable(true);
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
    }

    public ScreenMode getPlayerScreenMode() {
        return this.mPlayWindowPanel.getPlayerScreenMode();
    }

    public void resetBasicInfo(IVideo video, BlocksView blocksView) {
        LogRecordUtils.logd(this.TAG, ">> resetBasicInfo video = " + video);
        this.mVideo = video;
        if (this.mPlayWindowPanel != null && this.mPlayWindowPanel.getPlayerScreenMode() == ScreenMode.WINDOWED) {
            checkBasicCardVisible(false);
        }
        this.mAlbumInfo.setAlbum(video.getAlbum());
        if (this.mPlayWindowPanel != null) {
            this.mPlayWindowPanel.setVideo(this.mAlbumInfo, video);
        }
        if (this.mInnerContentHolder != null) {
            LogRecordUtils.logd(this.TAG, ">> refresh CardView remove holder");
            ((ViewGroup) this.mCardView).removeView(this.mInnerContentHolder.getWrappedContent().getView());
            this.mCardView.refreshDrawableState();
        }
        this.mInnerContentHolder = null;
        this.mInnerContentHolder = getInnerContent();
        if (this.mInnerContentHolder != null) {
            LogRecordUtils.logd(this.TAG, ">> create new holder, add");
            ((ViewGroup) this.mCardView).addView(this.mInnerContentHolder.getWrappedContent().getView());
            this.mInnerContentHolder.getWrappedContent().getFocusableView().setNextFocusUpId(R.id.share_detail_btn_album_full);
            this.mInnerContentHolder.getWrappedContent().show();
        }
        LogRecordUtils.logd(this.TAG, ">> reset Basic panel, ctrl panel, holder's Data");
        setupBasicInfo();
        Album album = this.mAlbumInfo.getAlbum();
        if (album != null && album.isSeries() && !album.isSourceType() && album.chnId == 2) {
            setDefaultFocus();
        }
        setLayoutParams(this.mLayoutParams);
        this.mCardView.refreshDrawableState();
        LogRecordUtils.logd(this.TAG, "<< resetBasicInfo, albumInfo = " + this.mAlbumInfo);
    }

    private void initView() {
        LogRecordUtils.logd(this.TAG, ">> initView");
        if (this.mAlbumInfo == null) {
            throw new IllegalStateException("albuminfo should not be null on init view.");
        }
        this.mCardView = LayoutInflater.from(this.mContext).inflate(R.layout.player_detail_basicinfo_card, this);
        initPanels(this.mRootView);
        this.mInnerContentHolder = getInnerContent();
        if (this.mInnerContentHolder != null) {
            ((ViewGroup) this.mCardView).addView(this.mInnerContentHolder.getWrappedContent().getView());
            this.mInnerContentHolder.getWrappedContent().getFocusableView().setNextFocusUpId(R.id.share_detail_btn_album_full);
        }
        setupVideoOnInit();
        setLayoutParams(this.mLayoutParams);
    }

    private void initPanels(android.view.View root) {
        LogRecordUtils.logd(this.TAG, ">> initPanels");
        this.mPlayWindowPanel = new PlayWindowPanel(this.mContext, this.mCardView, (FrameLayout) root.findViewById(R.id.fl_player_view_parent_news), this.mUIConfig);
        this.mBasicinfoPanel = new BasicInfoPanel(this.mContext, this.mCardView, this.mUIConfig);
        this.mCtrlButtonPanel = new CtrlButtonPanel(this.mContext, this.mCardView, this.mUIConfig);
        this.mPlayWindowPanel.setOnPlayWindowClickedListener(this.mPlayWindowClickedListener);
        this.mBasicinfoPanel.setOnBasicDescVisibilityListener(this.mBasicDescVisibilityListener);
        this.mCtrlButtonPanel.setOnCtrlFocusChangeListener(this.mCtrlFocusChangeListener);
        this.mCtrlButtonPanel.setOnCtrlButtonClickedListener(this.mOnCtrlButtonClickedListener);
    }

    private void setupVideoOnInit() {
        LogRecordUtils.logd(this.TAG, ">> setupVideoOnInit, mInnerContentHolder=" + this.mInnerContentHolder + ", video =" + this.mVideo);
        setupBasicInfo();
        this.mPlayWindowPanel.setVideo(this.mAlbumInfo, this.mVideo);
        this.mPlayWindowPanel.createPlayer(-1);
    }

    private void setupBasicInfo() {
        LogRecordUtils.logd(this.TAG, ">> setupBasicInfo, mInnerContentHolder=" + this.mInnerContentHolder + ", mAlbumInfo =" + this.mAlbumInfo);
        if (this.mInnerContentHolder != null) {
            IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
            if (content instanceof ContentWrapper) {
                ((ContentWrapper) content).setSelection(this.mAlbumInfo);
            } else if (content instanceof ProgramCardContent) {
                ((ProgramCardContent) content).setSelection(this.mAlbumInfo);
            }
        }
        updateInfo(this.mAlbumInfo);
        this.mBasicinfoPanel.setAlbumInfo(this.mAlbumInfo);
        this.mBasicinfoPanel.showOrUpdateBasicInfo();
        this.mCtrlButtonPanel.setAlbumInfo(this.mAlbumInfo);
        this.mCtrlButtonPanel.updateFullBtnText();
    }

    public void clearAlbumListDefaultSelectedTextColor() {
        if (this.mInnerContentHolder != null) {
            IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
            if (content instanceof ContentWrapper) {
                ((ContentWrapper) content).clearAlbumListDefaultSelectedTextColor();
            }
        }
    }

    private ContentHolder getInnerContent() {
        ContentHolder holder;
        switch (this.mAlbumInfo.getKind()) {
            case ALBUM_EPISODE:
            case VIDEO_EPISODE:
                if (DataHelper.showEpisodeAsGallery(this.mAlbumInfo.getAlbum())) {
                    holder = createProgramContentHolder();
                    this.mLayoutParams.height = ResourceUtil.getDimen(R.dimen.dimen_631dp);
                    return holder;
                }
                holder = createEpisodeContentHolder();
                this.mLayoutParams.height = ResourceUtil.getDimen(R.dimen.dimen_537dp);
                return holder;
            case ALBUM_SOURCE:
            case VIDEO_SOURCE:
                holder = createProgramContentHolder();
                this.mLayoutParams.height = ResourceUtil.getDimen(R.dimen.dimen_631dp);
                return holder;
            default:
                this.mLayoutParams.height = ResourceUtil.getDimen(R.dimen.dimen_366dp);
                return null;
        }
    }

    private void changeLayoutParams(Presenter object) {
        this.mAlbumInfo = object.getDetailOverlay().getAlbumInfo();
        switch (this.mAlbumInfo.getKind()) {
            case ALBUM_EPISODE:
            case VIDEO_EPISODE:
                if (DataHelper.showEpisodeAsGallery(this.mAlbumInfo.getAlbum())) {
                    this.mLayoutParams.height = ResourceUtil.getDimen(R.dimen.dimen_631dp);
                    return;
                }
                this.mLayoutParams.height = ResourceUtil.getDimen(R.dimen.dimen_537dp);
                return;
            case ALBUM_SOURCE:
            case VIDEO_SOURCE:
                this.mLayoutParams.height = ResourceUtil.getDimen(R.dimen.dimen_631dp);
                return;
            default:
                this.mLayoutParams.height = ResourceUtil.getDimen(R.dimen.dimen_366dp);
                return;
        }
    }

    public int getDetailDescRealCount() {
        if (this.mBasicinfoPanel != null) {
            return this.mBasicinfoPanel.getDetailDescRealCount();
        }
        return 0;
    }

    private ContentHolder createEpisodeContentHolder() {
        ContentWrapper wrapper = new ContentWrapper(this.mContext, new EpisodeAlbumListContent(this.mContext, PlayerAppConfig.getUIStyle().getEpisodeListUIStyle(), DetailConstants.CONTENT_TITLE_EPISODE, false));
        wrapper.setItemListener(new MyItemListener(1));
        wrapper.getView().setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        return new ContentHolder(DetailConstants.CONTENT_TAG_EPISODE, 1, wrapper);
    }

    private ContentHolder createProgramContentHolder() {
        IContent<CardModel, AlbumInfo> programs = new ProgramCardContent(this.mContext);
        programs.setItemListener(new MyItemListener(2));
        android.view.View view = programs.getView();
        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(-1, -2);
        LP.topMargin = ResourceUtil.getDimen(R.dimen.dimen_16dp);
        LP.leftMargin = ResourceUtil.getDimen(R.dimen.dimen_25dp);
        view.setLayoutParams(LP);
        return new ContentHolder(DetailConstants.CONTENT_TAG_PROGRAM, 2, programs);
    }

    public android.view.View getView() {
        if (this.mCardView == null) {
            initView();
        }
        return this.mCardView;
    }

    public android.view.View getFocusableView() {
        if (this.mInnerContentHolder != null) {
            return this.mInnerContentHolder.getWrappedContent().getFocusableView();
        }
        return this.mCtrlButtonPanel.getDefaultFocusView();
    }

    public void setEpisodeList(AlbumInfo albumInfo) {
        LogRecordUtils.logd(this.TAG, ">> set Episode/Program List, albumInfo=" + albumInfo);
        if (this.mInnerContentHolder != null) {
            IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
            if (content instanceof ProgramCardContent) {
                convertDataAndPostToCard(albumInfo, (ProgramCardContent) content);
                return;
            }
            ((ContentWrapper) content).setData(albumInfo.getEpisodeVideos());
            if (content.getFocusableView().getVisibility() == 0) {
                this.mCtrlButtonPanel.updateNextFocusDown(content.getFocusableView().getId());
            }
            startTimeKeep();
        }
    }

    public void notifyEpisodeListUpdate() {
        if (this.mInnerContentHolder != null) {
            IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
            if (content instanceof ProgramCardContent) {
                ((ProgramCardContent) content).update();
            }
        }
    }

    private void convertDataAndPostToCard(final AlbumInfo albumInfo, final ProgramCardContent content) {
        LogRecordUtils.logd(this.TAG, ">> convertDataAndPostToCard");
        ThreadUtils.execute(new Runnable() {
            public void run() {
                final CardModel card = DataHelper.convertEpisodesToCardModelAlbum(albumInfo.getEpisodeVideos());
                BasicInfoContent.this.mMainHandler.post(new Runnable() {
                    public void run() {
                        content.setData(card);
                        BasicInfoContent.this.mCtrlButtonPanel.updateNextFocusDown(content.getFocusableView().getId());
                        BasicInfoContent.this.startTimeKeep();
                    }
                });
            }
        });
    }

    public void erasePlayingIcon() {
        if (this.mInnerContentHolder != null) {
            IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
            if (content != null && !(content instanceof ContentWrapper) && (content instanceof ProgramCardContent)) {
                ((ProgramCardContent) content).setSelection(null);
            }
        }
    }

    public void setAlbumInfo(AlbumInfo albumInfo) {
        this.mAlbumInfo = albumInfo;
    }

    public void setSelection(IVideo video) {
        this.mVideo = video;
        if (StringUtils.isEmpty(video.getAlbumDesc())) {
            video.setAlbumDesc(this.mAlbumInfo.getAlbumDesc());
        }
        video.getAlbum().type = this.mAlbumInfo.getAlbum().type;
        video.getAlbum().vipInfo = this.mAlbumInfo.getAlbum().vipInfo;
        this.mAlbumInfo.setAlbum(video.getAlbum());
        if (this.mInnerContentHolder != null) {
            IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
            if (content != null) {
                if (content instanceof ContentWrapper) {
                    ((ContentWrapper) content).setSelection(this.mAlbumInfo);
                } else if (content instanceof ProgramCardContent) {
                    ((ProgramCardContent) content).setSelection(this.mAlbumInfo);
                }
            }
        }
        LogRecordUtils.logd(this.TAG, ">> setSelection AlbumInfo " + this.mAlbumInfo);
        LogRecordUtils.logd(this.TAG, ">> setSelection IVideo " + video);
        if (this.mCtrlButtonPanel != null) {
            this.mCtrlButtonPanel.setAlbumInfo(this.mAlbumInfo);
        }
        if (this.mBasicinfoPanel != null) {
            this.mBasicinfoPanel.setAlbumInfo(this.mAlbumInfo);
        }
        if (this.mPlayWindowPanel != null) {
            this.mPlayWindowPanel.setVideo(this.mAlbumInfo, video);
        }
    }

    public void show() {
        if (this.mCardView != null) {
            if (this.mInnerContentHolder != null) {
                this.mInnerContentHolder.getWrappedContent().show();
            }
            if (this.mCardView.getVisibility() != 0) {
                this.mCardView.setVisibility(0);
            }
        }
    }

    public void hide() {
        if (this.mCardView != null) {
            if (this.mInnerContentHolder != null) {
                this.mInnerContentHolder.getWrappedContent().hide();
            }
            if (this.mCardView.getVisibility() == 0) {
                this.mCardView.setVisibility(4);
            }
        }
    }

    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (this.isFirstGainFocus) {
            this.isFirstGainFocus = false;
            return this.mCtrlButtonPanel.setDefaultFocus();
        } else if (direction == 17 && getChildCount() == 2) {
            return getChildAt(1).requestFocus();
        } else {
            if (this.mUIConfig.isEnableWindowPlay()) {
                return this.mPlayWindowPanel.getPlayWindowView().requestFocus();
            }
            return this.mCtrlButtonPanel.setDefaultFocus();
        }
    }

    public void addFocusables(ArrayList<android.view.View> views, int direction, int focusableMode) {
        android.view.View parent = (android.view.View) getParent();
        if (parent == null || parent.hasFocus()) {
            super.addFocusables(views, direction, focusableMode);
        } else {
            views.add(this);
        }
    }

    public void setDefaultFocus() {
        LogRecordUtils.logd(this.TAG, "setDefaultFocus");
        this.mCtrlButtonPanel.setDefaultFocus();
    }

    public void setSummaryFocus() {
        this.mCtrlButtonPanel.requestSummaryFocus();
    }

    public void notifyBasicInfoReady(AlbumInfo albumInfo) {
        LogRecordUtils.logd(this.TAG, "notifyBasicInfoReady , albumInfo = " + albumInfo);
        this.mBasicinfoPanel.setAlbumInfo(albumInfo);
        this.mBasicinfoPanel.showOrUpdateBasicInfo();
        this.mCtrlButtonPanel.updateFullBtnText();
        this.mPlayWindowPanel.notifyBasicInfoReady();
        updateInfo(albumInfo);
    }

    public void notifyVIPInfoReady() {
        LogRecordUtils.logd(this.TAG, "notifyVIPInfoReady");
        this.mCtrlButtonPanel.notifyVIPInfoReady();
    }

    public void notifyCouponReady() {
        LogRecordUtils.logd(this.TAG, "notifyCouponReady");
        this.mCtrlButtonPanel.notifyCouponReady();
    }

    public void notifyTvodReady() {
        LogRecordUtils.logd(this.TAG, "notifyTvodReady");
        this.mCtrlButtonPanel.notifyTvodReady();
    }

    public void notifyFavInfoReady(AlbumInfo albumInfo) {
        LogRecordUtils.logd(this.TAG, "notifyFavInfoReady , albumInfo = " + albumInfo);
        if (!(albumInfo == null || this.mAlbumInfo == null)) {
            this.mAlbumInfo.setFavored(albumInfo.isFavored());
        }
        this.mCtrlButtonPanel.setFavored();
    }

    public void notifyPlayFinished() {
        LogRecordUtils.logd(this.TAG, "notifyPlayFinished");
        if (this.mInnerContentHolder != null && DetailConstants.CONTENT_TAG_PROGRAM.equals(this.mInnerContentHolder.getTag())) {
            IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
            if (content instanceof ProgramCardContent) {
                ((ProgramCardContent) content).setSelection(null);
            }
        }
    }

    public void notifyScreenModeSwitched(ScreenMode mode, boolean isError) {
        this.mCtrlButtonPanel.notifyScreenModeSwitched(mode, isError);
    }

    private void updateInfo(AlbumInfo albumInfo) {
        if (this.mInnerContentHolder != null) {
            IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
            if (content != null) {
                if (content instanceof ContentWrapper) {
                    if (isNeedEpisodeUpdateInfo()) {
                        ((ContentWrapper) content).setUpdateInfo(AlbumTextHelper.getUpdateInfo(albumInfo, this.mContext));
                    }
                } else if (content instanceof ProgramCardContent) {
                    ((ProgramCardContent) content).updateInfo(albumInfo);
                }
            }
        }
    }

    private boolean isNeedEpisodeUpdateInfo() {
        if (this.mAlbumInfo != null && this.mAlbumInfo.isSeries()) {
            int count = this.mAlbumInfo.getAlbum().tvCount;
            int set = this.mAlbumInfo.getAlbum().tvsets;
            if (!this.mAlbumInfo.isSourceType()) {
                if (set == 0 && count != 0) {
                    return true;
                }
                if (count != set && count != 0) {
                    return true;
                }
                if (count == set && set != 0) {
                    return false;
                }
            }
        }
        return false;
    }

    public ViewGroup.LayoutParams getLayoutParams() {
        return this.mLayoutParams;
    }

    public boolean isComplextContent() {
        return this.mInnerContentHolder != null;
    }

    public void onActivityResumed(int resultCode) {
        LogRecordUtils.logd(this.TAG, ">> onActivityResumed" + this.mIsInited);
        if (this.mIsInited) {
            this.mIsInited = false;
        } else if (this.mIsContentVisible || (this.mPlayWindowPanel != null && this.mPlayWindowPanel.getPlayerScreenMode() == ScreenMode.FULLSCREEN)) {
            handleResultCode(resultCode);
        } else {
            LogRecordUtils.logd(this.TAG, ">> mNeedTryWakeUp" + this.mResultCode);
            this.mNeedTryWakeUp = true;
            this.mResultCode = resultCode;
        }
        checkBasicCardVisible(true);
    }

    private void handleResultCode(int resultCode) {
        LogRecordUtils.logd(this.TAG, "handleResultCode = " + resultCode);
        switch (resultCode) {
            case 10:
                UIEventDispatcher.instance().post(this.mContext, 5, Boolean.valueOf(this.mAnonymousFav));
                break;
        }
        this.mPlayWindowPanel.wakeupPlayer(resultCode);
    }

    public void onActivityPaused() {
        LogRecordUtils.logd(this.TAG, ">> onActivityPaused mFilled " + this.mFilled);
        if (this.mFilled) {
            checkBasicCardVisible(false);
        }
        this.mPlayWindowPanel.sleepPlayer();
    }

    public void onActivityFinishing() {
        LogRecordUtils.logd(this.TAG, ">> onActivityFinishing");
        this.mPlayWindowPanel.onActivityFinishing();
        this.mBasicinfoPanel.onActivityFinishing();
        this.mCtrlButtonPanel.onActivityFinishing();
        this.mCtrlButtonPanel.setOnCtrlButtonClickedListener(null);
        this.mCtrlButtonPanel.setOnCtrlFocusChangeListener(null);
        this.mMainHandler.removeCallbacksAndMessages(null);
        this.mAlbumInfo = null;
        this.mFilled = false;
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (this.mPlayWindowPanel.handleKeyEvent(event)) {
            LogRecordUtils.logd(this.TAG, "handleKeyEvent, handled by mPlayWindowPanel, event=" + event);
            return true;
        } else if (this.mCtrlButtonPanel.handleKeyEvent(event)) {
            LogRecordUtils.logd(this.TAG, "handleKeyEvent, handled by mCtrlButtonPanel, event=" + event);
            return true;
        } else {
            LogRecordUtils.logd(this.TAG, "handleKeyEvent, unhandled key event, event=" + event);
            return false;
        }
    }

    private void startTimeKeep() {
        this.mStartTime = SystemClock.elapsedRealtime();
        this.mIsTimeKeeping = true;
    }

    private void endTimeKeep() {
        this.mEndTime = SystemClock.elapsedRealtime();
        this.mIsTimeKeeping = false;
    }

    public void updateVisibility(boolean isPlayViewVisible, boolean isScrolled, boolean changePlayerState) {
        if (isPlayViewVisible && !this.mIsTimeKeeping) {
            startTimeKeep();
        }
        if (!isPlayViewVisible && this.mIsTimeKeeping) {
            endTimeKeep();
        }
        LogRecordUtils.logd(this.TAG, "updateVisibility, isPlayViewVisible=" + isPlayViewVisible + ", isScrolled=" + isScrolled + ", changePlayerState=" + changePlayerState + ", mIsContentVisible=" + this.mIsContentVisible);
        if (isPlayViewVisible != this.mIsContentVisible) {
            if (changePlayerState) {
                handleContentVisibilityChanged(isPlayViewVisible, isScrolled);
            }
            this.mIsContentVisible = isPlayViewVisible;
        }
        if (isContentVisible() && this.mInnerContentHolder != null) {
            IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
            if (content instanceof ProgramCardContent) {
                ((ProgramCardContent) content).reloadBitmap();
                LogRecordUtils.logd(this.TAG, "updateVisibility, reloadBitmap");
            }
        }
    }

    public void checkBasicCardVisible(boolean visible) {
        LogRecordUtils.logd(this.TAG, "checkEpisodelistViewVisible:" + visible);
        if (!visible && isContentVisible()) {
            sendCardShowPingback();
        }
    }

    public boolean isContentVisible() {
        boolean isContentVisible = false;
        if (this.mInnerContentHolder != null) {
            IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
            if (content == null || content.getView() == null) {
                LogRecordUtils.logd(this.TAG, "content is  exception");
            } else {
                isContentVisible = !this.mDetailOverlay.isViewScrolled();
            }
            LogRecordUtils.logd(this.TAG, "isContentVisible = " + isContentVisible);
        }
        return isContentVisible;
    }

    public boolean isEquityShow() {
        if (this.mCtrlButtonPanel != null) {
            return this.mCtrlButtonPanel.isEquityShow();
        }
        return false;
    }

    private void handleContentVisibilityChanged(boolean isPlayViewVisible, boolean isScrolled) {
        LogRecordUtils.logd(this.TAG, "handleContentVisibilityChanged, isPlayViewVisible=" + isPlayViewVisible + ", isScrolled=" + isScrolled);
        if (isPlayViewVisible) {
            if (isScrolled) {
                this.mPlayWindowPanel.addPlayerSurfaceView();
            }
            this.mPlayWindowPanel.resumePlayer();
            if (this.mNeedTryWakeUp) {
                this.mPlayWindowPanel.wakeupPlayer(this.mResultCode);
                this.mNeedTryWakeUp = false;
                this.mResultCode = 0;
                return;
            }
            return;
        }
        if (isScrolled) {
            this.mPlayWindowPanel.removePlayerSurfaceView();
        }
        this.mPlayWindowPanel.pausePlayer();
    }

    public List<AbsVoiceAction> getPlayerSupportedVoices(List<AbsVoiceAction> actions) {
        if (this.mPlayWindowPanel != null) {
            actions = this.mPlayWindowPanel.getSupportedVoices(actions);
        }
        IVoiceCommon provider = CreateInterfaceTools.createVoiceCommon();
        actions.add(provider.createAbsVoiceAction(ResourceUtil.getStr(R.string.voice_cmd_collection), new FavorRunnable(false), KeyWordType.DEFAULT));
        actions.add(provider.createAbsVoiceAction(ResourceUtil.getStr(R.string.voice_cmd_cancel_collection), new FavorRunnable(true), KeyWordType.DEFAULT));
        if (this.mUIConfig.isEnableWindowPlay()) {
            actions.add(provider.createAbsVoiceAction(ResourceUtil.getStr(R.string.voice_cmd_full_screen), new FullScreenRunnable(), KeyWordType.DEFAULT));
        }
        return actions;
    }

    public void onBind(Presenter object) {
        Log.v(this.TAG, "onBind , hashcode = " + hashCode() + " mHasBind = " + this.mHasBind);
        changeLayoutParams(object);
        object.setViewLayoutParams(this.mLayoutParams);
        if (!this.mHasBind) {
            object.getDetailOverlay().onBind(this);
            object.setView(this);
            this.mHasBind = true;
        }
    }

    public void onUnbind(Presenter object) {
        Log.v(this.TAG, "onUnbind");
    }

    public void onShow(Presenter object) {
        Log.v(this.TAG, "onShow");
    }

    public void onHide(Presenter object) {
        Log.v(this.TAG, "onHide");
    }

    public void startTrailers(PlayParams param) {
        this.mPlayWindowPanel.startTrailer(param);
    }

    public void handlerFavor(boolean isFavored) {
        boolean isLogin = GetInterfaceTools.getIGalaAccountManager().isLogin(this.mContext);
        LogRecordUtils.logd(this.TAG, ">> isLogin = " + isLogin);
        if (isLogin) {
            UIEventDispatcher.instance().post(this.mContext, 5, Boolean.valueOf(isFavored));
            return;
        }
        this.mAnonymousFav = isFavored;
        UIEventDispatcher.instance().post(this.mContext, 11, Boolean.valueOf(isFavored));
    }

    public void setCurrentFocusView(android.view.View view) {
        if (this.mCtrlButtonPanel != null) {
            this.mCtrlButtonPanel.setCurrentFocusView(view);
        }
    }

    private void handleEpisodeClicked(Object data, int index) {
        LogRecordUtils.logd(this.TAG, ">> handleEpisodeClicked, data=" + data);
        if (isPlayerSurfaceValidate()) {
            AlbumInfo albumInfo;
            if (data instanceof AlbumInfo) {
                albumInfo = (AlbumInfo) data;
            } else {
                albumInfo = (AlbumInfo) this.mAlbumInfo.getEpisodeVideos().get(index);
            }
            IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
            if (content instanceof ContentWrapper) {
                ((ContentWrapper) content).setSelection(albumInfo);
            }
            int curIndex = DataHelper.findVideoIndexByTvidAlbum(this.mAlbumInfo.getEpisodeVideos(), this.mAlbumInfo);
            if (curIndex >= 0) {
                boolean isPre = false;
                if (albumInfo.getAlbum().getContentType() == ContentType.PREVUE) {
                    isPre = true;
                }
                String tvId = albumInfo.getTvId();
                String nowQPID = this.mAlbumInfo.getAlbumId();
                String prevue = isPre ? "1" : "0";
                PingBackCollectionFieldUtils.setNow_c1(Integer.toString(this.mAlbumInfo.getChannelId()));
                PingBackCollectionFieldUtils.setNow_qpid(nowQPID);
                PingbackFactory.instance().createPingback(34).addItem(PingbackStore.R.R_TYPE(tvId)).addItem(BLOCKTYPE.VIDEOLIST).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(String.valueOf(index + 1))).addItem(RPAGETYPE.DETAIL).addItem(ISPREVUE.ISPREVUE_TYPE(prevue)).addItem(C1.C1_TYPE(String.valueOf(this.mAlbumInfo.getChannelId()))).addItem(this.mPingbackContext.getItem("e")).addItem(this.mPingbackContext.getItem("rfr")).addItem(NOW_C1.NOW_C1_TYPE(Integer.toString(this.mAlbumInfo.getChannelId()))).addItem(NOW_QPID.NOW_QPID_TYPE(nowQPID)).addItem(NOW_EPISODE.ITEM(Integer.toString(curIndex + 1))).addItem(LINE.ITEM("1")).addItem(new PingbackItem(Keys.CARDLINE, "1")).addItem(new PingbackItem(Keys.ALLLINE, "1")).post();
                this.mVideo = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getVideoItemFactory().createVideoItem(this.mSourceType, albumInfo.getAlbum(), new MyPlayerProfile());
                this.mPlayWindowPanel.startPlay(this.mVideo);
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "handleEpisodeClicked, invalid current index!!!");
            }
        }
    }

    public void setPlayParamsSourceType(SourceType sourceType) {
        this.mSourceType = sourceType;
    }

    private void handleProgramClicked(Object data, int index) {
        LogRecordUtils.logd(this.TAG, ">> handleProgramClicked, data=" + data + ", index=" + index);
        if (this.mAlbumInfo == null) {
            LogRecordUtils.logd(this.TAG, "handleProgramClicked, mCurVideo is null.");
        } else if (isPlayerSurfaceValidate()) {
            AlbumInfo albumInfo = (AlbumInfo) this.mAlbumInfo.getEpisodeVideos().get(index);
            IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
            if ((content instanceof ProgramCardContent) && !DataHelper.isSameVideoByTVID(albumInfo, this.mAlbumInfo)) {
                ((ProgramCardContent) content).setSelection(albumInfo);
            }
            int curIndex = DataHelper.findVideoIndexByTvidAlbum(this.mAlbumInfo.getEpisodeVideos(), this.mAlbumInfo);
            if (curIndex >= 0) {
                boolean isPre = false;
                if (albumInfo.getAlbum().getContentType() == ContentType.PREVUE) {
                    isPre = true;
                }
                String tvId = albumInfo.getTvId();
                String nowQPID = this.mAlbumInfo.getAlbumId();
                String prevue = isPre ? "1" : "0";
                PingBackCollectionFieldUtils.setNow_c1(Integer.toString(this.mAlbumInfo.getChannelId()));
                PingBackCollectionFieldUtils.setNow_qpid(nowQPID);
                PingbackFactory.instance().createPingback(34).addItem(PingbackStore.R.R_TYPE(tvId)).addItem(BLOCKTYPE.VIDEOLIST).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(String.valueOf(index + 1))).addItem(RPAGETYPE.DETAIL).addItem(ISPREVUE.ISPREVUE_TYPE(prevue)).addItem(C1.C1_TYPE(String.valueOf(this.mAlbumInfo.getChannelId()))).addItem(this.mPingbackContext.getItem("e")).addItem(this.mPingbackContext.getItem("rfr")).addItem(NOW_C1.NOW_C1_TYPE(Integer.toString(this.mAlbumInfo.getChannelId()))).addItem(NOW_QPID.NOW_QPID_TYPE(nowQPID)).addItem(NOW_EPISODE.ITEM(Integer.toString(curIndex + 1))).addItem(LINE.ITEM("1")).addItem(new PingbackItem(Keys.CARDLINE, "1")).addItem(new PingbackItem(Keys.ALLLINE, "1")).post();
                this.mVideo = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getVideoItemFactory().createVideoItem(this.mSourceType, albumInfo.getAlbum(), new MyPlayerProfile());
                this.mPlayWindowPanel.startPlay(this.mVideo);
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "handleProgramClicked, invalid current index!!!");
            }
        }
    }

    private boolean isPlayerSurfaceValidate() {
        if (!this.mUIConfig.isEnableWindowPlay() || this.mPlayWindowPanel == null || this.mPlayWindowPanel.isPlayerSurfaceValidate()) {
            return true;
        }
        return false;
    }

    private void sendFullButtonClickedPingback() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> sendFullButtonClickedPingback");
        }
        if (this.mAlbumInfo != null && this.mPingbackContext != null) {
            PingbackFactory.instance().createPingback(2).addItem(PingbackStore.R.R_TYPE(this.mAlbumInfo.getAlbum().qpId)).addItem(this.mPingbackContext.getItem("block")).addItem(RTTYPE.RT_I).addItem(RSEATTYPE.FULLSCREEN).addItem(RPAGETYPE.DETAIL).addItem(C1.C1_TYPE(String.valueOf(this.mAlbumInfo.getAlbum().chnId))).addItem(this.mPingbackContext.getItem("e")).addItem(this.mPingbackContext.getItem("rfr")).addItem(NOW_QPID.NOW_QPID_TYPE(this.mAlbumInfo.getAlbumId())).addItem(NOW_C1.NOW_C1_TYPE(String.valueOf(this.mAlbumInfo.getChannelId()))).post();
        }
    }

    private void sendCardShowPingback() {
        LogRecordUtils.logd(this.TAG, "sendCardShowPingback, mAlbumInfo " + this.mAlbumInfo + ", mPingbackContext " + this.mPingbackContext);
        if (!isComplextContent()) {
            Log.v(this.TAG, "mInnerContentHolder is null");
        } else if (this.mAlbumInfo == null) {
            LogRecordUtils.logd(this.TAG, "sendCardShowPingback, mCurVideo is null.");
        } else {
            String cardline = "1";
            String allline = "1";
            if (this.mInnerContentHolder != null) {
                IContent<?, ?> content = this.mInnerContentHolder.getWrappedContent();
                if (content instanceof ProgramCardContent) {
                    int dftitem = ((ProgramCardContent) content).getDftItemCount();
                    int sawitem = ((ProgramCardContent) content).getSawItemCount();
                }
            }
            PingbackFactory.instance().createPingback(15).addItem(BTSPTYPE.BSTP_1).addItem(C1.C1_TYPE(String.valueOf(this.mAlbumInfo.getChannelId()))).addItem(QTCURLTYPE.DETAIL).addItem(QPLD.QPLD_TYPE(this.mAlbumInfo.getAlbumId())).addItem(this.mPingbackContext.getItem("rfr")).addItem(this.mPingbackContext.getItem("e")).addItem(PAGE_SHOW.BLOCKTYPE.VIDEOLIST).addItem(LINE.ITEM(String.valueOf(1))).addItem(new PingbackItem(Keys.CARDLINE, cardline)).addItem(new PingbackItem(Keys.ALLLINE, allline)).addItem(NOW_C1.NOW_C1_TYPE(String.valueOf(this.mAlbumInfo.getChannelId()))).post();
        }
    }
}
