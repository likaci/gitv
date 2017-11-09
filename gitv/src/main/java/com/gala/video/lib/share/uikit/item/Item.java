package com.gala.video.lib.share.uikit.item;

import com.gala.video.lib.share.uikit.Component;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.contract.ItemContract.Presenter;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;

public class Item extends Component implements Presenter {
    private int mHeight;
    protected ItemInfoModel mItemInfoModel;
    private Card mParent;
    private int mWidth;

    public void setModel(ItemInfoModel itemInfoModel) {
        int leftPadding;
        int i = 0;
        this.mItemInfoModel = itemInfoModel;
        short width = this.mItemInfoModel.getWidth();
        if (this.mItemInfoModel.getWidth() > (short) 0) {
            leftPadding = getLeftPadding() + getRightPadding();
        } else {
            leftPadding = 0;
        }
        this.mWidth = leftPadding + width;
        short height = this.mItemInfoModel.getHeight();
        if (this.mItemInfoModel.getWidth() > (short) 0) {
            i = getTopPadding() + getBottomPadding();
        }
        this.mHeight = height + i;
    }

    public ItemInfoModel getModel() {
        return this.mItemInfoModel;
    }

    public int getType() {
        return this.mItemInfoModel.getItemType();
    }

    public int getCardId() {
        return this.mParent.getId();
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    protected int getLeftPadding() {
        return 0;
    }

    protected int getTopPadding() {
        return 0;
    }

    protected int getRightPadding() {
        return 0;
    }

    protected int getBottomPadding() {
        return 0;
    }

    public void assignParent(Card parent) {
        this.mParent = parent;
    }

    public Card getParent() {
        return this.mParent;
    }

    public boolean isVisible() {
        return isVisible(false);
    }

    public boolean isVisible(boolean fully) {
        return this.mParent.isChildVisible(this, fully);
    }
}
