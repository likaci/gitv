package com.gala.video.lib.framework.core.network.download.core;

import com.gala.video.lib.framework.core.network.download.GalaDownloadException;
import com.gala.video.lib.framework.core.network.download.IGalaDownloadListener;

public abstract class GalaDownloadListener implements IGalaDownloadListener {
    public void onStart() {
    }

    public void onSuccess(Object file, String path) {
    }

    public void onProgress(long download, long fileSize) {
    }

    public void onError(GalaDownloadException e) {
    }

    public void onCancel() {
    }

    public void onExisted(String path) {
    }
}
