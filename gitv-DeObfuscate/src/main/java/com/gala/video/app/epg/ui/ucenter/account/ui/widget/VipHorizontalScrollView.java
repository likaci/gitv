package com.gala.video.app.epg.ui.ucenter.account.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;

public class VipHorizontalScrollView extends HorizontalScrollView {
    public static final int DEFAULT_DISTANCE = ((int) AppRuntimeEnv.get().getApplicationContext().getResources().getDimension(C0508R.dimen.dimen_100dp));
    private int mExtraDistance;

    public VipHorizontalScrollView(Context context) {
        super(context);
    }

    public VipHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VipHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        int result = super.computeScrollDeltaToGetChildRectOnScreen(rect);
        int extra = this.mExtraDistance < 0 ? DEFAULT_DISTANCE : this.mExtraDistance;
        if (result == 0) {
            return result;
        }
        if (result > 0) {
            return result + extra;
        }
        return result - extra;
    }

    public void setExtraDistance(int mExtraDistance) {
        this.mExtraDistance = mExtraDistance;
    }
}
