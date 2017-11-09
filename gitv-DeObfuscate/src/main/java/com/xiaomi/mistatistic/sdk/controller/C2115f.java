package com.xiaomi.mistatistic.sdk.controller;

import android.os.Handler;
import android.os.HandlerThread;
import java.util.ArrayList;
import java.util.Iterator;

class C2115f extends HandlerThread {
    final /* synthetic */ C2112b f2202a;

    public C2115f(C2112b c2112b, String str) {
        this.f2202a = c2112b;
        super(str);
    }

    protected void onLooperPrepared() {
        this.f2202a.f2196c = new Handler();
        ArrayList arrayList = null;
        synchronized (this.f2202a.f2197d) {
            if (!this.f2202a.f2197d.isEmpty()) {
                arrayList = (ArrayList) this.f2202a.f2197d.clone();
                this.f2202a.f2197d.clear();
            }
        }
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((C2105e) it.next()).mo4534a();
            }
        }
        super.onLooperPrepared();
    }
}
