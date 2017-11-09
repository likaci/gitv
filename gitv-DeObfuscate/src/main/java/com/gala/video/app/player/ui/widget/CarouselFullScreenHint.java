package com.gala.video.app.player.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.gala.sdk.player.FullScreenHintType;
import com.gala.sdk.player.OnFullScreenHintChangedListener;
import com.gala.video.app.player.C1291R;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class CarouselFullScreenHint extends AbsFullScreenHint {
    protected static final int SHOW_DURATION = 10000;
    protected static final String TAG = "Player/Ui/CarouselFullScreenHint";
    private FullScreenHintType mHintType;
    private OnFullScreenHintChangedListener mListener;

    class C15171 implements OnClickListener {
        C15171() {
        }

        public void onClick(View v) {
            LogUtils.m1568d(CarouselFullScreenHint.TAG, "onClick");
            CarouselFullScreenHint.this.dismissHint(v);
        }
    }

    public CarouselFullScreenHint(Context context) {
        super(context);
        initView();
    }

    public CarouselFullScreenHint(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CarouselFullScreenHint(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public void initView() {
        super.initView();
    }

    public void show(FullScreenHintType hintType) {
        LogUtils.m1574i(TAG, "show FullScreenHintType");
        this.mHintType = hintType;
        ((ImageView) this.mContent).setImageResource(C1291R.drawable.player_carousel_indirection);
        setBackgroundColor(0);
        this.mContainer.setVisibility(0);
        setVisibility(0);
    }

    protected void hide() {
        super.hide();
        clearFocus();
        setVisibility(8);
    }

    protected void onFinishInflate() {
        this.mContent.setFocusable(true);
        this.mContent.setOnClickListener(new C15171());
        super.onFinishInflate();
    }

    public void setHintListener(OnFullScreenHintChangedListener listener) {
        LogUtils.m1568d(TAG, "setHintListener: " + listener);
        this.mListener = listener;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtils.m1574i(TAG, "dispatchKeyEvent event = " + event);
        if (isShown()) {
            boolean isFirstDownEvent;
            int keyCode = event.getKeyCode();
            if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                isFirstDownEvent = true;
            } else {
                isFirstDownEvent = false;
            }
            if (isFirstDownEvent) {
                switch (keyCode) {
                    case 24:
                    case 25:
                    case 164:
                        break;
                    default:
                        dismissHint(null);
                        break;
                }
            }
        }
        return false;
    }

    public void dismissHint(View v) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "dismissHint()");
        }
        if (getVisibility() == 0) {
            hide();
        }
    }

    public void clearBackgroundBitmap() {
        if (this.mContent == null) {
            this.mContent = findViewById(C1291R.id.fullscreen_hint_content);
        }
        LogUtils.m1568d(TAG, "clearBackgroundBitmap: content view={" + this.mContent + "}");
        if (this.mContent instanceof ImageView) {
            ((ImageView) this.mContent).setImageBitmap(null);
        }
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        switch (visibility) {
            case 0:
                if (this.mListener != null) {
                    this.mListener.onHintShown(this);
                    break;
                }
                break;
            case 4:
            case 8:
                if (this.mListener != null) {
                    this.mListener.onHintDismissed(this);
                    break;
                }
                break;
        }
        LogUtils.m1568d(TAG, "onVisibilityChanged() onVisibilityChanged=" + visibility + ",mListener=" + this.mListener);
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        if (!isFullScreen) {
        }
    }
}
