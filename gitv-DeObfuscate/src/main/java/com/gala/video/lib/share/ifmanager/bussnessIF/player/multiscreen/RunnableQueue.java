package com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen;

import com.gala.multiscreen.dmr.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RunnableQueue {
    private Map<String, Runnable> mMatchValues = new HashMap();

    RunnableQueue() {
    }

    public void addMatch(String key, Runnable runnable) {
        if (key != null) {
            checkKey(key, runnable);
            this.mMatchValues.put(StringUtils.filter(key, ".,（,）"), runnable);
        }
    }

    public List<String> getKeys() {
        List<String> keys = new ArrayList();
        for (Object add : this.mMatchValues.keySet()) {
            keys.add(add);
        }
        return keys;
    }

    public Runnable getRunnable(String key) {
        if (this.mMatchValues.containsKey(key)) {
            return (Runnable) this.mMatchValues.get(key);
        }
        return null;
    }

    private void checkKey(String key, Runnable runnable) {
        if (key.contains("（")) {
            this.mMatchValues.put(StringUtils.getBeforeAString(key, "（"), runnable);
            this.mMatchValues.put(StringUtils.getBetweenAandB(key, "（", "）"), runnable);
        }
    }
}
