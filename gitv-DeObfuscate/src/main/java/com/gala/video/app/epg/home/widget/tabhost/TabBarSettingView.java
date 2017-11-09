package com.gala.video.app.epg.home.widget.tabhost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.utils.HomeItemUtils;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarAnimaitonUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.utils.AnimationUtil;

public class TabBarSettingView extends FrameLayout implements OnFocusChangeListener, OnClickListener {
    private static final String TAG = "home/TabBarSettingView";
    private Context mContext;
    private OnFocusChangeListener mFocusChangeListener;
    private ImageView mImageView;
    private long mLastAnimationX = 0;

    public TabBarSettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabBarSettingView(Context context) {
        super(context);
        init(context);
    }

    public TabBarSettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public View getTextView() {
        return this.mImageView;
    }

    public void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(C0508R.layout.epg_home_tab_setting, this, true);
        this.mImageView = (ImageView) findViewById(C0508R.id.epg_home_tab_setting_name);
        this.mImageView.setBackgroundResource(C0508R.drawable.epg_tab_manager_unfocus);
        this.mImageView.setOnClickListener(this);
        this.mImageView.setOnFocusChangeListener(this);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        this.mFocusChangeListener = listener;
    }

    public void updateTextColor() {
        AppClientUtils.setBackgroundDrawable(this.mImageView, getResources().getDrawable(C0508R.drawable.epg_tab_manager_unfocus));
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    private boolean executeKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return false;
        }
        switch (event.getKeyCode()) {
            case 22:
                doBounceAnimation(findFocus());
                return true;
            default:
                return false;
        }
    }

    private void doBounceAnimation(View view) {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastAnimationX > 500) {
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), C0508R.anim.epg_shake));
            this.mLastAnimationX = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == this.mImageView.getId()) {
            if (hasFocus) {
                AppClientUtils.setBackgroundDrawable(this.mImageView, getResources().getDrawable(C0508R.drawable.epg_tab_manager_focus));
            } else {
                AppClientUtils.setBackgroundDrawable(this.mImageView, getResources().getDrawable(C0508R.drawable.epg_tab_manager_unfocus));
            }
            AnimationUtil.zoomAnimation(v, hasFocus, 1.1f, ActionBarAnimaitonUtils.getDelay());
            if (this.mFocusChangeListener != null) {
                this.mFocusChangeListener.onFocusChange(v, hasFocus);
            }
        }
    }

    public void onClick(View v) {
        if (v.getId() == this.mImageView.getId()) {
            HomeItemUtils.onTabSettingClick(this.mContext, "");
            HomePingbackFactory.instance().createPingback(ClickPingback.TAB_BAR_CLICK_PINGBACK).addItem("r", "tab_桌面管理").addItem("rpage", "tab_桌面管理").addItem("block", "tab").addItem("rt", "i").addItem("count", "").addItem("rseat", "tab_桌面管理").setOthersNull().post();
        }
    }
}
