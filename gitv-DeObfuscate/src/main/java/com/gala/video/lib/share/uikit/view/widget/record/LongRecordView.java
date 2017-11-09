package com.gala.video.lib.share.uikit.view.widget.record;

import android.content.Context;
import android.graphics.Rect;
import android.view.KeyEvent;
import com.gala.cloudui.block.CuteImage;
import com.gala.cloudui.block.CuteText;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants;
import com.gala.video.lib.share.uikit.view.widget.UIKitCloudItemView;
import com.gala.video.lib.share.utils.ResourceUtil;

public class LongRecordView extends UIKitCloudItemView {
    private static final String LOG_TAG = "LongRecordView";
    private CuteImage mBottomLine;
    private CuteText mDescView;
    private CuteImage mPercentView;
    private CuteText mTitleView;

    public static class LongHistoryItemModel {
        public String mDesc;
        public int mPercent;
        public String mTitle;
    }

    public LongRecordView(Context context) {
        super(context);
    }

    public void setData(LongHistoryItemModel longHistoryItemModel) {
        if (longHistoryItemModel != null) {
            getTitleView();
            getDescView();
            getPerentView();
            if (this.mTitleView != null) {
                this.mTitleView.setText(longHistoryItemModel.mTitle);
                setContentDescription(longHistoryItemModel.mTitle);
            }
            if (this.mDescView != null) {
                this.mDescView.setText(longHistoryItemModel.mDesc);
            }
            if (this.mPercentView != null) {
                LogUtils.m1576i(LOG_TAG, "longrecordview width = ", Integer.valueOf(getWidth()));
                int w = ResourceUtil.getDimen(C1632R.dimen.dimen_13dp);
                this.mPercentView.setWidth((((getWidth() - w) * longHistoryItemModel.mPercent) / 100) + w);
            }
        }
    }

    private CuteImage getPerentView() {
        if (this.mPercentView == null) {
            this.mPercentView = getCuteImage("ID_PERCENT_IMAGE");
        }
        return this.mPercentView;
    }

    private CuteText getDescView() {
        if (this.mDescView == null) {
            this.mDescView = getCuteText("ID_DESC");
        }
        return this.mDescView;
    }

    private CuteText getTitleView() {
        if (this.mTitleView == null) {
            this.mTitleView = getCuteText("ID_TITLE");
        }
        return this.mTitleView;
    }

    private CuteImage getBottomLine() {
        if (this.mBottomLine == null) {
            this.mBottomLine = getCuteImage("ID_BOTTOM_LINE");
        }
        return this.mBottomLine;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() != 66 && event.getKeyCode() != 23) || event.getAction() != 0) {
            return super.dispatchKeyEvent(event);
        }
        performClick();
        return true;
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (getBottomLine() != null && getPerentView() != null) {
            if (gainFocus) {
                getBottomLine().setWidth(getPerentView().getWidth());
            } else {
                getBottomLine().setWidth(ResourceUtil.getPx(AdsConstants.IMAGE_MAX_WIGTH));
            }
        }
    }
}
