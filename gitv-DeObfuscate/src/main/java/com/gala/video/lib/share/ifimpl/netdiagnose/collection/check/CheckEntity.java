package com.gala.video.lib.share.ifimpl.netdiagnose.collection.check;

import com.gala.tvapi.tv2.model.Album;

public class CheckEntity {
    private Album mAlbum;
    private String mQipu;
    private StringBuffer mStringBuffer = new StringBuffer();

    public String getQipu() {
        return this.mQipu;
    }

    public void setQipu(String mQipu) {
        this.mQipu = mQipu;
    }

    public StringBuffer getStringBuffer() {
        return this.mStringBuffer;
    }

    public void setStringBuffer(StringBuffer stringBuffer) {
        this.mStringBuffer = stringBuffer;
    }

    public Album getAlbum() {
        return this.mAlbum;
    }

    public void setAlbum(Album album) {
        this.mAlbum = album;
    }

    public void add(String result) {
        this.mStringBuffer.append(result + "\n");
    }
}
