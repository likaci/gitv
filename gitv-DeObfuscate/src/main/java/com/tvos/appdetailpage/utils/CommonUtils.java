package com.tvos.appdetailpage.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.TextView;
import java.util.List;

public class CommonUtils {
    public static boolean isListEmpty(List<?> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    public static Bitmap decodeResourceToBitmap(Context context, String resourceName) {
        if (context == null) {
            return null;
        }
        int resourceId = ResourcesUtils.getResourceId(context, "drawable", resourceName);
        if (resourceId == 0) {
            return null;
        }
        new Options().inJustDecodeBounds = true;
        return BitmapFactory.decodeResource(context.getResources(), resourceId);
    }

    public static void setTextViewTextSize(Context context, TextView textView, int dimenId) {
        if (context != null && textView != null) {
            textView.setTextSize(0, (float) context.getResources().getDimensionPixelSize(dimenId));
        }
    }
}
