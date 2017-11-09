package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.gala.cloudui.utils.CloudUtilsGala;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.font.FontManager;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.uikit.contract.HeaderContract.Presenter;
import com.gala.video.lib.share.uikit.contract.HeaderContract.View;
import com.gala.video.lib.share.uikit.utils.TypeUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import org.cybergarage.upnp.control.Control;

public class HeaderView extends LinearLayout implements IViewLifecycle<Presenter>, View {
    private static final int LINE_COLOR = Color.parseColor("#A0A0A0");
    private static final int PX2 = ResourceUtil.getPx(2);
    private static final int PXLINELABELSPACE = ResourceUtil.getPx(16);
    private static final int[] STATESET = new int[]{16842908};
    private String mLabel;
    private Rect mLabelBounds;
    private int mLabelColor;
    private Rect mLabelRect;
    private int mLabelSize;
    private Paint mPaint;
    private Presenter mPresenter;
    private ColorStateList mTimeTextColors;
    private int mTimeTextSize;
    private VipMarqueeTextView mTipTextView;
    private TextCanvas mTips;
    private Typeface mTypeface;

    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mLabelBounds = new Rect();
        this.mLabelRect = new Rect();
        initData();
        initView();
    }

    private void initData() {
        int px = ResourceUtil.getPx(20);
        setPadding(px, 0, px, 0);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
    }

    private void initView() {
        setLabelColor(Color.parseColor("#f8f8f8"));
        setLabelSize(ResourceUtil.getPx(40));
        setTypeface(FontManager.getInstance().getTypeface(getContext()));
        setTimeTextSize(ResourceUtil.getPx(30));
    }

    private void setLabelColor(int color) {
        this.mLabelColor = color;
    }

    private void setLabelSize(int size) {
        this.mLabelSize = size;
    }

    private void setTimeColor(ColorStateList colors) {
        this.mTimeTextColors = colors;
    }

    private void setTimeTextSize(int size) {
        this.mTimeTextSize = size;
    }

    private void setLabelPadding(int left, int top, int right, int bottom) {
        this.mLabelRect.set(left, top, right, bottom);
    }

    private void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
    }

    private void setLabel(String label) {
        this.mLabel = label;
        setWillNotDraw(false);
    }

    public void setTips(TextCanvas tips) {
        this.mTips = tips;
    }

    public void onBind(Presenter object) {
        this.mPresenter = object;
        this.mPresenter.setView(this);
        setLabel(object.getTitle());
        setTips(object.getTips());
        setTimeColor(CloudUtilsGala.getColorStateListFromResidStr(StringUtils.append("share_normal_item_text_color", object.getSkinEndsWith())));
        setLabelPadding(object.getCardInfoModel().getHeaderPaddingLeft(), 0, ResourceUtil.getPx(13), ResourceUtil.getPx(22));
        initTip(this.mTips);
        invalidate();
    }

    public void onUnbind(Presenter object) {
    }

    public void onShow(Presenter object) {
        if (this.mTipTextView != null) {
            this.mTipTextView.setSelected(true);
        }
    }

    public void onHide(Presenter object) {
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLabel(canvas);
        drawTimeLine(canvas);
    }

    private boolean isLabelShow() {
        return (this.mLabel == null || this.mLabel.isEmpty()) ? false : true;
    }

    private void drawLabel(Canvas canvas) {
        canvas.save();
        if (isLabelShow()) {
            this.mPaint.setTypeface(this.mTypeface);
            this.mPaint.setColor(this.mLabelColor);
            this.mPaint.setTextSize((float) this.mLabelSize);
            this.mPaint.getTextBounds(this.mLabel, 0, this.mLabel.length(), this.mLabelBounds);
            canvas.translate((float) getScrollX(), 0.0f);
            canvas.drawText(this.mLabel, (float) (this.mLabelRect.left - this.mLabelBounds.left), (float) (this.mLabelRect.top - this.mLabelBounds.top), this.mPaint);
        }
        canvas.restore();
    }

    private void drawTimeLine(Canvas canvas) {
        if (this.mPresenter.getTimeLine() != null) {
            drawTime(canvas);
        }
    }

    private int getLabelBottom() {
        if (isLabelShow()) {
            return (this.mLabelRect.top + this.mLabelRect.bottom) + this.mLabelBounds.height();
        }
        return 0;
    }

    private void drawTime(Canvas canvas) {
        int size = this.mPresenter.getTimeLine().size();
        for (int i = 0; i < size; i++) {
            String text = (String) this.mPresenter.getTimeLine().get(i);
            if (text == null) {
                text = "";
            }
            if (this.mTimeTextColors != null) {
                int color = this.mTimeTextColors.getDefaultColor();
                if (this.mPresenter.getFocusPosition() == i) {
                    color = this.mTimeTextColors.getColorForState(STATESET, 0);
                }
                this.mPaint.setColor(color);
            }
            this.mPaint.setTextSize((float) this.mTimeTextSize);
            int textWidth = (int) this.mPaint.measureText(text);
            int viewCenter = TypeUtils.castToInt(this.mPresenter.getViewCenterList().get(i));
            int textMinWidth = Math.min(textWidth, this.mPresenter.getTimeTextMaxWidth());
            int textLeft = viewCenter - (textMinWidth / 2);
            int textRight = textLeft + textMinWidth;
            int textBottom = (int) (((float) getLabelBottom()) - this.mPaint.ascent());
            int textTop = getLabelBottom();
            canvas.save();
            canvas.clipRect((float) textLeft, (float) textTop, (float) textRight, ((float) textBottom) + (this.mPaint.ascent() + ((float) this.mTimeTextSize)));
            canvas.drawText(text, (float) textLeft, (float) textBottom, this.mPaint);
            canvas.restore();
            drawLine(canvas, TypeUtils.castToInt(this.mPresenter.getViewLeftList().get(i)), textLeft - this.mPresenter.getBlankSpace());
            int lineRight = TypeUtils.castToInt(this.mPresenter.getViewRightList().get(i));
            drawLine(canvas, textRight + this.mPresenter.getBlankSpace(), lineRight);
            if (i != size - 1) {
                drawLine(canvas, lineRight, TypeUtils.castToInt(this.mPresenter.getViewLeftList().get(i + 1)));
            }
        }
    }

    private void drawLine(Canvas canvas, int left, int right) {
        int top = getLabelBottom() + PXLINELABELSPACE;
        int bottom = top + PX2;
        canvas.save();
        this.mPaint.setColor(LINE_COLOR);
        canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, this.mPaint);
        canvas.restore();
    }

    private void initTip(TextCanvas textCanvas) {
        if (textCanvas == null || TextUtils.isEmpty(textCanvas.getText())) {
            LogUtils.i("HeaderView", Control.RETURN);
            if (getChildCount() > 0) {
                removeAllViews();
            }
        } else if (isLabelShow()) {
            this.mPaint.setTypeface(this.mTypeface);
            this.mPaint.setColor(this.mLabelColor);
            this.mPaint.setTextSize((float) this.mLabelSize);
            this.mPaint.getTextBounds(this.mLabel, 0, this.mLabel.length(), this.mLabelBounds);
            float x = (float) (this.mLabelRect.left - this.mLabelBounds.left);
            if (getChildCount() <= 0) {
                this.mTipTextView = new VipMarqueeTextView(getContext());
                this.mTipTextView.setVisibility(0);
                this.mTipTextView.setSingleLine();
                this.mTipTextView.setTextSize(0, (float) getResources().getDimensionPixelSize(R.dimen.dimen_16dp));
                this.mTipTextView.setMaxWidth(ResourceUtil.getDimen(R.dimen.dimen_240dp));
                int paddingLeft = getResources().getDimensionPixelSize(R.dimen.dimen_14dp);
                int paddingRight = getResources().getDimensionPixelSize(R.dimen.dimen_9dp);
                int paddingTop = getResources().getDimensionPixelSize(R.dimen.dimen_2dp);
                int paddingBottom = getResources().getDimensionPixelSize(R.dimen.dimen_2dp);
                this.mTipTextView.setEllipsize(TruncateAt.MARQUEE);
                this.mTipTextView.setMarqueeRepeatLimit(2);
                this.mTipTextView.setGravity(16);
                this.mTipTextView.setText(textCanvas.getText());
                this.mTipTextView.setTextColor(textCanvas.getTextColor());
                this.mTipTextView.setBackgroundDrawable(textCanvas.getBackground());
                this.mTipTextView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                this.mTipTextView.canScrollHorizontally(-1);
                this.mTipTextView.setHorizontalFadingEdgeEnabled(true);
                LayoutParams layoutParams = new LayoutParams(-2, -2);
                layoutParams.leftMargin = (int) (((float) this.mLabelBounds.width()) + x);
                addView(this.mTipTextView, layoutParams);
            }
        } else if (getChildCount() > 0) {
            removeAllViews();
        }
    }
}
