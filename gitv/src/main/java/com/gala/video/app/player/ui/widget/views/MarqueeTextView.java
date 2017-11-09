package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;

public class MarqueeTextView extends TextView {
    private static final int FLASH = 88;
    private static final int ORIGINAL = 99;
    private static final String TAG_S = "Player/Ui/MarqueeTextView";
    private String TAG;
    private float mCoordinateX;
    private boolean mEnableMarquee;
    private Handler mHandler;
    private int mHeight;
    private String mOriginalText;
    private boolean mStopMarquee;
    private float mTextWidth;
    private String mTxt;
    private int mWidth;

    public MarqueeTextView(Context context) {
        super(context);
        this.TAG = null;
        this.mStopMarquee = true;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MarqueeTextView.FLASH /*88*/:
                        if (Math.abs(MarqueeTextView.this.mCoordinateX) > MarqueeTextView.this.mTextWidth) {
                            MarqueeTextView.this.mCoordinateX = (float) MarqueeTextView.this.mWidth;
                            MarqueeTextView.this.invalidate();
                            if (!MarqueeTextView.this.mStopMarquee) {
                                sendEmptyMessageDelayed(MarqueeTextView.FLASH, 30);
                                return;
                            }
                            return;
                        }
                        MarqueeTextView.this.mCoordinateX = MarqueeTextView.this.mCoordinateX - 1.0f;
                        MarqueeTextView.this.invalidate();
                        if (!MarqueeTextView.this.mStopMarquee) {
                            sendEmptyMessageDelayed(MarqueeTextView.FLASH, 30);
                            return;
                        }
                        return;
                    case 99:
                        MarqueeTextView.this.mCoordinateX = 0.0f;
                        MarqueeTextView.this.invalidate();
                        return;
                    default:
                        return;
                }
            }
        };
        this.TAG = TAG_S + hashCode();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TAG = null;
        this.mStopMarquee = true;
        this.mHandler = /* anonymous class already generated */;
        this.TAG = TAG_S + hashCode();
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.TAG = null;
        this.mStopMarquee = true;
        this.mHandler = /* anonymous class already generated */;
        this.TAG = TAG_S + hashCode();
    }

    public void setText(String text) {
        if (text != null) {
            this.mOriginalText = text;
            this.mTextWidth = getPaint().measureText(text);
            if (this.mTextWidth > ((float) this.mWidth)) {
                this.mEnableMarquee = true;
                if (this.mStopMarquee) {
                    this.mTxt = getTruncateEnd();
                } else {
                    this.mTxt = this.mOriginalText;
                }
            } else {
                this.mEnableMarquee = false;
                this.mTxt = this.mOriginalText;
            }
            invalidate();
        }
    }

    public void setViewBound(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public void start() {
        if (this.mEnableMarquee && !StringUtils.isEmpty(this.mOriginalText)) {
            this.mTxt = this.mOriginalText;
            this.mHandler.sendEmptyMessageDelayed(FLASH, 1000);
        }
        this.mStopMarquee = false;
    }

    public void stop() {
        if (this.mTextWidth > ((float) this.mWidth)) {
            this.mTxt = getTruncateEnd();
        } else {
            this.mTxt = this.mOriginalText;
        }
        this.mHandler.sendEmptyMessage(99);
        this.mHandler.removeMessages(FLASH);
        this.mStopMarquee = true;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mTxt != null) {
            canvas.drawText(this.mTxt, this.mCoordinateX, (float) this.mHeight, getPaint());
        }
    }

    private String getTruncateEnd() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "getTruncateEnd()");
        }
        CharSequence ellipsized = null;
        if (!StringUtils.isEmpty(this.mOriginalText)) {
            ellipsized = TextUtils.ellipsize(this.mOriginalText, getPaint(), (float) this.mWidth, TruncateAt.END);
        }
        if (ellipsized != null) {
            return ellipsized.toString();
        }
        return "";
    }
}
