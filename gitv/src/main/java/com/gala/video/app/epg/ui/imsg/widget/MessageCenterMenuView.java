package com.gala.video.app.epg.ui.imsg.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.gala.video.app.epg.R;
import com.gala.video.lib.share.utils.ResourceUtil;

public class MessageCenterMenuView extends RelativeLayout {
    private View mCoreView;

    public MessageCenterMenuView(Context context) {
        super(context);
        init(context);
    }

    public MessageCenterMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MessageCenterMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mCoreView = LayoutInflater.from(context).inflate(R.layout.epg_message_center_menu, null);
        int dimen = ResourceUtil.getDimen(R.dimen.dimen_280dp);
        LayoutParams layoutParams = new LayoutParams(dimen, dimen);
        layoutParams.addRule(13);
        this.mCoreView.setLayoutParams(layoutParams);
        addView(this.mCoreView);
    }

    public void setOnClickListener(OnClickListener l) {
        if (this.mCoreView != null) {
            this.mCoreView.setOnClickListener(l);
        }
    }
}
