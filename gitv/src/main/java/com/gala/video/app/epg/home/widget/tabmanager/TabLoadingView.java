package com.gala.video.app.epg.home.widget.tabmanager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import com.gala.video.app.epg.R;

public class TabLoadingView extends ImageView {
    public static final int LOADING = -1;
    private Animation animation;
    private Context mContext;

    public TabLoadingView(Context context) {
        super(context);
        init(context);
    }

    public TabLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setBackgroundResource(R.drawable.epg_loading_skin_switch);
    }

    public void startAnimation() {
        if (this.animation == null) {
            this.animation = AnimationUtils.loadAnimation(this.mContext, R.anim.share_progress_rotate_anim);
            this.animation.setInterpolator(new LinearInterpolator());
        } else {
            this.animation.reset();
        }
        startAnimation(this.animation);
    }

    public void stopAnimation() {
        if (this.animation != null) {
            this.animation.cancel();
            clearAnimation();
        }
    }
}
