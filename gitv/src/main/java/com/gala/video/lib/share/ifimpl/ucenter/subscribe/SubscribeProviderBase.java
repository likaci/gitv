package com.gala.video.lib.share.ifimpl.ucenter.subscribe;

import android.util.Log;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.model.SubscribeState;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.tvapi.vrs.result.ApiResultSubScribeList;
import com.gala.tvapi.vrs.result.ApiResultSubscribeState;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.LoginCallbackRecorder;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.LoginCallbackRecorder.LoginCallbackRecorderListener;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.ISubscribeProvider.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.subscribe.SubscribeObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

abstract class SubscribeProviderBase extends Wrapper {
    static final String CODE_FAIL = "A00001";
    static final String CODE_LOGIN = "B00001";
    private static final String CODE_SUCCESS = "A00000";
    static final boolean DEBUG = true;
    static final String TAG = "SubscribeProviderBase";
    final Map<String, BasicItem> mCache = new HashMap();
    final Map<String, Collection<SubscribeObserver>> mItemObservers = new HashMap();
    LoginCallbackRecorderListener mLoginListener = new LoginCallbackRecorderListener() {
        public void onLogin(String uid) {
            SubscribeProviderBase.this.getUserCookie();
            if (SubscribeProviderBase.this.userCookie.length() == 0) {
                Log.e(SubscribeProviderBase.TAG, "login, user cookie is null");
            } else {
                ThreadUtils.execute(new Runnable() {
                    public void run() {
                        Set<String> set = SubscribeProviderBase.this.mItemObservers.keySet();
                        List<String> toUpdate = new ArrayList(set.size());
                        for (String id : set) {
                            BasicItem item = (BasicItem) SubscribeProviderBase.this.mCache.get(id);
                            if (item == null || !(SubscribeProviderBase.this.userCookie.length() == 0 || item.user != null || item.state == -1)) {
                                toUpdate.add(id);
                            }
                        }
                        if (toUpdate.size() > 0) {
                            SubscribeProviderBase.this.getSubscribeStateInner(toUpdate);
                        }
                    }
                });
            }
        }

        public void onLogout(String uid) {
            SubscribeProviderBase.this.clearUserCookie();
            synchronized (SubscribeProviderBase.this.mCache) {
                for (BasicItem item : SubscribeProviderBase.this.mCache.values()) {
                    if (item.user != null) {
                        item.user = null;
                        if (item.state == 1 || item.state == 2) {
                            item.state = 0;
                            final String qpid = item.id;
                            ThreadUtils.execute(new Runnable() {
                                public void run() {
                                    SubscribeProviderBase.this.onCancelSuccess(qpid);
                                }
                            });
                        }
                    }
                }
            }
        }
    };
    NetWorkManager netWorkManager;
    String userCookie = "";

    static class BasicItem {
        public String id;
        public int state = Integer.MIN_VALUE;
        public String user;

        BasicItem() {
        }
    }

    SubscribeProviderBase() {
        getUserCookie();
        LoginCallbackRecorder.get().addListener(this.mLoginListener);
        this.netWorkManager = NetWorkManager.getInstance();
    }

    private void getUserCookie() {
        String cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
        if (cookie == null) {
            cookie = "";
        }
        this.userCookie = cookie;
    }

    private void clearUserCookie() {
        this.userCookie = "";
    }

    boolean isNetworkConnected() {
        boolean result = true;
        int state = this.netWorkManager.getNetState();
        if (!(state == 2 || state == 1)) {
            result = false;
        }
        if (!result) {
            Log.e(TAG, "network state is: " + state);
        }
        return result;
    }

    void addSubscribeSync(final IVrsCallback<ApiResult> callback, final String qpid) {
        if (isNetworkConnected()) {
            checkDatabaseLoaded();
            UserHelper.subscribe.callSync(new IVrsCallback<ApiResultCode>() {
                public void onSuccess(ApiResultCode apiResultCode) {
                    synchronized (SubscribeProviderBase.this.mCache) {
                        BasicItem item = new BasicItem();
                        item.id = qpid;
                        item.state = 1;
                        item.user = SubscribeProviderBase.this.userCookie;
                        SubscribeProviderBase.this.mCache.put(qpid, item);
                    }
                    SubscribeProviderBase.this.updateSubscribeInCache(qpid, false);
                    callback.onSuccess(apiResultCode);
                    SubscribeProviderBase.this.onSubscribeSucess(qpid);
                }

                public void onException(ApiException e) {
                    callback.onException(e);
                }
            }, this.userCookie, qpid);
            return;
        }
        callbackFailed(callback, qpid);
    }

    void cancelSubscribeSync(final IVrsCallback<ApiResult> callback, final String qpid) {
        if (isNetworkConnected()) {
            checkDatabaseLoaded();
            UserHelper.unsubscribe.callSync(new IVrsCallback<ApiResultCode>() {
                public void onSuccess(ApiResultCode apiResultCode) {
                    synchronized (SubscribeProviderBase.this.mCache) {
                        BasicItem item = new BasicItem();
                        item.id = qpid;
                        item.state = 0;
                        item.user = SubscribeProviderBase.this.userCookie;
                        SubscribeProviderBase.this.mCache.put(qpid, item);
                    }
                    SubscribeProviderBase.this.updateCancelSubscribeInCache(qpid, false);
                    callback.onSuccess(apiResultCode);
                    SubscribeProviderBase.this.onCancelSuccess(qpid);
                }

                public void onException(ApiException e) {
                    callback.onException(e);
                }
            }, this.userCookie, qpid);
            return;
        }
        callbackFailed(callback, qpid);
    }

    void getSubscribeStateSync(final IVrsCallback<ApiResult> callback, String[] qpid) {
        checkDatabaseLoaded();
        List cached = new ArrayList(qpid.length);
        synchronized (this.mCache) {
            for (String id : qpid) {
                BasicItem item = (BasicItem) this.mCache.get(id);
                if (!(item == null || (this.userCookie.length() != 0 && item.user == null && item.state == 0))) {
                    cached.add(item);
                }
            }
        }
        if (cached.size() == qpid.length) {
            Log.d(TAG, "get state from memory: " + qpid[0]);
            callbackSuccess(callback, "first id: " + qpid[0]);
            onGetStateSuccess(cached);
            return;
        }
        cached.clear();
        if (isNetworkConnected()) {
            StringBuilder sb = new StringBuilder();
            for (String id2 : qpid) {
                sb.append(id2).append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            UserHelper.subscribeState.callSync(new IVrsCallback<ApiResultSubscribeState>() {
                public void onSuccess(ApiResultSubscribeState result) {
                    SubscribeProviderBase.this.updateStateInCache(result.data);
                    callback.onSuccess(result);
                    SubscribeProviderBase.this.onGetStateSuccess(result.data);
                }

                public void onException(ApiException e) {
                    callback.onException(e);
                }
            }, this.userCookie, sb.toString());
            return;
        }
        callbackFailed(callback, qpid[0]);
    }

    private void getSubscribeStateInner(List<String> uncached) {
        if (isNetworkConnected()) {
            checkDatabaseLoaded();
            StringBuilder sb = new StringBuilder();
            for (String id : uncached) {
                sb.append(id).append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            UserHelper.subscribeState.callSync(new IVrsCallback<ApiResultSubscribeState>() {
                public void onSuccess(ApiResultSubscribeState result) {
                    Log.d(SubscribeProviderBase.TAG, "getSubscribeSstateInner success");
                    SubscribeProviderBase.this.updateStateInCache(result.data);
                    SubscribeProviderBase.this.onGetStateSuccess(result.data);
                }

                public void onException(ApiException e) {
                    Log.e(SubscribeProviderBase.TAG, "getSubscribeStateInner", e);
                }
            }, this.userCookie, sb.toString());
        }
    }

    void getSubscribeList(IVrsCallback<ApiResultSubScribeList> callback, int page, int size, int type, int order) {
        if (!isNetworkConnected()) {
            callback.onException(new ApiException("", "A00001"));
        }
        if (this.userCookie.length() == 0) {
            callback.onException(new ApiException("", CODE_LOGIN));
        }
        final String sorder = order == 2 ? "latestUpdate" : "subscribeTime";
        final IVrsCallback<ApiResultSubScribeList> iVrsCallback = callback;
        final int i = page;
        final int i2 = size;
        final int i3 = type;
        ThreadUtils.execute(new Runnable() {
            public void run() {
                UserHelper.subscribeList.callSync(new IVrsCallback<ApiResultSubScribeList>() {
                    public void onSuccess(ApiResultSubScribeList result) {
                        iVrsCallback.onSuccess(result);
                    }

                    public void onException(ApiException e) {
                        iVrsCallback.onException(e);
                    }
                }, SubscribeProviderBase.this.userCookie, "" + i, "" + i2, "" + i3, sorder);
            }
        });
    }

    private void checkDatabaseLoaded() {
    }

    private void updateStateInCache(Map<String, SubscribeState> stateList) {
        synchronized (this.mCache) {
            for (Entry<String, SubscribeState> entry : stateList.entrySet()) {
                String qpid = (String) entry.getKey();
                SubscribeState state = (SubscribeState) entry.getValue();
                BasicItem item = (BasicItem) this.mCache.get(qpid);
                if (item == null) {
                    item = new BasicItem();
                    item.id = qpid;
                    this.mCache.put(qpid, item);
                } else if (item.state == state.state) {
                }
                item.state = state.state;
                item.user = this.userCookie.length() == 0 ? null : this.userCookie;
            }
        }
    }

    private void updateSubscribeInCache(final String qpid, boolean syncToServer) {
        final String cookie = this.userCookie;
        if (syncToServer) {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    UserHelper.subscribe.callSync(new IVrsCallback<ApiResultCode>() {
                        public void onSuccess(ApiResultCode apiResultCode) {
                            Log.d(SubscribeProviderBase.TAG, "success subscribe qpid: " + qpid);
                        }

                        public void onException(ApiException e) {
                            Log.e(SubscribeProviderBase.TAG, "exception subscribe qpid: " + qpid, e);
                        }
                    }, cookie, qpid);
                }
            });
        }
    }

    private void onSubscribeSucess(String qpid) {
        synchronized (this.mItemObservers) {
            Collection<SubscribeObserver> observers = (Collection) this.mItemObservers.get(qpid);
            if (observers != null) {
                for (SubscribeObserver o : observers) {
                    Log.d(TAG, qpid + " subscribe call observer: " + o.hashCode());
                    o.onItemState(qpid, 1);
                }
            }
        }
    }

    private void updateCancelSubscribeInCache(final String qpid, boolean syncToServer) {
        final String cookie = this.userCookie;
        if (syncToServer) {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    UserHelper.unsubscribe.callSync(new IVrsCallback<ApiResultCode>() {
                        public void onSuccess(ApiResultCode apiResultCode) {
                            Log.d(SubscribeProviderBase.TAG, "success unsubscribe qpid: " + qpid);
                        }

                        public void onException(ApiException e) {
                            Log.e(SubscribeProviderBase.TAG, "exception unsubscribe qpid: " + qpid, e);
                        }
                    }, cookie, qpid);
                }
            });
        }
    }

    private void onCancelSuccess(String qpid) {
        synchronized (this.mItemObservers) {
            Collection<SubscribeObserver> observers = (Collection) this.mItemObservers.get(qpid);
            if (observers != null) {
                for (SubscribeObserver o : observers) {
                    o.onItemState(qpid, 0);
                }
            }
        }
    }

    private void onGetStateSuccess(List<BasicItem> cached) {
        synchronized (this.mItemObservers) {
            for (BasicItem item : cached) {
                Collection<SubscribeObserver> observers = (Collection) this.mItemObservers.get(item.id);
                if (observers != null) {
                    int state = item.state;
                    if (state == 2) {
                        state = 1;
                    }
                    for (SubscribeObserver o : observers) {
                        o.onItemState(item.id, state);
                    }
                }
            }
        }
    }

    private void onGetStateSuccess(Map<String, SubscribeState> data) {
        synchronized (this.mItemObservers) {
            for (Entry<String, SubscribeState> entry : data.entrySet()) {
                String qpid = (String) entry.getKey();
                Collection<SubscribeObserver> observers = (Collection) this.mItemObservers.get(qpid);
                if (observers != null) {
                    int state = ((SubscribeState) entry.getValue()).state;
                    if (state == 2) {
                        state = 1;
                    }
                    for (SubscribeObserver o : observers) {
                        o.onItemState(qpid, state);
                    }
                }
            }
        }
    }

    private void callbackSuccess(IVrsCallback<ApiResult> callback, String msg) {
        ApiResult code = new ApiResult();
        code.setCode("A00000");
        code.setMsg(msg);
        callback.onSuccess(code);
    }

    private void callbackFailed(IVrsCallback<ApiResult> callback, String msg) {
        callback.onException(new ApiException(msg, "A00001"));
    }
}
