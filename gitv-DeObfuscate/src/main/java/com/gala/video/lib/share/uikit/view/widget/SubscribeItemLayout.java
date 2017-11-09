package com.gala.video.lib.share.uikit.view.widget;

import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import com.mcto.ads.internal.net.SendFlag;

public class SubscribeItemLayout extends LinearLayout {
    public SubscribeItemLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setChildrenDrawingOrderEnabled(true);
        setGravity(1);
        setOrientation(1);
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        OnFocusChangeListener listener = getOnFocusChangeListener();
        if (listener != null) {
            listener.onFocusChange(this, true);
        }
    }

    public void bringChildToFront(View child) {
        invalidate();
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        if (hasFocus() && !isFocused()) {
            int top = indexOfChild(getFocusedChild());
            if (i == childCount - 1) {
                return top;
            }
            if (i >= top) {
                return i + 1;
            }
        }
        return i;
    }
}
