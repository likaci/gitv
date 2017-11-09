package com.gala.video.app.player.ui.overlay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.IScreenUISwitcher;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;
import java.util.ArrayList;
import java.util.List;

public class MediaControllerContainer extends LinearLayout implements IMediaController, IScreenUISwitcher {
    private static final String TAG = "Player/Ui/MediaControllerContainer";
    private boolean m3dMode;
    private Context mContext;
    private int mControllerCount = 1;
    private List<AbsMediaController> mControllers = new ArrayList(2);
    private boolean mIsFullScreenMode;
    private OnAdStateListener mOnAdStateListener;
    private IVideo mVideo;
    private float mWindowZoomRatio;

    public MediaControllerContainer(Context context) {
        super(context);
        this.mContext = context;
    }

    public MediaControllerContainer(Context context, AttributeSet attr) {
        super(context, attr);
        this.mContext = context;
    }

    public void updateView(boolean is3d, SourceType sourceType) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "updateView(" + is3d + ", " + sourceType + ") " + this.mControllerCount + ", config show 3d = " + Project.getInstance().getConfig().shouldDuplicateUIForStereo3D());
        }
        this.m3dMode = is3d;
        if (this.m3dMode && Project.getInstance().getConfig().shouldDuplicateUIForStereo3D()) {
            this.mControllerCount = 2;
        } else {
            this.mControllerCount = 1;
        }
        if (this.mControllers.size() != this.mControllerCount) {
            clear();
            for (int i = 0; i < this.mControllerCount; i++) {
                AbsMediaController controller = getMediaController(this.mContext, this.m3dMode, sourceType);
                controller.setOnAdStateListener(this.mOnAdStateListener);
                controller.setThreeDimensional(this.m3dMode);
                controller.setVideo(this.mVideo);
                controller.switchScreen(this.mIsFullScreenMode, this.mWindowZoomRatio);
                this.mControllers.add(controller);
                addView(controller, new LayoutParams(-1, -1, 1.0f));
                if (i == 0) {
                    controller.setPrimary(true);
                }
            }
        }
    }

    public void release() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "release()");
        }
        clear();
    }

    private void clear() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "clear()");
        }
        removeAllViews();
        this.mControllers.clear();
    }

    private AbsMediaController getMediaController(Context context, boolean stereo3d, SourceType type) {
        AbsMediaController controller = PlayerAppConfig.getMediaController(context, stereo3d, type);
        if (controller != null) {
            return controller;
        }
        if (type == SourceType.LIVE) {
            return new LiveMediaControllerOverlay(context);
        }
        return new MediaControllerOverlay(context);
    }

    public void setThreeDimensional(boolean enable) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setThreeDimensional(" + enable + ") m3dMode=" + this.m3dMode);
        }
        this.m3dMode = enable;
        if (this.mVideo != null) {
            updateView(this.m3dMode, this.mVideo.getSourceType());
        }
    }

    public void showPlaying(boolean simple) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showPlaying(" + simple + ")");
        }
        showSelf();
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.showPlaying(simple);
        }
    }

    public void showPaused() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showPaused()");
        }
        showSelf();
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.showPaused();
        }
    }

    public void showAdPlaying(int countDownTime) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showAdPlaying(" + countDownTime + ")");
        }
        showSelf();
        int time = countDownTime / 1000;
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.showAdPlaying(time);
        }
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hide()");
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.hide();
        }
    }

    private void showSelf() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showSelf() visibility=" + isShown());
        }
        if (!isShown()) {
            setVisibility(0);
        }
        checkChildrenLayout();
    }

    public void showBuffering() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showBuffering()");
        }
        showSelf();
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.showBuffering();
        }
    }

    public void hideBuffering() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hideBuffering()");
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.hideBuffering();
        }
    }

    public void setBufferPercent(int percent) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setBufferPercent(" + percent + ") size=" + this.mControllers.size());
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.setBufferPercent(percent);
        }
    }

    public void setNetSpeed(long bytePerSecond) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setNetSpeed(" + bytePerSecond + ") size=" + this.mControllers.size());
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.setNetSpeed(bytePerSecond);
        }
    }

    public void showTip(TipWrapper tip) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showTip(" + tip.getContent() + ") size=" + this.mControllers.size());
        }
        showSelf();
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.showTip(tip);
        }
    }

    public void hideTip() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hideTip() size=" + this.mControllers.size());
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.hideTip();
        }
    }

    public void showPlayOverFlow(boolean forward, long distance) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showPlayOverFlow() size=" + this.mControllers.size());
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.showPlayOverFlow(forward, distance);
        }
    }

    public void hidePlayOverFlow(boolean forward, long distance) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hidePlayOverFlow() size=" + this.mControllers.size());
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.hidePlayOverFlow(forward, distance);
        }
    }

    public void showVolumePanel(int currentVolume) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showVolumePanel() size=" + this.mControllers.size());
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.showVolumePanel(currentVolume);
        }
    }

    public void hideVolumePanel() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hideVolumePanel() size=" + this.mControllers.size());
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.hideVolumePanel();
        }
    }

    public void showBrightnessPanel(int lightCount) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showBrightnessPanel() lightCount = " + lightCount);
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.showBrightnessPanel(lightCount);
        }
    }

    public void hideBrightnessPanel() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hideBrightnessPanel() size=" + this.mControllers.size());
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.hideBrightnessPanel();
        }
    }

    public void setSubtitle(String subtitle) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setSubtitle(" + subtitle + ") size=" + this.mControllers.size());
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.setSubtitle(subtitle);
        }
    }

    public int getProgress() {
        int progress = 0;
        if (!ListUtils.isEmpty(this.mControllers)) {
            progress = ((AbsMediaController) this.mControllers.get(0)).getProgress();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getProgress() return " + progress);
        }
        return progress;
    }

    public void setMaxProgress(int maxProgress, int maxSeekableProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setMaxProgress(" + maxProgress + ", " + maxSeekableProgress + ")");
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.setMaxProgress(maxProgress, maxSeekableProgress);
        }
    }

    public void setHeadAndTailProgress(int headProgress, int tailProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setHeadAndTailProgress(" + headProgress + ", " + tailProgress + ")");
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.setHeadAndTailProgress(headProgress, tailProgress);
        }
    }

    public void setProgress(int progress) {
        for (int i = 0; i < this.mControllers.size(); i++) {
            ((AbsMediaController) this.mControllers.get(i)).setProgress(progress);
        }
    }

    public void setSecondaryProgress(int secondaryProgress) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setSecondaryProgress(" + secondaryProgress + ")" + this.mControllers.size());
        }
        for (int i = 0; i < this.mControllers.size(); i++) {
            ((AbsMediaController) this.mControllers.get(i)).setSecondaryProgress(secondaryProgress);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "dispatchKeyEvent(" + event + ")");
        }
        boolean isDispatched = false;
        for (AbsMediaController ctrl : this.mControllers) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, ">> dispatchKeyEvent(" + event + ");" + isDispatched + " ctrl = " + ctrl);
            }
            if (this.mVideo != null && this.mVideo.isPreview() && this.m3dMode) {
                if (event.getKeyCode() == 66 || event.getKeyCode() == 23) {
                    if (!isDispatched) {
                        isDispatched = ctrl.dispatchKeyEvent(event);
                    }
                } else if (isDispatched) {
                    ctrl.dispatchKeyEvent(event);
                } else {
                    isDispatched = ctrl.dispatchKeyEvent(event);
                }
            } else if (isDispatched) {
                ctrl.dispatchKeyEvent(event);
            } else {
                isDispatched = ctrl.dispatchKeyEvent(event);
            }
            LogUtils.m1568d(TAG, "<< dispatchKeyEvent(" + event + ")" + isDispatched + " ctrl = " + ctrl);
        }
        return isDispatched;
    }

    private void checkChildrenLayout() {
        if (getWidth() != 0 && getChildCount() > 0 && getChildAt(0).getWidth() == 0) {
            requestLayout();
            LogUtils.m1577w(TAG, "checkChildrenLayout()() Why not layout children??????");
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "checkChildrenLayout() ready!");
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onAttachedToWindow()");
        }
        checkChildrenLayout();
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setWindowMode(" + isFullScreen + ")");
        }
        this.mIsFullScreenMode = isFullScreen;
        this.mWindowZoomRatio = zoomRatio;
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.switchScreen(isFullScreen, zoomRatio);
        }
    }

    public void showPanel(int duration) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showPanel(" + duration + ")");
        }
        showSelf();
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.showPanel(duration);
        }
    }

    public void onSeekBegin(View view, int i) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onSeekBegin(" + i + ")" + this.mControllers.size());
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.onSeekBegin(view, i);
        }
    }

    public void onProgressChanged(View view, int i) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onProgressChanged(" + i + ")" + this.mControllers.size());
        }
    }

    public void onSeekEnd(View view, int i) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onSeekEnd(" + i + ")" + this.mControllers.size());
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.onSeekEnd(view, i);
        }
    }

    public void updateBitStreamDefinition(String bitStream) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "updateBitStreamDefinition(" + bitStream + ") size=" + this.mControllers.size());
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.updateBitStreamDefinition(bitStream);
        }
    }

    public void clearMediaControllerState() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "clearMediaControllerState");
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.clearMediaControllerState();
        }
    }

    public void showBottomAndTop(int duration) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showBottomAndTop: size=" + this.mControllers.size() + ", duration=" + duration);
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.showBottomAndTop(duration);
        }
    }

    public void hideBottomAndTop(int duration) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hideBottomAndTop()");
        }
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.hideBottomAndTop(duration);
        }
    }

    public boolean showSeekBar() {
        boolean isShowSeekBar = false;
        for (AbsMediaController ctrl : this.mControllers) {
            isShowSeekBar = ctrl.showSeekBar();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "showSeekBar(" + isShowSeekBar + ")");
        }
        return isShowSeekBar;
    }

    public void setVideo(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setVideo()");
        }
        this.mVideo = video;
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.setVideo(video);
        }
    }

    public void showMiddleAdEnd() {
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.showMiddleAdEnd();
        }
    }

    public void clearAd() {
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.clearAd();
        }
    }

    public void setAdStateListener(OnAdStateListener listener) {
        this.mOnAdStateListener = listener;
    }

    public void setPrimary(boolean b) {
    }

    public void hideGuideTip() {
        for (AbsMediaController ctrl : this.mControllers) {
            ctrl.hideGuideTip();
        }
    }
}
