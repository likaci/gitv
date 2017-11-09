package com.squareup.picasso;

import android.graphics.Bitmap;
import com.squareup.picasso.Picasso.LoadedFrom;

class FetchAction extends Action<Void> {
    FetchAction(Picasso picasso, Request data, boolean skipCache, String key) {
        super(picasso, null, data, skipCache, false, 0, null, key);
    }

    void complete(Bitmap result, LoadedFrom from) {
    }

    public void error() {
    }
}
