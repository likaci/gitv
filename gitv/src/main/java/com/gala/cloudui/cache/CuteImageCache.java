package com.gala.cloudui.cache;

import com.gala.cloudui.block.Cute;
import com.gala.cloudui.block.CuteImage;
import java.util.ArrayList;
import java.util.List;

public class CuteImageCache {
    private static final CuteImage a = new CuteImage();
    private static final CuteImageCache f280a = new CuteImageCache();
    private final List<CuteImage> f281a = new ArrayList();

    public static CuteImageCache newInstance() {
        return f280a;
    }

    public CuteImage pop(Cute newCute) {
        CuteImage cuteImage;
        if (this.f281a.size() == 0) {
            cuteImage = new CuteImage();
        } else {
            cuteImage = (CuteImage) this.f281a.get(0);
            if (cuteImage == null) {
                cuteImage = new CuteImage();
            } else {
                this.f281a.remove(cuteImage);
            }
        }
        cuteImage.suck(newCute);
        return cuteImage;
    }

    public void push(CuteImage oldCute) {
        if (oldCute != null) {
            oldCute.suck(a);
            if (!this.f281a.contains(oldCute)) {
                this.f281a.add(oldCute);
            }
        }
    }
}
