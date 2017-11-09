package com.gala.cloudui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.gala.cloudui.block.Cute;
import com.gala.cloudui.block.CuteBg;
import com.gala.cloudui.block.CuteImage;
import com.gala.cloudui.block.CuteText;
import com.gala.cloudui.imp.ICloudViewGala;
import com.gala.cloudui.utils.CloudUtilsGala;
import com.gala.cloudui.utils.CuteUtils;

public class CloudViewGala extends View implements ICloudViewGala {
    private int a;
    private Rect f246a;
    private CuteBg f247a;
    private Cute[] f248a;
    private int b;
    private int c;
    private int d;

    public CloudViewGala(Context context) {
        super(context);
        this.f246a = new Rect();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public CloudViewGala(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloudViewGala(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.f246a = new Rect();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (CloudUtilsGala.isArrayEmpty(this.f248a)) {
            Log.e("q/cloud/CloudViewGala", "q/cloud/CloudViewGala---onDraw--return---mcuteMap = null ");
        } else {
            a(canvas);
        }
    }

    private void a(boolean z) {
        if (z) {
            CloudUtilsGala.calcNinePatchBorders(getBackground(), this.f246a);
        }
        int measuredHeight = getMeasuredHeight();
        if (measuredHeight > 0) {
            this.b = measuredHeight;
        }
        measuredHeight = getMeasuredWidth();
        if (measuredHeight > 0) {
            this.a = measuredHeight;
        }
        this.c = (getItemWidth() - this.f246a.left) - this.f246a.right;
        this.d = (getItemHeight() - this.f246a.top) - this.f246a.bottom;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!CloudUtilsGala.isArrayEmpty(this.f248a)) {
            if (getItemHeight() != getMeasuredHeight() || getItemWidth() != getMeasuredWidth()) {
                a(false);
            }
        }
    }

    private void a(Canvas canvas) {
        int arraySize = CloudUtilsGala.getArraySize(this.f248a);
        for (int i = 0; i < arraySize; i++) {
            Cute cute = this.f248a[i];
            if (cute != null) {
                cute.draw(canvas);
            }
        }
    }

    @SuppressLint({"MissingSuperCall"})
    protected boolean verifyDrawable(Drawable who) {
        return true;
    }

    public void setBackgroundResource(int resId) {
        a(resId <= 0 ? null : getResources().getDrawable(resId));
    }

    public void setBackground(Drawable drawable) {
        a(drawable);
    }

    public Drawable getBackground() {
        return this.f247a != null ? this.f247a.getBackgroundDrawable() : null;
    }

    public void setBackgroundDrawable(Drawable drawable) {
        a(drawable);
    }

    private void a(Drawable drawable) {
        if (drawable != null && this.f247a != null) {
            this.f247a.setBackgroundDrawable(drawable);
            a(true);
            invalidate();
        }
    }

    public int getItemWidth() {
        return this.a;
    }

    public int getItemHeight() {
        return this.b;
    }

    public Rect getNinePatchBorders() {
        return this.f246a;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (this.f247a != null) {
            this.f247a.setPaddingLeft(left);
            this.f247a.setPaddingTop(top);
            this.f247a.setPaddingRight(right);
            this.f247a.setPaddingBottom(bottom);
            a(false);
            invalidate();
        }
    }

    public void setBgPaddingLeft(int bgPaddingLeft) {
        if (this.f247a != null) {
            this.f247a.setPaddingLeft(bgPaddingLeft);
            a(false);
            invalidate();
        }
    }

    public void setBgPaddingTop(int bgPaddingTop) {
        if (this.f247a != null) {
            this.f247a.setPaddingTop(bgPaddingTop);
            a(false);
            invalidate();
        }
    }

    public void setBgPaddingRight(int bgPaddingRight) {
        if (this.f247a != null) {
            this.f247a.setPaddingRight(bgPaddingRight);
            a(false);
            invalidate();
        }
    }

    public void setBgPaddingBottom(int bgPaddingBottom) {
        if (this.f247a != null) {
            this.f247a.setPaddingBottom(bgPaddingBottom);
            a(false);
            invalidate();
        }
    }

    public int getPaddingLeft() {
        return this.f247a == null ? 0 : this.f247a.getPaddingLeft();
    }

    public int getPaddingTop() {
        return this.f247a == null ? 0 : this.f247a.getPaddingTop();
    }

    public int getPaddingRight() {
        return this.f247a == null ? 0 : this.f247a.getPaddingRight();
    }

    public int getPaddingBottom() {
        return this.f247a == null ? 0 : this.f247a.getPaddingBottom();
    }

    public int getContentWidth() {
        return this.c;
    }

    public int getContentHeight() {
        return this.d;
    }

    public void setStyleByName(String styleName) {
        if (TextUtils.isEmpty(styleName)) {
            Log.e("q/cloud/CloudViewGala", "setStyleByName, return, styleName=" + styleName);
            CuteCacheUtils.recycleCute(this.f248a);
            this.f247a = null;
            this.f248a = null;
            a(true);
            return;
        }
        Cute[] cutes = CuteUtils.getCutes(styleName);
        this.f247a = null;
        this.f248a = CuteCacheUtils.getCutes(this, cutes);
        a(true);
    }

    public void recycle() {
        CuteCacheUtils.recycleCute(this.f248a);
        this.f248a = null;
        this.f247a = null;
    }

    public CuteBg getCuteBg() {
        return this.f247a;
    }

    void a(CuteBg cuteBg) {
        this.f247a = cuteBg;
    }

    public Cute getChildAt(String id) {
        int arraySize = CloudUtilsGala.getArraySize(this.f248a);
        for (int i = 0; i < arraySize; i++) {
            Cute cute = this.f248a[i];
            if (cute != null && TextUtils.equals(id, cute.getId())) {
                return cute;
            }
        }
        return null;
    }

    public CuteText getCuteText(String id) {
        Cute childAt = getChildAt(id);
        if (childAt instanceof CuteText) {
            return (CuteText) childAt;
        }
        return null;
    }

    public CuteImage getCuteImage(String id) {
        Cute childAt = getChildAt(id);
        if (childAt instanceof CuteImage) {
            return (CuteImage) childAt;
        }
        return null;
    }

    public void setZOrder(String id, int order) {
        Cute childAt = getChildAt(id);
        if (!CloudUtilsGala.isArrayEmpty(this.f248a) && childAt != null) {
            childAt.setZOrder(order);
            CuteUtils.sortCuteMap(this.f248a);
            invalidate();
        }
    }

    public void setZOrder(Cute cute, int order) {
        if (!CloudUtilsGala.isArrayEmpty(this.f248a) && cute != null) {
            cute.setZOrder(order);
            CuteUtils.sortCuteMap(this.f248a);
            invalidate();
        }
    }

    Cute[] a() {
        return this.f248a;
    }

    public void setText(String id, String text) {
        CuteText cuteText = getCuteText(id);
        if (cuteText != null) {
            cuteText.setText(text);
        }
    }

    public void setDrawable(String id, Drawable drawable) {
        CuteImage cuteImage = getCuteImage(id);
        if (cuteImage != null) {
            cuteImage.setDrawable(drawable);
        }
    }
}
