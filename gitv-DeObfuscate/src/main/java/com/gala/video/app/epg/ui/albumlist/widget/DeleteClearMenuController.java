package com.gala.video.app.epg.ui.albumlist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class DeleteClearMenuController extends RelativeLayout {
    public DeleteClearMenuController(Context context) {
        super(context);
        init();
    }

    public DeleteClearMenuController(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DeleteClearMenuController(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.setChildrenDrawingOrderEnabled(true);
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        View focusView = getFocusedChild();
        int focusPosition = -1;
        for (int index = 0; index < childCount; index++) {
            if (getChildAt(index) == focusView) {
                focusPosition = index;
                break;
            }
        }
        if (focusPosition < 0 || i < focusPosition || i < focusPosition) {
            return i;
        }
        return ((childCount - 1) - i) + focusPosition;
    }
}
