package com.gala.video.widget.episode;

public class ItemStyleParamBuilder {
    private int mColorParentTextNormal = -1;
    private int mColorTextFocused;
    private int mColorTextNormal;
    private int mColorTextSelected;
    private boolean mParentNormalOverride;

    public ItemStyleParamBuilder setTextNormalColor(int color) {
        this.mColorTextNormal = color;
        return this;
    }

    public ItemStyleParamBuilder setTextSelectedColor(int color) {
        this.mColorTextSelected = color;
        return this;
    }

    public ItemStyleParamBuilder setTextFocusedColor(int color) {
        this.mColorTextFocused = color;
        return this;
    }

    public ItemStyleParamBuilder setParentTextNormalColor(int color) {
        this.mColorParentTextNormal = color;
        this.mParentNormalOverride = true;
        return this;
    }

    int getParentTextNormalColor() {
        return this.mParentNormalOverride ? this.mColorParentTextNormal : this.mColorTextNormal;
    }

    int getTextNormalColor() {
        return this.mColorTextNormal;
    }

    int getTextSelectedColor() {
        return this.mColorTextSelected;
    }

    int getTextFocusedColor() {
        return this.mColorTextFocused;
    }
}
