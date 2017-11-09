package com.gala.video.app.epg.ui.albumlist.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class CursorTextView extends TextView {
    private static char DOT = '•';
    private String mCurrentText = null;
    private int mCursorColor;
    private CursorHandler mCursorHandler = null;
    private HandlerThread mCursorThread = null;
    private int mCursorWidth = 3;
    private String mHint = null;
    private boolean mIsPostShow = false;
    private boolean mIsPreShow = false;
    private boolean mIsRunning = false;
    private Drawable mPostCursorDrawable = null;
    private Drawable mPreCursorDrawable = null;
    private int mSpaceWidth = -1;
    private float mTextWidth = -1.0f;

    private static class CursorHandler extends Handler {
        private long interval;
        private CursorTextView view;

        public CursorHandler(Looper looper, CursorTextView view, long interval) {
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

    public CursorTextView(Context context) {
        super(context);
        init();
    }

    public CursorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CursorTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.mCursorColor = Color.parseColor("#F1F1F1");
        Rect bounds = new Rect();
        getPaint().getTextBounds("a a", 0, "a a".length(), bounds);
        this.mSpaceWidth = bounds.right;
        getPaint().getTextBounds("aa", 0, "aa".length(), bounds);
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
        if (text != null) {
            this.mCurrentText = text.toString();
            if (this.mCurrentText == null || this.mCurrentText.equals(getHintString())) {
                super.setText(text, type);
                return;
            }
            if (this.mTextWidth <= 0.0f) {
                this.mTextWidth = (float) ((getWidth() - getPaddingLeft()) - getPaddingRight());
            }
            super.setText(TextUtils.ellipsize(text, getPaint(), this.mTextWidth, TruncateAt.START), type);
        }
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCursor(canvas);
    }

    private void drawCursor(Canvas canvas) {
        String currentText = getText().toString();
        if (currentText != null) {
            if (currentText.equals(getHintString())) {
                drawCursorForHint(canvas);
            } else {
                drawCursorForText(canvas);
            }
        }
    }

    private void drawCursorForHint(Canvas canvas) {
        if (this.mPreCursorDrawable == null) {
            this.mPreCursorDrawable = new ColorDrawable(this.mCursorColor);
            this.mPreCursorDrawable.setBounds(0, getHeight() / 6, this.mCursorWidth, getHeight() - (getHeight() / 6));
        }
        if (!this.mIsRunning || this.mIsPreShow) {
            this.mPreCursorDrawable.setAlpha(0);
            this.mIsPreShow = false;
        } else {
            this.mPreCursorDrawable.setAlpha(255);
            this.mIsPreShow = true;
        }
        int offsetX = getPaddingLeft() - this.mCursorWidth;
        canvas.save();
        canvas.translate((float) offsetX, 0.0f);
        this.mPreCursorDrawable.draw(canvas);
        canvas.restore();
    }

    private void drawCursorForText(Canvas canvas) {
        if (this.mPostCursorDrawable == null) {
            this.mPostCursorDrawable = new ColorDrawable(this.mCursorColor);
            this.mPostCursorDrawable.setBounds(0, getHeight() / 6, this.mCursorWidth, getHeight() - (getHeight() / 6));
        }
        if (!this.mIsRunning || this.mIsPostShow) {
            this.mPostCursorDrawable.setAlpha(0);
            this.mIsPostShow = false;
        } else {
            this.mPostCursorDrawable.setAlpha(255);
            this.mIsPostShow = true;
        }
        Rect bounds = new Rect();
        String currentString = getText().toString();
        if (getTransformationMethod() instanceof PasswordTransformationMethod) {
            int num = currentString.length();
            StringBuilder builder = new StringBuilder("");
            for (int i = 0; i < num; i++) {
                builder.append(DOT);
            }
            currentString = builder.toString();
        }
        String stringWithSpace = currentString.replace(" ", "");
        getPaint().getTextBounds(stringWithSpace, 0, stringWithSpace.length(), bounds);
        int offsetX = (bounds.right + getPaddingLeft()) + this.mCursorWidth;
        if (this.mSpaceWidth > 0) {
            offsetX += getSpaceNum(currentString) * this.mSpaceWidth;
        }
        canvas.save();
        canvas.translate((float) offsetX, 0.0f);
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

    private int getSpaceNum(String str) {
        if (!str.contains(" ")) {
            return 0;
        }
        byte[] temp = str.getBytes();
        int count = 0;
        for (byte b : temp) {
            if (b == (byte) 32) {
                count++;
            }
        }
        return count;
    }

    public int getCursorColor() {
        return this.mCursorColor;
    }

    public void setCursorColor(int cursorColor) {
        this.mCursorColor = cursorColor;
    }
}
