package com.gala.download.base;

import android.content.Context;
import java.util.List;

public interface IDownloader {
    String getLocalPath(FileRequest fileRequest);

    void initialize(Context context);

    void initialize(Context context, String str);

    boolean isEnableFastSave();

    boolean isEnableFullPathCacheKey();

    void loadFile(FileRequest fileRequest, IFileCallback iFileCallback);

    void loadFiles(List<FileRequest> list, IFileCallback iFileCallback);

    void loadGif(FileRequest fileRequest, IGifCallback iGifCallback);

    void loadGifs(List<FileRequest> list, IGifCallback iGifCallback);

    void setDiskCacheCount(int i);

    void setDiskCacheSize(int i);

    void setEnableFastSave(boolean z);

    void setEnableFullPathCacheKey(boolean z);

    void setGifLimitSize(int i);

    void setThreadSize(int i);

    void stopAllTasks();
}
