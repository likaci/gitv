package com.gala.video.app.player.ui.overlay.contents;

import com.gala.video.app.player.ui.overlay.IUIHolder;

public class ContentHolder implements IUIHolder<IContent<?, ?>> {
    private IContent<?, ?> mContent;
    private boolean mIsVisible = false;
    private String mTag;
    private int mType;

    public ContentHolder(String tag, int type, IContent<?, ?> content) {
        this.mContent = content;
        this.mTag = tag;
        this.mType = type;
    }

    public IContent<?, ?> getWrappedContent() {
        return this.mContent;
    }

    public String getTag() {
        return this.mTag;
    }

    public int getType() {
        return this.mType;
    }

    public void setVisibility(boolean visible) {
        this.mIsVisible = visible;
    }

    public boolean getVisibility() {
        return this.mIsVisible;
    }

    public String toString() {
        return "ContentHolder{mTag='" + this.mTag + '\'' + ", mIsVisible=" + this.mIsVisible + ", mContent=" + this.mContent + '}';
    }
}
