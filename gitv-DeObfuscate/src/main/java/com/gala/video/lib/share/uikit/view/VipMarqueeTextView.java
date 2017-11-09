package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class VipMarqueeTextView extends TextView {
    public VipMarqueeTextView(Context context) {
        this(context, null);
    }

    public VipMarqueeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VipMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
