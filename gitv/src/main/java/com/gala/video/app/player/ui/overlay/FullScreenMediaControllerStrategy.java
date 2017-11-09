package com.gala.video.app.player.ui.overlay;

import android.content.Context;
import android.view.View;
import com.gala.video.app.player.R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;

public class FullScreenMediaControllerStrategy extends AbsMediaControllerStrategy {
    private static final String TAG = "Player/ui/FullScreenMediaControllerStrategy";

    public void initView(Context mContext, View mRoot) {
        super.initView(mContext, mRoot);
    }

    public void showPlaying(boolean simple) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showPlaying(" + simple + ")");
        }
        super.showPlaying(simple);
        showPlaying(simple, 5000);
    }

    public void showPaused() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showPaused()" + dumpState());
        }
        this.mState = 13;
        this.mMediaStateBean.setPlayerState(this.mState);
        clearHideViewMessageQueue();
        hideView(this.VIEWID_LOGO, this.VIEWID_BUFFER, this.VIEWID_TIP);
        checkSysTime();
        this.mPause.setImageDrawable(this.mContext.getResources().getDrawable(R.drawable.player_play_button));
        showView(0, this.VIEWID_VIDEONAME, this.VIEWID_SYSTIME, this.VIEWID_SEEKBAR, this.VIEWID_BITSTREAM, this.VIEWID_HDR);
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

    public void showHeader() {
        super.showHeader();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showHeader() mShowHeader=" + this.mShowHeader);
        }
        showView(0, this.VIEWID_VIDEONAME, this.VIEWID_BITSTREAM, this.VIEWID_SYSTIME, this.VIEWID_HDR);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(2, 0, 0));
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(3, 0, 0), 2000);
    }

    public void hideHeader() {
        super.hideHeader();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "hideHeader() mShowHeader=" + this.mShowHeader);
        }
        if (!this.mMediaStateBean.isSeeking() && this.mMediaStateBean.getPlayerState() != 13) {
            hideView(this.VIEWID_VIDEONAME, this.VIEWID_BITSTREAM, this.VIEWID_SYSTIME, this.VIEWID_HDR);
        }
    }

    public void showPanelWithTip(int duration) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showPanelWithTip(" + duration + ")" + dumpState());
        }
        hideView(this.VIEWID_LOGO, this.VIEWID_BITSTREAM, this.VIEWID_VIDEONAME, this.VIEWID_SYSTIME, this.VIEWID_SEEKBAR, this.VIEWID_HDR);
        super.showPanelWithTip(duration);
    }

    public void showPanelWithoutTip(int duration) {
        super.showPanelWithoutTip(duration);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showPanelWithoutTip(" + duration + ")" + dumpState());
        }
        showView(duration, this.VIEWID_VIDEONAME, this.VIEWID_SYSTIME, this.VIEWID_SEEKBAR, this.VIEWID_BITSTREAM, this.VIEWID_HDR);
    }

    protected void showLogo() {
        clearHideViewMessageQueue();
        showWaterLogo();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showLogo()" + dumpState());
        }
        hideView(this.VIEWID_VIDEONAME, this.VIEWID_SYSTIME, this.VIEWID_SEEKBAR, this.VIEWID_BITSTREAM, this.VIEWID_HDR);
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        super.switchScreen(isFullScreen, zoomRatio);
        if (this.mSysTime != null) {
            this.mSysTime.setTextSize(0, this.mSysTimeFullSize);
        }
    }

    protected void showPlaying(boolean simple, int duration) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showPlaying(" + simple + ", " + duration + ")" + dumpState());
        }
        if (simple && !this.mMediaStateBean.isTipShown() && !this.mMediaStateBean.isSeeking()) {
            showLogo();
        } else if (this.mMediaStateBean.isSeeking()) {
            showPanelWithoutTip(duration);
        } else if (this.mMediaStateBean.isTipShown()) {
            showPanelWithTip(duration);
        }
        if (!this.mMediaStateBean.isShown()) {
            showHeader();
            this.mMediaStateBean.setIsShown(true);
        }
    }

    private void showWaterLogo() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showWaterLogo()");
        }
        boolean hideLogo = Project.getInstance().getBuild().isNoLogoUI();
        LogUtils.d(TAG, "showWaterLogo hideLogo = " + hideLogo);
        if (hideLogo) {
            this.mLogo.setVisibility(8);
        } else {
            this.mLogo.setVisibility(0);
        }
    }

    protected void doHide() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "doHide()" + dumpState());
        }
        switch (this.mMediaStateBean.getPlayerState()) {
            case 11:
            case 13:
                return;
            case 12:
                if (this.mMediaStateBean.isSeeking()) {
                    showPanelWithoutTip(0);
                    return;
                } else if (!this.mMediaStateBean.isTipShown()) {
                    showLogo();
                    return;
                } else {
                    return;
                }
            default:
                clearHideViewMessageQueue();
                this.mSeekBar.stopTipMode();
                this.mSeekBar.hide();
                this.mTipView.hide(true);
                hideView(this.VIEWID_VIDEONAME, this.VIEWID_SYSTIME, this.VIEWID_SEEKBAR, this.VIEWID_TIP, this.VIEWID_BITSTREAM, this.VIEWID_HDR);
                return;
        }
    }
}
