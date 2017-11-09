package com.gala.video.app.epg.ui.albumlist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.gala.video.app.epg.C0508R;

public class DeleteClearMenuItem extends FrameLayout {
    public DeleteClearMenuItem(Context context) {
        super(context);
        init(context);
    }

    public DeleteClearMenuItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DeleteClearMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        addView(((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0508R.layout.epg_q_delete_clear_menu_item, null));
    }
}
