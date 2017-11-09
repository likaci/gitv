package com.gala.video.albumlist.widget;

import com.gala.video.albumlist.widget.BlocksView.C0400d;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import java.util.HashMap;
import java.util.Map.Entry;

public class C0427g {
    final HashMap<ViewHolder, C0426a> f1612a = new HashMap();

    interface C0391b {
        void mo882a(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2);

        void mo883b(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2);

        void mo884c(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2);
    }

    static class C0426a {
        int f1609a;
        C0400d f1610a;
        C0400d f1611b;

        C0426a() {
        }
    }

    void m1203a(ViewHolder viewHolder, C0400d c0400d) {
        C0426a c0426a = (C0426a) this.f1612a.get(viewHolder);
        if (c0426a == null) {
            c0426a = new C0426a();
            this.f1612a.put(viewHolder, c0426a);
        }
        c0426a.f1610a = c0400d;
        c0426a.f1609a |= 4;
    }

    void m1205b(ViewHolder viewHolder, C0400d c0400d) {
        C0426a c0426a = (C0426a) this.f1612a.get(viewHolder);
        if (c0426a == null) {
            c0426a = new C0426a();
            this.f1612a.put(viewHolder, c0426a);
        }
        c0426a.f1611b = c0400d;
        c0426a.f1609a |= 8;
    }

    public void m1204a(C0391b c0391b) {
        for (Entry entry : this.f1612a.entrySet()) {
            ViewHolder viewHolder = (ViewHolder) entry.getKey();
            C0426a c0426a = (C0426a) entry.getValue();
            if ((c0426a.f1609a & 12) == 12) {
                c0391b.mo884c(viewHolder, c0426a.f1610a, c0426a.f1611b);
            } else if ((c0426a.f1609a & 4) != 0) {
                c0391b.mo882a(viewHolder, c0426a.f1610a, null);
            } else if ((c0426a.f1609a & 8) != 0) {
                c0391b.mo883b(viewHolder, c0426a.f1610a, c0426a.f1611b);
            }
        }
        this.f1612a.clear();
    }
}
