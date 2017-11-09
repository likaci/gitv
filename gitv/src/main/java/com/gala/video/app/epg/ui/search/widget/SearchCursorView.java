package com.gala.video.app.epg.ui.search.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.ResourceUtil;

public class SearchCursorView extends TextView {
    private static final String LOG_TAG = "EPG/widget/CursorTextView";
    private int animation_temp_param = 0;
    private int mCurOffsetX;
    private String mCurrentText = null;
    private int mCursorColor;
    private CursorHandler mCursorHandler = null;
    private int mCursorIndex;
    private HandlerThread mCursorThread = null;
    private final int mCursorWidth = 1;
    private String mHint = null;
    private SpannableString mHintString;
    private boolean mIsPostShow = false;
    private boolean mIsPreShow = false;
    private boolean mIsRunning = false;
    private NextRightFocusListener mListener;
    private Drawable mPostCursorDrawable = null;
    private Drawable mPreCursorDrawable = null;
    private int mScrollCompleteAnimDuration = 200;
    private Interpolator mScrollCompleteAnimFirstInterpolator = new DecelerateInterpolator();
    private int mScrollCompleteAnimHeight = 20;
    private Interpolator mScrollCompleteAnimSecondInterpolator = new AccelerateInterpolator();
    private int mSpaceWidth = -1;
    private int mStartOffsetX;
    private float mTextWidth = -1.0f;

    public interface NextRightFocusListener {
        void onNextRightFocusChanged();
    }

    private static class CursorHandler extends Handler {
        private long interval;
        private SearchCursorView view;

        public CursorHandler(Looper looper, SearchCursorView view, long interval) {
            super(looper);
            this.view = view;
            this.interval = interval;
        }

        public void handleMessage(Message msg) {
            if (this.view.isRunning()) {
                this.view.postInvalidate();
                sendEmptyMessageDelayed(0, this.interval);
            }
        }
    }

    public void setListener(NextRightFocusListener listener) {
        this.mListener = listener;
    }

    public SearchCursorView(Context context) {
        super(context);
        init();
    }

    public SearchCursorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchCursorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.mHintString = new SpannableString("  输入片名/人名的首字母或全拼");
        this.mHintString.setSpan(new AbsoluteSizeSpan(ResourceUtil.getPx(28)), 2, 16, 33);
        this.mHintString.setSpan(new ForegroundColorSpan(ResourceUtil.getColor(R.color.search_focus_text)), 1, 10, 33);
        this.mHintString.setSpan(new ForegroundColorSpan(ResourceUtil.getColor(R.color.albumview_yellow_color)), 10, 13, 33);
        this.mHintString.setSpan(new ForegroundColorSpan(ResourceUtil.getColor(R.color.search_focus_text)), 13, 14, 33);
        this.mHintString.setSpan(new ForegroundColorSpan(ResourceUtil.getColor(R.color.albumview_yellow_color)), 14, 16, 33);
        setBackgroundResource(R.drawable.epg_full_keyboard_bg);
        setPadding(ResourceUtil.getDimen(R.dimen.dimen_29dp), 0, 0, 0);
        setHint(this.mHintString);
        this.mCurOffsetX = getPaddingLeft() + 1;
        this.mCursorColor = ResourceUtil.getColor(R.color.keyboard_letter);
        Rect bounds = new Rect();
        getPaint().getTextBounds("a a", 0, "a a".length(), bounds);
        this.mSpaceWidth = bounds.right;
        getPaint().getTextBounds("aa", 0, "aa".length(), bounds);
        Log.d(LOG_TAG, "textBounds=" + bounds.right);
        this.mSpaceWidth -= bounds.right;
        getHintString();
    }

    public void startCursor(long interval) {
        stopCursor();
        this.mIsRunning = true;
        this.mCursorThread = new HandlerThread(getId() + "");
        this.mCursorThread.start();
        this.mCursorHandler = new CursorHandler(this.mCursorThread.getLooper(), this, interval);
        this.mCursorHandler.sendEmptyMessage(0);
    }

    public void stopCursor() {
        if (this.mCursorHandler != null) {
            this.mCursorHandler.removeMessages(0);
            this.mCursorHandler = null;
        }
        if (this.mCursorThread != null) {
            this.mCursorThread.quit();
            this.mCursorThread.interrupt();
            this.mCursorThread = null;
        }
        this.mIsRunning = false;
        postInvalidate();
    }

    public String getCurrentText() {
        return this.mCurrentText;
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    private void distory() {
        this.mIsRunning = false;
        this.mIsPreShow = false;
        this.mIsPostShow = false;
        this.mCursorThread = null;
        this.mPreCursorDrawable = null;
        this.mPostCursorDrawable = null;
        this.mCurrentText = null;
        this.mHint = null;
    }

    protected void onDetachedFromWindow() {
        stopCursor();
        distory();
        super.onDetachedFromWindow();
    }

    public void setText(CharSequence text, BufferType type) {
        Log.d(LOG_TAG, "EPG/widget/CursorTextView------ setText(CharSequence text, BufferType type)");
        this.mCurrentText = text.toString();
        if (this.mCurrentText == null || this.mCurrentText.equals(getHintString())) {
            this.mCursorIndex = -1;
            super.setText(text, type);
            return;
        }
        if (this.mTextWidth <= 0.0f) {
            this.mTextWidth = (float) ((getWidth() - getPaddingLeft()) - getPaddingRight());
        }
        Log.d(LOG_TAG, "EPG/widget/CursorTextView--setText()---mTextWidth" + this.mTextWidth);
        super.setText(TextUtils.ellipsize(text, getPaint(), this.mTextWidth, TruncateAt.START), type);
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCursor(canvas);
    }

    private void drawCursor(Canvas canvas) {
        String currentText = getText().toString();
        if (currentText != null) {
            if (currentText.isEmpty()) {
                drawCursorForHint(canvas);
            } else {
                drawCursorForText(canvas);
            }
        }
    }

    private void drawCursorForHint(Canvas canvas) {
        if (this.mPreCursorDrawable == null) {
            this.mPreCursorDrawable = new ColorDrawable(this.mCursorColor);
            this.mPreCursorDrawable.setBounds(0, (int) (((double) getHeight()) / 3.2d), 1, getHeight() - ((int) (((double) getHeight()) / 3.2d)));
        }
        if (!this.mIsRunning || this.mIsPreShow) {
            this.mPreCursorDrawable.setAlpha(0);
            this.mIsPreShow = false;
        } else {
            this.mPreCursorDrawable.setAlpha(255);
            this.mIsPreShow = true;
        }
        this.mStartOffsetX = (getPaddingLeft() + 1) + ResourceUtil.getPx(55);
        this.mCurOffsetX = this.mStartOffsetX;
        canvas.save();
        canvas.translate((float) this.mCurOffsetX, 0.0f);
        this.mPreCursorDrawable.draw(canvas);
        canvas.restore();
    }

    private void drawCursorForText(Canvas canvas) {
        if (this.mPostCursorDrawable == null) {
            this.mPostCursorDrawable = new ColorDrawable(this.mCursorColor);
            this.mPostCursorDrawable.setBounds(0, (int) (((double) getHeight()) / 3.2d), 1, getHeight() - ((int) (((double) getHeight()) / 3.2d)));
        }
        if (!this.mIsRunning || this.mIsPostShow) {
            this.mPostCursorDrawable.setAlpha(0);
            this.mIsPostShow = false;
        } else {
            this.mPostCursorDrawable.setAlpha(255);
            this.mIsPostShow = true;
        }
        Rect bounds = new Rect();
        String stringWithSpace = getText().toString().replace(" ", "");
        getPaint().getTextBounds(stringWithSpace, 0, stringWithSpace.length(), bounds);
        canvas.save();
        canvas.translate((float) this.mCurOffsetX, 0.0f);
        this.mPostCursorDrawable.draw(canvas);
        canvas.restore();
    }

    private String getHintString() {
        if (this.mHint != null) {
            return this.mHint;
        }
        if (getHint() != null) {
            this.mHint = getHint().toString();
        } else {
            this.mHint = "";
        }
        return this.mHint;
    }

    public int getCursorColor() {
        return this.mCursorColor;
    }

    public void setCursorColor(int cursorColor) {
        this.mCursorColor = cursorColor;
        this.mPreCursorDrawable = new ColorDrawable(this.mCursorColor);
        this.mPreCursorDrawable.setBounds(0, (int) (((double) getHeight()) / 3.2d), 1, getHeight() - ((int) (((double) getHeight()) / 3.2d)));
        this.mPostCursorDrawable = new ColorDrawable(this.mCursorColor);
        this.mPostCursorDrawable.setBounds(0, (int) (((double) getHeight()) / 3.2d), 1, getHeight() - ((int) (((double) getHeight()) / 3.2d)));
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.dispatchKeyEvent(event);
        }
        switch (event.getKeyCode()) {
            case 21:
                Log.d(LOG_TAG, "EPG/widget/CursorTextView---KeyEvent.KEYCODE_DPAD_RIGHT---mCursorIndex = " + this.mCursorIndex);
                if (this.mCursorIndex == -1) {
                    Log.e(LOG_TAG, "EPG/widget/CursorTextView---dispatchKeyEvent()---最左边   --- 左移 无效");
                    exeScrollOverAnimation(-1);
                    return true;
                }
                String indexChar = getText().charAt(this.mCursorIndex) + "";
                Log.d(LOG_TAG, "EPG/widget/CursorTextView---dispatchKeyEvent()----getText().charAt(mCursorIndex)----" + this.mCursorIndex + "----" + indexChar);
                getPaint().setTextSize(getTextSize());
                int indexX = (int) getPaint().measureText(indexChar);
                Log.d(LOG_TAG, "EPG/widget/CursorTextView---dispatchKeyEvent()---getPaint().measureText(indexChar) ----" + indexChar + "----" + indexX);
                this.mCurOffsetX -= indexX;
                this.mCursorIndex--;
                invalidate();
                return true;
            case 22:
                Log.d(LOG_TAG, "EPG/widget/CursorTextView---dispatchKeyEvent()---KeyEvent.KEYCODE_DPAD_RIGHT---mCursorIndex = " + this.mCursorIndex);
                if (this.mCursorIndex == getText().length() - 1) {
                    Log.e(LOG_TAG, "EPG/widget/CursorTextView---dispatchKeyEvent()---最右边   --- 右移无效");
                    if (this.mListener == null) {
                        return true;
                    }
                    this.mListener.onNextRightFocusChanged();
                    return true;
                }
                String indexChar2 = getText().charAt(this.mCursorIndex + 1) + "";
                Log.d(LOG_TAG, "EPG/widget/CursorTextView---dispatchKeyEvent()---getText().charAt(mCursorIndex)----" + this.mCursorIndex + "----" + indexChar2);
                getPaint().setTextSize(getTextSize());
                int indexX2 = (int) getPaint().measureText(indexChar2);
                Log.d(LOG_TAG, "EPG/widget/CursorTextView---dispatchKeyEvent()---getPaint().measureText(indexChar) ----" + indexChar2 + "----" + indexX2);
                this.mCurOffsetX += indexX2;
                this.mCursorIndex++;
                invalidate();
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    public void delete() {
        if (this.mCursorIndex < 0 || getText() == null || getText().length() == 0) {
            Log.e(LOG_TAG, "EPG/widget/CursorTextView----Fail Delete --- mCursorIndex<0 --- illegality! ");
            return;
        }
        String currentText = getText().toString();
        if (currentText.length() == 1) {
            clear();
            return;
        }
        String deleteTarget = Character.valueOf(currentText.charAt(this.mCursorIndex)).toString();
        Log.i(LOG_TAG, "EPG/widget/CursorTextView---delete --- " + this.mCursorIndex + " --- " + deleteTarget);
        getPaint().setTextSize(getTextSize());
        int indexX = (int) getPaint().measureText(deleteTarget);
        Log.i(LOG_TAG, "EPG/widget/CursorTextView----setText()---getPaint().measureText(indexChar) ----" + this.mCurrentText + "----" + indexX);
        this.mCurOffsetX -= indexX;
        if (this.mCursorIndex == 0) {
            currentText = currentText.substring(1);
        } else if (this.mCursorIndex == currentText.length() - 1) {
            currentText = currentText.substring(0, currentText.length() - 1);
        } else {
            String preText = currentText.substring(0, this.mCursorIndex);
            currentText = preText + currentText.substring(this.mCursorIndex + 1, currentText.length());
        }
        this.mCursorIndex--;
        setText(currentText);
    }

    public void clear() {
        Log.i(LOG_TAG, "EPG/widget/CursorTextView---Clear CursorTextView");
        this.mCursorIndex = -1;
        setText("");
    }

    public boolean appendText(String text) {
        if (text == null) {
            return false;
        }
        StringBuilder builder = new StringBuilder("");
        String oldText = getText().toString();
        int length = oldText.length();
        if (length == 0) {
            this.mCursorIndex = -1;
        }
        this.mCurrentText = text;
        if (!textIsValide(oldText)) {
            return false;
        }
        this.mCurOffsetX += (int) getPaint().measureText(text);
        if (this.mCursorIndex < length - 1) {
            String preText = oldText.substring(0, this.mCursorIndex + 1);
            builder = builder.append(preText).append(text).append(oldText.substring(this.mCursorIndex + 1, length));
        } else if (this.mCursorIndex == length - 1) {
            builder = builder.append(oldText).append(text);
        }
        this.mCursorIndex += this.mCurrentText.length();
        setText(builder.toString());
        return true;
    }

    public boolean textIsValide(String text) {
        getPaint().setTextSize(getTextSize());
        if (((int) getPaint().measureText(text)) < ((getWidth() - getPaddingLeft()) - ((int) getResources().getDimension(R.dimen.dimen_16dp))) - ((int) getPaint().measureText("WW1"))) {
            return true;
        }
        exeScrollOverAnimation(1);
        return false;
    }

    public boolean isDeletable() {
        if (this.mCursorIndex >= 0) {
            return true;
        }
        LogUtils.e(LOG_TAG, "cursor index < 0 --- delete invalid");
        return false;
    }

    private void exeScrollOverAnimation(int direction) {
        this.animation_temp_param = direction;
        Animation translateAnimation = new TranslateAnimation(0.0f, (float) (this.animation_temp_param * this.mScrollCompleteAnimHeight), 0.0f, 0.0f);
        translateAnimation.setInterpolator(this.mScrollCompleteAnimFirstInterpolator);
        translateAnimation.setDuration((long) (this.mScrollCompleteAnimDuration / 2));
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                Animation translateAnimation1 = new TranslateAnimation((float) (SearchCursorView.this.animation_temp_param * SearchCursorView.this.mScrollCompleteAnimHeight), 0.0f, 0.0f, 0.0f);
                translateAnimation1.setInterpolator(SearchCursorView.this.mScrollCompleteAnimSecondInterpolator);
                translateAnimation1.setDuration((long) (SearchCursorView.this.mScrollCompleteAnimDuration / 2));
                translateAnimation1.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                    }
                });
                SearchCursorView.this.startAnimation(translateAnimation1);
            }
        });
        startAnimation(translateAnimation);
    }
}
