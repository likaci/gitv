package com.gala.video.app.epg.screensaver.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import com.gala.video.lib.share.utils.MemoryLevelInfo;

public class BitmapUtil {
    private static final int MAX_IMAGE_DIMENSION = 2073600;

    public Bitmap getBitmapFromPath(String absolutePath) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(absolutePath, options);
        if (options.outWidth * options.outHeight > 2073600) {
            options.inSampleSize = 2;
        } else if (MemoryLevelInfo.isLowMemoryDevice() && options.outWidth * options.outHeight == 2073600) {
            options.inSampleSize = 2;
            options.inDither = true;
        }
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeFile(absolutePath, options);
    }
}
