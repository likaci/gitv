package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter;

import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultSubScribeList;
import com.gala.video.api.ApiResult;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.subscribe.SubscribeObserver;
import com.gala.video.lib.share.utils.TraceEx;
import java.util.Collection;

public interface ISubscribeProvider extends IInterfaceWrapper {

    public static abstract class Wrapper implements ISubscribeProvider {
        public Object getInterface() {
            return this;
        }

        public static ISubscribeProvider asInterface(Object wrapper) {
            TraceEx.beginSection("IGalaAccountManager.asInterface");
            if (wrapper == null || !(wrapper instanceof ISubscribeProvider)) {
                TraceEx.endSection();
                return null;
            }
            TraceEx.endSection();
            return (ISubscribeProvider) wrapper;
        }
    }

    void addObserver(SubscribeObserver subscribeObserver, String str);

    void addSubscribe(IVrsCallback<ApiResult> iVrsCallback, String str);

    void cancelSubscribe(IVrsCallback<ApiResult> iVrsCallback, String str);

    void getMySubscribeList(IVrsCallback<ApiResultSubScribeList> iVrsCallback, int i, int i2, int i3, int i4);

    void getSubscribeState(IVrsCallback<ApiResult> iVrsCallback, Collection<String> collection);

    void getSubscribeState(IVrsCallback<ApiResult> iVrsCallback, String[] strArr);

    void removeObserver(SubscribeObserver subscribeObserver);

    void removeObserver(String str, SubscribeObserver subscribeObserver);

    void reset();
}
