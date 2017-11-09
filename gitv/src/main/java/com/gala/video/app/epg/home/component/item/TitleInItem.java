package com.gala.video.app.epg.home.component.item;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.component.Widget;
import com.gala.video.app.epg.home.component.item.corner.ItemCorner;
import com.gala.video.app.epg.home.component.item.corner.TitleInItemCorner;
import com.gala.video.app.epg.home.component.item.widget.ItemCloudViewType;
import com.gala.video.app.epg.home.utils.ItemUiFactory;
import com.gala.video.app.epg.utils.EpgImageCache;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.share.utils.ResourceUtil;

public class TitleInItem extends AndroidItem {
    private CuteImageView mBottomBgGreenColorView;
    private String mChannelId;
    private CuteImageView mChannelIdImageView;
    private CuteTextView mChannelIdTextView;
    private ItemCorner mItemCorner;
    private Widget mMirror;
    private String mTitle;
    private CuteImageView mTitleBgView;
    private CuteTextView mTitleView;

    public TitleInItem(int itemtype) {
        super(itemtype, ItemCloudViewType.DEFAULT);
    }

    void setMirror(Widget mirror) {
        this.mMirror = mirror;
    }

    public boolean isVisibleToUser() {
        return this.mMirror != null ? this.mMirror.isVisibleToUser() : super.isVisibleToUser();
    }

    protected void setData() {
        setTitle();
        setChannelId();
        setCorner();
        this.mView.setBackgroundDrawable(ItemUiFactory.getRectBgDrawable(this.mItemData.isVipTab));
        invalidateViewStatus(this.mView.isFocused());
    }

    private void setCorner() {
        if (this.mItemCorner != null) {
            this.mItemCorner.updateItemData(this.mItemData);
            this.mItemCorner.setCorner();
        }
    }

    private void setChannelId() {
        if (this.mItemData.isCarouselChannel) {
            this.mChannelId = ItemUiFactory.getCarsoulChannelId(this.mItemData.carouselChannelId);
            if (!TextUtils.isEmpty(this.mChannelId)) {
                this.mTitleView.setMarginLeft(ResourceUtil.getDimen(R.dimen.dimen_40dp));
                this.mTitleView.setPaddingRight(ResourceUtil.getDimen(R.dimen.dimen_40dp));
                this.mTitleBgView.setMarginLeft(ResourceUtil.getDimen(R.dimen.dimen_40dp));
                this.mChannelIdTextView.setMarginBottom(0);
                this.mChannelIdImageView.setMarginLeft(0);
                this.mChannelIdImageView.setMarginBottom(0);
                this.mChannelIdImageView.setWidth(ResourceUtil.getDimen(R.dimen.dimen_40dp));
                this.mChannelIdImageView.setHeight(ResourceUtil.getDimen(R.dimen.dimen_40dp));
            }
            if (TextUtils.isEmpty(this.mTitleView.getText())) {
                this.mChannelIdTextView.setText(null);
                return;
            } else {
                this.mChannelIdTextView.setText(this.mChannelId);
                return;
            }
        }
        this.mChannelIdTextView.setText(null);
    }

    private void setTitle() {
        this.mTitle = this.mItemData.getTitle();
        if (!TextUtils.isEmpty(this.mTitle)) {
            this.mTitleView.setMarginLeft(0);
            this.mTitleView.setPaddingRight(ResourceUtil.getDimen(R.dimen.dimen_6dp));
            this.mTitleBgView.setMarginLeft(0);
            this.mTitleBgView.setWidth(ResourceUtil.getDimen(R.dimen.dimen_1920dp));
            this.mTitleBgView.setHeight(ResourceUtil.getDimen(R.dimen.dimen_40dp));
            this.mTitleBgView.setMarginBottom(0);
        }
        this.mTitleView.setText(this.mTitle);
        this.mView.setContentDescription(this.mTitle);
    }

    protected void initViewComponent() {
        if (this.mView != null) {
            this.mTitleView = this.mView.getTitleView();
            this.mTitleBgView = this.mView.getTitleBgView();
            this.mCoreImageView = this.mView.getCoreImageView();
            this.mBottomBgGreenColorView = this.mView.getBottomBgView();
            this.mChannelIdTextView = this.mView.getChannelIdView();
            this.mChannelIdImageView = this.mView.getChnIdBgView();
            this.mItemCorner = new TitleInItemCorner(this.mView);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mItemCorner != null) {
            this.mItemCorner.destory();
        }
    }

    private void invalidateViewStatus(boolean hasFocus) {
        if (hasFocus) {
            if (!TextUtils.isEmpty(this.mTitle) && !TextUtils.isEmpty(this.mChannelId) && this.mItemData.isCarouselChannel) {
                this.mTitleBgView.setDrawable(null);
                this.mChannelIdImageView.setDrawable(null);
                this.mBottomBgGreenColorView.setDrawable(ItemUiFactory.getFocusColorDrawable(this.mItemData.isVipTab));
            } else if (TextUtils.isEmpty(this.mTitle)) {
                this.mTitleBgView.setDrawable(null);
                this.mChannelIdImageView.setDrawable(null);
                this.mBottomBgGreenColorView.setDrawable(null);
            } else {
                this.mTitleBgView.setDrawable(ItemUiFactory.getFocusColorDrawable(this.mItemData.isVipTab));
                this.mChannelIdImageView.setDrawable(null);
                this.mBottomBgGreenColorView.setDrawable(null);
            }
        } else if (!TextUtils.isEmpty(this.mTitle) && !TextUtils.isEmpty(this.mChannelId) && this.mItemData.isCarouselChannel) {
            this.mTitleBgView.setDrawable(EpgImageCache.CARD_COVER_COLOR_UNFOCUS_DRAWABLE);
            this.mChannelIdImageView.setDrawable(EpgImageCache.UNFOCUS_CHANNELID_COLOR_DRAWABLE);
            this.mBottomBgGreenColorView.setDrawable(null);
        } else if (TextUtils.isEmpty(this.mTitle)) {
            this.mTitleBgView.setDrawable(null);
            this.mChannelIdImageView.setDrawable(null);
            this.mBottomBgGreenColorView.setDrawable(null);
        } else {
            this.mTitleBgView.setDrawable(EpgImageCache.CARD_COVER_COLOR_UNFOCUS_DRAWABLE);
            this.mChannelIdImageView.setDrawable(null);
            this.mBottomBgGreenColorView.setDrawable(null);
        }
    }

    String getLogTag() {
        return "TitleInItem";
    }

    protected void initOnFocusChangeListener() {
        final OnFocusChangeListener onFocusChangeListener = this.mView.getOnFocusChangeListener();
        this.mView.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(v, hasFocus);
                }
                TitleInItem.this.invalidateViewStatus(hasFocus);
            }
        });
    }
}
