package com.gala.video.lib.share.common.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.animation.LinearInterpolator;
import com.gala.cloudui.utils.CloudUtilsGala;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.SysPropUtils;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.uikit.view.FocusView;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.WeakHashMap;

public class CardFocusHelper implements OnScrollChangedListener {
    public static final int FOCUS_ANIMATION_TIME = R.id.animation_time;
    private static final int FOCUS_ANIM_DURATION = 200;
    public static final int FOCUS_RES = R.id.focus_res;
    public static final int FOCUS_RES_ENDS_WITH = R.id.focus_res_ends_with;
    static final int FORCE_INVISIBLE = 4;
    public static final int FocusRect = R.id.focus_rect;
    static final int HideFocusHL = 2;
    public static final int ItemDeltaHigh = R.id.item_delta_high;
    public static final int KeyNotScale = R.id.not_scale;
    static final int MaxCheckLoop = 8;
    static final int Move2LastFocus = 1;
    private static int MoveDelay = 10;
    public static final int RESOURCE_PADDING = R.id.resource_padding;
    static WeakHashMap<Context, WeakReference<CardFocusHelper>> SMgrMap = new WeakHashMap();
    private static final String TAG = CardFocusHelper.class.getSimpleName();
    static final int UPDATE_FOCUS = 3;
    private static HashMap<String, String> mResourceMap = new HashMap(4);
    private static boolean sDebugable;
    ValueAnimator mAnim = ValueAnimator.ofFloat(new float[]{1.0f, 1.1f});
    Callback mCb = new Callback() {
        public boolean handleMessage(Message aMsg) {
            switch (aMsg.what) {
                case 1:
                    CardFocusHelper.this.move2LastFocus(false);
                    break;
                case 2:
                    CardFocusHelper.this.invisibleFocus();
                    break;
                case 3:
                    CardFocusHelper.MoveDelay = 2;
                    CardFocusHelper.this.mRefreshTimes = (aMsg.arg1 / CardFocusHelper.MoveDelay) + 2;
                    CardFocusHelper.this.move2LastFocus(false);
                    break;
                case 4:
                    CardFocusHelper.this.mScrollListenerEnable = ((Boolean) aMsg.obj).booleanValue();
                    CardFocusHelper.this.mCheckLoop = 100;
                    CardFocusHelper.this.invisibleFocus();
                    LayoutParams lp = CardFocusHelper.this.mFocusHLT.getLayoutParams();
                    lp.width = 0;
                    lp.height = 0;
                    if (CardFocusHelper.sDebugable) {
                        Log.d(CardFocusHelper.TAG, "forceVisible false");
                        break;
                    }
                    break;
            }
            return true;
        }
    };
    int mCheckLoop;
    private boolean mDebugable = false;
    private int mDefaultPadding = ResourceUtil.getPx(24);
    private int mDeltaHeiht;
    FocusView mFocusHLT;
    Handler mHandler;
    private int mInvisiableMarginTop;
    View mLastFocus;
    int mLastX;
    int mLastY;
    float mPX;
    float mPY;
    private int mRefreshTimes = 8;
    private boolean mScrollListenerEnable;
    Rect mTmpP = new Rect();
    Rect mTmpR = new Rect();

    static {
        boolean z = true;
        mResourceMap.put("share_item_circle_bg_focus", "share_item_circle_bg_focus_home");
        mResourceMap.put("share_skew_image_bg_focus", "share_skew_image_bg_focus_home");
        mResourceMap.put("share_bg_focus_home", "share_bg_focus_home");
        mResourceMap.put("share_bg_focus_home_vip", "share_bg_focus_home_vip");
        if (SysPropUtils.getInt("log.focus.debug", 0) != 1) {
            z = false;
        }
        sDebugable = z;
    }

    public CardFocusHelper(View aFocusHL) {
        this.mFocusHLT = (FocusView) aFocusHL;
        SMgrMap.put(aFocusHL.getContext(), new WeakReference(this));
        aFocusHL.getViewTreeObserver().addOnScrollChangedListener(this);
        this.mHandler = new Handler(Looper.myLooper(), this.mCb);
        this.mAnim.setDuration(200);
        this.mAnim.setInterpolator(new LinearInterpolator());
        this.mAnim.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator anim) {
                CardFocusHelper.this.updateFocusRect(((Float) anim.getAnimatedValue()).floatValue());
            }
        });
        this.mAnim.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                if (CardFocusHelper.sDebugable || CardFocusHelper.this.mDebugable) {
                    Log.d(CardFocusHelper.TAG, "mDeltaHeiht = " + CardFocusHelper.this.mDeltaHeiht);
                }
            }

            public void onAnimationEnd(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void updateFocusRect(float scaleF) {
        modifyHLT(scaleF);
        updateXY(scaleF);
    }

    private void updateXY(float scale) {
        int curY = (int) (((((float) this.mTmpR.top) - (((((float) getNewHeight(scale)) + (((float) this.mDeltaHeiht) * scale)) - ((float) this.mTmpR.height())) * this.mPY)) + this.mLastFocus.getTranslationY()) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
        this.mFocusHLT.setX((float) ((int) (((float) this.mTmpR.left) - (((float) (getNewWidth(scale) - this.mTmpR.width())) * this.mPX))));
        this.mFocusHLT.setY((float) curY);
    }

    public void setInvisiableMarginTop(int marginTop) {
        this.mInvisiableMarginTop = marginTop;
        if (this.mFocusHLT instanceof FocusView) {
            this.mFocusHLT.setInvisiableMarginTop(this.mInvisiableMarginTop);
        }
    }

    void modifyHLT(float aScale) {
        LayoutParams lp = this.mFocusHLT.getLayoutParams();
        int nw = getNewWidth(aScale);
        int nh = getNewHeight(aScale);
        if (!(lp.width == nw && lp.height == nh)) {
            lp.width = nw;
            lp.height = nh;
        }
        this.mFocusHLT.requestLayout();
    }

    private int getNewHeight(float aScale) {
        int h = (int) ((((((float) this.mTmpR.height()) * aScale) + ((float) this.mTmpP.top)) + ((float) this.mTmpP.bottom)) - (((float) this.mDeltaHeiht) * aScale));
        if (h % 2 == 1) {
            h--;
        }
        if (sDebugable) {
            Log.d(TAG, "getNewHeight = " + h);
        }
        return h;
    }

    private int getNewWidth(float aScale) {
        return (int) (((((float) this.mTmpR.width()) * aScale) + ((float) this.mTmpP.left)) + ((float) this.mTmpP.right));
    }

    public void viewGotFocus(View aFV) {
        this.mHandler.removeMessages(2);
        boolean focusChanged = this.mLastFocus != aFV || 4 == this.mFocusHLT.getVisibility();
        this.mLastFocus = aFV;
        Log.d(TAG, "viewGotFocus mLastFocus " + this.mLastFocus);
        this.mScrollListenerEnable = true;
        clearAnimation();
        move2LastFocus(focusChanged);
    }

    public void viewLostFocus(View aFV) {
        Log.d(TAG, "viewLostFocus mLastFocus " + this.mLastFocus + " lostFocus = " + aFV);
        if (this.mLastFocus == aFV) {
            this.mHandler.sendEmptyMessageDelayed(2, 0);
        }
    }

    void move2LastFocus(boolean aFocusChanged, boolean forceInvisible) {
        if (this.mLastFocus == null) {
            invisibleFocus();
            this.mFocusHLT.invalidate();
            resetSampleRate();
        } else if (this.mLastFocus.hasFocus()) {
            try {
                this.mFocusHLT.setVisibility(4);
                boolean noRect = false;
                if (this.mLastFocus.getTag(FocusRect) != null) {
                    noRect = ((Boolean) this.mLastFocus.getTag(FocusRect)).booleanValue();
                }
                String baseRes = null;
                if (noRect || forceInvisible) {
                    invisibleFocus();
                } else {
                    String resStr;
                    this.mFocusHLT.setVisibility(0);
                    Object resTag = this.mLastFocus.getTag(R.id.focus_res);
                    if (resTag != null) {
                        resStr = (String) resTag;
                    } else {
                        resStr = "share_bg_focus_home";
                    }
                    baseRes = resStr;
                    Object endsTag = this.mLastFocus.getTag(R.id.focus_res_ends_with);
                    if (endsTag != null) {
                        resStr = StringUtils.append(resStr, (String) endsTag);
                    } else {
                        resStr = (String) mResourceMap.get(resStr);
                        baseRes = resStr;
                    }
                    if (sDebugable) {
                        Log.d(TAG, "resStr = " + resStr + "  resTag = " + resTag);
                    }
                    this.mFocusHLT.setBackgroundDrawable(CloudUtilsGala.getDrawableFromResidStr(resStr));
                }
                this.mLastFocus.getDrawingRect(this.mTmpR);
                if (this.mLastFocus.getTag(ItemDeltaHigh) != null) {
                    this.mDeltaHeiht = ((Integer) this.mLastFocus.getTag(ItemDeltaHigh)).intValue();
                    if (this.mDeltaHeiht == 0 && (sDebugable || this.mDebugable)) {
                        Log.d(TAG, "focusView = " + this.mLastFocus);
                    }
                } else {
                    this.mDeltaHeiht = 0;
                }
                if (sDebugable || this.mDebugable) {
                    Log.d(TAG, "mDeltaHeiht = " + this.mDeltaHeiht);
                }
                float startScale = 1.0f;
                if (this.mLastFocus.getTag(R.id.focus_start_scale) != null) {
                    startScale = ((Float) this.mLastFocus.getTag(R.id.focus_start_scale)).floatValue();
                }
                float scale = 1.1f;
                if (this.mLastFocus.getTag(R.id.focus_end_scale) != null) {
                    scale = ((Float) this.mLastFocus.getTag(R.id.focus_end_scale)).floatValue();
                }
                if (sDebugable || this.mDebugable) {
                    Log.d(TAG, "startScale:" + startScale + "  endScale:" + scale + " orW:" + this.mLastFocus.getWidth() + " orH:" + this.mLastFocus.getHeight());
                }
                if (this.mLastFocus.getTag(FOCUS_ANIMATION_TIME) != null) {
                    this.mAnim.setDuration((long) ((Integer) this.mLastFocus.getTag(FOCUS_ANIMATION_TIME)).intValue());
                } else {
                    this.mAnim.setDuration(200);
                }
                this.mAnim.setFloatValues(new float[]{startScale, scale});
                ((ViewGroup) this.mFocusHLT.getParent()).offsetDescendantRectToMyCoords(this.mLastFocus, this.mTmpR);
                if (sDebugable || this.mDebugable) {
                    Log.d(TAG, "focus view rect " + this.mTmpR + "  H:" + this.mTmpR.height());
                }
                Rect padding = getDrawablePadding();
                if (this.mFocusHLT.getBackground() instanceof NinePatchDrawable) {
                    ((NinePatchDrawable) this.mFocusHLT.getBackground()).getPadding(this.mTmpP);
                    if (padding != null) {
                        this.mTmpP.left = padding.left;
                        this.mTmpP.top = padding.top;
                        this.mTmpP.right = padding.right;
                        this.mTmpP.bottom = padding.bottom;
                    }
                } else if (TextUtils.isEmpty(baseRes)) {
                    this.mFocusHLT.getBackground().getPadding(this.mTmpP);
                    if (padding != null) {
                        this.mTmpP.left = padding.left;
                        this.mTmpP.top = padding.top;
                        this.mTmpP.right = padding.right;
                        this.mTmpP.bottom = padding.bottom;
                    }
                } else {
                    setPadding(this.mTmpP, padding);
                }
                if (sDebugable || this.mDebugable) {
                    Log.d(TAG, "focus view paddingRect " + this.mTmpP);
                }
                boolean notScale = this.mLastFocus.getTag(KeyNotScale) != null;
                if (aFocusChanged && !notScale) {
                    if (sDebugable || this.mDebugable) {
                        Log.d(TAG, "mAnim.start");
                    }
                    if (this.mAnim.isStarted()) {
                        this.mAnim.end();
                    }
                    this.mAnim.start();
                }
                float value = 1.0f;
                if (notScale) {
                    if (sDebugable) {
                        Log.d(TAG, "notScale to cancel animation");
                    }
                    this.mAnim.cancel();
                } else {
                    value = ((Float) this.mAnim.getAnimatedValue()).floatValue();
                }
                modifyHLT(value);
                this.mPX = ThreeDimensionalParams.TEXT_SCALE_FOR_3D;
                this.mPY = ((float) (this.mDeltaHeiht / this.mTmpR.height())) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D;
                int curX = (int) (((float) this.mTmpR.left) - (((float) (getNewWidth(value) - this.mTmpR.width())) * this.mPX));
                int curY = (int) (((((float) this.mTmpR.top) - (((((float) getNewHeight(value)) + (((float) this.mDeltaHeiht) * value)) - ((float) this.mTmpR.height())) * this.mPY)) + this.mLastFocus.getTranslationY()) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
                this.mFocusHLT.setX((float) curX);
                this.mFocusHLT.setY((float) curY);
                if (curX == this.mLastX && curY == this.mLastY) {
                    this.mCheckLoop++;
                    if (this.mCheckLoop < 8 || this.mCheckLoop < this.mRefreshTimes) {
                        if (sDebugable || this.mDebugable) {
                            Log.d(TAG, "verify2 TY = " + this.mLastFocus.getTranslationY() + " pivotX = " + this.mFocusHLT.getPivotX() + " pivotY = " + this.mFocusHLT.getPivotY());
                        }
                        postCheckMsg();
                    } else {
                        resetSampleRate();
                        if (sDebugable || this.mDebugable) {
                            Log.d(TAG, "not verify FocusH = " + this.mFocusHLT.getHeight() + " FocusW = " + this.mFocusHLT.getWidth() + " pivotX = " + this.mFocusHLT.getPivotX() + " pivotY = " + this.mFocusHLT.getPivotY());
                        }
                    }
                } else {
                    if (sDebugable || this.mDebugable) {
                        Log.d(TAG, "verify1 TY = " + this.mLastFocus.getTranslationY() + " pivotX = " + this.mFocusHLT.getPivotX() + " pivotY = " + this.mFocusHLT.getPivotY());
                    }
                    postCheckMsg();
                    this.mCheckLoop = 0;
                }
                this.mLastX = curX;
                this.mLastY = curY;
            } catch (Exception e) {
                Log.d(TAG, "view recycled");
            }
        } else {
            invisibleFocus();
            if (sDebugable || this.mDebugable) {
                Log.d(TAG, "move2LastFocus mLastFocus has no focus visible = " + (this.mFocusHLT.getVisibility() != 4));
            }
            this.mFocusHLT.invalidate();
        }
    }

    private void resetSampleRate() {
        this.mRefreshTimes = 8;
        MoveDelay = 10;
    }

    void move2LastFocus(boolean aFocusChanged) {
        move2LastFocus(aFocusChanged, false);
    }

    private void setPadding(Rect desRect, Rect srcRect) {
        if (srcRect == null) {
            desRect.set(this.mDefaultPadding, this.mDefaultPadding, this.mDefaultPadding, this.mDefaultPadding);
        } else {
            desRect.set(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
        }
    }

    private Rect getDrawablePadding() {
        Rect paddingObject = this.mLastFocus.getTag(R.id.resource_padding);
        if (paddingObject != null) {
            return paddingObject;
        }
        return null;
    }

    public static void triggerFoucs(View view, boolean hasFocus) {
        CardFocusHelper mgr = getMgr(view.getContext());
        if (mgr == null) {
            return;
        }
        if (hasFocus) {
            mgr.viewGotFocus(view);
        } else {
            mgr.viewLostFocus(view);
        }
    }

    public static void updateFocusDraw(Context context, int duration) {
        CardFocusHelper mgr = getMgr(context);
        if (mgr != null) {
            mgr.updateFocusDraw(duration);
        }
    }

    public void updateFocusDraw(int duration) {
        if (sDebugable || this.mDebugable) {
            Log.d(TAG, "updateFocusDraw");
        }
        if (this.mScrollListenerEnable) {
            this.mCheckLoop = 0;
            this.mHandler.obtainMessage(3, duration, 0).sendToTarget();
        }
    }

    public static void setMarginLR(Context context, int marginLeft, int marginRight) {
        CardFocusHelper cfh = getMgr(context);
        if (cfh != null) {
            cfh.mFocusHLT.setMarginLeft(marginLeft);
            cfh.mFocusHLT.setMarginRight(marginRight);
        }
    }

    public static CardFocusHelper getMgr(Context aCt) {
        WeakReference<CardFocusHelper> ref = (WeakReference) SMgrMap.get(aCt);
        if (ref != null) {
            return (CardFocusHelper) ref.get();
        }
        return null;
    }

    public void setScrollListenerEnable(boolean scrollListenerEnable) {
        if (sDebugable || this.mDebugable) {
            Log.d(TAG, "setScrollListenerEnable false by tab");
        }
        this.mScrollListenerEnable = scrollListenerEnable;
        if (!scrollListenerEnable) {
            invisibleFocus();
        }
    }

    void postCheckMsg() {
        if (this.mScrollListenerEnable) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, (long) MoveDelay);
        }
    }

    public void disableFocusVisible() {
        if (this.mLastFocus != null) {
            this.mHandler.removeMessages(1);
            Message msg = Message.obtain();
            msg.what = 2;
            this.mHandler.sendMessageAtFrontOfQueue(msg);
            this.mAnim.end();
            move2LastFocus(false, true);
        }
        if (sDebugable) {
            Log.d(TAG, "disableFocusVisible visible");
        }
    }

    public static void edgeEffect(Context context, int direction, long duration, float cycle, float px) {
        CardFocusHelper mgr = getMgr(context);
        if (mgr != null) {
            boolean noRect = false;
            if (!(mgr.mLastFocus == null || mgr.mLastFocus.getTag(FocusRect) == null)) {
                noRect = ((Boolean) mgr.mLastFocus.getTag(FocusRect)).booleanValue();
            }
            if (!noRect) {
                AnimationUtil.shakeAnimation(context, mgr.mFocusHLT, direction, duration, cycle, px);
            }
        }
    }

    public static void forceVisible(Context context, boolean visible) {
        CardFocusHelper mgr = getMgr(context);
        if (mgr != null && !visible) {
            Message msg = Message.obtain();
            msg.what = 4;
            msg.obj = Boolean.valueOf(!visible);
            mgr.mHandler.sendMessageAtFrontOfQueue(msg);
        }
    }

    private void invisibleFocus() {
        this.mFocusHLT.setVisibility(4);
        clearAnimation();
    }

    private void clearAnimation() {
        if (this.mFocusHLT.getAnimation() != null) {
            this.mFocusHLT.clearAnimation();
            this.mFocusHLT.setTag(AnimationUtil.SHAKE_X, null);
            this.mFocusHLT.setTag(AnimationUtil.SHAKE_Y, null);
        }
    }

    public void onScrollChanged() {
        if (this.mLastFocus != null && this.mScrollListenerEnable) {
            if (sDebugable) {
                Log.d(TAG, "onScrollChanged");
            }
            if (this.mLastFocus.hasFocus()) {
                move2LastFocus(false);
            } else {
                invisibleFocus();
            }
        } else if (!this.mScrollListenerEnable) {
            if (sDebugable) {
                Log.d(TAG, "disable scroll");
            }
            disableFocusVisible();
        }
    }

    public void destroy() {
        this.mFocusHLT.getViewTreeObserver().removeOnScrollChangedListener(this);
    }
}
