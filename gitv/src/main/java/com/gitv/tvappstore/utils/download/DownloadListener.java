package com.gitv.tvappstore.utils.download;

public interface DownloadListener {
    void onComplete(int i, String str, String str2);

    void onPause();

    void onProgress(int i);

    void onStart();

    void onStop();
}
