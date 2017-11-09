package com.gala.video.app.epg.ui.ucenter.account.utils;

public class TabContainer {
    int imageId;
    int tag;
    int textId;

    public TabContainer(int imageId, int textId) {
        this.imageId = imageId;
        this.textId = textId;
    }

    public int getImageId() {
        return this.imageId;
    }

    public int getTextId() {
        return this.textId;
    }
}
