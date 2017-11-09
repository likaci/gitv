package com.gala.video.app.epg.ui.star.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ResourceUtil;

public class ProgressImage extends ImageView {
    public ProgressImage(Context context) {
        this(context, null);
    }

    public ProgressImage(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public ProgressImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setFocusable(false);
        if (Project.getInstance().getBuild().isLitchi() || Project.getInstance().getConfig().setAnimationInXml()) {
            setImageDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_new_anim_load_center));
            AnimationDrawable ad = (AnimationDrawable) getDrawable();
            if (ad != null) {
                ad.start();
                return;
            }
            return;
        }
        Animation animation = Project.getInstance().getControl().getLoadingViewAnimation();
        clearAnimation();
        setAnimation(animation);
        if (animation != null) {
            animation.startNow();
        }
    }
}
