package com.gala.video.test;

public class GalleryData {
    private int mId;
    private String mName;

    public GalleryData(int id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public int getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }
}
