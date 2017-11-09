package com.gala.video.app.epg.init.task;

import com.gala.video.lib.share.uikit.data.data.cache.LayoutCache;

public class LayoutCacheInitTask implements Runnable {
    public void run() {
        LayoutCache.getInstance().getCard();
    }
}
