package com.gala.download;

import com.gala.download.base.IDownloader;
import com.gala.imageprovider.private.q;

public class DownloaderAPI {
    public static IDownloader getDownloader() {
        return q.a();
    }

    public static void setChacheFile(boolean z) {
        q.a().a();
    }
}
