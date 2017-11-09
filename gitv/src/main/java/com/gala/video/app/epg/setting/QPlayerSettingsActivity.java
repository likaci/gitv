package com.gala.video.app.epg.setting;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.RadioButton;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import com.gala.video.lib.share.utils.AnimationUtil;

public class QPlayerSettingsActivity extends QMultiScreenActivity implements OnClickListener {
    private static final int[] SCREEN_SCALE_IDS = new int[]{R.id.epg_default_scale, R.id.epg_full_screen};
    private static final String[] SCREEN_SCALE_TEXTS = new String[]{"原始比例", "强制全屏"};
    private static final int[] SKIP_SETTING_IDS = new int[]{R.id.epg_setting_jump, R.id.epg_setting_no_jump};
    private static final String[] SKIP_SETTING_TEXTS = new String[]{"跳过", "不跳过"};
    private static final int[] STREAM_SETTING_IDS = new int[]{R.id.epg_stream_1080p, R.id.epg_stream_720p, R.id.epg_stream_high};
    private static final String[] STREAM_SETTING_TEXTS = new String[]{"1080P", "720P", "高清"};
    ColorStateList litchi_csl;
    private OnFocusChangeListener mFocusListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            AnimationUtil.zoomAnimation(v, hasFocus, 1.05f, 200);
        }
    };
    private RadioButton[] mScreenScaleViews = new RadioButton[2];
    private RadioButton[] mSkipCheckViews = new RadioButton[2];
    private RadioButton[] mStreamCheckViews = new RadioButton[3];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epg_activity_player_settings);
        initLayout();
    }

    private void initLayout() {
        int i;
        for (i = 0; i < this.mSkipCheckViews.length; i++) {
            this.mSkipCheckViews[i] = (RadioButton) findViewById(SKIP_SETTING_IDS[i]);
            this.mSkipCheckViews[i].setOnClickListener(this);
            this.mSkipCheckViews[i].setText(SKIP_SETTING_TEXTS[i]);
            this.mSkipCheckViews[i].setOnFocusChangeListener(this.mFocusListener);
        }
        for (i = 0; i < this.mStreamCheckViews.length; i++) {
            this.mStreamCheckViews[i] = (RadioButton) findViewById(STREAM_SETTING_IDS[i]);
            this.mStreamCheckViews[i].setOnClickListener(this);
            this.mStreamCheckViews[i].setText(STREAM_SETTING_TEXTS[i]);
            this.mStreamCheckViews[i].setOnFocusChangeListener(this.mFocusListener);
        }
        for (i = 0; i < SCREEN_SCALE_TEXTS.length; i++) {
            this.mScreenScaleViews[i] = (RadioButton) findViewById(SCREEN_SCALE_IDS[i]);
            this.mScreenScaleViews[i].setOnClickListener(this);
            this.mScreenScaleViews[i].setText(SCREEN_SCALE_TEXTS[i]);
            this.mScreenScaleViews[i].setOnFocusChangeListener(this.mFocusListener);
        }
    }

    protected void onStart() {
        super.onStart();
        showCheckedSettings();
    }

    private void showCheckedSettings() {
        setJump(SettingPlayPreference.getJumpStartEnd(AppRuntimeEnv.get().getApplicationContext()));
        setStream(SettingPlayPreference.getStreamType(AppRuntimeEnv.get().getApplicationContext()));
        setScreenScale(SettingPlayPreference.getStretchPlaybackToFullScreen(AppRuntimeEnv.get().getApplicationContext()));
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.epg_setting_jump) {
            setJump(true);
        } else if (i == R.id.epg_setting_no_jump) {
            setJump(false);
        } else if (i == R.id.epg_stream_1080p) {
            setStream(5);
        } else if (i == R.id.epg_stream_720p) {
            setStream(4);
        } else if (i == R.id.epg_stream_high) {
            setStream(2);
        } else if (i == R.id.epg_default_scale) {
            setScreenScale(false);
        } else if (i == R.id.epg_full_screen) {
            setScreenScale(true);
        }
    }

    private void setJump(boolean jump) {
        SettingPlayPreference.setJumpStartEnd(AppRuntimeEnv.get().getApplicationContext(), jump);
        if (jump) {
            this.mSkipCheckViews[0].setChecked(true);
            this.mSkipCheckViews[1].setChecked(false);
            return;
        }
        this.mSkipCheckViews[1].setChecked(true);
        this.mSkipCheckViews[0].setChecked(false);
    }

    private void setStream(int stream) {
        SettingPlayPreference.setStreamType(AppRuntimeEnv.get().getApplicationContext(), stream);
        switch (stream) {
            case 2:
                setChecked(2);
                return;
            case 3:
            case 4:
                setChecked(1);
                return;
            case 5:
                setChecked(0);
                return;
            default:
                return;
        }
    }

    private void setScreenScale(boolean isFullScreen) {
        SettingPlayPreference.setStretchPlaybackToFullScreen(AppRuntimeEnv.get().getApplicationContext(), isFullScreen);
        if (isFullScreen) {
            this.mScreenScaleViews[1].setChecked(true);
            this.mScreenScaleViews[0].setChecked(false);
            return;
        }
        this.mScreenScaleViews[0].setChecked(true);
        this.mScreenScaleViews[1].setChecked(false);
    }

    private void setChecked(int num) {
        for (int i = 0; i < this.mStreamCheckViews.length; i++) {
            if (i == num) {
                this.mStreamCheckViews[i].setChecked(true);
            } else {
                this.mStreamCheckViews[i].setChecked(false);
            }
        }
    }

    protected View getBackgroundContainer() {
        return findViewById(R.id.epg_player_setting_layout);
    }
}
