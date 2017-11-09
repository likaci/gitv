package com.gala.video.cloudui.view.impl;

import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import com.gala.video.cloudui.Gravity4CuteText;
import com.gala.video.cloudui.ScaleType4CuteTextBg;

public interface IText {
    int getAlphaPercentage();

    Drawable getBgDrawable();

    int getBgHeight();

    int getBgPaddingBottom();

    int getBgPaddingLeft();

    int getBgPaddingRight();

    int getBgPaddingTop();

    ScaleType4CuteTextBg getBgScaleType();

    int getBgVisible();

    TruncateAt getEllipsize();

    int getFirstLineWidth();

    int getFirstLineWidth(String str);

    int getFocusColor();

    String getFont();

    Gravity4CuteText getGravity();

    int getHeight();

    String getLastLineText();

    String getLastLineText(String str);

    int getLastLineWidth();

    int getLastLineWidth(String str);

    int getLineSpace();

    int getLineWidth(int i);

    int getLineWidth(String str, int i);

    int getLines();

    int getMarginBottom();

    int getMarginLeft();

    int getMarginRight();

    int getMarginTop();

    int getMarqueeDelay();

    float getMarqueeSpeed();

    int getMarqueeTextSpace();

    int getMaxWidth();

    int getMaxWidth(int i);

    int getNormalColor();

    int getPaddingLeft();

    int getPaddingRight();

    TextPaint getPaint();

    int getRealBgHeight();

    int getRealBgHeight(String str);

    int getRealBgWidth();

    int getRealBgWidth(String str);

    int getRealLineCount();

    int getRealLineCount(String str);

    float getScaleX();

    int getShadowLayerColor();

    float getShadowLayerDx();

    float getShadowLayerDy();

    float getShadowLayerRadius();

    int getSize();

    float getSkewX();

    Object getTag();

    String getText();

    String getText(int i);

    String getText(String str, int i);

    int getVisible();

    int getWidth();

    boolean isAntiAlias();

    void setAlphaPercentage(int i);

    void setAntiAlias(boolean z);

    void setBgDrawable(Drawable drawable);

    void setBgHeight(int i);

    void setBgPaddingBottom(int i);

    void setBgPaddingLeft(int i);

    void setBgPaddingRight(int i);

    void setBgPaddingTop(int i);

    void setBgScaleType(ScaleType4CuteTextBg scaleType4CuteTextBg);

    void setBgVisible(int i);

    void setEllipsize(TruncateAt truncateAt);

    void setFocusColor(int i);

    void setFont(String str);

    void setGravity(Gravity4CuteText gravity4CuteText);

    void setHeight(int i);

    void setLineSpace(int i);

    void setLines(int i);

    void setMarginBottom(int i);

    void setMarginLeft(int i);

    void setMarginRight(int i);

    void setMarginTop(int i);

    void setMarqueeDelay(int i);

    void setMarqueeSpeed(float f);

    void setMarqueeTextSpace(int i);

    void setNormalColor(int i);

    void setPaddingLeft(int i);

    void setPaddingRight(int i);

    void setScaleX(float f);

    void setShadowLayerColor(int i);

    void setShadowLayerDx(float f);

    void setShadowLayerDy(float f);

    void setShadowLayerRadius(float f);

    void setSize(int i);

    void setSkewX(float f);

    void setTag(Object obj);

    void setText(String str);

    void setVisible(int i);

    void setWidth(int i);

    String subText(int i);

    String subText(String str, int i);
}
