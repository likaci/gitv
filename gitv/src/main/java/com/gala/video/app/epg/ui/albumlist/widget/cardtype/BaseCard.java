package com.gala.video.app.epg.ui.albumlist.widget.cardtype;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.SearchCard;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.component.item.corner.HomeCornerProvider;
import com.gala.video.app.epg.ui.albumlist.widget.CardView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class BaseCard implements ICard {
    protected CardView mCardView;

    public BaseCard(CardView cardView) {
        this.mCardView = cardView;
    }

    public void setTextData(IData info) {
        if (info != null) {
            Album album = info.getAlbum();
            if (album != null) {
                SearchCard getcard = album.getcard();
                if (getcard != null) {
                    List cardInfos = getcard.getCardInfo();
                    if (ListUtils.isLegal(cardInfos, 0)) {
                        setTitleAndDivideLine((String) cardInfos.get(0));
                        setDescViewText(getDescStrings(cardInfos), 0);
                    }
                }
            }
        }
    }

    protected void setTitleAndDivideLine(String title) {
        getTitleView().setText(title);
        this.mCardView.setContentDescription(title);
        getDivideLineView().setDrawable(this.mCardView.getDIVIDE_LINE_DRAWABLE());
    }

    protected List<String> getDescStrings(List<String> cardInfos) {
        int viewSize = ListUtils.getCount(this.mCardView.getDescViews());
        int infoSize = ListUtils.getCount((List) cardInfos);
        List<String> descStrings = new ArrayList(viewSize);
        int i = 1;
        while (i < infoSize && i < viewSize + 1) {
            descStrings.add(cardInfos.get(i));
            i++;
        }
        return descStrings;
    }

    protected void setDescViewText(List<String> descStrings, int viewStartIndex) {
        if (!ListUtils.isEmpty((List) descStrings) && viewStartIndex >= 0) {
            List descViews = this.mCardView.getDescViews();
            int viewSize = ListUtils.getCount(descViews);
            int infoSize = ListUtils.getCount((List) descStrings);
            int viewIndex = viewStartIndex;
            for (int i = 0; i < infoSize && viewIndex < viewSize; i++) {
                String info = (String) descStrings.get(i);
                if (!TextUtils.isEmpty(info)) {
                    ((CuteTextView) descViews.get(viewIndex)).setText(info);
                    viewIndex++;
                }
            }
        }
    }

    public void setImageData(IData info) {
    }

    public void releaseData() {
        clearLiveCorner();
        getTitleView().setText(null);
        getScoreView().setText(null);
        getPopupGreenView().setText(null);
        getOfflineCountDown1().setText(null);
        getOfflineCountDown2().setText(null);
        getOfflineCountDown3().setText(null);
        getXinView().setBitmap(null);
        getSignRBView().setBitmap(null);
        getLine1BgView().setBitmap(null);
        getCornerLTView().setBitmap(null);
        getCornerRTView().setBitmap(null);
        getCornerLB1View().setBitmap(null);
        getCornerLB2View().setBitmap(null);
        getDivideLineView().setBitmap(null);
        getCornerLBBgView().setBitmap(null);
        getTitleView().setMarginBottom(this.mCardView.getTITLE_MARGIN_BOTTOM_LINE_2());
        resetDescViews();
    }

    private void resetDescViews() {
        if (this.mCardView != null) {
            List descViews = this.mCardView.getDescViews();
            getRightDesc1().setText(null);
            getRightDesc2().setText(null);
            getRightDesc3().setText(null);
            getRightDesc4().setText(null);
            if (ListUtils.getCount(descViews) != 4) {
                descViews.clear();
                descViews.add(getRightDesc1());
                descViews.add(getRightDesc2());
                descViews.add(getRightDesc3());
                descViews.add(getRightDesc4());
            }
        }
    }

    protected void clearLiveCorner() {
        if (this.mCardView != null) {
            this.mCardView.clearLiveCorner();
        }
    }

    protected Drawable getDrawable(int resId) {
        return ResourceUtil.getDrawable(resId);
    }

    protected int getDimenSize(int dimen) {
        return ResourceUtil.getDimensionPixelSize(dimen);
    }

    protected CuteImageView getImageView() {
        return this.mCardView.getImageView();
    }

    protected CuteImageView getCornerRTView() {
        return this.mCardView.getCornerRTView();
    }

    protected CuteImageView getCornerLTView() {
        return this.mCardView.getCornerLTView();
    }

    protected CuteImageView getCornerLB1View() {
        return this.mCardView.getCornerLB1View();
    }

    protected CuteImageView getCornerLB2View() {
        return this.mCardView.getCornerLB2View();
    }

    protected CuteImageView getCornerLBBgView() {
        return this.mCardView.getCornerLBBgView();
    }

    protected CuteImageView getXinView() {
        return this.mCardView.getXinView();
    }

    protected CuteImageView getSignRBView() {
        return this.mCardView.getSignRBView();
    }

    protected CuteTextView getPopupGreenView() {
        return this.mCardView.getPopupGreenView();
    }

    protected CuteTextView getScoreView() {
        return this.mCardView.getScoreView();
    }

    protected CuteImageView getLine1BgView() {
        return this.mCardView.getLine1BgView();
    }

    protected CuteTextView getTitleView() {
        return this.mCardView.getTitleView();
    }

    protected CuteImageView getDivideLineView() {
        return this.mCardView.getDivideLineView();
    }

    protected CuteTextView getOfflineCountDown1() {
        return this.mCardView.getOfflineCountDown1();
    }

    protected CuteTextView getOfflineCountDown2() {
        return this.mCardView.getOfflineCountDown2();
    }

    protected CuteTextView getOfflineCountDown3() {
        return this.mCardView.getOfflineCountDown3();
    }

    protected CuteTextView getRightDesc1() {
        return this.mCardView.getRightDesc1();
    }

    protected CuteTextView getRightDesc2() {
        return this.mCardView.getRightDesc2();
    }

    protected CuteTextView getRightDesc3() {
        return this.mCardView.getRightDesc3();
    }

    protected CuteTextView getRightDesc4() {
        return this.mCardView.getRightDesc4();
    }

    protected final void setTopCorner(IData info) {
        if (info != null) {
            setLeftTopCorner(info);
            setRightTopCorner(info);
        }
    }

    private void setLeftTopCorner(IData info) {
        if (info != null) {
            boolean isVip = info.getCornerStatus(0);
            boolean isSingleBuy = info.getCornerStatus(1);
            if (info.getCornerStatus(7)) {
                setCornerCouponsVisible();
            } else if (isSingleBuy) {
                setCornerBuyVisible();
            } else if (isVip) {
                setCornerVIPVisible();
            }
        }
    }

    protected void setRightTopCorner(IData info) {
        if (info != null) {
            setDujiaAndDuboCorner(info.getCornerStatus(3), info.getCornerStatus(2));
        }
    }

    private void setDujiaAndDuboCorner(boolean isDujia, boolean isDubo) {
        if (isDujia) {
            setCornerDujiaVisible();
        } else if (isDubo) {
            setCornerDuboVisible();
        }
    }

    protected void setScore(Album album) {
        float score = HomeCornerProvider.getScore(album);
        CuteTextView scoreView = getScoreView();
        if (score <= 0.0f || score > 10.0f) {
            scoreView.setVisible(8);
            return;
        }
        scoreView.setVisible(0);
        scoreView.setBgDrawable(ImageCacheUtil.CORNER_BG_RIGHT);
        scoreView.setText(String.valueOf(score));
    }

    protected void setCountDownDay(long days) {
        if (days > 0) {
            CuteTextView offlineCountDown1View = getOfflineCountDown1();
            CuteTextView offlineCountDown2View = getOfflineCountDown2();
            CuteTextView offlineCountDown3View = getOfflineCountDown3();
            if (days < 10) {
                offlineCountDown1View.setMarginLeft(getDimenSize(R.dimen.dimen_25dp));
                offlineCountDown2View.setMarginLeft(getDimenSize(R.dimen.dimen_116dp));
                offlineCountDown3View.setMarginLeft(getDimenSize(R.dimen.dimen_127dp));
            } else if (days < 100) {
                offlineCountDown1View.setMarginLeft(getDimenSize(R.dimen.dimen_20dp));
                offlineCountDown2View.setMarginLeft(getDimenSize(R.dimen.dimen_111dp));
                offlineCountDown3View.setMarginLeft(getDimenSize(R.dimen.dimen_133dp));
            } else if (days < 1000) {
                offlineCountDown1View.setMarginLeft(getDimenSize(R.dimen.dimen_15dp));
                offlineCountDown2View.setMarginLeft(getDimenSize(R.dimen.dimen_106dp));
                offlineCountDown3View.setMarginLeft(getDimenSize(R.dimen.dimen_137dp));
            } else if (days < 10000) {
                offlineCountDown1View.setMarginLeft(getDimenSize(R.dimen.dimen_10dp));
                offlineCountDown2View.setMarginLeft(getDimenSize(R.dimen.dimen_101dp));
                offlineCountDown3View.setMarginLeft(getDimenSize(R.dimen.dimen_141dp));
            } else {
                offlineCountDown1View.setMarginLeft(getDimenSize(R.dimen.dimen_5dp));
                offlineCountDown2View.setMarginLeft(getDimenSize(R.dimen.dimen_96dp));
                offlineCountDown3View.setMarginLeft(getDimenSize(R.dimen.dimen_145dp));
            }
            offlineCountDown1View.setText("距上映还有");
            offlineCountDown2View.setText(days >= 100000 ? "9999+" : days + "");
            offlineCountDown3View.setText("天");
            getLine1BgView().setDrawable(this.mCardView.getLINE_1_BG_DRAWABLE());
        }
    }

    protected void setBottomCorner(IData info) {
        boolean isDolby = info.getCornerStatus(4);
        boolean is3d = info.getCornerStatus(5);
        if (isDolby && is3d) {
            setCornerDolby3DVisible();
        } else if (isDolby && !is3d) {
            setCornerDolbyVisible();
        } else if (!isDolby && is3d) {
            setCorner3DVisible();
        }
    }

    protected void setCornerDujiaVisible() {
        getCornerRTView().setDrawable(this.mCardView.getDU_JIA_DRAWABLE());
    }

    protected void setCornerDuboVisible() {
        getCornerRTView().setDrawable(ImageCacheUtil.CORNER_DUBO_DRAWABLE);
    }

    protected void setCornerVIPVisible() {
        getCornerLTView().setDrawable(ImageCacheUtil.CORNER_VIP_DRAWABLE);
    }

    protected void setCornerBuyVisible() {
        getCornerLTView().setDrawable(ImageCacheUtil.CORNER_FUFEIDIANBO_DRAWABLE);
    }

    protected void setCornerCouponsVisible() {
        getCornerLTView().setDrawable(ImageCacheUtil.CORNER_DIANBOQUAN_DRAWABLE);
    }

    protected void setCornerDolby3DVisible() {
        getCornerLB1View().setDrawable(ImageCacheUtil.CORNER_DOLBY_DRAWABLE);
        getCornerLB2View().setDrawable(ImageCacheUtil.CORNER_3D_DRAWABLE);
        getCornerLBBgView().setDrawable(ImageCacheUtil.CORNER_BG_LEFT);
        getCornerLBBgView().setWidth(getDimenSize(R.dimen.dimen_95dp));
    }

    protected void setCornerDolbyVisible() {
        getCornerLB1View().setDrawable(ImageCacheUtil.CORNER_DOLBY_DRAWABLE);
        getCornerLB2View().setDrawable(null);
        getCornerLBBgView().setDrawable(ImageCacheUtil.CORNER_BG_LEFT);
        getCornerLBBgView().setWidth(getDimenSize(R.dimen.dimen_66dp));
    }

    protected void setCorner3DVisible() {
        getCornerLB1View().setDrawable(ImageCacheUtil.CORNER_3D_DRAWABLE);
        getCornerLB2View().setDrawable(null);
        getCornerLBBgView().setDrawable(ImageCacheUtil.CORNER_BG_LEFT);
        getCornerLBBgView().setWidth(getDimenSize(R.dimen.dimen_46dp));
    }
}
