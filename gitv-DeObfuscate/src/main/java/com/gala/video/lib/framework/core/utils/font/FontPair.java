package com.gala.video.lib.framework.core.utils.font;

import android.graphics.Typeface;

public class FontPair {
    private String mFontPath;
    private Typeface mTypeface;

    public FontPair(String fontPath, Typeface typeface) {
        this.mFontPath = fontPath;
        this.mTypeface = typeface;
    }

    public String getFontPath() {
        return this.mFontPath;
    }

    public void setFontPath(String fontPath) {
        this.mFontPath = fontPath;
    }

    public Typeface getTypeface() {
        return this.mTypeface;
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
    }
}
