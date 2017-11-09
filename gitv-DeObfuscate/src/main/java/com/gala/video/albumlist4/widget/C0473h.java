package com.gala.video.albumlist4.widget;

import com.gala.video.albumlist4.widget.RecyclerView.C0446d;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import java.util.HashMap;
import java.util.Map.Entry;

public class C0473h {
    final HashMap<ViewHolder, C0472a> f1839a = new HashMap();

    interface C0437b {
        void mo981a(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2);

        void mo982b(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2);

        void mo983c(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2);
    }

    static class C0472a {
        int f1836a;
        C0446d f1837a;
        C0446d f1838b;

        C0472a() {
        }
    }

    void m1512a(ViewHolder viewHolder, C0446d c0446d) {
        C0472a c0472a = (C0472a) this.f1839a.get(viewHolder);
        if (c0472a == null) {
            c0472a = new C0472a();
            this.f1839a.put(viewHolder, c0472a);
        }
        c0472a.f1837a = c0446d;
        c0472a.f1836a |= 4;
    }

    void m1514b(ViewHolder viewHolder, C0446d c0446d) {
        C0472a c0472a = (C0472a) this.f1839a.get(viewHolder);
        if (c0472a == null) {
            c0472a = new C0472a();
            this.f1839a.put(viewHolder, c0472a);
        }
        c0472a.f1838b = c0446d;
        c0472a.f1836a |= 8;
    }

    public void m1513a(C0437b c0437b) {
        for (Entry entry : this.f1839a.entrySet()) {
            ViewHolder viewHolder = (ViewHolder) entry.getKey();
            C0472a c0472a = (C0472a) entry.getValue();
            if ((c0472a.f1836a & 12) == 12) {
                c0437b.mo983c(viewHolder, c0472a.f1837a, c0472a.f1838b);
            } else if ((c0472a.f1836a & 4) != 0) {
                c0437b.mo981a(viewHolder, c0472a.f1837a, null);
            } else if ((c0472a.f1836a & 8) != 0) {
                c0437b.mo982b(viewHolder, c0472a.f1837a, c0472a.f1838b);
            }
        }
        this.f1839a.clear();
    }
}
