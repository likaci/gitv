package com.gala.video.app.player.ui.overlay;

public class MediaControllerStateBean {
    private boolean mBuffering;
    private boolean mIsShown;
    private int mPlayerState;
    private boolean mSeeking;
    private boolean mShowHeader;
    private boolean mTipShown;

    public int getPlayerState() {
        return this.mPlayerState;
    }

    public void setPlayerState(int playerState) {
        this.mPlayerState = playerState;
    }

    public boolean isBuffering() {
        return this.mBuffering;
    }

    public void setBuffering(boolean buffering) {
        this.mBuffering = buffering;
    }

    public boolean isShowHeader() {
        return this.mShowHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.mShowHeader = showHeader;
    }

    public boolean isTipShown() {
        return this.mTipShown;
    }

    public void setTipShown(boolean tipShown) {
        this.mTipShown = tipShown;
    }

    public boolean isSeeking() {
        return this.mSeeking;
    }

    public void setSeeking(boolean seeking) {
        this.mSeeking = seeking;
    }

    public void setIsShown(boolean isFirstShow) {
        this.mIsShown = isFirstShow;
    }

    public boolean isShown() {
        return this.mIsShown;
    }
}
