package com.gala.video.app.epg.screensaver;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverAnimation.IImage;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverAnimation.IRegisterImage;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate.IScreenSaverBeforeFadeIn;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverModel;
import java.util.ArrayList;
import java.util.List;

class ScreenSaverImageAnimation extends FadeAnim implements IRegisterImage {
    private static final int LOOP_COUNT = 5;
    private static final String TAG = "ScreenSaverImageAnimation";
    private List<IImage> mAnimListener = new ArrayList();
    private Bitmap mBitmap = null;
    private ScreenSaverPreEndCallback mCallback;
    private View mContainerView;
    private IScreenSaverBeforeFadeIn mIScreenSaverBeforeFadeInCallback = null;
    private boolean mIsShowing = false;
    private ScreenSaverModel mScreenSaverModel;
    private ScreenSaverImageProvider mScreenSaverProvider = null;
    private ImageView mScreenSaverView;
    private TextView mTvClickTxtView;

    class C07781 implements Runnable {
        C07781() {
        }

        public void run() {
            ScreenSaverImageAnimation.this.mIsShowing = false;
            ScreenSaverImageAnimation.this.mCallback.onScreenSaverEnd();
        }
    }

    public interface ScreenSaverPreEndCallback {
        void onEachScreenSaverShow(ScreenSaverModel screenSaverModel);

        void onScreenSaverEnd();

        void onScreenSaverPreEnd();
    }

    ScreenSaverImageAnimation() {
    }

    public void register(IImage listener) {
        this.mAnimListener.add(listener);
    }

    public void unregister(IImage listener) {
        this.mAnimListener.remove(listener);
    }

    public void setWidgets(View containerView, ImageView view, TextView text) {
        this.mScreenSaverView = view;
        this.mTvClickTxtView = text;
        this.mContainerView = containerView;
    }

    public void setSceenSaverEndCallback(ScreenSaverPreEndCallback scrSaverEndCallback) {
        this.mCallback = scrSaverEndCallback;
    }

    protected View getAnimView() {
        return this.mContainerView;
    }

    protected boolean getNext(int index) {
        if (this.mScreenSaverProvider == null) {
            return false;
        }
        Bitmap bitmap = this.mScreenSaverProvider.getBitmap(index);
        if (bitmap == null) {
            return false;
        }
        this.mScreenSaverView.setImageBitmap(bitmap);
        if (bitmap != this.mBitmap) {
            if (this.mBitmap != null) {
                this.mBitmap.recycle();
            }
            this.mBitmap = bitmap;
        }
        return true;
    }

    protected boolean isEndWithoutPlay(int currentIndex) {
        if (this.mScreenSaverProvider == null || this.mScreenSaverProvider.getLocalDataSize() == 0) {
            return true;
        }
        return false;
    }

    protected boolean isLoopLast(int showingCount) {
        if (showingCount % 5 == 0) {
            return true;
        }
        return false;
    }

    protected void onEndWithoutPlay() {
        LogUtils.m1568d(TAG, "mFadeInRunnable run() localImageSize = 0");
        this.mCallback.onScreenSaverPreEnd();
        this.mHandler.postDelayed(new C07781(), 10000);
    }

    protected void onEndWithPlay() {
        if (this.mScreenSaverView != null) {
            this.mScreenSaverView.setImageBitmap(null);
        }
        if (this.mCallback != null) {
            this.mIsShowing = false;
            this.mCallback.onScreenSaverEnd();
        }
    }

    protected int getShowingDuration(int currentIndex) {
        return 8000;
    }

    protected void beforeFadeIn(int currentIndex) {
        LogUtils.m1568d(TAG, "beforeFadeIn, index = " + currentIndex);
        this.mScreenSaverModel = this.mScreenSaverProvider.getScreenSaverModel(currentIndex);
        boolean isEnableJump = false;
        if (this.mIScreenSaverBeforeFadeInCallback != null) {
            isEnableJump = this.mIScreenSaverBeforeFadeInCallback.onBeforeFadeIn(this.mScreenSaverModel, this.mTvClickTxtView);
        }
        this.mCallback.onEachScreenSaverShow(this.mScreenSaverModel);
        for (IImage listener : this.mAnimListener) {
            listener.beforeFadeIn(isEnableJump);
        }
    }

    public void setScreenSaverBeforeFadeInCallBack(IScreenSaverBeforeFadeIn callBack) {
        this.mIScreenSaverBeforeFadeInCallback = callBack;
    }

    protected void afterFadeOut(int showingCount) {
        if ((showingCount + 1) % 5 == 0) {
            LogUtils.m1568d(TAG, "screen saver nearly end current pingback count = " + showingCount);
            if (this.mCallback != null) {
                this.mCallback.onScreenSaverPreEnd();
            }
        }
    }

    public void start(ScreenSaverImageProvider imageProvider) {
        this.mIsShowing = true;
        this.mScreenSaverProvider = imageProvider;
        this.mHandler.postDelayed(this.mFadeInRunnable, 1);
    }

    public void stop() {
        this.mHandler.removeCallbacks(this.mFadeInRunnable);
        this.mHandler.removeCallbacks(this.mFadeOutRunnable);
        this.mBitmap = null;
        if (this.mScreenSaverView != null) {
            this.mScreenSaverView.setImageBitmap(null);
        }
        this.mScreenSaverView = null;
        this.mIsShowing = false;
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }
}
