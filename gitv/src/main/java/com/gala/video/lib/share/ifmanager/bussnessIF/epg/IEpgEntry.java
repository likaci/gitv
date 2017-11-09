package com.gala.video.lib.share.ifmanager.bussnessIF.epg;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IEpgEntry extends IInterfaceWrapper {

    public static abstract class Wrapper implements IEpgEntry {
        public Object getInterface() {
            return this;
        }

        public static IEpgEntry asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IEpgEntry)) {
                return null;
            }
            return (IEpgEntry) wrapper;
        }
    }

    void search(Context context, String str, boolean z);

    void startHomeActivity(Context context, boolean z);

    void startSearchResultPage(Context context, int i, String str, int i2, String str2, String str3);
}
