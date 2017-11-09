package com.gala.video.lib.share.uikit.cache;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.gala.tvapi.vrs.result.ApiResultGroupDetail;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.provider.DataRefreshPeriodism;
import com.gala.video.lib.share.uikit.loader.UikitEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UikitDataCache implements IUikitDataCache {
    private static final String TAG = "UikitDataCache";
    private static final UikitDataCache sCache = new UikitDataCache();
    private CacheHandler mCacheHandler;
    private HandlerThread mCacheThread;
    private int mCurrentUikitEngineId;
    private String mFilePath;
    private long mHomeKeyEventTime;
    private List<String> mHomeSourceIdList;
    private boolean mIsHomeActivityActive;
    private Map<String, UikitResourceData> mUikitCahce;
    private List<UpdateCacheMessage> mUikitEngineAndSourceList;
    private Map<Integer, List<UpdateCardInfo>> mUpdateCardInfoMap;

    private class CacheHandler extends Handler {
        private final long NO_ACTIVE_INTERVAL = 600000;

        public CacheHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            UpdateCacheMessage updateCacheMessage;
            UikitEvent groupEvent;
            if (msg.what == 1) {
                Log.d(UikitDataCache.TAG, "check update queue");
                updateCacheMessage = msg.obj;
                UikitEvent event;
                switch (updateCacheMessage.getUpdateType()) {
                    case 0:
                        if (UikitDataCache.this.mHomeSourceIdList.contains(updateCacheMessage.getSourceId())) {
                            UpdateCacheMessage delayUpdateMsg;
                            Message sendMsg;
                            if (!UikitDataCache.this.mIsHomeActivityActive) {
                                Log.d(UikitDataCache.TAG, "Active Update-not active-don't update-" + updateCacheMessage.getSourceId());
                                delayUpdateMsg = new UpdateCacheMessage(updateCacheMessage);
                                sendMsg = Message.obtain();
                                msg.what = 1;
                                msg.obj = delayUpdateMsg;
                                UikitDataCache.this.mCacheHandler.sendMessageDelayed(sendMsg, (long) DataRefreshPeriodism.instance().getRefreshInterval(DataRefreshPeriodism.instance().getRefreshLevel(updateCacheMessage.getSourceId())));
                                return;
                            } else if (SystemClock.elapsedRealtime() - UikitDataCache.this.mHomeKeyEventTime > 600000) {
                                Log.d(UikitDataCache.TAG, "Active Update-not key event-don't update-" + updateCacheMessage.getSourceId());
                                delayUpdateMsg = new UpdateCacheMessage(updateCacheMessage);
                                sendMsg = Message.obtain();
                                msg.what = 1;
                                msg.obj = delayUpdateMsg;
                                UikitDataCache.this.mCacheHandler.sendMessageDelayed(sendMsg, delayUpdateMsg.getUpdateInterval());
                                return;
                            }
                        }
                        groupEvent = new UikitEvent();
                        groupEvent.eventType = 48;
                        groupEvent.pageNo = updateCacheMessage.pageNo;
                        groupEvent.uikitEngineId = updateCacheMessage.uikitEngineId;
                        groupEvent.sourceId = updateCacheMessage.sourceId;
                        UikitDataCache.this.onPostEvent(groupEvent);
                        Log.d(UikitDataCache.TAG, "Group Detail update- on post event-3-" + updateCacheMessage.sourceId);
                        return;
                    case 1:
                        List<UpdateCardInfo> list = (List) UikitDataCache.this.mUpdateCardInfoMap.get(Integer.valueOf(1));
                        if (list != null && list.size() > 0) {
                            for (UpdateCardInfo info : list) {
                                event = new UikitEvent();
                                event.eventType = 49;
                                event.pageNo = info.pageNo;
                                event.cardId = info.cardId;
                                event.uikitEngineId = info.uikitEngineId;
                                event.sourceId = info.sourceId;
                                UikitDataCache.this.onPostEvent(event);
                            }
                            return;
                        }
                        return;
                    case 2:
                        List<UpdateCardInfo> applist = (List) UikitDataCache.this.mUpdateCardInfoMap.get(Integer.valueOf(2));
                        if (applist != null && applist.size() > 0) {
                            for (UpdateCardInfo info2 : applist) {
                                event = new UikitEvent();
                                event.eventType = 50;
                                event.pageNo = info2.pageNo;
                                event.cardId = info2.cardId;
                                event.uikitEngineId = info2.uikitEngineId;
                                event.sourceId = info2.sourceId;
                                UikitDataCache.this.onPostEvent(event);
                            }
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if (msg.what == 2) {
                updateCacheMessage = (UpdateCacheMessage) msg.obj;
                Log.d(UikitDataCache.TAG, "Active Update-home state changed-" + updateCacheMessage.getSourceId());
                long updateTime = updateCacheMessage.getUpdateTime();
                long now = SystemClock.elapsedRealtime();
                Log.d(UikitDataCache.TAG, "Active Update-home state changed-update time-" + updateTime + "-now time-" + now);
                if (now - updateTime < ((long) DataRefreshPeriodism.instance().getRefreshInterval(DataRefreshPeriodism.instance().getRefreshLevel(updateCacheMessage.getSourceId())))) {
                    Log.d(UikitDataCache.TAG, "Active Update-home state changed-not update");
                    return;
                }
                groupEvent = new UikitEvent();
                groupEvent.eventType = 48;
                groupEvent.pageNo = updateCacheMessage.pageNo;
                groupEvent.uikitEngineId = updateCacheMessage.uikitEngineId;
                groupEvent.sourceId = updateCacheMessage.sourceId;
                UikitDataCache.this.onPostEvent(groupEvent);
                Log.d(UikitDataCache.TAG, "Active Update-home state changed-update-" + updateCacheMessage.getSourceId());
            }
        }
    }

    public static class UpdateCacheMessage {
        private int pageNo;
        private String sourceId;
        private int uikitEngineId;
        private long updateInterval;
        private long updateTime;
        private int updateType;

        public UpdateCacheMessage(int updateType, int pageNo, long interval, String sourceId, int uikitEngineId) {
            this.updateType = updateType;
            this.sourceId = sourceId;
            this.pageNo = pageNo;
            this.updateInterval = interval;
            this.uikitEngineId = uikitEngineId;
        }

        public UpdateCacheMessage(UpdateCacheMessage msg) {
            this.updateType = msg.getUpdateType();
            this.sourceId = msg.getSourceId();
            this.pageNo = msg.getPageNo();
            this.updateInterval = msg.getUpdateInterval();
            this.uikitEngineId = msg.getUikitEngineId();
            this.updateTime = msg.getUpdateTime();
        }

        public int getUpdateType() {
            return this.updateType;
        }

        public String getSourceId() {
            return this.sourceId;
        }

        public int getPageNo() {
            return this.pageNo;
        }

        public long getUpdateInterval() {
            return this.updateInterval;
        }

        public int getUikitEngineId() {
            return this.uikitEngineId;
        }

        public void setUpdateTime(long time) {
            this.updateTime = time;
        }

        public long getUpdateTime() {
            return this.updateTime;
        }
    }

    private class UpdateCardInfo {
        public String cardId;
        public int pageNo;
        public String sourceId;
        public int uikitEngineId;
        public int updateType;

        private UpdateCardInfo() {
        }
    }

    public class UpdateMessagerType {
        public static final int ACTIVE = 2;
        public static final int ADD = 1;
    }

    public class UpdateType {
        public static final int APP = 2;
        public static final int CHANNEL = 1;
        public static final int GROUP = 0;
    }

    public static UikitDataCache getInstance() {
        return sCache;
    }

    public UikitDataCache() {
        this.mCacheThread = null;
        this.mCacheHandler = null;
        this.mIsHomeActivityActive = true;
        this.mCurrentUikitEngineId = -1;
        this.mHomeKeyEventTime = 0;
        this.mUpdateCardInfoMap = new HashMap(5);
        this.mUikitCahce = new LinkedHashMap(10);
        this.mHomeSourceIdList = new ArrayList(10);
        this.mUikitEngineAndSourceList = new ArrayList(10);
        this.mCacheThread = new HandlerThread(TAG);
        this.mCacheThread.start();
        this.mCacheHandler = new CacheHandler(this.mCacheThread.getLooper());
        this.mFilePath = AppRuntimeEnv.get().getApplicationContext().getFilesDir() + "/";
    }

    public void register() {
        EventBus.getDefault().register(this);
    }

    public void addHomeSourceId(String id) {
        this.mHomeSourceIdList.add(id);
    }

    public void setHomeActivityState(boolean isActive) {
        Log.d(TAG, "Active Update-set home activity state- before-" + this.mIsHomeActivityActive + "-after- " + isActive);
        if (isActive) {
            this.mHomeKeyEventTime = SystemClock.elapsedRealtime();
        }
        if (!this.mIsHomeActivityActive && isActive) {
            synchronized (this.mUikitEngineAndSourceList) {
                for (UpdateCacheMessage msg : this.mUikitEngineAndSourceList) {
                    if (this.mHomeSourceIdList.contains(msg.getSourceId())) {
                        UpdateCacheMessage updateMsg = new UpdateCacheMessage(msg);
                        Message message = Message.obtain();
                        message.what = 2;
                        message.obj = updateMsg;
                        this.mCacheHandler.sendMessage(message);
                    }
                }
            }
        }
        this.mIsHomeActivityActive = isActive;
    }

    public void notifyHomeKeyEvent() {
        this.mHomeKeyEventTime = SystemClock.elapsedRealtime();
    }

    public void onPostEvent(UikitEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onGetEvent(UikitEvent event) {
    }

    public List<CardInfoModel> read(int cacheType, String sourceId, int pageNo, int unkitengineId, boolean isReadDisk) {
        UikitResourceData resourceData;
        UikitCardListData cardListData;
        if (isReadDisk) {
            List<CardInfoModel> list = readDisk(cacheType, sourceId);
            if (list == null || list.size() == 0) {
                resourceData = (UikitResourceData) this.mUikitCahce.get(sourceId);
                if (resourceData != null) {
                    cardListData = resourceData.getPageCache(pageNo);
                    if (cardListData != null) {
                        return cardListData.getCardList();
                    }
                }
                return null;
            }
            resourceData = (UikitResourceData) this.mUikitCahce.get(sourceId);
            UikitCardListData cardlistData;
            if (resourceData == null) {
                resourceData = new UikitResourceData();
                cardlistData = new UikitCardListData();
                cardlistData.setCardList(list);
                resourceData.addPageCache(pageNo, cardlistData);
                this.mUikitCahce.put(sourceId, resourceData);
                return list;
            }
            cardlistData = resourceData.getPageCache(pageNo);
            if (cardlistData == null) {
                cardlistData = new UikitCardListData();
            }
            cardlistData.setCardList(list);
            resourceData.addPageCache(pageNo, cardlistData);
            this.mUikitCahce.put(sourceId, resourceData);
            return list;
        }
        resourceData = (UikitResourceData) this.mUikitCahce.get(sourceId);
        if (resourceData == null) {
            return null;
        }
        cardListData = resourceData.getPageCache(pageNo);
        if (cardListData == null) {
            return null;
        }
        return cardListData.getCardList();
    }

    public CardInfoModel read(int cacheType, String sourceId, int pageNo, String cardId, int unkitengineId) {
        UikitResourceData resourceData = (UikitResourceData) this.mUikitCahce.get(sourceId);
        if (resourceData == null) {
            return null;
        }
        UikitCardListData cardListData = resourceData.getPageCache(pageNo);
        if (cardListData != null) {
            return cardListData.getCard(cardId);
        }
        return null;
    }

    public void write(int cacheType, String sourceId, int pageNo, int unkitengineId, List<CardInfoModel> list) {
        UikitResourceData resourceData = (UikitResourceData) this.mUikitCahce.get(sourceId);
        UikitCardListData cardListData;
        if (resourceData != null) {
            cardListData = resourceData.getPageCache(pageNo);
            if (cardListData != null) {
                cardListData.setCardList(list);
                return;
            }
            cardListData = new UikitCardListData();
            cardListData.setCardList(list);
            resourceData.addPageCache(pageNo, cardListData);
            return;
        }
        resourceData = new UikitResourceData();
        cardListData = new UikitCardListData();
        cardListData.setCardList(list);
        resourceData.addPageCache(pageNo, cardListData);
        this.mUikitCahce.put(sourceId, resourceData);
    }

    public boolean update(int cacheType, String sourceId, int pageNo, String cardId, int unkitengineId, CardInfoModel model) {
        UikitResourceData resourceData = (UikitResourceData) this.mUikitCahce.get(sourceId);
        UikitCardListData cardListData;
        if (resourceData == null) {
            resourceData = new UikitResourceData();
            cardListData = new UikitCardListData();
            cardListData.addCard(model);
            resourceData.addPageCache(pageNo, cardListData);
            this.mUikitCahce.put(sourceId, resourceData);
        } else {
            cardListData = resourceData.getPageCache(pageNo);
            if (cardListData == null) {
                cardListData = new UikitCardListData();
                cardListData.updateCard(cardId, model);
                resourceData.addPageCache(pageNo, cardListData);
            } else {
                cardListData.updateItems(cardId, model.getItemInfoModels());
            }
        }
        return true;
    }

    public void remove(int cacheType, String sourceId, int fromPageNo, int unkitengineId) {
        UikitResourceData resourceData = (UikitResourceData) this.mUikitCahce.get(sourceId);
        if (resourceData != null) {
            int index = fromPageNo;
            UikitCardListData cardListData = resourceData.getPageCache(index);
            while (cardListData != null) {
                resourceData.removePage(index);
                index++;
                cardListData = resourceData.getPageCache(index);
            }
        }
    }

    public List<CardInfoModel> readDisk(int cacheType, String sourceId) {
        try {
            List<CardInfoModel> list = (List) SerializableUtils.read("home/home_cache/uikit_" + sourceId + ".dem");
            Log.d(TAG, "read disk cost = " + (System.currentTimeMillis() - System.currentTimeMillis()));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CardInfoModel> readDisk(int cacheType, String sourceId, int pageNo, int engineid, boolean isVip) {
        List<CardInfoModel> readPageInfoFromLocalCache;
        synchronized (sourceId) {
            readPageInfoFromLocalCache = readPageInfoFromLocalCache(sourceId, engineid, isVip);
        }
        return readPageInfoFromLocalCache;
    }

    public List<CardInfoModel> readPageInfoFromLocalCache(String id, int engineid, boolean isVip) {
        IOException e;
        Throwable th;
        Exception e1;
        RandomAccessFile rFile = null;
        FileChannel fc = null;
        List<CardInfoModel> cardInfoModelList = null;
        String filePath = this.mFilePath + "home/uikit_cache_" + id + ".dem";
        try {
            long startTime = System.currentTimeMillis();
            LogUtils.d(TAG, "[start performance] read page data to cache start, id=" + id);
            RandomAccessFile randomAccessFile = new RandomAccessFile(new File(filePath), "r");
            try {
                fc = randomAccessFile.getChannel();
                byte[] buffer = new byte[((int) randomAccessFile.length())];
                fc.map(MapMode.READ_ONLY, 0, randomAccessFile.length()).get(buffer);
                LogUtils.d(TAG, "read page data from cache finished,id=" + id + ",consumed = " + (System.currentTimeMillis() - startTime) + " ms");
                startTime = System.currentTimeMillis();
                cardInfoModelList = JSON.parseArray(new String(buffer), CardInfoModel.class);
                LogUtils.d(TAG, "[start performance] read and parse page data from cache finished,id=" + id + ",consumed = " + (System.currentTimeMillis() - startTime) + " ms");
                if (fc != null) {
                    try {
                        fc.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        rFile = randomAccessFile;
                    }
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                rFile = randomAccessFile;
            } catch (IOException e3) {
                e2 = e3;
                rFile = randomAccessFile;
                try {
                    LogUtils.e(TAG, "read page info from local cache exception : " + e2);
                    if (fc != null) {
                        try {
                            fc.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    if (rFile != null) {
                        rFile.close();
                    }
                    return cardInfoModelList;
                } catch (Throwable th2) {
                    th = th2;
                    if (fc != null) {
                        try {
                            fc.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                            throw th;
                        }
                    }
                    if (rFile != null) {
                        rFile.close();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                e1 = e4;
                rFile = randomAccessFile;
                LogUtils.e(TAG, "read page info from local cache exception : " + e1);
                if (fc != null) {
                    try {
                        fc.close();
                    } catch (IOException e2222) {
                        e2222.printStackTrace();
                    }
                }
                if (rFile != null) {
                    rFile.close();
                }
                return cardInfoModelList;
            } catch (Throwable th3) {
                th = th3;
                rFile = randomAccessFile;
                if (fc != null) {
                    fc.close();
                }
                if (rFile != null) {
                    rFile.close();
                }
                throw th;
            }
        } catch (IOException e5) {
            e2222 = e5;
            LogUtils.e(TAG, "read page info from local cache exception : " + e2222);
            if (fc != null) {
                fc.close();
            }
            if (rFile != null) {
                rFile.close();
            }
            return cardInfoModelList;
        } catch (Exception e6) {
            e1 = e6;
            LogUtils.e(TAG, "read page info from local cache exception : " + e1);
            if (fc != null) {
                fc.close();
            }
            if (rFile != null) {
                rFile.close();
            }
            return cardInfoModelList;
        }
        return cardInfoModelList;
    }

    public ApiResultGroupDetail parse(byte[] raw) {
        ApiResultGroupDetail result = (ApiResultGroupDetail) JSON.parseObject(raw, (Type) ApiResultGroupDetail.class, new Feature[0]);
        LogUtils.d(TAG, "parse bytes time = " + (System.currentTimeMillis() - System.currentTimeMillis()));
        return result;
    }

    public void writePageInfoToLocalCache(List<CardInfoModel> list, String raw, String id) {
        IOException e;
        Throwable th;
        Exception e1;
        synchronized (id) {
            String result = JSON.toJSONString(list);
            RandomAccessFile rFile = null;
            FileChannel mFc = null;
            try {
                String path = this.mFilePath + "home/uikit_cache_" + id + ".dem";
                LogUtils.d(TAG, "write page data to cache start,id=" + id + ", path = " + path);
                RandomAccessFile rFile2 = new RandomAccessFile(new File(path), "rws");
                try {
                    mFc = rFile2.getChannel();
                    rFile2.setLength((long) result.getBytes().length);
                    mFc.map(MapMode.READ_WRITE, 0, (long) result.getBytes().length).put(result.getBytes());
                    if (mFc != null) {
                        try {
                            mFc.close();
                        } catch (IOException e2) {
                        }
                    }
                    if (rFile2 != null) {
                        rFile2.close();
                    }
                    LogUtils.d(TAG, "write page data to cache finished, id=" + id);
                    rFile = rFile2;
                } catch (IOException e3) {
                    e = e3;
                    rFile = rFile2;
                    try {
                        e.printStackTrace();
                        if (mFc != null) {
                            try {
                                mFc.close();
                            } catch (IOException e4) {
                                LogUtils.d(TAG, "write page data to cache finished, id=" + id);
                            }
                        }
                        if (rFile != null) {
                            rFile.close();
                        }
                        LogUtils.d(TAG, "write page data to cache finished, id=" + id);
                    } catch (Throwable th2) {
                        th = th2;
                        if (mFc != null) {
                            try {
                                mFc.close();
                            } catch (IOException e5) {
                                LogUtils.d(TAG, "write page data to cache finished, id=" + id);
                                throw th;
                            }
                        }
                        if (rFile != null) {
                            rFile.close();
                        }
                        LogUtils.d(TAG, "write page data to cache finished, id=" + id);
                        throw th;
                    }
                } catch (Exception e6) {
                    e1 = e6;
                    rFile = rFile2;
                    e1.printStackTrace();
                    if (mFc != null) {
                        try {
                            mFc.close();
                        } catch (IOException e7) {
                            LogUtils.d(TAG, "write page data to cache finished, id=" + id);
                        }
                    }
                    if (rFile != null) {
                        rFile.close();
                    }
                    LogUtils.d(TAG, "write page data to cache finished, id=" + id);
                } catch (Throwable th3) {
                    th = th3;
                    rFile = rFile2;
                    if (mFc != null) {
                        mFc.close();
                    }
                    if (rFile != null) {
                        rFile.close();
                    }
                    LogUtils.d(TAG, "write page data to cache finished, id=" + id);
                    throw th;
                }
            } catch (IOException e8) {
                e = e8;
                e.printStackTrace();
                if (mFc != null) {
                    mFc.close();
                }
                if (rFile != null) {
                    rFile.close();
                }
                LogUtils.d(TAG, "write page data to cache finished, id=" + id);
            } catch (Exception e9) {
                e1 = e9;
                e1.printStackTrace();
                if (mFc != null) {
                    mFc.close();
                }
                if (rFile != null) {
                    rFile.close();
                }
                LogUtils.d(TAG, "write page data to cache finished, id=" + id);
            }
        }
    }

    public void writeDisk(List<CardInfoModel> list, int cacheType, String sourceId) {
        try {
            SerializableUtils.write(list, "home/home_cache/uikit_" + sourceId + ".dem");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDisk(List<CardInfoModel> list, String raw, int cacheType, String sourceId) {
        writePageInfoToLocalCache(list, raw, sourceId);
    }

    public void rewriteDisk(int cacheType, String sourceId, int UikitEngineId) {
        List<CardInfoModel> list = read(cacheType, sourceId, 1, UikitEngineId, false);
        if (list != null && list.size() > 0) {
            writeDisk(list, cacheType, sourceId);
        }
    }

    public void removeDisk(String sourceId) {
        File file = new File(UikitDataCacheConstants.PATH + "uikit_" + sourceId + ".dem");
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public void setCurrentUikitEngineId(int uikitEngineId) {
        this.mCurrentUikitEngineId = uikitEngineId;
    }

    public int getCurrentUikitEngineId() {
        return this.mCurrentUikitEngineId;
    }

    public int getCardsCount(int cacheType, String sourceId) {
        return ((UikitResourceData) this.mUikitCahce.get(sourceId)).cardsCount();
    }

    public void postAddCheckUpdateMessage(UpdateCacheMessage updateMessage) {
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = updateMessage;
        if (updateMessage.getUpdateInterval() == 0) {
            this.mCacheHandler.sendMessage(msg);
        } else {
            this.mCacheHandler.sendMessageDelayed(msg, updateMessage.getUpdateInterval());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addUpdateCardInfo(int r7, int r8, java.lang.String r9, java.lang.String r10, int r11) {
        /*
        r6 = this;
        r0 = new com.gala.video.lib.share.uikit.cache.UikitDataCache$UpdateCardInfo;
        r3 = 0;
        r0.<init>();
        r0.updateType = r7;
        r0.pageNo = r8;
        r0.cardId = r9;
        r0.sourceId = r10;
        r0.uikitEngineId = r11;
        r4 = r6.mUpdateCardInfoMap;
        monitor-enter(r4);
        r3 = r6.mUpdateCardInfoMap;	 Catch:{ all -> 0x0048 }
        r5 = java.lang.Integer.valueOf(r7);	 Catch:{ all -> 0x0048 }
        r2 = r3.get(r5);	 Catch:{ all -> 0x0048 }
        r2 = (java.util.List) r2;	 Catch:{ all -> 0x0048 }
        if (r2 != 0) goto L_0x0035;
    L_0x0021:
        r2 = new java.util.ArrayList;	 Catch:{ all -> 0x0048 }
        r3 = 1;
        r2.<init>(r3);	 Catch:{ all -> 0x0048 }
        r2.add(r0);	 Catch:{ all -> 0x0048 }
        r3 = r6.mUpdateCardInfoMap;	 Catch:{ all -> 0x0048 }
        r5 = java.lang.Integer.valueOf(r7);	 Catch:{ all -> 0x0048 }
        r3.put(r5, r2);	 Catch:{ all -> 0x0048 }
    L_0x0033:
        monitor-exit(r4);	 Catch:{ all -> 0x0048 }
    L_0x0034:
        return;
    L_0x0035:
        r3 = r2.size();	 Catch:{ all -> 0x0048 }
        if (r3 != 0) goto L_0x004b;
    L_0x003b:
        r2.add(r0);	 Catch:{ all -> 0x0048 }
        r3 = r6.mUpdateCardInfoMap;	 Catch:{ all -> 0x0048 }
        r5 = java.lang.Integer.valueOf(r7);	 Catch:{ all -> 0x0048 }
        r3.put(r5, r2);	 Catch:{ all -> 0x0048 }
        goto L_0x0033;
    L_0x0048:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0048 }
        throw r3;
    L_0x004b:
        r3 = r2.iterator();	 Catch:{ all -> 0x0048 }
    L_0x004f:
        r5 = r3.hasNext();	 Catch:{ all -> 0x0048 }
        if (r5 == 0) goto L_0x006d;
    L_0x0055:
        r1 = r3.next();	 Catch:{ all -> 0x0048 }
        r1 = (com.gala.video.lib.share.uikit.cache.UikitDataCache.UpdateCardInfo) r1;	 Catch:{ all -> 0x0048 }
        r5 = r1.sourceId;	 Catch:{ all -> 0x0048 }
        r5 = r5.equals(r10);	 Catch:{ all -> 0x0048 }
        if (r5 == 0) goto L_0x004f;
    L_0x0063:
        r5 = r1.cardId;	 Catch:{ all -> 0x0048 }
        r5 = r5.equals(r9);	 Catch:{ all -> 0x0048 }
        if (r5 == 0) goto L_0x004f;
    L_0x006b:
        monitor-exit(r4);	 Catch:{ all -> 0x0048 }
        goto L_0x0034;
    L_0x006d:
        r2.add(r0);	 Catch:{ all -> 0x0048 }
        r3 = r6.mUpdateCardInfoMap;	 Catch:{ all -> 0x0048 }
        r5 = java.lang.Integer.valueOf(r7);	 Catch:{ all -> 0x0048 }
        r3.put(r5, r2);	 Catch:{ all -> 0x0048 }
        goto L_0x0033;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.lib.share.uikit.cache.UikitDataCache.addUpdateCardInfo(int, int, java.lang.String, java.lang.String, int):void");
    }

    public void removeUpdateCardInfoList(int updateType, int pageNo, String sourceId, int uikitEngineId) {
        synchronized (this.mUpdateCardInfoMap) {
            List<UpdateCardInfo> list = (List) this.mUpdateCardInfoMap.get(Integer.valueOf(updateType));
            if (list == null) {
            } else if (list.size() == 0) {
            } else {
                List<UpdateCardInfo> deleteList = new ArrayList(1);
                for (UpdateCardInfo info : list) {
                    if (info.sourceId.equals(sourceId) && info.pageNo >= pageNo) {
                        deleteList.add(info);
                    }
                }
                for (UpdateCardInfo deleteCard : deleteList) {
                    list.remove(deleteCard);
                }
            }
        }
    }

    public void setLock(String sourceId, int pageNo, boolean lock) {
        UikitResourceData resourceData = (UikitResourceData) this.mUikitCahce.get(sourceId);
        if (resourceData != null) {
            UikitCardListData cardListData = resourceData.getPageCache(pageNo);
            if (cardListData != null && cardListData.isLock() != lock) {
                cardListData.setLock(lock);
            }
        }
    }

    public boolean getLock(String sourceId, int pageNo) {
        UikitResourceData resourceData = (UikitResourceData) this.mUikitCahce.get(sourceId);
        if (resourceData != null) {
            UikitCardListData cardListData = resourceData.getPageCache(pageNo);
            if (cardListData != null) {
                return cardListData.isLock();
            }
        }
        return false;
    }

    public boolean isPageCached(String sourceId) {
        File file = new File(UikitDataCacheConstants.PATH + "uikit_" + sourceId + ".dem");
        LogUtils.d(TAG, "isPageCached path = " + file.getAbsolutePath());
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public void addUikitEngineAndSourceId(int uikitEngineId, String sourceId) {
        synchronized (this.mUikitEngineAndSourceList) {
            this.mUikitEngineAndSourceList.add(new UpdateCacheMessage(0, 1, 0, sourceId, uikitEngineId));
        }
    }

    public void setUpdateTime(String sourceId, long time) {
        synchronized (this.mUikitEngineAndSourceList) {
            for (UpdateCacheMessage msg : this.mUikitEngineAndSourceList) {
                if (msg.getSourceId().equals(sourceId)) {
                    msg.setUpdateTime(time);
                }
            }
        }
    }
}
