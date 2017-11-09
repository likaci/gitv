package com.xiaomi.mistatistic.sdk.controller;

import android.os.Handler;
import android.os.HandlerThread;
import java.util.ArrayList;
import java.util.Iterator;

class f extends HandlerThread {
    final /* synthetic */ b a;

    public f(b bVar, String str) {
        this.a = bVar;
        super(str);
    }

    protected void onLooperPrepared() {
        this.a.c = new Handler();
        ArrayList arrayList = null;
        synchronized (this.a.d) {
            if (!this.a.d.isEmpty()) {
                arrayList = (ArrayList) this.a.d.clone();
                this.a.d.clear();
            }
        }
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((e) it.next()).a();
            }
        }
        super.onLooperPrepared();
    }
}
