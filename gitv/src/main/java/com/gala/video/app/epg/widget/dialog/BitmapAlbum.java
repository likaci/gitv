package com.gala.video.app.epg.widget.dialog;

import android.graphics.Bitmap;
import com.gala.tvapi.tv2.model.Album;

public class BitmapAlbum {
    private Album mAlbum;
    private Bitmap mBitmap;

    public Album getAlbum() {
        return this.mAlbum;
    }

    public void setAlbum(Album album) {
        this.mAlbum = album;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }
}
