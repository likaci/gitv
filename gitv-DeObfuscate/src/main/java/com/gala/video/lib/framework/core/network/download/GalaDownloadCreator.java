package com.gala.video.lib.framework.core.network.download;

import com.gala.video.lib.framework.core.network.download.core.GalaFileDownloader;
import com.gala.video.lib.framework.core.network.download.core.IGalaDownloader;

public class GalaDownloadCreator {
    public static IGalaDownloader createFileDownloader() {
        return new GalaFileDownloader();
    }
}
