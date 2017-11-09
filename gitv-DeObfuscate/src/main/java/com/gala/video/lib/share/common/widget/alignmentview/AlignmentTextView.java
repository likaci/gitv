package com.gala.video.lib.share.common.widget.alignmentview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants;
import java.util.ArrayList;

public class AlignmentTextView extends TextView {
    private final String TAG;
    private Rect mBackGroundRect;
    private Context mContext;
    private int mFontHeight;
    private boolean mIsCustomSize;
    private boolean mIsFirstSet;
    private float mLineSpace;
    private OnLineCountListener mListener;
    private float mOffsetX;
    private float mOffsetY;
    private String mOldText;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private Paint mPaint;
    private String mText;
    private float mTextSize;
    private int mTextWidth;
    private int maxLine;
    private ArrayList<LinePar> tempLineArray;

    public interface OnLineCountListener {
        void getRealCount(int i);
    }

    private class LinePar {
        private int mEnd;
        private int mLineCount;
        private int mStart;
        private float mWordSpaceOffset;

        private LinePar() {
        }

        public void setStart(int mStart) {
            this.mStart = mStart;
        }

        public void setEnd(int mEnd) {
            this.mEnd = mEnd;
        }

        public void setLineCount(int count) {
            this.mLineCount = count;
        }

        public void setWordSpaceOffset(float mWordSpaceOffset) {
            this.mWordSpaceOffset = mWordSpaceOffset;
        }

        public int getStart() {
            return this.mStart;
        }

        public int getEnd() {
            return this.mEnd;
        }

        public int getLineCount() {
            return this.mLineCount;
        }

        public float getWordSpaceOffset() {
            return this.mWordSpaceOffset;
        }
    }

    public AlignmentTextView(Context context) {
        this(context, null);
    }

    public AlignmentTextView(Context context, AttributeSet set) {
        this(context, set, 0);
    }

    public AlignmentTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = null;
        this.mPaint = null;
        this.mTextWidth = AdsConstants.IMAGE_MAX_WIGTH;
        this.mText = "";
        this.mLineSpace = 2.0f;
        this.mTextSize = 0.0f;
        this.mFontHeight = 0;
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        this.tempLineArray = null;
        this.mIsFirstSet = false;
        this.maxLine = 300;
        this.mBackGroundRect = null;
        this.mOffsetX = 0.0f;
        this.mOffsetY = 0.0f;
        this.mIsCustomSize = false;
        this.TAG = "AlignmentTextView/DetailActivity@" + Integer.toHexString(hashCode());
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, C1632R.styleable.alignmenttextview);
        if (mTypedArray != null) {
            this.mContext = context;
            this.mIsFirstSet = true;
            this.mPaint = new Paint();
            this.mPaint.setTypeface(Typeface.MONOSPACE);
            this.mPaint.setAntiAlias(true);
            this.maxLine = mTypedArray.getInteger(C1632R.styleable.alignmenttextview_max_line_is, 300);
            mTypedArray.recycle();
        }
    }

    public void setOnLineCountListener(OnLineCountListener listener) {
        this.mListener = listener;
    }

    public void setLineSpace(float a) {
        this.mLineSpace = a;
    }

    public int getMaxLine() {
        return this.maxLine;
    }

    public void setMaxLine(int num) {
        this.maxLine = num;
    }

    public void setText(CharSequence text, BufferType type) {
        if (!this.mIsFirstSet) {
            this.mIsFirstSet = !StringUtils.equals(this.mOldText, text);
            if (!(this.mListener == null || this.tempLineArray == null)) {
                this.mListener.getRealCount(this.tempLineArray.size());
            }
        }
        this.mOldText = text.toString();
        super.setText(text, type);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtils.m1568d(this.TAG, ">> onMeasure tempLineArray1 " + widthMeasureSpec + ", height " + heightMeasureSpec + ", heightMode:" + heightMode);
    }

    protected void onDraw(Canvas canvas) {
        Log.d(this.TAG, "onDraw is caculate? " + this.mIsFirstSet);
        if (this.mIsFirstSet) {
            this.mIsFirstSet = false;
            this.mText = getText().toString().trim();
            this.mBackGroundRect = getBgDrawablePaddings(getBackground());
            if (!StringUtils.isEmpty(this.mText)) {
                this.mTextSize = getTextSize();
                this.mFontHeight = (int) this.mTextSize;
                this.mPaddingRight = getPaddingRight() + this.mBackGroundRect.right;
                this.mPaddingBottom = getPaddingBottom() + this.mBackGroundRect.bottom;
                if (!this.mIsCustomSize) {
                    this.mTextWidth = getWidth();
                    this.mPaddingLeft = getPaddingLeft() + this.mBackGroundRect.left;
                    this.mPaddingTop = getPaddingTop() + this.mBackGroundRect.top;
                    this.mTextWidth = (this.mTextWidth - this.mPaddingLeft) - this.mPaddingRight;
                }
                LogUtils.m1568d(this.TAG, ">> onDraw mPaddingLeft, " + this.mPaddingLeft + ", mPaddingTop " + this.mPaddingTop + ", mTextWidth " + this.mTextWidth);
                this.mPaint.setTextSize(this.mTextSize);
                this.tempLineArray = getLineParList(this.mText);
            } else {
                return;
            }
        }
        this.mPaint.setColor(getCurrentTextColor());
        drawText(this.tempLineArray, this.mText, canvas);
    }

    private Rect getBgDrawablePaddings(Drawable d) {
        Rect bgDrawablePaddings = new Rect();
        if (d != null) {
            d.getPadding(bgDrawablePaddings);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "getBgDrawablePaddings: " + bgDrawablePaddings);
        }
        return bgDrawablePaddings;
    }

    public boolean setCustomSizeEnabled(boolean isEnable) {
        this.mIsCustomSize = isEnable;
        return this.mIsCustomSize;
    }

    public void setParameters(int width, int paddingTop, int paddingLeft) {
        if (this.mIsCustomSize) {
            this.mPaddingTop = paddingTop;
            this.mPaddingLeft = paddingLeft;
            this.mTextWidth = width;
        }
    }

    private ArrayList<LinePar> getLineParList(String mTextStr) {
        if (mTextStr == null || mTextStr.isEmpty()) {
            return null;
        }
        int tempStart = 0;
        int tempLineWidth = 0;
        int tempLineCount = 0;
        ArrayList<LinePar> tempLineArray = new ArrayList();
        int i = 0;
        while (i < mTextStr.length()) {
            char ch = mTextStr.charAt(i);
            String str = String.valueOf(ch);
            float strWidth = 0.0f;
            if (!(str == null || str.isEmpty())) {
                strWidth = (float) AlignmentConstant.getWidthofString(str, this.mPaint);
            }
            if (ch != '\n' || tempStart == i) {
                tempLineWidth = (int) (((double) tempLineWidth) + Math.ceil((double) strWidth));
                if (tempLineWidth >= this.mTextWidth - this.mPaddingRight) {
                    tempLineCount++;
                    if (AlignmentConstant.isLeftPunctuation(ch)) {
                        i--;
                        addLinePar(tempStart, i, tempLineCount, ((float) ((((double) tempLineWidth) - Math.ceil((double) strWidth)) - ((double) this.mTextWidth))) / ((float) (i - tempStart)), tempLineArray);
                    } else if (AlignmentConstant.isRightPunctuation(ch)) {
                        if (i == mTextStr.length() - 1) {
                            addLinePar(tempStart, i, tempLineCount, 0.0f, tempLineArray);
                            break;
                        }
                        char nextChar = mTextStr.charAt(i + 1);
                        if ((AlignmentConstant.isHalfPunctuation(nextChar) || AlignmentConstant.isPunctuation(nextChar)) && !AlignmentConstant.isLeftPunctuation(nextChar)) {
                            String nextStr = String.valueOf(nextChar);
                            float nextStrWidth = 0.0f;
                            if (!(nextStr == null || nextStr.isEmpty())) {
                                nextStrWidth = (float) AlignmentConstant.getWidthofString(nextStr, this.mPaint);
                            }
                            i++;
                            addLinePar(tempStart, i, tempLineCount, ((float) ((((double) tempLineWidth) + Math.ceil((double) nextStrWidth)) - ((double) this.mTextWidth))) / ((float) (i - tempStart)), tempLineArray);
                        } else {
                            addLinePar(tempStart, i, tempLineCount, ((float) (tempLineWidth - this.mTextWidth)) / ((float) (i - tempStart)), tempLineArray);
                        }
                    } else if (AlignmentConstant.isHalfPunctuation(ch) || AlignmentConstant.isPunctuation(ch)) {
                        addLinePar(tempStart, i, tempLineCount, ((float) (tempLineWidth - this.mTextWidth)) / ((float) (i - tempStart)), tempLineArray);
                    } else if (i >= 1) {
                        char preChar = mTextStr.charAt(i - 1);
                        if (AlignmentConstant.isLeftPunctuation(preChar)) {
                            String preStr = String.valueOf(preChar);
                            float preStrWidth = 0.0f;
                            if (!(preStr == null || preStr.isEmpty())) {
                                preStrWidth = (float) AlignmentConstant.getWidthofString(preStr, this.mPaint);
                            }
                            i -= 2;
                            addLinePar(tempStart, i, tempLineCount, ((float) (((((double) tempLineWidth) - Math.ceil((double) strWidth)) - Math.ceil((double) preStrWidth)) - ((double) this.mTextWidth))) / ((float) (i - tempStart)), tempLineArray);
                        } else {
                            i--;
                            addLinePar(tempStart, i, tempLineCount, ((float) ((((double) tempLineWidth) - Math.ceil((double) strWidth)) - ((double) this.mTextWidth))) / ((float) (i - tempStart)), tempLineArray);
                        }
                    }
                    if (i == mTextStr.length() - 1) {
                        break;
                    }
                    tempStart = i + 1;
                    tempLineWidth = 0;
                } else if (i == mTextStr.length() - 1) {
                    tempLineCount++;
                    addLinePar(tempStart, i, tempLineCount, 0.0f, tempLineArray);
                    break;
                }
            } else {
                tempLineCount++;
                addLinePar(tempStart, i, tempLineCount, 0.0f, tempLineArray);
                if (i == mTextStr.length() - 1) {
                    break;
                }
                tempStart = i + 1;
                tempLineWidth = 0;
            }
            i++;
        }
        if (this.mListener == null) {
            return tempLineArray;
        }
        LogUtils.m1568d(this.TAG, ">>  mListener notify count = " + tempLineCount);
        this.mListener.getRealCount(tempLineCount);
        return tempLineArray;
    }

    private void addLinePar(int start, int end, int lineCount, float wordSpaceOffset, ArrayList<LinePar> lineList) {
        if (lineList != null) {
            LinePar linePar = new LinePar();
            linePar.setLineCount(lineCount);
            linePar.setStart(start);
            linePar.setEnd(end);
            linePar.setWordSpaceOffset(wordSpaceOffset);
            lineList.add(linePar);
        }
    }

    private void drawText(ArrayList<LinePar> tempLineArray, String mTextStr, Canvas canvas) {
        if (tempLineArray != null && canvas != null && mTextStr != null) {
            if (!mTextStr.equals("")) {
                for (int lineNum = 0; lineNum < tempLineArray.size(); lineNum++) {
                    LinePar linePar = (LinePar) tempLineArray.get(lineNum);
                    int start = linePar.getStart();
                    int end = linePar.getEnd();
                    float width = linePar.getWordSpaceOffset();
                    int lineCount = linePar.getLineCount();
                    if (start <= end && end <= mTextStr.length() - 1) {
                        float lineWidth = 0.0f;
                        int strNum = start;
                        while (strNum <= end) {
                            char ch = mTextStr.charAt(strNum);
                            String str = String.valueOf(ch);
                            if (!(str == null || str.equals(""))) {
                                if (ch == '\n') {
                                    str = "";
                                }
                                if (strNum > end) {
                                    continue;
                                    break;
                                } else if (strNum >= start && strNum <= end && lineCount >= 1) {
                                    if (lineCount <= this.maxLine) {
                                        if (lineCount == this.maxLine && strNum == end && ((float) (AlignmentConstant.getWidthofString("...", this.mPaint) * 2)) + lineWidth > ((float) this.mTextWidth)) {
                                            str = "...";
                                        }
                                        this.mOffsetY = (((float) (this.mFontHeight * lineCount)) + (((float) (lineCount - 1)) * this.mLineSpace)) + ((float) this.mPaddingTop);
                                        this.mOffsetX = ((float) this.mPaddingLeft) + lineWidth;
                                        canvas.drawText(str, this.mOffsetX, this.mOffsetY, this.mPaint);
                                        lineWidth = (lineWidth + ((float) AlignmentConstant.getWidthofString(str, this.mPaint))) - width;
                                    } else {
                                        return;
                                    }
                                }
                            }
                            strNum++;
                        }
                        continue;
                    }
                }
            }
        }
    }
}
