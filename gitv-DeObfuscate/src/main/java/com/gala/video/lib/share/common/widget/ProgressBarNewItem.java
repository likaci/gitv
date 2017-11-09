package com.gala.video.lib.share.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.gala.video.lib.share.C1632R;

public class ProgressBarNewItem extends FrameLayout {
    private ImageView progessBarImage;

    public ProgressBarNewItem(Context context) {
        super(context);
        init(context);
    }

    public ProgressBarNewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressBarNewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View progressbarView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C1632R.layout.share_progressbar_newitem, null);
        addView(progressbarView);
        this.progessBarImage = (ImageView) progressbarView.findViewById(C1632R.id.share_progressbar_item_image_id);
        Animation animation = AnimationUtils.loadAnimation(context, C1632R.anim.share_progress_rotate_anim);
        animation.setInterpolator(new LinearInterpolator());
        this.progessBarImage.startAnimation(animation);
    }
}
