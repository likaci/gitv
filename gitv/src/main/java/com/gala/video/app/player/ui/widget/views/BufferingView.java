package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.gala.sdk.player.IThreeDimensional;
import com.gala.video.app.player.R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.config.PlayerConfigManager;
import com.gala.video.app.player.ui.IScreenUISwitcher;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class BufferingView extends RelativeLayout implements IThreeDimensional, IScreenUISwitcher {
    private static final String TAG = "Player/Ui/BufferingView";
    private int mAnimHeightFullSize;
    private float mAnimHeightWindow;
    private int mAnimWidthFullSize;
    private float mAnimWidthWindow;
    private EnhancedTextView mBuffer;
    private int mBufferFullSize;
    private float mBufferWindowSize;
    private Context mContext;
    private boolean mIsShowPercent = false;
    private EnhancedImageView mProgressBarImage;

    public void setBufferPercent(int percent) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setBufferProgress(" + percent + ")");
        }
        if (percent > 0) {
            showSpeedOrPercent(percent + "%");
            this.mIsShowPercent = true;
        }
    }

    public void setNetSpeed(long bytePerSecond) {
        String speed;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setNetSpeed(" + bytePerSecond + ")");
        }
        bytePerSecond /= 128;
        if (bytePerSecond < 0) {
            speed = "0Kb/s";
        } else if (bytePerSecond < 0 || bytePerSecond >= 1024) {
            int h = ((int) (bytePerSecond % 1024)) / 102;
            speed = (((int) bytePerSecond) / 1024) + "." + h + "Mb/s";
        } else {
            speed = bytePerSecond + "Kb/s";
        }
        LogUtils.d("ProgressBarCenter", "net speed=" + speed);
        if (!this.mIsShowPercent) {
            showSpeedOrPercent(speed);
        }
    }

    private void showSpeedOrPercent(String str) {
        this.mBuffer.setText(str);
        this.mBuffer.setVisibility(0);
    }

    public BufferingView(Context context) {
        super(context);
        this.mContext = context;
        init(context);
    }

    public BufferingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context);
    }

    public BufferingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init(context);
    }

    private int getLayoutId() {
        return PlayerAppConfig.getBufferViewLayoutId();
    }

    private void init(Context context) {
        View progressbarView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(getLayoutId(), null);
        addView(progressbarView);
        this.mProgressBarImage = (EnhancedImageView) progressbarView.findViewById(R.id.progressbar_item_center_image_id);
        this.mBuffer = (EnhancedTextView) progressbarView.findViewById(R.id.progress_or_speed);
        this.mBufferFullSize = context.getResources().getDimensionPixelSize(R.dimen.dimen_24dp);
        this.mAnimWidthFullSize = context.getResources().getDimensionPixelSize(R.dimen.dimen_67dp);
        this.mAnimHeightFullSize = context.getResources().getDimensionPixelSize(R.dimen.dimen_56dp);
    }

    public void setThreeDimensional(boolean enable) {
        if (this.mBuffer != null) {
            this.mBuffer.setThreeDimensional(enable);
        }
        if (this.mProgressBarImage != null) {
            this.mProgressBarImage.setThreeDimensional(enable);
        }
    }

    private void startAnim() {
        if (this.mProgressBarImage != null) {
            AnimationDrawable animation = (AnimationDrawable) this.mProgressBarImage.getDrawable();
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "startAnim animation=" + animation);
            }
            if (animation != null) {
                animation.start();
                return;
            }
            AnimationDrawable anim;
            if (PlayerConfigManager.getPlayerConfig().isSupportAnimation()) {
                anim = (AnimationDrawable) this.mContext.getResources().getDrawable(R.drawable.share_new_anim_load_center);
            } else {
                anim = (AnimationDrawable) this.mContext.getResources().getDrawable(R.drawable.player_new_anim_load_center_single);
            }
            if (anim != null) {
                this.mProgressBarImage.setImageDrawable(anim);
                anim.start();
            }
        }
    }

    public void clearAnimation() {
        if (this.mProgressBarImage != null) {
            this.mProgressBarImage.clearAnimation();
        }
    }

    private void updateWindowSize(float zoomRatio) {
        this.mBufferWindowSize = ((float) this.mBufferFullSize) * zoomRatio;
        this.mAnimHeightWindow = ((float) this.mAnimHeightFullSize) * zoomRatio;
        this.mAnimWidthWindow = ((float) this.mAnimWidthFullSize) * zoomRatio;
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        updateWindowSize(zoomRatio);
        if (isFullScreen) {
            this.mBuffer.setTextSize(0, (float) this.mBufferFullSize);
            int width = this.mAnimWidthFullSize;
            int height = this.mAnimHeightFullSize;
            LayoutParams params = (LayoutParams) this.mProgressBarImage.getLayoutParams();
            params.width = width;
            params.height = height;
            this.mProgressBarImage.setLayoutParams(params);
            return;
        }
        this.mBuffer.setTextSize(0, this.mBufferWindowSize);
        height = (int) this.mAnimHeightWindow;
        params = (LayoutParams) this.mProgressBarImage.getLayoutParams();
        params.width = (int) this.mAnimWidthWindow;
        params.height = height;
        this.mProgressBarImage.setLayoutParams(params);
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onVisibilityChanged visibility=" + visibility);
        }
        if (visibility == 0) {
            startAnim();
        } else if (visibility == 8) {
            clearAnimation();
        }
    }
}
