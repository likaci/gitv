package com.gala.video.app.player.utils.debug;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.gala.video.app.player.R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;

public class DebugOptionsActivity extends QMultiScreenActivity implements OnCheckedChangeListener {
    private static final String TAG = "Debug/DebugOptionsActivity";
    private CheckBox mCbLeftPerfFloatingWindow;
    private CheckBox mCbPerfFloatingWindow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity_debug_options);
        initViews();
        initData();
    }

    private void initViews() {
        this.mCbPerfFloatingWindow = (CheckBox) findViewById(R.id.cb_perf_floatingwindow);
        this.mCbPerfFloatingWindow.setOnCheckedChangeListener(this);
        this.mCbLeftPerfFloatingWindow = (CheckBox) findViewById(R.id.cb_perf_leftfloatingwindow);
        this.mCbLeftPerfFloatingWindow.setOnCheckedChangeListener(this);
    }

    private void initData() {
        boolean perfFloatingWindow = DebugOptionsCache.isPerfFloatingWindowEnabled(this);
        boolean perfFloatingLeftWindow = DebugOptionsCache.isPerfLeftFloatingWindowEnabled(this);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "initData: perfFloatingWindow=" + perfFloatingWindow);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "initData: perfFloatingLeftWindow=" + perfFloatingLeftWindow);
        }
        this.mCbPerfFloatingWindow.setChecked(perfFloatingWindow);
        this.mCbLeftPerfFloatingWindow.setChecked(perfFloatingLeftWindow);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int i = buttonView.getId();
        if (i == R.id.cb_perf_floatingwindow) {
            DebugOptionsCache.setPerfFloatingWindowEnabled(this, isChecked);
        } else if (i == R.id.cb_perf_leftfloatingwindow) {
            DebugOptionsCache.setPerfLeftFloatingWindowEnabled(this, isChecked);
        }
    }
}
