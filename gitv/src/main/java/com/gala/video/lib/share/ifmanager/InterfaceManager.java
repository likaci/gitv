package com.gala.video.lib.share.ifmanager;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;

public class InterfaceManager {
    private static final String TAG = "IntrefaceManager";
    Map<String, IInterfaceWrapper> mMap;

    private static class SingletonHelper {
        private static InterfaceManager sIns = new InterfaceManager();

        private SingletonHelper() {
        }
    }

    private InterfaceManager() {
        this.mMap = new HashMap();
    }

    public static InterfaceManager get() {
        return SingletonHelper.sIns;
    }

    public synchronized void register(String key, IInterfaceWrapper ifWrapper) {
        this.mMap.put(key, ifWrapper);
    }

    public synchronized void unRegister(String key) {
        this.mMap.remove(key);
    }

    public synchronized IInterfaceWrapper get(String key) {
        return (IInterfaceWrapper) this.mMap.get(key);
    }

    public synchronized void printMap() {
        Log.d(TAG, "interface map's size() = " + this.mMap.size() + "mMap = " + this.mMap);
        for (String key : this.mMap.keySet()) {
            Log.d(TAG, "key= " + key + " and value= " + this.mMap.get(key));
        }
    }
}
