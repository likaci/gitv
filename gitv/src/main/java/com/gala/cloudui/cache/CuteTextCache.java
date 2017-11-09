package com.gala.cloudui.cache;

import com.gala.cloudui.block.Cute;
import com.gala.cloudui.block.CuteText;
import java.util.ArrayList;
import java.util.List;

public class CuteTextCache {
    private static final CuteText a = new CuteText();
    private static final CuteTextCache f278a = new CuteTextCache();
    private final List<CuteText> f279a = new ArrayList();

    public static CuteTextCache newInstance() {
        return f278a;
    }

    public CuteText pop(Cute newCute) {
        CuteText cuteText;
        if (this.f279a.size() == 0) {
            cuteText = new CuteText();
        } else {
            cuteText = (CuteText) this.f279a.get(0);
            if (cuteText == null) {
                cuteText = new CuteText();
            } else {
                this.f279a.remove(cuteText);
            }
        }
        cuteText.suck(newCute);
        return cuteText;
    }

    public void push(CuteText oldCute) {
        if (oldCute != null) {
            oldCute.suck(a);
            if (!this.f279a.contains(oldCute)) {
                this.f279a.add(oldCute);
            }
        }
    }
}
