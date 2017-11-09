package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import com.gala.sdk.player.IThreeDimensional;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class EnhancedImageView extends ImageView implements IThreeDimensional, ThreeDimensionalParams {
    private static final String TAG = "Player/Ui/EnhancedImageView";
    private Context mContext;

    public EnhancedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public EnhancedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public EnhancedImageView(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setThreeDimensional(boolean enable) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setThreeDimensional(" + enable + ")");
        }
        int width = this.mContext.getResources().getDimensionPixelSize(R.dimen.gala_logo_width);
        int height = this.mContext.getResources().getDimensionPixelSize(R.dimen.gala_logo_height);
        LayoutParams lp = getLayoutParams();
        if (enable) {
            lp.width = width;
            lp.height = height;
        } else {
            lp.width = width;
            lp.height = height;
        }
        setLayoutParams(lp);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setThreeDimensional lp.width = " + getLayoutParams().width);
        }
    }
}
