package com.gala.video.lib.share.utils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class NineDrawableUtils {
    public static int calNinePatchBorder(Context context, int drawableId) {
        return calNinePatchBorder(context, context.getResources().getDrawable(drawableId));
    }

    public static int calNinePatchBorder(Context context, Drawable drawable) {
        NinePatchDrawable nineDrawable = (NinePatchDrawable) drawable;
        Rect padding = new Rect();
        nineDrawable.getPadding(padding);
        return padding.top;
    }
}
