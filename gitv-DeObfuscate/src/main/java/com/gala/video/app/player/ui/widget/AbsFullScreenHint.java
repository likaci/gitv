package com.gala.video.app.player.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.gala.sdk.player.FullScreenHintType;
import com.gala.sdk.player.OnFullScreenHintChangedListener;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.ui.IScreenUISwitcher;
import com.gala.video.lib.framework.core.utils.LogUtils;

public abstract class AbsFullScreenHint extends RelativeLayout implements IScreenUISwitcher {
    private static final String TAG = "Player/Ui/AbsFullScreenHint";
    protected RelativeLayout mContainer;
    protected View mContent;
    protected Context mContext;

    public AbsFullScreenHint(Context context) {
        super(context);
        this.mContext = context;
    }

    public AbsFullScreenHint(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public AbsFullScreenHint(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    protected void initView() {
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C1291R.layout.player_fullscreenhint_layout, this);
        this.mContent = findViewById(C1291R.id.fullscreen_hint_content);
        this.mContainer = (RelativeLayout) findViewById(C1291R.id.full_screen_container);
        LogUtils.m1568d(TAG, "initView: AbsFullScreenHint");
    }

    public void clearBackgroundBitmap() {
    }

    public void dismissHint(Object object) {
    }

    public void show(FullScreenHintType hintType) {
        setVisibility(0);
    }

    public void setHintListener(OnFullScreenHintChangedListener listener) {
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    protected void hide() {
        setVisibility(8);
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
    }
}
