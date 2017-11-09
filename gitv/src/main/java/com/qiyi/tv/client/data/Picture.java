package com.qiyi.tv.client.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.Serializable;
import java.net.URL;

public class Picture implements Serializable {
    private static final long serialVersionUID = 1;
    private String mUrl;

    public Picture(String url) {
        this.mUrl = url;
    }

    public Bitmap getBitmap() {
        try {
            return BitmapFactory.decodeStream(new URL(this.mUrl).openStream());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUrl() {
        return this.mUrl;
    }
}
