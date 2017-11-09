package com.gala.video.cloudui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.gala.video.cloudui.view.impl.ICloudView;
import com.gala.video.cloudui.view.model.QCloudViewInfoModel;

public class CloudView extends View implements ICloudView {
    private int f1900a;
    private Drawable f1901a;
    private String f1902a;
    private boolean f1903a;
    private Drawable f1904b;
    private String f1905b;
    private Drawable f1906c;
    public QCloudViewInfoModel infoModel;

    public CloudView(Context context) {
        super(context);
    }

    public CloudView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloudView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        String attributeValue = attrs.getAttributeValue(null, "style");
        int attributeResourceValue = attrs.getAttributeResourceValue(null, "style", 0);
        Log.e("q/cloud/CloudView", "q/cloud/CloudView---new CloudView from xml,resourceId = " + attributeResourceValue + ",or---path=" + attributeValue);
        if (attributeResourceValue > 0) {
            setStyle(CloudUtils.m1545a(context).getResources().getString(attributeResourceValue));
        } else {
            setStyle(attributeValue);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.infoModel == null || (this.infoModel.viewMap.length == 0 && this.infoModel.itemBackground == null)) {
            Log.e("q/cloud/CloudView", "q/cloud/CloudView---onDraw--return---mInfoModel = " + this.infoModel);
        } else {
            m1534a(canvas);
        }
    }

    private void m1536a(boolean z) {
        if (this.infoModel == null) {
            Log.e("q/cloud/CloudView", "q/cloud/CloudView---initComponent error mInfoModel==null");
            return;
        }
        if (z) {
            if (this.f1901a == null) {
                this.f1901a = this.infoModel.itemBackground;
            } else {
                this.infoModel.itemBackground = this.f1901a;
            }
            this.infoModel.ninePatchBorder = CloudUtils.calcNinePatchBorder(this.f1901a);
            this.f1900a = this.infoModel.ninePatchBorder * 2;
            this.f1904b = CloudUtils.getCurStateDrawable(this.f1901a, new int[]{16842908});
            this.f1906c = CloudUtils.getCurStateDrawable(this.f1901a, new int[]{16842910});
        }
        int measuredHeight = getMeasuredHeight();
        if (measuredHeight > 0) {
            this.infoModel.itemHeight = measuredHeight;
        }
        measuredHeight = getMeasuredWidth();
        if (measuredHeight > 0) {
            this.infoModel.itemWidth = measuredHeight;
        }
        this.infoModel.contentWidth = this.infoModel.itemWidth - this.f1900a;
        this.infoModel.contentHeight = this.infoModel.itemHeight - this.f1900a;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.infoModel == null) {
            return;
        }
        if (this.infoModel.itemHeight != getMeasuredHeight() || this.infoModel.itemWidth != getMeasuredWidth()) {
            m1536a(false);
        }
    }

    private void m1534a(Canvas canvas) {
        int i = 0;
        this.infoModel.focused = isFocused();
        this.f1903a = false;
        CuteView[] cuteViewArr = this.infoModel.viewMap;
        int length = cuteViewArr.length;
        int i2 = this.infoModel.order;
        while (i < length + 1) {
            if (!this.f1903a && i2 == i) {
                this.f1903a = true;
                m1537b(canvas);
                i--;
            } else if (!(i == length || cuteViewArr[i] == null)) {
                cuteViewArr[i].draw(canvas);
            }
            i++;
        }
        if (!this.f1903a) {
            this.f1903a = true;
            m1537b(canvas);
        }
    }

    private void m1537b(Canvas canvas) {
        int i = this.infoModel.bgPaddingLeft;
        int i2 = this.infoModel.bgPaddingTop;
        int i3 = this.infoModel.itemWidth - this.infoModel.bgPaddingRight;
        int i4 = this.infoModel.itemHeight - this.infoModel.bgPaddingBottom;
        if (isFocused()) {
            m1535a(canvas, i, i2, i3, i4, this.f1904b);
            return;
        }
        m1535a(canvas, i, i2, i3, i4, this.f1906c);
    }

    private void m1535a(Canvas canvas, int i, int i2, int i3, int i4, Drawable drawable) {
        if (drawable == null) {
            drawable = CloudUtils.getCurStateDrawable(this.infoModel.itemBackground, getDrawableState());
        }
        if (drawable != null) {
            drawable.setBounds(i, i2, i3, i4);
            drawable.draw(canvas);
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        return true;
    }

    public void setBackgroundResource(int resId) {
        setItemBackground(resId <= 0 ? null : getResources().getDrawable(resId));
    }

    public void setBackground(Drawable drawable) {
        setItemBackground(drawable);
    }

    public void setBackgroundDrawable(Drawable drawable) {
        setItemBackground(drawable);
    }

    public void setItemBackground(Drawable drawable) {
        this.f1901a = drawable;
        if (this.infoModel != null) {
            this.infoModel.itemBackground = this.f1901a;
            m1536a(true);
            invalidate();
        }
    }

    public Drawable getBackground() {
        return this.f1901a;
    }

    public Drawable getItemBackground() {
        return this.f1901a;
    }

    public void setOrder(int order) {
        this.infoModel.order = order;
        m1536a(false);
        invalidate();
    }

    public int getOrder() {
        return this.infoModel.order;
    }

    public int getItemWidth() {
        return this.infoModel.itemWidth;
    }

    public int getItemHeight() {
        return this.infoModel.itemHeight;
    }

    public void setBgPaddingLeft(int bgPaddingLeft) {
        this.infoModel.bgPaddingLeft = bgPaddingLeft;
        m1536a(false);
        invalidate();
    }

    public int getBgPaddingLeft() {
        return this.infoModel.bgPaddingLeft;
    }

    public void setBgPaddingTop(int bgPaddingTop) {
        this.infoModel.bgPaddingTop = bgPaddingTop;
        m1536a(false);
        invalidate();
    }

    public int getBgPaddingTop() {
        return this.infoModel.bgPaddingTop;
    }

    public void setBgPaddingRight(int bgPaddingRight) {
        this.infoModel.bgPaddingRight = bgPaddingRight;
        m1536a(false);
        invalidate();
    }

    public int getBgPaddingRight() {
        return this.infoModel.bgPaddingRight;
    }

    public void setBgPaddingBottom(int bgPaddingBottom) {
        this.infoModel.bgPaddingBottom = bgPaddingBottom;
        m1536a(false);
        invalidate();
    }

    public int getBgPaddingBottom() {
        return this.infoModel.bgPaddingBottom;
    }

    public int getContentWidth() {
        return this.infoModel.contentWidth;
    }

    public int getContentHeight() {
        return this.infoModel.contentHeight;
    }

    public int getNinePatchBorder() {
        return this.infoModel.ninePatchBorder;
    }

    public void setStyle(String stylePath) {
        this.f1902a = stylePath;
        boolean isEmpty = TextUtils.isEmpty(this.f1902a);
        this.infoModel = isEmpty ? null : CloudUtils.m1548a(this.f1902a, this);
        setFocusable(isEmpty ? false : this.infoModel.focusable);
        m1536a(true);
    }

    public String getStyle() {
        return this.f1902a;
    }

    public void setStyleStream(String styleStream) {
        this.f1905b = styleStream;
        boolean isEmpty = TextUtils.isEmpty(styleStream);
        this.infoModel = isEmpty ? null : CloudUtils.m1547a(this, this.f1905b);
        setFocusable(isEmpty ? false : this.infoModel.focusable);
        m1536a(true);
    }

    public String getStyleStream() {
        return this.f1905b;
    }

    public CuteView getChildAt(String id) {
        if (id == null || "".equals(id) || this.infoModel.viewMap == null) {
            return null;
        }
        int length = this.infoModel.viewMap.length;
        for (int i = 0; i < length; i++) {
            if (id.equals(this.infoModel.viewMap[i].getId())) {
                return this.infoModel.viewMap[i];
            }
        }
        return null;
    }

    public CuteTextView getTextView(String id) {
        return (CuteTextView) getChildAt(id);
    }

    public CuteImageView getImageView(String id) {
        return (CuteImageView) getChildAt(id);
    }
}
