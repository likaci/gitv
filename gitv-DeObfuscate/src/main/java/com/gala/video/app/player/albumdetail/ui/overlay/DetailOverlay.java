package com.gala.video.app.player.albumdetail.ui.overlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackItem;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.PAGE_SHOW.BTSPTYPE;
import com.gala.pingback.PingbackStore.PAGE_SHOW.QTCURLTYPE;
import com.gala.pingback.PingbackStore.QPLD;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.KeyWordType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.albumlist.widget.BlocksView.ItemDecoration;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumVideoItem;
import com.gala.video.app.player.albumdetail.data.AllViewBlocksView;
import com.gala.video.app.player.albumdetail.data.AllViewBlocksView.OnBackClickedListener;
import com.gala.video.app.player.albumdetail.data.PingbackActionPolicy;
import com.gala.video.app.player.albumdetail.data.cache.AlbumInfoCacheManager;
import com.gala.video.app.player.albumdetail.ui.IDetailOverlay;
import com.gala.video.app.player.albumdetail.ui.card.BasicInfoContent;
import com.gala.video.app.player.albumdetail.ui.overlay.panels.FullDescriptionPanel;
import com.gala.video.app.player.albumdetail.ui.overlay.panels.FullDescriptionPanel.OnFullDescClickedListener;
import com.gala.video.app.player.controller.IKeyEventListener;
import com.gala.video.app.player.controller.KeyDispatcher;
import com.gala.video.app.player.pingback.detail.DetailPingBackUtils;
import com.gala.video.app.player.ui.config.IAlbumDetailUiConfig;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectInfoModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.voice.IVoiceCommon;
import com.gala.video.lib.share.pingback.AlbumDetailPingbackUtils;
import com.gala.video.lib.share.uikit.UIKitEngine;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig.Source;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.item.StandardItem;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailOverlay implements IDetailOverlay, IKeyEventListener {
    private static boolean mActivityResumed = false;
    private final String TAG = ("Detail/UI/DetailOverlay@" + Integer.toHexString(hashCode()));
    private long UIKITVIEWHIDEDELAYTIME = 1000;
    private boolean isPlayFromAllView = false;
    private AlbumInfo mAlbumInfo;
    private AlbumVideoItem mAlbumVideoItem;
    private AllViewBlocksView mAllBlocksView;
    private UIKitEngine mAllViewEngine;
    private BasicInfoContent mBasicInfoContent;
    private BlocksView mBlocksView;
    private CardFocusHelper mCardFocusHelper;
    private PingbackActionPolicy mCardShowAllViewPingbackActionPolicy;
    private PingbackActionPolicy mCardShowPingbackActionPolicy;
    private Context mContext;
    private ScreenMode mCurScreenMode = ScreenMode.WINDOWED;
    private UIKitEngine mEngine;
    private DetailStarsErrorView mErrorView;
    private OnFullDescClickedListener mFullDescClickedListener = new C13323();
    private FullDescriptionPanel mFullDescPanel;
    private IMultiSubjectInfoModel mIntentModel;
    private boolean mIsAppDownloadComplete = false;
    private boolean mIsWindowPlay = false;
    private TextView mLoadingView;
    private IPingbackContext mPingbackContext;
    private View mRootView;
    private IAlbumDetailUiConfig mUiConfig;
    public Runnable mUikitAllEngineViewHideRunnable = new C13345();
    public Runnable mUikitEngineViewHideRunnable = new C13334();
    private IVideo mVideo;

    class C13301 implements OnBackClickedListener {
        C13301() {
        }

        public void onBackClicked() {
            Log.v(DetailOverlay.this.TAG, "onBackClicked");
            DetailOverlay.this.mCardShowAllViewPingbackActionPolicy.onSendCardShowPingback(DetailOverlay.this.mAllBlocksView, DetailOverlay.this.mAllViewEngine.getPage(), false, true, false);
            DetailOverlay.this.mAllViewEngine.setData(new ArrayList());
            DetailOverlay.this.mAllBlocksView.hide();
            DetailOverlay.this.mAllViewEngine.stop();
            DetailOverlay.this.mAllBlocksView.setFocusPosition(0);
            DetailOverlay.this.mAllViewEngine.getPage().backToTop();
            DetailOverlay.this.mBlocksView.setVisibility(0);
            DetailOverlay.this.mBasicInfoContent.updateVisibility(false, true, true);
            DetailOverlay.this.mBasicInfoContent.checkBasicCardVisible(true);
            DetailOverlay.this.mBasicInfoContent.notifyEpisodeListUpdate();
        }
    }

    class C13312 extends ItemDecoration {
        C13312() {
        }

        public int getItemOffsets(int i, BlocksView blocksView) {
            if (i == 0) {
                return ResourceUtil.getDimen(C1291R.dimen.dimen_010dp);
            }
            return 0;
        }
    }

    class C13323 implements OnFullDescClickedListener {
        C13323() {
        }

        public void onFullDescClicked() {
            DetailOverlay.this.getFullDescriptionPanel().hide();
            DetailOverlay.this.mBasicInfoContent.setDefaultFocus();
            DetailOverlay.this.mPingbackContext.setItem("block", new PingbackItem("block", DetailPingBackUtils.getBlock(DetailOverlay.this.mAlbumInfo)));
            DetailOverlay.this.sendDetailPageShowPingback();
            if (DetailOverlay.this.mBasicInfoContent.isEquityShow()) {
                DetailOverlay.this.mBasicInfoContent.updateVisibility(true, false, true);
                DetailOverlay.this.mBasicInfoContent.checkBasicCardVisible(true);
            } else {
                DetailOverlay.this.mBasicInfoContent.updateVisibility(true, false, true);
                DetailOverlay.this.mBasicInfoContent.checkBasicCardVisible(true);
            }
        }
    }

    class C13334 implements Runnable {
        C13334() {
        }

        public void run() {
            DetailOverlay.this.mEngine.stop();
            DetailOverlay.this.mEngine.hide();
        }
    }

    class C13345 implements Runnable {
        C13345() {
        }

        public void run() {
            DetailOverlay.this.mAllViewEngine.setData(new ArrayList());
            DetailOverlay.this.mAllBlocksView.hide();
            DetailOverlay.this.mAllViewEngine.stop();
        }
    }

    class RecommendRunnable implements Runnable {
        private ItemModel mItem;

        public RecommendRunnable(ItemModel item) {
            this.mItem = item;
        }

        public void run() {
            DataSource itemData = CreateInterfaceTools.createModelHelper().convertToDataSource(this.mItem);
            ItemDataType itemDataType = this.mItem.getItemType();
            if (DetailOverlay.this.mIsWindowPlay && itemDataType == ItemDataType.TRAILERS) {
                LogRecordUtils.logd(DetailOverlay.this.TAG, ">> RecommendRunnable album " + DataUtils.albumInfoToString(this.mItem.getData().getVideo()));
                List<Album> trailerlist = new ArrayList();
                for (ItemModel item : DetailOverlay.this.mIntentModel.getCardModel().getItemModelList()) {
                    if (item != null) {
                        trailerlist.add(item.getData().getVideo());
                    }
                }
                PlayParams playParams = new PlayParams();
                playParams.continuePlayList = trailerlist;
                playParams.playListId = "";
                playParams.playIndex = DetailOverlay.this.mIntentModel.getPlayIndex();
                playParams.sourceType = SourceType.DETAIL_TRAILERS;
                playParams.isDetailTrailer = true;
                DetailOverlay.this.mBasicInfoContent.startTrailers(playParams);
                return;
            }
            CreateInterfaceTools.createMultiSubjectUtils().OnItemClick(DetailOverlay.this.mContext, itemData, DetailOverlay.this.mIntentModel);
        }
    }

    public boolean isWindowPlay() {
        return this.mIsWindowPlay;
    }

    public DetailOverlay(Context context, View root, IAlbumDetailUiConfig config) {
        this.mContext = context;
        this.mRootView = root;
        this.mUiConfig = config;
        this.mIsWindowPlay = this.mUiConfig.isEnableWindowPlay();
        this.mPingbackContext = (IPingbackContext) this.mContext;
        initView();
        initPolicy();
        KeyDispatcher.instance().register(this);
    }

    private void initView() {
        this.mBlocksView = (BlocksView) this.mRootView.findViewById(C1291R.id.detail_scroll_view);
        this.mEngine = UIKitEngine.newInstance(this.mContext);
        this.mEngine.bindView(this.mBlocksView);
        this.mBlocksView.setClipToPadding(false);
        this.mBlocksView.setClipChildren(false);
        this.mBlocksView.setShakeForbidden(33);
        initAllView();
        this.mCardFocusHelper = new CardFocusHelper(this.mRootView.findViewById(C1291R.id.card_focus));
        LayoutParams lp = this.mBlocksView.getLayoutParams();
        if (lp instanceof MarginLayoutParams) {
            this.mCardFocusHelper.setInvisiableMarginTop(((MarginLayoutParams) lp).topMargin);
        }
    }

    public AlbumInfo getAlbumInfo() {
        return this.mAlbumInfo;
    }

    private void initAllView() {
        this.mAllBlocksView = (AllViewBlocksView) this.mRootView.findViewById(C1291R.id.detail_all_view);
        this.mAllViewEngine = UIKitEngine.newInstance(this.mContext);
        this.mAllViewEngine.bindView(this.mAllBlocksView);
        this.mAllBlocksView.setClipToPadding(false);
        this.mAllBlocksView.setClipChildren(false);
        this.mAllBlocksView.setShakeForbidden(33);
        this.mAllBlocksView.setBackClickedListener(new C13301());
    }

    public void initPolicy() {
        this.mCardShowPingbackActionPolicy = new PingbackActionPolicy(this.mEngine.getPage(), this.mContext);
        this.mEngine.getPage().registerActionPolicy(this.mCardShowPingbackActionPolicy);
        this.mCardShowAllViewPingbackActionPolicy = new PingbackActionPolicy(this.mAllViewEngine.getPage(), this.mContext);
        this.mAllViewEngine.getPage().registerActionPolicy(this.mCardShowAllViewPingbackActionPolicy);
    }

    public void startTrailers(PlayParams param) {
        this.mBasicInfoContent.startTrailers(param);
    }

    public void startPlayFromAllView(boolean isplayFromAllView) {
        this.isPlayFromAllView = isplayFromAllView;
    }

    public void playTraAllView() {
        this.mCardShowAllViewPingbackActionPolicy.onSendCardShowPingback(this.mAllBlocksView, this.mAllViewEngine.getPage(), false, true, false);
        this.mAllViewEngine.setData(new ArrayList());
        this.mAllBlocksView.hide();
        this.mAllViewEngine.stop();
    }

    public void showAllViews() {
        if (this.mCardShowPingbackActionPolicy != null) {
            this.mCardShowPingbackActionPolicy.initTimestamp(this.mBlocksView);
            this.mCardShowPingbackActionPolicy.onSendCardShowPingback(this.mBlocksView, this.mEngine.getPage(), false, false, true);
        }
        this.mBlocksView.setVisibility(8);
        this.mAllViewEngine.getPage().hideLoading();
        this.mAllViewEngine.start();
        this.mAllBlocksView.show();
        this.mBasicInfoContent.updateVisibility(false, false, true);
    }

    public void setAllViewPlayGif() {
        ItemInfoModel itemInfoModel = getLastItemInfoModel();
        if (!this.mIsWindowPlay || itemInfoModel == null) {
            LogRecordUtils.logd(this.TAG, ">> mIsWindowPlay is false or itemInfoModel =null");
            return;
        }
        List cards = this.mAllViewEngine.getPage().getCards();
        if (ListUtils.isEmpty(cards)) {
            LogRecordUtils.logd(this.TAG, ">> setAllViewPlayGif why cardList null");
            return;
        }
        List<Item> items = null;
        int i = 0;
        while (i < cards.size()) {
            if (((Card) cards.get(i)).getModel().mSource != null && (((Card) cards.get(i)).getModel().mSource.equals(Source.TRAILERS) || ((Card) cards.get(i)).getModel().mSource.equals(Source.ABOUT_TOPIC) || ((Card) cards.get(i)).getModel().mSource.equals(Source.SUPER_ALBUM))) {
                items = ((Card) cards.get(i)).getItems();
                break;
            }
            i++;
        }
        if (items == null || items.size() <= 0) {
            LogRecordUtils.logd(this.TAG, ">> setAllViewPlayGif why items.size() < 0");
            return;
        }
        for (i = 0; i < items.size(); i++) {
            if (items.get(i) != null) {
                String nowTraTVid = itemInfoModel.getCuteViewData(UIKitConfig.KEY_DETAIL_SPECIAL_DATA_ALLVIEW, UIKitConfig.KEY_SPECIAL_DATA_ALLVIEW_PLAYING_TVID);
                String tvID = ((Item) items.get(i)).getModel().getCuteViewData(UIKitConfig.KEY_DETAIL_SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_TVID);
                LogRecordUtils.logd(this.TAG, ">> setAllViewPlayGif  tvID  ->" + tvID + " nowTraTVid  ->" + nowTraTVid);
                boolean isPlaying = StringUtils.equals(tvID, nowTraTVid) && this.mIsWindowPlay;
                ((StandardItem) items.get(i)).setPlayingGif(isPlaying);
            }
        }
    }

    public ItemInfoModel getLastItemInfoModel() {
        LogRecordUtils.logd(this.TAG, ">> getLastItemInfoModel");
        List cards = this.mEngine.getPage().getCards();
        if (!ListUtils.isEmpty(cards)) {
            List<Item> items = null;
            int i = 0;
            while (i < cards.size()) {
                if (((Card) cards.get(i)).getModel().mSource != null && (((Card) cards.get(i)).getModel().mSource.equals(Source.TRAILERS) || ((Card) cards.get(i)).getModel().mSource.equals(Source.ABOUT_TOPIC) || ((Card) cards.get(i)).getModel().mSource.equals(Source.SUPER_ALBUM))) {
                    items = ((Card) cards.get(i)).getItems();
                    break;
                }
                i++;
            }
            if (items != null && items.size() > 0) {
                if (items.get(items.size() - 1) != null) {
                    return ((Item) items.get(items.size() - 1)).getModel();
                }
                LogRecordUtils.logd(this.TAG, ">> items.get(items.size() - 1) == null");
            }
        }
        return null;
    }

    public void setCurrentFocusView(View view) {
        this.mBasicInfoContent.setCurrentFocusView(view);
    }

    public void showLoading() {
        LogRecordUtils.logd(this.TAG, ">> showLoading");
        ViewGroup rootView = this.mRootView;
        this.mLoadingView = (TextView) LayoutInflater.from(rootView.getContext()).inflate(C1291R.layout.share_cardlist_loading, null);
        FrameLayout.LayoutParams LP = new FrameLayout.LayoutParams(-2, -2);
        LP.gravity = 17;
        this.mLoadingView.setLayoutParams(LP);
        rootView.addView(this.mLoadingView);
        ((AnimationDrawable) this.mLoadingView.getCompoundDrawables()[0]).start();
    }

    public void notifyVideoDataCreated(AlbumVideoItem albumVideoItem) {
        this.mAlbumInfo = albumVideoItem.getAlbumInfo();
        this.mVideo = albumVideoItem.getVideo();
        this.mAlbumVideoItem = albumVideoItem;
        Album album = AlbumInfoCacheManager.getInstance().getCacheAlbum(this.mAlbumInfo.getAlbumId());
        if (album != null) {
            album.order = this.mVideo.getPlayOrder();
        }
        this.mAlbumInfo.setPlayOrder(this.mVideo.getPlayOrder());
        this.mAlbumInfo.setTvId(this.mVideo.getTvId());
        this.mCardShowPingbackActionPolicy.setAlbumInfo(this.mAlbumInfo);
        this.mCardShowAllViewPingbackActionPolicy.setAlbumInfo(this.mAlbumInfo);
        this.mCardShowAllViewPingbackActionPolicy.setisAllView(true);
        LogRecordUtils.logd(this.TAG, ">> notifyVideoDataCreated, child count " + this.mBlocksView.getChildCount());
        if (this.mLoadingView != null) {
            ((ViewGroup) this.mRootView).removeView(this.mLoadingView);
            this.mLoadingView = null;
        }
        CardInfoModel cardInfoModel = new CardInfoModel();
        cardInfoModel.setCardType(UIKitConfig.CARD_TYPE_DETAIL_TOP);
        cardInfoModel.detailCreateInfo = this;
        List<CardInfoModel> list = new ArrayList();
        list.add(cardInfoModel);
        this.mEngine.setDataSync(list);
    }

    public IMultiSubjectInfoModel getIntentModel() {
        return this.mIntentModel;
    }

    public void onBind(BasicInfoContent basicInfoContent) {
        this.mBasicInfoContent = basicInfoContent;
        this.mBasicInfoContent.init(this.mContext, this.mRootView, this.mUiConfig, this);
        LogRecordUtils.logd(this.TAG, ">> mBasicInfoContent " + this.mBasicInfoContent.getRootView());
        this.mBasicInfoContent.setPlayParamsSourceType(this.mAlbumVideoItem.getSourceType());
        this.mBasicInfoContent.setAlbumInfo(this.mAlbumInfo);
        this.mBasicInfoContent.setSelection(this.mAlbumVideoItem.getVideo());
        this.mBasicInfoContent.getView();
        this.mIntentModel = createIntentModel();
        this.mBasicInfoContent.show();
        correctLineOneMargin();
        if (mActivityResumed) {
            this.mBasicInfoContent.onActivityResumed(0);
            this.mBasicInfoContent.setDefaultFocus();
        }
        this.mAlbumVideoItem.getDataProvider().startLoad();
    }

    private void correctLineOneMargin() {
        int height = this.mBasicInfoContent.getLayoutParams().height;
        LogRecordUtils.logd(this.TAG, ">> correctLineOneMargin height " + height);
        if (height >= ResourceUtil.getDimen(C1291R.dimen.dimen_620dp)) {
            this.mBlocksView.setItemDecoration(new C13312());
        }
    }

    private IMultiSubjectInfoModel createIntentModel() {
        String from;
        String tabSrc;
        IMultiSubjectInfoModel multiSubjectInfoModel = CreateInterfaceTools.createMultiSubjectInfoModel();
        Intent intent = ((Activity) this.mContext).getIntent();
        String oriFrom = intent.getStringExtra("from");
        if (oriFrom == null) {
            oriFrom = "";
        }
        if (oriFrom.startsWith("openAPI") || oriFrom.equals(IntentConfig2.FROM_EXIT_DIALOG)) {
            from = "openAPI_detail";
        } else {
            from = "detail";
        }
        if (oriFrom.startsWith("openAPI")) {
            tabSrc = "其他";
        } else {
            tabSrc = "tab_" + PingBackUtils.getTabName();
        }
        AlbumDetailPingbackUtils.getInstance().setS2(from);
        AlbumDetailPingbackUtils.getInstance().setAllViewS2(from);
        AlbumDetailPingbackUtils.getInstance().setTabSrc(tabSrc);
        String buySource = intent.getStringExtra("buy_source");
        multiSubjectInfoModel.setFrom(from);
        multiSubjectInfoModel.setBuysource(buySource != null ? buySource : "");
        multiSubjectInfoModel.setItemId("");
        multiSubjectInfoModel.setPlayType("");
        LogRecordUtils.logd(this.TAG, ">> createIntentModel buySource=" + buySource + ", from=" + from + " ");
        return multiSubjectInfoModel;
    }

    public void updateBasicInfo(AlbumInfo albumInfo) {
        if (LogUtils.mIsDebug) {
            LogRecordUtils.logd(this.TAG, ">> updateBasicInfo " + albumInfo);
        }
        if (LogUtils.mIsDebug) {
            LogRecordUtils.logd(this.TAG, ">> albumInfo.getAlbum().type " + albumInfo.getAlbum().type);
        }
        this.mPingbackContext.setItem("block", new PingbackItem("block", DetailPingBackUtils.getBlock(albumInfo)));
        this.mBasicInfoContent.notifyBasicInfoReady(albumInfo);
    }

    public void updateVIPInfo(AlbumInfo albumInfo) {
        LogRecordUtils.logd(this.TAG, ">> updateVIPInfo " + albumInfo);
        this.mBasicInfoContent.notifyVIPInfoReady();
        this.mPingbackContext.setItem("block", new PingbackItem("block", DetailPingBackUtils.getBlock(albumInfo)));
        if (this.mCurScreenMode == ScreenMode.WINDOWED) {
            sendDetailPageShowPingback();
        }
    }

    public void updateCoupon(AlbumInfo albumInfo) {
        LogRecordUtils.logd(this.TAG, ">> updateCoupon " + albumInfo);
        this.mBasicInfoContent.notifyCouponReady();
    }

    public void updateTvod(AlbumInfo albumInfo) {
        LogRecordUtils.logd(this.TAG, ">> updateTvod " + albumInfo);
        this.mBasicInfoContent.notifyTvodReady();
    }

    public void updateFavInfo(AlbumInfo albumInfo) {
        LogRecordUtils.logd(this.TAG, ">> updateFavInfo " + albumInfo);
        this.mBasicInfoContent.notifyFavInfoReady(albumInfo);
    }

    public void updateEpisodeList(AlbumInfo albumInfo) {
        LogRecordUtils.logd(this.TAG, ">> updateEpisodeList " + albumInfo);
        if (albumInfo != null) {
            this.mBasicInfoContent.setEpisodeList(albumInfo);
        }
    }

    public void showFullDescPanel(AlbumInfo albumInfo) {
        getFullDescriptionPanel().show(DataHelper.getVideoTitle(albumInfo), DataHelper.getVideoDesc(albumInfo), DataHelper.getVideoImageURL(albumInfo), this.mBasicInfoContent.getDetailDescRealCount());
        this.mBasicInfoContent.updateVisibility(false, false, true);
        this.mBasicInfoContent.checkBasicCardVisible(false);
        if (this.mCardShowPingbackActionPolicy != null) {
            this.mCardShowPingbackActionPolicy.initTimestamp(this.mBlocksView);
            this.mCardShowPingbackActionPolicy.onSendCardShowPingback(this.mBlocksView, this.mEngine.getPage(), false, false, true);
        }
    }

    private void showMainView() {
        LogRecordUtils.logd(this.TAG, ">> showMainView ");
        if (!isViewScrolled()) {
            this.mBasicInfoContent.setDefaultFocus();
        }
        this.mBlocksView.setVisibility(0);
    }

    public void notifyVideoSwitched(IVideo video) {
        LogRecordUtils.logd(this.TAG, ">> notifyVideoSwitched video " + video);
        if (video == null) {
            LogRecordUtils.logd(this.TAG, "notifyVideoSwitched, error, video is null!!!");
        } else if (this.mBasicInfoContent == null) {
            LogRecordUtils.logd(this.TAG, "notifyVideoSwitched, error, mBasicInfoContent is null!!!");
        } else {
            this.mBasicInfoContent.setSelection(video);
            eraseTrailersPlayIcon();
        }
    }

    public ViewGroup getView() {
        return this.mBlocksView;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyScreenModeSwitched(com.gala.sdk.player.ScreenMode r8, boolean r9) {
        /*
        r7 = this;
        r5 = 1;
        r3 = 0;
        r0 = r7.TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "notifyScreenModeSwitched ";
        r1 = r1.append(r2);
        r1 = r1.append(r8);
        r2 = " isError ";
        r1 = r1.append(r2);
        r1 = r1.append(r9);
        r2 = "mCurScreenMode";
        r1 = r1.append(r2);
        r2 = r7.mCurScreenMode;
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils.logd(r0, r1);
        r0 = r7.mBasicInfoContent;
        r0.notifyScreenModeSwitched(r8, r9);
        if (r9 == 0) goto L_0x0045;
    L_0x003a:
        r7.mCurScreenMode = r8;
        r0 = r7.mBasicInfoContent;
        r0.updateVisibility(r5, r3, r3);
        r7.eraseTrailersPlayIcon();
    L_0x0044:
        return;
    L_0x0045:
        r0 = com.gala.sdk.player.ScreenMode.FULLSCREEN;
        if (r8 != r0) goto L_0x00b6;
    L_0x0049:
        r0 = r7.TAG;
        r1 = ">> hideMainView ";
        com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils.logd(r0, r1);
        r0 = r7.mRootView;
        r0.clearFocus();
        r0 = r7.mBlocksView;
        r1 = 4;
        r0.setVisibility(r1);
        r0 = r7.mCurScreenMode;
        r1 = com.gala.sdk.player.ScreenMode.WINDOWED;
        if (r0 != r1) goto L_0x00a4;
    L_0x0062:
        r0 = r7.mBasicInfoContent;
        r0.updateVisibility(r3, r3, r3);
        r0 = r7.mBasicInfoContent;
        r0.checkBasicCardVisible(r3);
        r0 = r7.TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = ">> isPlayFromAllView = ";
        r1 = r1.append(r2);
        r2 = r7.isPlayFromAllView;
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils.logd(r0, r1);
        r0 = r7.mCardShowPingbackActionPolicy;
        if (r0 == 0) goto L_0x00a4;
    L_0x008b:
        r0 = r7.isPlayFromAllView;
        if (r0 != 0) goto L_0x00a4;
    L_0x008f:
        r0 = r7.mCardShowPingbackActionPolicy;
        r1 = r7.mBlocksView;
        r0.initTimestamp(r1);
        r0 = r7.mCardShowPingbackActionPolicy;
        r1 = r7.mBlocksView;
        r2 = r7.mEngine;
        r2 = r2.getPage();
        r4 = r3;
        r0.onSendCardShowPingback(r1, r2, r3, r4, r5);
    L_0x00a4:
        r7.mCurScreenMode = r8;
        r0 = r7.mIsAppDownloadComplete;
        if (r0 == 0) goto L_0x0044;
    L_0x00aa:
        r7.mIsAppDownloadComplete = r3;
        r6 = com.gala.video.lib.share.ifmanager.CreateInterfaceTools.createAppDownloadManager();
        if (r6 == 0) goto L_0x0044;
    L_0x00b2:
        r6.startInstall();
        goto L_0x0044;
    L_0x00b6:
        r0 = r7.mCurScreenMode;
        r1 = com.gala.sdk.player.ScreenMode.FULLSCREEN;
        if (r0 != r1) goto L_0x0113;
    L_0x00bc:
        r1 = r7.mBasicInfoContent;
        r0 = r7.isViewScrolled();
        if (r0 != 0) goto L_0x0117;
    L_0x00c4:
        r0 = r5;
    L_0x00c5:
        r1.updateVisibility(r0, r3, r3);
        r0 = r7.mBasicInfoContent;
        r1 = r7.isViewScrolled();
        if (r1 != 0) goto L_0x0119;
    L_0x00d0:
        r0.checkBasicCardVisible(r5);
        r0 = r7.mPingbackContext;
        r1 = "block";
        r2 = new com.gala.pingback.PingbackItem;
        r4 = "block";
        r5 = r7.mAlbumInfo;
        r5 = com.gala.video.app.player.pingback.detail.DetailPingBackUtils.getBlock(r5);
        r2.<init>(r4, r5);
        r0.setItem(r1, r2);
        r7.sendDetailPageShowPingback();
        r0 = r7.TAG;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "notifyScreenModeSwitched(): isEquityShow -> ";
        r1 = r1.append(r2);
        r2 = r7.mBasicInfoContent;
        r2 = r2.isEquityShow();
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils.logd(r0, r1);
        r0 = r7.mBasicInfoContent;
        r0 = r0.isEquityShow();
        if (r0 == 0) goto L_0x0113;
    L_0x0113:
        r7.showMainView();
        goto L_0x00a4;
    L_0x0117:
        r0 = r3;
        goto L_0x00c5;
    L_0x0119:
        r5 = r3;
        goto L_0x00d0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.app.player.albumdetail.ui.overlay.DetailOverlay.notifyScreenModeSwitched(com.gala.sdk.player.ScreenMode, boolean):void");
    }

    public void notifyVideoPlayFinished() {
        this.mBasicInfoContent.notifyPlayFinished();
        eraseTrailersPlayIcon();
    }

    public void showError(Object e) {
        LogRecordUtils.logd(this.TAG, ">> showError, e=" + e);
        this.mRootView.removeAllViews();
        GetInterfaceTools.getUICreator().maketNoResultView(ResourceUtil.getContext(), getErrorView().getNoResultPanel(), ErrorKind.SHOW_QR, (ApiException) e);
        getErrorView().showNoResultPanel();
    }

    private DetailStarsErrorView getErrorView() {
        LogRecordUtils.logd(this.TAG, ">> getErrorView");
        if (this.mErrorView == null) {
            View rootView = LayoutInflater.from(this.mRootView.getContext()).inflate(C1291R.layout.player_detail_error_view, null);
            this.mRootView.addView(rootView);
            this.mErrorView = new DetailStarsErrorView(rootView);
        }
        return this.mErrorView;
    }

    private FullDescriptionPanel getFullDescriptionPanel() {
        LogRecordUtils.logd(this.TAG, ">> getFullDescriptionPanel");
        if (this.mFullDescPanel == null) {
            this.mFullDescPanel = new FullDescriptionPanel(this.mRootView);
            this.mFullDescPanel.setOnFullDescClickedListener(this.mFullDescClickedListener);
        }
        return this.mFullDescPanel;
    }

    public boolean isViewScrolled() {
        View v = this.mBlocksView.getViewByPosition(0);
        if (v == null) {
            LogRecordUtils.logd(this.TAG, ">> MultiSubjectGridView.getViewByPosition(0) is null ");
            LogRecordUtils.logd(this.TAG, ">> mMultiSubjectGridView.getChildCount() =  " + this.mBlocksView.getChildCount());
            if (this.mBlocksView.getChildCount() == 0) {
                return false;
            }
            return true;
        }
        int topY = v.getTop() - this.mBlocksView.getScrollY();
        LogRecordUtils.logd(this.TAG, ">> isViewScrolled," + topY);
        if (topY >= 0) {
            return false;
        }
        return true;
    }

    private int findCardPosition(List<CardModel> list, CardModel card) {
        int position = -1;
        if (!ListUtils.isEmpty((List) list) && card != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (card.getTitle().equals(((CardModel) list.get(i)).getTitle())) {
                    position = i;
                    break;
                }
            }
        }
        LogRecordUtils.logd(this.TAG, "<< refreshCard, position=" + position);
        return position;
    }

    public List<AbsVoiceAction> getSupportedVoices(List<AbsVoiceAction> actions) {
        if (this.mCurScreenMode == ScreenMode.WINDOWED) {
            actions = getRecommendVoices(actions);
        }
        if (this.mBasicInfoContent != null) {
            return this.mBasicInfoContent.getPlayerSupportedVoices(actions);
        }
        return actions;
    }

    private List<AbsVoiceAction> getRecommendVoices(List<AbsVoiceAction> actions) {
        List items = getRecommendItemList();
        if (!ListUtils.isEmpty(items)) {
            IVoiceCommon provider = CreateInterfaceTools.createVoiceCommon();
            int size = items.size();
            for (int i = 0; i < size; i++) {
                ItemModel item = (ItemModel) items.get(i);
                if (!StringUtils.isEmpty(i + "")) {
                    actions.add(provider.createAbsVoiceAction("第" + (i + 1) + "个", new RecommendRunnable(item), KeyWordType.RESERVED));
                }
                if (!StringUtils.isEmpty(item.getTitle())) {
                    actions.add(provider.createAbsVoiceAction(item.getTitle(), new RecommendRunnable(item), KeyWordType.FUZZY));
                }
            }
        }
        return actions;
    }

    private List<ItemModel> getRecommendItemList() {
        return new ArrayList();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mFullDescPanel != null && this.mFullDescPanel.handleKeyEvent(event)) {
            return true;
        }
        if (this.mAllBlocksView != null && this.mAllBlocksView.handleKeyEvent(event)) {
            return true;
        }
        if (this.mBasicInfoContent == null || !this.mBasicInfoContent.handleKeyEvent(event)) {
            return false;
        }
        return true;
    }

    public void onActivityResumed(int resultCode) {
        LogRecordUtils.logd(this.TAG, ">> onActivityResumed, mCurScreenMode=" + this.mCurScreenMode);
        this.mBlocksView.removeCallbacks(this.mUikitEngineViewHideRunnable);
        if (this.mCardShowPingbackActionPolicy != null) {
            this.mCardShowPingbackActionPolicy.initTimestamp(this.mBlocksView);
        }
        if (this.mEngine != null) {
            this.mEngine.start();
        }
        if (this.mBlocksView != null) {
            if (this.mCurScreenMode == ScreenMode.WINDOWED) {
                this.mBlocksView.setVisibility(0);
            } else {
                this.mBlocksView.setVisibility(4);
            }
        }
        if (this.mBasicInfoContent != null) {
            this.mBasicInfoContent.onActivityResumed(resultCode);
            if (!(this.mBlocksView == null || isViewScrolled())) {
                this.mBasicInfoContent.setDefaultFocus();
            }
            this.mBasicInfoContent.notifyEpisodeListUpdate();
        }
        mActivityResumed = true;
    }

    public void onActivityFinishing() {
        LogRecordUtils.logd(this.TAG, ">> onActivityFinishing, mBasicInfoContent=" + this.mBasicInfoContent);
        if (this.mBasicInfoContent != null) {
            this.mBasicInfoContent.onActivityFinishing();
            this.mBlocksView = null;
            this.mFullDescPanel = null;
            this.mBasicInfoContent = null;
            this.mAlbumInfo = null;
        }
        KeyDispatcher.instance().unregister(this);
    }

    public void onActivityPaused() {
        LogRecordUtils.logd(this.TAG, ">> onActivityPaused");
        if (this.mBasicInfoContent != null) {
            this.mBasicInfoContent.onActivityPaused();
        }
        if (this.mEngine != null) {
            this.mBlocksView.postDelayed(this.mUikitEngineViewHideRunnable, this.UIKITVIEWHIDEDELAYTIME);
        }
        if (this.mAllViewEngine != null) {
            this.mAllViewEngine.setData(new ArrayList());
            this.mAllBlocksView.hide();
            this.mAllViewEngine.stop();
        }
        if (this.isPlayFromAllView) {
            if (this.mAllViewEngine != null) {
                this.mCardShowAllViewPingbackActionPolicy.onSendCardShowPingback(this.mAllBlocksView, this.mAllViewEngine.getPage(), false, true, false);
            }
        } else if (this.mEngine != null) {
            this.mCardShowPingbackActionPolicy.initTimestamp(this.mBlocksView);
            this.mCardShowPingbackActionPolicy.onSendCardShowPingback(this.mBlocksView, this.mEngine.getPage(), false, false, true);
        }
        mActivityResumed = false;
    }

    public void onActivityStarted() {
        LogRecordUtils.logd(this.TAG, ">> onActivityStarted");
        if (this.mBlocksView == null) {
        }
    }

    public void onActivityStopped() {
        LogRecordUtils.logd(this.TAG, ">> onActivityStopped");
        if (this.mBlocksView != null) {
            CardFocusHelper.forceVisible(this.mContext, false);
        } else {
            CardFocusHelper.forceVisible(this.mContext, false);
        }
    }

    public void onScrollBefore(ViewGroup parent, ViewHolder holder) {
        LogRecordUtils.logd(this.TAG, "onScrollBefore() mBlocksView  = " + this.mBlocksView);
        if (this.mBlocksView != null && this.mBlocksView.getFocusPosition() > 0) {
            this.mBasicInfoContent.updateVisibility(false, true, true);
            this.mBasicInfoContent.checkBasicCardVisible(false);
        }
    }

    public void onScrollStart(ViewGroup parent) {
        LogRecordUtils.logd(this.TAG, ">> onScrollStart");
    }

    public void onScrollStop(ViewGroup parent) {
        LogRecordUtils.logd(this.TAG, ">> onScrollStop");
        dealWithBasicCardVisible();
    }

    public void dealWithBasicCardVisible() {
        boolean scrolled = false;
        if (this.mBlocksView != null) {
            View viewByPosition = this.mBlocksView.getViewByPosition(0);
            if (viewByPosition instanceof BasicInfoContent) {
                boolean isVisible = isViewVisible();
                if (!isViewScrolled()) {
                    scrolled = true;
                }
                ((BasicInfoContent) viewByPosition).updateVisibility(isVisible, scrolled, true);
                this.mBasicInfoContent.checkBasicCardVisible(isVisible);
                this.mBasicInfoContent.notifyEpisodeListUpdate();
            }
            LogRecordUtils.logd(this.TAG, ">> onVerticalScroll position stop=" + this.mBlocksView.getFocusPosition());
        }
    }

    public boolean isViewVisible() {
        View v = this.mBlocksView.getViewByPosition(0);
        if (v == null) {
            return false;
        }
        int topY = v.getTop() - this.mBlocksView.getScrollY();
        int bottomY = v.getBottom() - this.mBlocksView.getScrollY();
        LogUtils.m1571e(this.TAG, "isViewVisible --- topY = " + topY + "v.getTop() = " + v.getTop() + " getScrollY() = " + this.mBlocksView.getScrollY());
        LogUtils.m1571e(this.TAG, "isViewVisible --- bottomY = " + topY + "v.getBottom() = " + v.getBottom() + " getScrollY() = " + this.mBlocksView.getScrollY());
        int height = this.mBlocksView.getBottom() - this.mBlocksView.getTop();
        if (topY < 0 || topY >= height || bottomY <= 0 || bottomY > height) {
            return false;
        }
        return true;
    }

    public void notifyScreenSaverStart() {
        boolean z;
        this.mBasicInfoContent.updateVisibility(!isViewScrolled(), false, false);
        BasicInfoContent basicInfoContent = this.mBasicInfoContent;
        if (isViewScrolled()) {
            z = false;
        } else {
            z = true;
        }
        basicInfoContent.checkBasicCardVisible(z);
        if (this.mCardShowPingbackActionPolicy != null) {
            this.mCardShowPingbackActionPolicy.initTimestamp(this.mBlocksView);
            this.mCardShowPingbackActionPolicy.onSendCardShowPingback(this.mBlocksView, this.mEngine.getPage(), false, false, true);
        }
    }

    public void notifyScreenSaverStop() {
        this.mPingbackContext.setItem("block", new PingbackItem("block", DetailPingBackUtils.getBlock(this.mAlbumInfo)));
        sendDetailPageShowPingback();
        this.mBasicInfoContent.updateVisibility(!isViewScrolled(), false, false);
        this.mBasicInfoContent.checkBasicCardVisible(isViewScrolled());
        if (!isViewScrolled()) {
        }
    }

    private void sendDetailPageShowPingback() {
        LogRecordUtils.logd(this.TAG, ">> sendDetailPageShowPingback");
        if (this.mAlbumInfo == null) {
            LogRecordUtils.logd(this.TAG, "sendDetailPageShowPingback sendDetailPageShown null == mCurVideo");
        } else {
            PingbackFactory.instance().createPingback(11).addItem(BTSPTYPE.BSTP_1).addItem(C1.C1_TYPE(String.valueOf(this.mAlbumInfo.getChannelId()))).addItem(QTCURLTYPE.DETAIL).addItem(QPLD.QPLD_TYPE(this.mAlbumInfo.getAlbumId())).addItem(this.mPingbackContext.getItem("rfr")).addItem(this.mPingbackContext.getItem("e")).addItem(this.mPingbackContext.getItem("block")).addItem(NOW_C1.NOW_C1_TYPE(String.valueOf(this.mAlbumInfo.getChannelId()))).post();
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
            LogUtils.m1568d(this.TAG, "<<getUid" + uid + ",context=" + this.mContext);
        }
        return uid;
    }

    public void clearAlbumListDefaultSelectedTextColor() {
        this.mBasicInfoContent.clearAlbumListDefaultSelectedTextColor();
    }

    public void updateAlbumDetailTotally(IVideo data) {
        this.mBasicInfoContent.resetBasicInfo(data, this.mBlocksView);
        if (this.mCardShowPingbackActionPolicy != null && this.mIsWindowPlay && this.mBasicInfoContent != null && this.mBasicInfoContent.getPlayerScreenMode() == ScreenMode.WINDOWED) {
            this.mCardShowPingbackActionPolicy.initTimestamp(this.mBlocksView);
            this.mCardShowPingbackActionPolicy.onSendCardShowPingback(this.mBlocksView, this.mEngine.getPage(), false, false, true);
        }
        LogRecordUtils.logd(this.TAG, ">> updateAlbumDetailTotally end" + ListUtils.getCount(this.mEngine.getPage().getModel()));
    }

    private void eraseTrailersPlayIcon() {
        LogRecordUtils.logd(this.TAG, ">> eraseTrailersPlayIcon");
        if (this.mIsWindowPlay) {
            List cards = this.mEngine.getPage().getCards();
            if (ListUtils.isEmpty(cards)) {
                LogRecordUtils.logd(this.TAG, ">> eraseTrailersPlayIcon why cardListNull");
                return;
            }
            List<Item> items = null;
            int i = 0;
            while (i < cards.size()) {
                if (((Card) cards.get(i)).getModel().mSource != null && (((Card) cards.get(i)).getModel().mSource.equals(Source.TRAILERS) || ((Card) cards.get(i)).getModel().mSource.equals(Source.ABOUT_TOPIC))) {
                    items = ((Card) cards.get(i)).getItems();
                    break;
                }
                i++;
            }
            if (items == null) {
                LogRecordUtils.logd(this.TAG, ">> items == null");
                return;
            } else if (items.size() > 0) {
                for (i = 0; i < items.size(); i++) {
                    if (items.get(i) != null) {
                        ((StandardItem) items.get(i)).setPlayingGif(false);
                    }
                }
                return;
            } else {
                LogRecordUtils.logd(this.TAG, ">> eraseTrailersPlayIcon why itemList null");
                return;
            }
        }
        LogRecordUtils.logd(this.TAG, " mIsWindowPlay is  false");
    }

    public void updateAlbumDetailTrailers(IVideo data) {
        boolean z = true;
        this.mBasicInfoContent.notifyPlayFinished();
        LogRecordUtils.logd(this.TAG, ">> updateAlbumDetailTrailers  id  ->" + data.getTvId());
        List cards = this.mEngine.getPage().getCards();
        if (ListUtils.isEmpty(cards)) {
            LogRecordUtils.logd(this.TAG, ">> updateAlbumDetailTrailers why cardList null");
            return;
        }
        List<Item> items = null;
        int i = 0;
        while (i < cards.size()) {
            if (((Card) cards.get(i)).getModel().mSource != null && (((Card) cards.get(i)).getModel().mSource.equals(Source.TRAILERS) || ((Card) cards.get(i)).getModel().mSource.equals(Source.ABOUT_TOPIC))) {
                items = ((Card) cards.get(i)).getItems();
                break;
            }
            i++;
        }
        if (items == null || items.size() <= 0) {
            LogRecordUtils.logd(this.TAG, ">> updateAlbumDetailTrailers why items.size() < 0");
            return;
        }
        boolean allViewShowGif = true;
        boolean hasViewShowGif = false;
        for (i = 0; i < items.size(); i++) {
            if (items.get(i) != null) {
                boolean isPlaying;
                String tvID = ((Item) items.get(i)).getModel().getCuteViewData(UIKitConfig.KEY_DETAIL_SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_TVID);
                LogRecordUtils.logd(this.TAG, ">> updateAlbumDetailTrailers  tvID  ->" + tvID);
                if (StringUtils.equals(tvID, data.getTvId()) && this.mIsWindowPlay) {
                    isPlaying = true;
                } else {
                    isPlaying = false;
                }
                ((StandardItem) items.get(i)).setPlayingGif(isPlaying);
                if (isPlaying) {
                    allViewShowGif = false;
                }
                if (((Item) items.get(i)).getModel().getActionModel().getItemType() == ItemDataType.ENTER_ALL) {
                    hasViewShowGif = true;
                }
            }
        }
        LogRecordUtils.logd(this.TAG, ">> updateAlbumDetailTrailers all view , hasViewShowGif = " + hasViewShowGif + " ,allViewShowGif = " + allViewShowGif);
        if (hasViewShowGif) {
            StandardItem standardItem = (StandardItem) items.get(items.size() - 1);
            if (!(this.mIsWindowPlay && allViewShowGif)) {
                z = false;
            }
            standardItem.setPlayingGif(z);
        }
        if (this.mIsWindowPlay) {
            HashMap<String, String> map1 = new HashMap();
            map1.put(UIKitConfig.KEY_SPECIAL_DATA_ALLVIEW_PLAYING_TVID, data.getTvId());
            ((StandardItem) items.get(items.size() - 1)).getModel().getCuteViewDatas().put(UIKitConfig.KEY_DETAIL_SPECIAL_DATA_ALLVIEW, map1);
        }
    }

    public ScreenMode getCurrentScreenMode() {
        return this.mCurScreenMode;
    }

    public void setAppDownloadComplete(boolean isComplete) {
        this.mIsAppDownloadComplete = isComplete;
    }

    public UIKitEngine getEngine() {
        return this.mEngine;
    }

    public UIKitEngine getAllViewEngine() {
        return this.mAllViewEngine;
    }
}
