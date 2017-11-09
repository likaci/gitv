package com.gala.video.albumlist.widget;

import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import java.util.HashMap;
import java.util.Map.Entry;

public class g {
    final HashMap<ViewHolder, a> a = new HashMap();

    interface b {
        void a(ViewHolder viewHolder, d dVar, d dVar2);

        void b(ViewHolder viewHolder, d dVar, d dVar2);

        void c(ViewHolder viewHolder, d dVar, d dVar2);
    }

    static class a {
        int a;
        d f631a;
        d b;

        a() {
        }
    }

    void a(ViewHolder viewHolder, d dVar) {
        a aVar = (a) this.a.get(viewHolder);
        if (aVar == null) {
            aVar = new a();
            this.a.put(viewHolder, aVar);
        }
        aVar.f631a = dVar;
        aVar.a |= 4;
    }

    void b(ViewHolder viewHolder, d dVar) {
        a aVar = (a) this.a.get(viewHolder);
        if (aVar == null) {
            aVar = new a();
            this.a.put(viewHolder, aVar);
        }
        aVar.b = dVar;
        aVar.a |= 8;
    }

    public void a(b bVar) {
        for (Entry entry : this.a.entrySet()) {
            ViewHolder viewHolder = (ViewHolder) entry.getKey();
            a aVar = (a) entry.getValue();
            if ((aVar.a & 12) == 12) {
                bVar.c(viewHolder, aVar.f631a, aVar.b);
            } else if ((aVar.a & 4) != 0) {
                bVar.a(viewHolder, aVar.f631a, null);
            } else if ((aVar.a & 8) != 0) {
                bVar.b(viewHolder, aVar.f631a, aVar.b);
            }
        }
        this.a.clear();
    }
}
