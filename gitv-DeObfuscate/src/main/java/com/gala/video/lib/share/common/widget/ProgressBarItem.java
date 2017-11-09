package com.gala.video.lib.share.common.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ResourceUtil;

public class ProgressBarItem extends FrameLayout {
    private TextView itemTextView;
    private RelativeLayout mLoadingBg;
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

    private int getLayoutId() {
        return C1632R.layout.share_progressbar_item;
    }

    private void init(Context context) {
        View progressbarView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(getLayoutId(), null);
        addView(progressbarView);
        this.mLoadingBg = (RelativeLayout) progressbarView.findViewById(C1632R.id.share_loading_bg);
        this.itemTextView = (TextView) progressbarView.findViewById(C1632R.id.share_progressbar_tagtext_id);
        this.progessBarImage = (ImageView) progressbarView.findViewById(C1632R.id.share_progressbar_item_image_id);
        if (Project.getInstance().getBuild().isLitchi() || Project.getInstance().getConfig().setAnimationInXml()) {
            this.progessBarImage.setImageDrawable(ResourceUtil.getDrawable(C1632R.drawable.share_new_anim_load_center));
            AnimationDrawable ad = (AnimationDrawable) this.progessBarImage.getDrawable();
            if (ad != null) {
                ad.start();
                return;
            }
            return;
        }
        Animation animation = Project.getInstance().getControl().getLoadingViewAnimation();
        this.progessBarImage.clearAnimation();
        this.progessBarImage.setAnimation(animation);
        if (animation != null) {
            animation.startNow();
        }
    }

    public void setText(String itemText) {
        if (itemText != null) {
            this.itemTextView.setText(itemText);
        }
    }

    public void setTextColor(int color) {
        if (this.itemTextView != null) {
            this.itemTextView.setTextColor(color);
        }
    }

    public void setLoadingBackgroudResource(int resId) {
        this.mLoadingBg.setBackgroundResource(resId);
    }
}
