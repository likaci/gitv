package com.gala.video.app.epg.ui.netspeed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.netspeed.model.NetSpeedSeriesDataSet;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.cybergarage.upnp.UPnPStatus;

public class NetSpeedChatView extends View {
    private static final int ANNOTATION_FRAME_DURATION = 40;
    private static final int ANNOTATION_STEP = 10;
    private static final int MAX_ANNOTATION_FRAMES = 25;
    private static final int MSG_FADE_IN = 0;
    private int mAlpha;
    private final Paint mBgPaint;
    private int mCoverColor;
    private Drawable mCoverDrawable;
    private Paint mCoverPaint;
    private Paint mDividerLinePaint;
    private Handler mHandler;
    private boolean mIs4KSupported;
    private boolean mIsFinished;
    private Drawable mLabelCoverDrawable;
    private Drawable mLabelSelectedDrawable;
    private final Paint mLabelTextPaint;
    private List<String> mLabelTxtArray;
    private int mLabelTxtColor;
    private int mLabelTxtMarginLeft;
    private int mLabelWidth;
    private int mLineColor;
    private int mLineHeight;
    private int mMainPanelHeight;
    private int mMainPanelWidth;
    private Drawable mNinePatchTextBgDrawable;
    private NetSpeedSeriesDataSet mPixDataSet;
    private Rect mRect;
    private String mSelectedLabel;
    private Bitmap mSpeedIndicator;
    private int mSpeedLineEndColor;
    private Paint mSpeedLinePaint;
    private int mSpeedLineStartColor;
    private int mSpeedViewMarginLeft;
    private int mSpeedViewMarginRight;
    private Rect mSpeedViewRect;
    private int mTextColor;
    private final TextPaint mTextPaint;
    private Rect mTextRect;
    private float mTextSize;
    private NetSpeedSeriesDataSet mValueDataSet;

    private static class AnnotationHandler extends Handler {
        WeakReference<NetSpeedChatView> mRef;

        public AnnotationHandler(NetSpeedChatView view) {
            this.mRef = new WeakReference(view);
        }

        public void handleMessage(Message msg) {
            if (this.mRef.get() != null) {
                NetSpeedChatView netSpeedChatView = (NetSpeedChatView) this.mRef.get();
                netSpeedChatView.mAlpha = netSpeedChatView.mAlpha + 10;
                if (((NetSpeedChatView) this.mRef.get()).mAlpha < 255) {
                    ((NetSpeedChatView) this.mRef.get()).invalidate();
                    sendEmptyMessageDelayed(0, 40);
                }
            }
        }
    }

    public NetSpeedChatView(Context context) {
        this(context, null);
    }

    public NetSpeedChatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetSpeedChatView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mSpeedLinePaint = new Paint();
        this.mDividerLinePaint = new Paint();
        this.mCoverPaint = new Paint();
        this.mRect = new Rect();
        this.mSpeedViewRect = new Rect();
        this.mTextRect = new Rect();
        this.mLabelTxtArray = new ArrayList();
        this.mLabelWidth = 0;
        this.mLabelTxtMarginLeft = 0;
        this.mSpeedViewMarginLeft = 0;
        this.mSpeedViewMarginRight = 0;
        this.mIs4KSupported = false;
        this.mSelectedLabel = "";
        this.mIsFinished = false;
        this.mAlpha = 0;
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.chatview);
        this.mCoverDrawable = mTypedArray.getDrawable(R.styleable.chatview_cover_panel);
        this.mLabelCoverDrawable = mTypedArray.getDrawable(R.styleable.chatview_label_cover_panel);
        this.mLabelSelectedDrawable = mTypedArray.getDrawable(R.styleable.chatview_label_selected_image);
        this.mNinePatchTextBgDrawable = mTypedArray.getDrawable(R.styleable.chatview_textBackground);
        int labelTxtSize = mTypedArray.getDimensionPixelSize(R.styleable.chatview_labelTextSize, 24);
        this.mLabelTxtColor = getResources().getColor(R.color.net_chat_Label_text);
        this.mLabelWidth = mTypedArray.getDimensionPixelSize(R.styleable.chatview_labelWidth, 60);
        this.mMainPanelWidth = mTypedArray.getDimensionPixelSize(R.styleable.chatview_main_panel_width, 945);
        this.mMainPanelHeight = mTypedArray.getDimensionPixelSize(R.styleable.chatview_main_panel_height, UPnPStatus.OUT_OF_SYNC);
        this.mLabelTxtMarginLeft = mTypedArray.getDimensionPixelSize(R.styleable.chatview_labelTxtMaginLeft, 17);
        this.mTextColor = mTypedArray.getColor(R.styleable.chatview_netSpeedtextColor, -1);
        this.mTextSize = (float) mTypedArray.getDimensionPixelSize(R.styleable.chatview_netSpeedtextSize, 24);
        this.mLineHeight = mTypedArray.getDimensionPixelSize(R.styleable.chatview_lineHeight, 2);
        this.mSpeedViewMarginLeft = mTypedArray.getDimensionPixelSize(R.styleable.chatview_divider_line_margin_left, 20);
        this.mSpeedViewMarginRight = mTypedArray.getDimensionPixelSize(R.styleable.chatview_divider_line_margin_right, 20);
        this.mLineColor = getResources().getColor(R.color.net_chat_divider_line);
        this.mSpeedLineStartColor = mTypedArray.getColor(R.styleable.chatview_speedlineStartColor, -1);
        this.mSpeedLineEndColor = mTypedArray.getColor(R.styleable.chatview_speedlineEndColor, -1);
        this.mCoverColor = mTypedArray.getColor(R.styleable.chatview_coverColor, 1619560584);
        int speedIndicator = mTypedArray.getResourceId(R.styleable.chatview_net_speed_recorded, R.drawable.epg_net_chat_current_speed);
        mTypedArray.recycle();
        this.mDividerLinePaint.setColor(this.mLineColor);
        this.mDividerLinePaint.setAntiAlias(true);
        this.mDividerLinePaint.setStrokeWidth((float) this.mLineHeight);
        this.mDividerLinePaint.setDither(true);
        this.mSpeedLinePaint.setAntiAlias(true);
        this.mSpeedLinePaint.setStrokeWidth(2.0f);
        this.mSpeedLinePaint.setDither(true);
        this.mSpeedLinePaint.setColor(this.mSpeedLineStartColor);
        this.mTextPaint = new TextPaint(1);
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setTextSize(this.mTextSize);
        this.mLabelTextPaint = new TextPaint(1);
        this.mLabelTextPaint.setColor(this.mLabelTxtColor);
        this.mLabelTextPaint.setTextSize((float) labelTxtSize);
        this.mLabelTextPaint.setStyle(Style.FILL);
        this.mBgPaint = new Paint();
        this.mBgPaint.setColor(234881023);
        this.mCoverPaint.setColor(this.mCoverColor);
        this.mSpeedIndicator = BitmapFactory.decodeResource(getResources(), speedIndicator);
        this.mHandler = new AnnotationHandler(this);
    }

    public void setDataSet(NetSpeedSeriesDataSet dataSet) {
        this.mAlpha = 255;
        this.mValueDataSet = dataSet;
        this.mIsFinished = false;
        invalidate();
    }

    public void setAllDataSet(NetSpeedSeriesDataSet dataSet, String seletedLabel) {
        this.mValueDataSet = dataSet;
        this.mSelectedLabel = seletedLabel;
        this.mIsFinished = true;
        this.mAlpha = 0;
        this.mHandler.sendEmptyMessage(0);
    }

    public void clear() {
        if (this.mValueDataSet != null && this.mPixDataSet != null) {
            this.mValueDataSet.clear();
            this.mPixDataSet.clear();
            this.mIsFinished = false;
            this.mSelectedLabel = "";
            invalidate();
        }
    }

    public void setLabels(List<String> labelsTexts) {
        this.mLabelTxtArray = labelsTexts;
        if (this.mLabelTxtArray != null && this.mLabelTxtArray.size() < 4) {
            this.mIs4KSupported = false;
        }
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mLabelTxtArray != null) {
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            canvas.getClipBounds(this.mRect);
            int paddingLeft = (getWidth() - this.mMainPanelWidth) / 2;
            int paddingTop = (getHeight() - this.mMainPanelHeight) / 2;
            int paddingBottom = paddingTop;
            int top = this.mRect.top + paddingTop;
            int left = this.mRect.left + paddingLeft;
            int bottom = this.mRect.bottom - paddingBottom;
            int right = this.mRect.right - paddingLeft;
            int height = (this.mRect.height() - paddingBottom) - paddingTop;
            canvas.drawRect(new RectF(new RectF((float) left, (float) top, (float) right, (float) bottom)), this.mBgPaint);
            this.mSpeedViewRect.set(this.mSpeedViewMarginLeft + left, top, (right - this.mSpeedViewMarginRight) - this.mLabelWidth, bottom);
            canvas.save();
            if (this.mValueDataSet != null) {
                this.mPixDataSet = NetSpeedSeriesDataSet.normalizeSeries(this.mSpeedViewRect.width(), height, this.mValueDataSet, this.mIs4KSupported);
            }
            Canvas canvas2 = canvas;
            canvas2.translate((float) this.mSpeedViewRect.left, (float) paddingTop);
            drawDividerLine(canvas, 0, ((this.mMainPanelWidth - this.mSpeedViewMarginRight) - this.mSpeedViewMarginLeft) - this.mLabelWidth, height);
            if (this.mPixDataSet != null && this.mPixDataSet.getSize() > 0) {
                for (int index = 1; index < this.mPixDataSet.getSize(); index++) {
                    Canvas canvas3 = canvas;
                    canvas3.drawLine((float) this.mPixDataSet.getXByIndex(index - 1), (float) (height - this.mPixDataSet.getYByIndex(index - 1)), (float) this.mPixDataSet.getXByIndex(index), (float) (height - this.mPixDataSet.getYByIndex(index)), this.mSpeedLinePaint);
                }
                drawCircle(canvas, this.mSpeedViewRect.width(), height);
                if (!this.mIsFinished) {
                    drawText(canvas, height);
                }
            }
            canvas.restore();
            if (this.mPixDataSet != null && this.mPixDataSet.getSize() > 0) {
                if (this.mIsFinished) {
                    drawCover(canvas, left, top, (right - this.mLabelWidth) - 2, height);
                } else {
                    drawCover(canvas, left, top, height);
                }
            }
            drawLabel(canvas, new Rect(left, top, right, bottom));
        }
    }

    private void drawLabel(Canvas canvas, Rect rect) {
        int labelStartX = rect.right - this.mLabelWidth;
        this.mLabelCoverDrawable.setBounds(labelStartX, rect.top, rect.right, rect.bottom);
        this.mLabelCoverDrawable.draw(canvas);
        if (this.mLabelTxtArray != null && this.mLabelTxtArray.size() > 0) {
            int dividerHeight = rect.height() / (this.mLabelTxtArray.size() + 1);
            int index = 0;
            while (index < this.mLabelTxtArray.size()) {
                Rect txtRect = new Rect();
                this.mLabelTextPaint.getTextBounds((String) this.mLabelTxtArray.get(index), 0, ((String) this.mLabelTxtArray.get(index)).length(), txtRect);
                if (TextUtils.isEmpty(this.mSelectedLabel) || !this.mSelectedLabel.equals(this.mLabelTxtArray.get(index))) {
                    canvas.drawText((String) this.mLabelTxtArray.get(index), (float) (this.mLabelTxtMarginLeft + labelStartX), (float) ((rect.bottom - ((index + 1) * dividerHeight)) + (txtRect.height() / 2)), this.mLabelTextPaint);
                } else {
                    this.mLabelTextPaint.setColor(getResources().getColor(R.color.skin_net_yellow_tip_txt));
                    this.mLabelTextPaint.setAlpha(this.mAlpha);
                    canvas.drawText((String) this.mLabelTxtArray.get(index), (float) (this.mLabelTxtMarginLeft + labelStartX), (float) ((rect.bottom - ((index + 1) * dividerHeight)) + (txtRect.height() / 2)), this.mLabelTextPaint);
                    this.mLabelTextPaint.setColor(this.mLabelTxtColor);
                    int width = this.mLabelSelectedDrawable.getIntrinsicWidth();
                    int height = this.mLabelSelectedDrawable.getIntrinsicHeight();
                    int startX = ((this.mLabelTxtMarginLeft + labelStartX) + txtRect.width()) + 10;
                    int startY = (rect.bottom - ((index + 1) * dividerHeight)) - (height / 2);
                    this.mLabelSelectedDrawable.setBounds(startX, startY, startX + width, startY + height);
                    this.mLabelSelectedDrawable.setAlpha(this.mAlpha);
                    this.mLabelSelectedDrawable.draw(canvas);
                }
                index++;
            }
        }
    }

    private void drawTextBg(Canvas canvas, Rect rect) {
        if (this.mNinePatchTextBgDrawable != null) {
            this.mNinePatchTextBgDrawable.setBounds(rect);
            this.mNinePatchTextBgDrawable.draw(canvas);
        }
    }

    private void drawText(Canvas canvas, int height) {
        int stopX = this.mPixDataSet.getXByIndex(this.mPixDataSet.getSize() - 1);
        int stopY = this.mPixDataSet.getYByIndex(this.mPixDataSet.getSize() - 1);
        String text = getSpeedDisplayStrKb(this.mValueDataSet.getYByIndex(this.mValueDataSet.getSize() - 1));
        this.mTextPaint.getTextBounds(text, 0, text.length(), this.mTextRect);
        Rect rect = new Rect(((stopX - this.mTextRect.width()) - 20) - 5, ((height - stopY) - (this.mTextRect.height() / 2)) - 6, stopX - 5, ((height - stopY) + 6) + (this.mTextRect.height() / 2));
        drawTextBg(canvas, rect);
        canvas.drawText(text, (float) (rect.left + 10), (float) (rect.bottom - 6), this.mTextPaint);
    }

    private void drawCover(Canvas canvas, int left, int top, int height) {
        this.mCoverDrawable.setBounds(left, top, this.mSpeedViewRect.left + this.mPixDataSet.getXByIndex(this.mPixDataSet.getSize() - 1), top + height);
        this.mCoverDrawable.draw(canvas);
    }

    private void drawCover(Canvas canvas, int left, int top, int right, int height) {
        this.mCoverDrawable.setBounds(left, top, right, top + height);
        this.mCoverDrawable.draw(canvas);
    }

    private void drawCircle(Canvas canvas, int width, int height) {
        int bitmapWidth = this.mSpeedIndicator.getWidth();
        int bitmapHeight = this.mSpeedIndicator.getHeight();
        for (int index = 0; index < this.mPixDataSet.getSize(); index++) {
            canvas.drawBitmap(this.mSpeedIndicator, (float) (this.mPixDataSet.getXByIndex(index) - (bitmapWidth / 2)), (float) ((height - this.mPixDataSet.getYByIndex(index)) - (bitmapHeight / 2)), null);
        }
    }

    private void drawDividerLine(Canvas canvas, int left, int right, int height) {
        int itemHeight = height / (this.mLabelTxtArray.size() + 1);
        int selectedLine = -1;
        if (this.mIsFinished) {
            for (int index = 0; index < this.mLabelTxtArray.size(); index++) {
                if (this.mSelectedLabel.equals(this.mLabelTxtArray.get(index))) {
                    selectedLine = index;
                }
            }
        }
        for (int i = 1; i < this.mLabelTxtArray.size() + 1; i++) {
            if (selectedLine + 1 == i) {
                this.mDividerLinePaint.setColor(getResources().getColor(R.color.skin_net_yellow_tip_txt));
                this.mDividerLinePaint.setAlpha(this.mAlpha / 2);
                canvas.drawLine((float) left, (float) (height - (itemHeight * i)), (float) right, (float) (height - (itemHeight * i)), this.mDividerLinePaint);
                this.mDividerLinePaint.setColor(this.mLineColor);
            } else {
                canvas.drawLine((float) left, (float) (height - (itemHeight * i)), (float) right, (float) (height - (itemHeight * i)), this.mDividerLinePaint);
            }
        }
    }

    private String getSpeedDisplayStrKb(int kb) {
        if (kb < 1024) {
            return kb + "Kb/s";
        }
        return new DecimalFormat("0.0").format((double) (((float) kb) / 1024.0f)) + "Mb/s";
    }
}
