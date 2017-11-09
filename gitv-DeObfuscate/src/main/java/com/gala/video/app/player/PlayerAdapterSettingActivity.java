package com.gala.video.app.player;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.gala.video.app.player.utils.debug.DebugOptionsCache;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.project.Project;

public class PlayerAdapterSettingActivity extends QMultiScreenActivity implements OnClickListener, OnCheckedChangeListener {
    private static final String TAG = "PlayerAdapterSettingActivity";
    private Button mBtnReset;
    private Button mBtnSystemInfoTest;
    private CheckBox mCBADCache;
    private CheckBox mCBADSeek;
    private CheckBox mCBH211;
    private CheckBox mCBMovieSeek;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(-2);
        getWindow().addFlags(128);
        if (Project.getInstance().getBuild().isHomeVersion()) {
            setTheme(C1291R.style.AppTheme);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "onCreate: setTheme for home version");
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(C1291R.layout.player_adapter_setting);
        initViews();
        initData();
    }

    private void initData() {
        this.mCBADCache.setChecked(DebugOptionsCache.isCloseADCache());
        this.mCBH211.setChecked(DebugOptionsCache.isEnableH211());
        this.mCBADSeek.setChecked(DebugOptionsCache.isADSeekAfterStart());
        this.mCBMovieSeek.setChecked(DebugOptionsCache.isMovieSeekAfterStart());
    }

    private void initViews() {
        this.mBtnSystemInfoTest = (Button) findViewById(C1291R.id.btn_system_info);
        this.mBtnSystemInfoTest.setOnClickListener(this);
        this.mBtnReset = (Button) findViewById(C1291R.id.btn_reset);
        this.mBtnReset.setOnClickListener(this);
        this.mCBH211 = (CheckBox) findViewById(C1291R.id.cb_h211);
        this.mCBH211.setOnCheckedChangeListener(this);
        this.mCBMovieSeek = (CheckBox) findViewById(C1291R.id.cb_movie_seek_start);
        this.mCBMovieSeek.setOnCheckedChangeListener(this);
        this.mCBADSeek = (CheckBox) findViewById(C1291R.id.cb_ad_seek_start);
        this.mCBADSeek.setOnCheckedChangeListener(this);
        this.mCBADCache = (CheckBox) findViewById(C1291R.id.cb_ad_cache);
        this.mCBADCache.setOnCheckedChangeListener(this);
    }

    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == C1291R.id.btn_system_info) {
            startActivity(new Intent(this, SystemInfoTestActivity.class));
        } else if (viewId == C1291R.id.btn_reset) {
            resetAll();
        }
    }

    private void resetAll() {
        this.mCBH211.setChecked(false);
        DebugOptionsCache.setEnableH211(false);
        this.mCBMovieSeek.setChecked(false);
        DebugOptionsCache.setMovieSeekAfterStart(false);
        this.mCBADCache.setChecked(false);
        DebugOptionsCache.setCloseAdCache(false);
        this.mCBADSeek.setChecked(false);
        DebugOptionsCache.setADSeekAfterStart(false);
        DebugOptionsCache.setEnableADLocalServer(false);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int buttonViewId = buttonView.getId();
        if (buttonViewId == C1291R.id.cb_ad_cache) {
            DebugOptionsCache.setCloseAdCache(isChecked);
        } else if (buttonViewId == C1291R.id.cb_ad_seek_start) {
            DebugOptionsCache.setADSeekAfterStart(isChecked);
        } else if (buttonViewId == C1291R.id.cb_movie_seek_start) {
            DebugOptionsCache.setMovieSeekAfterStart(isChecked);
        } else if (buttonViewId == C1291R.id.cb_h211) {
            DebugOptionsCache.setEnableH211(isChecked);
        }
    }
}
