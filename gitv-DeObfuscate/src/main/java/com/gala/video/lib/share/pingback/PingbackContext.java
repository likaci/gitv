package com.gala.video.lib.share.pingback;

import com.gala.pingback.IPingbackContext;
import com.gala.pingback.IPingbackValueProvider;
import com.gala.pingback.PingbackItem;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.HashMap;

public class PingbackContext implements IPingbackContext {
    private static final String TAG = "PingbackContext";
    private final HashMap<String, PingbackItem> mMap = new HashMap();
    private IPingbackValueProvider mProvider;

    public synchronized void setItem(String key, PingbackItem item) {
        PingbackItem old = (PingbackItem) this.mMap.put(key, item);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "set key=" + key + ", item=" + item + ", old=" + old);
        }
    }

    public synchronized PingbackItem getItem(String key) {
        PingbackItem value;
        if ("e".equals(key) && this.mProvider != null) {
            value = this.mProvider.getValue("e");
        } else if (this.mMap.containsKey(key)) {
            value = (PingbackItem) this.mMap.get(key);
        } else {
            throw new RuntimeException("can not find:" + key);
        }
        return value;
    }

    public void setPingbackValueProvider(IPingbackValueProvider provider) {
        this.mProvider = provider;
    }
}
