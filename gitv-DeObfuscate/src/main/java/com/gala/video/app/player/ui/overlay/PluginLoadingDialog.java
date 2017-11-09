package com.gala.video.app.player.ui.overlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.gala.video.app.player.C1291R;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class PluginLoadingDialog extends AlertDialog {
    private Context mContext;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private TextView mTxt;
    Runnable runnable = new C14791();

    class C14791 implements Runnable {
        C14791() {
        }

        public void run() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d("PluginLoadingDialog", "dismiss");
            }
            PluginLoadingDialog.this.dismiss();
        }
    }

    public PluginLoadingDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d("PluginLoadingDialog", "onWindowFocusChanged " + hasFocus);
            }
            this.mMainHandler.postDelayed(this.runnable, 1000);
        }
    }

    private void init() {
        addContentView(LayoutInflater.from(this.mContext).inflate(C1291R.layout.player_plugindialog, null), new LayoutParams(-1, -1));
        this.mTxt = (TextView) findViewById(C1291R.id.content);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("PluginLoadingDialog", "<< init");
        }
    }

    public void show() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("PluginLoadingDialog", "<< show");
        }
        try {
            if (!(getContext() instanceof Activity)) {
                super.show();
            } else if (!((Activity) getContext()).isFinishing()) {
                super.show();
            }
        } catch (Exception e) {
        }
        Drawable[] drawables = this.mTxt.getCompoundDrawables();
        if (drawables != null) {
            Drawable drawable = drawables[0];
            if (drawable != null) {
                ((AnimationDrawable) drawable).start();
            }
        }
    }

    public void dismiss() {
        try {
            if (!(getContext() instanceof Activity)) {
                super.dismiss();
            } else if (!((Activity) getContext()).isFinishing()) {
                super.dismiss();
            }
        } catch (Exception e) {
        }
    }
}
