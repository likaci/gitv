package com.gala.video.lib.share.uikit.view.widget.record;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.alibaba.fastjson.asm.Opcodes;
import com.gala.cloudui.block.CuteImage;
import com.gala.cloudui.block.CuteText;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.view.widget.UIKitCloudItemView;

public class SearchHistoryView extends UIKitCloudItemView {
    private CuteImage mBGView;
    private CuteImage mIconView;
    private float mIconWeight = 0.85f;
    private SearchHistoryItemModel mSearchHistoryItemModel;
    private CuteText mTitleView;

    public static class SearchHistoryItemModel {
        public Drawable mIcon;
        public String mTitle;
    }

    public SearchHistoryView(Context context) {
        super(context);
    }

    public void setData(SearchHistoryItemModel searchHistoryItemModel) {
        if (searchHistoryItemModel != null) {
            getIconView();
            getTitleView();
            if (this.mIconView != null) {
                this.mIconView.setDrawable(searchHistoryItemModel.mIcon);
            }
            if (this.mTitleView != null) {
                this.mTitleView.setText(searchHistoryItemModel.mTitle);
                setContentDescription(searchHistoryItemModel.mTitle);
            }
        }
    }

    public void adjustTitleViewPlaceAndIconSize(int widgetType, int viewHeight, int viewWidth) {
        int iconWidth;
        int iconHeight;
        getIconView();
        getTitleView();
        float aspectRatio = (((float) viewWidth) * 1.0f) / ((float) viewHeight);
        if (aspectRatio < 1.0f) {
            this.mIconWeight = 0.65f;
            iconWidth = (int) (((float) ((viewWidth - getNinePatchBorders().left) - getNinePatchBorders().right)) * this.mIconWeight);
            iconHeight = calcIconHeight(iconWidth);
        } else {
            if (((double) aspectRatio) < 1.5d) {
                this.mIconWeight = 0.47f;
            } else if (aspectRatio <= 2.0f || ((double) aspectRatio) >= 2.5d) {
                this.mIconWeight = 0.85f;
            } else {
                this.mIconWeight = ThreeDimensionalParams.TEXT_SCALE_FOR_3D;
            }
            iconHeight = (int) (((float) ((viewHeight - getNinePatchBorders().top) - getNinePatchBorders().bottom)) * this.mIconWeight);
            iconWidth = calcIconWidth(iconHeight);
        }
        if (this.mIconView != null) {
            this.mIconView.setHeight(iconHeight);
            this.mIconView.setWidth(iconWidth);
        }
        if (this.mTitleView != null) {
            if (((double) aspectRatio) < 1.5d) {
                this.mTitleView.setWidth(viewWidth);
                this.mTitleView.setHeight(((viewHeight - getNinePatchBorders().top) - getNinePatchBorders().bottom) - iconHeight);
                this.mTitleView.setMarginLeft(0);
            } else if (widgetType == 3) {
                this.mTitleView.setWidth((viewWidth - getNinePatchBorders().left) - getNinePatchBorders().right);
                this.mTitleView.setHeight(viewHeight);
                this.mTitleView.setMarginLeft(0);
            } else {
                this.mTitleView.setWidth(((viewWidth - getNinePatchBorders().left) - getNinePatchBorders().right) - iconWidth);
                this.mTitleView.setHeight(viewHeight);
                this.mTitleView.setMarginLeft(iconWidth);
            }
        }
    }

    private int calcIconWidth(int iconHeight) {
        return (iconHeight * 212) / Opcodes.IFNULL;
    }

    private int calcIconHeight(int iconWidth) {
        return (iconWidth * Opcodes.IFNULL) / 212;
    }

    public SearchHistoryItemModel getSearchHistoryItemModel() {
        if (this.mSearchHistoryItemModel == null) {
            this.mSearchHistoryItemModel = new SearchHistoryItemModel();
        }
        return this.mSearchHistoryItemModel;
    }

    public CuteImage getBGView() {
        if (this.mBGView == null) {
            this.mBGView = getCuteImage(UIKitConfig.ID_BG);
        }
        return this.mBGView;
    }

    public CuteText getTitleView() {
        if (this.mTitleView == null) {
            this.mTitleView = getCuteText("ID_TITLE");
        }
        return this.mTitleView;
    }

    public CuteImage getIconView() {
        if (this.mIconView == null) {
            this.mIconView = getCuteImage("ID_IMAGE");
        }
        return this.mIconView;
    }
}
