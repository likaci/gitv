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
    private int a;
    private Drawable f818a;
    private String f819a;
    private boolean f820a;
    private Drawable b;
    private String f821b;
    private Drawable c;
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
            setStyle(CloudUtils.a(context).getResources().getString(attributeResourceValue));
        } else {
            setStyle(attributeValue);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.infoModel == null || (this.infoModel.viewMap.length == 0 && this.infoModel.itemBackground == null)) {
            Log.e("q/cloud/CloudView", "q/cloud/CloudView---onDraw--return---mInfoModel = " + this.infoModel);
        } else {
            a(canvas);
        }
    }

    private void a(boolean z) {
        if (this.infoModel == null) {
            Log.e("q/cloud/CloudView", "q/cloud/CloudView---initComponent error mInfoModel==null");
            return;
        }
        if (z) {
            if (this.f818a == null) {
                this.f818a = this.infoModel.itemBackground;
            } else {
                this.infoModel.itemBackground = this.f818a;
            }
            this.infoModel.ninePatchBorder = CloudUtils.calcNinePatchBorder(this.f818a);
            this.a = this.infoModel.ninePatchBorder * 2;
            this.b = CloudUtils.getCurStateDrawable(this.f818a, new int[]{16842908});
            this.c = CloudUtils.getCurStateDrawable(this.f818a, new int[]{16842910});
        }
        int measuredHeight = getMeasuredHeight();
        if (measuredHeight > 0) {
            this.infoModel.itemHeight = measuredHeight;
        }
        measuredHeight = getMeasuredWidth();
        if (measuredHeight > 0) {
            this.infoModel.itemWidth = measuredHeight;
        }
        this.infoModel.contentWidth = this.infoModel.itemWidth - this.a;
        this.infoModel.contentHeight = this.infoModel.itemHeight - this.a;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.infoModel == null) {
            return;
        }
        if (this.infoModel.itemHeight != getMeasuredHeight() || this.infoModel.itemWidth != getMeasuredWidth()) {
            a(false);
        }
    }

    private void a(Canvas canvas) {
        int i = 0;
        this.infoModel.focused = isFocused();
        this.f820a = false;
        CuteView[] cuteViewArr = this.infoModel.viewMap;
        int length = cuteViewArr.length;
        int i2 = this.infoModel.order;
        while (i < length + 1) {
            if (!this.f820a && i2 == i) {
                this.f820a = true;
                b(canvas);
                i--;
            } else if (!(i == length || cuteViewArr[i] == null)) {
                cuteViewArr[i].draw(canvas);
            }
            i++;
        }
        if (!this.f820a) {
            this.f820a = true;
            b(canvas);
        }
    }

    private void b(Canvas canvas) {
        int i = this.infoModel.bgPaddingLeft;
        int i2 = this.infoModel.bgPaddingTop;
        int i3 = this.infoModel.itemWidth - this.infoModel.bgPaddingRight;
        int i4 = this.infoModel.itemHeight - this.infoModel.bgPaddingBottom;
        if (isFocused()) {
            a(canvas, i, i2, i3, i4, this.b);
            return;
        }
        a(canvas, i, i2, i3, i4, this.c);
    }

    private void a(Canvas canvas, int i, int i2, int i3, int i4, Drawable drawable) {
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
        this.f818a = drawable;
        if (this.infoModel != null) {
            this.infoModel.itemBackground = this.f818a;
            a(true);
            invalidate();
        }
    }

    public Drawable getBackground() {
        return this.f818a;
    }

    public Drawable getItemBackground() {
        return this.f818a;
    }

    public void setOrder(int order) {
        this.infoModel.order = order;
        a(false);
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
        a(false);
        invalidate();
    }

    public int getBgPaddingLeft() {
        return this.infoModel.bgPaddingLeft;
    }

    public void setBgPaddingTop(int bgPaddingTop) {
        this.infoModel.bgPaddingTop = bgPaddingTop;
        a(false);
        invalidate();
    }

    public int getBgPaddingTop() {
        return this.infoModel.bgPaddingTop;
    }

    public void setBgPaddingRight(int bgPaddingRight) {
        this.infoModel.bgPaddingRight = bgPaddingRight;
        a(false);
        invalidate();
    }

    public int getBgPaddingRight() {
        return this.infoModel.bgPaddingRight;
    }

    public void setBgPaddingBottom(int bgPaddingBottom) {
        this.infoModel.bgPaddingBottom = bgPaddingBottom;
        a(false);
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
        this.f819a = stylePath;
        boolean isEmpty = TextUtils.isEmpty(this.f819a);
        this.infoModel = isEmpty ? null : CloudUtils.a(this.f819a, this);
        setFocusable(isEmpty ? false : this.infoModel.focusable);
        a(true);
    }

    public String getStyle() {
        return this.f819a;
    }

    public void setStyleStream(String styleStream) {
        this.f821b = styleStream;
        boolean isEmpty = TextUtils.isEmpty(styleStream);
        this.infoModel = isEmpty ? null : CloudUtils.a(this, this.f821b);
        setFocusable(isEmpty ? false : this.infoModel.focusable);
        a(true);
    }

    public String getStyleStream() {
        return this.f821b;
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
