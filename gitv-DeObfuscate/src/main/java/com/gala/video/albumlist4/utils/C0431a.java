package com.gala.video.albumlist4.utils;

import android.content.Context;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;

public class C0431a {
    public static int m1212a(Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().density * f) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
    }
}
