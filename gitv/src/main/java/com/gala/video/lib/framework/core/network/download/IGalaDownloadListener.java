package com.gala.video.lib.framework.core.network.download;

public interface IGalaDownloadListener<T> {
    void onCancel();

    void onError(GalaDownloadException galaDownloadException);

    void onExisted(String str);

    void onProgress(long j, long j2);

    void onStart();

    void onSuccess(T t, String str);
}
