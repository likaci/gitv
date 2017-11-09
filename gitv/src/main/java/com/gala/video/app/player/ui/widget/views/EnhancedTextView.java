package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.gala.sdk.player.IThreeDimensional;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class EnhancedTextView extends TextView implements IThreeDimensional, ThreeDimensionalParams {
    private static final String TAG = "Player/Ui/EnhancedTextView";

    public EnhancedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EnhancedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EnhancedTextView(Context context) {
        super(context);
    }

    public void setThreeDimensional(boolean enable) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setThreeDimensional(" + enable + ")");
        }
        setTextScaleX(enable ? ThreeDimensionalParams.TEXT_SCALE_FOR_3D : 1.0f);
    }
}
