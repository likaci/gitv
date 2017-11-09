package com.gala.imageprovider.base;

import android.content.Context;
import android.graphics.Bitmap.Config;
import java.util.List;

public interface IImageProvider {
    void initialize(Context context);

    void initialize(Context context, String str);

    boolean isEnableFastSave();

    boolean isEnableFullPathCacheKey();

    void loadImage(ImageRequest imageRequest, IImageCallback iImageCallback);

    void loadImageFromFile(ImageRequest imageRequest, IImageCallback iImageCallback);

    void loadImages(List<ImageRequest> list, IImageCallback iImageCallback);

    void recycleBitmap(String str);

    void setBitmapPoolSize(int i);

    void setDecodeConfig(Config config);

    void setDiskCacheCount(int i);

    void setDiskCacheSize(int i);

    void setEnableDebugLog(boolean z);

    void setEnableFastSave(boolean z);

    void setEnableFullPathCacheKey(boolean z);

    void setEnableScale(boolean z);

    void setMemoryCacheSize(int i);

    void setThreadSize(int i);

    void stopAllTasks();
}
