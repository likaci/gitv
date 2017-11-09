package com.gala.video.app.player.ui.overlay;

import android.content.Context;
import android.view.View;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;

public class WindowMediaControllerStrategy extends AbsMediaControllerStrategy {
    private static final String TAG = "Player/ui/WindowMediaControllerStrategy";

    public void showPaused() {
    }

    public void initView(Context mContext, View mRoot) {
        super.initView(mContext, mRoot);
    }

    public void showTip(TipWrapper tip) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showTip(" + tip + ")" + dumpState());
        }
        if (this.mMediaStateBean.getPlayerState() != 11) {
            return;
        }
        if (tip == null || !tip.getTipType().isSupportPersistent()) {
            this.mTipShown = true;
            this.mMediaStateBean.setTipShown(this.mTipShown);
            if (tip != null && StringUtils.isEmpty(tip.getContent())) {
                hideView(this.VIEWID_ADTIP);
            } else if (PlayerAppConfig.isShowTipWhenPlayingAd() && tip != null) {
                this.mAdTip.setText(tip.getContent());
                showView(0, this.VIEWID_ADTIP);
            }
        }
    }

    private void updateWindowSize(float zoomRatio) {
        if (0.0f == this.mLogoViewWidthWindowSize) {
            this.mLogoViewHeightWindowSize = this.mLogoViewHeightFullSize * zoomRatio;
            this.mLogoViewWidthWindowSize = this.mLogoViewWidthFullSize * zoomRatio;
        }
        if (0.0f == this.mSysTimeWindowSize) {
            this.mSysTimeWindowSize = this.mSysTimeFullSize * zoomRatio;
        }
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        super.switchScreen(isFullScreen, zoomRatio);
        updateWindowSize(zoomRatio);
        if (this.mLogo != null) {
            hideView(this.VIEWID_LOGO);
        }
        if (this.mSysTime != null) {
            this.mSysTime.setTextSize(0, this.mSysTimeWindowSize);
        }
        this.mTipView.hide(false);
    }

    public void showPlaying(boolean simple) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showPlaying(" + simple + ")" + dumpState());
        }
        super.showPlaying(simple);
        this.mMediaStateBean.setIsShown(true);
        doHide();
    }

    public void showAdPlaying(int countDownTime) {
        super.showAdPlaying(countDownTime);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showAdPlaying(" + countDownTime + ")" + dumpState());
        }
        hideView(this.VIEWID_LOGO, this.VIEWID_VIDEONAME, this.VIEWID_SYSTIME, this.VIEWID_SUBTITLE);
        if (this.mMediaStateBean.isBuffering()) {
            showView(0, this.VIEWID_BUFFER);
            return;
        }
        hideView(this.VIEWID_BUFFER);
    }

    protected void doHide() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "doHide()" + dumpState());
        }
        switch (this.mMediaStateBean.getPlayerState()) {
            case 11:
            case 13:
                return;
            default:
                clearHideViewMessageQueue();
                this.mSeekBar.stopTipMode();
                this.mSeekBar.hide();
                this.mTipView.hide(true);
                hideView(this.VIEWID_VIDEONAME, this.VIEWID_SYSTIME, this.VIEWID_SEEKBAR, this.VIEWID_BITSTREAM, this.VIEWID_LOGO, this.VIEWID_HDR);
                return;
        }
    }

    public void showVolumePanel(int currentCount) {
    }

    public void hideVolumePanel() {
    }
}
