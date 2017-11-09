package com.tvos.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.mcto.ads.internal.net.SendFlag;

public class VFloatingWindow {
    public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
    public static final int INPUT_METHOD_NEEDED = 1;
    public static final int INPUT_METHOD_NOT_NEEDED = 2;
    public static final int SHOW_MODE_FROM_BOTTOM = 4;
    public static final int SHOW_MODE_FROM_FULL = 0;
    public static final int SHOW_MODE_FROM_LEFT = 1;
    public static final int SHOW_MODE_FROM_RIGHT = 2;
    public static final int SHOW_MODE_FROM_TOP = 3;
    private int mAnimationDuration;
    private int mAnimationStyle;
    private Drawable mBackground;
    private FrameLayout mBackgroundContainer;
    private boolean mClippingEnabled;
    private View mContentView;
    private Context mContext;
    private boolean mFocusable;
    private int mHeight;
    private int mHeightMode;
    private boolean mIgnoreCheekPress;
    private int mInputMethodMode;
    private boolean mIsShowing;
    private int mLastHeight;
    private int mLastWidth;
    private boolean mLayoutInScreen;
    private boolean mLayoutInsetDecor;
    private boolean mNotTouchModal;
    private OnDismissListener mOnDismissListener;
    private boolean mOutsideTouchable;
    private View mPopupView;
    private int mShowMode;
    private int mSoftInputMode;
    private int mSplitTouchEnabled;
    private OnTouchListener mTouchInterceptor;
    private boolean mTouchable;
    private int mWidth;
    private int mWidthMode;
    private int mWindowLayoutType;
    private WindowManager mWindowManager;

    class C20781 implements AnimationListener {
        C20781() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            try {
                VFloatingWindow.this.mWindowManager.removeViewImmediate(VFloatingWindow.this.mPopupView);
                if (VFloatingWindow.this.mPopupView != VFloatingWindow.this.mContentView && (VFloatingWindow.this.mPopupView instanceof ViewGroup)) {
                    ((ViewGroup) VFloatingWindow.this.mPopupView).removeView(VFloatingWindow.this.mContentView);
                }
                VFloatingWindow.this.mPopupView = null;
                if (VFloatingWindow.this.mOnDismissListener != null) {
                    VFloatingWindow.this.mOnDismissListener.onDismiss();
                }
            } catch (Throwable th) {
                Throwable th2 = th;
                if (VFloatingWindow.this.mPopupView != VFloatingWindow.this.mContentView && (VFloatingWindow.this.mPopupView instanceof ViewGroup)) {
                    ((ViewGroup) VFloatingWindow.this.mPopupView).removeView(VFloatingWindow.this.mContentView);
                }
                VFloatingWindow.this.mPopupView = null;
                if (VFloatingWindow.this.mOnDismissListener != null) {
                    VFloatingWindow.this.mOnDismissListener.onDismiss();
                }
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    private class PopupViewContainer extends FrameLayout {
        public PopupViewContainer(Context context) {
            super(context);
        }

        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() != 4) {
                return super.dispatchKeyEvent(event);
            }
            if (getKeyDispatcherState() == null) {
                return super.dispatchKeyEvent(event);
            }
            DispatcherState state;
            if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                state = getKeyDispatcherState();
                if (state == null) {
                    return true;
                }
                state.startTracking(event, this);
                return true;
            }
            if (event.getAction() == 1) {
                state = getKeyDispatcherState();
                if (!(state == null || !state.isTracking(event) || event.isCanceled())) {
                    VFloatingWindow.this.dismiss();
                    return true;
                }
            }
            return super.dispatchKeyEvent(event);
        }

        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (VFloatingWindow.this.mTouchInterceptor == null || !VFloatingWindow.this.mTouchInterceptor.onTouch(this, ev)) {
                return super.dispatchTouchEvent(ev);
            }
            return true;
        }

        public boolean onTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (event.getAction() == 0 && (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())) {
                VFloatingWindow.this.dismiss();
                return true;
            } else if (event.getAction() != 4) {
                return super.onTouchEvent(event);
            } else {
                VFloatingWindow.this.dismiss();
                return true;
            }
        }

        public void sendAccessibilityEvent(int eventType) {
            if (VFloatingWindow.this.mContentView != null) {
                VFloatingWindow.this.mContentView.sendAccessibilityEvent(eventType);
            } else {
                super.sendAccessibilityEvent(eventType);
            }
        }
    }

    public VFloatingWindow(Context context) {
        this.mInputMethodMode = 0;
        this.mSoftInputMode = 1;
        this.mTouchable = true;
        this.mOutsideTouchable = false;
        this.mClippingEnabled = true;
        this.mSplitTouchEnabled = -1;
        this.mLayoutInsetDecor = false;
        this.mAnimationDuration = 200;
        this.mWindowLayoutType = 1000;
        this.mIgnoreCheekPress = false;
        this.mAnimationStyle = -1;
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        setFocusable(true);
    }

    public VFloatingWindow() {
        this(null, 0, 0);
    }

    public VFloatingWindow(View contentView) {
        this(contentView, 0, 0);
    }

    public VFloatingWindow(int width, int height) {
        this(null, width, height);
    }

    public VFloatingWindow(View contentView, int width, int height) {
        this(contentView, width, height, true);
    }

    public VFloatingWindow(View contentView, int width, int height, boolean focusable) {
        this.mInputMethodMode = 0;
        this.mSoftInputMode = 1;
        this.mTouchable = true;
        this.mOutsideTouchable = false;
        this.mClippingEnabled = true;
        this.mSplitTouchEnabled = -1;
        this.mLayoutInsetDecor = false;
        this.mAnimationDuration = 200;
        this.mWindowLayoutType = 1000;
        this.mIgnoreCheekPress = false;
        this.mAnimationStyle = -1;
        if (contentView != null) {
            this.mContext = contentView.getContext();
            this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        }
        setContentView(contentView);
        setWidth(width);
        setHeight(height);
        setFocusable(focusable);
    }

    public void setAnimationDuration(int duration) {
        this.mAnimationDuration = duration;
    }

    public void showWindowBeside(VFloatingWindow base, View parent) {
        showWindowBeside(base, parent.getWindowToken());
    }

    public void showWindowBeside(VFloatingWindow base, IBinder token) {
        Animation animation = null;
        int mode = base.getShowMode();
        if (mode == 1) {
            this.mShowMode = mode;
            setWindowLayoutMode(-2, -1);
            showAtLocation(token, 3, base.getContentView().getWidth(), 0);
            animation = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
        } else if (mode == 2) {
            this.mShowMode = mode;
            setWindowLayoutMode(-2, -1);
            showAtLocation(token, 5, base.getContentView().getWidth(), 0);
            animation = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
        } else if (mode == 3) {
            this.mShowMode = mode;
            setWindowLayoutMode(-1, -2);
            showAtLocation(token, 48, 0, base.getContentView().getHeight());
            animation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
        } else if (mode == 4) {
            this.mShowMode = mode;
            setWindowLayoutMode(-1, -2);
            showAtLocation(token, 80, 0, base.getContentView().getHeight());
            animation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        }
        if (animation != null) {
            animation.setDuration((long) this.mAnimationDuration);
            animation.setFillAfter(true);
            this.mContentView.startAnimation(animation);
            this.mBackgroundContainer.startAnimation(animation);
        }
    }

    public void showWindow(int mode, View parent) {
        showWindow(mode, parent.getWindowToken());
    }

    public void showWindow(int mode, IBinder token) {
        Animation animation = null;
        if (mode == 1) {
            this.mShowMode = mode;
            setWindowLayoutMode(-2, -1);
            showAtLocation(token, 3, 0, 0);
            animation = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
        } else if (mode == 2) {
            this.mShowMode = mode;
            setWindowLayoutMode(-2, -1);
            showAtLocation(token, 5, 0, 0);
            animation = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
        } else if (mode == 3) {
            this.mShowMode = mode;
            setWindowLayoutMode(-1, -2);
            showAtLocation(token, 48, 0, 0);
            animation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
        } else if (mode == 4) {
            this.mShowMode = mode;
            setWindowLayoutMode(-1, -2);
            showAtLocation(token, 80, 0, 0);
            animation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        } else if (mode == 0) {
            this.mShowMode = mode;
            setWindowLayoutMode(-1, -1);
            showAtLocation(token, 17, 0, 0);
            animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, ThreeDimensionalParams.TEXT_SCALE_FOR_3D, 1, ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
        }
        if (animation != null) {
            animation.setDuration((long) this.mAnimationDuration);
            animation.setFillAfter(true);
            this.mContentView.startAnimation(animation);
            this.mBackgroundContainer.startAnimation(animation);
        }
    }

    public int getShowMode() {
        return this.mShowMode;
    }

    public Drawable getBackground() {
        return this.mBackground;
    }

    public void setBackgroundColor(int color) {
        this.mBackground = new ColorDrawable(color);
    }

    public void setBackgroundDrawable(Drawable background) {
        this.mBackground = background;
    }

    public int getAnimationStyle() {
        return this.mAnimationStyle;
    }

    public void setIgnoreCheekPress() {
        this.mIgnoreCheekPress = true;
    }

    public void setAnimationStyle(int animationStyle) {
        this.mAnimationStyle = animationStyle;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public void setContentView(View contentView) {
        if (!isShowing()) {
            this.mContentView = contentView;
            if (this.mContext == null && this.mContentView != null) {
                this.mContext = this.mContentView.getContext();
            }
            if (this.mWindowManager == null && this.mContentView != null) {
                this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
            }
        }
    }

    public void setTouchInterceptor(OnTouchListener l) {
        this.mTouchInterceptor = l;
    }

    public boolean isFocusable() {
        return this.mFocusable;
    }

    public void setFocusable(boolean focusable) {
        this.mFocusable = focusable;
    }

    public int getInputMethodMode() {
        return this.mInputMethodMode;
    }

    public void setInputMethodMode(int mode) {
        this.mInputMethodMode = mode;
    }

    public void setSoftInputMode(int mode) {
        this.mSoftInputMode = mode;
    }

    public int getSoftInputMode() {
        return this.mSoftInputMode;
    }

    public boolean isTouchable() {
        return this.mTouchable;
    }

    public void setTouchable(boolean touchable) {
        this.mTouchable = touchable;
    }

    public boolean isOutsideTouchable() {
        return this.mOutsideTouchable;
    }

    public void setOutsideTouchable(boolean touchable) {
        this.mOutsideTouchable = touchable;
    }

    public boolean isClippingEnabled() {
        return this.mClippingEnabled;
    }

    public void setClippingEnabled(boolean enabled) {
        this.mClippingEnabled = enabled;
    }

    public void setClipToScreenEnabled(boolean enabled) {
        setClippingEnabled(!enabled);
    }

    public boolean isSplitTouchEnabled() {
        if (this.mSplitTouchEnabled >= 0 || this.mContext == null) {
            if (this.mSplitTouchEnabled != 1) {
                return false;
            }
            return true;
        } else if (this.mContext.getApplicationInfo().targetSdkVersion >= 11) {
            return true;
        } else {
            return false;
        }
    }

    public void setSplitTouchEnabled(boolean enabled) {
        this.mSplitTouchEnabled = enabled ? 1 : 0;
    }

    public boolean isLayoutInScreenEnabled() {
        return this.mLayoutInScreen;
    }

    public void setLayoutInScreenEnabled(boolean enabled) {
        this.mLayoutInScreen = enabled;
    }

    public void setLayoutInsetDecor(boolean enabled) {
        this.mLayoutInsetDecor = enabled;
    }

    public void setWindowLayoutType(int layoutType) {
        this.mWindowLayoutType = layoutType;
    }

    public int getWindowLayoutType() {
        return this.mWindowLayoutType;
    }

    public void setTouchModal(boolean touchModal) {
        this.mNotTouchModal = !touchModal;
    }

    public void setWindowLayoutMode(int widthSpec, int heightSpec) {
        this.mWidthMode = widthSpec;
        this.mHeightMode = heightSpec;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        showAtLocation(parent.getWindowToken(), gravity, x, y);
    }

    public void showAtLocation(IBinder token, int gravity, int x, int y) {
        if (!isShowing() && this.mContentView != null) {
            int i;
            this.mIsShowing = true;
            LayoutParams p = createPopupLayout(token);
            p.windowAnimations = computeAnimationResource();
            preparePopup(p);
            if (gravity == 0) {
                gravity = 8388659;
            }
            p.gravity = gravity;
            p.x = x;
            p.y = y;
            if (this.mHeightMode < 0) {
                i = this.mHeightMode;
                this.mLastHeight = i;
                p.height = i;
            }
            if (this.mWidthMode < 0) {
                i = this.mWidthMode;
                this.mLastWidth = i;
                p.width = i;
            }
            invokePopup(p);
        }
    }

    private void preparePopup(LayoutParams p) {
        if (this.mContentView == null || this.mContext == null || this.mWindowManager == null) {
            throw new IllegalStateException("You must specify a valid content view by calling setContentView() before attempting to show the popup.");
        }
        ViewGroup.LayoutParams layoutParams = this.mContentView.getLayoutParams();
        int height = -1;
        if (layoutParams != null && layoutParams.height == -2) {
            height = -2;
        }
        PopupViewContainer popupViewContainer = new PopupViewContainer(this.mContext);
        FrameLayout.LayoutParams listParams = new FrameLayout.LayoutParams(-1, height);
        this.mBackgroundContainer = new FrameLayout(this.mContext);
        if (this.mBackground != null) {
            this.mBackgroundContainer.setBackgroundDrawable(this.mBackground);
        }
        popupViewContainer.addView(this.mBackgroundContainer, new FrameLayout.LayoutParams(-1, -1));
        popupViewContainer.addView(this.mContentView, listParams);
        this.mPopupView = popupViewContainer;
    }

    private void invokePopup(LayoutParams p) {
        if (this.mContext != null) {
            p.packageName = this.mContext.getPackageName();
        }
        this.mPopupView.setFitsSystemWindows(this.mLayoutInsetDecor);
        this.mWindowManager.addView(this.mPopupView, p);
    }

    private LayoutParams createPopupLayout(IBinder token) {
        LayoutParams p = new LayoutParams();
        p.gravity = 8388659;
        int i = this.mWidth;
        this.mLastWidth = i;
        p.width = i;
        i = this.mHeight;
        this.mLastHeight = i;
        p.height = i;
        p.format = -3;
        p.flags = computeFlags(p.flags);
        p.type = this.mWindowLayoutType;
        p.token = token;
        p.softInputMode = this.mSoftInputMode;
        p.setTitle("PopupWindow:" + Integer.toHexString(hashCode()));
        return p;
    }

    private int computeFlags(int curFlags) {
        curFlags &= -8815129;
        if (this.mIgnoreCheekPress) {
            curFlags |= 32768;
        }
        if (!this.mFocusable) {
            curFlags |= 8;
            if (this.mInputMethodMode == 1) {
                curFlags |= SendFlag.FLAG_KEY_PINGBACK_1Q;
            }
        } else if (this.mInputMethodMode == 2) {
            curFlags |= SendFlag.FLAG_KEY_PINGBACK_1Q;
        }
        if (!this.mTouchable) {
            curFlags |= 16;
        }
        if (this.mOutsideTouchable) {
            curFlags |= SendFlag.FLAG_KEY_PINGBACK_MID;
        }
        if (!this.mClippingEnabled) {
            curFlags |= 512;
        }
        if (isSplitTouchEnabled()) {
            curFlags |= 8388608;
        }
        if (this.mLayoutInScreen) {
            curFlags |= 256;
        }
        if (this.mLayoutInsetDecor) {
            curFlags |= SendFlag.FLAG_KEY_PINGBACK_ST;
        }
        if (this.mNotTouchModal) {
            return curFlags | 32;
        }
        return curFlags;
    }

    private int computeAnimationResource() {
        if (this.mAnimationStyle == -1) {
            return 0;
        }
        return this.mAnimationStyle;
    }

    public void dismiss() {
        if (isShowing() && this.mPopupView != null) {
            this.mIsShowing = false;
            Animation animation = null;
            if (this.mShowMode == 1) {
                animation = new TranslateAnimation(1, 0.0f, 1, -1.0f, 1, 0.0f, 1, 0.0f);
            } else if (this.mShowMode == 2) {
                animation = new TranslateAnimation(1, 0.0f, 1, 1.0f, 1, 0.0f, 1, 0.0f);
            } else if (this.mShowMode == 3) {
                animation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, -1.0f);
            } else if (this.mShowMode == 4) {
                animation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
            } else if (this.mShowMode == 0) {
                animation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 1, ThreeDimensionalParams.TEXT_SCALE_FOR_3D, 1, ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
            }
            if (animation != null) {
                animation.setDuration((long) this.mAnimationDuration);
                animation.setFillAfter(true);
                this.mContentView.startAnimation(animation);
                this.mBackgroundContainer.startAnimation(animation);
                animation.setAnimationListener(new C20781());
            }
        }
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void update() {
        if (isShowing() && this.mContentView != null) {
            LayoutParams p = (LayoutParams) this.mPopupView.getLayoutParams();
            boolean update = false;
            int newAnim = computeAnimationResource();
            if (newAnim != p.windowAnimations) {
                p.windowAnimations = newAnim;
                update = true;
            }
            int newFlags = computeFlags(p.flags);
            if (newFlags != p.flags) {
                p.flags = newFlags;
                update = true;
            }
            if (update) {
                this.mWindowManager.updateViewLayout(this.mPopupView, p);
            }
        }
    }

    public void update(int width, int height) {
        LayoutParams p = (LayoutParams) this.mPopupView.getLayoutParams();
        update(p.x, p.y, width, height, false);
    }

    public void update(int x, int y, int width, int height) {
        update(x, y, width, height, false);
    }

    public void update(int x, int y, int width, int height, boolean force) {
        if (width != -1) {
            this.mLastWidth = width;
            setWidth(width);
        }
        if (height != -1) {
            this.mLastHeight = height;
            setHeight(height);
        }
        if (isShowing() && this.mContentView != null) {
            LayoutParams p = (LayoutParams) this.mPopupView.getLayoutParams();
            boolean update = force;
            int finalWidth = this.mWidthMode < 0 ? this.mWidthMode : this.mLastWidth;
            if (!(width == -1 || p.width == finalWidth)) {
                this.mLastWidth = finalWidth;
                p.width = finalWidth;
                update = true;
            }
            int finalHeight = this.mHeightMode < 0 ? this.mHeightMode : this.mLastHeight;
            if (!(height == -1 || p.height == finalHeight)) {
                this.mLastHeight = finalHeight;
                p.height = finalHeight;
                update = true;
            }
            if (p.x != x) {
                p.x = x;
                update = true;
            }
            if (p.y != y) {
                p.y = y;
                update = true;
            }
            int newAnim = computeAnimationResource();
            if (newAnim != p.windowAnimations) {
                p.windowAnimations = newAnim;
                update = true;
            }
            int newFlags = computeFlags(p.flags);
            if (newFlags != p.flags) {
                p.flags = newFlags;
                update = true;
            }
            if (update) {
                this.mWindowManager.updateViewLayout(this.mPopupView, p);
            }
        }
    }
}
