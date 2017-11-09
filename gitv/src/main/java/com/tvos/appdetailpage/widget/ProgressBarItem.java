package com.tvos.appdetailpage.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.tvos.appdetailpage.utils.CommonUtils;
import com.tvos.appdetailpage.utils.ResourcesUtils;

public class ProgressBarItem extends FrameLayout {
    private TextView itemTextView;
    private ImageView progessBarImage;

    public ProgressBarItem(Context context) {
        super(context);
        init(context);
    }

    public ProgressBarItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressBarItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View progressbarView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(getResId("layout", "apps_progressbar_item"), null);
        addView(progressbarView);
        this.itemTextView = (TextView) progressbarView.findViewById(getResId("id", "apps_progressbar_tagtext_id"));
        this.progessBarImage = (ImageView) progressbarView.findViewById(getResId("id", "apps_progressbar_item_image_id"));
        AnimationDrawable ad = (AnimationDrawable) this.progessBarImage.getDrawable();
        if (ad != null) {
            ad.start();
        }
    }

    public void setText(String itemText) {
        if (itemText != null) {
            this.itemTextView.setText(itemText);
        }
    }

    public void setTextSize(int deminId) {
        CommonUtils.setTextViewTextSize(getContext(), this.itemTextView, deminId);
    }

    private int getResId(String className, String name) {
        return ResourcesUtils.getResourceId(getContext(), className, name);
    }
}
