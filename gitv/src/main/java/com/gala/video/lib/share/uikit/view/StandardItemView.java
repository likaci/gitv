package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.gala.cloudui.block.CuteImage;
import com.gala.cloudui.block.CuteText;
import com.gala.cloudui.utils.CloudUtilsGala;
import com.gala.tvapi.type.LivePlayingType;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.uikit.contract.StandardItemContract.Presenter;
import com.gala.video.lib.share.uikit.contract.StandardItemContract.View;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.item.presenter.StandardLoaderPresenter;
import com.gala.video.lib.share.uikit.utils.TypeUtils;
import com.gala.video.lib.share.uikit.view.widget.UIKitCloudItemView;
import com.gala.video.lib.share.utils.ResourceUtil;

public class StandardItemView extends UIKitCloudItemView implements IViewLifecycle<Presenter>, View {
    private static final String TAG = "StandardItemView";
    private static final int px34 = ResourceUtil.getPx(34);
    private static final int px54 = ResourceUtil.getPx(54);
    private static final int px88 = ResourceUtil.getPx(88);
    private static final Rect rectCircleFocus = new Rect(ResourceUtil.getPx(24), ResourceUtil.getPx(24), ResourceUtil.getPx(24), ResourceUtil.getPx(24));
    private static final Rect rectSkewFocus = new Rect(ResourceUtil.getPx(22), ResourceUtil.getPx(24), ResourceUtil.getPx(24), ResourceUtil.getPx(24));
    private ItemInfoModel mItemModel;
    private StandardLoaderPresenter mLoaderPresenter = new StandardLoaderPresenter();
    private Presenter mPresenter;

    public StandardItemView(Context context) {
        super(context);
    }

    public void onBind(Presenter object) {
        this.mPresenter = object;
        if (this.mPresenter != null) {
            this.mItemModel = object.getModel();
            if (this.mItemModel != null) {
                initUIStyle(object);
                initView();
                initFocus();
                setDefaultImage();
                this.mLoaderPresenter.recycleAndShowDefaultImage();
                this.mPresenter.setView(this);
                this.mPresenter.removeLiveCornerObserver();
            }
        }
    }

    private void initFocus() {
        setTag(CardFocusHelper.ItemDeltaHigh, Integer.valueOf(0));
        setTag(CardFocusHelper.FOCUS_RES_ENDS_WITH, this.mItemModel.getSkinEndsWith());
        if (isCircleNoTitleType()) {
            setTag(CardFocusHelper.RESOURCE_PADDING, null);
            setTag(CardFocusHelper.FOCUS_RES, "share_item_circle_bg_focus");
        } else if (isCircleTitleType()) {
            setTag(CardFocusHelper.FOCUS_RES, "share_item_circle_bg_focus");
            setTag(CardFocusHelper.RESOURCE_PADDING, rectCircleFocus);
            setTag(CardFocusHelper.ItemDeltaHigh, Integer.valueOf(ResourceUtil.getPx(63)));
        } else if (isSkewType()) {
            setTag(CardFocusHelper.RESOURCE_PADDING, rectSkewFocus);
            setTag(CardFocusHelper.FOCUS_RES, "share_skew_image_bg_focus");
        } else {
            setTag(CardFocusHelper.RESOURCE_PADDING, null);
            setTag(CardFocusHelper.FOCUS_RES, null);
        }
        invalidateViewStatus(isFocused());
    }

    protected void initUIStyle(Presenter object) {
        setStyleByName(object.getModel().getStyle());
    }

    public void onUnbind(Presenter object) {
        object.removeLiveCornerObserver();
        this.mLoaderPresenter.recycleAndShowDefaultImage();
        recycle();
    }

    public void onShow(Presenter object) {
        this.mLoaderPresenter.loadImage(this, object.getModel());
    }

    public void onHide(Presenter object) {
        this.mLoaderPresenter.recycleAndShowDefaultImage();
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        invalidateViewStatus(gainFocus);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public void onLoadImageSuccess(Bitmap bitmap) {
        updateUI(this.mItemModel);
        CuteImage coreImageView = getCoreImageView();
        if (coreImageView != null) {
            coreImageView.setBitmap(bitmap);
        }
    }

    public void onLoadImageSuccess(Drawable drawable) {
        updateUI(this.mItemModel);
        CuteImage coreImageView = getCoreImageView();
        if (coreImageView != null) {
            coreImageView.setDrawable(drawable);
        }
    }

    public void onLoadImageFail() {
        updateUI(this.mItemModel);
        this.mLoaderPresenter.recycleAndShowDefaultImage();
    }

    public void updateUI(ItemInfoModel mItemModel) {
        super.updateUI(mItemModel);
        updatePlayingGif(mItemModel);
        updateLBCorner(mItemModel);
        updateLiveCorner(mItemModel);
    }

    private void updateLiveCorner(ItemInfoModel mItemModel) {
        if (getCornerRTView() != null) {
            String liveType = mItemModel.getCuteViewData("ID_CORNER_R_T", UIKitConfig.KEY_LIVE_PLAYING_TYPE);
            if (!TextUtils.isEmpty(liveType) && !StringUtils.equals(liveType, LivePlayingType.DEFAULT.name())) {
                this.mPresenter.addLiveCornerObserver();
            }
        }
    }

    private void updateLBCorner(ItemInfoModel itemModel) {
        CuteImage cornerBgLeftView = getCornerBgLeftView();
        if (cornerBgLeftView != null) {
            cornerBgLeftView.setDrawable(CloudUtilsGala.getDrawable(itemModel.getCuteViewData("ID_CORNER_BG_LEFT", "value")));
            if (cornerBgLeftView.getDrawable() != null) {
                cornerBgLeftView.setWidth(ResourceUtil.getPx(TypeUtils.castToInt(itemModel.getCuteViewData("ID_CORNER_BG_LEFT", "w"))));
                boolean titleinType = isTitleinType();
                CuteImage cornerLB1View = getCornerLB1View();
                CuteImage cornerLB2View = getCornerLB2View();
                if (titleinType) {
                    CuteImage playingGif = getPlayingGif();
                    if (playingGif != null) {
                        if (playingGif.getDrawable() == null) {
                            cornerBgLeftView.setDrawable(null);
                        } else if (TextUtils.isEmpty(this.mItemModel.getCuteViewData("ID_TITLE", "text"))) {
                            cornerBgLeftView.setMarginBottom(0);
                            playingGif.setMarginBottom(ResourceUtil.getPx(5));
                        }
                    }
                    if (cornerLB1View != null) {
                        cornerLB1View.setVisible(0);
                    }
                    if (cornerLB2View != null) {
                        cornerLB2View.setVisible(0);
                        return;
                    }
                    return;
                }
                String lb1Visible = itemModel.getCuteViewData("ID_CORNER_L_B_1", "visible");
                if (!(lb1Visible == null || cornerLB1View == null)) {
                    cornerLB1View.setVisible(TypeUtils.castToInt(lb1Visible));
                }
                String lb2Visible = itemModel.getCuteViewData("ID_CORNER_L_B_2", "visible");
                if (lb2Visible != null && cornerLB2View != null) {
                    cornerLB2View.setVisible(TypeUtils.castToInt(lb2Visible));
                }
            }
        }
    }

    private void updatePlayingGif(ItemInfoModel itemModel) {
        CuteImage playingGif = getPlayingGif();
        if (playingGif != null) {
            playingGif.setDrawable(ResourceUtil.getDrawableFromResidStr(itemModel.getCuteViewData(UIKitConfig.ID_PLAYING_GIF, "value")));
            Drawable gifDrawable = playingGif.getDrawable();
            if (gifDrawable instanceof AnimationDrawable) {
                AnimationDrawable drawable = (AnimationDrawable) gifDrawable;
                if (drawable != null) {
                    drawable.start();
                }
            }
        }
    }

    public void updatePlayingGifUI() {
        updatePlayingGif(this.mItemModel);
        updateLBCorner(this.mItemModel);
    }

    public void setDefaultImage() {
        CuteImage coreImageView = getCoreImageView();
        if (coreImageView != null) {
            coreImageView.setDrawable(coreImageView.getDefaultDrawable());
        }
    }

    private void invalidateViewStatus(boolean gainFocus) {
        if (canTitleChangedToTwoLines()) {
            CuteText titleView = getTitleView();
            if (TextUtils.isEmpty(titleView.getText())) {
                setTag(CardFocusHelper.ItemDeltaHigh, Integer.valueOf(px88));
                return;
            }
            if (gainFocus) {
                titleView.setLines(2);
            } else {
                titleView.setLines(1);
            }
            if (titleView.getRealLineCount() == 2) {
                setTag(CardFocusHelper.ItemDeltaHigh, Integer.valueOf(0));
                titleView.setHeight(px88);
                titleView.setBgHeight(px88);
                titleView.setMarginBottom(0);
                titleView.setBgMarginBottom(0);
                return;
            }
            setTag(CardFocusHelper.ItemDeltaHigh, Integer.valueOf(px34));
            titleView.setHeight(px54);
            titleView.setBgHeight(px54);
            titleView.setMarginBottom(px34);
            titleView.setBgMarginBottom(px34);
        }
    }

    protected boolean canTitleChangedToTwoLines() {
        return isTitleoutType();
    }

    private boolean isTitleinType() {
        CuteText titleView = getTitleView();
        return titleView != null && titleView.getTitleType() == 2;
    }

    private boolean isNoTitleType() {
        return getTitleView() == null && !isCircleNoTitleType();
    }

    public boolean isTitleoutType() {
        CuteText titleView = getTitleView();
        if (titleView == null || titleView.getTitleType() != 1) {
            return false;
        }
        return true;
    }

    public boolean isCircleTitleType() {
        CuteImage coreImageView = getCoreImageView();
        if (getTitleView() == null || coreImageView == null || coreImageView.getClipType() != 1) {
            return false;
        }
        return true;
    }

    public boolean isCircleNoTitleType() {
        CuteImage coreImageView = getCoreImageView();
        if (getTitleView() == null && coreImageView != null && coreImageView.getClipType() == 1) {
            return true;
        }
        return false;
    }

    public void showLiveCorner(String status) {
        CuteImage cornerRTView = getCornerRTView();
        if (cornerRTView != null && this.mItemModel != null && !TextUtils.isEmpty(status)) {
            cornerRTView.setDrawable(CloudUtilsGala.getDrawableFromResidStr(this.mItemModel.getCuteViewData("ID_CORNER_R_T", status)));
        }
    }

    private boolean isSkewType() {
        CuteImage coreImageView = getCoreImageView();
        return coreImageView != null && coreImageView.getClipType() == 2;
    }

    private CuteImage getCornerRTView() {
        return getCuteImage("ID_CORNER_R_T");
    }

    private CuteImage getCornerBgLeftView() {
        return getCuteImage("ID_CORNER_BG_LEFT");
    }

    private CuteImage getCornerLB1View() {
        return getCuteImage("ID_CORNER_L_B_1");
    }

    private CuteImage getCornerLB2View() {
        return getCuteImage("ID_CORNER_L_B_2");
    }

    private CuteImage getCoreImageView() {
        return getCuteImage("ID_IMAGE");
    }

    private CuteText getTitleView() {
        return getCuteText("ID_TITLE");
    }

    private CuteImage getPlayingGif() {
        return getCuteImage(UIKitConfig.ID_PLAYING_GIF);
    }

    private void initView() {
        CuteText titleView = getTitleView();
        if (titleView != null) {
            String title = this.mItemModel.getCuteViewData("ID_TITLE", "text");
            titleView.setText(title);
            setContentDescription(title);
        }
    }
}
