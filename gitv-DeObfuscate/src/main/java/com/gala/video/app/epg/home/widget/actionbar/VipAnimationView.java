package com.gala.video.app.epg.home.widget.actionbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarAdapter.VipOnFocusChangeListener;
import com.gala.video.lib.framework.core.cache.BuildCache;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class VipAnimationView extends LinearLayout {
    private final int ANINATION_DELAY = 120000;
    private final int MSG_START_VIP_ANIMATION = 0;
    private final String TAG = "VipAnimationView";
    private ActionBarAdapter mActionBarAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            VipAnimationView.this.startAnimation();
            sendEmptyMessageDelayed(0, HomeDataConfig.THEME_REQUEST_DELAY);
        }
    };
    private boolean mHasFocus;
    private ImageView mImageView;
    private int mLeft;
    private float mScale;
    private int mWidth;

    class C07032 implements VipOnFocusChangeListener {
        C07032() {
        }

        public void onFocusChanged(boolean hasFocus) {
            VipAnimationView.this.mHasFocus = hasFocus;
            AnimationUtil.zoomAnimationSoftwareRender(VipAnimationView.this, hasFocus, 1.05f, ActionBarAnimaitonUtils.getDelay());
            if (hasFocus) {
                VipAnimationView.this.startAnimation(false);
            }
        }
    }

    class C07043 implements Runnable {
        C07043() {
        }

        public void run() {
            VipAnimationView.this.startAnimation();
        }
    }

    class C07054 implements AnimationListener {
        C07054() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            VipAnimationView.this.mImageView.clearAnimation();
            VipAnimationView.this.mImageView.setVisibility(8);
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    public VipAnimationView(Context context) {
        super(context);
        init(context);
    }

    public VipAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VipAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayerType(1, null);
        setWillNotDraw(false);
        this.mImageView = new ImageView(context);
        LayoutParams params = new LayoutParams(-2, -2);
        params.leftMargin = -getImageWidth();
        params.gravity = 16;
        this.mImageView.setImageResource(C0508R.drawable.epg_vip_animation);
        this.mImageView.setVisibility(8);
        setGravity(16);
        addView(this.mImageView, params);
        this.mScale = ((float) getResources().getDisplayMetrics().widthPixels) / 1920.0f;
    }

    public void setActionBarAdpter(ActionBarAdapter adapter) {
        this.mActionBarAdapter = adapter;
        this.mActionBarAdapter.setVipOnFocusChangedListener(new C07032());
    }

    public void startAnimation(boolean delay) {
        if (Project.getInstance().getBuild().isEnabledVipAnimation()) {
            this.mHandler.removeMessages(0);
            if (!delay) {
                this.mHandler.postDelayed(new C07043(), 100);
            }
            this.mHandler.sendEmptyMessageDelayed(0, HomeDataConfig.THEME_REQUEST_DELAY);
        }
    }

    public void stopAnimation() {
        if (Project.getInstance().getBuild().isEnabledVipAnimation()) {
            this.mHandler.removeMessages(0);
            this.mImageView.clearAnimation();
            this.mImageView.setVisibility(8);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Project.getInstance().getBuild().isEnabledVipAnimation()) {
            Path path = new Path();
            float radius = ((float) getRawPixel(32.0f)) * 1.0f;
            path.moveTo(radius, radius);
            int startLeft = this.mHasFocus ? 1 : 0;
            path.addArc(new RectF((float) startLeft, 0.0f, (2.0f * radius) + ((float) startLeft), 2.0f * radius), 90.0f, 180.0f);
            path.moveTo(0.0f, radius);
            int leftCircleStart = this.mActionBarAdapter.getVipViewWidth() - getRawPixel(this.mHasFocus ? 38.0f : 47.0f);
            path.addRect(new RectF(radius, 0.0f, (float) leftCircleStart, 2.0f * radius), Direction.CCW);
            path.moveTo((float) leftCircleStart, radius);
            path.addArc(new RectF(((float) leftCircleStart) - radius, 0.0f, ((float) leftCircleStart) + radius, 2.0f * radius), -90.0f, 180.0f);
            if (this.mActionBarAdapter.getTipViewWidth() > 0) {
                float tipLeft = (((float) leftCircleStart) + radius) + ((float) getRawPixel(this.mHasFocus ? 19.0f : 22.0f));
                path.moveTo(tipLeft, 0.0f);
                path.addRect(new RectF(tipLeft, (float) getRawPixel(13.0f), ((float) getRawPixel(this.mHasFocus ? -18.0f : -15.0f)) + (tipLeft + ((float) this.mActionBarAdapter.getTipViewWidth())), (2.0f * radius) - ((float) getRawPixel(14.0f))), Direction.CCW);
            }
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            try {
                canvas.clipPath(path);
            } catch (Exception e) {
                LogUtils.m1569d("VipAnimationView", "onDraw", e);
                BuildCache.getInstance().putString(BuildConstance.APK_ENABLE_VIP_ANIMATION, "false");
                this.mHandler.removeMessages(0);
                setVisibility(8);
                sendPingback();
            }
        }
    }

    private void startAnimation() {
        if (this.mActionBarAdapter != null) {
            startAnimation(this.mActionBarAdapter.getAnimationLength(), this.mActionBarAdapter.getVipViewLeft());
        }
    }

    private void startAnimation(int length, int left) {
        this.mImageView.setVisibility(0);
        if (!(length == this.mWidth && left == this.mLeft)) {
            layout(length, left);
            this.mWidth = length;
            this.mLeft = left;
        }
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, (float) (getImageWidth() + length), 0.0f, 0.0f);
        translateAnimation.setDuration(2000);
        translateAnimation.setRepeatMode(1);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        translateAnimation.setAnimationListener(new C07054());
        this.mImageView.startAnimation(translateAnimation);
    }

    private void layout(int w, int left) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        layoutParams.width = w;
        layoutParams.leftMargin = getRawPixel(23.0f) + left;
        layoutParams.topMargin = getRawPixel(22.0f);
        invalidate();
    }

    private int getImageWidth() {
        return ResourceUtil.getDimensionPixelSize(C0508R.dimen.dimen_76dp);
    }

    public int getRawPixel(float pixel) {
        return Math.round(this.mScale * pixel);
    }

    private void sendPingback() {
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "11");
        params.add("ct", "vip_animation");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}
