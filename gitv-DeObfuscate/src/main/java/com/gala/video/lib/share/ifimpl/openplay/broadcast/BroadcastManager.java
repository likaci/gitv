package com.gala.video.lib.share.ifimpl.openplay.broadcast;

import android.util.Log;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.ActionHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BroadcastManager {
    private static BroadcastManager sManager = new BroadcastManager();
    private final String TAG = getClass().getName();
    private final List<ActionHolder> mSupportActionHolderList = new CopyOnWriteArrayList();

    private BroadcastManager() {
    }

    public static BroadcastManager instance() {
        return sManager;
    }

    public synchronized boolean addBroadcastActionHolder(ActionHolder actionHolder) {
        boolean add;
        if (actionHolder != null) {
            if (!this.mSupportActionHolderList.contains(actionHolder)) {
                add = this.mSupportActionHolderList.add(actionHolder);
            }
        }
        add = false;
        return add;
    }

    public synchronized BaseAction findBroadcastActionByKey(String key) {
        BaseAction find;
        for (ActionHolder actionHolder : this.mSupportActionHolderList) {
            if (key != null && actionHolder.getKey() != null && key.equalsIgnoreCase(actionHolder.getKey())) {
                find = actionHolder.getAction();
                break;
            }
        }
        Log.d(this.TAG, "not find the BroadcastAction,return null. ");
        find = null;
        return find;
    }

    public String getSupportActionList() {
        StringBuffer stringBuffer = new StringBuffer();
        for (ActionHolder actionHolder : this.mSupportActionHolderList) {
            stringBuffer.append(actionHolder.toString()).append(";");
        }
        return stringBuffer.toString();
    }
}
