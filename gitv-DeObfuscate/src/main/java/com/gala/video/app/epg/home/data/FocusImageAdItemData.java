package com.gala.video.app.epg.home.data;

public class FocusImageAdItemData extends AdItemData {
    private static final String TAG = "data/FocusImageAdItemData";
    private int adIndex;

    public int getAdIndex() {
        return this.adIndex;
    }

    public void setAdIndex(int adIndex) {
        this.adIndex = adIndex;
    }

    public String toString() {
        return "FocusImageAdItemData{adIndex=" + this.adIndex + '}';
    }
}
