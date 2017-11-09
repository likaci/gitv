package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import com.gala.video.app.player.R;

public class GuideTip {
    private AlphaAnimation mAnim;
    private Context mContext;
    private ImageView mGuide;
    private ImageView mGuideflashy;

    public GuideTip(Context context) {
        this.mContext = context;
    }

    public void init(ViewGroup rootview) {
        if (this.mGuide == null) {
            ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.player_guide_tip, rootview);
            this.mGuide = (ImageView) rootview.findViewById(R.id.guide_tip);
            this.mGuideflashy = (ImageView) rootview.findViewById(R.id.guide_flashy);
            this.mGuide.setVisibility(8);
            initAnim();
        }
    }

    public void hide() {
        if (this.mGuide != null) {
            this.mGuide.setImageDrawable(null);
            this.mGuide.setVisibility(8);
            this.mGuideflashy.clearAnimation();
            this.mGuideflashy.setImageDrawable(null);
            this.mGuideflashy.setVisibility(8);
        }
    }

    public void show() {
        if (this.mGuide != null) {
            this.mGuide.setImageDrawable(this.mContext.getResources().getDrawable(R.drawable.player_guide_tip));
            this.mGuide.setVisibility(0);
            this.mGuideflashy.setImageDrawable(this.mContext.getResources().getDrawable(R.drawable.player_guide_flashy));
            this.mGuideflashy.setVisibility(0);
            if (this.mAnim != null) {
                this.mGuideflashy.setAnimation(this.mAnim);
                this.mAnim.start();
            }
        }
    }

    public void initAnim() {
        this.mAnim = new AlphaAnimation(1.0f, 0.0f);
        this.mAnim.setDuration(800);
        this.mAnim.setRepeatCount(-1);
        this.mAnim.setRepeatMode(1);
    }

    public boolean isShow() {
        if (this.mGuide == null || this.mGuide.getVisibility() != 0) {
            return false;
        }
        return true;
    }
}
