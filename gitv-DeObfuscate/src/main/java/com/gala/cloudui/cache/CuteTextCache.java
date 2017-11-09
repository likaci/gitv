package com.gala.cloudui.cache;

import com.gala.cloudui.block.Cute;
import com.gala.cloudui.block.CuteText;
import java.util.ArrayList;
import java.util.List;

public class CuteTextCache {
    private static final CuteText f479a = new CuteText();
    private static final CuteTextCache f480a = new CuteTextCache();
    private final List<CuteText> f481a = new ArrayList();

    public static CuteTextCache newInstance() {
        return f480a;
    }

    public CuteText pop(Cute newCute) {
        CuteText cuteText;
        if (this.f481a.size() == 0) {
            cuteText = new CuteText();
        } else {
            cuteText = (CuteText) this.f481a.get(0);
            if (cuteText == null) {
                cuteText = new CuteText();
            } else {
                this.f481a.remove(cuteText);
            }
        }
        cuteText.suck(newCute);
        return cuteText;
    }

    public void push(CuteText oldCute) {
        if (oldCute != null) {
            oldCute.suck(f479a);
            if (!this.f481a.contains(oldCute)) {
                this.f481a.add(oldCute);
            }
        }
    }
}
