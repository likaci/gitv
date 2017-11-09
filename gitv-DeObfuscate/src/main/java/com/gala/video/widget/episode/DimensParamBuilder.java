package com.gala.video.widget.episode;

public class DimensParamBuilder {
    int mChildHeight = Integer.MIN_VALUE;
    int mChildTextSizeResId = 0;
    int mChildWidth = Integer.MIN_VALUE;
    int mItemSpacing = 0;
    int mParentHeight = Integer.MIN_VALUE;
    ParentLayoutMode mParentLayoutMode = ParentLayoutMode.DOUBLE_CHILD_WIDTH;
    int mParentTextSizeResId = 0;
    int mParentWidth = Integer.MIN_VALUE;

    public DimensParamBuilder setChildTextSizeResId(int id) {
        this.mChildTextSizeResId = id;
        return this;
    }

    public DimensParamBuilder setChildWidth(int childWidth) {
        this.mChildWidth = childWidth;
        return this;
    }

    public DimensParamBuilder setChildHeight(int childHeight) {
        this.mChildHeight = childHeight;
        return this;
    }

    public DimensParamBuilder setItemSpacing(int itemSpacing) {
        this.mItemSpacing = itemSpacing;
        return this;
    }

    public DimensParamBuilder setParentWidth(int parentWidth) {
        this.mParentWidth = parentWidth;
        return this;
    }

    public DimensParamBuilder setParentHeight(int parentHeight) {
        this.mParentHeight = parentHeight;
        return this;
    }

    public DimensParamBuilder setParentTextSizeResId(int parentTextSize) {
        this.mParentTextSizeResId = parentTextSize;
        return this;
    }

    public DimensParamBuilder setParentLayoutMode(ParentLayoutMode mode) {
        this.mParentLayoutMode = mode;
        return this;
    }

    int getValidParamCount() {
        return 0;
    }

    boolean isValid() {
        int i;
        int i2 = 1;
        if (this.mChildTextSizeResId != 0) {
            i = 1;
        } else {
            i = 0;
        }
        boolean isValid = true & i;
        if (this.mChildWidth > 0) {
            i = 1;
        } else {
            i = 0;
        }
        isValid &= i;
        if (this.mChildHeight > 0) {
            i = 1;
        } else {
            i = 0;
        }
        isValid &= i;
        if (this.mItemSpacing < 0) {
            i2 = 0;
        }
        return isValid & i2;
    }
}
