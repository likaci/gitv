package com.gala.video.app.epg.home.component.item.corner;

import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import com.gala.tvapi.type.LivePlayingType;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.component.item.widget.ComplexItemCloudView;
import com.gala.video.app.epg.home.data.ItemData;
import com.gala.video.app.epg.utils.EpgImageCache;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory.LiveCornerListener;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class ItemCorner {
    protected CuteImageView mCornerLB1Image;
    protected CuteImageView mCornerLB2Image;
    protected CuteImageView mCornerLBBg;
    protected CuteImageView mCornerLTImage;
    protected CuteTextView mCornerRBDesL1;
    protected CuteImageView mCornerRTImage;
    protected CuteImageView mCornerRankImage;
    protected CuteTextView mCornerScoreText;
    protected CuteTextView mCornerToBeOnlineText;
    private boolean mDisableGifAnim = Project.getInstance().getControl().disableGifAnimForDetailPage();
    protected boolean mIs3DShowing;
    protected boolean mIsDolbyShowing;
    protected boolean mIsRankShowing;
    protected boolean mIsScoreShowing;
    protected boolean mIsToBeOnlineShowing;
    protected ItemData mItemData;
    private ILiveCornerFactory mLiveCornerFactory;
    private final LiveCornerListener mLiveCornerListener = new C06011();
    private CuteImageView mPlayingGifView;
    protected ComplexItemCloudView mView;

    class C06011 implements LiveCornerListener {
        C06011() {
        }

        public void showBefore() {
            ItemCorner.this.setCornerBeforeLiveVisible();
        }

        public void showPlaying() {
            ItemCorner.this.setCornerLivingVisible();
        }

        public void showEnd() {
            ItemCorner.this.setCornerEndLiveVisible();
        }
    }

    ItemCorner(ComplexItemCloudView view) {
        this.mView = view;
    }

    public void updateItemData(ItemData itemData) {
        this.mItemData = itemData;
    }

    public void destory() {
        if (this.mLiveCornerFactory != null) {
            this.mLiveCornerFactory.end();
        }
    }

    public void setCorner() {
        if (this.mItemData != null && this.mItemData.mLabel != null) {
            resetCornerState();
            if (this.mItemData.isCoupons) {
                setCornerCouponsVisible();
            } else if (this.mItemData.isCharge) {
                setCornerChargeVisible();
            } else if (this.mItemData.isVip) {
                setCornerVIPVisible();
            }
            if (this.mLiveCornerFactory != null) {
                this.mLiveCornerFactory.end();
            }
            if (!LivePlayingType.DEFAULT.equals(this.mItemData.mLabel.getLivePlayingType())) {
                if (this.mLiveCornerFactory == null) {
                    this.mLiveCornerFactory = CreateInterfaceTools.createLiveCornerFactory();
                }
                this.mLiveCornerFactory.start(this.mItemData.mLabel, this.mLiveCornerListener);
            } else if (this.mItemData.isDuJia) {
                setCornerDujiaVisible();
            } else if (this.mItemData.isDuBo) {
                setCornerDuboVisible();
            } else if (this.mItemData.isSubject) {
                setCornerSubjectVisible();
            }
            if (this.mItemData.isRanked) {
                setCornerRankVisible();
            } else if (this.mItemData.isToBeOnline) {
                setCornerToBeOnlineVisible();
            } else if (this.mItemData.isPlaying) {
                setPlaying(this.mItemData.isPlaying);
            } else if (this.mItemData.isDolby && this.mItemData.is3D) {
                setCornerDolbyAnd3DVisible();
            } else if (this.mItemData.isDolby && !this.mItemData.is3D) {
                setCornerDolbyVisible();
            } else if (!this.mItemData.isDolby && this.mItemData.is3D) {
                setCorner3DVisible();
            }
            if (this.mItemData.isShowScore && !this.mIsToBeOnlineShowing && !this.mIsRankShowing) {
                setCornerScoreVisible();
            } else if (!(!this.mItemData.isShowRBDes1 || this.mIsToBeOnlineShowing || this.mIsRankShowing)) {
                setCornerRBDesL1Visible();
            }
            setCornerBg();
        }
    }

    void setCornerRBDesL1Visible() {
        getCornerRBDesL1View();
        if (this.mCornerRBDesL1 != null && !TextUtils.isEmpty(this.mItemData.desL1RBString)) {
            this.mCornerRBDesL1.setText(this.mItemData.desL1RBString);
            this.mCornerRBDesL1.setBgDrawable(ImageCacheUtil.CORNER_BG_RIGHT);
            adjustCornerRBDesL1Place();
        }
    }

    private void resetCornerState() {
        this.mIsRankShowing = false;
        this.mIsToBeOnlineShowing = false;
        this.mIsDolbyShowing = false;
        this.mIs3DShowing = false;
        this.mIsScoreShowing = false;
        if (this.mCornerLTImage != null) {
            this.mCornerLTImage.setDrawable(null);
        }
        if (this.mCornerRTImage != null) {
            this.mCornerRTImage.setDrawable(null);
        }
        if (this.mCornerLB1Image != null) {
            this.mCornerLB1Image.setDrawable(null);
        }
        if (this.mPlayingGifView != null) {
            this.mPlayingGifView.setDrawable(null);
        }
        if (this.mCornerLB2Image != null) {
            this.mCornerLB2Image.setDrawable(null);
        }
        if (this.mCornerLBBg != null) {
            this.mCornerLBBg.setDrawable(null);
        }
        if (this.mCornerToBeOnlineText != null) {
            this.mCornerToBeOnlineText.setText(null);
        }
        if (this.mCornerRankImage != null) {
            this.mCornerRankImage.setDrawable(null);
        }
        if (this.mCornerScoreText != null) {
            this.mCornerScoreText.setText(null);
        }
        if (this.mCornerRBDesL1 != null) {
            this.mCornerRBDesL1.setText(null);
        }
    }

    CuteTextView getToBeOnLineView() {
        if (this.mCornerToBeOnlineText == null && this.mView != null) {
            this.mCornerToBeOnlineText = this.mView.getToBeOnLineView();
        }
        return this.mCornerToBeOnlineText;
    }

    CuteImageView getCornerRankView() {
        if (this.mCornerRankImage == null && this.mView != null) {
            this.mCornerRankImage = this.mView.getRankView();
        }
        return this.mCornerRankImage;
    }

    CuteImageView getCornerLTView() {
        if (this.mCornerLTImage == null && this.mView != null) {
            this.mCornerLTImage = this.mView.getCornerLTView();
        }
        return this.mCornerLTImage;
    }

    CuteImageView getCornerRLView() {
        if (this.mCornerRTImage == null && this.mView != null) {
            this.mCornerRTImage = this.mView.getCornerRTView();
        }
        return this.mCornerRTImage;
    }

    CuteImageView getCornerLB2View() {
        if (this.mCornerLB2Image == null && this.mView != null) {
            this.mCornerLB2Image = this.mView.getCornerLB2View();
        }
        return this.mCornerLB2Image;
    }

    CuteImageView getCornerLB1View() {
        if (this.mCornerLB1Image == null && this.mView != null) {
            this.mCornerLB1Image = this.mView.getCornerLB1View();
        }
        return this.mCornerLB1Image;
    }

    CuteTextView getCornerScoreView() {
        if (this.mCornerScoreText == null && this.mView != null) {
            this.mCornerScoreText = this.mView.getScoreView();
        }
        return this.mCornerScoreText;
    }

    CuteImageView getCornerLBBgView() {
        if (this.mCornerLBBg == null && this.mView != null) {
            this.mCornerLBBg = this.mView.getCornerBgLeftView();
        }
        return this.mCornerLBBg;
    }

    CuteImageView getPlayingGifView() {
        if (this.mPlayingGifView == null && this.mView != null) {
            this.mPlayingGifView = this.mView.getPlayingGif();
        }
        return this.mPlayingGifView;
    }

    CuteTextView getCornerRBDesL1View() {
        if (this.mCornerRBDesL1 == null && this.mView != null) {
            this.mCornerRBDesL1 = this.mView.getRBDescView();
        }
        return this.mCornerRBDesL1;
    }

    void setCornerBg() {
        if (!this.mIsToBeOnlineShowing && !this.mIsRankShowing) {
            getCornerLBBgView();
            if (this.mItemData.isPlaying) {
                setCornerDolbyAnd3DGone();
            } else if (this.mCornerLBBg != null) {
                if (this.mIsDolbyShowing || this.mIs3DShowing) {
                    this.mCornerLBBg.setDrawable(ImageCacheUtil.CORNER_BG_LEFT);
                }
                if (this.mIsDolbyShowing && this.mIs3DShowing) {
                    this.mCornerLBBg.setWidth(ResourceUtil.getDimen(C0508R.dimen.dimen_105dp));
                } else if (this.mIsDolbyShowing && !this.mIs3DShowing) {
                    this.mCornerLBBg.setWidth(ResourceUtil.getDimen(C0508R.dimen.dimen_70dp));
                } else if (!this.mIsDolbyShowing && this.mIs3DShowing) {
                    this.mCornerLBBg.setWidth(ResourceUtil.getDimen(C0508R.dimen.dimen_50dp));
                }
                adjustCornerLBBgPlace();
            }
        }
    }

    private void setPlaying(boolean isPlaying) {
        getPlayingGifView();
        getCornerLBBgView();
        if (isPlaying) {
            if (this.mDisableGifAnim) {
                this.mPlayingGifView.setResourceId(C0508R.drawable.share_detail_gif_playing_6);
            } else {
                this.mPlayingGifView.setResourceId(C0508R.drawable.share_detail_gif_playing);
                AnimationDrawable drawable = (AnimationDrawable) this.mPlayingGifView.getDrawable();
                if (drawable != null) {
                    drawable.start();
                }
            }
            this.mCornerLBBg.setVisible(0);
            this.mCornerLBBg.setDrawable(ImageCacheUtil.CORNER_BG_LEFT);
            this.mCornerLBBg.setWidth(ResourceUtil.getDimen(C0508R.dimen.dimen_48dp));
            adjustGifPlace();
            return;
        }
        this.mPlayingGifView.setResourceId(0);
        this.mCornerLBBg.setVisible(8);
    }

    void setCornerScoreVisible() {
        getCornerScoreView();
        if (this.mCornerScoreText != null && !TextUtils.isEmpty(this.mItemData.score)) {
            this.mCornerScoreText.setText(this.mItemData.score);
            this.mCornerScoreText.setBgDrawable(ImageCacheUtil.CORNER_BG_RIGHT);
            this.mIsScoreShowing = true;
            adjustCornerScorePlace();
        }
    }

    void setCorner3DVisible() {
        getCornerLB1View();
        if (this.mCornerLB1Image != null) {
            this.mCornerLB1Image.setDrawable(ImageCacheUtil.CORNER_3D_DRAWABLE);
            this.mIsDolbyShowing = false;
            this.mIs3DShowing = true;
            adjustCorner3DPlace();
        }
    }

    void setCornerDolbyVisible() {
        getCornerLB1View();
        if (this.mCornerLB1Image != null) {
            this.mCornerLB1Image.setDrawable(ImageCacheUtil.CORNER_DOLBY_DRAWABLE);
            this.mIsDolbyShowing = true;
            this.mIs3DShowing = false;
            adjustCornerDolbyPlace();
        }
    }

    void setCornerDolbyAnd3DVisible() {
        getCornerLB1View();
        getCornerLB2View();
        if (this.mCornerLB1Image != null && this.mCornerLB2Image != null) {
            this.mCornerLB1Image.setDrawable(ImageCacheUtil.CORNER_DOLBY_DRAWABLE);
            this.mCornerLB2Image.setDrawable(ImageCacheUtil.CORNER_3D_DRAWABLE);
            this.mIsDolbyShowing = true;
            this.mIs3DShowing = true;
            adjustCornerDolbyAnd3DPlace();
        }
    }

    void setCornerDolbyAnd3DGone() {
        getCornerLB1View();
        getCornerLB2View();
        if (this.mCornerLB1Image != null && this.mCornerLB2Image != null) {
            this.mCornerLB1Image.setDrawable(null);
            this.mCornerLB2Image.setDrawable(null);
            this.mIsDolbyShowing = false;
            this.mIs3DShowing = false;
            if (!this.mItemData.isPlaying) {
                this.mCornerLBBg.setVisible(8);
            }
        }
    }

    void setCornerToBeOnlineVisible() {
        getToBeOnLineView();
        if (this.mCornerToBeOnlineText != null) {
            this.mCornerToBeOnlineText.setText("即将上线");
            this.mCornerToBeOnlineText.setBgDrawable(EpgImageCache.CARD_ITEM_DESC_BG);
            this.mIsToBeOnlineShowing = true;
            adjustCornerToBeOnlinePlace();
        }
    }

    void setCornerRankVisible() {
        getCornerRankView();
        if (this.mCornerRankImage != null && this.mItemData != null) {
            int rank = this.mItemData.rankedNum;
            this.mIsRankShowing = true;
            switch (rank) {
                case 1:
                    this.mCornerRankImage.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_corner_rank_1));
                    break;
                case 2:
                    this.mCornerRankImage.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_corner_rank_2));
                    break;
                case 3:
                    this.mCornerRankImage.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_corner_rank_3));
                    break;
                case 4:
                    this.mCornerRankImage.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_corner_rank_4));
                    break;
                case 5:
                    this.mCornerRankImage.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_corner_rank_5));
                    break;
                case 6:
                    this.mCornerRankImage.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_corner_rank_6));
                    break;
                case 7:
                    this.mCornerRankImage.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_corner_rank_7));
                    break;
                case 8:
                    this.mCornerRankImage.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_corner_rank_8));
                    break;
                case 9:
                    this.mCornerRankImage.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_corner_rank_9));
                    break;
                case 10:
                    this.mCornerRankImage.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_corner_rank_10));
                    break;
            }
            adjustCornerRankViewPlace();
        }
    }

    void setCornerBeforeLiveVisible() {
        getCornerRLView();
        if (this.mCornerRTImage != null) {
            this.mCornerRTImage.setDrawable(ImageCacheUtil.CORNER_NOTICE_DRAWABLE);
            adjustCornerRTViewPlace();
        }
    }

    void setCornerEndLiveVisible() {
        getCornerRLView();
        if (this.mCornerRTImage != null) {
            this.mCornerRTImage.setDrawable(ImageCacheUtil.CORNER_END_LIVING_DRAWABLE);
            adjustCornerRTViewPlace();
        }
    }

    void setCornerSubjectVisible() {
        getCornerRLView();
        if (this.mCornerRTImage != null) {
            this.mCornerRTImage.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_corner_zhuanti));
            adjustCornerRTViewPlace();
        }
    }

    void setCornerDuboVisible() {
        getCornerRLView();
        if (this.mCornerRTImage != null) {
            this.mCornerRTImage.setDrawable(ImageCacheUtil.CORNER_DUBO_DRAWABLE);
            adjustCornerRTViewPlace();
        }
    }

    void setCornerDujiaVisible() {
        getCornerRLView();
        if (this.mCornerRTImage != null) {
            this.mCornerRTImage.setDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_corner_dujia));
            adjustCornerRTViewPlace();
        }
    }

    void setCornerLivingVisible() {
        getCornerRLView();
        if (this.mCornerRTImage != null) {
            this.mCornerRTImage.setDrawable(ImageCacheUtil.CORNER_LIVING_DRAWABLE);
            adjustCornerRTViewPlace();
        }
    }

    void setCornerCouponsVisible() {
        getCornerLTView();
        if (this.mCornerLTImage != null) {
            this.mCornerLTImage.setDrawable(ImageCacheUtil.CORNER_DIANBOQUAN_DRAWABLE);
            adjustCornerLTViewPlace();
        }
    }

    void setCornerChargeVisible() {
        getCornerLTView();
        if (this.mCornerLTImage != null) {
            this.mCornerLTImage.setDrawable(ImageCacheUtil.CORNER_FUFEIDIANBO_DRAWABLE);
            adjustCornerLTViewPlace();
        }
    }

    void setCornerVIPVisible() {
        getCornerLTView();
        if (this.mCornerLTImage != null) {
            this.mCornerLTImage.setDrawable(ImageCacheUtil.CORNER_VIP_DRAWABLE);
            adjustCornerLTViewPlace();
        }
    }

    void adjustCornerLTViewPlace() {
    }

    void adjustCornerRTViewPlace() {
    }

    void adjustCornerRankViewPlace() {
    }

    void adjustCornerToBeOnlinePlace() {
    }

    void adjustCornerDolbyAnd3DPlace() {
    }

    void adjustCornerDolbyPlace() {
    }

    void adjustCorner3DPlace() {
    }

    void adjustCornerScorePlace() {
    }

    void adjustGifPlace() {
    }

    void adjustCornerLBBgPlace() {
    }

    void adjustCornerRBDesL1Place() {
    }
}
