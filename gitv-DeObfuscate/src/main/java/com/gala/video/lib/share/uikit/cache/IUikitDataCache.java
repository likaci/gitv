package com.gala.video.lib.share.uikit.cache;

import com.gala.video.lib.share.uikit.loader.UikitEvent;

public interface IUikitDataCache {
    void onGetEvent(UikitEvent uikitEvent);

    void onPostEvent(UikitEvent uikitEvent);

    void register();
}
