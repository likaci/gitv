package com.gala.video.app.epg.home.component;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.gala.video.app.epg.home.data.ItemData;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.MultiSubjectImp;
import com.gala.video.lib.share.pingback.HomeAdPingbackModel;
import com.gala.video.lib.share.utils.MemoryLevelInfo;
import com.gala.video.lib.share.utils.ResourceUtil;

public class Item extends Widget implements MultiSubjectImp {
    protected String TAG;
    protected int mActualHeight;
    protected int mActualWidth;
    protected final int mItemType;
    protected boolean mUseGif;

    public Item(int itemType) {
        this.mItemType = itemType;
    }

    public Object buildUI(Context context) {
        return null;
    }

    public Object updateUI() {
        return null;
    }

    public int getWidth() {
        if (getDataSource() != null) {
            return getDataSource().getWidth();
        }
        return 0;
    }

    public boolean isUseGif() {
        return this.mUseGif;
    }

    public void setActualItemSize(int width, int height) {
        boolean z = false;
        this.mActualWidth = width;
        this.mActualHeight = height;
        ItemData d = getDataSource();
        if (d != null && !TextUtils.isEmpty(d.mGif)) {
            if (width > ResourceUtil.getPx(500)) {
                this.mUseGif = false;
            } else if (!MemoryLevelInfo.isLowConfigDevice()) {
                int t = height * d.mGifWidth;
                if (Math.abs((d.mGifHigh * width) - t) < (t >> 7)) {
                    z = true;
                }
                this.mUseGif = z;
            }
        }
    }

    protected void checkGifAvailable() {
        boolean z = false;
        ItemData d = getDataSource();
        if (d == null || TextUtils.isEmpty(d.mGif)) {
            this.mUseGif = false;
        } else if (this.mActualWidth > ResourceUtil.getPx(500)) {
            this.mUseGif = false;
        } else if (!MemoryLevelInfo.isLowConfigDevice()) {
            int t = this.mActualHeight * d.mGifWidth;
            if (Math.abs((this.mActualWidth * d.mGifHigh) - t) < (t >> 7)) {
                z = true;
            }
            this.mUseGif = z;
        }
    }

    protected int getActualItemWidth() {
        return this.mActualWidth;
    }

    protected int getActualItemHeight() {
        return this.mActualHeight;
    }

    public void setActualItemWidth(int width) {
        this.mActualWidth = width;
    }

    public void setActualItemHeight(int height) {
        this.mActualHeight = height;
    }

    public int getHeight() {
        if (getDataSource() != null) {
            return getDataSource().getHeight();
        }
        return 0;
    }

    public int getPaddingTop() {
        return 21;
    }

    public int getPaddingBottom() {
        return 21;
    }

    public int getPaddingLeftRight() {
        return 21;
    }

    public void onEvent(int event, Object arg) {
        if (event == 773) {
            setVisibilityInParent(((Boolean) arg).booleanValue() ? 1 : 0);
        }
    }

    public ItemData getDataSource() {
        return (ItemData) super.getDataSource();
    }

    public View onCreateViewHolder(Context mContext) {
        return null;
    }

    public void onBindViewHolder(DataSource DataSource) {
    }

    public void loadImage() {
    }

    public void recycleAndShowDefaultImage() {
    }

    public void onDestroy() {
    }

    public int getCardLine() {
        if (this.mParent != null) {
            WidgetTree curPage = this.mParent.getParent();
            if (curPage != null) {
                return curPage.indexOfChildForPingback(this.mParent) + 1;
            }
            return HomeAdPingbackModel.DEFAULT_H5_ENTER_TYPE;
        }
        LogUtils.m1577w(this.TAG, "current card is null");
        return HomeAdPingbackModel.DEFAULT_H5_ENTER_TYPE;
    }
}
