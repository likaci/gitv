package com.gala.video.app.epg.ui.applist.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;

public class AppMenuItem extends FrameLayout {
    private ImageView mIcon;
    private TextView mText;

    public AppMenuItem(Context context) {
        super(context);
        init(context);
    }

    public AppMenuItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public AppMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint({"InflateParams"})
    private void init(Context context) {
        View view = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0508R.layout.epg_app_menu_item, null);
        RelativeLayout mLayout = (RelativeLayout) view.findViewById(C0508R.id.epg_app_menu_layout);
        this.mIcon = (ImageView) mLayout.findViewById(C0508R.id.epg_app_menu_icon);
        this.mText = (TextView) mLayout.findViewById(C0508R.id.epg_app_menu_text);
        this.mText.setShadowLayer(8.0f, 0.0f, 4.0f, 1711276032);
        addView(view);
    }

    public void setImageResource(int resId) {
        if (this.mIcon != null) {
            this.mIcon.setImageResource(resId);
        }
    }

    public void setText(String string) {
        if (this.mText != null) {
            this.mText.setText(string);
        }
    }
}
