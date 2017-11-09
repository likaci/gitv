package com.gala.video.lib.framework.core.network.download.core;

import com.gala.video.lib.framework.core.network.download.IGalaDownloadListener;

public interface IGalaDownloader {
    void callAsync(IGalaDownloadListener iGalaDownloadListener);

    void callSync(IGalaDownloadListener iGalaDownloadListener);

    void cancel();

    IGalaDownloadParameter getDownloadParameter();

    boolean isDownloading();
}
