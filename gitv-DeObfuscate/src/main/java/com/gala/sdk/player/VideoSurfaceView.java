package com.gala.sdk.player;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

public class VideoSurfaceView extends SurfaceView implements IVideoSizeable {
    public static final int FORCE_VIDEO_SIZE_MODE_DEFAULT = 200;
    public static final int FORCE_VIDEO_SIZE_MODE_SCREEN_SIZE = 202;
    public static final int FORCE_VIDEO_SIZE_MODE_VIEW_SIZE = 201;
    private static final float RATIO_16_BY_9 = 1.7777778f;
    private static final float RATIO_4_BY_3 = 1.3333334f;
    public static final int SET_VIDEO_SIZE = 101;
    public static final int SET_VIEW_SIZE = 100;
    private static final String TAG = "PlayerSdk/VideoSurfaceView";
    protected int mVideoHeight;
    protected int mVideoRatio = 1;
    protected int mVideoWidth;

    class C01681 implements Runnable {
        C01681() {
        }

        public void run() {
            VideoSurfaceView.this.requestLayout();
        }
    }

    class C01692 implements Runnable {
        C01692() {
        }

        public void run() {
            VideoSurfaceView.this.requestLayout();
        }
    }

    public VideoSurfaceView(Context context) {
        super(context);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float ratioValue = getRatioValue(this.mVideoRatio);
        if (ratioValue > 0.0f) {
            measureFixedRatio(widthMeasureSpec, heightMeasureSpec, ratioValue);
            return;
        }
        Log.d(TAG, "onMeasure: super.onMeasure()");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float getRatioValue(int i) {
        switch (this.mVideoRatio) {
            case 1:
                if (this.mVideoWidth <= 0 || this.mVideoHeight <= 0) {
                    return -1.0f;
                }
                return ((float) this.mVideoWidth) / ((float) this.mVideoHeight);
            case 2:
                return RATIO_4_BY_3;
            case 3:
                return RATIO_16_BY_9;
            default:
                return -1.0f;
        }
    }

    public void setVideoSize(int width, int height) {
        Log.d(TAG, "setVideoSize(width=" + width + ", height=" + height + ")");
        this.mVideoWidth = width;
        this.mVideoHeight = height;
        post(new C01681());
    }

    public void setVideoRatio(int ratio) {
        if (ratio != 0) {
            this.mVideoRatio = ratio;
            post(new C01692());
        }
    }

    private void measureFixedRatio(int widthMeasureSpec, int heightMeasureSpec, float ratio) {
        int defaultSize = getDefaultSize(this.mVideoWidth, widthMeasureSpec);
        int defaultSize2 = getDefaultSize(this.mVideoHeight, heightMeasureSpec);
        Log.d(TAG, "measureFixedRatio: widthMeasureSpec=" + widthMeasureSpec + ", heightMeasureSpec=" + heightMeasureSpec + ", mVideoWidth=" + this.mVideoWidth + ", mVideoHeight=" + this.mVideoHeight + ", mVideoRatio=" + this.mVideoRatio + ", width=" + defaultSize + ", height=" + defaultSize2);
        if (ratio > ((float) defaultSize) / ((float) defaultSize2)) {
            defaultSize2 = Math.round(((float) defaultSize) / ratio);
        } else if (ratio > 0.0f) {
            defaultSize = Math.round(((float) defaultSize2) * ratio);
        }
        Log.d(TAG, "measureFixedRatio: measured w/h=" + defaultSize + "/" + defaultSize2);
        setMeasuredDimension(defaultSize, defaultSize2);
    }
}
