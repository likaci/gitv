package com.gala.video.lib.share.ifimpl.ucenter.history.impl;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.tvapi.vrs.result.ApiResultHistoryList;
import com.gala.tvapi.vrs.result.ApiResultHistoryListForUser;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.cache.MemoryCache;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifimpl.ucenter.history.cache.HistoryDbCache;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo.Builder;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.IHistoryResultCallBack;
import com.gala.video.lib.share.utils.DataUtils;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class HistoryDataBuilder extends HistoryBase {
    private int mCallbackId = 0;
    private Map<Integer, IHistoryResultCallBack> mCallbackMap = new Hashtable(1);
    private HistoryDbCache mHistoryDbCache;
    private MemoryCache<HistoryInfo> mLoginMemoryCache = new MemoryCache(200);
    private MemoryCache<HistoryInfo> mNoLoginMemoryCache = new MemoryCache(200);

    private class DeleteListener implements IVrsCallback<ApiResultCode> {
        private DeleteListener() {
        }

        public void onSuccess(ApiResultCode result) {
            LogUtils.e(HistoryInfoHelper.TAG, "---deleteCallback---onSuccess--");
        }

        public void onException(ApiException e) {
            LogUtils.e(HistoryInfoHelper.TAG, "---deleteCallback---onException()--code=" + (e != null ? e.getCode() : ""));
        }
    }

    public static class HistoryCloudRequest {
        private int mPageIndex = 1;
        private int mPageSize = 60;
        private int mType = 1;

        public HistoryCloudRequest(int pageIndex, int PageSize, int type) {
            this.mPageIndex = pageIndex;
            this.mPageSize = PageSize;
            this.mType = type;
        }

        public int getPageIndex() {
            return this.mPageIndex;
        }

        public int getPageSize() {
            return this.mPageSize;
        }

        public int getType() {
            return this.mType;
        }

        public String toString() {
            return "page index=" + String.valueOf(this.mPageIndex) + " page size=" + this.mPageSize + " type=" + this.mType;
        }
    }

    class HistoryDataHandler extends HistoryHandler {
        HistoryDataHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            LogUtils.d(HistoryInfoHelper.TAG, "handleMessage(" + msg + ")");
            IHistoryResultCallBack callBack = HistoryDataBuilder.this.getHistoryResultCallback(msg.arg1);
            switch (msg.what) {
                case 1:
                    HistoryDataBuilder.this.handleMergeDeviceAndCloudHistoryMsg();
                    return;
                case 2:
                    HistoryDataBuilder.this.HandleUploadHistory((HistoryInfo) msg.obj);
                    return;
                case 3:
                    HistoryDataBuilder.this.handleClearAllHistory();
                    return;
                case 4:
                    HistoryDataBuilder.this.handleLoadHistoryList((HistoryCloudRequest) msg.obj, callBack);
                    GetInterfaceTools.getDataBus().postEvent(IDataBus.HISTORY_DB_RELOAD_COMPLETED);
                    HistoryDataBuilder.this.mCallbackMap.remove(Integer.valueOf(msg.arg1));
                    return;
                case 5:
                    HistoryDataBuilder.this.handleClearHistoryForUser();
                    return;
                case 6:
                    HistoryDataBuilder.this.handleLoadHistoryListFromCloud();
                    GetInterfaceTools.getDataBus().postEvent(IDataBus.HISTORY_CLOUD_SYNC_COMPLETED);
                    return;
                case 7:
                    String[] params = (String[]) msg.obj;
                    HistoryDataBuilder.this.handleDeleteHistory(params[0], params[1]);
                    return;
                case 8:
                    HistoryDataBuilder.this.synchronizeHistoryListForNoLogin();
                    GetInterfaceTools.getDataBus().postEvent(IDataBus.HISTORY_DB_RELOAD_COMPLETED);
                    return;
                default:
                    return;
            }
        }
    }

    private static class HistoryResults {
        private List<Album> mResults;
        private int mTotalSize = 0;

        public HistoryResults(List<Album> results, int totalSize) {
            this.mResults = results;
            this.mTotalSize = totalSize;
        }

        public List<Album> getResults() {
            return this.mResults;
        }

        public int getTotalSize() {
            return this.mTotalSize;
        }
    }

    private class Holder {
        private final String mCookie;
        private final List<HistoryInfo> mList = new ArrayList();

        public Holder(String cookie) {
            this.mCookie = cookie;
        }

        public List<HistoryInfo> getResult() {
            return this.mList;
        }

        public void addUniqueAll(List<Album> list) {
            if (ListUtils.isEmpty((List) list)) {
                LogUtils.w(HistoryInfoHelper.TAG, "addUniqueAll() empty list!");
                return;
            }
            for (Album album : list) {
                if (!(TextUtils.isEmpty(album.sourceCode) || "0".equals(album.sourceCode))) {
                    album.qpId = album.sourceCode;
                }
                if (contains(album.qpId) || TextUtils.isEmpty(album.qpId) || TextUtils.isEmpty(album.tvQid)) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(HistoryInfoHelper.TAG, "addUniqueAll() remove " + DataUtils.albumInfoToString(album));
                    }
                } else if (album.chnId != 10) {
                    long addTime = StringUtils.parse(album.addTime, -1);
                    this.mList.add(new Builder(this.mCookie).album(album).addedTime(addTime).uploadTime(addTime).build());
                }
            }
        }

        private boolean contains(String qpId) {
            for (HistoryInfo one : this.mList) {
                if (equalStr(qpId, one.getQpId())) {
                    return true;
                }
            }
            return false;
        }

        private boolean equalStr(String str1, String str2) {
            if (str1 == null && str2 == null) {
                return true;
            }
            if (str1 == null || !str1.equals(str2)) {
                return false;
            }
            return true;
        }
    }

    private class LoadAnonymityListener extends Holder implements IVrsCallback<ApiResultHistoryList> {
        private boolean mIsSuccess = false;

        public LoadAnonymityListener(String cookie) {
            super(cookie);
        }

        public void onSuccess(ApiResultHistoryList list) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(HistoryInfoHelper.TAG, "AnonyousListener.onSuccess(" + list + ")");
            }
            this.mIsSuccess = true;
            if (list != null) {
                addUniqueAll(list.getAlbumList());
            }
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(HistoryInfoHelper.TAG, "AnonyousListener.onException() " + exception.getCode() + ", " + exception.getHttpCode());
            }
        }

        public boolean isSuccess() {
            return this.mIsSuccess;
        }
    }

    private class LoadUserListener extends Holder implements IVrsCallback<ApiResultHistoryListForUser> {
        private boolean mIsSuccess = false;
        private int mTotal = 0;

        public LoadUserListener(String cookie) {
            super(cookie);
        }

        public void onSuccess(ApiResultHistoryListForUser list) {
            this.mIsSuccess = true;
            if (!(list == null || list.data == null)) {
                this.mTotal = list.data.total;
                LogUtils.d(HistoryInfoHelper.TAG, "UserListener.onSuccess total size(" + this.mTotal + ")");
            }
            LogUtils.d(HistoryInfoHelper.TAG, "UserListener.onSuccess(" + list + ")");
            if (list != null) {
                addUniqueAll(list.getAlbumList());
            }
            CreateInterfaceTools.createEpgPingback().onLoadUser(true);
        }

        public void onException(ApiException exception) {
            if (exception != null && LogUtils.mIsDebug) {
                LogUtils.d(HistoryInfoHelper.TAG, "UserListener.onException() " + exception.getCode() + ", " + exception.getHttpCode());
            }
            CreateInterfaceTools.createEpgPingback().onLoadUser(false);
        }

        public boolean isSuccess() {
            return this.mIsSuccess;
        }

        public int getTotalSize() {
            return this.mTotal < 0 ? 0 : this.mTotal;
        }
    }

    private class SaveTvHistoryListener implements IVrsCallback<ApiResultCode> {
        private SaveTvHistoryListener() {
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(HistoryInfoHelper.TAG, "SaveTvHistoryListener.onException() " + exception.getCode() + ", " + exception.getHttpCode());
            }
            CreateInterfaceTools.createEpgPingback().onSaveTvHistory(false);
        }

        public void onSuccess(ApiResultCode result) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(HistoryInfoHelper.TAG, "SaveTvHistoryListener.onSuccess(" + result + ")");
            }
            CreateInterfaceTools.createEpgPingback().onSaveTvHistory(true);
        }
    }

    public /* bridge */ /* synthetic */ Message obtainMessage(int i, Object obj) {
        return super.obtainMessage(i, obj);
    }

    public /* bridge */ /* synthetic */ void removeCallbacksAndMessages(Object obj) {
        super.removeCallbacksAndMessages(obj);
    }

    public /* bridge */ /* synthetic */ void removeMessages(int i) {
        super.removeMessages(i);
    }

    public /* bridge */ /* synthetic */ void sendEmptyMessage(int i) {
        super.sendEmptyMessage(i);
    }

    public /* bridge */ /* synthetic */ void sendEmptyMessageDelayed(int i, int i2) {
        super.sendEmptyMessageDelayed(i, i2);
    }

    public /* bridge */ /* synthetic */ void sendMessage(Message message) {
        super.sendMessage(message);
    }

    public /* bridge */ /* synthetic */ void sendMessageDelayed(Message message, int i) {
        super.sendMessageDelayed(message, i);
    }

    public HistoryDataBuilder() {
        HandlerThread handlerThread = new HandlerThread("history-thread");
        handlerThread.start();
        this.mHistoryHandler = new HistoryDataHandler(handlerThread.getLooper());
        this.mHistoryDbCache = new HistoryDbCache(300);
    }

    public int addHistoryResultCallback(IHistoryResultCallBack callback) {
        if (this.mCallbackId == Integer.MAX_VALUE) {
            this.mCallbackId = 0;
        } else {
            this.mCallbackId++;
        }
        this.mCallbackMap.put(Integer.valueOf(this.mCallbackId), callback);
        return this.mCallbackId;
    }

    private IHistoryResultCallBack getHistoryResultCallback(int id) {
        IHistoryResultCallBack iHistoryResultCallBack;
        synchronized (this.mCallbackMap) {
            iHistoryResultCallBack = (IHistoryResultCallBack) this.mCallbackMap.get(Integer.valueOf(id));
        }
        return iHistoryResultCallBack;
    }

    public List<HistoryInfo> getHistoryLongVideoMsg(int count) {
        List<HistoryInfo> list = new ArrayList(count);
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext())) {
            List<HistoryInfo> loginMemoryCache = this.mLoginMemoryCache.obtainAll();
            if (loginMemoryCache == null || loginMemoryCache.size() == 0) {
                return null;
            }
            for (HistoryInfo info : loginMemoryCache) {
                if (HistoryInfoHelper.isLongVideo(info)) {
                    list.add(info);
                }
            }
        } else {
            List<HistoryInfo> noLoginMemoryCache = this.mNoLoginMemoryCache.obtainAll();
            if (noLoginMemoryCache == null || noLoginMemoryCache.size() == 0) {
                return null;
            }
            for (HistoryInfo info2 : noLoginMemoryCache) {
                if (HistoryInfoHelper.isLongVideo(info2)) {
                    list.add(info2);
                }
            }
        }
        if (list.size() == 0) {
            return list;
        }
        List<HistoryInfo> temp = HistoryInfoHelper.sort(list);
        for (HistoryInfo info22 : temp) {
            LogUtils.d(HistoryInfoHelper.TAG, "getHistoryLongVideoMsg info = " + info22.toString());
        }
        if (temp.size() > count) {
            return temp.subList(0, count);
        }
        return temp;
    }

    public List<HistoryInfo> getHistoryVideoMsg(int count) {
        List<HistoryInfo> list = new ArrayList(count);
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext())) {
            List<HistoryInfo> loginMemoryCache = this.mLoginMemoryCache.obtainAll();
            if (loginMemoryCache == null || loginMemoryCache.size() == 0) {
                return null;
            }
            for (HistoryInfo info : loginMemoryCache) {
                if (HistoryInfoHelper.isLongVideo(info)) {
                    list.add(info);
                }
            }
            if (list.size() == 0) {
                list = loginMemoryCache;
            }
        } else {
            List<HistoryInfo> noLoginMemoryCache = this.mNoLoginMemoryCache.obtainAll();
            if (noLoginMemoryCache == null || noLoginMemoryCache.size() == 0) {
                return null;
            }
            for (HistoryInfo info2 : noLoginMemoryCache) {
                if (HistoryInfoHelper.isLongVideo(info2)) {
                    list.add(info2);
                }
            }
            if (list.size() == 0) {
                list = noLoginMemoryCache;
            }
        }
        if (list.size() == 0) {
            return list;
        }
        List<HistoryInfo> temp = HistoryInfoHelper.sort(list);
        for (HistoryInfo info22 : temp) {
            LogUtils.d(HistoryInfoHelper.TAG, "getHistoryVideoMsg info = " + info22.toString());
        }
        if (temp.size() > count) {
            return temp.subList(0, count);
        }
        return temp;
    }

    private void handleLoadHistoryList(HistoryCloudRequest request, IHistoryResultCallBack callback) {
        if (callback != null) {
            List<HistoryInfo> list = this.mHistoryDbCache.reload(AppClientUtils.getCookie(AppRuntimeEnv.get().getApplicationContext()));
            LogUtils.d(HistoryInfoHelper.TAG, "total album cache size: " + list.size());
            if (list.size() == 0 && request.getPageIndex() == 1) {
                handleLoadHistoryListFromCloud();
                list = this.mHistoryDbCache.reload(AppClientUtils.getCookie(AppRuntimeEnv.get().getApplicationContext()));
            }
            if (list != null && list.size() > 0) {
                list = HistoryInfoHelper.sort(list);
                this.mHistoryDbCache.update(list);
            }
            if (GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext())) {
                this.mLoginMemoryCache.clear();
                if (list != null && list.size() > 0) {
                    for (HistoryInfo info : list) {
                        this.mLoginMemoryCache.put(getAlbumKey(info.getQpId()), info);
                    }
                }
            } else {
                this.mNoLoginMemoryCache.clear();
                if (list != null && list.size() > 0) {
                    for (HistoryInfo info2 : list) {
                        this.mNoLoginMemoryCache.put(getAlbumKey(info2.getQpId()), info2);
                    }
                }
            }
            HistoryResults result = getHistoryList(request, list);
            callback.onSuccess(result.getResults(), result.getTotalSize());
        }
    }

    private void handleLoadHistoryListFromCloud() {
        boolean hasLogIn = GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext());
        LogUtils.d(HistoryInfoHelper.TAG, "reload cloud play record is login? " + hasLogIn);
        List<HistoryInfo> list = new ArrayList();
        if (hasLogIn) {
            String cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
            LoadUserListener listener = new LoadUserListener(cookie);
            for (int i = 0; i < 2; i++) {
                UserHelper.historyListForUser.callSync(listener, cookie, String.valueOf(i + 1), "100", "", "0");
                if ((i + 1) * 100 >= listener.getTotalSize() || !listener.isSuccess()) {
                    break;
                }
            }
            this.mHistoryDbCache.update(listener.getResult());
            this.mLoginMemoryCache.clear();
            list = this.mHistoryDbCache.reload(cookie);
            if (LogUtils.mIsDebug) {
                LogUtils.d(HistoryInfoHelper.TAG, "reload login user cloud end. " + list.size() + "request count=" + 2);
            }
            if (list.size() > 0) {
                for (HistoryInfo info : list) {
                    this.mLoginMemoryCache.put(getAlbumKey(info.getQpId()), info);
                }
            }
        }
        this.mHistoryHandler.sendEmptyMessageDelayed(101, 3600000);
    }

    private void synchronizeHistoryListForNoLogin() {
        if (!GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext())) {
            List<HistoryInfo> list = HistoryInfoHelper.sort(this.mHistoryDbCache.reload(AppClientUtils.getCookie(AppRuntimeEnv.get().getApplicationContext())));
            this.mHistoryDbCache.update(list);
            this.mNoLoginMemoryCache.clear();
            if (list != null && list.size() > 0) {
                for (HistoryInfo info : list) {
                    this.mNoLoginMemoryCache.put(getAlbumKey(info.getQpId()), info);
                }
            }
        }
    }

    private HistoryResults getHistoryList(HistoryCloudRequest param, List<HistoryInfo> totalRecords) {
        int pageIndex = param.getPageIndex();
        int pageSize = param.getPageSize();
        int type = param.getType();
        int totalSize = 0;
        List<Album> albums = new ArrayList();
        List<Album> results = new ArrayList();
        for (HistoryInfo item : totalRecords) {
            albums.add(item.getAlbum());
            if (LogUtils.mIsDebug) {
                LogUtils.d(HistoryInfoHelper.TAG, "getHistoryList return (" + item + ")");
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(HistoryInfoHelper.TAG, "play history request parameters(" + param.toString() + ")");
        }
        if (pageSize > 0 && pageIndex - 1 >= 0) {
            int startPos = (pageIndex - 1) * pageSize;
            int endPos = pageIndex * pageSize;
            if (type == 1) {
                List<Album> longResults = new ArrayList();
                for (Album item2 : albums) {
                    if (HistoryInfoHelper.isLongVideo(item2)) {
                        longResults.add(item2);
                    }
                }
                totalSize = longResults.size();
                if (startPos < longResults.size()) {
                    if (endPos >= longResults.size()) {
                        endPos = longResults.size();
                    }
                    results.addAll(longResults.subList(startPos, endPos));
                }
            } else {
                totalSize = albums.size();
                if (startPos < albums.size()) {
                    if (endPos >= albums.size()) {
                        endPos = albums.size();
                    }
                    results.addAll(albums.subList(startPos, endPos));
                }
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(HistoryInfoHelper.TAG, "getHistoryList size(" + albums.size() + ")");
        }
        return new HistoryResults(results, totalSize);
    }

    private void handleMergeDeviceAndCloudHistoryMsg() {
        List<HistoryInfo> temp = new ArrayList();
        for (HistoryInfo item : this.mHistoryDbCache.reload(AppRuntimeEnv.get().getDefaultUserId())) {
            LogUtils.d(HistoryInfoHelper.TAG, "cookie = " + AppClientUtils.getCookie(AppRuntimeEnv.get().getApplicationContext()));
            item.changeToUserCookie(AppClientUtils.getCookie(AppRuntimeEnv.get().getApplicationContext()));
            temp.add(item);
        }
        LogUtils.d(HistoryInfoHelper.TAG, "local history size = " + temp.size());
        List<HistoryInfo> list = this.mHistoryDbCache.reload(AppClientUtils.getCookie(AppRuntimeEnv.get().getApplicationContext()));
        if (list != null) {
            LogUtils.d(HistoryInfoHelper.TAG, "cloud history size = " + list.size());
        }
        List<HistoryInfo> resultList = HistoryInfoHelper.merge(temp, list);
        this.mHistoryDbCache.merge(resultList);
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext())) {
            this.mLoginMemoryCache.clear();
            if (resultList != null && resultList.size() > 0) {
                for (HistoryInfo info : resultList) {
                    this.mLoginMemoryCache.put(getAlbumKey(info.getQpId()), info);
                }
            }
        }
    }

    private void handleDeleteHistory(String qpId, String tvId) {
        boolean hasLogIn = GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext());
        LogUtils.d(HistoryInfoHelper.TAG, "cookie=" + AppClientUtils.getCookie(AppRuntimeEnv.get().getApplicationContext()));
        DeleteListener listener = new DeleteListener();
        if (hasLogIn) {
            UserHelper.deleteHistoryAlbum.callSync(listener, cookie, tvId);
            this.mLoginMemoryCache.remove(getAlbumKey(qpId));
        } else {
            UserHelper.deleteHistoryAlbumForForAnonymity.callSync(listener, cookie, tvId);
            this.mNoLoginMemoryCache.remove(getAlbumKey(qpId));
        }
        this.mHistoryDbCache.removeAlbum(qpId);
        GetInterfaceTools.getDataBus().postEvent(IDataBus.HISTORY_DELETE);
    }

    private void HandleUploadHistory(HistoryInfo info) {
        this.mHistoryDbCache.put(info);
        int playTime = info.getPlayTime();
        String tvId = info.getTvId();
        boolean hasLogIn = GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext());
        if (playTime == -2) {
            playTime = 0;
        }
        if (playTime == -1) {
            playTime = 1;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(HistoryInfoHelper.TAG, "save() tvId=" + tvId + ", time=" + playTime);
        }
        String cookie = AppClientUtils.getCookie(AppRuntimeEnv.get().getApplicationContext());
        if (hasLogIn) {
            this.mLoginMemoryCache.put(getAlbumKey(info.getQpId()), info);
            UserHelper.uploadHistory.call(new SaveTvHistoryListener(), cookie, tvId, String.valueOf(playTime));
        } else {
            this.mNoLoginMemoryCache.put(getAlbumKey(info.getQpId()), info);
            UserHelper.uploadHistoryForAnonymity.call(new SaveTvHistoryListener(), cookie, tvId, String.valueOf(playTime));
        }
        GetInterfaceTools.getOpenapiReporterManager().onAddPlayRecord(info.getAlbum());
    }

    public HistoryInfo getHistoryByAlbumId(String qpid) {
        return this.mHistoryDbCache.getAlbum(qpid);
    }

    public HistoryInfo getHistoryByTvId(String tvid) {
        return this.mHistoryDbCache.getTv(tvid);
    }

    private void handleClearAllHistory() {
        this.mHistoryDbCache.clear();
        boolean hasLogIn = GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext());
        String cookie = AppClientUtils.getCookie(AppRuntimeEnv.get().getApplicationContext());
        DeleteListener listener = new DeleteListener();
        if (hasLogIn) {
            UserHelper.clearHistory.callSync(listener, cookie);
            this.mLoginMemoryCache.clear();
        } else {
            UserHelper.clearHistoryForAnonymity.callSync(listener, cookie);
            this.mNoLoginMemoryCache.clear();
        }
        GetInterfaceTools.getDataBus().postEvent(IDataBus.HISTORY_DELETE);
        GetInterfaceTools.getOpenapiReporterManager().onDeleteAllPlayRecord();
    }

    private void handleClearHistoryForUser() {
        this.mHistoryDbCache.clearDbForUser();
        this.mLoginMemoryCache.clear();
    }

    private String getAlbumKey(String id) {
        return AppClientUtils.getCookie(AppRuntimeEnv.get().getApplicationContext()) + "-" + id;
    }
}
