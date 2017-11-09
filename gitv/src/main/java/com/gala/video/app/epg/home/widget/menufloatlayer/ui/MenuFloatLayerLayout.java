package com.gala.video.app.epg.home.widget.menufloatlayer.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.widget.menufloatlayer.adapter.MenuFloatLayerStateListener;

public class MenuFloatLayerLayout extends RelativeLayout implements OnFocusChangeListener, OnClickListener {
    private static final String TAG = "home/widget/MenuFloatLayerLayout";
    private MenuFloatLayerOnClickCallBack mClickCallBack;
    private Context mContext;
    private View mCurrentFocusedView;
    private LinearLayout mHorizontalScrollView;
    private MenuFloatLayerOnKeyEventCallBack mKeyEventCallBack;
    private long mLastSpringBackAnimationTime;
    private MenuFloatLayerStateListener mMenuFloatLayerStateListener;

    public MenuFloatLayerLayout(Context context) {
        super(context);
        this.mHorizontalScrollView = null;
        this.mKeyEventCallBack = null;
        this.mClickCallBack = null;
        this.mMenuFloatLayerStateListener = null;
        this.mLastSpringBackAnimationTime = 0;
        initView(context);
    }

    public MenuFloatLayerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuFloatLayerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mHorizontalScrollView = null;
        this.mKeyEventCallBack = null;
        this.mClickCallBack = null;
        this.mMenuFloatLayerStateListener = null;
        this.mLastSpringBackAnimationTime = 0;
        initView(context);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        this.mKeyEventCallBack.onKeyEvent(event);
        dealWithSpringBackAnimation(this.mCurrentFocusedView, event);
        return super.dispatchKeyEvent(event);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        this.mMenuFloatLayerStateListener.onChildFocusChanged(v, hasFocus);
        this.mCurrentFocusedView = v;
    }

    public void onClick(View v) {
        this.mClickCallBack.onClick(v);
        this.mMenuFloatLayerStateListener.onClick(v);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.epg_home_menu_float_layer, this, true);
        this.mContext = context;
        this.mHorizontalScrollView = (LinearLayout) findViewById(R.id.epg_home_menu_float_layer_horizontal_scrollview);
    }

    public void setOnKeyEventCallBack(MenuFloatLayerOnKeyEventCallBack callBack) {
        this.mKeyEventCallBack = callBack;
    }

    public void setOnClickEventCallBack(MenuFloatLayerOnClickCallBack callBack) {
        this.mClickCallBack = callBack;
    }

    private void dealWithSpringBackAnimation(View v, KeyEvent event) {
        if (v != null) {
            if (event.getAction() == 0 && event.getKeyCode() == 22) {
                if (this.mHorizontalScrollView.indexOfChild(v) == this.mHorizontalScrollView.getChildCount() - 1) {
                    startSpringBackAnimation(v);
                }
            } else if (event.getAction() == 0 && event.getKeyCode() == 21 && this.mHorizontalScrollView.indexOfChild(v) == 0) {
                startSpringBackAnimation(v);
            }
        }
    }

    private void startSpringBackAnimation(View view) {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastSpringBackAnimationTime > 500) {
            view.startAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.epg_shake));
            this.mLastSpringBackAnimationTime = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public void setAdapter(MenuFloatLayerStateListener listener) {
        this.mMenuFloatLayerStateListener = listener;
        for (int i = 0; i < listener.getCount(); i++) {
            View itemView = listener.getView(i, null, null);
            LayoutParams layoutParams = (LayoutParams) itemView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.dimen_197dp), getResources().getDimensionPixelSize(R.dimen.dimen_197dp));
            }
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.dimen_197dp);
            layoutParams.width = getResources().getDimensionPixelSize(R.dimen.dimen_197dp);
            layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.dimen_3dp);
            this.mHorizontalScrollView.addView(itemView, layoutParams);
            itemView.setOnClickListener(this);
            itemView.setOnFocusChangeListener(this);
        }
        this.mHorizontalScrollView.requestFocus();
    }
}
