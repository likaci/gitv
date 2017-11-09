package com.gala.cloudui.cache;

import com.gala.cloudui.block.Cute;
import com.gala.cloudui.block.CuteBg;
import java.util.ArrayList;
import java.util.List;

public class CuteBgCache {
    private static final CuteBg a = new CuteBg();
    private static final CuteBgCache f276a = new CuteBgCache();
    private final List<CuteBg> f277a = new ArrayList();

    public static CuteBgCache newInstance() {
        return f276a;
    }

    public CuteBg pop(Cute newCute) {
        CuteBg cuteBg;
        if (this.f277a.size() == 0) {
            cuteBg = new CuteBg();
        } else {
            cuteBg = (CuteBg) this.f277a.get(0);
            if (cuteBg == null) {
                cuteBg = new CuteBg();
            } else {
                this.f277a.remove(cuteBg);
            }
        }
        cuteBg.suck(newCute);
        return cuteBg;
    }

    public void push(CuteBg oldCute) {
        if (oldCute != null) {
            oldCute.suck(a);
            if (!this.f277a.contains(oldCute)) {
                this.f277a.add(oldCute);
            }
        }
    }
}
