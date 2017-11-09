package com.gala.video.app.player.pingback;

import com.gala.pingback.IPingback;
import com.gala.pingback.PingbackItem;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;

public abstract class Pingback implements IPingback {
    private static final String TAG = "Pingback";
    private final String[] mAllItemTypes;
    private final String[] mItemTypes;
    private final List<PingbackItem> mItems = new ArrayList();

    public abstract void doSend(Map<String, String> map);

    public abstract void doSend(String[] strArr);

    public Pingback(String[] keys, String[] allKeys) {
        if (keys == null || keys.length == 0 || allKeys == null || keys.length > allKeys.length) {
            throw new RuntimeException("pingback key is error");
        }
        this.mItemTypes = keys;
        this.mAllItemTypes = allKeys;
    }

    public final Pingback addItem(PingbackItem item) {
        if (item == null) {
            throw new RuntimeException("pingback additem is null");
        }
        this.mItems.add(item);
        return this;
    }

    public final void post() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "post()" + this);
        }
        EventBus.getDefault().post(this);
    }

    public final boolean check() {
        boolean ready = true;
        HashMap<String, String> map = new HashMap();
        for (PingbackItem item : this.mItems) {
            map.put(item.getKey(), item.getValue());
        }
        int i = 0;
        int size = this.mItemTypes.length;
        while (i < size) {
            String type = this.mItemTypes[i];
            if (map.containsKey(type)) {
                i++;
            } else {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1571e(TAG, "check() fail for not find key:" + type);
                }
                ready = false;
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "check(" + ready + ")");
                }
                return ready;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "check(" + ready + ")");
        }
        return ready;
    }

    public final void send() {
        String[] arrays = new String[this.mAllItemTypes.length];
        Map map = getDataMap();
        int size = this.mAllItemTypes.length;
        for (int i = 0; i < size; i++) {
            String value = (String) map.get(this.mAllItemTypes[i]);
            if (value == null) {
                arrays[i] = "";
            } else {
                arrays[i] = value;
            }
        }
        if (LogUtils.mIsDebug) {
            StringBuilder builder = new StringBuilder();
            for (String str : arrays) {
                builder.append(str).append(",");
            }
            LogUtils.m1568d(TAG, "mypingbacklog send(" + builder.toString() + ")" + "," + this);
        }
        doSend(map);
    }

    protected Map<String, String> getDataMap() {
        Map<String, String> map = new HashMap();
        for (PingbackItem item : this.mItems) {
            map.put(item.getKey(), item.getValue());
        }
        return map;
    }
}
