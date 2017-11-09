package com.gala.afinal.bitmap.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.imageprovider.p000private.C0123G;
import java.io.FileDescriptor;

public class BitmapDecoder {
    public static final String LOG_TAG = "ImageProvider/BitmapDecoder";

    private BitmapDecoder() {
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = m6a(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeResource(res, resId, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, bitmap, options);
        options.inSampleSize = m6a(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try {
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int offset, int length, int reqWidth, int reqHeight, ImageRequest imageRequest) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        options.inSampleSize = m6a(options, reqWidth, reqHeight);
        if (options.inSampleSize != 1) {
            C0123G.m279a(LOG_TAG, ">>>>>【afinal】, inSampleSize is " + options.inSampleSize + ", [width,height]-" + reqWidth + "," + reqHeight + ", [oriWidth, oriHeight]-" + options.outWidth + ", " + options.outHeight + ", [ScaleType]-" + imageRequest.getScaleType() + ", [url]-" + imageRequest.getUrl());
        }
        options.inMutable = true;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = imageRequest.getDecodeConfig();
        options.inDither = true;
        options.inBitmap = LruBitmapPoolImpl.get().getBitmap(options);
        try {
            return BitmapFactory.decodeByteArray(data, offset, length, options);
        } catch (IllegalArgumentException e) {
            if (options.inBitmap == null) {
                throw e;
            }
            options.inBitmap = null;
            return BitmapFactory.decodeByteArray(data, offset, length, options);
        }
    }

    private static int m6a(Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i > 0 && i2 > 0 && (i3 > i2 || i4 > i)) {
            if (i4 > i3) {
                i5 = Math.round(((float) i3) / ((float) i2));
            } else {
                i5 = Math.round(((float) i4) / ((float) i));
            }
            float f = (float) (i3 * i4);
            while (f / ((float) (i5 * i5)) > ((float) ((i * i2) << 1))) {
                i5++;
            }
        }
        return i5;
    }
}
