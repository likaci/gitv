package com.gala.afinal.bitmap.display;

import android.graphics.Bitmap;
import android.view.View;
import com.gala.afinal.bitmap.core.BitmapDisplayConfig;

public interface Displayer {
    void loadCompletedisplay(View view, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig);

    void loadFailDisplay(View view, Bitmap bitmap);
}
