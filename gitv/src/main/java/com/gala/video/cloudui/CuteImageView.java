package com.gala.video.cloudui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.gala.video.cloudui.view.impl.IImage;
import java.io.Serializable;
import pl.droidsonroids.gif.GifDrawable;

public class CuteImageView extends CuteView implements IImage, Serializable {
    public boolean clipCanvas = true;
    public Drawable drawable;
    public Gravity4CuteImage gravity = Gravity4CuteImage.CENTER_IN_PARENT;
    public int height;
    public int marginBottom;
    public int marginLeft;
    public int marginRight;
    public int marginTop;
    public int paddingBottom;
    public int paddingLeft;
    public int paddingRight;
    public int paddingTop;
    public ScaleType4CuteImage scaleType = ScaleType4CuteImage.FIT_XY;
    public int visible = 0;
    public int width;

    public ScaleType4CuteImage getScaleType() {
        return this.scaleType;
    }

    public void setScaleType(ScaleType4CuteImage scaleType) {
        if (this.scaleType != scaleType) {
            this.scaleType = scaleType;
            invalidate();
        }
    }

    public Gravity4CuteImage getGravity() {
        return this.gravity;
    }

    public void setGravity(Gravity4CuteImage gravity) {
        if (this.gravity != gravity) {
            this.gravity = gravity;
            invalidate();
        }
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public void setDrawable(Drawable drawable) {
        if (drawable != null) {
            drawable.setCallback(getCloudView());
        }
        if (this.drawable != drawable) {
            this.drawable = drawable;
            invalidate();
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.drawable = null;
        if (bitmap != null) {
            this.drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            if (this.drawable != null) {
                this.drawable.setCallback(getCloudView());
            }
        }
        invalidate();
    }

    public void setResourceId(int resId) {
        a(resId);
        invalidate();
    }

    void a(int i) {
        this.drawable = null;
        if (i > 0) {
            Resources a = CloudUtils.a(getContext());
            try {
                this.drawable = new GifDrawable(a, i);
            } catch (Exception e) {
                try {
                    this.drawable = a.getDrawable(i);
                } catch (Exception e2) {
                }
            }
            if (this.drawable != null) {
                this.drawable.setCallback(getCloudView());
            }
        }
    }

    public int getVisible() {
        return this.visible;
    }

    public void setVisible(int visible) {
        if (this.visible != visible) {
            this.visible = visible;
            invalidate();
        }
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        if (this.width != width) {
            this.width = width;
            invalidate();
        }
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        if (this.height != height) {
            this.height = height;
            invalidate();
        }
    }

    public int getMarginLeft() {
        return this.marginLeft;
    }

    public void setMarginLeft(int leftMargin) {
        if (this.marginLeft != leftMargin) {
            this.marginLeft = leftMargin;
            invalidate();
        }
    }

    public int getMarginTop() {
        return this.marginTop;
    }

    public void setMarginTop(int topMargin) {
        if (this.marginTop != topMargin) {
            this.marginTop = topMargin;
            invalidate();
        }
    }

    public int getMarginRight() {
        return this.marginRight;
    }

    public void setMarginRight(int rightMargin) {
        if (this.marginRight != rightMargin) {
            this.marginRight = rightMargin;
            invalidate();
        }
    }

    public int getMarginBottom() {
        return this.marginBottom;
    }

    public void setMarginBottom(int bottomMargin) {
        if (this.marginBottom != bottomMargin) {
            this.marginBottom = bottomMargin;
            invalidate();
        }
    }

    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        if (this.paddingLeft != paddingLeft) {
            this.paddingLeft = paddingLeft;
            invalidate();
        }
    }

    public int getPaddingTop() {
        return this.paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        if (this.paddingTop != paddingTop) {
            this.paddingTop = paddingTop;
            invalidate();
        }
    }

    public int getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        if (this.paddingRight != paddingRight) {
            this.paddingRight = paddingRight;
            invalidate();
        }
    }

    public int getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        if (this.paddingBottom != paddingBottom) {
            this.paddingBottom = paddingBottom;
            invalidate();
        }
    }

    public boolean isClipCanvas() {
        return this.clipCanvas;
    }

    public void setClipCanvas(boolean clipCanvas) {
        if (this.clipCanvas != clipCanvas) {
            this.clipCanvas = clipCanvas;
            invalidate();
        }
    }

    public void draw(Canvas canvas) {
        if (this.drawable != null && getInfoModel() != null && this.visible == 0) {
            int i;
            int i2;
            int i3;
            int i4 = this.width;
            int i5 = this.height;
            if (this.clipCanvas) {
                i = getInfoModel().ninePatchBorder;
            } else {
                i = 0;
            }
            if (this.scaleType == ScaleType4CuteImage.MATRIX) {
                if (i4 == 0) {
                    i4 = getInfoModel().itemWidth - (i * 2);
                }
                if (i5 == 0) {
                    i5 = getInfoModel().itemHeight - (i * 2);
                }
                i2 = (i4 - this.paddingLeft) - this.paddingRight;
                i4 = (i5 - this.paddingTop) - this.paddingBottom;
                if (this.gravity == Gravity4CuteImage.LEFT_OF_TOP) {
                    i5 = i;
                } else if (this.gravity == Gravity4CuteImage.LEFT_OF_CENTER) {
                    i5 = i;
                    i = (getInfoModel().itemHeight / 2) - (i4 / 2);
                } else if (this.gravity == Gravity4CuteImage.LEFT_OF_BOTTOM) {
                    i5 = i;
                    i = (getInfoModel().itemHeight - i) - i4;
                } else if (this.gravity == Gravity4CuteImage.RIGHT_OF_TOP) {
                    i5 = (getInfoModel().itemWidth - i) - i2;
                } else if (this.gravity == Gravity4CuteImage.RIGHT_OF_CENTER) {
                    i5 = (getInfoModel().itemWidth - i) - i2;
                    i = (getInfoModel().itemHeight / 2) - (i4 / 2);
                } else if (this.gravity == Gravity4CuteImage.RIGHT_OF_BOTTOM) {
                    i5 = (getInfoModel().itemWidth - i) - i2;
                    i = (getInfoModel().itemHeight - i) - i4;
                } else if (this.gravity == Gravity4CuteImage.CENTER_OF_TOP) {
                    i5 = (getInfoModel().itemWidth / 2) - (i2 / 2);
                } else if (this.gravity == Gravity4CuteImage.CENTER_OF_BOTTOM) {
                    i5 = (getInfoModel().itemWidth / 2) - (i2 / 2);
                    i = (getInfoModel().itemHeight - i) - i4;
                } else {
                    i5 = (getInfoModel().itemWidth / 2) - (i2 / 2);
                    i = (getInfoModel().itemHeight / 2) - (i4 / 2);
                }
                i3 = (i + this.marginTop) - this.marginBottom;
                i = i4;
                int i6 = i2;
                i2 = (i5 + this.marginLeft) - this.marginRight;
                i5 = i6;
            } else if (this.scaleType == ScaleType4CuteImage.FIT_START) {
                if (i4 == 0) {
                    i4 = getInfoModel().itemWidth - (i * 2);
                }
                i4 = (i4 - this.paddingLeft) - this.paddingRight;
                if (i5 == 0) {
                    i5 = (this.drawable.getIntrinsicHeight() * i4) / this.drawable.getIntrinsicWidth();
                }
                i2 = this.paddingLeft + i;
                i3 = this.paddingTop + i;
                i = (i5 - this.paddingTop) - this.paddingBottom;
                i5 = i4;
            } else if (this.scaleType == ScaleType4CuteImage.FIT_END) {
                if (i4 == 0) {
                    i4 = getInfoModel().itemWidth - (i * 2);
                }
                i4 = (i4 - this.paddingLeft) - this.paddingRight;
                if (i5 == 0) {
                    i5 = (this.drawable.getIntrinsicHeight() * i4) / this.drawable.getIntrinsicWidth();
                }
                i5 = (i5 - this.paddingTop) - this.paddingBottom;
                i2 = this.paddingLeft + i;
                i3 = (getInfoModel().itemHeight - (i * 2)) - ((this.paddingBottom + i5) - i);
                i = i5;
                i5 = i4;
            } else {
                if (i4 == 0) {
                    i4 = getInfoModel().itemWidth - (i * 2);
                }
                if (i5 == 0) {
                    i5 = getInfoModel().itemHeight - (i * 2);
                }
                i2 = this.paddingLeft + i;
                i3 = this.paddingTop + i;
                i = (i5 - this.paddingTop) - this.paddingBottom;
                i5 = (i4 - this.paddingLeft) - this.paddingRight;
            }
            int i7 = i2 + i5;
            int i8 = i3 + i;
            if (!(this.drawable instanceof GifDrawable) || !((GifDrawable) this.drawable).isRecycled()) {
                drawImageBylimit(this.drawable, canvas, i2, i3, i7, i8, this.clipCanvas);
            }
        }
    }
}
