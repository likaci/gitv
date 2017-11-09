package com.gala.cloudui.imp;

import android.graphics.drawable.Drawable;
import android.text.TextPaint;

public interface ICuteText {
    int getBgClipPadding();

    Drawable getBgDrawable();

    Drawable getBgFocusDrawable();

    int getBgGravity();

    int getBgHeight();

    int getBgMarginBottom();

    int getBgMarginLeft();

    int getBgMarginRight();

    int getBgMarginTop();

    int getBgPaddingBottom();

    int getBgPaddingLeft();

    int getBgPaddingRight();

    int getBgPaddingTop();

    int getBgScaleType();

    int getBgVisible();

    int getBgWidth();

    String getDefaultText();

    int getEllipsize();

    int getFocusFontColor();

    int getFont();

    int getFontColor();

    int getFontSize();

    int getGravity();

    int getHeight();

    int getLineSpace();

    int getLines();

    int getMarginBottom();

    int getMarginLeft();

    int getMarginRight();

    int getMarginTop();

    int getMarqueeDelay();

    float getMarqueeSpeed();

    int getMarqueeTextSpace();

    int getPaddingBottom();

    int getPaddingLeft();

    int getPaddingRight();

    int getPaddingTop();

    TextPaint getPaint();

    int getRealLineCount();

    int getRealLineCount(String str);

    int getShadowLayerColor();

    int getShadowLayerDx();

    int getShadowLayerDy();

    float getShadowLayerRadius();

    float getSkewX();

    String getText();

    int getTitleType();

    int getVisible();

    int getWidth();

    void setBgClipPadding(int i);

    void setBgDrawable(Drawable drawable);

    void setBgFocusDrawable(Drawable drawable);

    void setBgGravity(int i);

    void setBgHeight(int i);

    void setBgMarginBottom(int i);

    void setBgMarginLeft(int i);

    void setBgMarginRight(int i);

    void setBgMarginTop(int i);

    void setBgPaddingBottom(int i);

    void setBgPaddingLeft(int i);

    void setBgPaddingRight(int i);

    void setBgPaddingTop(int i);

    void setBgScaleType(int i);

    void setBgVisible(int i);

    void setBgWidth(int i);

    void setDefaultText(String str);

    void setEllipsize(int i);

    void setFocusFontColor(int i);

    void setFont(int i);

    void setFontColor(int i);

    void setFontSize(int i);

    void setGravity(int i);

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

    void setPaddingBottom(int i);

    void setPaddingLeft(int i);

    void setPaddingRight(int i);

    void setPaddingTop(int i);

    void setShadowLayerColor(int i);

    void setShadowLayerDx(int i);

    void setShadowLayerDy(int i);

    void setShadowLayerRadius(float f);

    void setSkewX(float f);

    void setText(String str);

    void setTitleType(int i);

    void setVisible(int i);

    void setWidth(int i);
}
