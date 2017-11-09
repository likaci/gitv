package com.gala.video.lib.share.uikit.item;

import com.gala.video.lib.share.uikit.contract.HeaderContract.Presenter;
import com.gala.video.lib.share.uikit.contract.HeaderContract.View;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.view.TextCanvas;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class HeaderItem extends Item implements Presenter {
    private int mBlankSpace;
    private int mFocusPosition = -1;
    private String mSkinEndsWith;
    private List<String> mTimeLineList;
    private int mTimeTextMaxWidth;
    private TextCanvas mTips;
    private String mTitle;
    private View mView;
    private List<Integer> mViewCenterList;
    private List<Integer> mViewLeftList;
    private List<Integer> mViewRightList;

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setSkinEndsWith(String skinEndsWith) {
        this.mSkinEndsWith = skinEndsWith;
    }

    public String getSkinEndsWith() {
        return this.mSkinEndsWith;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public int getType() {
        return UIKitConfig.ITEM_TYPE_HEADER;
    }

    public int getWidth() {
        return -1;
    }

    public int getHeight() {
        if (isTitleShow() && isTimeLineShow()) {
            return ResourceUtil.getPx(122);
        }
        if (isTitleShow()) {
            return ResourceUtil.getPx(62);
        }
        if (isTimeLineShow()) {
            return ResourceUtil.getPx(62);
        }
        return 0;
    }

    private boolean isTitleShow() {
        return this.mTitle != null && this.mTitle.length() > 0;
    }

    private boolean isTimeLineShow() {
        return this.mTimeLineList != null && this.mTimeLineList.size() > 0;
    }

    public List<String> getTimeLine() {
        return this.mTimeLineList;
    }

    public void setTimeLine(List<String> timeLineList) {
        this.mTimeLineList = timeLineList;
    }

    public int getFocusPosition() {
        return this.mFocusPosition;
    }

    public void setFocusPosition(int focusPosition) {
        this.mFocusPosition = focusPosition;
        if (this.mView != null) {
            this.mView.invalidate();
        }
    }

    public int getTimeTextMaxWidth() {
        return this.mTimeTextMaxWidth;
    }

    public void setTimeTextMaxWidth(int timeTextMaxWidth) {
        this.mTimeTextMaxWidth = timeTextMaxWidth;
    }

    public int getBlankSpace() {
        return this.mBlankSpace;
    }

    public void setBlankSpace(int blankSpace) {
        this.mBlankSpace = blankSpace;
    }

    public void setViewCenterList(List<Integer> viewCenterList) {
        this.mViewCenterList = viewCenterList;
    }

    public List<Integer> getViewCenterList() {
        return this.mViewCenterList;
    }

    public List<Integer> getViewLeftList() {
        return this.mViewLeftList;
    }

    public void setViewLeftList(List<Integer> viewLeftList) {
        this.mViewLeftList = viewLeftList;
    }

    public List<Integer> getViewRightList() {
        return this.mViewRightList;
    }

    public void setViewRightList(List<Integer> viewRightList) {
        this.mViewRightList = viewRightList;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public CardInfoModel getCardInfoModel() {
        return getParent().getModel();
    }

    public void setTips(TextCanvas tips) {
        this.mTips = tips;
    }

    public TextCanvas getTips() {
        return this.mTips;
    }
}
