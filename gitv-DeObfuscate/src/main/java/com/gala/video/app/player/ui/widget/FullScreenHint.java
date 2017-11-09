package com.gala.video.app.player.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import com.gala.sdk.player.FullScreenHintType;
import com.gala.sdk.player.OnFullScreenHintChangedListener;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class FullScreenHint extends AbsFullScreenHint {
    protected static final String TAG = "Player/Ui/FullScreenHint";
    private FullScreenHintType mHintType;
    private OnFullScreenHintChangedListener mListener;

    class C15301 implements OnClickListener {
        C15301() {
        }

        public void onClick(View v) {
            LogUtils.m1568d(FullScreenHint.TAG, "onClick");
            if (FullScreenHint.this.mHintType != FullScreenHintType.LIVE) {
                FullScreenHint.this.dismissHint(v);
            }
        }
    }

    public FullScreenHint(Context context) {
        super(context);
        initView();
    }

    public FullScreenHint(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FullScreenHint(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public void initView() {
        super.initView();
        this.mContent.setFocusable(true);
        this.mContent.setOnClickListener(new C15301());
    }

    public void show(FullScreenHintType hintType) {
        super.show(hintType);
        LogUtils.m1574i(TAG, "show FullScreenHintType hintType=" + hintType);
        this.mHintType = hintType;
        if (this.mContent instanceof ImageView) {
            if (hintType == FullScreenHintType.THREE_DIMENSIONAL) {
                ((ImageView) this.mContent).setImageResource(PlayerAppConfig.get3dFullScreenHintBgResId());
                setBackgroundColor(0);
            } else if (hintType == FullScreenHintType.LIVE) {
                ((ImageView) this.mContent).setImageResource(C1291R.drawable.player_bg_fullscreen_live_nosignal);
                LayoutParams params = (LayoutParams) this.mContent.getLayoutParams();
                params.width = -1;
                params.height = -1;
                this.mContent.setLayoutParams(params);
                setBackgroundColor(1907745);
            }
        }
        this.mContainer.setVisibility(0);
        setVisibility(0);
    }

    protected void hide() {
        super.hide();
        LogUtils.m1568d(TAG, "hide()");
        clearFocus();
        setVisibility(8);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setHintListener(OnFullScreenHintChangedListener listener) {
        LogUtils.m1568d(TAG, "setHintListener: " + listener);
        this.mListener = listener;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtils.m1574i(TAG, "dispatchKeyEvent event = " + event);
        if (!isShown()) {
            return false;
        }
        int keyCode = event.getKeyCode();
        int action = event.getAction();
        switch (keyCode) {
            case 4:
                if (action == 0 && this.mHintType == FullScreenHintType.THREE_DIMENSIONAL) {
                    dismissHint(null);
                    return true;
                } else if (this.mHintType == FullScreenHintType.LIVE) {
                    return false;
                }
                break;
            case 23:
            case 66:
            case 85:
                break;
        }
        if (action == 0) {
            dismissHint(null);
            return true;
        }
        LogUtils.m1574i(TAG, "<<dispatchKeyEvent event = " + event);
        return true;
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
                    return;
                }
                return;
            case 4:
            case 8:
                if (this.mListener != null) {
                    this.mListener.onHintDismissed(this);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
