package com.gala.video.lib.share.common.model.player;

import com.gala.video.lib.share.common.model.TabDataItem;
import java.io.Serializable;
import java.util.List;

public class NewsParams implements Serializable {
    private int mPlayingItemIndex;
    private List<TabDataItem> mTabLabelList;

    public NewsParams(List<TabDataItem> list, int playingIndex) {
        this.mTabLabelList = list;
        this.mPlayingItemIndex = playingIndex;
    }

    public List<TabDataItem> getTabLabelList() {
        return this.mTabLabelList;
    }

    public int getPlaytingItemIndex() {
        return this.mPlayingItemIndex;
    }

    public String toString() {
        return "NewsParams[mPlayingItemIndex=" + this.mPlayingItemIndex + ",mTabLabelList=" + this.mTabLabelList;
    }
}
