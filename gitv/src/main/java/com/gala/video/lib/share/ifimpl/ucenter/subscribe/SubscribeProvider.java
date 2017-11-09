package com.gala.video.lib.share.ifimpl.ucenter.subscribe;

import android.text.TextUtils;
import android.util.Log;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultSubScribeList;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.LoginCallbackRecorder;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.subscribe.SubscribeObserver;
import java.util.Collection;
import java.util.LinkedList;

public class SubscribeProvider extends SubscribeProviderBase {
    SubscribeProvider() {
    }

    public void addSubscribe(final IVrsCallback<ApiResult> callback, final String qpid) {
        if (TextUtils.isEmpty(qpid)) {
            callback.onException(new ApiException("qpid=null", LoginConstant.ACCOUNT_ECODE_A00001));
        } else if (this.userCookie.length() == 0) {
            callback.onException(new ApiException("", "B00001"));
        } else {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    SubscribeProvider.this.addSubscribeSync(callback, qpid);
                }
            });
        }
    }

    public void cancelSubscribe(final IVrsCallback<ApiResult> callback, final String qpid) {
        if (TextUtils.isEmpty(qpid)) {
            callback.onException(new ApiException("qpid=null", LoginConstant.ACCOUNT_ECODE_A00001));
        } else if (this.userCookie.length() == 0) {
            callback.onException(new ApiException("", "B00001"));
        } else {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    SubscribeProvider.this.cancelSubscribeSync(callback, qpid);
                }
            });
        }
    }

    public void getSubscribeState(IVrsCallback<ApiResult> callback, Collection<String> qpid) {
        if (qpid == null) {
            callback.onException(new ApiException("qpid=null", LoginConstant.ACCOUNT_ECODE_A00001));
        } else {
            getSubscribeState((IVrsCallback) callback, (String[]) qpid.toArray(new String[qpid.size()]));
        }
    }

    public void getSubscribeState(final IVrsCallback<ApiResult> callback, final String[] qpid) {
        if (qpid == null || qpid.length == 0) {
            callback.onException(new ApiException("qpid=null", LoginConstant.ACCOUNT_ECODE_A00001));
            return;
        }
        try {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    SubscribeProvider.this.getSubscribeStateSync(callback, qpid);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMySubscribeList(IVrsCallback<ApiResultSubScribeList> callback, int page, int size, int type, int order) {
        if (!isNetworkConnected()) {
            callback.onException(new ApiException("", LoginConstant.ACCOUNT_ECODE_A00001));
        }
        if (this.userCookie.length() == 0) {
            callback.onException(new ApiException("", "B00001"));
        }
        getSubscribeList(callback, page, size, type, order);
    }

    public void addObserver(SubscribeObserver observer, String qpid) {
        Log.d("SubscribeProviderBase", qpid + " add observer: " + observer.hashCode());
        synchronized (this.mItemObservers) {
            if (qpid != null) {
                Collection<SubscribeObserver> list = (Collection) this.mItemObservers.get(qpid);
                if (list == null) {
                    list = new LinkedList();
                    list.add(observer);
                    this.mItemObservers.put(qpid, list);
                } else if (!list.contains(observer)) {
                    list.add(observer);
                }
            }
        }
    }

    public void removeObserver(SubscribeObserver observer) {
        Log.d("SubscribeProviderBase", "remove observer: " + observer.hashCode());
        synchronized (this.mItemObservers) {
            for (Collection<SubscribeObserver> list : this.mItemObservers.values()) {
                if (list.remove(observer)) {
                }
            }
        }
    }

    public void removeObserver(String qpid, SubscribeObserver observer) {
        synchronized (this.mItemObservers) {
            Collection<SubscribeObserver> list = (Collection) this.mItemObservers.get(qpid);
            if (list != null) {
                list.remove(observer);
            }
        }
    }

    public void reset() {
        this.userCookie = "";
        LoginCallbackRecorder.get().removeListener(this.mLoginListener);
        if (this.mItemObservers != null) {
            this.mItemObservers.clear();
        }
        if (this.mCache != null) {
            this.mCache.clear();
        }
        this.netWorkManager = null;
    }
}
