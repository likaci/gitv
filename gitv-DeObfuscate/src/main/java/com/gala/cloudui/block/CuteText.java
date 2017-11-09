package com.gala.cloudui.block;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import com.gala.cloudui.imp.ICuteText;
import com.gala.cloudui.utils.CloudUtilsGala;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class CuteText extends Cute implements ICuteText {
    private int f408A = 5;
    private int f409B = 1;
    private int f410C = 1;
    private int f411D;
    private int f412E;
    private int f413F;
    private int f414G;
    private int f415H;
    private int f416I;
    private int f417J;
    private int f418K;
    private int f419L;
    private int f420M;
    private int f421N = 1;
    private int f422O = 2;
    private int f423P = 3;
    private final int f424Q = 100;
    private int f425R;
    private int f426S;
    private int f427T;
    private int f428U;
    private int f429V;
    private int f430W;
    private int f431X;
    private int f432Y;
    private float f433a = 0.25f;
    private int f434a;
    private Canvas f435a;
    private Drawable f436a;
    private TextPaint f437a;
    private Runnable f438a;
    private String f439a;
    private boolean f440a;
    private int[] f441a = new int[3];
    private String[] f442a = new String[3];
    private float f443b = 1.0f;
    private int f444b;
    private Drawable f445b;
    private String f446b;
    private int[] f447b = new int[3];
    private float f448c;
    private int f449c;
    private int f450d;
    private int f451e;
    private int f452f;
    private int f453g;
    private int f454h;
    private int f455i;
    private int f456j;
    private int f457k = 1;
    private int f458l = 0;
    private int f459m;
    private int f460n;
    private int f461o;
    private int f462p = 1;
    private int f463q = 0;
    private int f464r;
    private int f465s = 0;
    private int f466t = 0;
    private int f467u = 500;
    private int f468v;
    private int f469w;
    private int f470x;
    private int f471y;
    private int f472z = 0;

    class C01121 implements Runnable {
        final /* synthetic */ CuteText f407a;

        C01121(CuteText this$0) {
            this.f407a = this$0;
        }

        public void run() {
            this.f407a.f440a = false;
            this.f407a.invalidate();
        }
    }

    public void setFocusFontColor(int focusColor) {
        if (this.f461o != focusColor) {
            this.f461o = focusColor;
            invalidate();
        }
    }

    public int getFontColor() {
        return this.f460n;
    }

    public void setFontColor(int normalColor) {
        if (this.f460n != normalColor) {
            this.f460n = normalColor;
            invalidate();
        }
    }

    public int getShadowLayerColor() {
        return this.f471y;
    }

    public void setShadowLayerColor(int shadowLayerColor) {
        if (this.f471y != shadowLayerColor) {
            this.f471y = shadowLayerColor;
            invalidate();
        }
    }

    public int getEllipsize() {
        return this.f466t;
    }

    public void setEllipsize(int ellipsize) {
        if (this.f466t != ellipsize) {
            this.f466t = ellipsize;
            this.f430W = 0;
            this.f440a = false;
            if (ellipsize == 1) {
                this.f440a = true;
                if (this.f438a == null) {
                    this.f438a = new C01121(this);
                }
                if (getCloudView() != null) {
                    getCloudView().postDelayed(this.f438a, (long) this.f467u);
                }
            } else if (getCloudView() != null) {
                getCloudView().removeCallbacks(this.f438a);
            }
            invalidate();
        }
    }

    public int getMarqueeDelay() {
        return this.f467u;
    }

    public void setMarqueeDelay(int marqueeDelay) {
        if (this.f467u != marqueeDelay) {
            this.f467u = marqueeDelay;
            invalidate();
        }
    }

    public int getMarqueeTextSpace() {
        return this.f468v;
    }

    public void setMarqueeTextSpace(int marqueeTextSpace) {
        if (this.f468v != marqueeTextSpace) {
            this.f468v = marqueeTextSpace;
            invalidate();
        }
    }

    public float getMarqueeSpeed() {
        return this.f443b;
    }

    public void setMarqueeSpeed(float marqueeSpeed) {
        if (this.f443b != marqueeSpeed) {
            this.f443b = marqueeSpeed;
            invalidate();
        }
    }

    public int getGravity() {
        return this.f465s;
    }

    public void setGravity(int gravity) {
        if (this.f465s != gravity) {
            this.f465s = gravity;
            invalidate();
        }
    }

    public int getFont() {
        return this.f458l;
    }

    public void setFont(int font) {
        if (this.f458l != font) {
            this.f458l = font;
            invalidate();
        }
    }

    public float getSkewX() {
        return this.f433a;
    }

    public void setSkewX(float skewX) {
        if (this.f433a != skewX) {
            this.f433a = skewX;
            invalidate();
        }
    }

    public int getLines() {
        return this.f462p;
    }

    public void setLines(int lines) {
        if (this.f462p != lines) {
            this.f462p = lines;
            invalidate();
        }
    }

    public int getLineSpace() {
        return this.f464r;
    }

    public void setLineSpace(int lineSpace) {
        if (this.f464r != lineSpace) {
            this.f464r = lineSpace;
            invalidate();
        }
    }

    public int getWidth() {
        return this.f434a;
    }

    public void setWidth(int width) {
        if (this.f434a != width) {
            this.f434a = width;
            invalidate();
        }
    }

    public int getHeight() {
        return this.f444b;
    }

    public void setHeight(int height) {
        if (this.f444b != height) {
            this.f444b = height;
            invalidate();
        }
    }

    public int getPaddingLeft() {
        return this.f449c;
    }

    public void setPaddingLeft(int paddingLeft) {
        if (this.f449c != paddingLeft) {
            this.f449c = paddingLeft;
            invalidate();
        }
    }

    public int getPaddingTop() {
        return this.f450d;
    }

    public void setPaddingTop(int paddingTop) {
        this.f450d = paddingTop;
        if (this.f450d != paddingTop) {
            this.f450d = paddingTop;
            invalidate();
        }
    }

    public int getPaddingBottom() {
        return this.f452f;
    }

    public void setPaddingBottom(int paddingBottom) {
        if (this.f452f != paddingBottom) {
            this.f452f = paddingBottom;
            invalidate();
        }
    }

    public int getPaddingRight() {
        return this.f451e;
    }

    public void setPaddingRight(int paddingRight) {
        if (this.f451e != paddingRight) {
            this.f451e = paddingRight;
            invalidate();
        }
    }

    public int getMarginTop() {
        return this.f454h;
    }

    public void setMarginTop(int marginTop) {
        if (this.f454h != marginTop) {
            this.f454h = marginTop;
            invalidate();
        }
    }

    public int getMarginBottom() {
        return this.f456j;
    }

    public void setMarginBottom(int marginBottom) {
        if (this.f456j != marginBottom) {
            this.f456j = marginBottom;
            invalidate();
        }
    }

    public int getMarginLeft() {
        return this.f453g;
    }

    public void setMarginLeft(int marginLeft) {
        if (this.f453g != marginLeft) {
            this.f453g = marginLeft;
            invalidate();
        }
    }

    public int getMarginRight() {
        return this.f455i;
    }

    public void setMarginRight(int marginRight) {
        if (this.f455i != marginRight) {
            this.f455i = marginRight;
            invalidate();
        }
    }

    public int getFontSize() {
        return this.f459m;
    }

    public void setFontSize(int size) {
        if (this.f459m != size) {
            this.f459m = size;
            invalidate();
        }
    }

    public float getShadowLayerRadius() {
        return this.f448c;
    }

    public void setShadowLayerRadius(float shadowLayerRadius) {
        if (this.f448c != shadowLayerRadius) {
            this.f448c = shadowLayerRadius;
            invalidate();
        }
    }

    public int getShadowLayerDx() {
        return this.f469w;
    }

    public void setShadowLayerDx(int shadowLayerDx) {
        if (this.f469w != shadowLayerDx) {
            this.f469w = shadowLayerDx;
            invalidate();
        }
    }

    public int getShadowLayerDy() {
        return this.f470x;
    }

    public void setShadowLayerDy(int shadowLayerDy) {
        if (this.f470x != shadowLayerDy) {
            this.f470x = shadowLayerDy;
            invalidate();
        }
    }

    public String getText() {
        return this.f439a;
    }

    public void setText(String text) {
        if (!TextUtils.equals(text, this.f439a)) {
            this.f439a = text;
            invalidate();
        }
    }

    public String getDefaultText() {
        return this.f446b;
    }

    public void setDefaultText(String text) {
        this.f446b = text;
    }

    public int getVisible() {
        return this.f457k;
    }

    public void setVisible(int visible) {
        if (this.f457k != visible) {
            this.f457k = visible;
            invalidate();
        }
    }

    public int getTitleType() {
        return this.f463q;
    }

    public void setTitleType(int type) {
        this.f463q = type;
    }

    public Drawable getBgDrawable() {
        return this.f436a;
    }

    public void setBgDrawable(Drawable bgDrawable) {
        if (this.f436a != bgDrawable) {
            this.f436a = bgDrawable;
            invalidate();
        }
    }

    public Drawable getBgFocusDrawable() {
        return this.f445b;
    }

    public void setBgFocusDrawable(Drawable bgDrawable) {
        if (this.f445b != bgDrawable) {
            this.f445b = bgDrawable;
            invalidate();
        }
    }

    public int getBgScaleType() {
        return this.f472z;
    }

    public void setBgScaleType(int bgScaleType) {
        if (this.f472z != bgScaleType) {
            this.f472z = bgScaleType;
            invalidate();
        }
    }

    public int getFocusFontColor() {
        return this.f461o;
    }

    public int getBgGravity() {
        return this.f408A;
    }

    public void setBgGravity(int bgGravity) {
        if (this.f408A != bgGravity) {
            this.f408A = bgGravity;
            invalidate();
        }
    }

    public int getBgHeight() {
        return this.f412E;
    }

    public void setBgHeight(int bgHeight) {
        if (this.f412E != bgHeight) {
            this.f412E = bgHeight;
            invalidate();
        }
    }

    public int getBgWidth() {
        return this.f411D;
    }

    public void setBgWidth(int bgWidth) {
        if (this.f411D != bgWidth) {
            this.f411D = bgWidth;
            invalidate();
        }
    }

    public int getBgPaddingLeft() {
        return this.f413F;
    }

    public void setBgPaddingLeft(int bgPaddingLeft) {
        if (this.f413F != bgPaddingLeft) {
            this.f413F = bgPaddingLeft;
            invalidate();
        }
    }

    public int getBgPaddingTop() {
        return this.f414G;
    }

    public void setBgPaddingTop(int bgPaddingTop) {
        if (this.f414G != bgPaddingTop) {
            this.f414G = bgPaddingTop;
            invalidate();
        }
    }

    public int getBgPaddingRight() {
        return this.f415H;
    }

    public void setBgPaddingRight(int bgPaddingRight) {
        if (this.f415H != bgPaddingRight) {
            this.f415H = bgPaddingRight;
            invalidate();
        }
    }

    public int getBgPaddingBottom() {
        return this.f416I;
    }

    public void setBgPaddingBottom(int bgPaddingBottom) {
        if (this.f416I != bgPaddingBottom) {
            this.f416I = bgPaddingBottom;
            invalidate();
        }
    }

    public int getBgMarginLeft() {
        return this.f417J;
    }

    public void setBgMarginLeft(int bgMarginLeft) {
        if (this.f417J != bgMarginLeft) {
            this.f417J = bgMarginLeft;
            invalidate();
        }
    }

    public int getBgMarginTop() {
        return this.f418K;
    }

    public void setBgMarginTop(int bgMarginTop) {
        if (this.f418K != bgMarginTop) {
            this.f418K = bgMarginTop;
            invalidate();
        }
    }

    public int getBgMarginRight() {
        return this.f419L;
    }

    public void setBgMarginRight(int bgMarginRight) {
        if (this.f419L != bgMarginRight) {
            this.f419L = bgMarginRight;
            invalidate();
        }
    }

    public int getBgMarginBottom() {
        return this.f420M;
    }

    public void setBgMarginBottom(int bgMarginBottom) {
        if (this.f420M != bgMarginBottom) {
            this.f420M = bgMarginBottom;
            invalidate();
        }
    }

    public int getBgClipPadding() {
        return this.f410C;
    }

    public void setBgClipPadding(int clipPadding) {
        if (this.f410C != clipPadding) {
            this.f410C = clipPadding;
            invalidate();
        }
    }

    public int getBgVisible() {
        return this.f409B;
    }

    public void setBgVisible(int bgVisible) {
        if (this.f409B != bgVisible) {
            this.f409B = bgVisible;
            invalidate();
        }
    }

    public int getRealLineCount() {
        return getRealLineCount(this.f439a);
    }

    public int getRealLineCount(String str) {
        m258a(str);
        return this.f427T;
    }

    private int m251a() {
        int i = this.f434a;
        if (this.f465s == 4 || this.f465s == 5 || i <= 0 || (i > getCloudView().getContentWidth() && getCloudView().getContentWidth() > 0)) {
            i = getCloudView().getContentWidth();
        }
        i = (i - this.f449c) - this.f451e;
        m251a();
        float textSkewX = this.f437a.getTextSkewX();
        if (textSkewX != 0.0f) {
            return (int) (((float) i) + (textSkewX * ((float) this.f459m)));
        }
        return i;
    }

    public TextPaint getPaint() {
        m251a();
        return this.f437a;
    }

    private synchronized void m258a(String str) {
        String str2 = this.f439a;
        int i = this.f457k;
        int i2 = this.f409B;
        Drawable drawable = this.f436a;
        Canvas canvas = this.f435a;
        setText(str);
        setVisible(1);
        setBgVisible(1);
        this.f435a = null;
        setBgDrawable(getCloudView().getBackground());
        draw(null);
        setText(str2);
        setVisible(i);
        setBgVisible(i2);
        this.f435a = canvas;
        setBgDrawable(drawable);
    }

    public int calcLineCount(int maxWidth) {
        int i = 6;
        int length = this.f439a.length();
        if (length <= 6) {
            i = length;
        }
        length = i;
        i = 0;
        while (length <= this.f439a.length() && length < 100) {
            int measureText = (int) this.f437a.measureText(this.f439a, 0, length);
            if (measureText >= maxWidth) {
                if (i == 2 || length == 0) {
                    break;
                }
                length--;
                i = 1;
            } else if (i == 1) {
                break;
            } else {
                length++;
                i = 2;
            }
            this.f426S = measureText;
        }
        return length - 1;
    }

    public void draw(Canvas canvas) {
        if (!getCloudView().isFocused()) {
            getCloudView().removeCallbacks(this.f438a);
        }
        this.f435a = canvas;
        this.f427T = 0;
        this.f426S = 0;
        this.f425R = 0;
        this.f442a[0] = "";
        this.f442a[1] = "";
        this.f442a[2] = "";
        this.f441a[0] = 0;
        this.f441a[1] = 0;
        this.f441a[2] = 0;
        this.f447b[0] = 0;
        this.f447b[1] = 0;
        this.f447b[2] = 0;
        if (!TextUtils.isEmpty(this.f439a) && this.f462p > 0 && this.f457k == 1) {
            int a = m251a();
            if (a > 0 && a <= 2048) {
                int contentHeight = getCloudView().getContentHeight() + getCloudView().getNinePatchBorders().top;
                if (this.f462p == 1 || this.f466t == 1 || this.f465s == 5) {
                    this.f427T = 1;
                    this.f426S = (int) this.f437a.measureText(this.f439a);
                    if (this.f426S <= a) {
                        m257a(canvas, this.f439a, contentHeight, false, this.f421N, a);
                        return;
                    }
                    m257a(canvas, this.f439a, contentHeight, true, this.f421N, a);
                    return;
                }
                int c = m262c();
                int calcLineCount;
                String substring;
                String substring2;
                if (this.f462p == 2) {
                    if (this.f466t != 0) {
                        setEllipsize(0);
                    }
                    calcLineCount = calcLineCount(a);
                    substring = this.f439a.substring(0, calcLineCount);
                    if (calcLineCount == this.f439a.length()) {
                        this.f427T = 1;
                        m257a(canvas, substring, contentHeight, false, this.f421N, a);
                        return;
                    }
                    substring2 = this.f439a.substring(calcLineCount, this.f439a.length());
                    this.f427T = 2;
                    m257a(canvas, substring, (contentHeight - c) - this.f464r, false, this.f421N, a);
                    m257a(canvas, substring2, contentHeight, true, this.f422O, a);
                    return;
                }
                if (this.f466t != 0) {
                    setEllipsize(0);
                }
                calcLineCount = calcLineCount(a);
                substring = this.f439a.substring(0, calcLineCount);
                if (calcLineCount == this.f439a.length()) {
                    substring2 = "";
                } else {
                    substring2 = this.f439a.substring(calcLineCount, this.f439a.length());
                }
                calcLineCount = 1;
                while (calcLineCount <= substring2.length() && calcLineCount < 100 && this.f437a.measureText(substring2, 0, calcLineCount) < ((float) a)) {
                    calcLineCount++;
                }
                calcLineCount--;
                if (substring2.length() > calcLineCount) {
                    String substring3 = substring2.substring(calcLineCount, substring2.length());
                    String substring4 = substring2.substring(0, calcLineCount);
                    this.f427T = 3;
                    m257a(canvas, substring, (contentHeight - (c * 2)) - (this.f464r * 2), false, this.f421N, a);
                    m257a(canvas, substring4, (contentHeight - c) - this.f464r, false, this.f422O, a);
                    m257a(canvas, substring3, contentHeight, true, this.f423P, a);
                } else if (TextUtils.isEmpty(substring2)) {
                    this.f427T = 1;
                    m257a(canvas, substring, contentHeight, false, this.f421N, a);
                } else {
                    this.f427T = 2;
                    m257a(canvas, substring, (contentHeight - c) - this.f464r, false, this.f421N, a);
                    m257a(canvas, substring2, contentHeight, false, this.f422O, a);
                }
            }
        }
    }

    private void m257a(Canvas canvas, String str, int i, boolean z, int i2, int i3) {
        if (this.f466t == 1) {
            z = false;
        }
        String str2 = z ? (String) TextUtils.ellipsize(str, this.f437a, (float) i3, CloudUtilsGala.getTruncateAt(this.f466t)) : str;
        if (this.f462p == 1 && z) {
            this.f426S = (int) this.f437a.measureText(str2);
        }
        int i4 = this.f426S;
        if (i2 != this.f421N && this.f472z == 0) {
            i4 = (int) this.f437a.measureText(str2);
            if (i4 > this.f426S) {
                this.f426S = i4;
            }
        }
        int b = m261b();
        if (this.f465s == 3 || this.f465s == 2) {
            i4 = ((i3 - i4) / 2) + b;
        } else if (this.f465s == 4) {
            i4 = ((getCloudView().getContentWidth() - i4) / 2) + b;
        } else if (this.f465s == 5) {
            i4 = (((b + getCloudView().getContentWidth()) - i4) - this.f455i) + this.f453g;
        } else {
            i4 = b;
        }
        int i5 = ((this.f454h + i) - this.f456j) - this.f431X;
        this.f425R = i5;
        b = this.f444b;
        if (this.f465s == 4 || this.f465s == 1 || this.f465s == 3) {
            if (b == 0 || this.f465s == 4) {
                b = getCloudView().getContentHeight();
            }
            int c = m262c();
            if (this.f427T == 1) {
                i5 -= (b - c) / 2;
                this.f425R = i5;
            } else if (this.f427T == 2) {
                if (i2 == this.f421N) {
                    i5 = (i5 - ((b - (c * 2)) - this.f464r)) + (((b - (c * 2)) - this.f464r) / 2);
                }
                if (i2 == this.f422O) {
                    i5 = (i5 - ((b - (c * 2)) - this.f464r)) + (((b - (c * 2)) - this.f464r) / 2);
                    this.f425R = i5;
                }
            } else if (this.f427T == 3) {
                if (i2 == this.f421N) {
                    i5 = (i5 - ((b - (c * 3)) - (this.f464r * 2))) + (((b - (c * 3)) - (this.f464r * 2)) / 2);
                }
                if (i2 == this.f422O) {
                    i5 = (i5 - ((b - (c * 2)) - this.f464r)) + ((b - c) / 2);
                }
                if (i2 == this.f423P) {
                    i5 = this.f464r + (((b / 2) + (i5 - (b - c))) + (c / 2));
                    this.f425R = i5;
                }
            }
        }
        this.f442a[i2 - 1] = str2;
        this.f441a[i2 - 1] = i5;
        this.f447b[i2 - 1] = i4;
        if (i2 == this.f427T) {
            m255a(canvas, i3);
        }
    }

    private void m255a(Canvas canvas, int i) {
        int i2 = this.f447b[0];
        m256a(canvas, i2, this.f425R, this.f426S);
        if (canvas != null) {
            int b = m261b();
            int i3 = this.f431X + this.f425R;
            int c = i3 - ((this.f427T * m262c()) + ((this.f427T - 1) * this.f464r));
            int a = m252a(b, i);
            for (int i4 = 0; i4 < this.f427T; i4++) {
                if (this.f466t == 1 && !this.f440a) {
                    if (this.f430W == 0) {
                        m254a(a);
                    }
                    if (this.f429V > this.f430W) {
                        canvas.save();
                        canvas.clipRect(b, c, a, i3);
                        canvas.drawText(this.f439a, (float) (i2 - this.f428U), (float) this.f441a[i4], this.f437a);
                        canvas.restore();
                        canvas.save();
                        canvas.clipRect(b, c, a, i3);
                        canvas.drawText(this.f439a, (float) (((i2 + this.f429V) + this.f468v) - this.f428U), (float) this.f441a[i4], this.f437a);
                        canvas.restore();
                        this.f428U = (int) (((float) this.f428U) + this.f443b);
                        if (this.f428U >= this.f429V + this.f468v) {
                            this.f428U = 0;
                        }
                        invalidate();
                        return;
                    }
                }
                canvas.save();
                canvas.clipRect(b, c, a, i3);
                canvas.drawText(this.f442a[i4], (float) this.f447b[i4], (float) this.f441a[i4], this.f437a);
                canvas.restore();
            }
        }
    }

    private void m254a(int i) {
        this.f428U = 0;
        this.f429V = m263d();
        this.f430W = i - m261b();
        if (this.f468v == 0) {
            setMarqueeTextSpace(this.f459m * 2);
        }
    }

    private int m261b() {
        int paddingLeft = ((getCloudView().getNinePatchBorders().left + getCloudView().getPaddingLeft()) + this.f453g) + this.f449c;
        float textSkewX = this.f437a.getTextSkewX();
        if (textSkewX > 0.0f) {
            return (int) (((float) paddingLeft) - (textSkewX * ((float) this.f459m)));
        }
        return paddingLeft;
    }

    private int m252a(int i, int i2) {
        int i3 = i + i2;
        int paddingLeft = (getCloudView().getNinePatchBorders().left + getCloudView().getPaddingLeft()) + getCloudView().getContentWidth();
        if (i3 <= paddingLeft) {
            paddingLeft = i3;
        }
        float textSkewX = this.f437a.getTextSkewX();
        if (textSkewX < 0.0f) {
            return (int) (((float) paddingLeft) - (textSkewX * ((float) this.f459m)));
        }
        return paddingLeft;
    }

    private int m262c() {
        return this.f431X - this.f432Y;
    }

    private int m263d() {
        return (int) this.f437a.measureText(this.f439a);
    }

    private void m253a() {
        if (this.f437a == null) {
            this.f437a = new TextPaint();
            this.f437a.setAntiAlias(true);
        }
        this.f437a.setShadowLayer(this.f448c, (float) this.f469w, (float) this.f470x, this.f471y);
        int i = this.f461o;
        int i2 = this.f460n;
        if (!getCloudView().isFocused() || i == 0) {
            i = i2;
        }
        this.f437a.setColor(i);
        this.f437a.setTextSize((float) this.f459m);
        if (CloudUtilsGala.getTypeface() != null) {
            this.f437a.setTypeface(CloudUtilsGala.getTypeface());
        }
        if (this.f458l == 0) {
            m259a(false, false);
        } else if (this.f458l == 1) {
            m259a(true, false);
        } else if (this.f458l == 2) {
            m259a(false, true);
        } else if (this.f458l == 3) {
            m259a(true, true);
        }
        this.f431X = (int) this.f437a.descent();
        this.f432Y = (int) this.f437a.ascent();
    }

    private void m259a(boolean z, boolean z2) {
        this.f437a.setFakeBoldText(z);
        this.f437a.setTextSkewX(z2 ? -this.f433a : 0.0f);
    }

    private void m256a(Canvas canvas, int i, int i2, int i3) {
        int i4 = 0;
        if (!(this.f436a == null && this.f445b == null) && i3 > 0 && this.f409B == 1) {
            int i5;
            int i6;
            int i7;
            int i8 = this.f411D;
            int i9 = this.f412E;
            int i10 = i2 + this.f431X;
            if (this.f472z == 0) {
                if (i9 == 0) {
                    i9 = (this.f427T * m262c()) + ((this.f427T - 1) * this.f464r);
                }
                if (i8 != 0) {
                    i3 = i8;
                }
                i5 = i + this.f413F;
                i6 = (i + i3) - this.f415H;
                i7 = (i10 - i9) + this.f414G;
                i10 -= this.f416I;
            } else {
                if (this.f410C == 1) {
                    i6 = getCloudView().getNinePatchBorders().left;
                    i7 = getCloudView().getNinePatchBorders().top;
                    i10 = getCloudView().getNinePatchBorders().bottom;
                    i5 = i7;
                    i4 = i6;
                    i6 = getCloudView().getNinePatchBorders().right;
                } else {
                    i6 = 0;
                    i10 = 0;
                    i5 = 0;
                }
                if (i8 == 0) {
                    i7 = (getCloudView().getItemWidth() - i4) - i6;
                } else {
                    i7 = i8;
                }
                if (i9 == 0) {
                    i8 = (getCloudView().getItemHeight() - i5) - i10;
                } else {
                    i8 = i9;
                }
                int i11 = (i7 - this.f413F) - this.f415H;
                int i12 = (i8 - this.f414G) - this.f416I;
                if (this.f408A == 1) {
                    i8 = i4;
                    i9 = i5;
                } else if (this.f408A == 4) {
                    i9 = (getCloudView().getItemHeight() / 2) - (i12 / 2);
                    i8 = i4;
                } else if (this.f408A == 7) {
                    i9 = (getCloudView().getItemHeight() - i10) - i12;
                    i8 = i4;
                } else if (this.f408A == 3) {
                    i8 = (getCloudView().getItemWidth() - i6) - i11;
                    i9 = i5;
                } else if (this.f408A == 6) {
                    i8 = (getCloudView().getItemWidth() - i6) - i11;
                    i9 = (getCloudView().getItemHeight() / 2) - (i12 / 2);
                } else if (this.f408A == 9) {
                    i8 = (getCloudView().getItemWidth() - i6) - i11;
                    i9 = (getCloudView().getItemHeight() - i10) - i12;
                } else if (this.f408A == 2) {
                    i8 = (getCloudView().getItemWidth() / 2) - (i11 / 2);
                    i9 = i5;
                } else if (this.f408A == 8) {
                    i8 = (getCloudView().getItemWidth() / 2) - (i11 / 2);
                    i9 = (getCloudView().getItemHeight() - i10) - i12;
                } else {
                    i8 = (getCloudView().getItemWidth() / 2) - (i11 / 2);
                    i9 = (getCloudView().getItemHeight() / 2) - (i12 / 2);
                }
                i5 = (i8 + this.f417J) - this.f419L;
                i7 = (this.f418K + i9) - this.f420M;
                i6 = i5 + i11;
                i10 = i7 + i12;
            }
            Drawable drawable = this.f445b;
            Drawable drawable2 = this.f436a;
            if (!getCloudView().isFocused() || drawable == null) {
                drawable = drawable2;
            }
            drawImageBylimit(drawable, canvas, i5, i7, i6, i10, this.f410C);
        }
    }

    public String toString() {
        return "CuteTextViewData [mText=" + this.f439a + ", mWidth=" + this.f434a + ", mHeight=" + this.f444b + ", mVisible=" + this.f457k + AlbumEnterFactory.SIGN_STR;
    }

    public void suck(Cute cute) {
        super.suck(cute);
        CuteText cuteText = (CuteText) cute;
        this.f439a = cuteText.f439a;
        this.f446b = cuteText.f446b;
        this.f434a = cuteText.f434a;
        this.f444b = cuteText.f444b;
        this.f449c = cuteText.f449c;
        this.f450d = cuteText.f450d;
        this.f451e = cuteText.f451e;
        this.f452f = cuteText.f434a;
        this.f453g = cuteText.f453g;
        this.f454h = cuteText.f454h;
        this.f455i = cuteText.f455i;
        this.f456j = cuteText.f456j;
        this.f457k = cuteText.f457k;
        this.f458l = cuteText.f458l;
        this.f433a = cuteText.f433a;
        this.f459m = cuteText.f459m;
        this.f460n = cuteText.f460n;
        this.f461o = cuteText.f461o;
        this.f462p = cuteText.f462p;
        this.f463q = cuteText.f463q;
        this.f464r = cuteText.f464r;
        this.f465s = cuteText.f465s;
        this.f466t = cuteText.f466t;
        this.f443b = cuteText.f443b;
        this.f467u = cuteText.f467u;
        this.f468v = cuteText.f468v;
        this.f469w = cuteText.f469w;
        this.f470x = cuteText.f470x;
        this.f471y = cuteText.f471y;
        this.f448c = cuteText.f448c;
        clearDrawable(this.f436a);
        this.f436a = cuteText.f436a;
        clearDrawable(this.f445b);
        this.f445b = cuteText.f445b;
        this.f472z = cuteText.f472z;
        this.f408A = cuteText.f408A;
        this.f409B = cuteText.f409B;
        this.f410C = cuteText.f410C;
        this.f411D = cuteText.f411D;
        this.f412E = cuteText.f412E;
        this.f413F = cuteText.f413F;
        this.f414G = cuteText.f414G;
        this.f415H = cuteText.f415H;
        this.f416I = cuteText.f416I;
        this.f417J = cuteText.f417J;
        this.f418K = cuteText.f418K;
        this.f419L = cuteText.f419L;
        this.f420M = cuteText.f420M;
        this.f438a = cuteText.f438a;
        this.f440a = cuteText.f440a;
        this.f421N = cuteText.f421N;
        this.f422O = cuteText.f422O;
        this.f423P = cuteText.f423P;
        this.f435a = cuteText.f435a;
        this.f442a = cuteText.f442a;
        this.f441a = cuteText.f441a;
        this.f447b = cuteText.f447b;
        this.f425R = cuteText.f425R;
        this.f426S = cuteText.f426S;
        this.f427T = cuteText.f427T;
        this.f428U = cuteText.f428U;
        this.f429V = cuteText.f429V;
        this.f430W = cuteText.f430W;
        this.f431X = cuteText.f431X;
        this.f432Y = cuteText.f432Y;
    }
}
