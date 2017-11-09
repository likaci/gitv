package com.gala.cloudui.cache;

import com.gala.cloudui.block.Cute;
import com.gala.cloudui.block.CuteImage;
import java.util.ArrayList;
import java.util.List;

public class CuteImageCache {
    private static final CuteImage f476a = new CuteImage();
    private static final CuteImageCache f477a = new CuteImageCache();
    private final List<CuteImage> f478a = new ArrayList();

    public static CuteImageCache newInstance() {
        return f477a;
    }

    public CuteImage pop(Cute newCute) {
        CuteImage cuteImage;
        if (this.f478a.size() == 0) {
            cuteImage = new CuteImage();
        } else {
            cuteImage = (CuteImage) this.f478a.get(0);
            if (cuteImage == null) {
                cuteImage = new CuteImage();
            } else {
                this.f478a.remove(cuteImage);
            }
        }
        cuteImage.suck(newCute);
        return cuteImage;
    }

    public void push(CuteImage oldCute) {
        if (oldCute != null) {
            oldCute.suck(f476a);
            if (!this.f478a.contains(oldCute)) {
                this.f478a.add(oldCute);
            }
        }
    }
}
