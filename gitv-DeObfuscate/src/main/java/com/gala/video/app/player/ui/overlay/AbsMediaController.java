package com.gala.video.app.player.ui.overlay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import com.gala.video.app.player.ui.IScreenUISwitcher;

public abstract class AbsMediaController extends FrameLayout implements IMediaController, IScreenUISwitcher {
    private static final String TAG = "Player/Proj/AbsMediaController";

    public AbsMediaController(Context context) {
        super(context);
    }

    public AbsMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsMediaController(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    public void clearAd() {
    }

    public void setOnAdStateListener(OnAdStateListener listener) {
    }
}
