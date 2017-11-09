package com.gala.video.lib.share.common.widget;

import android.view.View;

public class ScrollHolder extends AnimHolder {
    private int distanceX;
    private int distanceY;
    private View mView;
    private int startX = -1;
    private int startY = -1;

    public ScrollHolder(View view, boolean isX, int start, int end) {
        this.mView = view;
        if (isX) {
            this.startX = start;
            this.distanceX = end - start;
            return;
        }
        this.startY = start;
        this.distanceY = end - start;
    }

    public ScrollHolder(View view, int startX, int endX, int startY, int endY) {
        this.mView = view;
        this.startX = startX;
        this.startY = startY;
        this.distanceX = endX - startX;
        this.distanceY = endY - startY;
    }

    public void doFrame(float fraction) {
        if (this.distanceX != 0 && this.distanceY != 0) {
            this.mView.scrollTo(this.startX + ((int) (((float) this.distanceX) * fraction)), this.startY + ((int) (((float) this.distanceY) * fraction)));
        } else if (this.distanceX != 0) {
            this.mView.setScrollX(this.startX + ((int) (((float) this.distanceX) * fraction)));
        } else if (this.distanceY != 0) {
            this.mView.setScrollY(this.startY + ((int) (((float) this.distanceY) * fraction)));
        }
    }
}
