package com.gala.cloudui.cache;

import com.gala.cloudui.block.Cute;
import com.gala.cloudui.block.CuteBg;
import java.util.ArrayList;
import java.util.List;

public class CuteBgCache {
    private static final CuteBg f473a = new CuteBg();
    private static final CuteBgCache f474a = new CuteBgCache();
    private final List<CuteBg> f475a = new ArrayList();

    public static CuteBgCache newInstance() {
        return f474a;
    }

    public CuteBg pop(Cute newCute) {
        CuteBg cuteBg;
        if (this.f475a.size() == 0) {
            cuteBg = new CuteBg();
        } else {
            cuteBg = (CuteBg) this.f475a.get(0);
            if (cuteBg == null) {
                cuteBg = new CuteBg();
            } else {
                this.f475a.remove(cuteBg);
            }
        }
        cuteBg.suck(newCute);
        return cuteBg;
    }

    public void push(CuteBg oldCute) {
        if (oldCute != null) {
            oldCute.suck(f473a);
            if (!this.f475a.contains(oldCute)) {
                this.f475a.add(oldCute);
            }
        }
    }
}
