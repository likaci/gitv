package com.gala.video.lib.share.common.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import com.gala.cloudui.constants.CuteConstants;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryInfoHelper;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

public class QToast {
    private static final int DELAY_SHOW = 40;
    private static final float LEFT_RIGHT_WIDTH = ((float) ResourceUtil.getDimen(C1632R.dimen.dimen_48dp));
    public static final int LENGTH_3000 = 3000;
    public static final int LENGTH_4000 = 4000;
    public static final int LENGTH_LONG = 3500;
    public static final int LENGTH_SHORT = 2000;
    private static final String TAG = "QToast";
    private static final int TEXT_22_LENGTH = 22;
    static String mActivityName = "";
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    private static LayoutParams mParams = new LayoutParams();
    private static QToastStatusListener mQToastStatusListener;
    private static WeakReference<View> mReference;
    private static QToast mToast;
    private static WindowManager sWindowManager;
    private int mDuration = 2000;
    private int mGravity = 81;
    private final Runnable mHide = new C16523();
    private float mHorizontalMargin;
    private View mNextView;
    private final Runnable mShow = new C16512();
    private float mVerticalMargin;
    private View mView;
    private int mX = 0;
    private int mY = getToastMarginBottom();

    public interface QToastStatusListener {
        void onHide();

        void onShow();
    }

    class C16512 implements Runnable {
        C16512() {
        }

        public void run() {
            QToast.this.handleShow();
        }
    }

    class C16523 implements Runnable {
        C16523() {
        }

        public void run() {
            QToast.this.handleHide();
        }
    }

    private int getToastMarginBottom() {
        return ResourceUtil.getResource().getDimensionPixelSize(C1632R.dimen.dimen_80dp);
    }

    public static QToast get(Context context) {
        if (mToast == null) {
            mToast = new QToast(context);
        }
        mParams = getParams();
        return mToast;
    }

    private QToast(Context context) {
        init(context);
    }

    public static void makeTextAndShow(final Context context, final CharSequence text, final int duration) {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                QToast.makeText(context, text, duration).show();
            }
        }, 40);
    }

    public static void makeTextAndShow(Context context, int rId, int duration) {
        makeText(context, context.getResources().getText(rId), duration).show();
    }

    public static QToast makeText(Context context, CharSequence text, int duration) {
        TextView textView;
        Log.e(TAG, "makeText ---- text = " + text + ", duration = " + duration);
        if (duration == 1) {
            duration = LENGTH_LONG;
        } else if (duration == 0) {
            duration = 2000;
        }
        mToast = get(context);
        if (mToast.mView != null) {
            textView = (TextView) mToast.mView.findViewById(C1632R.id.share_txt_toastmsg);
            if (!(textView == null || StringUtils.isEmpty(text) || !text.equals(textView.getText()))) {
                mToast.mNextView = mToast.mView;
                mToast.mDuration = duration;
                return mToast;
            }
        }
        View toastLayout = LayoutInflater.from(AppRuntimeEnv.get().getApplicationContext()).inflate(C1632R.layout.share_toastview, null);
        textView = (TextView) toastLayout.findViewById(C1632R.id.share_txt_toastmsg);
        measureAndParam(text, textView);
        if (text.toString().contains(CuteConstants.FONT)) {
            textView.setText(Html.fromHtml(text.toString()));
        } else {
            textView.setText(text);
        }
        mToast.mNextView = toastLayout;
        mToast.mDuration = duration;
        return mToast;
    }

    private static void measureAndParam(CharSequence text, TextView textView) {
        String temp;
        if (text.toString().contains(CuteConstants.FONT)) {
            temp = getRealString(text.toString());
        } else {
            temp = text.toString();
        }
        if (temp.length() > 22) {
            temp = temp.substring(0, 22);
        }
        float measureW = textView.getPaint().measureText(temp.trim());
        LogUtils.m1574i(TAG, "measureAndParam ---measureW: " + measureW);
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        int i = (int) (LEFT_RIGHT_WIDTH + measureW);
        mParams.width = i;
        layoutParams.width = i;
    }

    private static String getRealString(String toast) {
        return Pattern.compile("<.+?>", 32).matcher(toast).replaceAll("");
    }

    public static QToast makeText(Context context, int rId, int duration) {
        return makeText(context, context.getText(rId), duration);
    }

    public void setView(View view) {
        this.mNextView = view;
    }

    public View getView() {
        return this.mNextView;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
        this.mHorizontalMargin = horizontalMargin;
        this.mVerticalMargin = verticalMargin;
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        this.mGravity = gravity;
        this.mX = xOffset;
        this.mY = yOffset;
    }

    public void show() {
        try {
            mHandler.removeCallbacks(this.mHide);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHandler.post(this.mShow);
        if (this.mDuration > 0) {
            mHandler.postDelayed(this.mHide, (long) this.mDuration);
        } else {
            LogUtils.m1571e(TAG, "QToast.show duration must be positive ......");
        }
    }

    public void hide() {
        mHandler.post(this.mHide);
    }

    private static void init(Context context) {
        sWindowManager = (WindowManager) context.getApplicationContext().getSystemService("window");
    }

    private static LayoutParams getParams() {
        LayoutParams params = new LayoutParams();
        params.gravity = 80;
        params.height = -2;
        params.width = -2;
        params.flags = 152;
        params.format = -3;
        params.windowAnimations = C1632R.style.custom_toast_anim;
        params.type = 2005;
        return params;
    }

    private void handleShow() {
        if (this.mView != this.mNextView) {
            handleHide();
            this.mView = this.mNextView;
            int gravity = this.mGravity;
            mParams.gravity = gravity;
            if ((gravity & 7) == 7) {
                mParams.horizontalWeight = 1.0f;
            }
            if ((gravity & HistoryInfoHelper.MSG_MERGE) == HistoryInfoHelper.MSG_MERGE) {
                mParams.verticalWeight = 1.0f;
            }
            mParams.x = this.mX;
            mParams.y = this.mY;
            mParams.verticalMargin = this.mVerticalMargin;
            mParams.horizontalMargin = this.mHorizontalMargin;
            try {
                if (this.mView.getParent() != null) {
                    sWindowManager.removeView(this.mView);
                }
                sWindowManager.addView(this.mView, mParams);
                onToastShow(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleHide() {
        if (this.mView != null) {
            if (this.mView.getParent() != null) {
                sWindowManager.removeView(this.mView);
                onToastShow(false);
            }
            this.mView = null;
        }
    }

    public static void hidePreToast() {
        if (mReference != null && mReference.get() != null) {
            View toastContent = (View) mReference.get();
            if (toastContent != null) {
                toastContent.setVisibility(8);
                try {
                    ((WindowManager) AppRuntimeEnv.get().getApplicationContext().getSystemService("window")).removeViewImmediate(toastContent);
                } catch (Exception e) {
                    LogUtils.m1568d(TAG, "hidePlayerToast catch = " + e.getMessage());
                }
                mReference = null;
                if (mQToastStatusListener != null) {
                    mQToastStatusListener.onHide();
                }
            }
        }
    }

    public static void registerStatusListener(QToastStatusListener l) {
        mQToastStatusListener = l;
    }

    public static void unregisterStatusListener() {
        mQToastStatusListener = null;
    }

    private void onToastShow(boolean isshow) {
        if (isshow) {
            this.mView.setVisibility(0);
            if (mQToastStatusListener != null) {
                mQToastStatusListener.onShow();
            }
            mReference = new WeakReference(this.mView);
            return;
        }
        this.mView.setVisibility(8);
        if (mQToastStatusListener != null) {
            mQToastStatusListener.onHide();
        }
    }

    public static void setCurrent(String actName) {
        Log.e(TAG, "--- setCurrent --- " + actName + ", original = " + mActivityName);
        if (actName != null && mActivityName != null && !actName.equals(mActivityName)) {
            mActivityName = actName;
            hidePreToast();
        }
    }
}
