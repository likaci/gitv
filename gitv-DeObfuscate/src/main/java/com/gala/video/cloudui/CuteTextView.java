package com.gala.video.cloudui;

import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import com.gala.video.app.player.feature.PluginStateChangedListener;
import com.gala.video.cloudui.view.impl.IText;
import java.io.Serializable;

public class CuteTextView extends CuteView implements IText, Serializable {
    private float f1984a;
    private int f1985a = 1;
    private transient Canvas f1986a;
    private transient TextPaint f1987a;
    private final transient Runnable f1988a = new C15851(this);
    private boolean f1989a;
    private int[] f1990a = new int[3];
    private String[] f1991a = new String[3];
    public int alphaPercentage = 100;
    public boolean antiAlias = true;
    private float f1992b;
    private int f1993b = 2;
    private int[] f1994b = new int[3];
    public boolean bgClipCanvas = true;
    public Drawable bgDrawable;
    public int bgHeight;
    public int bgPaddingBottom;
    public int bgPaddingLeft;
    public int bgPaddingRight;
    public int bgPaddingTop;
    public ScaleType4CuteTextBg bgScaleType = ScaleType4CuteTextBg.FIT_XY;
    public int bgVisible = 0;
    public int bgWidth;
    private float f1995c;
    private int f1996c = 3;
    private float f1997d;
    private int f1998d;
    private float f1999e;
    private int f2000e;
    public TruncateAt ellipsize = TruncateAt.END;
    private int f2001f;
    public int focusColor;
    public String font;
    private int f2002g;
    public Gravity4CuteText gravity = Gravity4CuteText.CENTER_NONE;
    private int f2003h;
    public int height;
    private final int f2004i = 100;
    public int lineSpace;
    public int lines = 1;
    public int marginBottom;
    public int marginLeft;
    public int marginRight;
    public int marginTop;
    public int marqueeDelay = 500;
    public float marqueeSpeed = 1.0f;
    public int marqueeTextSpace;
    public int normalColor;
    public int paddingLeft;
    public int paddingRight;
    public float scaleX = 1.0f;
    public int shadowLayerColor;
    public float shadowLayerDx;
    public float shadowLayerDy;
    public float shadowLayerRadius;
    public int size;
    public float skewX = 0.25f;
    public String text;
    public int visible = 0;
    public int width;

    class C15851 implements Runnable {
        final /* synthetic */ CuteTextView f1983a;

        C15851(CuteTextView this$0) {
            this.f1983a = this$0;
        }

        public void run() {
            this.f1983a.f1989a = false;
            this.f1983a.invalidate();
        }
    }

    public int getFocusColor() {
        return this.focusColor;
    }

    public void setFocusColor(int focusColor) {
        if (this.focusColor != focusColor) {
            this.focusColor = focusColor;
            invalidate();
        }
    }

    public int getNormalColor() {
        return this.normalColor;
    }

    public void setNormalColor(int normalColor) {
        if (this.normalColor != normalColor) {
            this.normalColor = normalColor;
            invalidate();
        }
    }

    public int getShadowLayerColor() {
        return this.shadowLayerColor;
    }

    public void setShadowLayerColor(int shadowLayerColor) {
        if (this.shadowLayerColor != shadowLayerColor) {
            this.shadowLayerColor = shadowLayerColor;
            invalidate();
        }
    }

    public TruncateAt getEllipsize() {
        return this.ellipsize;
    }

    public void setEllipsize(TruncateAt ellipsize) {
        if (this.ellipsize != ellipsize) {
            this.ellipsize = ellipsize;
            this.f1995c = 0.0f;
            this.f1989a = false;
            if (ellipsize == TruncateAt.MARQUEE) {
                this.f1989a = true;
                getHandler().postDelayed(this.f1988a, (long) this.marqueeDelay);
            } else {
                getHandler().removeCallbacks(this.f1988a);
            }
            invalidate();
        }
    }

    public int getMarqueeDelay() {
        return this.marqueeDelay;
    }

    public void setMarqueeDelay(int marqueeDelay) {
        if (this.marqueeDelay != marqueeDelay) {
            this.marqueeDelay = marqueeDelay;
            invalidate();
        }
    }

    public int getMarqueeTextSpace() {
        return this.marqueeTextSpace;
    }

    public void setMarqueeTextSpace(int marqueeTextSpace) {
        if (this.marqueeTextSpace != marqueeTextSpace) {
            this.marqueeTextSpace = marqueeTextSpace;
            invalidate();
        }
    }

    public float getMarqueeSpeed() {
        return this.marqueeSpeed;
    }

    public void setMarqueeSpeed(float marqueeSpeed) {
        if (this.marqueeSpeed != marqueeSpeed) {
            this.marqueeSpeed = marqueeSpeed;
            invalidate();
        }
    }

    public Gravity4CuteText getGravity() {
        return this.gravity;
    }

    public void setGravity(Gravity4CuteText gravity) {
        if (this.gravity != gravity) {
            this.gravity = gravity;
            invalidate();
        }
    }

    public String getFont() {
        return this.font;
    }

    public void setFont(String font) {
        if (this.font == null || !this.font.equals(font)) {
            this.font = font;
            invalidate();
        }
    }

    public float getSkewX() {
        return this.skewX;
    }

    public void setSkewX(float skewX) {
        if (this.skewX != skewX) {
            this.skewX = skewX;
            invalidate();
        }
    }

    public boolean isAntiAlias() {
        return this.antiAlias;
    }

    public void setAntiAlias(boolean antiAlias) {
        if (this.antiAlias != antiAlias) {
            this.antiAlias = antiAlias;
            invalidate();
        }
    }

    public int getAlphaPercentage() {
        return this.alphaPercentage;
    }

    public void setAlphaPercentage(int alphaPercentage) {
        if (this.alphaPercentage != alphaPercentage) {
            this.alphaPercentage = alphaPercentage;
            invalidate();
        }
    }

    public int getLines() {
        return this.lines;
    }

    public void setLines(int lines) {
        if (this.lines != lines) {
            this.lines = lines;
            invalidate();
        }
    }

    public int getLineSpace() {
        return this.lineSpace;
    }

    public void setLineSpace(int lineSpace) {
        if (this.lineSpace != lineSpace) {
            this.lineSpace = lineSpace;
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

    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        if (this.paddingLeft != paddingLeft) {
            this.paddingLeft = paddingLeft;
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

    public int getMarginTop() {
        return this.marginTop;
    }

    public void setMarginTop(int marginTop) {
        if (this.marginTop != marginTop) {
            this.marginTop = marginTop;
            invalidate();
        }
    }

    public int getMarginBottom() {
        return this.marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        if (this.marginBottom != marginBottom) {
            this.marginBottom = marginBottom;
            invalidate();
        }
    }

    public int getMarginLeft() {
        return this.marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        if (this.marginLeft != marginLeft) {
            this.marginLeft = marginLeft;
            invalidate();
        }
    }

    public int getMarginRight() {
        return this.marginRight;
    }

    public void setMarginRight(int marginRight) {
        if (this.marginRight != marginRight) {
            this.marginRight = marginRight;
            invalidate();
        }
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        if (this.size != size) {
            this.size = size;
            invalidate();
        }
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public void setScaleX(float scaleX) {
        if (this.scaleX != scaleX) {
            this.scaleX = scaleX;
            invalidate();
        }
    }

    public float getShadowLayerRadius() {
        return this.shadowLayerRadius;
    }

    public void setShadowLayerRadius(float shadowLayerRadius) {
        if (this.shadowLayerRadius != shadowLayerRadius) {
            this.shadowLayerRadius = shadowLayerRadius;
            invalidate();
        }
    }

    public float getShadowLayerDx() {
        return this.shadowLayerDx;
    }

    public void setShadowLayerDx(float shadowLayerDx) {
        if (this.shadowLayerDx != shadowLayerDx) {
            this.shadowLayerDx = shadowLayerDx;
            invalidate();
        }
    }

    public float getShadowLayerDy() {
        return this.shadowLayerDy;
    }

    public void setShadowLayerDy(float shadowLayerDy) {
        if (this.shadowLayerDy != shadowLayerDy) {
            this.shadowLayerDy = shadowLayerDy;
            invalidate();
        }
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        if (this.text == null) {
            if (text == null) {
                return;
            }
        } else if (this.text.equals(text)) {
            return;
        }
        this.text = text;
        invalidate();
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

    public Drawable getBgDrawable() {
        return this.bgDrawable;
    }

    public void setBgDrawable(Drawable bgDrawable) {
        if (this.bgDrawable != bgDrawable) {
            this.bgDrawable = bgDrawable;
            invalidate();
        }
    }

    public ScaleType4CuteTextBg getBgScaleType() {
        return this.bgScaleType;
    }

    public void setBgScaleType(ScaleType4CuteTextBg bgScaleType) {
        if (this.bgScaleType != bgScaleType) {
            this.bgScaleType = bgScaleType;
            invalidate();
        }
    }

    public int getBgHeight() {
        return this.bgHeight;
    }

    public void setBgHeight(int bgHeight) {
        if (this.bgHeight != bgHeight) {
            this.bgHeight = bgHeight;
            invalidate();
        }
    }

    public int getBgWidth() {
        return this.bgWidth;
    }

    public void setBgWidth(int bgWidth) {
        if (this.bgWidth != bgWidth) {
            this.bgWidth = bgWidth;
            invalidate();
        }
    }

    public int getBgPaddingLeft() {
        return this.bgPaddingLeft;
    }

    public void setBgPaddingLeft(int bgPaddingLeft) {
        if (this.bgPaddingLeft != bgPaddingLeft) {
            this.bgPaddingLeft = bgPaddingLeft;
            invalidate();
        }
    }

    public int getBgPaddingTop() {
        return this.bgPaddingTop;
    }

    public void setBgPaddingTop(int bgPaddingTop) {
        if (this.bgPaddingTop != bgPaddingTop) {
            this.bgPaddingTop = bgPaddingTop;
            invalidate();
        }
    }

    public int getBgPaddingRight() {
        return this.bgPaddingRight;
    }

    public void setBgPaddingRight(int bgPaddingRight) {
        if (this.bgPaddingRight != bgPaddingRight) {
            this.bgPaddingRight = bgPaddingRight;
            invalidate();
        }
    }

    public int getBgPaddingBottom() {
        return this.bgPaddingBottom;
    }

    public void setBgPaddingBottom(int bgPaddingBottom) {
        if (this.bgPaddingBottom != bgPaddingBottom) {
            this.bgPaddingBottom = bgPaddingBottom;
            invalidate();
        }
    }

    public boolean isBgClipCanvas() {
        return this.bgClipCanvas;
    }

    public void setBgClipCanvas(boolean clipCanvas) {
        if (this.bgClipCanvas != clipCanvas) {
            this.bgClipCanvas = clipCanvas;
            invalidate();
        }
    }

    public int getBgVisible() {
        return this.bgVisible;
    }

    public void setBgVisible(int bgVisible) {
        if (this.bgVisible != bgVisible) {
            this.bgVisible = bgVisible;
            invalidate();
        }
    }

    public int getRealLineCount() {
        return getRealLineCount(this.text);
    }

    public int getRealLineCount(String str) {
        m1563a(str);
        return this.f2001f;
    }

    public int getRealBgWidth() {
        return getRealBgWidth(this.text);
    }

    public int getRealBgWidth(String str) {
        m1563a(str);
        return this.f2002g;
    }

    public int getRealBgHeight() {
        m1563a(this.text);
        return this.f2003h;
    }

    public int getRealBgHeight(String str) {
        m1563a(str);
        return this.f2003h;
    }

    public int getLastLineWidth() {
        return getLastLineWidth(this.text);
    }

    public int getLastLineWidth(String str) {
        int realLineCount = getRealLineCount(str);
        if (realLineCount <= 0 || this.f1991a.length < this.f2001f - 1) {
            Log.e("q/cloud/CuteTextView", "fatal-error lastLineWidth--mRealLineCount" + this.f2001f + "---mStringList.size=" + this.f1991a.length + "---realLineCount=" + realLineCount + "---text=" + this.text);
            return 0;
        }
        String str2 = this.f1991a[this.f2001f - 1];
        if (str2 != null) {
            return (int) getPaint().measureText(str2);
        }
        Log.e("q/cloud/CuteTextView", "fatal-error lastLineWidth--text=" + this.text + "---mStringList.get(mRealLineCount - 1)=" + str2);
        return 0;
    }

    public int getFirstLineWidth() {
        return getFirstLineWidth(this.text);
    }

    public int getFirstLineWidth(String str) {
        int realLineCount = getRealLineCount(str);
        if (realLineCount <= 0 || this.f1991a.length == 0) {
            Log.e("q/cloud/CuteTextView", "fatal-error lastLineWidth--mRealLineCount" + this.f2001f + "---realLineCount=" + realLineCount + "---text=" + this.text);
            return 0;
        }
        String str2 = this.f1991a[0];
        if (str2 != null) {
            return (int) getPaint().measureText(str2);
        }
        Log.e("q/cloud/CuteTextView", "fatal-error lastLineWidth--text=" + this.text + "---mStringList.get(mRealLineCount - 1)=" + str2);
        return 0;
    }

    public int getLineWidth(int lineIndex) {
        return (int) getPaint().measureText(getText(lineIndex));
    }

    public int getLineWidth(String str, int lineIndex) {
        return (int) getPaint().measureText(getText(str, lineIndex));
    }

    public int getMaxWidth() {
        int i = this.width;
        if (this.gravity == Gravity4CuteText.CENTER_IN_PARENT || this.gravity == Gravity4CuteText.ALIGN_PARENT_RIGHT || i <= 0 || (i > getInfoModel().contentWidth && getInfoModel().contentWidth > 0)) {
            i = getInfoModel().contentWidth;
        }
        i = (i - this.paddingLeft) - this.paddingRight;
        m1555a();
        float textSkewX = this.f1987a.getTextSkewX();
        if (textSkewX != 0.0f) {
            return (int) (((float) i) + (textSkewX * ((float) this.size)));
        }
        return i;
    }

    public int getMaxWidth(int itemWidth) {
        int i = this.width;
        if (this.gravity == Gravity4CuteText.CENTER_IN_PARENT || this.gravity == Gravity4CuteText.ALIGN_PARENT_RIGHT) {
            i = getInfoModel().contentWidth;
        }
        int i2 = itemWidth - (getInfoModel().ninePatchBorder * 2);
        if (i <= 0 || (i > i2 && i2 > 0)) {
            i = i2;
        }
        i = (i - this.paddingLeft) - this.paddingRight;
        m1555a();
        float textSkewX = this.f1987a.getTextSkewX();
        if (textSkewX != 0.0f) {
            return (int) (((float) i) + (textSkewX * ((float) this.size)));
        }
        return i;
    }

    public String subText(int width) {
        if (width <= 0 || TextUtils.isEmpty(this.text)) {
            return "";
        }
        return (String) TextUtils.ellipsize(this.text, getPaint(), (float) width, this.ellipsize);
    }

    public String subText(String text, int width) {
        if (width <= 0 || TextUtils.isEmpty(text)) {
            return "";
        }
        return (String) TextUtils.ellipsize(text, getPaint(), (float) width, TruncateAt.END);
    }

    public String getLastLineText() {
        return getLastLineText(this.text);
    }

    public String getLastLineText(String str) {
        int realLineCount = getRealLineCount(str);
        if (realLineCount <= 0 || this.f1991a.length < this.f2001f - 1) {
            Log.e("q/cloud/CuteTextView", "fatal-error lastLineWidth--mRealLineCount" + this.f2001f + "---mStringList.size=" + this.f1991a.length + "---realLineCount=" + realLineCount + "---text=" + this.text);
            return "";
        }
        String str2 = this.f1991a[realLineCount - 1];
        return str2 == null ? "" : str2;
    }

    public String getText(int lineIndex) {
        return getText(this.text, lineIndex);
    }

    public String getText(String str, int lineIndex) {
        int realLineCount = getRealLineCount(str);
        if (realLineCount <= 0 || this.f1991a.length < lineIndex) {
            Log.e("q/cloud/CuteTextView", "fatal-error lastLineWidth--realLineCount" + realLineCount + "---mStringList.size=" + this.f1991a.length + "---index=" + lineIndex + "---text=" + this.text);
            return "";
        }
        String str2 = this.f1991a[lineIndex];
        return str2 == null ? "" : str2;
    }

    public TextPaint getPaint() {
        m1555a();
        return this.f1987a;
    }

    private synchronized void m1563a(String str) {
        String str2 = this.text;
        int i = this.visible;
        int i2 = this.bgVisible;
        Drawable drawable = this.bgDrawable;
        Canvas canvas = this.f1986a;
        this.text = str;
        this.visible = 0;
        this.bgVisible = 0;
        this.f1986a = null;
        this.bgDrawable = getInfoModel().itemBackground;
        draw(null);
        this.text = str2;
        this.visible = i;
        this.bgVisible = i2;
        this.f1986a = canvas;
        this.bgDrawable = drawable;
    }

    public synchronized void draw(Canvas canvas) {
        this.f1986a = canvas;
        this.f2002g = 0;
        this.f2003h = 0;
        this.f2001f = 0;
        this.f2000e = 0;
        this.f1998d = 0;
        this.f1991a[0] = "";
        this.f1991a[1] = "";
        this.f1991a[2] = "";
        this.f1990a[0] = 0;
        this.f1990a[1] = 0;
        this.f1990a[2] = 0;
        this.f1994b[0] = 0;
        this.f1994b[1] = 0;
        this.f1994b[2] = 0;
        if (!TextUtils.isEmpty(this.text) && getInfoModel() != null && this.lines > 0 && this.visible == 0) {
            int maxWidth = getMaxWidth();
            if (maxWidth > 0 && maxWidth <= 2048) {
                int i = getInfoModel().contentHeight + getInfoModel().ninePatchBorder;
                if (this.lines == 1 || this.ellipsize == TruncateAt.MARQUEE || this.gravity == Gravity4CuteText.ALIGN_PARENT_RIGHT) {
                    this.f2001f = 1;
                    m1562a(canvas, this.text, i, true, this.f1985a, maxWidth);
                } else {
                    int b = m1565b();
                    int i2;
                    String substring;
                    String substring2;
                    if (this.lines == 2) {
                        if (this.ellipsize != TruncateAt.END) {
                            this.ellipsize = TruncateAt.END;
                        }
                        i2 = 1;
                        while (i2 <= this.text.length() && i2 < 100 && this.f1987a.measureText(this.text, 0, i2) < ((float) maxWidth)) {
                            i2++;
                        }
                        i2--;
                        substring = this.text.substring(0, i2);
                        if (i2 == this.text.length()) {
                            this.f2001f = 1;
                            m1562a(canvas, substring, i, false, this.f1985a, maxWidth);
                        } else {
                            substring2 = this.text.substring(i2, this.text.length());
                            this.f2001f = 2;
                            m1562a(canvas, substring, (i - b) - this.lineSpace, false, this.f1985a, maxWidth);
                            m1562a(canvas, substring2, i, true, this.f1993b, maxWidth);
                        }
                    } else {
                        if (this.ellipsize != TruncateAt.END) {
                            this.ellipsize = TruncateAt.END;
                        }
                        i2 = 1;
                        while (i2 <= this.text.length() && i2 < 100 && this.f1987a.measureText(this.text, 0, i2) < ((float) maxWidth)) {
                            i2++;
                        }
                        i2--;
                        substring = this.text.substring(0, i2);
                        if (i2 == this.text.length()) {
                            substring2 = "";
                        } else {
                            substring2 = this.text.substring(i2, this.text.length());
                        }
                        i2 = 1;
                        while (i2 <= substring2.length() && i2 < 100 && this.f1987a.measureText(substring2, 0, i2) < ((float) maxWidth)) {
                            i2++;
                        }
                        i2--;
                        if (substring2.length() > i2) {
                            String substring3 = substring2.substring(i2, substring2.length());
                            String substring4 = substring2.substring(0, i2);
                            this.f2001f = 3;
                            m1562a(canvas, substring, (i - (b * 2)) - (this.lineSpace * 2), false, this.f1985a, maxWidth);
                            m1562a(canvas, substring4, (i - b) - this.lineSpace, false, this.f1993b, maxWidth);
                            m1562a(canvas, substring3, i, true, this.f1996c, maxWidth);
                        } else if (TextUtils.isEmpty(substring2)) {
                            this.f2001f = 1;
                            m1562a(canvas, substring, i, false, this.f1985a, maxWidth);
                        } else {
                            this.f2001f = 2;
                            m1562a(canvas, substring, (i - b) - this.lineSpace, false, this.f1985a, maxWidth);
                            m1562a(canvas, substring2, i, false, this.f1993b, maxWidth);
                        }
                    }
                }
            }
        }
    }

    private void m1562a(Canvas canvas, String str, int i, boolean z, int i2, int i3) {
        int measureText;
        if (this.ellipsize == TruncateAt.MARQUEE) {
            z = false;
        }
        String str2 = z ? (String) TextUtils.ellipsize(str, this.f1987a, (float) i3, this.ellipsize) : str;
        if (i2 == this.f1985a) {
            this.f2000e = (int) this.f1987a.measureText(str2);
        }
        int a = m1555a();
        if (this.gravity == Gravity4CuteText.CENTER_VERTICAL_HORIZONTAL || this.gravity == Gravity4CuteText.CENTER_HORIZONTAL) {
            measureText = ((int) ((((float) i3) - (i2 == this.f1985a ? (float) this.f2000e : this.f1987a.measureText(str2))) / 2.0f)) + a;
        } else if (this.gravity == Gravity4CuteText.CENTER_IN_PARENT) {
            measureText = ((int) ((((float) getInfoModel().contentWidth) - (i2 == this.f1985a ? (float) this.f2000e : this.f1987a.measureText(str2))) / 2.0f)) + a;
        } else if (this.gravity == Gravity4CuteText.ALIGN_PARENT_RIGHT) {
            measureText = ((int) (((((float) getInfoModel().contentWidth) - (i2 == this.f1985a ? (float) this.f2000e : this.f1987a.measureText(str2))) - ((float) this.marginRight)) + ((float) this.marginLeft))) + a;
        } else {
            measureText = a;
        }
        int i4 = ((this.marginTop + i) - this.marginBottom) - ((int) this.f1997d);
        this.f1998d = i4;
        a = this.height;
        if (this.gravity == Gravity4CuteText.CENTER_IN_PARENT || this.gravity == Gravity4CuteText.CENTER_VERTICAL || this.gravity == Gravity4CuteText.CENTER_VERTICAL_HORIZONTAL) {
            if (a == 0 || this.gravity == Gravity4CuteText.CENTER_IN_PARENT) {
                a = getInfoModel().contentHeight;
            }
            int b = m1565b();
            if (this.f2001f == 1) {
                i4 -= (a - b) / 2;
                this.f1998d = i4;
            } else if (this.f2001f == 2) {
                if (i2 == this.f1985a) {
                    i4 = (i4 - ((a - (b * 2)) - this.lineSpace)) + (((a - (b * 2)) - this.lineSpace) / 2);
                }
                if (i2 == this.f1993b) {
                    i4 = (i4 - ((a - (b * 2)) - this.lineSpace)) + (((a - (b * 2)) - this.lineSpace) / 2);
                    this.f1998d = i4;
                }
            } else if (this.f2001f == 3) {
                if (i2 == this.f1985a) {
                    i4 = (i4 - ((a - (b * 3)) - (this.lineSpace * 2))) + (((a - (b * 3)) - (this.lineSpace * 2)) / 2);
                }
                if (i2 == this.f1993b) {
                    i4 = (i4 - ((a - (b * 2)) - this.lineSpace)) + ((a - b) / 2);
                }
                if (i2 == this.f1996c) {
                    i4 = this.lineSpace + (((a / 2) + (i4 - (a - b))) + (b / 2));
                    this.f1998d = i4;
                }
            }
        }
        this.f1991a[i2 - 1] = str2;
        this.f1990a[i2 - 1] = i4;
        this.f1994b[i2 - 1] = measureText;
        if (i2 == this.f2001f) {
            m1560a(canvas, i3);
        }
    }

    private void m1560a(Canvas canvas, int i) {
        int i2 = 0;
        int i3 = this.f1994b[0];
        m1561a(canvas, i3, this.f1998d, this.f2000e);
        if (canvas != null) {
            int a = m1555a();
            int i4 = (int) (((float) this.f1998d) + this.f1997d);
            int b = i4 - ((this.f2001f * m1565b()) + ((this.f2001f - 1) * this.lineSpace));
            int a2 = m1557a(a, i);
            while (i2 < this.f2001f) {
                if (this.ellipsize == TruncateAt.MARQUEE && !this.f1989a) {
                    if (this.f1995c == 0.0f) {
                        m1559a(a2);
                    }
                    if (this.f1992b > this.f1995c) {
                        canvas.save();
                        canvas.clipRect(a, b, a2, i4);
                        canvas.drawText(this.text, ((float) i3) - this.f1984a, (float) this.f1990a[i2], this.f1987a);
                        canvas.restore();
                        canvas.save();
                        canvas.clipRect(a, b, a2, i4);
                        canvas.drawText(this.text, ((((float) i3) + this.f1992b) + ((float) this.marqueeTextSpace)) - this.f1984a, (float) this.f1990a[i2], this.f1987a);
                        canvas.restore();
                        this.f1984a += this.marqueeSpeed;
                        if (this.f1984a >= this.f1992b + ((float) this.marqueeTextSpace)) {
                            this.f1984a = 0.0f;
                        }
                        invalidate();
                        return;
                    }
                }
                canvas.save();
                canvas.clipRect(a, b, a2, i4);
                canvas.drawText(this.f1991a[i2], (float) this.f1994b[i2], (float) this.f1990a[i2], this.f1987a);
                canvas.restore();
                i2++;
            }
        }
    }

    private void m1559a(int i) {
        this.f1984a = 0.0f;
        this.f1992b = m1555a();
        this.f1995c = (float) (i - m1555a());
        if (this.marqueeTextSpace == 0) {
            this.marqueeTextSpace = this.size * 2;
        }
    }

    private int m1556a() {
        int i = (getInfoModel().ninePatchBorder + this.marginLeft) + this.paddingLeft;
        float textSkewX = this.f1987a.getTextSkewX();
        if (textSkewX > 0.0f) {
            return (int) (((float) i) - (textSkewX * ((float) this.size)));
        }
        return i;
    }

    private int m1557a(int i, int i2) {
        int i3 = i + i2;
        if (i3 > getInfoModel().ninePatchBorder + getInfoModel().contentWidth) {
            i3 = getInfoModel().ninePatchBorder + getInfoModel().contentWidth;
        }
        float textSkewX = this.f1987a.getTextSkewX();
        if (textSkewX < 0.0f) {
            return (int) (((float) i3) - (textSkewX * ((float) this.size)));
        }
        return i3;
    }

    private int m1565b() {
        return (int) (this.f1997d - this.f1999e);
    }

    private float m1555a() {
        return this.f1987a.measureText(this.text);
    }

    private void m1558a() {
        if (this.f1987a == null) {
            this.f1987a = new TextPaint();
        }
        this.f1987a.setShadowLayer(this.shadowLayerRadius, this.shadowLayerDx, this.shadowLayerDy, this.shadowLayerColor);
        this.f1987a.setColor(getInfoModel().focused ? this.focusColor : this.normalColor);
        this.f1987a.setAntiAlias(this.antiAlias);
        this.f1987a.setTextScaleX(this.scaleX);
        this.f1987a.setTextSize((float) this.size);
        this.f1987a.setAlpha((this.alphaPercentage * 255) / 100);
        if (CloudUtils.getTypeface() != null) {
            this.f1987a.setTypeface(CloudUtils.getTypeface());
        }
        if (!(TextUtils.isEmpty(this.font) || PluginStateChangedListener.ERROR_TYPE_DEFAULT.equalsIgnoreCase(this.font))) {
            if ("BOLD".equalsIgnoreCase(this.font)) {
                m1566b(1);
            } else if ("ITALIC".equalsIgnoreCase(this.font)) {
                m1566b(2);
            } else if ("BOLD_ITALIC".equalsIgnoreCase(this.font)) {
                m1566b(3);
            }
        }
        this.f1997d = this.f1987a.descent();
        this.f1999e = this.f1987a.ascent();
    }

    private void m1566b(int i) {
        int style;
        boolean z = false;
        if (Typeface.create(Typeface.MONOSPACE, i) != null) {
            style = Typeface.create(Typeface.MONOSPACE, i).getStyle();
        } else {
            style = 0;
        }
        style = (style ^ -1) & i;
        TextPaint textPaint = this.f1987a;
        if ((style & 1) != 0) {
            z = true;
        }
        textPaint.setFakeBoldText(z);
        this.f1987a.setTextSkewX((style & 2) != 0 ? -this.skewX : 0.0f);
    }

    private void m1561a(Canvas canvas, int i, int i2, int i3) {
        if (this.bgDrawable != null && i3 > 0 && this.bgVisible == 0) {
            int i4;
            int i5;
            int i6;
            int i7 = this.bgWidth;
            int i8 = this.bgHeight;
            int i9 = (int) (((float) i2) + this.f1997d);
            if (this.bgScaleType == ScaleType4CuteTextBg.MATRIX) {
                if (i8 == 0) {
                    i8 = (this.f2001f * m1565b()) + ((this.f2001f - 1) * this.lineSpace);
                }
                if (i7 != 0) {
                    i3 = i7;
                }
                i4 = i + this.bgPaddingLeft;
                i5 = (i + i3) - this.bgPaddingRight;
                i6 = (i9 - i8) + this.bgPaddingTop;
                i9 -= this.bgPaddingBottom;
            } else {
                int i10;
                if (i8 == 0) {
                    i8 = getInfoModel().contentHeight;
                }
                if (this.bgClipCanvas) {
                    i10 = getInfoModel().ninePatchBorder;
                } else {
                    i10 = 0;
                }
                i4 = this.bgPaddingLeft + i10;
                i6 = (i9 - i8) + this.bgPaddingTop;
                i9 -= this.bgPaddingBottom;
                i5 = i7 == 0 ? getInfoModel().contentWidth + i10 : i7 - this.bgPaddingRight;
            }
            this.f2002g = i5 - i4;
            this.f2003h = i9 - i6;
            drawImageBylimit(this.bgDrawable, canvas, i4, i6, i5, i9, this.bgClipCanvas);
        }
    }
}
