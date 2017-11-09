package com.gala.video.lib.share.ifmanager.bussnessIF.epg.data;

public abstract class DataSource {
    protected String mImage;
    protected String mTitle;

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getImage() {
        return this.mImage;
    }
}
