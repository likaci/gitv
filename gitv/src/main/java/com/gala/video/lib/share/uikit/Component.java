package com.gala.video.lib.share.uikit;

public abstract class Component {
    private int mLine;

    public abstract int getType();

    public void setLine(int line) {
        this.mLine = line;
    }

    public int getLine() {
        return this.mLine;
    }
}
