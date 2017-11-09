package com.gala.video.albumlist4.utils;

import android.content.Context;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;

public class a {
    public static int a(Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().density * f) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
    }
}
