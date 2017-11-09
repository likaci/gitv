package com.gala.video.uiutils.albumcorner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.widget.util.LogUtils;

public class AlbumItemCornerImage extends FrameLayout {
    private static final float LAND_RATIO = 0.2625f;
    private static final float PORT_RATIO = 0.4294f;
    private static final String TAG = "AlbumItemCornerImage";
    private ImageView albumImage = null;
    private ImageView cornerImage = null;
    private int mCornerImageHeight = -1;
    private int mCornerImageWidth = -1;

    public AlbumItemCornerImage(Context context) {
        super(context);
        init(context);
    }

    public AlbumItemCornerImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AlbumItemCornerImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LogUtils.d(TAG, InterfaceKey.SHARE_IT);
        this.albumImage = new ImageView(context);
        this.cornerImage = new ImageView(context);
        this.albumImage.setScaleType(ScaleType.FIT_XY);
        this.cornerImage.setScaleType(ScaleType.FIT_XY);
        this.cornerImage.setAdjustViewBounds(true);
        LayoutParams cornerParams = new LayoutParams(-2, -2, 53);
        addView(this.albumImage);
        addView(this.cornerImage, cornerParams);
    }

    public void setImageBitmap(Bitmap bm) {
        this.albumImage.setImageBitmap(bm);
    }

    public void setImageDrawable(Drawable drawable) {
        this.albumImage.setImageDrawable(drawable);
    }

    public void setImageResource(int resId) {
        this.albumImage.setImageResource(resId);
    }

    public void setCornerResId(int resId) {
        Drawable d = null;
        try {
            d = getContext().getResources().getDrawable(resId);
        } catch (Exception e) {
            LogUtils.e(TAG, "setCornerResId(" + resId + "), exception happened: " + e.getMessage());
        }
        this.cornerImage.setImageDrawable(d);
    }

    public void setCornerDrawable(Drawable drawable) {
        this.cornerImage.setImageDrawable(drawable);
    }

    public void setCornerBitmap(Bitmap bmp) {
        this.cornerImage.setImageBitmap(bmp);
    }

    public void setCornerDimens(int cornerWidth, int cornerHeight) {
        this.mCornerImageWidth = cornerWidth;
        this.mCornerImageHeight = cornerHeight;
    }

    public Drawable getImageDrawable() {
        return this.albumImage.getDrawable();
    }

    @Deprecated
    public Drawable getDrawable() {
        return this.albumImage.getDrawable();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width > 0 && height > 0) {
            LayoutParams params = (LayoutParams) this.cornerImage.getLayoutParams();
            if (this.mCornerImageWidth <= 0 || this.mCornerImageHeight <= 0) {
                if (width > height) {
                    params.width = Math.round(((float) width) * LAND_RATIO);
                } else {
                    params.width = Math.round(((float) width) * PORT_RATIO);
                }
                this.cornerImage.setLayoutParams(params);
            } else {
                params.width = this.mCornerImageWidth;
                params.height = this.mCornerImageHeight;
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
