package com.gala.video.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.uiutils.albumcorner.AlbumItemCornerImage;
import com.gala.video.widget.IGridItemManager;
import com.gala.video.widget.util.ImageUtils;
import com.gala.video.widget.util.LogUtils;
import com.gala.video.widget.util.PageViewUtils;

public abstract class GridItemLayout extends LinearLayout implements OnFocusChangeListener, IGridItemManager {
    private static SparseIntArray sparseIntArray = new SparseIntArray();
    private int animationType = 0;
    private RelativeLayout downloadView;
    private boolean imageBright = isSetImageBright();
    private int imageHeight;
    private int imageWidth;
    private AlbumItemCornerImage mAlbumItemCornerImage;
    private float mBright = getBrightLevel();
    protected Context mContext = null;
    private TextView mDescText;
    private Drawable mFocusDrawable;
    private FrameLayout mFrameLayout;
    private TextView mNameText;
    private float mNormal = getDimLevel();
    private Drawable mNormalDrawable;
    private boolean mScaleWhenFocus = true;
    private int textHeight;
    private int textOffset;
    private int textWidth;

    public abstract float getBrightLevel();

    public abstract float getDimLevel();

    public abstract Drawable getFocusBg();

    public abstract int getImageHeight();

    public abstract int getImageWidth();

    public abstract Drawable getNormalBg();

    public abstract int getTextHeight();

    public abstract int getTextOffset();

    public abstract int getTextWidth();

    public abstract boolean isSetImageBright();

    public FrameLayout getFrameLayout() {
        return this.mFrameLayout;
    }

    public void setFrameLayout(FrameLayout mFrameLayout) {
        this.mFrameLayout = mFrameLayout;
    }

    public TextView getNameText() {
        return this.mNameText;
    }

    public void setNameText(TextView mNameText) {
        this.mNameText = mNameText;
    }

    public GridItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public GridItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GridItemLayout(Context context) {
        super(context);
        init(context);
    }

    protected void init(Context context) {
        this.mContext = context;
        setOrientation(1);
        initData(context);
        initImageLayer(context);
        initText(context);
    }

    private void initData(Context context) {
        this.imageWidth = getImageWidth();
        this.imageHeight = getImageHeight();
        this.textWidth = getTextWidth();
        this.textHeight = getTextHeight();
        this.textOffset = getTextOffset();
        this.mFocusDrawable = getFocusBg();
        this.mNormalDrawable = getNormalBg();
    }

    private void initImageLayer(Context context) {
        this.mFrameLayout = new FrameLayout(context);
        this.mAlbumItemCornerImage = new AlbumItemCornerImage(context);
        LayoutParams params = new LayoutParams(-1, -1);
        params.gravity = 1;
        this.mFrameLayout.addView(this.mAlbumItemCornerImage, params);
        this.mDescText = new TextView(context);
        this.mDescText.setTextColor(-1);
        this.mDescText.setFocusable(false);
        this.mDescText.setMaxLines(3);
        this.mDescText.setEllipsize(TruncateAt.END);
        this.mDescText.setTextSize(0, 18.0f);
        this.mDescText.setGravity(80);
        this.mDescText.setPadding(8, 0, 5, 3);
        this.mDescText.setTextColor(Color.parseColor("#999999"));
        this.mFrameLayout.addView(this.mDescText, new LayoutParams(-1, -1));
        this.downloadView = new RelativeLayout(context);
        this.mFrameLayout.addView(this.downloadView, new LayoutParams(-1, -1));
        LinearLayout.LayoutParams fl = new LinearLayout.LayoutParams(this.imageWidth, this.imageHeight);
        fl.gravity = 1;
        addView(this.mFrameLayout, fl);
    }

    private void initText(Context context) {
        this.mNameText = new TextView(context);
        this.mNameText.setFocusable(false);
        this.mNameText.setMaxLines(2);
        this.mNameText.setTextColor(Color.parseColor("#999999"));
        this.mNameText.setGravity(1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(this.textWidth, this.textHeight);
        params.gravity = 1;
        params.setMargins(0, this.textOffset, 0, 0);
        setTextStyle(false);
        addView(this.mNameText, params);
    }

    public void setText(CharSequence text) {
        this.mNameText.setTag(text);
        this.mNameText.setText((CharSequence) this.mNameText.getTag());
    }

    public void setTextColor(int color) {
        this.mNameText.setTextColor(color);
    }

    public void setTextSize(float size) {
        this.mNameText.setTextSize(0, size);
    }

    public void setDescBackground(int id) {
        this.mDescText.setBackgroundResource(id);
    }

    public void setDescSize(float size) {
        this.mDescText.setTextSize(0, size);
    }

    public void setDescText(CharSequence text) {
        if (text == null || text.length() <= 0) {
            this.mDescText.setVisibility(8);
        } else {
            this.mDescText.setVisibility(0);
        }
        this.mDescText.setText(text);
    }

    public void setDescColor(int color) {
        this.mDescText.setTextColor(color);
    }

    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return ((int) Math.ceil((double) (fm.descent - fm.top))) + 2;
    }

    private void setTextStyle(boolean selected) {
        if (selected) {
            setTextColor(Color.parseColor("#ffffff"));
            setDescColor(Color.parseColor("#ffffff"));
            this.mNameText.setEllipsize(TruncateAt.END);
            this.mNameText.setSingleLine(false);
            this.mNameText.setMaxLines(2);
            if (this.mNameText.getTag() != null) {
                this.mNameText.setText(TextUtils.ellipsize((CharSequence) this.mNameText.getTag(), this.mNameText.getPaint(), (float) (getTextWidth() * 2), TruncateAt.END));
            }
            changeContrast(this.mAlbumItemCornerImage.getImageDrawable(), this.mBright);
            return;
        }
        setTextColor(Color.parseColor("#999999"));
        setDescColor(Color.parseColor("#999999"));
        this.mNameText.setEllipsize(TruncateAt.MIDDLE);
        this.mNameText.setSingleLine(true);
        changeContrast(this.mAlbumItemCornerImage.getImageDrawable(), this.mNormal);
        this.mNameText.setText((CharSequence) this.mNameText.getTag());
    }

    public void setImageResource(int pos, int resId) {
        this.mAlbumItemCornerImage.setImageResource(resId);
        switch (this.animationType) {
            case 1:
                if (sparseIntArray.get(pos) != resId) {
                    sparseIntArray.put(pos, resId);
                    startAnimation(this.mAlbumItemCornerImage);
                    return;
                }
                this.mAlbumItemCornerImage.setAlpha(1.0f);
                return;
            default:
                return;
        }
    }

    public void setCornerResId(int resId) {
        this.mAlbumItemCornerImage.setCornerResId(resId);
        changeContrast(this.mAlbumItemCornerImage.getImageDrawable(), this.mNormal);
    }

    public void setImageBitmap(Bitmap bm) {
        this.mAlbumItemCornerImage.setImageBitmap(bm);
        changeContrast(this.mAlbumItemCornerImage.getImageDrawable(), this.mNormal);
    }

    public void setBackgroundResource(int resid) {
        this.mFrameLayout.setBackgroundResource(resid);
    }

    private void changeContrast(Drawable drawable, float brightness) {
        if (this.imageBright) {
            ImageUtils.changeContrast(drawable, brightness);
        }
    }

    private void startAnimation(View view) {
        switch (this.animationType) {
            case 1:
                Animation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(1000);
                animation.setFillAfter(true);
                view.startAnimation(animation);
                return;
            default:
                return;
        }
    }

    public void log(String msg) {
        LogUtils.e("griditemlayout", msg);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            this.mFrameLayout.setBackgroundDrawable(this.mFocusDrawable);
            setTextStyle(true);
        } else {
            this.mFrameLayout.setBackgroundDrawable(this.mNormalDrawable);
            setTextStyle(false);
        }
        if (this.mScaleWhenFocus) {
            PageViewUtils.zoomAnimation(this.mContext, this.mFrameLayout, hasFocus);
        }
    }

    public boolean isScaleWhenFocus() {
        return this.mScaleWhenFocus;
    }

    public void setScaleWhenFocus(boolean mScaleWhenFocus) {
        this.mScaleWhenFocus = mScaleWhenFocus;
    }

    public TextView getDescText() {
        return this.mDescText;
    }

    public RelativeLayout getDownloadView() {
        return this.downloadView;
    }
}
