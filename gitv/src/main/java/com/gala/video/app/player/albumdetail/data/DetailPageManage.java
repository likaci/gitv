package com.gala.video.app.player.albumdetail.data;

import android.content.Context;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Star;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.albumdetail.data.AlbumInfo.VideoKind;
import com.gala.video.app.player.albumdetail.data.task.FetchPeripheralTask;
import com.gala.video.app.player.albumdetail.data.task.FetchPeripheralTask.IFetchPeripheralListener;
import com.gala.video.app.player.albumdetail.data.task.FetchRecommendTask;
import com.gala.video.app.player.albumdetail.data.task.FetchRecommendTask.IFetchRecommendListener;
import com.gala.video.app.player.albumdetail.data.task.FetchStarTask;
import com.gala.video.app.player.albumdetail.data.task.FetchStarTask.IFetchStarTaskListener;
import com.gala.video.app.player.albumdetail.data.task.FetchSuperAlbumTask;
import com.gala.video.app.player.albumdetail.data.task.FetchSuperAlbumTask.IFetchSuperAlbumTaskListener;
import com.gala.video.app.player.albumdetail.data.task.FetchTrailerTask;
import com.gala.video.app.player.albumdetail.data.task.FetchTrailerTask.IFetchTrailerTaskListener;
import com.gala.video.app.player.albumdetail.ui.IDetailOverlay;
import com.gala.video.app.player.albumdetail.ui.card.BasicInfoCard;
import com.gala.video.app.player.albumdetail.ui.card.BasicInfoContent;
import com.gala.video.app.player.albumdetail.ui.card.BasicInfoItem;
import com.gala.video.app.player.data.DetailConstants;
import com.gala.video.app.player.data.DetailDataCacheManager;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import com.gala.video.lib.share.uikit.UIKitEngine;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.CardInfoBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.Item.CornerBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig.Source;
import com.gala.video.lib.share.uikit.loader.IUikitDataFetcherCallback;
import com.gala.video.lib.share.uikit.loader.IUikitDataLoader;
import com.gala.video.lib.share.uikit.loader.UikitDataFetcher;
import com.gala.video.lib.share.uikit.loader.UikitDataLoader;
import com.gala.video.lib.share.uikit.loader.UikitEvent;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xbill.DNS.WKSRecord.Service;

public class DetailPageManage {
    private static final int MSG_APPEND_DATA = 2;
    private static final int MSG_CHANGE_DATA = 3;
    private static final int MSG_INSERT_DATA = 6;
    private static final int MSG_REMOVE_DATA = 5;
    private static final int MSG_SET_ALLVIEW_DATA = 4;
    private static final int MSG_SET_DATA = 1;
    private static final int MSG_UPDATE_DATA = 7;
    private static String TAG = "DetailPageManage";
    private final long BLOCK_TIME = 30000;
    private Map<String, List<Album>> allEnterMap = new HashMap();
    private Map<String, List<Star>> allStarMap = new HashMap();
    IUikitDataLoader loader;
    public ActionPolicy mActionPolicy;
    private AlbumInfo mAlbumInfo;
    public ActionPolicy mAllViewActionPolicy;
    private UIKitEngine mAllViewEngine;
    private List<CardInfoModel> mBannerAds = null;
    private IDetailOverlay mDetailOverlay;
    private UIKitEngine mEngine;
    private boolean mIsSwitchSourceRefresh = false;
    JobController mJobController;
    private long mPageStartShowTime;
    private int mTrailerCardPosition = -1;
    public boolean misVip = false;
    public boolean mstarted = false;
    private LocalHandler myHandler;

    static class BaseListener {
        WeakReference<DetailPageManage> mOuter;

        public BaseListener(DetailPageManage outer) {
            this.mOuter = new WeakReference(outer);
        }
    }

    static class FetchPeripheralListener extends BaseListener implements IFetchPeripheralListener {
        private CardInfoModel mCard;
        private ConditionVariable mPeripheralLock;

        public FetchPeripheralListener(DetailPageManage outer, CardInfoModel card, ConditionVariable peripheralLock) {
            super(outer);
            this.mCard = card;
            this.mPeripheralLock = peripheralLock;
        }

        public void onSuccess(List<Album> albums) {
            DetailPageManage detailPageManage = (DetailPageManage) this.mOuter.get();
            if (detailPageManage != null) {
                LogRecordUtils.logd(DetailPageManage.TAG, ">> FetchPeripheralTask Listener onSuccess , albums.size() = " + albums.size());
                if (albums.size() > 0) {
                    if (PlayerDebugUtils.testAlbumDetailGroupDetailSource()) {
                        LogRecordUtils.logd(DetailPageManage.TAG, " testAlbumDetailGroupDetailSource ");
                    } else {
                        detailPageManage.allEnterMap.put(this.mCard.mSource, albums);
                        CardInfoBuildTool.buildRecommendVideoCard(this.mCard, albums, false);
                    }
                }
                this.mCard.setTitle(DetailConstants.CONTENT_TITLE_PERIPHERAL);
                this.mPeripheralLock.open();
                LogRecordUtils.logd(DetailPageManage.TAG, "FetchPeripheralTask open" + this.mPeripheralLock);
            }
        }

        public void onFailed(ApiException e) {
            if (((DetailPageManage) this.mOuter.get()) != null) {
                LogRecordUtils.loge(DetailPageManage.TAG, ">> FetchPeripheralTask TaskListener onFailed, e=" + e);
                this.mCard.setTitle(DetailConstants.CONTENT_TITLE_PERIPHERAL);
                this.mPeripheralLock.open();
            }
        }
    }

    static class FetchRecommendListener extends BaseListener implements IFetchRecommendListener {
        private CardInfoModel mCard;
        private ConditionVariable mRecommendLock;

        public FetchRecommendListener(DetailPageManage outer, CardInfoModel card, ConditionVariable recommendLock) {
            super(outer);
            this.mCard = card;
            this.mRecommendLock = recommendLock;
        }

        public void onSuccess(List<Album> albums) {
            DetailPageManage detailPageManage = (DetailPageManage) this.mOuter.get();
            if (detailPageManage != null) {
                LogRecordUtils.logd(DetailPageManage.TAG, ">> FetchRecommendTask Listener onSuccess , albums.size() = " + albums.size());
                if (albums.size() > 0) {
                    detailPageManage.allEnterMap.put(this.mCard.mSource, albums);
                    CardInfoBuildTool.buildAlbumCard(this.mCard, albums, false);
                }
                this.mCard.setTitle(DetailConstants.CONTENT_TITLE_RECOMMEND);
                this.mRecommendLock.open();
                LogRecordUtils.logd(DetailPageManage.TAG, "FetchRecommendTask open" + this.mRecommendLock);
            }
        }

        public void onFailed(ApiException e) {
            if (((DetailPageManage) this.mOuter.get()) != null) {
                LogRecordUtils.loge(DetailPageManage.TAG, ">> FetchRecommendTask TaskListener onFailed, e=" + e);
                this.mCard.setTitle(DetailConstants.CONTENT_TITLE_RECOMMEND);
                this.mRecommendLock.open();
            }
        }
    }

    static class FetchStarTaskListener extends BaseListener implements IFetchStarTaskListener {
        private CardInfoModel mCard;
        private ConditionVariable mStarLock;

        public FetchStarTaskListener(DetailPageManage outer, CardInfoModel card, ConditionVariable starLock) {
            super(outer);
            this.mCard = card;
            this.mStarLock = starLock;
        }

        public void onSuccess(List<Star> stars) {
            DetailPageManage detailPageManage = (DetailPageManage) this.mOuter.get();
            if (detailPageManage != null) {
                LogRecordUtils.logd(DetailPageManage.TAG, ">> fetchStar Listener onSuccess , episodeList.size() = " + stars.size());
                if (stars.size() > 0) {
                    detailPageManage.allStarMap.put(this.mCard.mSource, stars);
                    CardInfoBuildTool.buildStarsCard(this.mCard, stars, false);
                }
                this.mCard.setTitle(DetailConstants.CONTENT_TITLE_STARLIST);
                this.mStarLock.open();
                LogRecordUtils.logd(DetailPageManage.TAG, "fetchStar Listener open" + this.mStarLock);
            }
        }

        public void onFailed(ApiException e) {
            if (((DetailPageManage) this.mOuter.get()) != null) {
                LogRecordUtils.loge(DetailPageManage.TAG, ">>fetchStar onFailed, e=" + e);
                this.mCard.setTitle(DetailConstants.CONTENT_TITLE_STARLIST);
                this.mStarLock.open();
            }
        }
    }

    static class FetchSuperAlbumTaskListener extends BaseListener implements IFetchSuperAlbumTaskListener {
        private AlbumInfo mAlbumInfo;
        private CardInfoModel mCard;
        private ConditionVariable mLock;

        public FetchSuperAlbumTaskListener(DetailPageManage outer, CardInfoModel card, ConditionVariable lock, AlbumInfo albuminfo) {
            super(outer);
            this.mCard = card;
            this.mLock = lock;
            this.mAlbumInfo = albuminfo;
        }

        public void onSuccess(List<Album> albums) {
            DetailPageManage detailPageManage = (DetailPageManage) this.mOuter.get();
            if (detailPageManage != null) {
                LogRecordUtils.logd(DetailPageManage.TAG, ">> fetchSuperAlbum Listener onSuccess , episodeList.size() = " + albums.size());
                if (albums.size() > 0) {
                    detailPageManage.allEnterMap.put(this.mCard.mSource, albums);
                    CardInfoBuildTool.buildAlbumCard(this.mCard, albums, false);
                }
                boolean hasAllView = false;
                ItemInfoModel allViewItemInfoModel = null;
                boolean allViewShowGif = true;
                for (int i = 0; i < ListUtils.getArraySize(this.mCard.getItemInfoModels()); i++) {
                    ItemInfoModel[] itemInfoModels = this.mCard.getItemInfoModels()[i];
                    for (int j = 0; j < ListUtils.getArraySize(itemInfoModels); j++) {
                        ItemInfoModel itemInfoModel = itemInfoModels[j];
                        if (itemInfoModel != null) {
                            Log.v(DetailPageManage.TAG, "FetchSuperAlbumTaskListener itemInfoModel.getCuteViewData(UIKitConfig.KEY_DETAIL_SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_QPID) = " + itemInfoModel.getCuteViewData(UIKitConfig.KEY_DETAIL_SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_QPID));
                            Log.v(DetailPageManage.TAG, "FetchSuperAlbumTaskListener mAlbumInfo.getAlbum().qpId = " + this.mAlbumInfo.getAlbum().qpId);
                            boolean isplaying = (this.mAlbumInfo.getKind() == VideoKind.ALBUM_EPISODE || this.mAlbumInfo.getKind() == VideoKind.VIDEO_EPISODE || this.mAlbumInfo.getKind() == VideoKind.VIDEO_SINGLE) && itemInfoModel.getCuteViewData(UIKitConfig.KEY_DETAIL_SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_QPID).equals(this.mAlbumInfo.getAlbum().qpId);
                            CornerBuildTool.buildGifPlayingCorner(itemInfoModel, isplaying);
                            if (isplaying) {
                                allViewShowGif = false;
                            }
                            if (itemInfoModel.getActionModel().getItemType() == ItemDataType.ENTER_ALL) {
                                hasAllView = true;
                                allViewItemInfoModel = itemInfoModel;
                                HashMap<String, String> map1 = new HashMap();
                                map1.put(UIKitConfig.KEY_SPECIAL_DATA_ALLVIEW_PLAYING_TVID, this.mAlbumInfo.getTvId());
                                itemInfoModel.getCuteViewDatas().put(UIKitConfig.KEY_DETAIL_SPECIAL_DATA_ALLVIEW, map1);
                            }
                        }
                    }
                    LogRecordUtils.logd(DetailPageManage.TAG, ">> updateSuperAlbumTrailers all view , hasAllView = " + hasAllView + " ,allViewShowGif = " + allViewShowGif + "allViewItemInfoModel = " + allViewItemInfoModel);
                    if (hasAllView && allViewItemInfoModel != null) {
                        CornerBuildTool.buildGifPlayingCorner(allViewItemInfoModel, allViewShowGif);
                    }
                }
                this.mCard.setTitle(detailPageManage.mAlbumInfo != null ? detailPageManage.mAlbumInfo.getSuperName() : "");
                this.mLock.open();
                LogRecordUtils.logd(DetailPageManage.TAG, "fetchSuperAlbum open" + this.mLock);
            }
        }

        public void onFailed(ApiException e) {
            if (((DetailPageManage) this.mOuter.get()) != null) {
                LogRecordUtils.loge(DetailPageManage.TAG, ">> fetchSuperAlbum TaskListener onFailed, e=" + e);
                this.mLock.open();
            }
        }
    }

    static class FetchTrailerTaskListener extends BaseListener implements IFetchTrailerTaskListener {
        private CardInfoModel mCard;
        private ConditionVariable mLock;

        public FetchTrailerTaskListener(DetailPageManage outer, CardInfoModel card, ConditionVariable lock) {
            super(outer);
            this.mCard = card;
            this.mLock = lock;
        }

        public void onSuccess(List<Album> episodeList) {
            DetailPageManage detailPageManage = (DetailPageManage) this.mOuter.get();
            if (detailPageManage != null) {
                LogRecordUtils.logd(DetailPageManage.TAG, ">> fetchTrailer Listener onSuccess , episodeList.size() = " + episodeList.size());
                if (episodeList.size() > 0) {
                    if (PlayerDebugUtils.testAlbumDetailGroupDetailSource()) {
                        LogRecordUtils.logd(DetailPageManage.TAG, " testAlbumDetailGroupDetailSource ");
                    } else {
                        detailPageManage.allEnterMap.put(this.mCard.mSource, episodeList);
                        CardInfoBuildTool.buildAlbumCard(this.mCard, episodeList, false);
                    }
                }
                this.mCard.setTitle(DetailConstants.CONTENT_TITLE_TRAILERS);
                this.mLock.open();
                LogRecordUtils.logd(DetailPageManage.TAG, "fetchTrailer Listener open" + this.mLock);
            }
        }

        public void onFailed(ApiException e) {
            if (((DetailPageManage) this.mOuter.get()) != null) {
                LogRecordUtils.loge(DetailPageManage.TAG, ">>fetchTrailer onFailed, e=" + e);
                this.mCard.setTitle(DetailConstants.CONTENT_TITLE_TRAILERS);
                this.mLock.open();
            }
        }
    }

    public class LocalHandler extends Handler {
        public LocalHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            Log.v(DetailPageManage.TAG, "handleMessage , msg.arg1 = " + msg.arg1);
            Log.v(DetailPageManage.TAG, "mAllViewEngine id = @" + hashCode() + ">>>" + DetailPageManage.this.mAllViewEngine.getId());
            Log.v(DetailPageManage.TAG, "mEngine id = @" + hashCode() + ">>>" + DetailPageManage.this.mEngine.getId());
            switch (msg.arg1) {
                case 1:
                    Log.v(DetailPageManage.TAG, "MSG_SET_DATA");
                    DetailPageManage.this.mEngine.setData((List) msg.obj);
                    return;
                case 2:
                    Log.v(DetailPageManage.TAG, "MSG_APPEND_DATA");
                    DetailPageManage.this.mEngine.appendData((List) msg.obj);
                    return;
                case 3:
                    Log.v(DetailPageManage.TAG, "MSG_CHANGE_DATA");
                    DetailPageManage.this.mEngine.changeCardModel((CardInfoModel) msg.obj);
                    return;
                case 4:
                    Log.v(DetailPageManage.TAG, "MSG_SET_ALLVIEW_DATA");
                    DetailPageManage.this.mAllViewEngine.setData((List) msg.obj);
                    return;
                case 5:
                    Log.v(DetailPageManage.TAG, "MSG_REMOVE_DATA");
                    DetailPageManage.this.mEngine.removeCardModel(msg.arg2, ((Integer) msg.obj).intValue(), false);
                    return;
                case 6:
                    Log.v(DetailPageManage.TAG, "MSG_INSERT_DATA");
                    DetailPageManage.this.mEngine.insertCardModel(msg.arg2, (CardInfoModel) msg.obj);
                    return;
                case 7:
                    Log.v(DetailPageManage.TAG, "MSG_UPDATE_DATA");
                    DetailPageManage.this.mEngine.updateCaredModel((CardInfoModel) msg.obj);
                    return;
                default:
                    return;
            }
        }
    }

    static class UikitDataFetcherCallback extends BaseListener implements IUikitDataFetcherCallback {
        public UikitDataFetcherCallback(DetailPageManage outer) {
            super(outer);
        }

        public void onSuccess(List<CardInfoModel> list, String raw) {
            DetailPageManage detailPageManage = (DetailPageManage) this.mOuter.get();
            if (detailPageManage != null) {
                Log.v(DetailPageManage.TAG, " fetchBannerAdonSuccess ");
                if (list != null && list.size() > 0) {
                    Log.v(DetailPageManage.TAG, "fetchBannerAd,list.size() = " + list.size());
                    detailPageManage.mBannerAds.addAll(list);
                }
            }
        }

        public void onFailed() {
            if (((DetailPageManage) this.mOuter.get()) != null) {
                Log.v(DetailPageManage.TAG, "fetchBannerAd, onFailed ");
            }
        }
    }

    public Map<String, List<Album>> getAllEnterMap() {
        return this.allEnterMap;
    }

    public DetailPageManage(Context context, IDetailOverlay detailOverlay) {
        TAG = "DetailPageManage@" + Integer.toHexString(hashCode());
        this.mDetailOverlay = detailOverlay;
        this.myHandler = new LocalHandler(Looper.myLooper());
    }

    public void initialize() {
        Log.v(TAG, "initialize");
        EventBus.getDefault().register(this);
        this.mEngine = this.mDetailOverlay.getEngine();
        this.mEngine.getUIKitBuilder().registerSpecialItem(1000, BasicInfoItem.class, BasicInfoContent.class);
        this.mEngine.getUIKitBuilder().registerSpecialCard(Service.NNTP, BasicInfoCard.class);
        this.mAllViewEngine = this.mDetailOverlay.getAllViewEngine();
    }

    public void pageStop() {
        this.mEngine.stop();
        this.mAllViewEngine.stop();
    }

    public void pageDestroy() {
        this.myHandler.removeCallbacksAndMessages(null);
        this.mEngine.destroy();
        this.mAllViewEngine.destroy();
        destroy();
    }

    public void loadData(String resId, boolean isSwitchSourceRefresh, AlbumInfo albumInfo, JobController controller) {
        this.mJobController = controller;
        LogUtils.d(TAG, "resId:" + resId + " ,isSwitchSourceRefresh = " + isSwitchSourceRefresh + ", + albumInfo = " + albumInfo);
        this.mAlbumInfo = albumInfo;
        this.mIsSwitchSourceRefresh = isSwitchSourceRefresh;
        LogRecordUtils.logd(TAG, "execute,  mResId = " + resId);
        if (this.loader == null) {
            this.loader = new UikitDataLoader(2, resId, this.mEngine.getId());
            this.loader.register();
            this.loader.setVipLoader(this.misVip);
        }
        this.loader.setSourceID(resId);
        loadDetailGroupInfo();
    }

    public void updateDetailDate(String resId) {
        if (DetailDataCacheManager.instance().isGroupDetailOverTime(resId)) {
            LogRecordUtils.logd(TAG, "update cache");
            LogUtils.d(TAG, "updateDetailDate-" + System.currentTimeMillis());
            UikitEvent event = new UikitEvent();
            event.uikitEngineId = this.mEngine.getId();
            event.eventType = 80;
            event.pageNo = 1;
            onPostEvent(event);
        }
    }

    private void loadDetailGroupInfo() {
        LogUtils.d(TAG, "loadDetailGroupInfo-" + System.currentTimeMillis());
        UikitEvent event = new UikitEvent();
        event.uikitEngineId = this.mEngine.getId();
        event.eventType = 64;
        event.layoutChange = 1;
        event.pageNo = 1;
        onPostEvent(event);
    }

    public void clearOtherCards() {
        int listSize = this.mEngine.getPage().getModel().size();
        Log.v(TAG, "clearOtherCards, listSize = " + listSize);
        if (listSize > 1) {
            removeCardData(1, listSize - 1);
        } else {
            Log.v(TAG, "clearOtherCards, only top detail card");
        }
    }

    private List<CardInfoModel> getCardInfoModelList(CardInfoModel cardInfoModel) {
        List<CardInfoModel> cards = new ArrayList();
        cards.add(cardInfoModel);
        return cards;
    }

    public void appendPageData(List<CardInfoModel> cards) {
        Message msg = new Message();
        msg.arg1 = 2;
        msg.obj = cards;
        this.myHandler.sendMessage(msg);
    }

    public void appendPageData(CardInfoModel card) {
        Message msg = new Message();
        msg.arg1 = 2;
        msg.obj = getCardInfoModelList(card);
        this.myHandler.sendMessage(msg);
    }

    public void changeCardData(CardInfoModel cards) {
        Message msg = new Message();
        msg.arg1 = 3;
        msg.obj = cards;
        this.myHandler.sendMessage(msg);
    }

    public void bindDataSource(List<CardInfoModel> cards) {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.obj = cards;
        this.myHandler.sendMessage(msg);
    }

    public void bindAllViewDataSource(CardInfoModel card) {
        List<CardInfoModel> cards = new ArrayList();
        cards.add(card);
        Message msg = new Message();
        msg.arg1 = 4;
        msg.obj = cards;
        this.myHandler.sendMessage(msg);
    }

    public void setActionPolicy(ActionPolicy actionPolicy) {
        this.mActionPolicy = actionPolicy;
        if (this.mEngine != null) {
            this.mEngine.getPage().registerActionPolicy(this.mActionPolicy);
        }
    }

    public void setAllViewActionPolicy(ActionPolicy actionPolicy) {
        this.mAllViewActionPolicy = actionPolicy;
        if (this.mEngine != null) {
            this.mAllViewEngine.getPage().registerActionPolicy(this.mAllViewActionPolicy);
        }
    }

    public void onPostEvent(UikitEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onUikitEvent(UikitEvent uikitEvent) {
        if (uikitEvent.uikitEngineId == this.mEngine.getId()) {
            LogUtils.d(TAG, "uikitEvent.eventType = " + uikitEvent.eventType);
            int i;
            CardInfoModel model;
            List<CardInfoModel> notifyCard;
            Album album;
            List cardInfoModels;
            switch (uikitEvent.eventType) {
                case 64:
                    LogUtils.d(TAG, "onUikitEvent LOADER_GROUP_DEAIL ");
                    if (uikitEvent.layoutChange == 0) {
                        LogUtils.i(TAG, "initPageAction NODATA!!!  hideLoading");
                        this.mEngine.getPage().hideLoading();
                        return;
                    }
                    return;
                case 69:
                    LogUtils.d(TAG, "mIsSwitchSourceRefresh = " + this.mIsSwitchSourceRefresh);
                    if (uikitEvent.cardList == null) {
                        LogUtils.e(TAG, "uikitEvent.cardList null");
                        return;
                    }
                    LogUtils.d(TAG, "onUikitEvent LOADER_DETAIL_CARDS- uikitEvent.cardList = " + uikitEvent.cardList);
                    for (i = 0; i < ListUtils.getCount(uikitEvent.cardList); i++) {
                        LogRecordUtils.logd(TAG, "listCard -> " + i + ", data -> " + ((CardInfoModel) uikitEvent.cardList.get(i)).toString());
                    }
                    if (this.mIsSwitchSourceRefresh) {
                        List<CardInfoModel> cardInfoModelList = new ArrayList();
                        try {
                            cardInfoModelList = deepCopy(uikitEvent.cardList);
                        } catch (Exception e) {
                            Log.d(TAG, "deepCopy exception-");
                            cardInfoModelList.addAll(uikitEvent.cardList);
                            e.printStackTrace();
                        }
                        for (CardInfoModel model2 : cardInfoModelList) {
                            if (model2.mSource.equals("recommend")) {
                                Log.d(TAG, "onUikitEvent LOADER_SET_CARDS-recommend-" + model2.getId());
                                fetchRecommend(model2);
                            } else if (model2.mSource.equals("star")) {
                                Log.d(TAG, "onUikitEvent LOADER_SET_CARDS-star-" + model2.getId());
                                fetchStar(model2);
                            } else if (model2.mSource.equals(Source.TRAILERS)) {
                                Log.d(TAG, "onUikitEvent LOADER_SET_CARDS-trailers-" + model2.getId());
                                fetchTrailers(model2);
                            }
                            if (StringUtils.equals(model2.getTitle(), DetailConstants.CONTENT_TITLE_TRAILERS)) {
                                int originTrailers = getTrailerItem().length;
                                int newTrailers = 0;
                                if (model2.getItemInfoModels() != null) {
                                    newTrailers = model2.getItemInfoModels().length;
                                }
                                if (originTrailers != 0 && newTrailers != 0) {
                                    LogRecordUtils.logd(TAG, "originTrailers != 0 && newTrailers!= 0. change");
                                    changeCardData(model2);
                                } else if (originTrailers != 0 && newTrailers == 0) {
                                    LogRecordUtils.logd(TAG, "originTrailers != 0 && newTrailers== 0. remove");
                                    changeCardData(model2);
                                } else if (originTrailers == 0 && newTrailers != 0) {
                                    LogRecordUtils.logd(TAG, "originTrailers == 0 && newTrailers != 0. update");
                                    updateCardDataForSourceSwitch(model2);
                                } else if (originTrailers == 0 && newTrailers == 0) {
                                    LogRecordUtils.logd(TAG, "originTrailers == 0 && newTrailers == 0. do nothing");
                                }
                            } else {
                                changeCardData(model2);
                            }
                        }
                        return;
                    }
                    this.allEnterMap.clear();
                    this.allStarMap.clear();
                    notifyCard = new CopyOnWriteArrayList();
                    fetchBannerAd();
                    LogUtils.d(TAG, ">>>> fetchBannerAd ");
                    clearOtherCards();
                    for (CardInfoModel model22 : uikitEvent.cardList) {
                        if (this.mJobController == null || !this.mJobController.isCancelled()) {
                            if (model22 == null) {
                                Log.v(TAG, "model == null");
                            } else if (model22.mSource.equals("recommend")) {
                                Log.d(TAG, "onUikitEvent LOADER_SET_CARDS-recommend-" + model22.getId());
                                fetchRecommend(model22);
                            } else if (model22.mSource.equals("star")) {
                                Log.d(TAG, "onUikitEvent LOADER_SET_CARDS-star-" + model22.getId());
                                fetchStar(model22);
                            } else if (model22.mSource.equals(Source.TRAILERS)) {
                                Log.d(TAG, "onUikitEvent LOADER_SET_CARDS-trailers-" + model22.getId());
                                fetchTrailers(model22);
                                this.mTrailerCardPosition = this.mEngine.getPage().getModel().size();
                            } else if (model22.mSource.equals(Source.SUPER_ALBUM)) {
                                Log.d(TAG, "onUikitEvent LOADER_SET_CARDS-superAlbum-" + model22.getId());
                                fetchSuperAlbum(model22);
                            } else if (model22.mSource.equals(Source.ABOUT_TOPIC)) {
                                if (this.mAlbumInfo != null) {
                                    album = this.mAlbumInfo.getAlbum();
                                    if (album != null && album.isSeries() && !album.isSourceType() && album.chnId == 2) {
                                        Log.d(TAG, "onUikitEvent LOADER_SET_CARDS-Peripheral-" + model22.getId());
                                        fetchPeripheral(model22);
                                    }
                                }
                            } else if (model22.mSource.equals("banner")) {
                                Log.d(TAG, "onUikitEvent LOADER_SET_CARDS-BANNER_AD-" + model22.getId());
                                model22 = updateBannerAdCard(model22);
                                if (model22 != null) {
                                    notifyCard.add(model22);
                                }
                            } else {
                                notifyCard.add(model22);
                            }
                            notifyCard.add(model22);
                            cardInfoModels = new ArrayList();
                            cardInfoModels.addAll(notifyCard);
                            appendPageData(cardInfoModels);
                            notifyCard.clear();
                        } else {
                            LogRecordUtils.logd(TAG, "Job has been cancelled, stop data fetching process.");
                            return;
                        }
                    }
                    cardInfoModels = new ArrayList();
                    cardInfoModels.addAll(notifyCard);
                    appendPageData(cardInfoModels);
                    notifyCard.clear();
                    getUIkitEngine().getPage().showLoading();
                    return;
                case 71:
                    LogUtils.d(TAG, "onUikitEvent LOADER_MORE_DETAIL_CARDS-" + uikitEvent.sourceId + "-pageNo-" + uikitEvent.pageNo);
                    if (uikitEvent.cardList == null) {
                        LogUtils.e(TAG, "uikitEvent.cardList null");
                        this.mEngine.getPage().hideLoading();
                        return;
                    }
                    LogUtils.d(TAG, "onUikitEvent LOADER_MORE_DETAIL_CARDS- uikitEvent.cardList = " + uikitEvent.cardList);
                    for (i = 0; i < ListUtils.getCount(uikitEvent.cardList); i++) {
                        LogRecordUtils.logd(TAG, "listCard -> " + i + ", data -> " + ((CardInfoModel) uikitEvent.cardList.get(i)).toString());
                    }
                    notifyCard = new CopyOnWriteArrayList();
                    LogUtils.d(TAG, ">>>> fetchBannerAd ");
                    for (CardInfoModel model222 : uikitEvent.cardList) {
                        if (model222 == null) {
                            Log.v(TAG, "model == null");
                        } else if (model222.mSource.equals("recommend")) {
                            Log.d(TAG, "onUikitEvent LOADER_MORE_DETAIL_CARDS-recommend-" + model222.getId());
                            fetchRecommend(model222);
                        } else if (model222.mSource.equals("star")) {
                            Log.d(TAG, "onUikitEvent LOADER_MORE_DETAIL_CARDS-star-" + model222.getId());
                            fetchStar(model222);
                        } else if (model222.mSource.equals(Source.TRAILERS)) {
                            Log.d(TAG, "onUikitEvent LOADER_MORE_DETAIL_CARDS-trailers-" + model222.getId());
                            fetchTrailers(model222);
                            this.mTrailerCardPosition = this.mEngine.getPage().getModel().size();
                        } else if (model222.mSource.equals(Source.SUPER_ALBUM)) {
                            Log.d(TAG, "onUikitEvent LOADER_MORE_DETAIL_CARDS-superAlbum-" + model222.getId());
                            fetchSuperAlbum(model222);
                        } else if (model222.mSource.equals(Source.ABOUT_TOPIC)) {
                            if (this.mAlbumInfo != null) {
                                album = this.mAlbumInfo.getAlbum();
                                if (album != null && album.isSeries() && !album.isSourceType() && album.chnId == 2) {
                                    Log.d(TAG, "onUikitEvent LOADER_SET_CARDS-Peripheral-" + model222.getId());
                                    fetchPeripheral(model222);
                                }
                            }
                        } else if (model222.mSource.equals("banner")) {
                            Log.d(TAG, "onUikitEvent LOADER_MORE_DETAIL_CARDS-BANNER_AD-" + model222.getId());
                            if (updateBannerAdCard(model222) != null) {
                                notifyCard.add(model222);
                            }
                        } else {
                            notifyCard.add(model222);
                        }
                        notifyCard.add(model222);
                        cardInfoModels = new ArrayList();
                        cardInfoModels.addAll(notifyCard);
                        appendPageData(cardInfoModels);
                        notifyCard.clear();
                    }
                    cardInfoModels = new ArrayList();
                    cardInfoModels.addAll(notifyCard);
                    appendPageData(cardInfoModels);
                    notifyCard.clear();
                    return;
                case 73:
                    LogUtils.d(TAG, "onUikitEvent LOADER_DETAIL_SAVE_DATA ");
                    DetailDataCacheManager.instance().putDetailGroupTime(uikitEvent.sourceId);
                    return;
                default:
                    return;
            }
        }
        LogUtils.d(TAG, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx:" + uikitEvent.uikitEngineId + "  " + this.mEngine.getId());
    }

    public List<CardInfoModel> deepCopy(List<CardInfoModel> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        new ObjectOutputStream(byteOut).writeObject(src);
        return (List) new ObjectInputStream(new ByteArrayInputStream(byteOut.toByteArray())).readObject();
    }

    public ItemInfoModel[][] getTrailerItem() {
        ItemInfoModel[][] itemInfoModels = new ItemInfoModel[0][];
        for (CardInfoModel each : this.mEngine.getPage().getModel()) {
            if (each.mSource != null && each.mSource.equals(Source.TRAILERS)) {
                itemInfoModels = each.getItemInfoModels();
                if (itemInfoModels == null) {
                    return new ItemInfoModel[0][];
                }
                return itemInfoModels;
            }
        }
        return itemInfoModels;
    }

    private void fetchBannerAd() {
        Log.v(TAG, "fetchBannerAd ");
        this.mBannerAds = new ArrayList();
        UikitDataFetcher.callBannerAd(this.mAlbumInfo.getChannelId(), this.mAlbumInfo.getAlbumId(), this.mAlbumInfo.getTvId(), false, true, new UikitDataFetcherCallback(this));
    }

    private CardInfoModel updateBannerAdCard(CardInfoModel model) {
        if (this.mBannerAds == null || this.mBannerAds.size() <= 0) {
            Log.d(TAG, "updateBannerAdCard is null");
        } else {
            synchronized (this.mBannerAds) {
                Log.d(TAG, "updateBannerAdCard-ad id-" + model.getId());
                for (CardInfoModel adModel : this.mBannerAds) {
                    Log.d(TAG, "updateBannerAdCard-banner card id-" + adModel.getId());
                    if (model.getId().equals(adModel.getId())) {
                        Log.d(TAG, "Group Detail update- update banner 2");
                        adModel.mSource = "banner";
                        return adModel;
                    }
                }
            }
        }
        return null;
    }

    private void updateCardDataForSourceSwitch(CardInfoModel card) {
        LogRecordUtils.logd(TAG, ">> updateCardDataForSourceSwitch");
        Message msg = new Message();
        msg.arg1 = 7;
        msg.obj = card;
        this.myHandler.sendMessage(msg);
    }

    private void insertCardData(CardInfoModel card, int position) {
        LogRecordUtils.logd(TAG, ">> insertCardData");
        Message msg = new Message();
        msg.arg1 = 6;
        msg.arg2 = position;
        msg.obj = card;
        this.myHandler.sendMessage(msg);
    }

    private void removeCardData(int index, int count) {
        LogRecordUtils.logd(TAG, ">> removeCardData");
        Message msg = new Message();
        msg.arg1 = 5;
        msg.arg2 = index;
        msg.obj = Integer.valueOf(count);
        this.myHandler.sendMessage(msg);
    }

    public void updateCardList(List<CardInfoModel> cardList) {
        if (LogUtils.mIsDebug) {
            LogRecordUtils.logd(TAG, ">> updateCardList data ");
        }
        if (cardList != null && !ListUtils.isEmpty((List) cardList)) {
            Message msg = new Message();
            msg.arg1 = 3;
            msg.obj = cardList;
            this.myHandler.sendMessage(msg);
        }
    }

    private boolean isCardWrong(CardInfoModel card) {
        boolean wrong = card == null || card.getItemInfoModels() == null || card.getItemInfoModels().length == 0;
        if (wrong) {
            LogRecordUtils.loge(TAG, "isCardWrong");
        }
        return wrong;
    }

    public void createDefault(int index) {
        CardInfoModel loadingcardmodel = new CardInfoModel();
        loadingcardmodel.setCardType((short) 999);
        loadingcardmodel.setBodyHeight(ResourceUtil.getPxShort(200));
        List<CardInfoModel> cards = new ArrayList(2);
        cards.add(loadingcardmodel);
        bindDataSource(cards);
        LogUtils.d(TAG, "createDefault");
    }

    public void cleanDefault() {
    }

    public void destroy() {
        EventBus.getDefault().unregister(this);
        if (this.loader != null) {
            this.loader.unregister();
            this.loader.unRegisterThread3();
        }
    }

    public UIKitEngine getUIkitEngine() {
        return this.mEngine;
    }

    public UIKitEngine getAllViewEngine() {
        return this.mAllViewEngine;
    }

    public void showAllView(CardInfoModel cardInfoModel) {
        Log.v(TAG, " cardInfo.cardLayoutId = " + cardInfoModel.cardLayoutId);
        StringBuffer sb;
        if (cardInfoModel.mSource.equals("star")) {
            List<Star> stars = (List) this.allStarMap.get(cardInfoModel.mSource);
            sb = new StringBuffer();
            sb.append(DetailConstants.CONTENT_TITLE_STARLIST).append(" ").append(stars.size()).append("个");
            cardInfoModel.setTitle(sb.toString());
            cardInfoModel = CardInfoBuildTool.buildStarsCard(cardInfoModel, stars, true);
        } else {
            List<Album> albumList = (List) this.allEnterMap.get(cardInfoModel.mSource);
            sb = new StringBuffer();
            String title = cardInfoModel.getTitle();
            if (!title.contains("部")) {
                sb.append(title).append(" ").append(albumList.size()).append("部");
                cardInfoModel.setTitle(sb.toString());
            }
            cardInfoModel = CardInfoBuildTool.buildAlbumCard(cardInfoModel, albumList, true);
        }
        bindAllViewDataSource(cardInfoModel);
    }

    public void pageScrollStart() {
    }

    public void pageScrollEnd() {
        this.mEngine.stop();
        this.mstarted = false;
    }

    public void onPageIn() {
        if (!this.mstarted) {
            this.mEngine.start();
            this.mstarted = true;
        }
        onPagePingbackStartTime();
    }

    public void reset() {
    }

    public void onPageOut() {
        this.mEngine.getPage().backToTop();
        sendPageShowPingback(true);
        sendCardShowPingback(true);
    }

    public void onActivityIn() {
        LogUtils.d(TAG, "onActivityIn");
        if (this.mEngine != null && !this.mstarted) {
            this.mEngine.start();
            this.mstarted = true;
        }
    }

    public void onActivityOut() {
        LogUtils.d(TAG, "onActivityOut");
        this.mEngine.stop();
        this.mstarted = false;
        sendPageShowPingback(false);
        this.mEngine.stop();
        this.mstarted = false;
        sendCardShowPingback(false);
    }

    public void onScreenSaverDismiss() {
        onPagePingbackStartTime();
    }

    public void onScreenSaverShow() {
        sendPageShowPingback(false);
        sendCardShowPingback(false);
    }

    public void onExitDialogDismiss() {
        onPagePingbackStartTime();
    }

    public void onExitDialogShow() {
        sendPageShowPingback(false);
        sendCardShowPingback(false);
    }

    private void onPagePingbackStartTime() {
        this.mPageStartShowTime = SystemClock.elapsedRealtime();
    }

    private void sendPageShowPingback(boolean isPageSwitch) {
        if (SystemClock.elapsedRealtime() - this.mPageStartShowTime >= 500) {
        }
    }

    private void sendCardShowPingback(boolean isPageSwitch) {
        if (SystemClock.elapsedRealtime() - this.mPageStartShowTime >= 500) {
        }
    }

    private void fetchPeripheral(CardInfoModel card) {
        if (this.mAlbumInfo.getAlbum() != null) {
            LogRecordUtils.logd(TAG, ">> fetchPeripheral");
            ConditionVariable peripheralLock = new ConditionVariable();
            FetchPeripheralTask task = new FetchPeripheralTask(this.mAlbumInfo);
            task.setTaskListener(new FetchPeripheralListener(this, card, peripheralLock));
            task.execute();
            LogRecordUtils.logd(TAG, ">> fetchPeripheral block" + peripheralLock);
            peripheralLock.block(30000);
            return;
        }
        throw new IllegalArgumentException("Incoming Album is null!!");
    }

    private void fetchRecommend(CardInfoModel card) {
        if (this.mAlbumInfo.getAlbum() != null) {
            LogRecordUtils.logd(TAG, ">> fetchRecommend");
            ConditionVariable recommendLock = new ConditionVariable();
            FetchRecommendTask task = new FetchRecommendTask(this.mAlbumInfo);
            task.setTaskListener(new FetchRecommendListener(this, card, recommendLock));
            task.execute();
            LogRecordUtils.logd(TAG, ">> fetchRecommend block" + recommendLock);
            recommendLock.block(30000);
            return;
        }
        throw new IllegalArgumentException("Incoming Album is null!!");
    }

    private void fetchTrailers(CardInfoModel card) {
        if (this.mAlbumInfo.getAlbum() != null) {
            LogRecordUtils.logd(TAG, ">> fetchTrailer");
            ConditionVariable trailerLock = new ConditionVariable();
            FetchTrailerTask task = new FetchTrailerTask(this.mAlbumInfo);
            task.setTaskListener(new FetchTrailerTaskListener(this, card, trailerLock));
            task.execute();
            LogRecordUtils.logd(TAG, "fetchTrailer block" + trailerLock);
            trailerLock.block(30000);
            return;
        }
        throw new IllegalArgumentException("Incoming Album is null!!");
    }

    private void fetchStar(CardInfoModel card) {
        if (this.mAlbumInfo.getAlbum() != null) {
            LogRecordUtils.logd(TAG, ">> fetchStar");
            ConditionVariable starLock = new ConditionVariable();
            FetchStarTask task = new FetchStarTask(this.mAlbumInfo);
            task.setTaskListener(new FetchStarTaskListener(this, card, starLock));
            task.execute();
            LogRecordUtils.logd(TAG, "fetchStar block" + starLock);
            starLock.block(30000);
            return;
        }
        throw new IllegalArgumentException("Incoming Album is null!!");
    }

    private void fetchSuperAlbum(CardInfoModel card) {
        if (this.mAlbumInfo.getAlbum() != null) {
            LogRecordUtils.logd(TAG, ">> fetchSuperAlbum");
            ConditionVariable superAlbumLock = new ConditionVariable();
            FetchSuperAlbumTask task = new FetchSuperAlbumTask(this.mAlbumInfo);
            task.setTaskListener(new FetchSuperAlbumTaskListener(this, card, superAlbumLock, this.mAlbumInfo));
            task.execute();
            LogRecordUtils.logd(TAG, ">> fetchSuperAlbum block" + superAlbumLock);
            superAlbumLock.block(30000);
            return;
        }
        throw new IllegalArgumentException("Incoming Album is null!!");
    }
}
