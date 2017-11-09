package com.gala.cloudui.block;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import com.gala.cloudui.imp.ICuteText;
import com.gala.cloudui.utils.CloudUtilsGala;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class CuteText extends Cute implements ICuteText {
    private int A = 5;
    private int B = 1;
    private int C = 1;
    private int D;
    private int E;
    private int F;
    private int G;
    private int H;
    private int I;
    private int J;
    private int K;
    private int L;
    private int M;
    private int N = 1;
    private int O = 2;
    private int P = 3;
    private final int Q = 100;
    private int R;
    private int S;
    private int T;
    private int U;
    private int V;
    private int W;
    private int X;
    private int Y;
    private float a = 0.25f;
    private int f251a;
    private Canvas f252a;
    private Drawable f253a;
    private TextPaint f254a;
    private Runnable f255a;
    private String f256a;
    private boolean f257a;
    private int[] f258a = new int[3];
    private String[] f259a = new String[3];
    private float b = 1.0f;
    private int f260b;
    private Drawable f261b;
    private String f262b;
    private int[] f263b = new int[3];
    private float c;
    private int f264c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    private int j;
    private int k = 1;
    private int l = 0;
    private int m;
    private int n;
    private int o;
    private int p = 1;
    private int q = 0;
    private int r;
    private int s = 0;
    private int t = 0;
    private int u = 500;
    private int v;
    private int w;
    private int x;
    private int y;
    private int z = 0;

    public void setFocusFontColor(int focusColor) {
        if (this.o != focusColor) {
            this.o = focusColor;
            invalidate();
        }
    }

    public int getFontColor() {
        return this.n;
    }

    public void setFontColor(int normalColor) {
        if (this.n != normalColor) {
            this.n = normalColor;
            invalidate();
        }
    }

    public int getShadowLayerColor() {
        return this.y;
    }

    public void setShadowLayerColor(int shadowLayerColor) {
        if (this.y != shadowLayerColor) {
            this.y = shadowLayerColor;
            invalidate();
        }
    }

    public int getEllipsize() {
        return this.t;
    }

    public void setEllipsize(int ellipsize) {
        if (this.t != ellipsize) {
            this.t = ellipsize;
            this.W = 0;
            this.f257a = false;
            if (ellipsize == 1) {
                this.f257a = true;
                if (this.f255a == null) {
                    this.f255a = new Runnable(this) {
                        final /* synthetic */ CuteText a;

                        {
                            this.a = this$0;
                        }

                        public void run() {
                            this.a.f257a = false;
                            this.a.invalidate();
                        }
                    };
                }
                if (getCloudView() != null) {
                    getCloudView().postDelayed(this.f255a, (long) this.u);
                }
            } else if (getCloudView() != null) {
                getCloudView().removeCallbacks(this.f255a);
            }
            invalidate();
        }
    }

    public int getMarqueeDelay() {
        return this.u;
    }

    public void setMarqueeDelay(int marqueeDelay) {
        if (this.u != marqueeDelay) {
            this.u = marqueeDelay;
            invalidate();
        }
    }

    public int getMarqueeTextSpace() {
        return this.v;
    }

    public void setMarqueeTextSpace(int marqueeTextSpace) {
        if (this.v != marqueeTextSpace) {
            this.v = marqueeTextSpace;
            invalidate();
        }
    }

    public float getMarqueeSpeed() {
        return this.b;
    }

    public void setMarqueeSpeed(float marqueeSpeed) {
        if (this.b != marqueeSpeed) {
            this.b = marqueeSpeed;
            invalidate();
        }
    }

    public int getGravity() {
        return this.s;
    }

    public void setGravity(int gravity) {
        if (this.s != gravity) {
            this.s = gravity;
            invalidate();
        }
    }

    public int getFont() {
        return this.l;
    }

    public void setFont(int font) {
        if (this.l != font) {
            this.l = font;
            invalidate();
        }
    }

    public float getSkewX() {
        return this.a;
    }

    public void setSkewX(float skewX) {
        if (this.a != skewX) {
            this.a = skewX;
            invalidate();
        }
    }

    public int getLines() {
        return this.p;
    }

    public void setLines(int lines) {
        if (this.p != lines) {
            this.p = lines;
            invalidate();
        }
    }

    public int getLineSpace() {
        return this.r;
    }

    public void setLineSpace(int lineSpace) {
        if (this.r != lineSpace) {
            this.r = lineSpace;
            invalidate();
        }
    }

    public int getWidth() {
        return this.f251a;
    }

    public void setWidth(int width) {
        if (this.f251a != width) {
            this.f251a = width;
            invalidate();
        }
    }

    public int getHeight() {
        return this.f260b;
    }

    public void setHeight(int height) {
        if (this.f260b != height) {
            this.f260b = height;
            invalidate();
        }
    }

    public int getPaddingLeft() {
        return this.f264c;
    }

    public void setPaddingLeft(int paddingLeft) {
        if (this.f264c != paddingLeft) {
            this.f264c = paddingLeft;
            invalidate();
        }
    }

    public int getPaddingTop() {
        return this.d;
    }

    public void setPaddingTop(int paddingTop) {
        this.d = paddingTop;
        if (this.d != paddingTop) {
            this.d = paddingTop;
            invalidate();
        }
    }

    public int getPaddingBottom() {
        return this.f;
    }

    public void setPaddingBottom(int paddingBottom) {
        if (this.f != paddingBottom) {
            this.f = paddingBottom;
            invalidate();
        }
    }

    public int getPaddingRight() {
        return this.e;
    }

    public void setPaddingRight(int paddingRight) {
        if (this.e != paddingRight) {
            this.e = paddingRight;
            invalidate();
        }
    }

    public int getMarginTop() {
        return this.h;
    }

    public void setMarginTop(int marginTop) {
        if (this.h != marginTop) {
            this.h = marginTop;
            invalidate();
        }
    }

    public int getMarginBottom() {
        return this.j;
    }

    public void setMarginBottom(int marginBottom) {
        if (this.j != marginBottom) {
            this.j = marginBottom;
            invalidate();
        }
    }

    public int getMarginLeft() {
        return this.g;
    }

    public void setMarginLeft(int marginLeft) {
        if (this.g != marginLeft) {
            this.g = marginLeft;
            invalidate();
        }
    }

    public int getMarginRight() {
        return this.i;
    }

    public void setMarginRight(int marginRight) {
        if (this.i != marginRight) {
            this.i = marginRight;
            invalidate();
        }
    }

    public int getFontSize() {
        return this.m;
    }

    public void setFontSize(int size) {
        if (this.m != size) {
            this.m = size;
            invalidate();
        }
    }

    public float getShadowLayerRadius() {
        return this.c;
    }

    public void setShadowLayerRadius(float shadowLayerRadius) {
        if (this.c != shadowLayerRadius) {
            this.c = shadowLayerRadius;
            invalidate();
        }
    }

    public int getShadowLayerDx() {
        return this.w;
    }

    public void setShadowLayerDx(int shadowLayerDx) {
        if (this.w != shadowLayerDx) {
            this.w = shadowLayerDx;
            invalidate();
        }
    }

    public int getShadowLayerDy() {
        return this.x;
    }

    public void setShadowLayerDy(int shadowLayerDy) {
        if (this.x != shadowLayerDy) {
            this.x = shadowLayerDy;
            invalidate();
        }
    }

    public String getText() {
        return this.f256a;
    }

    public void setText(String text) {
        if (!TextUtils.equals(text, this.f256a)) {
            this.f256a = text;
            invalidate();
        }
    }

    public String getDefaultText() {
        return this.f262b;
    }

    public void setDefaultText(String text) {
        this.f262b = text;
    }

    public int getVisible() {
        return this.k;
    }

    public void setVisible(int visible) {
        if (this.k != visible) {
            this.k = visible;
            invalidate();
        }
    }

    public int getTitleType() {
        return this.q;
    }

    public void setTitleType(int type) {
        this.q = type;
    }

    public Drawable getBgDrawable() {
        return this.f253a;
    }

    public void setBgDrawable(Drawable bgDrawable) {
        if (this.f253a != bgDrawable) {
            this.f253a = bgDrawable;
            invalidate();
        }
    }

    public Drawable getBgFocusDrawable() {
        return this.f261b;
    }

    public void setBgFocusDrawable(Drawable bgDrawable) {
        if (this.f261b != bgDrawable) {
            this.f261b = bgDrawable;
            invalidate();
        }
    }

    public int getBgScaleType() {
        return this.z;
    }

    public void setBgScaleType(int bgScaleType) {
        if (this.z != bgScaleType) {
            this.z = bgScaleType;
            invalidate();
        }
    }

    public int getFocusFontColor() {
        return this.o;
    }

    public int getBgGravity() {
        return this.A;
    }

    public void setBgGravity(int bgGravity) {
        if (this.A != bgGravity) {
            this.A = bgGravity;
            invalidate();
        }
    }

    public int getBgHeight() {
        return this.E;
    }

    public void setBgHeight(int bgHeight) {
        if (this.E != bgHeight) {
            this.E = bgHeight;
            invalidate();
        }
    }

    public int getBgWidth() {
        return this.D;
    }

    public void setBgWidth(int bgWidth) {
        if (this.D != bgWidth) {
            this.D = bgWidth;
            invalidate();
        }
    }

    public int getBgPaddingLeft() {
        return this.F;
    }

    public void setBgPaddingLeft(int bgPaddingLeft) {
        if (this.F != bgPaddingLeft) {
            this.F = bgPaddingLeft;
            invalidate();
        }
    }

    public int getBgPaddingTop() {
        return this.G;
    }

    public void setBgPaddingTop(int bgPaddingTop) {
        if (this.G != bgPaddingTop) {
            this.G = bgPaddingTop;
            invalidate();
        }
    }

    public int getBgPaddingRight() {
        return this.H;
    }

    public void setBgPaddingRight(int bgPaddingRight) {
        if (this.H != bgPaddingRight) {
            this.H = bgPaddingRight;
            invalidate();
        }
    }

    public int getBgPaddingBottom() {
        return this.I;
    }

    public void setBgPaddingBottom(int bgPaddingBottom) {
        if (this.I != bgPaddingBottom) {
            this.I = bgPaddingBottom;
            invalidate();
        }
    }

    public int getBgMarginLeft() {
        return this.J;
    }

    public void setBgMarginLeft(int bgMarginLeft) {
        if (this.J != bgMarginLeft) {
            this.J = bgMarginLeft;
            invalidate();
        }
    }

    public int getBgMarginTop() {
        return this.K;
    }

    public void setBgMarginTop(int bgMarginTop) {
        if (this.K != bgMarginTop) {
            this.K = bgMarginTop;
            invalidate();
        }
    }

    public int getBgMarginRight() {
        return this.L;
    }

    public void setBgMarginRight(int bgMarginRight) {
        if (this.L != bgMarginRight) {
            this.L = bgMarginRight;
            invalidate();
        }
    }

    public int getBgMarginBottom() {
        return this.M;
    }

    public void setBgMarginBottom(int bgMarginBottom) {
        if (this.M != bgMarginBottom) {
            this.M = bgMarginBottom;
            invalidate();
        }
    }

    public int getBgClipPadding() {
        return this.C;
    }

    public void setBgClipPadding(int clipPadding) {
        if (this.C != clipPadding) {
            this.C = clipPadding;
            invalidate();
        }
    }

    public int getBgVisible() {
        return this.B;
    }

    public void setBgVisible(int bgVisible) {
        if (this.B != bgVisible) {
            this.B = bgVisible;
            invalidate();
        }
    }

    public int getRealLineCount() {
        return getRealLineCount(this.f256a);
    }

    public int getRealLineCount(String str) {
        a(str);
        return this.T;
    }

    private int a() {
        int i = this.f251a;
        if (this.s == 4 || this.s == 5 || i <= 0 || (i > getCloudView().getContentWidth() && getCloudView().getContentWidth() > 0)) {
            i = getCloudView().getContentWidth();
        }
        i = (i - this.f264c) - this.e;
        a();
        float textSkewX = this.f254a.getTextSkewX();
        if (textSkewX != 0.0f) {
            return (int) (((float) i) + (textSkewX * ((float) this.m)));
        }
        return i;
    }

    public TextPaint getPaint() {
        a();
        return this.f254a;
    }

    private synchronized void a(String str) {
        String str2 = this.f256a;
        int i = this.k;
        int i2 = this.B;
        Drawable drawable = this.f253a;
        Canvas canvas = this.f252a;
        setText(str);
        setVisible(1);
        setBgVisible(1);
        this.f252a = null;
        setBgDrawable(getCloudView().getBackground());
        draw(null);
        setText(str2);
        setVisible(i);
        setBgVisible(i2);
        this.f252a = canvas;
        setBgDrawable(drawable);
    }

    public int calcLineCount(int maxWidth) {
        int i = 6;
        int length = this.f256a.length();
        if (length <= 6) {
            i = length;
        }
        length = i;
        i = 0;
        while (length <= this.f256a.length() && length < 100) {
            int measureText = (int) this.f254a.measureText(this.f256a, 0, length);
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
            this.S = measureText;
        }
        return length - 1;
    }

    public void draw(Canvas canvas) {
        if (!getCloudView().isFocused()) {
            getCloudView().removeCallbacks(this.f255a);
        }
        this.f252a = canvas;
        this.T = 0;
        this.S = 0;
        this.R = 0;
        this.f259a[0] = "";
        this.f259a[1] = "";
        this.f259a[2] = "";
        this.f258a[0] = 0;
        this.f258a[1] = 0;
        this.f258a[2] = 0;
        this.f263b[0] = 0;
        this.f263b[1] = 0;
        this.f263b[2] = 0;
        if (!TextUtils.isEmpty(this.f256a) && this.p > 0 && this.k == 1) {
            int a = a();
            if (a > 0 && a <= 2048) {
                int contentHeight = getCloudView().getContentHeight() + getCloudView().getNinePatchBorders().top;
                if (this.p == 1 || this.t == 1 || this.s == 5) {
                    this.T = 1;
                    this.S = (int) this.f254a.measureText(this.f256a);
                    if (this.S <= a) {
                        a(canvas, this.f256a, contentHeight, false, this.N, a);
                        return;
                    }
                    a(canvas, this.f256a, contentHeight, true, this.N, a);
                    return;
                }
                int c = c();
                int calcLineCount;
                String substring;
                String substring2;
                if (this.p == 2) {
                    if (this.t != 0) {
                        setEllipsize(0);
                    }
                    calcLineCount = calcLineCount(a);
                    substring = this.f256a.substring(0, calcLineCount);
                    if (calcLineCount == this.f256a.length()) {
                        this.T = 1;
                        a(canvas, substring, contentHeight, false, this.N, a);
                        return;
                    }
                    substring2 = this.f256a.substring(calcLineCount, this.f256a.length());
                    this.T = 2;
                    a(canvas, substring, (contentHeight - c) - this.r, false, this.N, a);
                    a(canvas, substring2, contentHeight, true, this.O, a);
                    return;
                }
                if (this.t != 0) {
                    setEllipsize(0);
                }
                calcLineCount = calcLineCount(a);
                substring = this.f256a.substring(0, calcLineCount);
                if (calcLineCount == this.f256a.length()) {
                    substring2 = "";
                } else {
                    substring2 = this.f256a.substring(calcLineCount, this.f256a.length());
                }
                calcLineCount = 1;
                while (calcLineCount <= substring2.length() && calcLineCount < 100 && this.f254a.measureText(substring2, 0, calcLineCount) < ((float) a)) {
                    calcLineCount++;
                }
                calcLineCount--;
                if (substring2.length() > calcLineCount) {
                    String substring3 = substring2.substring(calcLineCount, substring2.length());
                    String substring4 = substring2.substring(0, calcLineCount);
                    this.T = 3;
                    a(canvas, substring, (contentHeight - (c * 2)) - (this.r * 2), false, this.N, a);
                    a(canvas, substring4, (contentHeight - c) - this.r, false, this.O, a);
                    a(canvas, substring3, contentHeight, true, this.P, a);
                } else if (TextUtils.isEmpty(substring2)) {
                    this.T = 1;
                    a(canvas, substring, contentHeight, false, this.N, a);
                } else {
                    this.T = 2;
                    a(canvas, substring, (contentHeight - c) - this.r, false, this.N, a);
                    a(canvas, substring2, contentHeight, false, this.O, a);
                }
            }
        }
    }

    private void a(Canvas canvas, String str, int i, boolean z, int i2, int i3) {
        if (this.t == 1) {
            z = false;
        }
        String str2 = z ? (String) TextUtils.ellipsize(str, this.f254a, (float) i3, CloudUtilsGala.getTruncateAt(this.t)) : str;
        if (this.p == 1 && z) {
            this.S = (int) this.f254a.measureText(str2);
        }
        int i4 = this.S;
        if (i2 != this.N && this.z == 0) {
            i4 = (int) this.f254a.measureText(str2);
            if (i4 > this.S) {
                this.S = i4;
            }
        }
        int b = b();
        if (this.s == 3 || this.s == 2) {
            i4 = ((i3 - i4) / 2) + b;
        } else if (this.s == 4) {
            i4 = ((getCloudView().getContentWidth() - i4) / 2) + b;
        } else if (this.s == 5) {
            i4 = (((b + getCloudView().getContentWidth()) - i4) - this.i) + this.g;
        } else {
            i4 = b;
        }
        int i5 = ((this.h + i) - this.j) - this.X;
        this.R = i5;
        b = this.f260b;
        if (this.s == 4 || this.s == 1 || this.s == 3) {
            if (b == 0 || this.s == 4) {
                b = getCloudView().getContentHeight();
            }
            int c = c();
            if (this.T == 1) {
                i5 -= (b - c) / 2;
                this.R = i5;
            } else if (this.T == 2) {
                if (i2 == this.N) {
                    i5 = (i5 - ((b - (c * 2)) - this.r)) + (((b - (c * 2)) - this.r) / 2);
                }
                if (i2 == this.O) {
                    i5 = (i5 - ((b - (c * 2)) - this.r)) + (((b - (c * 2)) - this.r) / 2);
                    this.R = i5;
                }
            } else if (this.T == 3) {
                if (i2 == this.N) {
                    i5 = (i5 - ((b - (c * 3)) - (this.r * 2))) + (((b - (c * 3)) - (this.r * 2)) / 2);
                }
                if (i2 == this.O) {
                    i5 = (i5 - ((b - (c * 2)) - this.r)) + ((b - c) / 2);
                }
                if (i2 == this.P) {
                    i5 = this.r + (((b / 2) + (i5 - (b - c))) + (c / 2));
                    this.R = i5;
                }
            }
        }
        this.f259a[i2 - 1] = str2;
        this.f258a[i2 - 1] = i5;
        this.f263b[i2 - 1] = i4;
        if (i2 == this.T) {
            a(canvas, i3);
        }
    }

    private void a(Canvas canvas, int i) {
        int i2 = this.f263b[0];
        a(canvas, i2, this.R, this.S);
        if (canvas != null) {
            int b = b();
            int i3 = this.X + this.R;
            int c = i3 - ((this.T * c()) + ((this.T - 1) * this.r));
            int a = a(b, i);
            for (int i4 = 0; i4 < this.T; i4++) {
                if (this.t == 1 && !this.f257a) {
                    if (this.W == 0) {
                        a(a);
                    }
                    if (this.V > this.W) {
                        canvas.save();
                        canvas.clipRect(b, c, a, i3);
                        canvas.drawText(this.f256a, (float) (i2 - this.U), (float) this.f258a[i4], this.f254a);
                        canvas.restore();
                        canvas.save();
                        canvas.clipRect(b, c, a, i3);
                        canvas.drawText(this.f256a, (float) (((i2 + this.V) + this.v) - this.U), (float) this.f258a[i4], this.f254a);
                        canvas.restore();
                        this.U = (int) (((float) this.U) + this.b);
                        if (this.U >= this.V + this.v) {
                            this.U = 0;
                        }
                        invalidate();
                        return;
                    }
                }
                canvas.save();
                canvas.clipRect(b, c, a, i3);
                canvas.drawText(this.f259a[i4], (float) this.f263b[i4], (float) this.f258a[i4], this.f254a);
                canvas.restore();
            }
        }
    }

    private void a(int i) {
        this.U = 0;
        this.V = d();
        this.W = i - b();
        if (this.v == 0) {
            setMarqueeTextSpace(this.m * 2);
        }
    }

    private int b() {
        int paddingLeft = ((getCloudView().getNinePatchBorders().left + getCloudView().getPaddingLeft()) + this.g) + this.f264c;
        float textSkewX = this.f254a.getTextSkewX();
        if (textSkewX > 0.0f) {
            return (int) (((float) paddingLeft) - (textSkewX * ((float) this.m)));
        }
        return paddingLeft;
    }

    private int a(int i, int i2) {
        int i3 = i + i2;
        int paddingLeft = (getCloudView().getNinePatchBorders().left + getCloudView().getPaddingLeft()) + getCloudView().getContentWidth();
        if (i3 <= paddingLeft) {
            paddingLeft = i3;
        }
        float textSkewX = this.f254a.getTextSkewX();
        if (textSkewX < 0.0f) {
            return (int) (((float) paddingLeft) - (textSkewX * ((float) this.m)));
        }
        return paddingLeft;
    }

    private int c() {
        return this.X - this.Y;
    }

    private int d() {
        return (int) this.f254a.measureText(this.f256a);
    }

    private void m67a() {
        if (this.f254a == null) {
            this.f254a = new TextPaint();
            this.f254a.setAntiAlias(true);
        }
        this.f254a.setShadowLayer(this.c, (float) this.w, (float) this.x, this.y);
        int i = this.o;
        int i2 = this.n;
        if (!getCloudView().isFocused() || i == 0) {
            i = i2;
        }
        this.f254a.setColor(i);
        this.f254a.setTextSize((float) this.m);
        if (CloudUtilsGala.getTypeface() != null) {
            this.f254a.setTypeface(CloudUtilsGala.getTypeface());
        }
        if (this.l == 0) {
            a(false, false);
        } else if (this.l == 1) {
            a(true, false);
        } else if (this.l == 2) {
            a(false, true);
        } else if (this.l == 3) {
            a(true, true);
        }
        this.X = (int) this.f254a.descent();
        this.Y = (int) this.f254a.ascent();
    }

    private void a(boolean z, boolean z2) {
        this.f254a.setFakeBoldText(z);
        this.f254a.setTextSkewX(z2 ? -this.a : 0.0f);
    }

    private void a(Canvas canvas, int i, int i2, int i3) {
        int i4 = 0;
        if (!(this.f253a == null && this.f261b == null) && i3 > 0 && this.B == 1) {
            int i5;
            int i6;
            int i7;
            int i8 = this.D;
            int i9 = this.E;
            int i10 = i2 + this.X;
            if (this.z == 0) {
                if (i9 == 0) {
                    i9 = (this.T * c()) + ((this.T - 1) * this.r);
                }
                if (i8 != 0) {
                    i3 = i8;
                }
                i5 = i + this.F;
                i6 = (i + i3) - this.H;
                i7 = (i10 - i9) + this.G;
                i10 -= this.I;
            } else {
                if (this.C == 1) {
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
                int i11 = (i7 - this.F) - this.H;
                int i12 = (i8 - this.G) - this.I;
                if (this.A == 1) {
                    i8 = i4;
                    i9 = i5;
                } else if (this.A == 4) {
                    i9 = (getCloudView().getItemHeight() / 2) - (i12 / 2);
                    i8 = i4;
                } else if (this.A == 7) {
                    i9 = (getCloudView().getItemHeight() - i10) - i12;
                    i8 = i4;
                } else if (this.A == 3) {
                    i8 = (getCloudView().getItemWidth() - i6) - i11;
                    i9 = i5;
                } else if (this.A == 6) {
                    i8 = (getCloudView().getItemWidth() - i6) - i11;
                    i9 = (getCloudView().getItemHeight() / 2) - (i12 / 2);
                } else if (this.A == 9) {
                    i8 = (getCloudView().getItemWidth() - i6) - i11;
                    i9 = (getCloudView().getItemHeight() - i10) - i12;
                } else if (this.A == 2) {
                    i8 = (getCloudView().getItemWidth() / 2) - (i11 / 2);
                    i9 = i5;
                } else if (this.A == 8) {
                    i8 = (getCloudView().getItemWidth() / 2) - (i11 / 2);
                    i9 = (getCloudView().getItemHeight() - i10) - i12;
                } else {
                    i8 = (getCloudView().getItemWidth() / 2) - (i11 / 2);
                    i9 = (getCloudView().getItemHeight() / 2) - (i12 / 2);
                }
                i5 = (i8 + this.J) - this.L;
                i7 = (this.K + i9) - this.M;
                i6 = i5 + i11;
                i10 = i7 + i12;
            }
            Drawable drawable = this.f261b;
            Drawable drawable2 = this.f253a;
            if (!getCloudView().isFocused() || drawable == null) {
                drawable = drawable2;
            }
            drawImageBylimit(drawable, canvas, i5, i7, i6, i10, this.C);
        }
    }

    public String toString() {
        return "CuteTextViewData [mText=" + this.f256a + ", mWidth=" + this.f251a + ", mHeight=" + this.f260b + ", mVisible=" + this.k + AlbumEnterFactory.SIGN_STR;
    }

    public void suck(Cute cute) {
        super.suck(cute);
        CuteText cuteText = (CuteText) cute;
        this.f256a = cuteText.f256a;
        this.f262b = cuteText.f262b;
        this.f251a = cuteText.f251a;
        this.f260b = cuteText.f260b;
        this.f264c = cuteText.f264c;
        this.d = cuteText.d;
        this.e = cuteText.e;
        this.f = cuteText.f251a;
        this.g = cuteText.g;
        this.h = cuteText.h;
        this.i = cuteText.i;
        this.j = cuteText.j;
        this.k = cuteText.k;
        this.l = cuteText.l;
        this.a = cuteText.a;
        this.m = cuteText.m;
        this.n = cuteText.n;
        this.o = cuteText.o;
        this.p = cuteText.p;
        this.q = cuteText.q;
        this.r = cuteText.r;
        this.s = cuteText.s;
        this.t = cuteText.t;
        this.b = cuteText.b;
        this.u = cuteText.u;
        this.v = cuteText.v;
        this.w = cuteText.w;
        this.x = cuteText.x;
        this.y = cuteText.y;
        this.c = cuteText.c;
        clearDrawable(this.f253a);
        this.f253a = cuteText.f253a;
        clearDrawable(this.f261b);
        this.f261b = cuteText.f261b;
        this.z = cuteText.z;
        this.A = cuteText.A;
        this.B = cuteText.B;
        this.C = cuteText.C;
        this.D = cuteText.D;
        this.E = cuteText.E;
        this.F = cuteText.F;
        this.G = cuteText.G;
        this.H = cuteText.H;
        this.I = cuteText.I;
        this.J = cuteText.J;
        this.K = cuteText.K;
        this.L = cuteText.L;
        this.M = cuteText.M;
        this.f255a = cuteText.f255a;
        this.f257a = cuteText.f257a;
        this.N = cuteText.N;
        this.O = cuteText.O;
        this.P = cuteText.P;
        this.f252a = cuteText.f252a;
        this.f259a = cuteText.f259a;
        this.f258a = cuteText.f258a;
        this.f263b = cuteText.f263b;
        this.R = cuteText.R;
        this.S = cuteText.S;
        this.T = cuteText.T;
        this.U = cuteText.U;
        this.V = cuteText.V;
        this.W = cuteText.W;
        this.X = cuteText.X;
        this.Y = cuteText.Y;
    }
}
