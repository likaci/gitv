package com.gala.video.lib.share.uikit.loader;

import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.gala.tvapi.tv2.constants.ChannelId;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SysPropUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.uikit.cache.UikitDataCache;
import com.gala.video.lib.share.uikit.cache.UikitDataCache.UpdateCacheMessage;
import com.gala.video.lib.share.uikit.cache.UikitSourceDataCache;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig.Source;
import com.gala.video.lib.share.uikit.data.provider.DataRefreshPeriodism;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UikitDataLoader extends BaseUikitDataLoader {
    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public /* bridge */ /* synthetic */ void onGetEvent(UikitEvent uikitEvent) {
        super.onGetEvent(uikitEvent);
    }

    public /* bridge */ /* synthetic */ void onPostEvent(UikitEvent uikitEvent) {
        super.onPostEvent(uikitEvent);
    }

    public /* bridge */ /* synthetic */ void register() {
        super.register();
    }

    public /* bridge */ /* synthetic */ void setBannerAdId(int i) {
        super.setBannerAdId(i);
    }

    public /* bridge */ /* synthetic */ void setChannelId(int i) {
        super.setChannelId(i);
    }

    public /* bridge */ /* synthetic */ void setVipLoader(boolean z) {
        super.setVipLoader(z);
    }

    public /* bridge */ /* synthetic */ void unRegisterThread3() {
        super.unRegisterThread3();
    }

    public /* bridge */ /* synthetic */ void unregister() {
        super.unregister();
    }

    public UikitDataLoader(int cacheType, String sourceId, int uikitEngineId) {
        super(cacheType, sourceId, uikitEngineId);
        this.mLoaderThread3 = new HandlerThread("UikitDataLoader-3");
        this.mLoaderThread3.start();
        this.mLoaderHandler = new LoaderHandler(this.mLoaderThread3.getLooper());
        Log.d("UikitDataLoader", "UikitDataLoader thread id = " + this.mLoaderThread3.getThreadId() + ", uikitengine id+" + this.mUikitEngineId);
    }

    public UikitDataLoader(int cacheType, String sourceId, int uikitEngineId, int pageIndex) {
        super(cacheType, sourceId, uikitEngineId);
        this.mLoaderHandler = new LoaderHandler(getHandlerThread(pageIndex).getLooper());
    }

    public void firstCardList() {
        createInitTask();
    }

    public void setSourceID(String sourceID) {
        this.mSourceId = sourceID;
    }

    private void createInitTask() {
        UikitEvent event = new UikitEvent();
        event.uikitEngineId = this.mUikitEngineId;
        event.eventType = 1;
        event.pageNo = 1;
        event.sourceId = this.mSourceId;
        Message msg = Message.obtain();
        msg.obj = event;
        this.mLoaderHandler.sendMessage(msg);
    }

    protected void invokeAction(UikitEvent event) {
        Log.d("UikitDataLoader", "eventtype-" + event.eventType + "-uikitengineid-" + event.uikitEngineId + "-sourceId-" + this.mSourceId);
        switch (event.eventType) {
            case 1:
                initPageAction(event);
                return;
            case 16:
            case 35:
                scrollTopAction(event);
                return;
            case 17:
                addCardsAction(event);
                return;
            case 18:
                scrollStopAction(event);
                return;
            case 36:
            case 48:
                if (AppRuntimeEnv.get().isPlayInHome() && ChannelId.CHANNEL_ID_CAROUSEL == this.mChannelId) {
                    LogUtils.d("UikitDataLoader", "carousel is playing,no update home carousel tab");
                    return;
                } else {
                    updateGroupDetailAction(event);
                    return;
                }
            case 49:
            case 65:
                updateChannelAction(event);
                return;
            case 50:
            case 66:
                updateAppAction(event);
                return;
            case 64:
                LogUtils.d("UikitDataLoader", "onUikitEvent LOADER_PAGE_INIT:" + event.layoutChange);
                if (event.layoutChange == 1) {
                    initDetailPageAction(event);
                    return;
                } else if (event.layoutChange == 0) {
                    LogUtils.i("UikitDataLoader", "initPageAction NODATA!!! ");
                    return;
                } else {
                    return;
                }
            case 67:
                updateBannerAdAction(event);
                return;
            case UikitEventType.UIKIT_ADD_DETAIL_CARDS /*70*/:
                addDetailCardsAction(event);
                return;
            case 73:
                LogUtils.d("UikitDataLoader", "onUikitEvent LOADER_DETAIL_SAVE_DATA ");
                this.mCache.write(this.mCacheType, this.mSourceId, 1, this.mUikitEngineId, event.cardList);
                return;
            case 80:
                LogUtils.d("UikitDataLoader", "onUikitEvent UPDATE_DETAIL_GROUP_DATA:" + event.layoutChange);
                updateDetailPageAction(event);
                return;
            default:
                return;
        }
    }

    private void initPageAction(final UikitEvent event) {
        if (this.mCacheType == 0) {
            List<CardInfoModel> firstCardList = this.mCache.read(this.mCacheType, this.mSourceId, 1, this.mUikitEngineId, true);
            if (firstCardList != null && firstCardList.size() > 0) {
                Log.d("UikitDataLoader", "initPageAction disk is not null-fetch data from cache-" + this.mSourceId);
                for (CardInfoModel model : firstCardList) {
                    if (model.getCardType() == UIKitConfig.CARD_TYPE_COVER_FLOW) {
                        model.isCacheData = true;
                    }
                }
                UikitEvent event1 = new UikitEvent(event);
                event1.eventType = 0;
                event1.layoutChange = 1;
                event1.uikitEngineId = this.mUikitEngineId;
                event1.cardList = firstCardList;
                onPostEvent(event1);
                addSourceUpdateInfo(firstCardList, true);
                this.mCache.setLock(this.mSourceId, 1, false);
                UikitEvent event2 = new UikitEvent(event);
                event2.eventType = 36;
                event2.uikitEngineId = this.mUikitEngineId;
                event2.pageNo = 1;
                Message msg = Message.obtain();
                msg.obj = event2;
                this.mLoaderHandler.sendMessage(msg);
                return;
            }
        }
        Log.d("UikitDataLoader", "initPageAction-disk is null-fetch data from online-" + this.mSourceId);
        UikitDataFetcher.callGroupDetail(this.mSourceId, 1, this.mUikitEngineId, this.mIsVipLoader, true, new IUikitDataFetcherCallback() {
            public void onSuccess(List<CardInfoModel> list, String back) {
                UikitEvent event1;
                if (list == null || list.size() == 0) {
                    LogUtils.d("UikitDataLoader", "initPageAction-disk is null-card size =0-" + UikitDataLoader.this.mSourceId);
                    if (UikitDataLoader.this.mCacheType == 0 || UikitDataLoader.this.mCacheType == 2) {
                        event1 = new UikitEvent(event);
                        event1.eventType = 0;
                        event1.layoutChange = 0;
                        event1.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                        UikitDataLoader.this.onPostEvent(event1);
                    } else if (UikitDataLoader.this.mCacheType == 3) {
                        event1 = new UikitEvent(event);
                        event1.eventType = 32;
                        event1.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                        event1.sourceId = UikitDataLoader.this.mSourceId;
                        event1.cardList = null;
                        event1.background = back;
                        UikitDataLoader.this.onPostEvent(event1);
                    }
                } else if (UikitDataLoader.this.mCacheType == 0 || UikitDataLoader.this.mCacheType == 2) {
                    event1 = new UikitEvent(event);
                    event1.eventType = 0;
                    event1.layoutChange = 1;
                    event1.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                    event1.cardList = list;
                    UikitDataLoader.this.onPostEvent(event1);
                    if (UikitDataLoader.this.mCacheType == 0) {
                        UikitDataLoader.this.mCache.writeDisk(list, UikitDataLoader.this.mCacheType, UikitDataLoader.this.mSourceId);
                    }
                    UikitDataLoader.this.mCache.write(UikitDataLoader.this.mCacheType, UikitDataLoader.this.mSourceId, 1, UikitDataLoader.this.mUikitEngineId, list);
                    UikitDataLoader.this.addSourceUpdateInfo(list, true);
                    UikitDataLoader.this.sendBannerAdMessage();
                } else if (UikitDataLoader.this.mCacheType == 3) {
                    event1 = new UikitEvent(event);
                    event1.eventType = 32;
                    event1.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                    event1.sourceId = UikitDataLoader.this.mSourceId;
                    event1.cardList = list;
                    event1.background = back;
                    UikitDataLoader.this.onPostEvent(event1);
                }
                if (UikitDataLoader.this.mCacheType == 0 || UikitDataLoader.this.mCacheType == 2) {
                    long time = SystemClock.elapsedRealtime();
                    UikitDataLoader.this.mCache.setUpdateTime(UikitDataLoader.this.mSourceId, SystemClock.elapsedRealtime());
                    Log.d("UikitDataLoader", "Active Update-set update time- " + time + "-" + UikitDataLoader.this.mSourceId);
                }
            }

            public void onFailed() {
                UikitEvent event1;
                if (UikitDataLoader.this.mCacheType == 0 || UikitDataLoader.this.mCacheType == 2) {
                    event1 = new UikitEvent(event);
                    event1.eventType = 0;
                    event1.layoutChange = 0;
                    event1.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                    UikitDataLoader.this.onPostEvent(event1);
                } else if (UikitDataLoader.this.mCacheType == 3) {
                    event1 = new UikitEvent(event);
                    event1.eventType = 32;
                    event1.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                    event1.sourceId = UikitDataLoader.this.mSourceId;
                    event1.cardList = null;
                    UikitDataLoader.this.onPostEvent(event1);
                }
            }
        });
        if (this.mCacheType == 0 || this.mCacheType == 2) {
            this.mCache.postAddCheckUpdateMessage(new UpdateCacheMessage(0, 1, (long) DataRefreshPeriodism.instance().getRefreshInterval(DataRefreshPeriodism.instance().getRefreshLevel(this.mSourceId)), this.mSourceId, this.mUikitEngineId));
        }
    }

    private void initDetailPageAction(final UikitEvent event) {
        Log.d("UikitDataLoader", "initDetailPageAction final UikitEvent event");
        if (this.mCacheType == 2) {
            List<CardInfoModel> firstCardList = this.mCache.read(this.mCacheType, this.mSourceId, 1, this.mUikitEngineId, false);
            if (firstCardList == null || firstCardList.size() <= 0) {
                Log.d("UikitDataLoader", "initDetailPageAction fetch data from online-" + this.mSourceId);
                UikitDataFetcher.callGroupDetail(this.mSourceId, 1, this.mUikitEngineId, this.mIsVipLoader, true, new IUikitDataFetcherCallback() {
                    public void onSuccess(List<CardInfoModel> list, String raw) {
                        UikitEvent event1;
                        if (list == null || list.size() == 0) {
                            LogUtils.d("UikitDataLoader", "initDetailPageAction-disk is null-card size =0-" + UikitDataLoader.this.mSourceId);
                            event1 = new UikitEvent(event);
                            event1.eventType = 64;
                            event1.layoutChange = 0;
                            event1.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                            UikitDataLoader.this.onPostEvent(event1);
                        } else {
                            List<CardInfoModel> cardInfoModelList = new ArrayList();
                            try {
                                cardInfoModelList = UikitDataLoader.this.deepCopy(list);
                            } catch (Exception e) {
                                Log.d("UikitDataLoader", "deepCopy exception-");
                                cardInfoModelList.addAll(list);
                                e.printStackTrace();
                            }
                            UikitEvent event0 = new UikitEvent(event);
                            event0.eventType = 73;
                            event0.sourceId = UikitDataLoader.this.mSourceId;
                            event0.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                            event0.cardList = cardInfoModelList;
                            UikitDataLoader.this.onPostEvent(event0);
                            event1 = new UikitEvent(event);
                            event1.eventType = 69;
                            event1.layoutChange = 1;
                            event1.sourceId = UikitDataLoader.this.mSourceId;
                            event1.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                            event1.cardList = list;
                            UikitDataLoader.this.onPostEvent(event1);
                        }
                        long time = SystemClock.elapsedRealtime();
                        UikitDataLoader.this.mCache.setUpdateTime(UikitDataLoader.this.mSourceId, SystemClock.elapsedRealtime());
                        Log.d("UikitDataLoader", "Active Update-set update time- " + time + "-" + UikitDataLoader.this.mSourceId);
                    }

                    public void onFailed() {
                        UikitEvent event1 = new UikitEvent(event);
                        event1.eventType = 64;
                        event1.layoutChange = 0;
                        event1.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                        UikitDataLoader.this.onPostEvent(event1);
                    }
                });
                return;
            }
            List<CardInfoModel> cardInfoModelList = new ArrayList();
            try {
                cardInfoModelList = deepCopy(firstCardList);
            } catch (Exception e) {
                Log.d("UikitDataLoader", "deepCopy exception-");
                cardInfoModelList.addAll(firstCardList);
                e.printStackTrace();
            }
            Log.d("UikitDataLoader", "initDetailPageAction fetch data from disk-" + this.mSourceId);
            UikitEvent event1 = new UikitEvent(event);
            event1.eventType = 69;
            event1.layoutChange = 1;
            event1.uikitEngineId = this.mUikitEngineId;
            event1.sourceId = this.mSourceId;
            event1.cardList = cardInfoModelList;
            onPostEvent(event1);
        }
    }

    public List<CardInfoModel> deepCopy(List<CardInfoModel> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        new ObjectOutputStream(byteOut).writeObject(src);
        return (List) new ObjectInputStream(new ByteArrayInputStream(byteOut.toByteArray())).readObject();
    }

    public void updateDetailPageAction(final UikitEvent event) {
        Log.d("UikitDataLoader", "updateDetailPageAction fetch data from online-" + this.mSourceId);
        UikitDataFetcher.callGroupDetail(this.mSourceId, 1, this.mUikitEngineId, this.mIsVipLoader, true, new IUikitDataFetcherCallback() {
            public void onSuccess(List<CardInfoModel> list, String raw) {
                if (list == null || list.size() == 0) {
                    LogUtils.d("UikitDataLoader", "updateDetailPageAction-disk is null-card size =0-" + UikitDataLoader.this.mSourceId);
                    UikitEvent event1 = new UikitEvent(event);
                    event1.eventType = 64;
                    event1.layoutChange = 0;
                    event1.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                    UikitDataLoader.this.onPostEvent(event1);
                    return;
                }
                List<CardInfoModel> cardInfoModelList = new ArrayList();
                cardInfoModelList.addAll(list);
                UikitEvent event0 = new UikitEvent(event);
                event0.eventType = 73;
                event0.sourceId = UikitDataLoader.this.mSourceId;
                event0.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                event0.cardList = cardInfoModelList;
                UikitDataLoader.this.onPostEvent(event0);
            }

            public void onFailed() {
                UikitEvent event1 = new UikitEvent(event);
                event1.eventType = 64;
                event1.layoutChange = 0;
                event1.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                UikitDataLoader.this.onPostEvent(event1);
            }
        });
    }

    private void scrollTopAction(UikitEvent event) {
        if (event.eventType == 16) {
            Log.d("UikitDataLoader", "UITKI_SCROLL_TOP-needupdate-" + this.mIsNeedUpdate + "-source id-" + this.mSourceId);
        } else {
            Log.d("UikitDataLoader", "LOADER_UPDATE_PAGE-needupdate-" + this.mIsNeedUpdate + "-source id-" + this.mSourceId);
        }
        if (this.mCacheType == 0 || this.mCacheType == 2) {
            this.mCurrentAddPageNo = 1;
            if (this.mIsNeedUpdate) {
                List<CardInfoModel> firstCardList;
                if (this.mCacheType == 0) {
                    firstCardList = this.mCache.read(this.mCacheType, this.mSourceId, 1, this.mUikitEngineId, (boolean) 1);
                } else {
                    firstCardList = this.mUpdateCardInfoModelList;
                }
                UikitEvent event1 = new UikitEvent(event);
                event1.eventType = 32;
                event1.uikitEngineId = this.mUikitEngineId;
                event1.sourceId = this.mSourceId;
                event1.cardList = firstCardList;
                onPostEvent(event1);
                Log.d("UikitDataLoader", "UITKI_SCROLL_TOP- on post event-sourceId-" + this.mSourceId);
                if (this.mCacheType == 0) {
                    this.mCache.remove(this.mCacheType, this.mSourceId, 2, this.mUikitEngineId);
                } else {
                    this.mCache.remove(this.mCacheType, this.mSourceId, 1, this.mUikitEngineId);
                    this.mCache.write(this.mCacheType, this.mSourceId, 1, this.mUikitEngineId, firstCardList);
                }
                removeSourceUpdateInfo(2, this.mSourceId, this.mUikitEngineId);
                if (!(this.mBannerAdId == -1 || this.mBannerAds == null)) {
                    this.mBannerAds.clear();
                }
                addSourceUpdateInfo(firstCardList, false);
                updateBannerAdCard(UikitSourceDataCache.readBannerAdList());
                this.mIsNeedUpdate = false;
            }
        }
    }

    private void addCardsAction(final UikitEvent event) {
        if (this.mCacheType != 0 || UikitDataCache.getInstance().getCurrentUikitEngineId() == this.mUikitEngineId) {
            final int addPageNo = event.cardInfoModel.mPageNo + 1;
            if (this.mCurrentAddPageNo == addPageNo) {
                Log.d("UikitDataLoader", "UIKIT_ADD_CARDS-current page-" + this.mCurrentAddPageNo + "-add page no-" + addPageNo);
            } else if (this.mCacheType == 0 || this.mCacheType == 2) {
                List<CardInfoModel> moreCardList = this.mCache.read(this.mCacheType, this.mSourceId, addPageNo, this.mUikitEngineId, false);
                if (moreCardList == null || moreCardList.size() == 0) {
                    Log.d("UikitDataLoader", "UIKIT_ADD_CARDS-cache is null-pageNo-" + addPageNo);
                    UikitDataFetcher.callGroupDetail(this.mSourceId, addPageNo, this.mUikitEngineId, this.mIsVipLoader, true, new IUikitDataFetcherCallback() {
                        public void onSuccess(List<CardInfoModel> list, String raw) {
                            if ((UikitDataLoader.this.mCacheType == 0 && UikitDataCache.getInstance().getCurrentUikitEngineId() == UikitDataLoader.this.mUikitEngineId) || UikitDataLoader.this.mCacheType == 2) {
                                List<CardInfoModel> lastCardList = UikitDataLoader.this.mCache.read(UikitDataLoader.this.mCacheType, UikitDataLoader.this.mSourceId, addPageNo - 1, UikitDataLoader.this.mUikitEngineId, false);
                                List<CardInfoModel> newCardList = null;
                                if (list != null && list.size() > 0) {
                                    newCardList = new ArrayList(list);
                                    if (lastCardList != null && lastCardList.size() > 0) {
                                        for (CardInfoModel lastModel : lastCardList) {
                                            for (int i = 0; i < list.size(); i++) {
                                                if (lastModel.mCardId.equals(((CardInfoModel) list.get(i)).mCardId)) {
                                                    newCardList.remove(i);
                                                }
                                            }
                                        }
                                    }
                                }
                                UikitEvent event2 = new UikitEvent(event);
                                event2.eventType = 33;
                                event2.pageNo = addPageNo;
                                event2.sourceId = UikitDataLoader.this.mSourceId;
                                event2.cardList = newCardList;
                                UikitDataLoader.this.onPostEvent(event2);
                                UikitDataLoader.this.mCurrentAddPageNo = addPageNo;
                                Log.d("UikitDataLoader", "UIKIT_ADD_CARDS-post event-pageNo-" + addPageNo);
                                if (newCardList != null && newCardList.size() > 0 && UikitDataLoader.this.mCacheType == 0) {
                                    UikitDataLoader.this.mCache.write(UikitDataLoader.this.mCacheType, UikitDataLoader.this.mSourceId, addPageNo, UikitDataLoader.this.mUikitEngineId, list);
                                    UikitDataLoader.this.addSourceUpdateInfo(list, false);
                                    UikitDataLoader.this.updateBannerAdCard(UikitSourceDataCache.readBannerAdList());
                                }
                            }
                        }

                        public void onFailed() {
                            UikitEvent event2 = new UikitEvent(event);
                            event2.eventType = 33;
                            event2.pageNo = addPageNo;
                            event2.sourceId = UikitDataLoader.this.mSourceId;
                            event2.cardList = null;
                            UikitDataLoader.this.onPostEvent(event2);
                            Log.e("UikitDataLoader", "UIKIT_ADD_CARDS-post event-pageNo-" + addPageNo);
                        }
                    });
                    return;
                }
                Log.d("UikitDataLoader", "UIKIT_ADD_CARDS-cache is not null-pageNo-" + addPageNo);
                for (CardInfoModel model : moreCardList) {
                    if (model.getCardType() == UIKitConfig.CARD_TYPE_COVER_FLOW) {
                        model.isCacheData = true;
                    }
                }
                UikitEvent event2 = new UikitEvent(event);
                event2.eventType = 33;
                event2.pageNo = addPageNo;
                event2.sourceId = this.mSourceId;
                event2.cardList = moreCardList;
                onPostEvent(event2);
                Log.e("UikitDataLoader", "UIKIT_ADD_CARDS-post event-pageNo-" + addPageNo);
                this.mCurrentAddPageNo = addPageNo;
            } else if (this.mCacheType == 3) {
                UikitDataFetcher.callGroupDetail(this.mSourceId, addPageNo, this.mUikitEngineId, this.mIsVipLoader, true, new IUikitDataFetcherCallback() {
                    public void onSuccess(List<CardInfoModel> list, String raw) {
                        if (list == null || list.size() == 0) {
                            UikitEvent event2 = new UikitEvent(event);
                            event2.eventType = 33;
                            event2.pageNo = addPageNo;
                            event2.sourceId = UikitDataLoader.this.mSourceId;
                            event2.cardList = null;
                            UikitDataLoader.this.onPostEvent(event2);
                            return;
                        }
                        event2 = new UikitEvent(event);
                        event2.eventType = 33;
                        event2.pageNo = addPageNo;
                        event2.sourceId = UikitDataLoader.this.mSourceId;
                        event2.cardList = list;
                        UikitDataLoader.this.onPostEvent(event2);
                        UikitDataLoader.this.mCurrentAddPageNo = addPageNo;
                    }

                    public void onFailed() {
                        UikitEvent event2 = new UikitEvent(event);
                        event2.eventType = 33;
                        event2.pageNo = addPageNo;
                        event2.sourceId = UikitDataLoader.this.mSourceId;
                        event2.cardList = null;
                        UikitDataLoader.this.onPostEvent(event2);
                    }
                });
            }
        }
    }

    private void addDetailCardsAction(final UikitEvent event) {
        final int addPageNo = event.cardInfoModel.mPageNo + 1;
        if (this.mCurrentAddPageNo == addPageNo) {
            Log.d("UikitDataLoader", "UIKIT_ADD_DETAIL_CARDS-current page-" + this.mCurrentAddPageNo + "-add page no-" + addPageNo);
            return;
        }
        List<CardInfoModel> moreCardList = this.mCache.read(this.mCacheType, this.mSourceId, addPageNo, this.mUikitEngineId, false);
        if (moreCardList == null || moreCardList.size() == 0) {
            Log.d("UikitDataLoader", "UIKIT_ADD_DETAIL_CARDS-cache is null-pageNo-" + addPageNo);
            UikitDataFetcher.callGroupDetail(this.mSourceId, addPageNo, this.mUikitEngineId, this.mIsVipLoader, true, new IUikitDataFetcherCallback() {
                public void onSuccess(List<CardInfoModel> list, String raw) {
                    List<CardInfoModel> lastCardList = UikitDataLoader.this.mCache.read(UikitDataLoader.this.mCacheType, UikitDataLoader.this.mSourceId, addPageNo - 1, UikitDataLoader.this.mUikitEngineId, false);
                    List<CardInfoModel> newCardList = null;
                    if (list != null && list.size() > 0) {
                        newCardList = new ArrayList(list);
                        if (lastCardList != null && lastCardList.size() > 0) {
                            for (CardInfoModel lastModel : lastCardList) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (lastModel.mCardId.equals(((CardInfoModel) list.get(i)).mCardId)) {
                                        newCardList.remove(i);
                                    }
                                }
                            }
                        }
                    }
                    UikitEvent event2 = new UikitEvent(event);
                    event2.eventType = 71;
                    event2.pageNo = addPageNo;
                    event2.sourceId = UikitDataLoader.this.mSourceId;
                    event2.cardList = newCardList;
                    UikitDataLoader.this.onPostEvent(event2);
                    UikitDataLoader.this.mCurrentAddPageNo = addPageNo;
                    Log.d("UikitDataLoader", "UIKIT_ADD_DETAIL_CARDS-post event-pageNo-" + addPageNo);
                }

                public void onFailed() {
                    UikitEvent event2 = new UikitEvent(event);
                    event2.eventType = 71;
                    event2.pageNo = addPageNo;
                    event2.sourceId = UikitDataLoader.this.mSourceId;
                    event2.cardList = null;
                    UikitDataLoader.this.onPostEvent(event2);
                    Log.e("UikitDataLoader", "UIKIT_ADD_DETAIL_CARDS-post event-pageNo-" + addPageNo);
                }
            });
            return;
        }
        Log.d("UikitDataLoader", "UIKIT_ADD_DETAIL_CARDS-cache is not null-pageNo-" + addPageNo);
        UikitEvent event2 = new UikitEvent(event);
        event2.eventType = 71;
        event2.pageNo = addPageNo;
        event2.sourceId = this.mSourceId;
        event2.cardList = moreCardList;
        onPostEvent(event2);
        Log.e("UikitDataLoader", "UIKIT_ADD_DETAIL_CARDS-post event-pageNo-" + addPageNo);
        this.mCurrentAddPageNo = addPageNo;
    }

    private void scrollStopAction(UikitEvent event) {
        Log.d("UikitDataLoader", "UIKIT_SCROLL_PLACE-pageno-" + event.cardInfoModel.mPageNo + "-needupdate-" + this.mIsNeedUpdate);
        if (this.mCacheType == 0 || this.mCacheType == 2) {
            for (int p = event.cardInfoModel.mPageNo; p > 0; p--) {
                this.mCache.setLock(this.mSourceId, p, true);
            }
            this.mCache.setCurrentUikitEngineId(this.mUikitEngineId);
        }
    }

    private void updateGroupDetailAction(UikitEvent event) {
        if (this.mCacheType == 0 || this.mCacheType == 2) {
            List<CardInfoModel> firstCardList = this.mCache.read(this.mCacheType, this.mSourceId, 1, this.mUikitEngineId, true);
            boolean isNeedFetch = false;
            if (firstCardList == null || firstCardList.size() == 0) {
                isNeedFetch = true;
            }
            UikitDataFetcher.callGroupDetail(this.mSourceId, 1, this.mUikitEngineId, this.mIsVipLoader, isNeedFetch, new IUikitDataFetcherCallback() {
                public void onSuccess(List<CardInfoModel> list, String raw) {
                    if (list != null && list.size() > 0) {
                        if (UikitDataLoader.this.mCacheType == 0) {
                            UikitDataLoader.this.mCache.writeDisk(list, UikitDataLoader.this.mCacheType, UikitDataLoader.this.mSourceId);
                        } else {
                            UikitDataLoader.this.mUpdateCardInfoModelList = list;
                        }
                        UikitDataLoader.this.mIsNeedUpdate = true;
                        UikitDataLoader.this.addSourceUpdateInfo(list, false);
                        UikitDataLoader.this.sendBannerAdMessage();
                        boolean isLock = UikitDataLoader.this.mCache.getLock(UikitDataLoader.this.mSourceId, 1);
                        Log.d("UikitDataLoader", "Update-group detail-" + UikitDataLoader.this.mSourceId + "-lock-" + isLock);
                        if (!isLock) {
                            UikitEvent event = new UikitEvent();
                            event.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                            event.eventType = 35;
                            event.pageNo = 1;
                            event.sourceId = UikitDataLoader.this.mSourceId;
                            Message msg = Message.obtain();
                            msg.obj = event;
                            UikitDataLoader.this.mLoaderHandler.sendMessage(msg);
                        }
                    }
                    long time = SystemClock.elapsedRealtime();
                    UikitDataLoader.this.mCache.setUpdateTime(UikitDataLoader.this.mSourceId, time);
                    Log.d("UikitDataLoader", "Active Update-set update time- " + time + "-" + UikitDataLoader.this.mSourceId);
                }

                public void onFailed() {
                    UikitDataLoader.this.mIsNeedUpdate = false;
                }
            });
            int interval = DataRefreshPeriodism.instance().getRefreshInterval(DataRefreshPeriodism.instance().getRefreshLevel(this.mSourceId));
            int debugInterval = SysPropUtils.getInt("log.interval.data.update", 0);
            if (AppClientUtils.isDebugMode() && debugInterval > 0) {
                interval = (debugInterval * 60) * 1000;
                LogUtils.d("UikitDataLoader", "initPageAction, debug interval : " + interval);
            }
            UpdateCacheMessage updateMsg = new UpdateCacheMessage(0, 1, (long) interval, this.mSourceId, this.mUikitEngineId);
            updateMsg.setUpdateTime(SystemClock.elapsedRealtime());
            this.mCache.postAddCheckUpdateMessage(updateMsg);
            Log.d("UikitDataLoader", "Group Detail update- send update msg" + this.mSourceId);
        }
    }

    private void updateChannelAction(final UikitEvent event) {
        CardInfoModel channelModel = this.mCache.read(this.mCacheType, this.mSourceId, event.pageNo, event.cardId, this.mUikitEngineId);
        if (channelModel != null) {
            Log.d("UikitDataLoader", "channel card model is not null");
            UikitDataFetcher.callChannelList(channelModel, new IUikitDataFetcherCallback() {
                public void onSuccess(List<CardInfoModel> list, String raw) {
                    if (list != null && list.size() > 0) {
                        CardInfoModel channelModel = (CardInfoModel) list.get(0);
                        if (UikitDataLoader.this.mCache.update(UikitDataLoader.this.mCacheType, UikitDataLoader.this.mSourceId, event.pageNo, event.cardId, UikitDataLoader.this.mUikitEngineId, channelModel)) {
                            UikitEvent event3 = new UikitEvent(event);
                            event3.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                            event3.eventType = 34;
                            event3.cardInfoModel = channelModel;
                            event3.pageNo = event.pageNo;
                            UikitDataLoader.this.onPostEvent(event3);
                            if (event.pageNo == 1) {
                                UikitDataLoader.this.mCache.rewriteDisk(UikitDataLoader.this.mCacheType, UikitDataLoader.this.mSourceId, UikitDataLoader.this.mUikitEngineId);
                            }
                        }
                    }
                }

                public void onFailed() {
                }
            });
        }
        this.mCache.postAddCheckUpdateMessage(new UpdateCacheMessage(1, event.pageNo, (long) DataRefreshPeriodism.instance().getRefreshInterval(3), this.mSourceId, this.mUikitEngineId));
    }

    private void updateAppAction(final UikitEvent event) {
        CardInfoModel appModel = this.mCache.read(this.mCacheType, this.mSourceId, event.pageNo, event.cardId, this.mUikitEngineId);
        if (appModel != null) {
            UikitDataFetcher.callApp(appModel, new IUikitDataFetcherCallback() {
                public void onSuccess(List<CardInfoModel> list, String raw) {
                    if (list != null && list.size() > 0) {
                        CardInfoModel appModel = (CardInfoModel) list.get(0);
                        if (UikitDataLoader.this.mCache.update(UikitDataLoader.this.mCacheType, UikitDataLoader.this.mSourceId, event.pageNo, event.cardId, UikitDataLoader.this.mUikitEngineId, appModel)) {
                            UikitEvent event4 = new UikitEvent(event);
                            event4.eventType = 34;
                            event4.uikitEngineId = UikitDataLoader.this.mUikitEngineId;
                            event4.cardInfoModel = appModel;
                            event4.pageNo = event.pageNo;
                            UikitDataLoader.this.onPostEvent(event4);
                            if (event.pageNo == 1) {
                                UikitDataLoader.this.mCache.rewriteDisk(UikitDataLoader.this.mCacheType, UikitDataLoader.this.mSourceId, UikitDataLoader.this.mUikitEngineId);
                            }
                        }
                    }
                }

                public void onFailed() {
                }
            });
        }
    }

    private void updateBannerAdAction(UikitEvent event) {
        Log.d("UikitDataLoader", "LOADER_BANNER_AD");
        UikitDataFetcher.callBannerAd(this.mChannelId, this.mAlbumid, this.mTvQid, this.mIsVipLoader, new IUikitDataFetcherCallback() {
            public void onSuccess(List<CardInfoModel> list, String raw) {
                if (list != null && list.size() > 0) {
                    Log.d("UikitDataLoader", "Group Detail update- load banner 1-" + UikitDataLoader.this.mSourceId);
                    UikitSourceDataCache.writeBannerAdList(list);
                    UikitDataLoader.this.updateBannerAdCard(list);
                }
            }

            public void onFailed() {
            }
        });
    }

    private void addSourceUpdateInfo(List<CardInfoModel> list, boolean isFirstLaunch) {
        if (list != null && list.size() > 0) {
            for (CardInfoModel model : list) {
                if (model.mSource != null) {
                    UikitEvent event;
                    Message msg;
                    if (model.mSource.equals(Source.CHANNEL_LIST)) {
                        this.mCache.addUpdateCardInfo(1, model.mPageNo, model.mCardId, this.mSourceId, this.mUikitEngineId);
                        ItemInfoModel[][] items = model.getItemInfoModels();
                        if (isFirstLaunch || items == null || items.length == 0) {
                            event = new UikitEvent();
                            event.uikitEngineId = this.mUikitEngineId;
                            event.eventType = 65;
                            event.pageNo = model.mPageNo;
                            event.sourceId = this.mSourceId;
                            event.cardId = model.mCardId;
                            msg = Message.obtain();
                            msg.obj = event;
                            this.mLoaderHandler.sendMessage(msg);
                        }
                    } else if (model.mSource.equals(Source.APPLICATION)) {
                        this.mCache.addUpdateCardInfo(2, model.mPageNo, model.mCardId, this.mSourceId, this.mUikitEngineId);
                        event = new UikitEvent();
                        event.uikitEngineId = this.mUikitEngineId;
                        event.eventType = 66;
                        event.pageNo = model.mPageNo;
                        event.sourceId = this.mSourceId;
                        event.cardId = model.mCardId;
                        msg = Message.obtain();
                        msg.obj = event;
                        this.mLoaderHandler.sendMessage(msg);
                    } else if (this.mBannerAdId != -1 && model.mSource.equals("banner")) {
                        if (this.mBannerAds == null) {
                            this.mBannerAds = new ArrayList(3);
                        }
                        Log.d("UikitDataLoader", "addSourceUpdateInfo-banner-model-" + model.getId());
                        this.mBannerAds.add(model);
                    }
                }
            }
        }
    }

    private void sendBannerAdMessage() {
        Log.d("UikitDataLoader", "channel id = " + this.mBannerAdId);
        if (this.mBannerAdId != -1) {
            if (this.mBannerAds == null) {
                this.mBannerAds = new ArrayList(3);
            }
            UikitEvent event3 = new UikitEvent();
            event3.eventType = 67;
            event3.uikitEngineId = this.mUikitEngineId;
            event3.sourceId = this.mSourceId;
            Message msg = Message.obtain();
            msg.obj = event3;
            this.mLoaderHandler.sendMessage(msg);
        }
    }

    private void updateBannerAdCard(List<CardInfoModel> list) {
        if (list != null && list.size() > 0 && this.mBannerAds != null && this.mBannerAds.size() > 0) {
            synchronized (this.mBannerAds) {
                for (CardInfoModel model : list) {
                    Log.d("UikitDataLoader", "updateBannerAdCard-ad id-" + model.getId());
                    for (CardInfoModel adModel : this.mBannerAds) {
                        Log.d("UikitDataLoader", "updateBannerAdCard-banner card id-" + adModel.getId());
                        if (model.getId().equals(adModel.getId())) {
                            adModel.setCardType(model.getCardType());
                            adModel.setItemInfoModels(model.getItemInfoModels());
                            adModel.setBodyHeight(model.getBodyHeight());
                            this.mCache.update(this.mCacheType, this.mSourceId, adModel.mPageNo, adModel.mCardId, this.mUikitEngineId, adModel);
                            UikitEvent event = new UikitEvent();
                            event.uikitEngineId = this.mUikitEngineId;
                            event.eventType = 34;
                            event.cardInfoModel = adModel;
                            event.cardId = adModel.mCardId;
                            event.sourceId = this.mSourceId;
                            event.pageNo = adModel.mPageNo;
                            onPostEvent(event);
                            Log.d("UikitDataLoader", "Group Detail update- update banner 2-" + this.mSourceId);
                        }
                    }
                }
            }
        }
    }

    private void removeSourceUpdateInfo(int pageNo, String sourceId, int uikitEngineId) {
        this.mCache.removeUpdateCardInfoList(1, pageNo, sourceId, uikitEngineId);
        this.mCache.removeUpdateCardInfoList(2, pageNo, sourceId, uikitEngineId);
    }
}
