package com.gala.imageprovider.p000private;

import android.content.Context;
import android.graphics.Bitmap.Config;
import com.gala.afinal.FinalBitmap;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.IImageProvider;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.imageprovider.base.ImageRequest.ImageType;
import com.gala.imageprovider.base.ImageRequest.ScaleType;
import java.util.ArrayList;
import java.util.List;

public final class C0121E implements IImageProvider {
    private static C0121E f534a;
    private Context f535a;
    private Config f536a;
    private boolean f537a = false;
    private boolean f538b = true;
    private boolean f539c;

    public static synchronized C0121E m276a() {
        C0121E c0121e;
        synchronized (C0121E.class) {
            if (f534a == null) {
                f534a = new C0121E();
            }
            c0121e = f534a;
        }
        return c0121e;
    }

    private C0121E() {
    }

    public final void initialize(Context context) {
        this.f535a = context;
        C0122F.m277a();
    }

    public final void initialize(Context context, String str) {
        this.f535a = context;
        C0122F.m277a();
    }

    public final void loadImage(ImageRequest imageRequest, IImageCallback imageCallback) {
        if (ImageRequest.checkRequestValid(imageRequest)) {
            List arrayList = new ArrayList();
            arrayList.add(imageRequest);
            loadImages(arrayList, imageCallback);
            return;
        }
        C0123G.m282b("ImageProvider/ImageProvider", ">>>>> loadImage: invalid request: " + imageRequest);
    }

    public final void loadImages(List<ImageRequest> imageRequestList, IImageCallback imageCallback) {
        if (this.f535a != null) {
            if (!(imageRequestList == null || imageRequestList.isEmpty())) {
                for (ImageRequest imageRequest : imageRequestList) {
                    if (imageRequest != null) {
                        Object obj = (!this.f537a || imageRequest.getTargetWidth() <= 0 || imageRequest.getTargetHeight() <= 0) ? null : 1;
                        if (obj != null && ScaleType.DEFAULT == imageRequest.getScaleType()) {
                            imageRequest.setScaleType(ScaleType.CENTER_CROP);
                        }
                        if (!(this.f536a == null || imageRequest.isArbitraryDecodeConfig())) {
                            imageRequest.setDecodeConfig(this.f536a);
                            if (imageRequest.getImageType() == ImageType.ROUND) {
                                imageRequest.setDecodeConfig(Config.ARGB_8888);
                            }
                        }
                    }
                }
            }
            for (ImageRequest imageRequest2 : imageRequestList) {
                FinalBitmap.create(this.f535a).loadBitmap(imageRequest2, imageCallback);
            }
        }
    }

    public final void loadImageFromFile(ImageRequest imageRequest, IImageCallback imageCallback) {
        loadImage(imageRequest, imageCallback);
    }

    public final void stopAllTasks() {
        if (this.f535a != null) {
            FinalBitmap.create(this.f535a).stopAllTasks();
        }
    }

    public final void setEnableScale(boolean enabled) {
        this.f537a = enabled;
    }

    public final void setEnableDebugLog(boolean enable) {
        C0123G.f541a = enable;
    }

    public final void setEnableFastSave(boolean enable) {
        this.f538b = enable;
    }

    public final boolean isEnableFastSave() {
        return this.f538b;
    }

    public final void setEnableFullPathCacheKey(boolean enable) {
        this.f539c = enable;
    }

    public final boolean isEnableFullPathCacheKey() {
        return this.f539c;
    }

    public final void setDecodeConfig(Config config) {
        this.f536a = config;
    }

    public final void recycleBitmap(String url) {
        if (this.f535a != null) {
            if (!C0126b.m307a(url)) {
                FinalBitmap.create(this.f535a).setBitmapRecycle(url);
            }
        }
    }

    public final void setMemoryCacheSize(int size) {
        if (this.f535a != null) {
            FinalBitmap.create(this.f535a).configMemoryCacheSize(size);
        }
    }

    public final void setDiskCacheSize(int size) {
        if (this.f535a != null) {
            FinalBitmap.create(this.f535a).configDiskCacheSize(size);
        }
    }

    public final void setThreadSize(int size) {
        if (this.f535a != null) {
            FinalBitmap.create(this.f535a).configBitmapLoadThreadSize(size);
        }
    }

    public final void setDiskCacheCount(int count) {
        if (this.f535a != null) {
            FinalBitmap.create(this.f535a).configDiskCacheCount(count);
        }
    }

    public final void setBitmapPoolSize(int size) {
        if (this.f535a != null) {
            FinalBitmap.create(this.f535a).configBitmapPoolSize(size);
        }
    }
}
