package com.gala.video.app.epg.home.component.item;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.component.item.corner.ItemCorner;
import com.gala.video.app.epg.home.component.item.corner.TitleOutItemCorner;
import com.gala.video.app.epg.home.component.item.widget.ItemCloudViewType;
import com.gala.video.app.epg.home.utils.ItemUiFactory;
import com.gala.video.app.epg.utils.EpgImageCache;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.cloudui.Gravity4CuteImage;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class TitleOutItem extends AndroidItem {
    private static final int CHANGE_FOCUS_EXTRA_SPACE = ResourceUtil.getDimen(C0508R.dimen.dimen_19dp);
    private CuteImageView mBottomBgGreenColorView;
    private CuteImageView mChannelIdImageView;
    private String mChannelIdStr;
    private CuteTextView mChannelIdTextView;
    private ItemCorner mItemCorner;
    private CuteImageView mTitleBgView;
    private String mTitleStr;
    private CuteTextView mTitleView;

    public TitleOutItem(int itemtype) {
        super(itemtype, ItemCloudViewType.DEFAULT);
    }

    protected void setData() {
        setTitle();
        setChannelId();
        setCorner();
        this.mView.setBackgroundDrawable(ItemUiFactory.getRectBgDrawable(this.mItemData.isVipTab));
        this.mTitleView.setNormalColor(this.mContext.getResources().getColor(C0508R.color.albumview_normal_color));
        invalidateViewStatus(this.mView.isFocused());
    }

    private void setCorner() {
        if (this.mItemCorner != null) {
            this.mItemCorner.updateItemData(this.mItemData);
            this.mItemCorner.setCorner();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mItemCorner != null) {
            this.mItemCorner.destory();
        }
    }

    private void setChannelId() {
        if (this.mItemData.isCarouselChannel) {
            this.mChannelIdStr = ItemUiFactory.getCarsoulChannelId(this.mItemData.carouselChannelId);
            if (!TextUtils.isEmpty(this.mChannelIdStr)) {
                this.mTitleView.setMarginLeft(ResourceUtil.getDimen(C0508R.dimen.dimen_42dp));
                this.mTitleView.setPaddingRight(ResourceUtil.getDimen(C0508R.dimen.dimen_42dp));
                this.mTitleBgView.setMarginLeft(ResourceUtil.getDimen(C0508R.dimen.dimen_42dp));
                this.mChannelIdImageView.setMarginLeft(0);
                this.mChannelIdImageView.setMarginBottom(CHANGE_FOCUS_EXTRA_SPACE);
                this.mChannelIdImageView.setWidth(ResourceUtil.getDimen(C0508R.dimen.dimen_42dp));
                this.mChannelIdImageView.setHeight(ResourceUtil.getDimen(C0508R.dimen.dimen_42dp));
            }
            if (TextUtils.isEmpty(this.mTitleView.getText())) {
                this.mChannelIdTextView.setText(null);
                return;
            } else {
                this.mChannelIdTextView.setText(this.mChannelIdStr);
                return;
            }
        }
        this.mChannelIdTextView.setText(null);
    }

    private void setTitle() {
        this.mTitleStr = this.mItemData.getTitle();
        if (!TextUtils.isEmpty(this.mTitleStr)) {
            this.mTitleView.setMarginLeft(0);
            this.mTitleView.setPaddingRight(ResourceUtil.getDimen(C0508R.dimen.dimen_6dp));
            this.mTitleBgView.setMarginLeft(0);
        }
        this.mTitleView.setText(this.mTitleStr);
        this.mView.setContentDescription(this.mTitleStr);
    }

    protected void initViewComponent() {
        this.mCoreImageView = this.mView.getCoreImageView();
        this.mTitleView = this.mView.getTitleView();
        this.mTitleBgView = this.mView.getTitleBgView();
        this.mChannelIdTextView = this.mView.getChannelIdView();
        this.mChannelIdImageView = this.mView.getChnIdBgView();
        this.mBottomBgGreenColorView = this.mView.getBottomBgView();
        this.mItemCorner = new TitleOutItemCorner(this.mView);
        this.mCoreImageView.setPaddingBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_55dp));
    }

    private void invalidateViewStatus(boolean hasFocus) {
        if (hasFocus) {
            if (TextUtils.isEmpty(this.mTitleStr)) {
                this.mBottomBgGreenColorView.setDrawable(null);
                this.mView.setBgPaddingBottom(CHANGE_FOCUS_EXTRA_SPACE);
            } else {
                this.mTitleView.setLines(2);
                if (this.mTitleView.getRealLineCount() == 1) {
                    this.mTitleView.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_17dp));
                    this.mView.setBgPaddingBottom(CHANGE_FOCUS_EXTRA_SPACE);
                    this.mBottomBgGreenColorView.setHeight(ResourceUtil.getDimen(C0508R.dimen.dimen_36dp));
                    this.mBottomBgGreenColorView.setGravity(Gravity4CuteImage.CENTER_OF_BOTTOM);
                    this.mBottomBgGreenColorView.setMarginBottom(CHANGE_FOCUS_EXTRA_SPACE);
                    this.mBottomBgGreenColorView.setDrawable(ItemUiFactory.getFocusColorDrawable(this.mItemData.isVipTab));
                } else {
                    this.mTitleView.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_6dp));
                    this.mView.setBgPaddingBottom(0);
                    this.mBottomBgGreenColorView.setHeight(ResourceUtil.getDimen(C0508R.dimen.dimen_55dp));
                    this.mBottomBgGreenColorView.setGravity(Gravity4CuteImage.CENTER_OF_BOTTOM);
                    this.mBottomBgGreenColorView.setMarginBottom(0);
                    this.mBottomBgGreenColorView.setDrawable(ItemUiFactory.getFocusColorDrawable(this.mItemData.isVipTab));
                }
                if (!TextUtils.isEmpty(this.mChannelIdStr) && this.mItemData.isCarouselChannel) {
                    if (this.mTitleView.getRealLineCount() == 2) {
                        this.mChannelIdTextView.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_15dp));
                    } else {
                        this.mChannelIdTextView.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_13dp));
                    }
                }
            }
            this.mTitleBgView.setDrawable(null);
            this.mChannelIdImageView.setDrawable(null);
        } else if (TextUtils.isEmpty(this.mTitleStr)) {
            this.mTitleBgView.setDrawable(null);
            this.mChannelIdImageView.setDrawable(null);
            this.mBottomBgGreenColorView.setDrawable(null);
            this.mView.setBgPaddingBottom(CHANGE_FOCUS_EXTRA_SPACE);
        } else {
            this.mTitleView.setLines(1);
            this.mTitleView.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_17dp));
            this.mView.setBgPaddingBottom(CHANGE_FOCUS_EXTRA_SPACE);
            this.mTitleBgView.setHeight(ResourceUtil.getDimen(C0508R.dimen.dimen_36dp));
            this.mTitleBgView.setGravity(Gravity4CuteImage.CENTER_OF_BOTTOM);
            this.mTitleBgView.setMarginBottom(CHANGE_FOCUS_EXTRA_SPACE);
            this.mTitleBgView.setDrawable(ImageCacheUtil.UNCOVER_COLOR_UNFOCUS_DRAWABLE);
            this.mBottomBgGreenColorView.setDrawable(null);
            if (TextUtils.isEmpty(this.mChannelIdStr) || !this.mItemData.isCarouselChannel) {
                this.mChannelIdImageView.setDrawable(null);
                return;
            }
            this.mChannelIdImageView.setDrawable(EpgImageCache.UNFOCUS_CHANNELID_COLOR_DRAWABLE);
            this.mChannelIdTextView.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_13dp));
        }
    }

    public int getPaddingBottom() {
        return 43;
    }

    String getLogTag() {
        return "TitleOutItem";
    }

    protected void initOnFocusChangeListener() {
        final OnFocusChangeListener onFocusChangeListener = this.mView.getOnFocusChangeListener();
        this.mView.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(v, hasFocus);
                }
                TitleOutItem.this.invalidateViewStatus(hasFocus);
            }
        });
    }
}
