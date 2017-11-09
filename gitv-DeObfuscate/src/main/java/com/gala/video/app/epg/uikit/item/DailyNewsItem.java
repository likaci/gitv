package com.gala.video.app.epg.uikit.item;

import com.gala.video.lib.share.uikit.contract.DailyNewsItemContract.Presenter;
import com.gala.video.lib.share.uikit.item.Item;

public class DailyNewsItem extends Item implements Presenter {
    public void registerDataUpdateObserver() {
    }

    public void unregisterDataUpdateObserver() {
    }

    public int getType() {
        return 216;
    }
}
