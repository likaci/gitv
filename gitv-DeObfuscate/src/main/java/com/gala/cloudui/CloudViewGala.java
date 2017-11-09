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
    private int f371a;
    private Rect f372a;
    private CuteBg f373a;
    private Cute[] f374a;
    private int f375b;
    private int f376c;
    private int f377d;

    public CloudViewGala(Context context) {
        super(context);
        this.f372a = new Rect();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public CloudViewGala(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloudViewGala(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.f372a = new Rect();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (CloudUtilsGala.isArrayEmpty(this.f374a)) {
            Log.e("q/cloud/CloudViewGala", "q/cloud/CloudViewGala---onDraw--return---mcuteMap = null ");
        } else {
            m243a(canvas);
        }
    }

    private void m245a(boolean z) {
        if (z) {
            CloudUtilsGala.calcNinePatchBorders(getBackground(), this.f372a);
        }
        int measuredHeight = getMeasuredHeight();
        if (measuredHeight > 0) {
            this.f375b = measuredHeight;
        }
        measuredHeight = getMeasuredWidth();
        if (measuredHeight > 0) {
            this.f371a = measuredHeight;
        }
        this.f376c = (getItemWidth() - this.f372a.left) - this.f372a.right;
        this.f377d = (getItemHeight() - this.f372a.top) - this.f372a.bottom;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!CloudUtilsGala.isArrayEmpty(this.f374a)) {
            if (getItemHeight() != getMeasuredHeight() || getItemWidth() != getMeasuredWidth()) {
                m245a(false);
            }
        }
    }

    private void m243a(Canvas canvas) {
        int arraySize = CloudUtilsGala.getArraySize(this.f374a);
        for (int i = 0; i < arraySize; i++) {
            Cute cute = this.f374a[i];
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
        m244a(resId <= 0 ? null : getResources().getDrawable(resId));
    }

    public void setBackground(Drawable drawable) {
        m244a(drawable);
    }

    public Drawable getBackground() {
        return this.f373a != null ? this.f373a.getBackgroundDrawable() : null;
    }

    public void setBackgroundDrawable(Drawable drawable) {
        m244a(drawable);
    }

    private void m244a(Drawable drawable) {
        if (drawable != null && this.f373a != null) {
            this.f373a.setBackgroundDrawable(drawable);
            m245a(true);
            invalidate();
        }
    }

    public int getItemWidth() {
        return this.f371a;
    }

    public int getItemHeight() {
        return this.f375b;
    }

    public Rect getNinePatchBorders() {
        return this.f372a;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (this.f373a != null) {
            this.f373a.setPaddingLeft(left);
            this.f373a.setPaddingTop(top);
            this.f373a.setPaddingRight(right);
            this.f373a.setPaddingBottom(bottom);
            m245a(false);
            invalidate();
        }
    }

    public void setBgPaddingLeft(int bgPaddingLeft) {
        if (this.f373a != null) {
            this.f373a.setPaddingLeft(bgPaddingLeft);
            m245a(false);
            invalidate();
        }
    }

    public void setBgPaddingTop(int bgPaddingTop) {
        if (this.f373a != null) {
            this.f373a.setPaddingTop(bgPaddingTop);
            m245a(false);
            invalidate();
        }
    }

    public void setBgPaddingRight(int bgPaddingRight) {
        if (this.f373a != null) {
            this.f373a.setPaddingRight(bgPaddingRight);
            m245a(false);
            invalidate();
        }
    }

    public void setBgPaddingBottom(int bgPaddingBottom) {
        if (this.f373a != null) {
            this.f373a.setPaddingBottom(bgPaddingBottom);
            m245a(false);
            invalidate();
        }
    }

    public int getPaddingLeft() {
        return this.f373a == null ? 0 : this.f373a.getPaddingLeft();
    }

    public int getPaddingTop() {
        return this.f373a == null ? 0 : this.f373a.getPaddingTop();
    }

    public int getPaddingRight() {
        return this.f373a == null ? 0 : this.f373a.getPaddingRight();
    }

    public int getPaddingBottom() {
        return this.f373a == null ? 0 : this.f373a.getPaddingBottom();
    }

    public int getContentWidth() {
        return this.f376c;
    }

    public int getContentHeight() {
        return this.f377d;
    }

    public void setStyleByName(String styleName) {
        if (TextUtils.isEmpty(styleName)) {
            Log.e("q/cloud/CloudViewGala", "setStyleByName, return, styleName=" + styleName);
            CuteCacheUtils.recycleCute(this.f374a);
            this.f373a = null;
            this.f374a = null;
            m245a(true);
            return;
        }
        Cute[] cutes = CuteUtils.getCutes(styleName);
        this.f373a = null;
        this.f374a = CuteCacheUtils.getCutes(this, cutes);
        m245a(true);
    }

    public void recycle() {
        CuteCacheUtils.recycleCute(this.f374a);
        this.f374a = null;
        this.f373a = null;
    }

    public CuteBg getCuteBg() {
        return this.f373a;
    }

    void m246a(CuteBg cuteBg) {
        this.f373a = cuteBg;
    }

    public Cute getChildAt(String id) {
        int arraySize = CloudUtilsGala.getArraySize(this.f374a);
        for (int i = 0; i < arraySize; i++) {
            Cute cute = this.f374a[i];
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
        if (!CloudUtilsGala.isArrayEmpty(this.f374a) && childAt != null) {
            childAt.setZOrder(order);
            CuteUtils.sortCuteMap(this.f374a);
            invalidate();
        }
    }

    public void setZOrder(Cute cute, int order) {
        if (!CloudUtilsGala.isArrayEmpty(this.f374a) && cute != null) {
            cute.setZOrder(order);
            CuteUtils.sortCuteMap(this.f374a);
            invalidate();
        }
    }

    Cute[] m247a() {
        return this.f374a;
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
