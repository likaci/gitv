package com.gala.video.app.epg.home.data.pingback;

import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback.Wrapper;
import com.gala.video.lib.share.pingback.PingBackParams;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadPoolExecutor;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class HomePingback extends Wrapper {
    protected String TAG = getClass().getSimpleName();
    private final HashMap<String, String> mMap = new HashMap();
    private ThreadPoolExecutor mThreadPoolExecutor;

    public IHomePingback createPingback(Object pingbackType) {
        return HomePingbackFactory.instance().createPingback(pingbackType);
    }

    public IHomePingback addItem(HashMap<String, String> pingbackMap) {
        this.mMap.putAll(pingbackMap);
        return this;
    }

    public final IHomePingback addItem(String key, String value) {
        this.mMap.put(key, value);
        return this;
    }

    public final HomePingback setOthersNull() {
        return this;
    }

    public final void post() {
        Map paramsMap = getDataMap();
        if (ListUtils.isEmpty(paramsMap)) {
            LogUtils.w(this.TAG, "send, PingbackLog: pingbackType=" + getType() + "send pingback failed, because pingback parameters are empty");
            return;
        }
        if (HomePingbackDebug.DEBUG_LOG) {
            StringBuilder builder = new StringBuilder();
            for (Entry entry : paramsMap.entrySet()) {
                String value = (String) entry.getValue();
                builder.append(entry.getKey().toString() + SearchCriteria.EQ + value + ", ");
            }
            print(builder);
        }
        PingBack.getInstance().postPingBackToLongYuan(paramsMap);
    }

    public final void send() {
    }

    private Map<String, String> getDataMap() {
        this.mMap.putAll(new PingBackParams().build());
        addDefaultField(this.mMap);
        return this.mMap;
    }

    protected void addDefaultField(HashMap<String, String> hashMap) {
    }

    public String getType() {
        return "default_illegal_pingback_type";
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.mThreadPoolExecutor = threadPoolExecutor;
    }

    private void print(final StringBuilder builder) {
        if (this.mThreadPoolExecutor == null) {
            LogUtils.w(this.TAG, "print, current ThreadPoolExecutor is null");
            return;
        }
        this.mThreadPoolExecutor.execute(new Runnable() {
            public void run() {
                if (HomePingbackDebug.DEBUG_LOG) {
                    LogUtils.d(HomePingback.this.TAG, "PingbackLog: pingbackType=" + HomePingback.this.getType() + ", send " + "(" + builder.toString() + ")");
                }
            }
        });
    }
}
