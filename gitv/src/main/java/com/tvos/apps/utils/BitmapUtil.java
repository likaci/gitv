package com.tvos.apps.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import java.io.ByteArrayOutputStream;

public class BitmapUtil {
    public static byte[] BitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        byte[] data = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    public static Bitmap byteToBitmap(byte[] base64Bytes) {
        return BitmapFactory.decodeByteArray(base64Bytes, 0, base64Bytes.length);
    }

    public static byte[] drawableToBytes(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        try {
            Config config;
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            if (drawable.getOpacity() != -1) {
                config = Config.ARGB_8888;
            } else {
                config = Config.RGB_565;
            }
            Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
